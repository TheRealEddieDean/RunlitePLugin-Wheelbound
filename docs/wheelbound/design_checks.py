"""Read-only documentation/content/source consistency checks, no plugin execution."""
import hashlib,json,re
from pathlib import Path
p=Path(__file__).parent
required='README BLUEPRINT GAME_RULES DECISIONS PROGRESSION WHEELS FATE_CARDS FATE_SHOP ECONOMY GRAND_FATES DEFY_FATE PUNISHMENTS BOUNTIES ACCOUNT_ACCESS UI_UX PERSISTENCE ARCHITECTURE RUNELITE_INTEGRATION EDGE_CASES TESTING SIMULATION_RESULTS IMPLEMENTATION_PLAN TASKS OPEN_QUESTIONS WORK_STATUS DEVELOPMENT_PIPELINE TRACEABILITY SOURCES'.split()
assert all((p/(x+'.md')).is_file() and len((p/(x+'.md')).read_text().strip())>100 for x in required)
s=(p/'DECISIONS.md').read_text();ids=re.findall(r'^## (WB-D\d+)',s,re.M);assert len(ids)==len(set(ids))
for f in p.glob('*.md'):
 for target in re.findall(r'\]\(([^)]+)\)',f.read_text()):
  if not target.startswith(('http','app:','#')):assert (f.parent/target.split('#')[0]).exists(),(f.name,target)
counts={'item_bounties':100,'daily_bounties':18,'punishments':50,'quest_metadata':213,'repository_boss_tiers':67}
for name,n in counts.items():
 rows=json.loads((p/'catalogs'/(name+'.json')).read_text())['entries'];assert len(rows)==n
 if name in ('item_bounties','daily_bounties','punishments'):
  assert len({r['id'] for r in rows})==n
  assert all(r['runtime_enabled'] is False for r in rows)
h3=json.loads((p/'simulation/catalog_trial_results.json').read_text());assert h3['total_trajectories']==12000
for name,sha in h3['source_hashes'].items():assert hashlib.sha256((p/'simulation'/name).read_bytes()).hexdigest()==sha
for name,sha in h3['catalog_hashes'].items():assert hashlib.sha256((p/'catalogs'/name).read_bytes()).hexdigest()==sha
sweep=json.loads((p/'simulation/weekend_sweep_results.json').read_text());assert sweep['total_trajectories']==18000
assert hashlib.sha256((p/'simulation/model.py').read_bytes()).hexdigest()==sweep['model_sha256']
print('PASS:',len(ids),'unique decisions; 28 required populated docs; local links; source/catalog execution hashes; 100/18/50/213/67 catalog counts; unverified runtime content disabled')

mode=json.loads((p/'catalogs/mode_boss_roster.json').read_text());assert len(mode['entries'])==64
from collections import Counter
assert Counter(r['tier'] for r in mode['entries'])=={'EASY':9,'MEDIUM':13,'HARD':22,'ELITE':11,'MASTER':6,'GRANDMASTER_RAIDS':3}
source=json.loads((p/'catalogs/repository_boss_tiers.json').read_text())['entries']
variants=[v for r in mode['entries'] for v in r['source_variants']]
assert len(variants)==len(set(variants))==67 and set(variants)=={r['hiscore'] for r in source}
assert all(r['runtime_enabled'] is False for r in mode['entries'])
for label,start,size,users in [('A02',1,100,50),('A03',101,100,50),('A04',201,100,50),('A05',301,136,68)]:
 review=json.loads((p/'research'/('RECOVERY_REVIEW_'+label+'.json')).read_text());rows=review['entries']
 assert [r['locator'] for r in rows]==['S8-M'+str(n).zfill(4) for n in range(start,start+size)]
 assert sum(r['review_status']=='USER_REVIEWED' for r in rows)==users
a=p.parent.parent/'automation'
assert all((a/(n+'.md')).is_file() for n in ['TASK_QUEUE','PROGRESS','SESSION_STATE','BLOCKERS','DECISIONS_PENDING'])
queue=re.findall(r'^\| (WB-A\d+) \|',(a/'TASK_QUEUE.md').read_text(),re.M);assert len(queue)==len(set(queue)) and len(queue)>=19
print('PASS: approved 64-node mode grouping; 436 contiguous indexed messages/218 reviewed user turns;',len(queue),'unique queued tasks and five checkpoints')

manifest=json.loads((p/'catalogs/AUTHORITY_MANIFEST.json').read_text())
assert len(manifest['catalogs'])==8
assert len({r['path'] for r in manifest['catalogs']})==8
for record in manifest['catalogs']:
 catalog=p/record['path'];data=json.loads(catalog.read_text())
 assert hashlib.sha256(catalog.read_bytes()).hexdigest()==record['sha256']
 assert len(data['entries'])==record['entry_count']
 assert record['authority'] and record['approval'] and record['enable_gates']
 assert record['runtime_enabled'] is False
for name,sha in h3['catalog_hashes'].items():
 record=next(r for r in manifest['catalogs'] if r['path']=='catalogs/'+name)
 assert record['h3_pinned_input'] and record['sha256']==sha
print('PASS: eight authority manifest hashes/counts/provenance/gates; historical H3 input pins retained')

diary=json.loads((p/'research/DIARY_COMPLETION_SOURCE_MAP.json').read_text())
assert len(diary['entries'])==len({x['varbit_id'] for x in diary['entries']})==48
assert not diary['runtime_enabled']
assert {x['symbol']:x['varbit_id'] for x in diary['entries'] if x['symbol'].startswith('ATJUN_')}=={'ATJUN_EASY_DONE':3578,'ATJUN_MED_DONE':3599,'ATJUN_HARD_DONE':3611}
print('PASS: 48 distinct diary source mappings, legacy Karamja aliases, disabled runtime status')

for name,n in [('BOUNTY_REVIEW_A08_ALL',100),('PUNISHMENT_REVIEW_A09_ALL',50)]:
 review=json.loads((p/'research'/(name+'.json')).read_text());assert len(review['entries'])==n
 assert len({x['id'] for x in review['entries']})==n and all(not x['runtime_enabled'] for x in review['entries'])
norm=json.loads((p/'catalogs/item_bounties_normative_v1.json').read_text())
assert len(norm['entries'])==100 and all(not x['runtime_enabled'] for x in norm['entries'])
family=[x for x in norm['entries'] if x.get('kind')=='grouped_family']
assert len(family)==2 and all(x['fp'] is None and x['claim_limit']==1 for x in family)
assert not {'WB-B093','WB-B100'} & {x['id'] for x in norm['entries']}
print('PASS: conditional reviews cover100 bounty/50 punishment records; separate finite100 family candidate; old inputs intact')

ca=json.loads((p/'research/CA_COMPLETION_SOURCE_CONTRACT.json').read_text())
assert [x['word_index'] for x in ca['completion_words']]==list(range(21))
assert len({x['varp_id'] for x in ca['completion_words']})==21
assert ca['completion_words'][13]['varp_id']==3387 and ca['struct_params']['boss']==1312
assert ca['task_tier_enums']==list(range(3981,3987)) and not ca['runtime_enabled']
print('PASS: explicit21 noncontiguous CA completion words and source-mapped task/boss schema')

newp=json.loads((p/'catalogs/punishments_normative_v1.json').read_text())
assert len(newp['entries'])==50 and all(not x['runtime_enabled'] for x in newp['entries'])
assert all('maximum_minutes' not in x['target'] for x in newp['entries'])
assert 'WITHHOLD' in next(x for x in newp['entries'] if x['id']=='WB-P043')['eligibility'][-1]
daily=json.loads((p/'research/DAILY_REVIEW_A10_ALL.json').read_text())
assert len(daily['entries'])==18 and all(not x['runtime_enabled'] for x in daily['entries'])
print('PASS: new50-template candidate removes global cap; risk withheld;18 daily conditional records')

# Later CI/capture receipts: distinguish actual build task from planned client cases.
baseline=json.loads((p/'research/BUILD_BASELINE.json').read_text())
assert baseline['conclusion']=='success' and baseline['java']=='Temurin 11.0.32+1'
assert baseline['executed_test_count'] is None and baseline['runtime_dependency_version'] is None
assert hashlib.sha256((p/'research/BUILD_LOG_EXCERPT.txt').read_bytes()).hexdigest()==baseline['log_excerpt_sha256']
assert hashlib.sha256((p/'research/BUILD_SOURCE_MANIFEST.json').read_bytes()).hexdigest()==baseline['source_manifest_sha256']
traces=json.loads((p/'research/TRACE_CASES_V1.json').read_text())
assert len(traces['cases'])==len({x['id'] for x in traces['cases']})==46
assert traces['live_trace_count']==0 and not traces['runtime_enabled']
assert all(x['status']=='NOT_RUN' and not x['runtime_enabled'] for x in traces['cases'])
schema=json.loads((p/'schemas/trace_capture.schema.json').read_text())
assert set(schema['properties']['case_id']['enum'])=={x['id'] for x in traces['cases']}
checks=json.loads((p/'research/TRACE_SCHEMA_CHECKS.json').read_text())
assert checks['status']=='PASS' and checks['live_trace_count']==0 and not checks['runtime_enabled']
for f in [p/'research/BUILD_BASELINE.md',p/'research/TRACE_CAPTURE_PROTOCOL.md']:
 for target in re.findall(r'\]\(([^)]+)\)',f.read_text()):
  if not target.startswith(('http','app:','#')):assert (f.parent/target.split('#')[0]).exists(),(f.name,target)
print('PASS: actual successful CI task with explicit report limits;46 NOT_RUN capture cases and offline schema/origin fixtures')

replay=json.loads((p/'simulation/h4/PROCESS_REPLAY_VALIDATION.json').read_text())
assert replay['status']=='PASS' and replay['cases_per_process']==23 and replay['worker_replays']==92
assert len({x['result_sha256'] for x in replay['hash_seed_checks']})==1
assert hashlib.sha256((p/'simulation/h4_replay_check.py').read_bytes()).hexdigest()==replay['checker_sha256']
assert hashlib.sha256((p/'simulation/h4/results_v1.json').read_bytes()).hexdigest()==replay['baseline_sha256']
for f in [p/'research/CI_PROPOSAL.md']:
 for target in re.findall(r'\]\(([^)]+)\)',f.read_text()):
  if not target.startswith(('http','app:','#')):assert (f.parent/target.split('#')[0]).exists(),(f.name,target)
print('PASS:23 saved H4 cases replayed across four hash-seed processes; model/input hashes remain pinned')
