"""Meaningful offline contract fixtures; not client validation."""
import copy
from model import Engine,CONFIG,verify_inputs
verify_inputs();policy=CONFIG['scenarios'][0]
# RNG restoration: frozen boss/cards restored from snapshot, no second allocation.
e=Engine(1,policy);e.fp=1000;e.p['starting_fp']=1000;e.bosses=[{'tier':'EASY','id':'test-boss'}]
x=e.freeze_assignment('Bossing');restored=Engine.restore(e.snapshot());again=restored.frozen;assert x==again and len(x['offers'])==2
assert e.spins==100 and e.obligation=='ACTIVE_OBJECTIVE'
# Actual namesake purchases do not cross pools.
e=Engine(2,policy);e.fp=1000;e.p['starting_fp']=1000;e.purchase_card('Skilling','Lesser');assert 'Lesser' not in e.cards['Bossing'];e.validate()
# No audit until completion invocation; kill XP by-product is not an unauthorized kill.
e=Engine(3,policy);assert e.fp==0;assert not e.end_audit(unauthorized_xp=999999,unauthorized_kills=0,kill_objective=True)
assert e.end_audit(unauthorized_xp=1001);assert e.streak==1 and e.fp==-100
assert not e.pay('cannot_spend_debt',1)
# Clean Penance resets; no extra spin. Repeated Penance can exceed120 minutes.
p=copy.deepcopy(policy);p['penance_violation_p']=0;e=Engine(4,p);e.streak=4;assert e.punishment('audit');assert e.stats['max_penance_hours']==4 and e.streak==0 and e.spins==100
# Forced selection genuinely random across candidates; quantity retained, not FIFO.
values=set()
for seed in range(100):
 e=Engine(seed,policy);e.owned=[{'id':1,'type':'a','unit_gp':10000,'quantity':2},{'id':2,'type':'b','unit_gp':100000,'quantity':1}]
 demand=e.freeze_sacrifice();values.add(demand['id']);assert demand['quantity'] in [1,2]
assert values=={1,2}
e=Engine(5,policy);assert e.freeze_sacrifice(known=False) is None
e.tx('special_allocation',spins=-1);e.sacrifice();assert e.stats['empty_recovery']==1 and e.spins>=100;e.validate()
# Legal player displacement at full capacity, ordinary diversity preserved.
e=Engine(6,policy)
while len(e.wheel)<24:e.wheel.append(e.slice('Mining'))
e.spins=0;e.ledger.append({'id':1,'reason':'fixture_allocations','fp':0,'spins':-100});e.defy();assert len(e.wheel)==24 and e.stats['displacements']==2;e.validate()
# Exhausted quest has no allocation/reward or automatic wheel edit.
e=Engine(7,policy);e.quests=0;assert e.freeze_assignment('Questing') is None and e.spins==100 and len(e.wheel)==5
# Whole-run seeded replay and both ledger identities.
a=Engine(20,policy).run();b=Engine.restore(Engine(20,policy).snapshot()).run();assert a==b and a['ledger_fp_identity'] and a['ledger_spin_identity']
print('PASS: H4 input pins, frozen draws, separate pools, completion audit, clean reset, >120min Penance, random demand, one debit/recovery, legal displacement, exhausted quest and seeded ledgers')
