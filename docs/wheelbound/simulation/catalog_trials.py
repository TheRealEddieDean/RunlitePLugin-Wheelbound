"""H3: 8 actual synthetic policies x 1500 trajectories. No live game action."""
import copy,hashlib,json
from pathlib import Path
from catalog_model import simulate,percentiles
p=Path(__file__).parent;base=json.loads((p/'parameters.json').read_text())
profiles={
 'balanced':('balanced',{}),
 'efficient':('focused',{}),
 'combat_rush':('focused',{'preference':'combat_rush','boss_access_after':5,'boss_purchase_interval':10}),
 'quest_rush':('balanced',{'preference':'quest_rush'}),
 'resource_constrained':('inefficient',{'acquisition_hours':[3,9]}),
 'risk_averse':('balanced',{'no_violations':True}),
 'minimal_bounty':('balanced',{'bounty_chance':0}),
 'voluntary_heavy':('focused',{'voluntary_probability_override':1}),
}
out={'seed':20261011,'accounts_per_profile':1500,'total_trajectories':12000,'events':300,
 'scope':'H3 synthetic policy comparison. Named bounty IDs/rewards and plain-XP punishment catalog are exercised; acquisition rates, real quest chains, actual boss access/kill rates, GP and resource routes remain proxies. Not full OSRS account/Grand Fate trials.',
 'source_hashes':{f:hashlib.sha256((p/f).read_bytes()).hexdigest() for f in ('catalog_model.py','catalog_trials.py')},
 'catalog_hashes':{f:hashlib.sha256((p.parent/'catalogs'/f).read_bytes()).hexdigest() for f in ('item_bounties.json','punishments.json')},'profiles':{}}
for j,(name,(strategy,overrides)) in enumerate(profiles.items()):
 config=copy.deepcopy(base);config.update(overrides)
 if config.pop('no_violations',False):
  config['reject_probability'][strategy]=0;config['violation_probability'][strategy]=0
 if 'voluntary_probability_override' in config:config['voluntary_probability'][strategy]=config.pop('voluntary_probability_override')
 rows=[simulate(out['seed']+j*1000000+i,strategy,config) for i in range(1500)]
 assert all(r['fp']==config['starting_fp']+r['income']-r['spend']-r['penalties'] for r in rows)
 out['profiles'][name]={'policy':strategy,'parameters':config,'percentiles':{k:percentiles([r[k] for r in rows]) for k in ('fp','hours','completions','quests','boss_tier','defies','bounty_income','punishment_template_count','small_punishment_pools','special_share')},'any_fp_debt':sum(r['negative_events']>0 for r in rows)/1500,'acquisition_incidence':sum(r['acquisition_recoveries']>0 for r in rows)/1500}
 print(name,'done',flush=True)
(p/'catalog_trial_results.json').write_text(json.dumps(out,indent=2,sort_keys=True)+'\n')
