"""Integrate A74-A82 source/content checks. No client or production activation."""
import argparse
from collections import Counter
from fractions import Fraction
import hashlib
import json
import math
from pathlib import Path

from challenge_tier_rules_v2 import tier_for_probability
from review_context_evidence import Capture, constants

ROOT = Path(__file__).resolve().parents[2]
D = ROOT / 'docs/wheelbound'


def validate(source_dir=None, primary_dir=None):
    checks = 0

    def check(condition):
        nonlocal checks
        assert condition
        checks += 1

    def read(path):
        return json.loads((D / path).read_text())

    draft = read('catalogs/challenge_hunts_v2_draft.json')
    index = read('catalogs/challenge_hunt_review_index_v2.json')
    stronghold = read('research/STRONGHOLD_CONTEXTS_A77.json')
    gems = read('research/ELEMENTAL_CONTEXTS_A78.json')
    first = read('research/FULL_CONTEXT_A74.json')
    lesser = read('research/LESSER_CONTEXT_SCREEN_A75.json')
    rate_screen = read('research/RATE_EVIDENCE_SCREEN_A76.json')
    effort = read('research/STARTER_EFFORT_QUOTES_A80.json')
    trace = read('research/RECEIPT_TRACE_PLAN_A82.json')
    check(len(draft['creatures']) == len(draft['hunts']) == 4)
    check(draft['runtime_enabled'] is False and draft['ready_for_new_offers'] is False)
    check(draft['initial_bundle']['historical_v1_grants_preserved'] is True)
    check(draft['initial_bundle']['normal_boss_unlocks_granted'] is False)
    keys = {c['key'] for c in draft['creatures']}
    check(len(keys) == 4 and set(draft['initial_bundle']['members']) == keys)
    check(draft['source_evidence_sha256'] == hashlib.sha256((D / draft['source_evidence_path']).read_bytes()).hexdigest())
    evidence = {e['canonical_key']: e for e in stronghold['entries']}
    check(set(evidence) == keys)
    by_creature = {c['key']: c for c in draft['creatures']}
    by_quote = {q['hunt_id']: q for q in effort['quotes']}
    for hunt in draft['hunts']:
        e = evidence[hunt['creature_key']]
        c = by_creature[hunt['creature_key']]
        check(hunt['runtime_enabled'] is False and c['runtime_enabled'] is False)
        check(hunt['accepted_drop_item_ids'] == [e['canonical_item']['id']] == [hunt['canonical_item_id']])
        check(c['composition_ids'] == [x['id'] for x in e['authored_form_allowlist']])
        check(c['requirements'] == e['authored_context'])
        check(c['hunts'] == [hunt['id']])
        probability = Fraction(hunt['base_rate'])
        check(tier_for_probability(probability) == hunt['rank'] == 'lesser')
        check(Fraction(hunt['personal_probability_source_model']['lower']) == probability)
        check(Fraction(hunt['personal_probability_source_model']['upper']) == probability)
        quote = hunt['reward_quote']
        check(quote == by_quote[hunt['id']])
        hours = 1 / probability / quote['reference_kills_per_hour']
        check(Fraction(quote['reference_mean_hours']) == hours)
        check(quote['fp_reward'] == hunt['fp_reward'] == 25 * math.ceil((100 + 100 * hours) / 25))
        check('AUTHORED_NOT_MEASURED' in quote['throughput_status'])
        for name, target in [('median', Fraction(1, 2)), ('p90', Fraction(9, 10)), ('p95', Fraction(19, 20))]:
            n = quote['reference_quantiles'][name]['kills_under_source_model']
            check(1 - (1 - probability) ** n >= target)
            check(1 - (1 - probability) ** (n - 1) < target)
        for row in quote['throughput_sensitivity']:
            sensitivity_hours = hours / Fraction(row['speed_multiplier'])
            check(Fraction(row['mean_hours']) == sensitivity_hours)
            check(row['recomputed_quote_for_comparison_only'] == 25 * math.ceil((100 + 100 * sensitivity_hours) / 25))
    check(len(gems['entries']) == 5 and gems['canonical_source_keys'] == 4)
    check(gems['conservative_sea_snake_family']['diversity_count'] == 1)
    check(gems['conservative_sea_snake_family']['accepted_source_composition_ids'] == [1097, 1098])
    check(sum(e['rate_status'] == 'ESTIMATED_NO_ITEM_CITATION_HOLD' for e in gems['entries']) == 3)
    check(trace['catalog_sha256'] == hashlib.sha256((D / 'catalogs/challenge_hunts_v2_draft.json').read_bytes()).hexdigest())
    check(trace['client_tests_executed'] == 0 and trace['case_count'] == len(trace['cases']) == 32)
    check(len({c['id'] for c in trace['cases']}) == 32)
    for case in trace['cases']:
        check(case['client_status'] == 'NOT_RUN' and case['observed_trace'] is None and case['runtime_enabled'] is False)
    check(rate_screen['index_sha256'] == hashlib.sha256((D / 'catalogs/challenge_hunt_review_index_v2.json').read_bytes()).hexdigest())
    signature = lambda x: (x['source_url'], x['item'], x['tier'], x['base_rate'])
    check(Counter(map(signature, rate_screen['entries'])) == Counter(map(signature, index['reviewed_hunts'] + index['source_candidates'])))
    check(sum(rate_screen['triage_counts'].values()) == len(rate_screen['entries']) == 483)
    check(len(lesser['groups']) == 27 and len(lesser['sources']) == 18)
    check(Counter((x['source_url'], x['item'], x['base_rate']) for x in lesser['groups']) == Counter((x['source_url'], x['item'], x['base_rate']) for x in index['source_candidates'] if x['tier'] == 'lesser'))
    manifest = read('catalogs/AUTHORITY_MANIFEST.json')
    for item in manifest['catalogs']:
        check(hashlib.sha256((D / item['path']).read_bytes()).hexdigest() == item['sha256'])
        check(item['runtime_enabled'] is False)
    all_evidence = first['entries'] + stronghold['entries'] + gems['entries']
    if source_dir:
        capture = Capture(source_dir)
        for e in all_evidence:
            reproduced = capture.evidence(e['source_url'], e['item'])
            for field in ['normalized_source_sha256', 'raw_file_sha256', 'reported_npc_ids', 'raw_drop_rows', 'html_drop_rows', 'html_identity_rows', 'page_estimate_statements']:
                check(reproduced[field] == e[field])
        # Index rows remain lossless relative to the original serialized catalog.
        for group in lesser['groups']:
            page = capture.pages[group['source_url']]
            for row in group['indexed_source_rows']:
                check(page['drops'][row['source_row_zero_based']] == row['raw_record'])
    if primary_dir:
        primary_dir = Path(primary_dir)
        for source in first['primary_sources']:
            check(hashlib.sha256((primary_dir / Path(source['path']).name).read_bytes()).hexdigest() == source['sha256'])
        items = constants(primary_dir / 'ItemID.java')
        npcs = constants(primary_dir / 'NpcID.java')
        for e in all_evidence:
            check(e['canonical_item'] == {'symbol': e['canonical_item']['symbol'], **items[e['canonical_item']['symbol']]})
            for form in e.get('npc_source_constants', e.get('authored_form_allowlist', e.get('primary_npc_constants', []))):
                check(form == {'symbol': form['symbol'], **npcs[form['symbol']]})
    result = {'version': 'integrated-partial-v2-a83-v1', 'status': 'PASS_OFFLINE_SOURCE_AND_DESIGN_NOT_RUNTIME',
              'assertions': checks, 'starter_keys': 4, 'starter_hunts': 4,
              'draft_fp_quotes': sorted(h['fp_reward'] for h in draft['hunts']),
              'rate_evidence_display_groups': 483, 'lesser_screen_groups': 27,
              'planned_client_cases': 32, 'executed_client_cases': 0,
              'full_capture_reproduction': bool(source_dir), 'pinned_primary_reproduction': bool(primary_dir),
              'runtime_enabled': False, 'full_four_rank_catalog_complete': False}
    (D / 'research/INTEGRATED_VALIDATION_A83.json').write_text(json.dumps(result, indent=2) + '\n')
    print(json.dumps(result))


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--source-dir', type=Path)
    parser.add_argument('--primary-dir', type=Path)
    args = parser.parse_args()
    validate(args.source_dir, args.primary_dir)
