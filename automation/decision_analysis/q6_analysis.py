"""Exact hypothetical probability/topology analysis, not a gameplay simulator.

No Q6 option is adopted. No account state, FP economy or eligibility API exists
in this script. Output records conditional arithmetic, never playable accounts.
"""
from fractions import Fraction
from pathlib import Path
import hashlib
import itertools
import json
import sys

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]


def fraction(value):
    return dict(exact=str(value), percent=round(float(value) * 100, 6))


def analyze(spec):
    slices = spec['slices']
    total = sum(Fraction(x['weight']) for x in slices)
    missing = [x for x in slices if x['activity'] in spec['unavailable']]
    retained = [x for x in slices if x['activity'] not in spec['unavailable']]
    remaining = sum(Fraction(x['weight']) for x in retained)
    ordinary = [x for x in retained if x['kind'] == 'ordinary']
    distinct = len({x['activity'] for x in ordinary})
    unavailable_weight = total - remaining
    new_count = max(0, 5 - len(ordinary), 3 - distinct)
    return dict(id=spec['id'], total_slots=len(slices), removed_slots=len(missing),
                unavailable_probability=fraction(unavailable_weight / total),
                renormalization_multiplier=str(total / remaining) if remaining else None,
                retained_ordinary_count=len(ordinary), retained_activity_count=distinct,
                ordinary_new_copy_lower_bound=new_count,
                new_activity_type_lower_bound=max(0, 3 - distinct),
                can_remove_all_unavailable_without_new_slices=len(ordinary) >= 5 and distinct >= 3,
                total_special_before=fraction(sum(Fraction(x['weight']) for x in slices if x['kind'] == 'special') / total),
                total_special_after=fraction(sum(Fraction(x['weight']) for x in retained if x['kind'] == 'special') / remaining) if remaining else None,
                retained_outcomes=[dict(activity=x['activity'], kind=x['kind'],
                                        before=fraction(Fraction(x['weight']) / total),
                                        after=fraction(Fraction(x['weight']) / remaining)) for x in retained])


def slice_(activity, weight='1', kind='ordinary'):
    return dict(activity=activity, weight=weight, kind=kind)


def compositions(total, parts):
    if parts == 1:
        yield (total,)
    else:
        for first in range(1, total - parts + 2):
            for rest in compositions(total - first, parts - 1):
                yield (first,) + rest


def main():
    scenarios = [
        dict(id='initial_five', slices=[slice_(x) for x in ['Mining', 'Fishing', 'Woodcutting', 'Combat', 'Questing']], unavailable=['Questing']),
        dict(id='minimum_three_activities', slices=[slice_('Mining'), slice_('Fishing')] + [slice_('Questing')] * 3, unavailable=['Questing']),
        dict(id='minimum_with_specials', slices=[slice_('Mining'), slice_('Fishing')] + [slice_('Questing')] * 3 + [slice_('Taint', kind='special'), slice_('Sacrifice', kind='special')], unavailable=['Questing']),
        dict(id='full_heavy_quest', slices=[slice_('Mining'), slice_('Fishing')] + [slice_('Questing')] * 20 + [slice_('Taint', kind='special'), slice_('Sacrifice', kind='special')], unavailable=['Questing']),
        dict(id='enhanced_unavailable', slices=[slice_('Mining'), slice_('Fishing'), slice_('Woodcutting'), slice_('Combat'), slice_('Questing', '3')], unavailable=['Questing']),
        dict(id='full_single_unavailable_third_activity', slices=[slice_('Mining')] * 20 + [slice_('Fishing'), slice_('Questing'), slice_('Taint', kind='special'), slice_('Sacrifice', kind='special')], unavailable=['Questing']),
    ]
    rows = [analyze(s) for s in scenarios]
    # Independent exact examples; no Monte Carlo or random sampling.
    assert rows[0]['unavailable_probability']['exact'] == '1/5'
    assert rows[0]['renormalization_multiplier'] == '5/4' and rows[0]['ordinary_new_copy_lower_bound'] == 1
    assert rows[1]['unavailable_probability']['exact'] == '3/5'
    assert rows[1]['renormalization_multiplier'] == '5/2' and rows[1]['ordinary_new_copy_lower_bound'] == 3
    assert rows[2]['total_special_before']['exact'] == '2/7' and rows[2]['total_special_after']['exact'] == '1/2'
    assert rows[3]['total_special_before']['exact'] == '1/12' and rows[3]['total_special_after']['exact'] == '1/2'
    assert rows[3]['renormalization_multiplier'] == '6'
    assert rows[4]['unavailable_probability']['exact'] == '3/7'
    assert rows[5]['new_activity_type_lower_bound'] == rows[5]['ordinary_new_copy_lower_bound'] == 1
    # Enumerate positive count compositions of3/4 activities, the last one
    # unavailable, with0/1/2 occupied special slots. Counts are topologies only.
    groups = []
    for activities, specials in itertools.product((3, 4), (0, 1, 2)):
        count = directly_removable = require_new = max_new = 0
        for n in range(5, 25 - specials):
            for counts in compositions(n, activities):
                retained = sum(counts[:-1])
                distinct = activities - 1
                need = max(0, 5 - retained, 3 - distinct)
                count += 1
                directly_removable += need == 0
                require_new += need > 0
                max_new = max(max_new, need)
                # Arithmetic feasibility only: new types/copies assumed possible.
                assert retained + need >= 5 and distinct + min(need, 3 - distinct if distinct < 3 else 0) >= 3
        groups.append(dict(ordinary_activity_types=activities, special_slots=specials,
                           composition_count=count, directly_removable=directly_removable,
                           requires_new_copies=require_new, maximum_new_copy_lower_bound=max_new))
    assert all(g['directly_removable'] == 0 for g in groups if g['ordinary_activity_types'] == 3)
    source_paths = ['docs/wheelbound/WHEELS.md', 'docs/wheelbound/FATE_SHOP.md', 'docs/wheelbound/OPEN_QUESTIONS.md', 'docs/wheelbound/DECISIONS.md']
    output = dict(status='EXACT_HYPOTHETICAL_ANALYSIS', option_adopted=False, runtime_enabled=False,
                  source_branch_checkpoint='e5d753dc90aa159f16acd6a94f92474200072373',
                  source_sha256={p: hashlib.sha256((ROOT / p).read_bytes()).hexdigest() for p in source_paths},
                  new_balance_trajectories=0, live_trace_count=0,
                  scenario_inputs=scenarios, scenarios=rows, topology_groups=groups,
                  enumerated_topologies=sum(g['composition_count'] for g in groups),
                  limits=['Not account/access/economy simulation', 'Synthetic special weights are examples, not adopted balance',
                          'Lower bounds assume new eligible activities/copies can be acquired',
                          'Paid storage affordability/capacity and destroyed investments are not waived',
                          'Recommended stats are not grounds to disable a permanently unlocked boss'])
    text = json.dumps(output, indent=2) + '\n'
    if '--verify' in sys.argv:
        assert (HERE / 'Q6_RESULTS.json').read_text() == text
    else:
        (HERE / 'Q6_RESULTS.json').write_text(text)
    print('PASS:6 exact scenarios;', output['enumerated_topologies'], 'count topologies; zero gameplay decisions adopted or new account trials')


if __name__ == '__main__':
    main()
