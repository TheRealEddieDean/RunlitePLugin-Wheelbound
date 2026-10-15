"""Adversarial accounting/replay checks; no live-client claims."""
from copy import deepcopy
import json
from pathlib import Path

from model import RANKS, make_pool, one_account

ROOT = Path(__file__).resolve().parents[3]
D = ROOT / 'docs/wheelbound'
FILES = ['challenge_hunts_v2_draft.json','challenge_standard_expansions_v2_draft.json','challenge_greater_superior_v2_draft.json']
pool = make_pool([json.loads((D/'catalogs'/p).read_text()) for p in FILES])
base = {'starting_fp':0,'ordinary_prerequisite_cost':500,'ordinary_reward':100,'ordinary_hours':0.75,
        'completion_spin_window':100,'hours_horizon':100,'q':0.2,'slots':2,'boss_activity_share':0.25,
        'price_schedule':'middle','access_capacities':[4,4,3,3],'target_expansion_price':150,
        'discretionary_fraction':0.25,'choice':'shortest','throughput_multiplier':1.0,'enable_policy':'all_owned'}
checks = 0

def check(c):
    global checks
    assert c
    checks += 1

before = deepcopy(pool)
for seed in range(64):
    one = one_account(base,pool,seed)
    two = one_account(base,pool,seed)
    check(one == two)
    check(one['fp'] == one['earned'] - one['progression_spend'] - one['discretionary_spend'])
    check(one['completed'] <= 100 and one['hours'] <= 100)
    check(pool == before)
# No Challenge family if all actual source capacities are underfloor.
c = {**base,'q':1.0,'boss_activity_share':1.0,'access_capacities':[2,2,2,2]}
r = one_account(c,pool,7)
check(r['challenge_completed'] == 0 and not r['ready_ranks'])
# Optional spending and source purchase failure cannot create a negative cash balance.
for cost in [50,150,10000]:
    r=one_account({**base,'ordinary_reward':10,'target_expansion_price':cost,'discretionary_fraction':0.9},pool,8)
    check(r['fp'] >= 0)
    check(r['fp'] == r['earned'] - r['progression_spend'] - r['discretionary_spend'])
# A selected assignment past the horizon remains censored and unpaid; no dry/timeout refund.
c={**base,'starting_fp':10000,'q':1.0,'boss_activity_share':1.0,'hours_horizon':1e-9}
r=one_account(c,pool,3)
check(r['completed']==r['earned']==0 and r['active_censored'] is not None)
check(r['fp']==10000-r['progression_spend'])
# Shared canonical targets are bought once even when several rank pools use them.
fake={rank:{source:[{'hunt_id':rank+source,'p':0.04,'kph':120,'reward':125,'mean_hours':25/120}]
            for source in ['a','b','c']} for rank in RANKS}
c={**base,'starting_fp':10000,'completion_spin_window':1,'discretionary_fraction':0}
r=one_account(c,fake,4)
check(r['progression_spend']==4250)
check(not any('target:' in item[1] for item in r['purchase_log']))
# Enabling only Lesser is an explicit between-assignment strategy, not loss of higher ownership.
r=one_account({**base,'starting_fp':10000,'enable_policy':'lesser_only'},pool,5)
check(set(r['owned_ranks'])==set(RANKS))
check(not set(r['accepted_ranks'])-{'lesser'})
print(json.dumps({'status':'PASS_SYNTHETIC_ACCOUNTING_REPLAY_BOUNDARIES','assertions':checks,'runtime_tests':'NOT_RUN'}))
