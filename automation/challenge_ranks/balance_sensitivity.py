"""Exact candidate-only price/family exposure study; not a production economy."""
import json
from fractions import Fraction
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
D = ROOT / 'docs/wheelbound'
p = json.loads((D / 'catalogs/challenge_hunts_v2_draft.json').read_text())
assert len(p['hunts']) == 4 and all(h['fp_reward'] in (125, 150) for h in p['hunts'])
mean_fp = Fraction(sum(h['fp_reward'] for h in p['hunts']), 4)
prices = []
for label, schedule in [('compact', [150, 300, 600, 1200]),
                        ('middle_trial', [250, 500, 1000, 2000]),
                        ('high', [400, 800, 1600, 3200])]:
    cumulative = 0
    ranks = []
    for rank, price in zip(('lesser', 'standard', 'greater', 'superior'), schedule):
        cumulative += price
        ranks.append({'rank': rank, 'candidate_price': price, 'cumulative_price': cumulative,
                      'reward_equivalent_at_uniform_four_starter_mean': str(Fraction(price) / mean_fp),
                      'cumulative_reward_equivalent': str(Fraction(cumulative) / mean_fp)})
    prices.append({'schedule': label, 'ranks': ranks, 'adopted': False})
exposure = []
checks = 0
for q in (Fraction(1, 10), Fraction(1, 5), Fraction(1, 4), Fraction(1, 3)):
    for slots in (2, 3):
        family_seen = 1 - (1 - q) ** slots
        for count in range(1, 5):
            per_rank = q / count
            rank_seen = 1 - (1 - per_rank) ** slots
            assert per_rank * count == q
            assert 0 < rank_seen <= family_seen <= 1
            checks += 2
            exposure.append({'candidate_q': str(q), 'slots': slots, 'ready_ranks': count,
                             'per_rank_slot_exposure': str(per_rank),
                             'any_challenge_offer_probability': str(family_seen),
                             'a_specific_rank_offer_probability': str(rank_seen),
                             'expected_challenge_slots': str(q * slots),
                             'naive_per_rank_family_weight_probability_NOT_ADOPTED': str(count * q / (1 + (count - 1) * q))})
out = {'version': 'a81-candidate-balance-v1', 'authority': 'SENSITIVITY_RESEARCH_NOT_ADOPTED',
       'runtime_enabled': False, 'prices_adopted': False, 'frequency_adopted': False,
       'reference_uniform_four_starter_fp_mean': str(mean_fp),
       'reference_mean_is_not_preentry_financing_or_accepted_assignment_distribution': True,
       'candidate_prices': prices, 'offering_exposure': exposure,
       'exposure_scenarios': len(exposure), 'exact_assertions': checks}
(Path(__file__).parent / 'BALANCE_SENSITIVITY_A81.json').write_text(json.dumps(out, indent=2) + '\n')
print(json.dumps({'scenarios': len(exposure), 'assertions': checks, 'mean_fp': str(mean_fp),
                  'adopted_prices_or_frequency': False}))
