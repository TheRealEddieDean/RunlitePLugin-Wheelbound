"""Matched-seed conversion/risk sensitivity; existing synthetic model, not full OSRS.
Run: python3 docs/wheelbound/simulation/weekend_sweep.py
"""
import copy,hashlib,json
from pathlib import Path
from model import simulate,percentiles
p=Path(__file__).parent
base=json.loads((p/'parameters.json').read_text())
profiles={
 'balanced_reference':('balanced',{}),
 'efficient_reference':('focused',{}),
 'resource_constrained_proxy':('inefficient',{'efficiency':.4,'acquisition_hours':[3,9]}),
 'risk_averse_proxy':('balanced',{'reject_probability':0,'violation_probability':0,'voluntary_probability':0}),
 'high_violation_proxy':('inefficient',{'reject_probability':.15,'violation_probability':.12}),
 'voluntary_heavy_proxy':('focused',{'voluntary_probability':1}),
}
out={'seed':20261010,'accounts_per_cell':1000,'total_trajectories':18000,
 'horizon_events':300,'model_sha256':hashlib.sha256((p/'model.py').read_bytes()).hexdigest(),
 'runner_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
 'scope':'Parameter profiles only. No new combat-rush/quest-rush policy, exact content or full Grand Fate endgame simulation. Minimum reward floor remains one spin; not approved.', 'cells':{}}
for conversion in (25000,100000,250000):
 for index,(name,(policy,overrides)) in enumerate(profiles.items()):
  config=copy.deepcopy(base);config['gp_per_spin']=conversion
  for k,v in overrides.items():
   if isinstance(config[k],dict):config[k][policy]=v
   else:config[k]=v
  rows=[simulate(out['seed']+index*1000000+i,policy,config) for i in range(1000)]
  assert all(r['fp']==config['starting_fp']+r['income']-r['spend']-r['penalties'] for r in rows)
  out['cells'][f'{conversion}/{name}']={'policy':policy,'parameters':config,
   'percentiles':{k:percentiles([r[k] for r in rows]) for k in ('fp','hours','defies','completions','recovery_hours','special_share')},
   'acquisition_incidence':sum(r['acquisition_recoveries']>0 for r in rows)/1000,
   'modeled_item_stop_rate':sum(r['no_eligible_item_stop']>0 for r in rows)/1000}
  print(conversion,name,'done',flush=True)
(p/'weekend_sweep_results.json').write_text(json.dumps(out,indent=2,sort_keys=True)+'\n')
