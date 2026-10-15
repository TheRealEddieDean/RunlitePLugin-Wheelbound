"""Read-only source/coverage validation for a planning handoff, not a save migrator."""
import hashlib
import json
from pathlib import Path

R=Path(__file__).resolve().parents[2];D=R/'docs/wheelbound'
packet=json.loads((D/'research/FROZEN_SOURCE_HANDOFF_A94.json').read_text())
assert packet['runtime_enabled'] is False
assert packet['client_tests']=='NOT_RUN' and packet['migration_tests']=='NOT_RUN'
assert packet['production_migrator_supplied'] is False
for source in packet['inputs']:
    assert hashlib.sha256((R/source['path']).read_bytes()).hexdigest()==source['sha256'],source['path']
core=json.loads((D/'catalogs/challenge_core_v2_draft.json').read_text())
expected_hunts=[{k:h[k] for k in ['id','creature_key','rank','canonical_item_id','accepted_drop_item_ids','source_fragment','source_quote_version','fp_reward']} for h in core['hunts']]
expected_sources=[{k:c[k] for k in ['key','source_context_id','composition_leads','source_fragment']} for c in core['creatures']]
assert packet['current_hunt_identity_references']==expected_hunts
assert packet['current_canonical_source_references']==expected_sources
assert len({h['id'] for h in expected_hunts})==16
assert len({c['key'] for c in expected_sources})==14
assert packet['adopted_new_gameplay_decisions']==[]
assert set(packet['still_open'])=={'A56 permanent ordinary invalidation','Committed-but-unselected snapshot reconciliation','Version-specific monolithic/obsolete-CA payload migration','Task continuation','Coffer/policy boundaries'}
assert packet['case_status']=='PLANNING_NOT_RUN'
print(json.dumps({'status':'PASS_HANDOFF_SOURCE_AND_REFERENCE_COVERAGE_ONLY','source_pins':len(packet['inputs']),'hunt_references':len(expected_hunts),'canonical_source_references':len(expected_sources),'client_and_migration_tests':'NOT_RUN'}))
