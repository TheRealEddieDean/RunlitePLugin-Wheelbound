"""H4 offline core experiment. No RuneLite or game inputs. See CONTRACT.md."""
import copy
import hashlib
import json
import math
import random
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent
CONFIG = json.loads((HERE/'parameters.json').read_text())
ROSTER = json.loads((ROOT/'catalogs/mode_boss_roster.json').read_text())['entries']
PUNISHMENTS = json.loads((ROOT/'catalogs/punishments_normative_v1.json').read_text())['entries']
T1 = ['Cooking','Firemaking','Smithing','Crafting','Fletching','Agility','Thieving','Runecraft','Prayer']
T2 = ['Slayer','Herblore','Farming','Construction','Sailing','Hunter']
INITIAL = ['Mining','Fishing','Woodcutting','Combat','Questing']
WEIGHTS = [1,1.25,1.5,2,3]

class Engine:
    def __init__(self,seed,scenario,config=None):
        self.p=copy.deepcopy(config or CONFIG);self.scenario=copy.deepcopy(scenario);self.rng=random.Random(seed)
        self.fp=self.p['starting_fp'];self.spins=self.p['starting_spins'];self.hours=0.;self.serial=0
        self.wheel=[];self.satchel=[];self.capacity=3;self.unlocks=set();self.bosses=[]
        self.cards={'Skilling':{'Standard'},'Bossing':{'Standard'}};self.offer_count=2
        self.owned=[];self.blessed=set();self.quests=self.p['quest_tokens'];self.streak=0
        self.ledger=[];self.spent={};self.duplicates={};self.obligation=None;self.frozen=None
        self.xp={x:0 for x in ['Mining','Fishing','Woodcutting','Attack','Strength','Defence','Ranged','Magic']+T1+T2}
        self.stats={key:0 for key in ['ordinary','special','rejects','audit_violations','penances','defies','forced','empty_recovery','debt_events','small_pools','displacements','donation_value','pardon_purchases']}
        self.stats.update(max_penance_hours=0.,max_streak=0,penance_hours=0.)
        for activity in INITIAL:self.wheel.append(self.slice(activity))
        self.validate()
    def slice(self,activity,kind='ordinary',weight=1):
        self.serial+=1;return {'id':self.serial,'activity':activity,'kind':kind,'weight':weight,'tier':0}
    def validate(self):
        normal=[x for x in self.wheel if x['kind']=='ordinary']
        assert len(self.wheel)<=24 and len(normal)>=5 and len({x['activity'] for x in normal})>=3
        assert len(self.satchel)<=self.capacity
        ids=[x['id'] for x in self.wheel+self.satchel];assert len(ids)==len(set(ids))
        assert self.spins>=0 and all(x['weight']>0 for x in self.wheel)
        assert self.fp==self.p['starting_fp']+sum(x['fp'] for x in self.ledger)
        assert self.spins==self.p['starting_spins']+sum(x['spins'] for x in self.ledger)
    def tx(self,reason,fp=0,spins=0):
        assert self.spins+spins>=0
        self.fp+=fp;self.spins+=spins;self.ledger.append({'id':len(self.ledger)+1,'reason':reason,'fp':fp,'spins':spins})
        self.stats['debt_events']+=int(self.fp<0 and fp<0)
    def pay(self,reason,price):
        if self.fp<price:return False
        self.tx(reason,-price);self.spent[reason]=self.spent.get(reason,0)+price;return True
    def fit(self):
        while len(self.wheel)>24:
            ordinary=[x for x in self.wheel if x['kind']=='ordinary']
            legal=[x for x in ordinary if len(ordinary)-1>=5 and len({v['activity'] for v in ordinary if v['id']!=x['id']})>=3]
            if not legal:raise ValueError('No legal player-policy displacement')
            chosen=legal[-1]  # Explicit synthetic player policy, never special random displacement.
            self.wheel.remove(chosen)
            if len(self.satchel)<self.capacity:self.satchel.append(chosen)
            self.stats['displacements']+=1
        self.validate()
    def defy(self):
        assert self.spins==0 and self.obligation is None
        self.stats['defies']+=1;d=self.stats['defies'];self.tx('defy',spins=self.p['defy_refill'])
        if d==1:self.wheel.append(self.slice('Sacrifice','sacrifice'));self.blessed.add('starter-protected-type')
        else:next(x for x in self.wheel if x['kind']=='sacrifice')['weight']+=.25
        taints=[x for x in self.wheel if x['kind']=='taint']
        if d%2:
            if len(taints)<3:self.wheel.append(self.slice('Taint','taint'))
            else:
                for x in taints:x['weight']+=.25
        self.fit()
    def snapshot(self):
        data=copy.deepcopy({k:v for k,v in self.__dict__.items() if k!='rng'})
        data['rng_state']=self.rng.getstate();data['schema_version']=1
        return json.dumps(data,default=lambda x:sorted(x) if isinstance(x,set) else None)
    @classmethod
    def restore(cls,payload):
        data=json.loads(payload);assert data.pop('schema_version')==1
        state=data.pop('rng_state')
        def tuples(x):return tuple(tuples(v) for v in x) if isinstance(x,list) else x
        e=cls.__new__(cls);e.__dict__.update(data);e.rng=random.Random();e.rng.setstate(tuples(state))
        e.unlocks=set(e.unlocks);e.blessed=set(e.blessed);e.cards={k:set(v) for k,v in e.cards.items()};e.validate();return e
    def purchase_card(self,pool,kind):
        if kind in self.cards[pool]:return False
        if self.pay('card_'+pool,self.p['card_prices'][kind]):self.cards[pool].add(kind);return True
        return False
    def offers(self,activity):
        pool='Bossing' if activity=='Bossing' else 'Skilling'
        targets=self.rng.sample(range(3,13) if pool=='Bossing' else range(10,41),self.offer_count)
        out=[]
        for target in targets:
            kind=self.rng.choice(sorted(self.cards[pool]));m={'Standard':1,'Lesser':.6,'Greater':1.6}[kind]
            base=target if pool=='Bossing' else target*100
            quantity=max(1,round(base*m));base_fp=self.p['boss_base_fp'] if pool=='Bossing' else self.p['ordinary_reward']
            reward=base_fp if pool=='Bossing' and kind=='Lesser' else round(base_fp*m)
            out.append({'kind':kind,'standard_target':base,'quantity':quantity,'reward':reward})
        assert len({x['standard_target'] for x in out})==self.offer_count
        return out
    def freeze_assignment(self,activity):
        assert self.obligation is None
        if activity=='Questing' and self.quests==0:return None
        if activity=='Questing':chosen={'kind':'QuestChoice','standard_target':1,'quantity':1,'reward':100}
        else:
            offers=self.offers(activity)
            chosen=max(offers,key=lambda x:x['reward']/x['quantity']) if self.scenario['choice']=='efficiency' else self.rng.choice(offers)
            chosen=copy.deepcopy(chosen);chosen['offers']=offers
        chosen['activity']=activity;chosen['boss']=None
        if activity=='Bossing':
            assert self.bosses;chosen['boss']=copy.deepcopy(self.rng.choice(self.bosses)) # After card selected.
        self.frozen=chosen;self.obligation='ACTIVE_OBJECTIVE';return copy.deepcopy(chosen)
    def end_audit(self,unauthorized_xp=0,unauthorized_kills=0,kill_objective=False):
        bad=unauthorized_kills>0 if kill_objective else unauthorized_xp>1000
        if bad:
            self.streak+=1;self.stats['audit_violations']+=1;self.stats['max_streak']=max(self.stats['max_streak'],self.streak)
            self.tx('completion_audit_penalty',-self.p['audit_fp_penalty'])
        else:self.streak=0
        return bad
    def punishment(self,reason):
        combat={'Attack','Strength','Defence','Ranged','Magic'}
        allowed=set(INITIAL[:3])|self.unlocks|combat
        pool=[x for x in PUNISHMENTS if x['family']=='plain_xp' and x['skill'] in allowed]
        assert pool;self.stats['small_pools']+=int(len(pool)<12)
        shown=self.rng.sample(pool,min(12,len(pool)));self.obligation='PUNISHMENT'
        while True:
            duration=self.p['base_penance_hours']*self.p['severity_multiplier']**max(0,self.streak-1)
            self.frozen={'template':None,'duration':duration,'reason':reason,'catalog':'normative-punishment-candidate-v1'}
            quantity=max(100,round(duration*14000/100)*100)
            feasible=[x for x in shown if self.xp[x['skill']]+quantity<=200000000]
            if not feasible:
                self.obligation='MODEL_ROUTE_UNAVAILABLE';return False
            template=self.rng.choice(feasible)
            self.frozen.update(template=template['id'],quantity=quantity)
            self.stats['penances']+=1;self.stats['max_penance_hours']=max(self.stats['max_penance_hours'],duration)
            # Horizon is observation end, not a punishment cap or task expiry.
            if self.hours+duration>self.p['horizon_hours']:return False
            self.hours+=duration;self.stats['penance_hours']+=duration;self.xp[template['skill']]+=quantity
            violation=self.rng.random()<self.scenario['penance_violation_p']
            bad=self.end_audit(unauthorized_xp=1001 if violation else 0)
            if not bad or self.scenario['audit_reward']=='retain':self.tx('punishment_reward',self.p['punishment_fp'])
            if not bad:self.obligation=None;self.frozen=None;return True
            reason='consecutive_penance' # New frozen obligation; no additional Master spin.
    def freeze_sacrifice(self,known=True):
        if not known:return None
        candidates=[x for x in self.owned if x['type'] not in self.blessed and x['unit_gp']>=self.p['minimum_item_gp']]
        if not candidates:return []
        self.obligation='SACRIFICE';self.frozen={'candidates':copy.deepcopy(candidates),'demand':copy.deepcopy(self.rng.choice(candidates))}
        return copy.deepcopy(self.frozen['demand'])
    def sacrifice(self):
        demand=self.freeze_sacrifice();self.stats['forced']+=1
        if demand==[]:
            self.obligation='SACRIFICE_ACQUISITION';self.stats['empty_recovery']+=1
            self.hours+=self.rng.uniform(*self.p['recovery_hours'])
            self.owned.append({'id':self.serial+10000+len(self.ledger),'type':'recovered-eligible-item','unit_gp':self.p['minimum_item_gp'],'quantity':1})
            demand=self.freeze_sacrifice()
        assert demand is not None and demand!=[]
        self.owned=[x for x in self.owned if x['id']!=demand['id']]
        value=demand['unit_gp']*demand['quantity'];self.stats['donation_value']+=value
        self.tx('verified_donation_proxy',spins=max(1,value//self.p['gp_per_spin']))
        self.hours+=.15;self.obligation=None;self.frozen=None
    def shops(self):
        # Each optional sink is conditional on budget; no guaranteed bounty income.
        for pool in ['Skilling','Bossing']:
            for kind in ['Lesser','Greater']:
                if kind not in self.cards[pool]:self.purchase_card(pool,kind);break
        if self.offer_count==2 and self.stats['ordinary']>35 and self.pay('third_card',self.p['third_card_price']):self.offer_count=3
        avail=[x for x in T1 if x not in self.unlocks]
        if len(self.unlocks&set(T1))>=5:avail+=[x for x in T2 if x not in self.unlocks]
        if avail and len(self.wheel)<24:
            skill=avail[0];price=150 if skill in T1 else 350
            if self.pay('activity_and_slice',price+25):self.unlocks.add(skill);self.wheel.append(self.slice(skill))
        if len(self.bosses)<len(ROSTER):
            candidate=ROSTER[len(self.bosses)]
            if self.pay('boss_node',200+25*min(5,len(self.bosses)//10)):
                self.bosses.append(copy.deepcopy(candidate))
                if not any(x['activity']=='Bossing' for x in self.wheel):self.wheel.append(self.slice('Bossing'));self.fit()
        mastery=len(self.unlocks&set(T2))>=4
        if mastery and not self.spent.get('mastery'):self.pay('mastery',self.p['mastery_price'])
        if self.spent.get('mastery'):
            ordinary=next(x for x in self.wheel if x['kind']=='ordinary')
            if ordinary['tier']<4 and self.pay('enhancement',self.p['enhancement_prices'][ordinary['tier']]):
                ordinary['tier']+=1;ordinary['weight']=WEIGHTS[ordinary['tier']]
            elif len(self.wheel)<24:
                activity='Mining';n=self.duplicates.get(activity,0);prices=self.p['duplicate_prices']
                price=prices[n] if n<len(prices) else math.ceil(prices[-1]*1.4**(n-len(prices)+1)/50)*50
                if self.pay('duplicate',price):self.duplicates[activity]=n+1;self.wheel.append(self.slice(activity))
        level=[3,6,10,15].index(self.capacity)
        if level<3 and self.stats['ordinary']%30==0 and self.pay('satchel',self.p['satchel_prices'][level]):self.capacity=[6,10,15][level]
        if self.stats['defies'] and len(self.blessed)<3:
            index=len(self.blessed)-1
            if self.pay('blessing',self.p['blessing_prices'][index]):self.blessed.add('synthetic-protected-type-'+str(index))
        taints=[x for x in self.wheel if x['kind']=='taint']
        if taints and self.scenario['cleansing'] and self.pay('cleansing',self.p['cleansing_price']):self.wheel.remove(taints[0])
        self.validate()
    def run(self):
        stop='DRAW_HORIZON'
        for _ in range(self.p['max_master_draws']):
            if self.hours>=self.p['horizon_hours']:stop='TIME_HORIZON';break
            if self.spins==0:self.defy()
            selected=self.rng.choices(self.wheel,weights=[x['weight'] for x in self.wheel])[0]
            kind=selected['kind']
            if kind!='ordinary':
                self.tx('special_allocation',spins=-1);self.stats['special']+=1
                if kind=='taint':
                    if not self.punishment('taint'):stop='PENDING_PENANCE_HORIZON';break
                else:self.sacrifice()
                self.validate();continue
            activity=selected['activity'];assignment=self.freeze_assignment(activity)
            if assignment is None:stop='BLOCKED_Q6_EXHAUSTED_QUEST';break
            self.tx('ordinary_allocation',spins=-1)
            if self.rng.random()<self.scenario['reject_p']:
                self.tx('reject_penalty',-self.p['reject_penalty']);self.stats['rejects']+=1
                if not self.punishment('reject'):stop='PENDING_PENANCE_HORIZON';break
                continue
            duration=.6 if activity=='Questing' else assignment['quantity']/14000
            if activity=='Bossing':
                index=['EASY','MEDIUM','HARD','ELITE','MASTER','GRANDMASTER_RAIDS'].index(assignment['boss']['tier'])
                duration=assignment['quantity']*[2,4,6,8,12,25][index]/60
            if self.hours+duration>self.p['horizon_hours']:stop='PENDING_FATE_HORIZON';break
            self.hours+=duration;self.stats['ordinary']+=1
            if activity=='Questing':self.quests-=1
            elif activity not in ['Combat','Bossing']:self.xp[activity]+=assignment['quantity']
            elif activity=='Combat':self.xp['Attack']+=assignment['quantity']
            violated=self.rng.random()<self.scenario['violation_p']
            bad=self.end_audit(unauthorized_xp=1001 if violated else 0,unauthorized_kills=1 if violated else 0,kill_objective=activity=='Bossing')
            if not bad or self.scenario['audit_reward']=='retain':self.tx('ordinary_reward',assignment['reward'])
            self.obligation=None;self.frozen=None
            if bad and not self.punishment('audit'):stop='PENDING_PENANCE_HORIZON';break
            if self.rng.random()<self.p['synthetic_owned_item_probability']:
                self.owned.append({'id':len(self.ledger)+50000,'type':'owned-loot-'+str(len(self.ledger)),'unit_gp':self.rng.randint(10000,150000),'quantity':self.rng.randint(1,3)})
            self.shops()
        self.validate()
        return {**self.stats,'stop':stop,'fp':self.fp,'hours':self.hours,'spins':self.spins,'spent':self.spent,'wheel_size':len(self.wheel),'boss_pool':len(self.bosses),'ordinary_unlocks':len(self.unlocks),'pending':self.obligation,'special_share':sum(x['weight'] for x in self.wheel if x['kind']!='ordinary')/sum(x['weight'] for x in self.wheel),'ledger_fp_identity':self.fp==self.p['starting_fp']+sum(x['fp'] for x in self.ledger),'ledger_spin_identity':self.spins==self.p['starting_spins']+sum(x['spins'] for x in self.ledger)}

def verify_inputs():
    manifest=json.loads((HERE/'INPUT_MANIFEST.json').read_text())
    for name,sha in manifest['hashes'].items():assert hashlib.sha256((ROOT/name).read_bytes()).hexdigest()==sha
