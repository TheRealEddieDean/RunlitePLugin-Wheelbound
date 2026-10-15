"""Check A68's frozen review against committed A67 evidence; no live admission."""
import hashlib
import json
from fractions import Fraction
from pathlib import Path

from challenge_tier_rules_v2 import tier_for_probability

ROOT = Path(__file__).resolve().parents[2]
DOCS = ROOT / 'docs/wheelbound'


def validate():
    packet = json.loads((DOCS / 'research/LOWER_TIER_CONTEXT_REVIEW_A68.json').read_text())
    input_path = DOCS / packet['input_path']
    source = json.loads(input_path.read_text())
    checks = 0

    def check(condition):
        nonlocal checks
        assert condition
        checks += 1

    check(hashlib.sha256(input_path.read_bytes()).hexdigest() == packet['input_sha256'])
    check(source['upload_sha256'] == packet['original_archive_sha256'])
    check(packet['runtime_enabled'] is False)
    check(len(packet['entries']) == 7)
    check(len({e['context_id'] for e in packet['entries']}) == 7)
    for entry in packet['entries']:
        matches = [g for g in source['source_candidates'] if
                   (g['source_url'], g['item'], g['tier'], g['base_rate']) ==
                   (entry['source_url'], entry['item'], entry['tier'], entry['base_rate'])]
        check(len(matches) == 1)
        check(entry['source_rows'] == matches[0]['source_rows'])
        check(entry['source_context'] == source['monster_source_contexts'][entry['source_url']])
        check(matches[0]['source_kind'] == 'MONSTER')
        check(entry['status'] == 'REVIEW_PACKET_ONLY_NOT_ADMITTED')
        check(entry['runtime_enabled'] is False)
        check(entry['canonical_item_id'] is None and entry['v2_fp'] is None)
        probability = Fraction(entry['base_rate'])
        check(tier_for_probability(probability) == entry['tier'])
        for row in entry['source_rows']:
            check(row['runtime_enabled'] is False)
            check(not row['raw_record']['canonical_item_ids'])
            check(row['source_sha256'] == entry['source_context']['capture']['sha256'])
            check(row['fetched_at'] == entry['source_context']['capture']['fetched_at'])
            raw = row['raw_record']['rarity']['exact_probability']
            check(Fraction(raw['numerator'], raw['denominator']) == probability)
        effort = entry['effort_sensitivity']
        check(Fraction(effort['expected_opportunities']) == 1 / probability)
        for field, target in [('median_opportunities', Fraction(1, 2)),
                              ('p90_opportunities', Fraction(9, 10)),
                              ('p95_opportunities', Fraction(19, 20))]:
            n = effort[field]
            check(1 - (1 - probability) ** n >= target)
            check(1 - (1 - probability) ** (n - 1) < target)
        check(all(effort[k] is None for k in ['kill_rate_per_hour', 'expected_hours', 'fp_quote']))
        check(bool(entry['context_holds']) and bool(entry['access_draft_from_capture']))
    by_id = {e['context_id']: e for e in packet['entries']}
    jelly = by_id['jelly_wilderness_mithril_boots']
    check(all('Wilderness Slayer Cave' in r['raw_record']['section'] for r in jelly['source_rows']))
    check(any('HELD_TASK_DEPENDENT' in h for h in jelly['context_holds']))
    infernal = by_id['infernal_mage_fire_ruby']
    check(any('estimated' in s for s in infernal['source_context']['page_context_first_three_paragraphs']))
    check(any('estimated' in h for h in infernal['context_holds']))
    for key in ['sea_snake_hatchling_water_sapphire', 'sea_snake_young_water_sapphire']:
        check(any('task requirement only' in s for s in by_id[key]['access_draft_from_capture']))
    check(len(packet['receipt_review_cases']) == 10)
    manifest = json.loads((DOCS / 'catalogs/AUTHORITY_MANIFEST.json').read_text())
    for catalog in manifest['catalogs']:
        check(hashlib.sha256((DOCS / catalog['path']).read_bytes()).hexdigest() == catalog['sha256'])
        check(catalog['runtime_enabled'] is False)
    print(json.dumps({'status': 'PASS_COMMITTED_SOURCE_REVIEW_NOT_RUNTIME',
                      'assertions': checks, 'contexts': 7, 'receipt_review_cases': 10,
                      'raw_cache_revalidation': 'NOT_RUN_A68', 'live_client_validation': 'NOT_RUN'}))


if __name__ == '__main__':
    validate()
