"""Compile a future client trace plan; never execute or certify a client adapter."""
from copy import deepcopy
import hashlib
import json
from pathlib import Path

R = Path(__file__).resolve().parents[2]
D = R / 'docs/wheelbound'
CORE = 'catalogs/challenge_core_v2_draft.json'
EARLIER = 'research/RECEIPT_TRACE_PLAN_A82.json'
OUTPUT = 'research/CORE_TRACE_PLAN_A93.json'

# Each row asks for a source-specific observation, not a guessed ready predicate.
SOURCE_CONTROLS = {
    'stronghold-minotaur': 'Verify the authored Stronghold floor1 forms; another Minotaur location/name or an assembled sceptre cannot establish this component receipt.',
    'stronghold-flesh-crawler': 'Verify floor2 encounter and bottom-component loot; another floor/component or a later inventory combination cannot cross-credit.',
    'stronghold-catablepon': 'Verify floor3 encounter/top-component receipt and distinct occurrence after commitment; previous component in bank cannot credit.',
    'stronghold-ankou': 'Verify floor4 context; exclude Wilderness/other Ankou routes and same-name observations without the authored normal members region.',
    'killerwatt': 'Observe passive470 to combat469 transformation and actual loot composition alias; orb interaction alone is not a kill, source receipt or new diversity key.',
    'molanisk': 'Observe exact NPC1 plus supported South Dungeon route/bell initiation and local loot correlation. Bell inventory/use or wall interaction alone is not completion.',
    'infernal-mage': 'Observe each claimed Tower form/loot alias and exact Fire ruby34422; no form accepted solely by shared display name or a combat-level guess.',
    'sea-snake-water-sapphire': 'Hatchling1098 only in the prospective authored context. Young1097/mother1101/boss zone are controls; no enum123/varplayer730/varbit2141 interchange, >=60-only access shortcut or Slayer40 kill gate.',
    'commander-zilyana': 'Normal solo boss2205, supported Saradomin room/entry and local recipient. Bodyguard/teammate loot does not qualify; crossbow11785 and hilt11814 are separate assigned hunts.',
    'kril-tsutsaroth': 'Normal solo boss3129, supported Zamorak room/entry and local recipient. Bodyguard/team allocation cannot qualify; staff11791 and hilt11816 never satisfy one another.',
    'kurask': 'Fremennik off-task route with usable permitted damage method; record actual form/loot mapping. Exclude superior/Iorwerth and unreviewed source variants; no extra source key for an alias.',
    'rune-dragon': 'Post-quest normal Lithkren only. Test quest/POH/other-context same-name exclusions even if a numeric identity overlaps; constants8031/8091 alone do not prove normal personal loot.',
    'spitting-wyvern': 'Normal off-task Fossil Island Spitting source7794; other ordinary wyvern species/Ancient/task-only route do not qualify merely because they can drop the same longsword.',
    'vyrewatch-sentinel': 'Combat Darkmeyer sentinel only. Observe actual form versus reported9756 loot alias; citizen pickpocket and venator blood shards cannot satisfy; no name-prefix identity.',
}

TARGETED = [
    ('water-stage-unknown', ['sea-snake-water-sapphire'], 'Only quest helper entered-dungeon flag is known; tunnel/prop/lift/rope/region evidence absent.', 'NO_NEW_OFFER_FROM_UNKNOWN_ACCESS'),
    ('water-young-held', ['sea-snake-water-sapphire'], 'Water sapphire34410 received from Young1097 after a Hatchling-only assignment.', 'NO_AUTHORED_CONTEXT_CREDIT'),
    ('water-mother-held', ['sea-snake-water-sapphire'], 'Matching item/name or source callback occurs in excluded mother/boss context.', 'NO_AUTHORED_CONTEXT_CREDIT'),
    ('boss-cross-item', ['commander-zilyana', 'kril-tsutsaroth'], 'The other reviewed item from the assigned God Wars boss drops while one specific item is assigned.', 'NO_ASSIGNED_ITEM_COMPLETION'),
    ('boss-bodyguard', ['commander-zilyana', 'kril-tsutsaroth'], 'Matching inventory delta is concurrent with a bodyguard kill or another player kill.', 'NO_CREDIT_WITHOUT_EXACT_PERSONAL_BOSS_RECEIPT'),
    ('boss-group-allocation', ['commander-zilyana', 'kril-tsutsaroth'], 'Local player participates in a group encounter but allocation/solo-context proof is absent.', 'NO_AUTHORED_SOLO_CONTEXT_CREDIT'),
    ('boss-entry-resource', ['commander-zilyana', 'kril-tsutsaroth'], 'Boss owned but repeatable faction-KC/entry/supply route is not legally available.', 'NO_NEW_OFFER_NO_FREE_ROUTE_UNLOCK'),
    ('kurask-alternate', ['kurask'], 'Matching axe received from unselected superior/Iorwerth context.', 'NO_AUTHORED_CONTEXT_CREDIT'),
    ('rune-quest-or-poh', ['rune-dragon'], 'Matching source name/ID occurs during quest or in a POH rather than normal post-quest Lithkren.', 'NO_AUTHORED_CONTEXT_CREDIT'),
    ('wyvern-other-species', ['spitting-wyvern'], 'Same longsword received from another wyvern species or unreviewed method/context.', 'NO_ASSIGNED_SOURCE_COMPLETION'),
    ('blood-pickpocket', ['vyrewatch-sentinel'], 'Blood shard comes from pickpocketing rather than the assigned combat sentinel.', 'NO_ASSIGNED_SOURCE_COMPLETION'),
    ('blood-venator', ['vyrewatch-sentinel'], 'Blood shard comes from a venator rather than the assigned sentinel.', 'NO_ASSIGNED_SOURCE_COMPLETION'),
    ('separate-earning-delivery', list(SOURCE_CONTROLS), 'Earning occurred active but callback/save processing arrives paused; repeat with paused earning and active delivery.', 'PRESERVE_PROVED_ACTIVE_EVIDENCE_NO_PAUSED_PAYOUT_NO_PAUSED_EARNING_BACKFILL'),
    ('crash-after-journal', list(SOURCE_CONTROLS), 'Crash after supported occurrence is durably journaled but before settlement commit.', 'RECOVER_SAME_ASSIGNMENT_QUOTE_OPERATION_ONCE'),
    ('crash-after-settlement', list(SOURCE_CONTROLS), 'Crash after settlement is committed but before visual acknowledgment; replay raw callbacks after restore.', 'ONE_TOTAL_PAYOUT_NO_ITEM_CONSUMPTION_NO_NEW_ASSIGNMENT_CREDIT'),
    ('unproved-occurrence-id', list(SOURCE_CONTROLS), 'Adapter cannot distinguish one duplicate callback from a second real occurrence from observed data.', 'PENDING_NO_FABRICATED_STABLE_EVENT_ID'),
    ('catalog-version-mismatch', list(SOURCE_CONTROLS), 'Live source mapping/quote changes after commitment while old frozen supported receipt remains available.', 'PRESERVE_FROZEN_SNAPSHOT_NO_SILENT_POOL_OR_QUOTE_REWRITE'),
    ('future-context-safety', list(SOURCE_CONTROLS), 'External change invalidates committed selected/unselected context, beyond currently settled policy.', 'HOLD_EXISTING_PAUSE_REJECT_RULES_NO_INFERRED_A56_RETIREMENT'),
]


def build():
    core = json.loads((D / CORE).read_text())
    earlier = json.loads((D / EARLIER).read_text())
    sources = [CORE, EARLIER, 'research/WATER_ACCESS_SOURCE_MAP_A85.json']
    cases = []
    for hunt in core['hunts']:
        cases.append({
            'id': 'WB-CORE-positive-' + hunt['id'], 'kind': 'positive-hunt',
            'hunt_ids': [hunt['id']], 'source_keys': [hunt['creature_key']],
            'item_ids': hunt['accepted_drop_item_ids'], 'rank': hunt['rank'],
            'frozen_quote_version': hunt['source_quote_version'], 'frozen_fp': hunt['fp_reward'],
            'scenario': 'After current supported commitment, obtain the exact fresh active personal item from the authored legal source/context and correlate earning time separately from delivery.',
            'expected': 'SETTLE_ONCE_KEEP_ITEM_AT_FROZEN_QUOTE',
            'client_status': 'NOT_RUN', 'observed_trace': None, 'runtime_enabled': False,
        })
    for old in earlier['cases']:
        if old['id'].startswith('WB-RC-positive-'):
            continue
        case = deepcopy(old)
        case['kind'] = 'inherited-shared-boundary'
        case['inherited_source_case_id'] = old['id']
        cases.append(case)
    for suffix, keys, scenario, expected in TARGETED:
        cases.append({'id': 'WB-CORE-' + suffix, 'kind': 'targeted-boundary',
                      'source_keys': keys, 'scenario': scenario, 'expected': expected,
                      'client_status': 'NOT_RUN', 'observed_trace': None, 'runtime_enabled': False})
    matrix = []
    for creature in core['creatures']:
        matrix.append({
            'key': creature['key'], 'context_id': creature['source_context_id'],
            'hunt_ids': creature['source_hunt_ids'], 'composition_leads': creature['composition_leads'],
            'access_source': creature['authored_access'], 'minimum_skills_source': creature['minimum_skills'],
            'specific_control': SOURCE_CONTROLS[creature['key']],
            'required_form_result': 'For every listed lead, record actual form/loot alias/region/method/recipient/earning-time observation. Unobserved or ambiguous mapping stays unsupported, never default-accept.',
            'client_status': 'NOT_RUN', 'observed_trace': None, 'runtime_enabled': False,
        })
    result = {
        'version': 'core-client-trace-plan-a93-v1',
        'scope': 'Future actual-client acceptance planning; no production adapter, trace, durable IO or approval supplied',
        'runtime_enabled': False, 'client_tests_executed': 0,
        'sources': [{'path': p, 'sha256': hashlib.sha256((D / p).read_bytes()).hexdigest()} for p in sources],
        'required_derived_receipt_fields': earlier['receipt_required_fields'],
        'derived_fields_are_not_promised_api_fields': True,
        'trace_envelope': ['Actual client/build and adapter/source versions', 'Account/run/assignment and immutable offer/rank/source/context/item/quote snapshots', 'Initialized quest/skill/region/world/method and renewable-route observations', 'Raw ordered event payloads with provenance; local-recipient and earning-versus-delivery timeline', 'Journal/settlement operation identities and before/after balance/spin/assignment records', 'Restore/replay result and matched positive/negative control', 'Every context/form unsupported until its required observation is resolved'],
        'source_matrix': matrix, 'cases': cases, 'case_count': len(cases),
        'plan_coverage_only': {'keys': len(matrix), 'positive_hunts': len(core['hunts']), 'inherited_boundaries': 28, 'targeted_boundaries': len(TARGETED)},
        'not_settled': ['A56 no-fault retirement', 'Committed-but-unselected invalidation policy', 'Task continuation', 'Coffer/full-account boundary', 'Restrictive-control policy', 'Product prices/family schedule', 'Engineering authorization'],
    }
    validate(result, core)
    return result


def validate(plan, core):
    def require(condition, reason):
        if not condition:
            raise ValueError(reason)
    require(plan['runtime_enabled'] is False and plan['client_tests_executed'] == 0, 'plan is not client execution')
    require(plan['derived_fields_are_not_promised_api_fields'] is True, 'API evidence must remain unproved')
    for source in plan['sources']:
        require(hashlib.sha256((D/source['path']).read_bytes()).hexdigest() == source['sha256'], 'source hash mismatch')
    ids = [c['id'] for c in plan['cases']]
    require(len(ids) == len(set(ids)) == plan['case_count'], 'case count/duplicate identity')
    positives = [c for c in plan['cases'] if c['kind'] == 'positive-hunt']
    require({c['hunt_ids'][0] for c in positives} == {h['id'] for h in core['hunts']} and len(positives) == len(core['hunts']), 'hunt coverage')
    by_hunt = {h['id']: h for h in core['hunts']}
    for c in positives:
        h = by_hunt[c['hunt_ids'][0]]
        require((c['source_keys'],c['item_ids'],c['rank'],c['frozen_quote_version'],c['frozen_fp']) == ([h['creature_key']],h['accepted_drop_item_ids'],h['rank'],h['source_quote_version'],h['fp_reward']), 'wrong frozen hunt inputs')
    require({m['key'] for m in plan['source_matrix']} == {c['key'] for c in core['creatures']} and len(plan['source_matrix']) == len(core['creatures']), 'canonical source coverage')
    by_key = {c['key']: c for c in core['creatures']}
    for m in plan['source_matrix']:
        c = by_key[m['key']]
        require((m['context_id'],m['composition_leads'],m['hunt_ids']) == (c['source_context_id'],c['composition_leads'],c['source_hunt_ids']), 'context/form coverage')
    for c in plan['cases'] + plan['source_matrix']:
        require(c['client_status'] == 'NOT_RUN' and c['observed_trace'] is None and c['runtime_enabled'] is False, 'fabricated trace/pass/activation')
    required = {'WB-CORE-'+x[0] for x in TARGETED}
    require(required <= set(ids), 'targeted source/boundary controls missing')
    require(plan['plan_coverage_only'] == {'keys':len(core['creatures']), 'positive_hunts':len(positives), 'inherited_boundaries':sum(c['kind']=='inherited-shared-boundary' for c in plan['cases']), 'targeted_boundaries':sum(c['kind']=='targeted-boundary' for c in plan['cases'])}, 'coverage totals')


if __name__ == '__main__':
    plan = build()
    (D/OUTPUT).write_text(json.dumps(plan,indent=2)+'\n')
    print(json.dumps({'status': 'PLAN_COMPILED_NOT_CLIENT_TESTED', **plan['plan_coverage_only'], 'cases':plan['case_count']}))
