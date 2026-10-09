"""Offline content and seeded replay checks; no live detector validation."""
import copy,json
from pathlib import Path
from catalog_model import simulate
p=Path(__file__).parent;c=p.parent/'catalogs'
for name,count,key in [('item_bounties.json',100,'id'),('punishments.json',50,'id'),('quest_metadata.json',213,'quest_id')]:
 rows=json.loads((c/name).read_text())['entries'];assert len(rows)==count;assert len({r[key] for r in rows})==count
 if name!='quest_metadata.json':assert all(not r['runtime_enabled'] for r in rows)
base=json.loads((p/'parameters.json').read_text())
for strategy in ('balanced','focused','inefficient'):
 a=simulate(20261011,strategy,base);assert a==simulate(20261011,strategy,base)
 assert a['fp']==base['starting_fp']+a['income']-a['spend']-a['penalties']
without=copy.deepcopy(base);without['bounty_chance']=0
assert simulate(20261011,'balanced',without)['bounty_income']==0
low=copy.deepcopy(base);low['starting_spins']=1;low['defy_refill']=25
rows=[simulate(i,'focused',low) for i in range(20)]
assert any(r['small_punishment_pools'] for r in rows)
assert all(r['punishment_template_count']<=23 for r in rows)
print('PASS: catalog identities/counts, unvalidated content disabled, seeded H3 replay, FP ledger, no-bounty policy, smaller eligible punishment pools')
