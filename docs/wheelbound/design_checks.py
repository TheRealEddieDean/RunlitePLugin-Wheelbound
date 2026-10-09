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
