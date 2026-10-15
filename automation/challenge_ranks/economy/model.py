"""Bounded scenario model, not gameplay implementation or factual account forecasts."""
import math
import random
from collections import defaultdict
from fractions import Fraction

RANKS = ('lesser', 'standard', 'greater', 'superior')
PRICE_SCHEDULES = {'compact': (150, 300, 600, 1200), 'middle': (250, 500, 1000, 2000), 'high': (400, 800, 1600, 3200)}


def make_pool(catalogs):
    pool = defaultdict(lambda: defaultdict(list))
    for catalog in catalogs:
        for hunt in catalog['hunts']:
            quote = hunt['reward_quote']
            pool[hunt['rank']][hunt['creature_key']].append({'hunt_id': hunt['id'],
                'p': float(Fraction(hunt['base_rate'])),
                'kph': quote['reference_kills_per_hour'], 'reward': hunt['fp_reward'],
                'mean_hours': float(Fraction(quote.get('reference_mean_hours', quote.get('reference_expected_hours'))))})
    return {r: dict(sources) for r, sources in pool.items()}


def one_account(config, pool, seed):
    rng = random.Random(seed)
    fp = config['starting_fp']
    earned = spent = discretionary = 0
    elapsed = 0.0
    completed = challenge_completed = 0
    enabled = set()
    owned = set()
    bought_targets = set()
    purchase_log = []
    first_rank_completion = {}
    first_ready_completion = {}
    accepted_ranks = defaultdict(int)
    offered_ranks = defaultdict(int)
    observed_duration = []
    active_censored = None
    # Every availability profile is synthetic. No real account eligibility is inferred.
    capacities = config['access_capacities']
    available = {r: sorted(pool[r])[:capacities[i]] for i, r in enumerate(RANKS)}
    rank_prices = PRICE_SCHEDULES[config['price_schedule']]

    def ready_ranks():
        return [r for r in RANKS if r in owned & enabled and
                len([s for s in available[r] if s in bought_targets]) >= 3]

    def buy_progression():
        nonlocal fp, spent
        # Ordinary prerequisite prices150/350 are inherited H4 hypotheses only.
        prereq_cost = config['ordinary_prerequisite_cost']
        if 'prerequisite_bundle' not in bought_targets:
            if fp < prereq_cost:
                return
            fp -= prereq_cost; spent += prereq_cost; bought_targets.add('prerequisite_bundle')
            purchase_log.append((completed, 'ordinary_prerequisites', prereq_cost))
        for i, rank in enumerate(RANKS):
            if rank not in owned:
                if fp < rank_prices[i]:
                    return
                fp -= rank_prices[i]; spent += rank_prices[i]
                owned.add(rank)
                # Strategy explicitly opts in at the legal between-assignment boundary.
                enabled.add(rank)
                first_rank_completion[rank] = completed
                purchase_log.append((completed, rank, rank_prices[i]))
                if rank == 'lesser':
                    bought_targets.update(pool['lesser'])
            # Target expansion is separate from buying the rank; need three actually available keys.
            for source in available[rank][:3]:
                if source in bought_targets:
                    continue
                price = config['target_expansion_price']
                if fp < price:
                    return
                fp -= price; spent += price; bought_targets.add(source)
                purchase_log.append((completed, 'target:' + source, price))
            if config.get('enable_policy','all_owned') == 'lesser_only':
                enabled.intersection_update({'lesser'})
            elif config.get('enable_policy') == 'highest_only':
                enabled.clear(); enabled.add(rank)
            if rank not in first_ready_completion and rank in ready_ranks():
                first_ready_completion[rank] = completed

    def card():
        ranks = ready_ranks()
        if ranks and rng.random() < config['q']:
            rank = rng.choice(ranks)
            sources = [s for s in available[rank] if s in bought_targets]
            source = rng.choice(sources)
            item = rng.choice(pool[rank][source])
            offered_ranks[rank] += 1
            return {'rank': rank, **item}
        return {'rank': 'ordinary', 'reward': config['ordinary_reward'],
                'mean_hours': config['ordinary_hours'] * rng.uniform(0.8, 1.2)}

    while completed < config['completion_spin_window'] and elapsed < config['hours_horizon']:
        buy_progression()
        if rng.random() < config['boss_activity_share']:
            offers = [card() for _ in range(config['slots'])]
            policy = config['choice']
            if policy == 'shortest':
                selected = min(offers, key=lambda x: (x['mean_hours'], -x['reward']))
            elif policy == 'highest_rank':
                selected = max(offers, key=lambda x: (RANKS.index(x['rank']) + 1 if x['rank'] in RANKS else 0, x['reward']))
            elif policy == 'highest_fp':
                selected = max(offers, key=lambda x: (x['reward'], -x['mean_hours']))
            else:
                raise ValueError(policy)
        else:
            selected = {'rank': 'ordinary', 'reward': config['ordinary_reward'], 'mean_hours': config['ordinary_hours']}
        if selected['rank'] in RANKS:
            # Geometric exact discrete opportunities under source model; no pity/cap/repricing.
            kills = math.floor(math.log1p(-rng.random()) / math.log1p(-selected['p'])) + 1
            duration = kills / selected['kph'] / config['throughput_multiplier']
        else:
            duration = selected['mean_hours'] / config['throughput_multiplier']
        if elapsed + duration > config['hours_horizon']:
            active_censored = {'rank': selected['rank'], 'hunt_id': selected.get('hunt_id'), 'frozen_reward': selected['reward']}
            elapsed = config['hours_horizon']
            break
        elapsed += duration
        completed += 1
        fp += selected['reward']; earned += selected['reward']
        if selected['rank'] in RANKS:
            challenge_completed += 1; accepted_ranks[selected['rank']] += 1
            observed_duration.append(duration)
        # Optional spending policy: only affordable discretionary spend, never fabricated debt.
        expense = min(fp, math.floor(selected['reward'] * config['discretionary_fraction']))
        fp -= expense; discretionary += expense
        assert fp >= 0
        assert fp == config['starting_fp'] + earned - spent - discretionary
    if active_censored is None:
        buy_progression()
    assert fp == config['starting_fp'] + earned - spent - discretionary
    assert len(owned) == 0 or owned == set(RANKS[:len(owned)])
    return {'seed': seed, 'fp': fp, 'earned': earned, 'progression_spend': spent,
            'discretionary_spend': discretionary, 'hours': elapsed,
            'completed': completed, 'challenge_completed': challenge_completed,
            'owned_ranks': sorted(owned), 'ready_ranks': ready_ranks(),
            'first_rank_completion': first_rank_completion,
            'first_ready_completion': first_ready_completion,
            'accepted_ranks': dict(accepted_ranks), 'offered_ranks': dict(offered_ranks),
            'active_censored': active_censored, 'purchase_log': purchase_log}
