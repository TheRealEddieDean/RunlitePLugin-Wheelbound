"""Versioned, seeded sensitivity batches with explicit omitted mechanics."""
from copy import deepcopy
import hashlib
import json
from pathlib import Path
import statistics
import sys

from model import make_pool, one_account

ROOT = Path(__file__).resolve().parents[3]
D = ROOT / 'docs/wheelbound'
HERE = Path(__file__).parent
FILES = ['catalogs/challenge_hunts_v2_draft.json', 'catalogs/challenge_standard_expansions_v2_draft.json', 'catalogs/challenge_greater_superior_v2_draft.json']


def run(accounts=200):
    catalogs = [json.loads((D / p).read_text()) for p in FILES]
    pool = make_pool(catalogs)
    base = {'starting_fp': 0, 'ordinary_prerequisite_cost': 500, 'ordinary_reward': 100,
            'ordinary_hours': 0.75, 'completion_spin_window': 100, 'hours_horizon': 100,
            'q': 0.2, 'slots': 2, 'boss_activity_share': 0.25, 'price_schedule': 'middle',
            'access_capacities': [4, 4, 3, 3], 'target_expansion_price': 150,
            'discretionary_fraction': 0.25, 'enable_policy': 'all_owned', 'choice': 'shortest', 'throughput_multiplier': 1.0}
    # One-factor changes preserve interpretability; selected interaction grid follows separately.
    scenarios = [('baseline', base)]
    for field, values in [('q', [0.1, 1/3]), ('slots', [3]), ('boss_activity_share', [0.1, 0.5]),
                          ('price_schedule', ['compact', 'high']), ('access_capacities', [[4,0,0,0], [4,3,0,0], [2,4,3,3]]),
                          ('target_expansion_price', [50,300]), ('discretionary_fraction', [0,0.5]),
                          ('enable_policy', ['lesser_only','highest_only']), ('choice', ['highest_rank','highest_fp']), ('throughput_multiplier', [0.5,2]),
                          ('ordinary_reward', [60,140]), ('hours_horizon', [50,200])]:
        for value in values:
            c = deepcopy(base); c[field] = value; scenarios.append((field + '=' + str(value), c))
    for choice in ['shortest','highest_rank','highest_fp']:
        for schedule in ['compact','middle','high']:
            for fraction in [0,0.5]:
                c = deepcopy(base);c.update(choice=choice,price_schedule=schedule,discretionary_fraction=fraction)
                scenarios.append(('interaction:'+choice+':'+schedule+':'+str(fraction),c))
    out = []; raw_sample = []
    for index,(label,config) in enumerate(scenarios):
        rows = [one_account(config,pool,2026101500 + k) for k in range(accounts)]
        first = lambda rank: [r['first_ready_completion'][rank] for r in rows if rank in r['first_ready_completion']]
        out.append({'scenario': label, 'config': config, 'accounts': accounts,
                    'mean_completed': statistics.mean(r['completed'] for r in rows),
                    'mean_challenge_completed': statistics.mean(r['challenge_completed'] for r in rows),
                    'mean_final_fp': statistics.mean(r['fp'] for r in rows),
                    'mean_progression_spend': statistics.mean(r['progression_spend'] for r in rows),
                    'mean_discretionary_spend': statistics.mean(r['discretionary_spend'] for r in rows),
                    'active_assignment_censored_fraction': sum(r['active_censored'] is not None for r in rows)/accounts,
                    'owned_fraction': {rank:sum(rank in r['owned_ranks'] for r in rows)/accounts for rank in pool},
                    'ready_fraction': {rank:sum(rank in r['ready_ranks'] for r in rows)/accounts for rank in pool},
                    'mean_first_ready_completion_conditional': {rank:statistics.mean(first(rank)) if first(rank) else None for rank in pool},
                    'accepted_rank_completions': {rank:sum(r['accepted_ranks'].get(rank,0) for r in rows) for rank in pool}})
        if index in [0,15,16]:raw_sample += [{'scenario':label,**r} for r in rows[:3]]
    result = {'version':'rank-economy-a100-visible-v1','status':'SEEDED_SYNTHETIC_SENSITIVITY_NOT_GAME_FORECAST',
              'scenarios':len(scenarios),'accounts_per_scenario':accounts,'accounts_simulated':accounts*len(scenarios),
              'source_inputs':[{'path':p,'sha256':hashlib.sha256((D/p).read_bytes()).hexdigest()} for p in FILES],
              'model_sha256':hashlib.sha256((HERE/'model.py').read_bytes()).hexdigest(),
              'run_sha256':hashlib.sha256((HERE/'run.py').read_bytes()).hexdigest(),
              'not_adopted':['initial100-spin allotment','100-hour horizon','ordinary rewards/hours','card/source prices','numeric family q','activity mix','discretionary spending','throughput assumptions'],
              'omitted':['actual account progression and unlock access','actual activity card generation','Reject/audit failure/violations/Pause','Defy/Taint/Sacrifice/Grand transitions','item bounty income and cash/gear economy','runtime receipt/durable IO'],
              'information_boundary':'visible rank and creature-first public expectations only; selected target resolved afterward from committed per-offer seed',
              'comparison_note':'A87 uses a different random-consumption stream; aggregate scenario comparison is not paired treatment estimation',
              'runtime_enabled':False,'results':out,'raw_examples':raw_sample}
    (HERE/'RESULTS_A100.json').write_text(json.dumps(result,indent=2)+'\n')
    print(json.dumps({'scenarios':len(scenarios),'accounts_simulated':accounts*len(scenarios),'baseline':out[0]}))


if __name__ == '__main__':run(int(sys.argv[1]) if len(sys.argv)>1 else 200)
