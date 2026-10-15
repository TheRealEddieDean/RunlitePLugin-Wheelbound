"""Synthetic design oracle for WB-D147-150. No RuneLite/runtime implementation."""
from copy import deepcopy
from fractions import Fraction
from itertools import product
from pathlib import Path
import hashlib
import json

ROOT = Path(__file__).resolve().parents[2]
DOCS = ROOT / 'docs/wheelbound'
RANKS = ('lesser', 'standard', 'greater', 'superior')
CHECKS = 0


def check(condition):
    global CHECKS
    assert condition
    CHECKS += 1


def valid_owned(owned):
    return tuple(rank for rank in RANKS if rank in owned) == RANKS[:len(owned)]


def settings(state, requested, boundary):
    if not boundary or not requested <= state['owned'] or not valid_owned(state['owned']):
        return deepcopy(state), False
    result = deepcopy(state)
    result['enabled'] = set(requested)
    return result, True


def purchase(state, rank, operation, ordinary_gate, boundary, quote_known=True, opt_in=False):
    """Pure conceptual transaction; price100/targets below are synthetic fixtures."""
    if operation in state['receipts']:
        return deepcopy(state), 'REPLAY'
    index = RANKS.index(rank)
    allowed = (valid_owned(state['owned']) and rank not in state['owned']
               and (ordinary_gate if index == 0 else RANKS[index - 1] in state['owned'])
               and quote_known and state['fp'] >= 100
               and (boundary if index == 0 or opt_in else True))
    if not allowed:
        return deepcopy(state), 'BLOCKED'
    result = deepcopy(state)
    result['owned'].add(rank)
    result['fp'] -= 100
    if index == 0:
        result['targets'].update(('synthetic_a', 'synthetic_b', 'synthetic_c'))
    if index == 0 or opt_in:
        result['enabled'].add(rank)
    result['receipts'].add(operation)
    return result, 'COMMITTED'


def pool(rows, owned_targets):
    """Require authored canonical keys; raw aliases never create new keys."""
    result = {}
    for row in rows:
        if (row['source'] not in owned_targets or not row['supported'] or
                not row['access'] or not row['quote']):
            continue
        result.setdefault(row['rank'], {}).setdefault(row['source'], set()).add(row['item'])
    return result


def distribution(owned, enabled, pools, q):
    ready = [r for r in RANKS if r in owned & enabled and len(pools.get(r, {})) >= 3]
    if not ready:
        return {}, {}
    by_rank = {r: q / len(ready) for r in ready}
    paths = {}
    for rank in ready:
        creatures = pools[rank]
        for source, items in creatures.items():
            for item in items:
                paths[(rank, source, item)] = by_rank[rank] / len(creatures) / len(items)
    return by_rank, paths


def subsets():
    return [set(r for r, bit in zip(RANKS, bits) if bit) for bits in product((False, True), repeat=4)]


def run():
    global CHECKS
    CHECKS = 0
    schedule_cases = purchase_cases = settings_cases = 0
    rank_rules = json.loads((DOCS / 'catalogs/challenge_rank_contract_v1.json').read_text())
    check(tuple(rank_rules['ranks_in_order']) == RANKS)
    check(rank_rules['minimum_actual_canonical_creatures_per_rank'] == 3)
    check(rank_rules['challenge_family_entries'] == 1)
    check(rank_rules['default_offer_slots'] == 2 and rank_rules['upgraded_offer_slots'] == 3)
    check(rank_rules['rank_prices'] is None and rank_rules['numeric_family_frequency_schedule'] is None)
    check(rank_rules['runtime_enabled'] is False and rank_rules['ready_v2_roster'] is False)
    source_manifest = json.loads((Path(__file__).parent / 'SOURCE_MANIFEST.json').read_text())
    for source in source_manifest['sources']:
        check(hashlib.sha256((ROOT / source['path']).read_bytes()).hexdigest() == source['sha256'])
    q = Fraction(1, 4)  # illustrative family share only, not a production weight
    for owned in subsets():
        for enabled in subsets():
            state = {'owned': owned, 'enabled': set(), 'targets': set(), 'fp': 200, 'receipts': set()}
            for boundary in (False, True):
                updated, ok = settings(state, enabled, boundary)
                settings_cases += 1
                check(ok == (boundary and enabled <= owned and valid_owned(owned)))
                check(updated == state if not ok else updated['enabled'] == enabled)
            if not valid_owned(owned) or not enabled <= owned:
                continue
            for counts in product(range(4), repeat=4):
                pools = {r: {f'c{i}': {'one', 'two'} if i == 0 else {'one'}
                             for i in range(count)} for r, count in zip(RANKS, counts)}
                ranks, paths = distribution(owned, enabled, pools, q)
                schedule_cases += 1
                expected = {r for r, count in zip(RANKS, counts) if r in owned & enabled and count >= 3}
                check(set(ranks) == expected)
                check(sum(ranks.values()) == (q if expected else 0))
                check(sum(paths.values()) == (q if expected else 0))
                for rank in expected:
                    check(ranks[rank] == q / len(expected))
                    check(sum(v for (r, _, _), v in paths.items() if r == rank) == ranks[rank])
                    for source in pools[rank]:
                        check(sum(v for (r, s, _), v in paths.items() if r == rank and s == source)
                              == ranks[rank] / len(pools[rank]))
    for length in range(5):
        owned = set(RANKS[:length])
        for rank, fp, gate, boundary, known, opt_in in product(RANKS, (0, 99, 100, 300),
                                                             (False, True), (False, True),
                                                             (False, True), (False, True)):
            state = {'owned': owned, 'enabled': set(owned), 'targets': {'existing'},
                     'fp': fp, 'receipts': set()}
            before = deepcopy(state)
            result, status = purchase(state, rank, 'op', gate, boundary, known, opt_in)
            purchase_cases += 1
            check(state == before)
            if status == 'BLOCKED':
                check(result == state)
                continue
            check(valid_owned(result['owned']))
            check(result['owned'] == owned | {rank})
            check(result['fp'] == fp - 100)
            check(result['targets'] == ({'existing', 'synthetic_a', 'synthetic_b', 'synthetic_c'}
                                        if rank == 'lesser' else {'existing'}))
            check(result['enabled'] == (state['enabled'] | {rank}
                                        if rank == 'lesser' or opt_in else state['enabled']))
            replayed, replay_status = purchase(result, rank, 'op', False, False, False, False)
            check(replayed == result and replay_status == 'REPLAY')
    # A total of three keys across ranks is not sufficient for either rank.
    ranks, _ = distribution(set(RANKS), set(RANKS),
                            {'lesser': {'a': {'x'}}, 'standard': {'b': {'x'}, 'c': {'x'}}}, q)
    check(not ranks)
    rows = []
    for rank in ('lesser', 'standard'):
        for source in ('a', 'b', 'c'):
            rows.append({'rank': rank, 'source': source, 'item': 'x', 'supported': True,
                         'access': True, 'quote': True, 'npc_alias': 'form1'})
    base = pool(rows, {'a', 'b', 'c'})
    duplicate = deepcopy(rows[0]); duplicate['npc_alias'] = 'form2'
    check(pool(rows + [duplicate, deepcopy(rows[0])], {'a', 'b', 'c'}) == base)
    for missing in ('supported', 'access', 'quote'):
        bad = deepcopy(rows); bad[0][missing] = False
        reduced = pool(bad, {'a', 'b', 'c'})
        check(len(reduced['lesser']) == 2)
        check(set(distribution(set(RANKS), set(RANKS), reduced, q)[0]) == {'standard'})
    check(not pool(rows, set()))
    # Snapshot serialization preserves committed rank and complete pool; changes don't edit it.
    snapshot = {'offered_rank': 'lesser', 'settings_revision': 7, 'catalog': 'synthetic-v1',
                'pool': {'a': ['x'], 'b': ['y'], 'c': ['z']}, 'committed_rng': 'fixture-opaque'}
    encoded = json.dumps(snapshot, sort_keys=True)
    check(json.loads(encoded) == snapshot)
    changed = deepcopy(snapshot); changed['offered_rank'] = 'superior'
    check(json.loads(encoded)['offered_rank'] == 'lesser')
    for slots in (2, 3):
        offers = [deepcopy(snapshot) for _ in range(slots)]
        check(len(offers) == slots)
        check(all(o['offered_rank'] == 'lesser' for o in offers))  # same-rank offers allowed
    model_hash = hashlib.sha256(Path(__file__).read_bytes()).hexdigest()
    manifest = json.loads((DOCS / 'catalogs/AUTHORITY_MANIFEST.json').read_text())
    for record in manifest['catalogs']:
        check(hashlib.sha256((DOCS / record['path']).read_bytes()).hexdigest() == record['sha256'])
        check(record['runtime_enabled'] is False)
    out = {'version': 'challenge-rank-synthetic-a73-v1', 'authority': 'DESIGN_ORACLE_ONLY',
           'decisions': ['WB-D147', 'WB-D148', 'WB-D149', 'WB-D150'],
           'status': 'PASS_SYNTHETIC_ONLY_NOT_RUNTIME', 'assertions': CHECKS,
           'settings_cases': settings_cases, 'purchase_cases': purchase_cases,
           'offering_scenarios': schedule_cases, 'synthetic_family_q': str(q),
           'synthetic_purchase_price': 100, 'real_card_weights_or_prices_adopted': False,
           'model_sha256': model_hash, 'runtime_enabled': False,
           'client_durable_io_receipt_tests': 'NOT_RUN', 'v2_ready_roster': False}
    (Path(__file__).parent / 'RESULTS.json').write_text(json.dumps(out, indent=2) + '\n')
    print(json.dumps(out))


if __name__ == '__main__':
    run()
