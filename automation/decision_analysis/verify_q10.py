"""Verify saved hypothetical arithmetic/source pins; not a settlement engine."""
import hashlib
import itertools
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
data = json.loads((Path(__file__).parent / 'Q10_COMPARISON.json').read_text())
assert not data['option_adopted'] and not data['runtime_enabled']
assert data['new_balance_trajectories'] == data['live_trace_count'] == 0
for path, sha in data['source_sha256'].items():
    assert hashlib.sha256((ROOT / path).read_bytes()).hexdigest() == sha
rows = data['policy_comparisons']
policies = ['independent_reward_survives', 'void_unclaimed_only', 'provisional_escrow', 'explicit_claimed_clawback']
expected = set(itertools.product([50, 500, 2000], [100, 300, 1000], policies, [False, True]))
keys = [(r['bounty_input_fp'], r['reject_penalty_input_fp'], r['policy'], r['claim_requested_before_reject']) for r in rows]
assert len(keys) == len(set(keys)) == data['comparison_count'] == 72 and set(keys) == expected
for r in rows:
    reward_kept = r['policy'] == 'independent_reward_survives' or (r['policy'] == 'void_unclaimed_only' and r['claim_requested_before_reject'])
    assert r['net_fp_after_reject_and_attempted_claim'] == (r['bounty_input_fp'] if reward_kept else 0) - r['reject_penalty_input_fp']
    assert r['completion_fp_paid'] == r['additional_spin_cost_beyond_assignment_allocation'] == 0
    assert r['mandatory_punishment_still_required'] and r['raw_acquisition_evidence_retained']
x = data['conditional_spend_example']
assert x['manual_claim_balance'] == x['starting_fp'] + x['active_qualified_bounty']
assert x['after_purchase'] == x['manual_claim_balance'] - x['optional_purchase'] == 0
assert x['proposed_final_fp'] == x['after_purchase'] - x['reject_penalty'] - x['proposed_bounty_clawback'] == -600
print('PASS:72 hypothetical Q10 comparisons; conditional spent-FP example; source pins; no adopted policy')
