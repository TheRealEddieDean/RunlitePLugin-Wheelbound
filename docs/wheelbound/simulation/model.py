"""Offline Wheelbound design model; no RuneLite/game actions.

Run from any directory: python3 model.py --accounts-per-strategy 4000
All game-duration, loot and access proxies are assumptions, not live OSRS data.
"""
import argparse
import bisect
import hashlib
import json
import math
import random
from pathlib import Path

BASE = Path(__file__).resolve().parent
SKILLS = {
    "Mining": (14000, 60000), "Fishing": (14000, 75000),
    "Woodcutting": (18000, 90000), "Combat": (20000, 100000),
    "Cooking": (60000, 250000), "Firemaking": (50000, 250000),
    "Smithing": (25000, 130000), "Crafting": (30000, 180000),
    "Fletching": (30000, 200000), "Agility": (10000, 60000),
    "Thieving": (15000, 130000), "Runecraft": (8000, 45000),
    "Prayer": (25000, 200000), "Slayer": (8000, 40000),
    "Herblore": (30000, 160000), "Farming": (15000, 60000),
    "Construction": (50000, 250000), "Sailing": (12000, 70000),
    "Hunter": (15000, 100000),
}
T1 = list(SKILLS)[4:13]
T2 = list(SKILLS)[13:]
XP = [0]
total = 0
for level in range(1, 99):
    total += math.floor(level + 300 * 2 ** (level / 7))
    XP.append(total // 4)
BRACKETS = [(30,1000,3000),(50,4000,10000),(70,12000,30000),
            (80,35000,65000),(90,75000,130000),(100,140000,250000)]

def skill_level(xp):
    return min(99, bisect.bisect_right(XP, xp))

def validate(wheel, satchel, cap):
    ordinary = [s for s in wheel if s["kind"] == "ordinary"]
    assert len(wheel) <= 24 and len(ordinary) >= 5
    assert len({s["activity"] for s in ordinary}) >= 3
    assert len(satchel) <= cap
    ids = [s["id"] for s in wheel + satchel]
    assert len(ids) == len(set(ids))
    assert all(s["weight"] > 0 for s in wheel)
    total_weight=sum(t["weight"] for t in wheel)
    assert abs(sum(s["weight"] / total_weight for s in wheel)-1) < 1e-12

def distinct_draws(rng, low, high, count):
    """Independent proposals, rejection sampling duplicate targets."""
    values = []
    assert high-low+1 >= count
    while len(values) < count:
        value = rng.randint(low, high)
        if value not in values:
            values.append(value)
    return values

def simulate(seed, strategy, params):
    rng = random.Random(seed)
    wheel = []
    next_id = 0
    def new_slice(activity, kind="ordinary", weight=1):
        nonlocal next_id
        next_id += 1
        return dict(id=next_id, activity=activity, kind=kind, weight=weight, tier=0)
    for activity in ["Mining","Fishing","Woodcutting","Combat","Questing"]:
        wheel.append(new_slice(activity))
    satchel, cap = [], 3
    xp = {s: 0 for s in SKILLS}
    fp, spins, gp = params["starting_fp"], params["starting_spins"], 0
    income, spend, penalties, hours = 0, 0, 0, 0.0
    defies = exhaustions = completions = rejects = 0
    forced = punishments = no_items = negative = 0
    acquisition_recoveries = 0
    recovery_hours = 0.0
    unlocked = set()
    dup_count = {}
    card_types = ["Standard"]
    offer_count = 2
    quests = quest_tier = bosses = boss_tier = vendors = pardons = bans = 0
    ge = False
    gear_tier = 0
    bounty_claims = set()
    item_values = []
    spent_by = {}
    special_landings = 0
    def pay(category, price):
        nonlocal fp, spend
        if fp < price:
            return False
        fp -= price
        spend += price
        spent_by[category] = spent_by.get(category, 0)+price
        return True
    def earn(value):
        nonlocal fp, income
        fp += value
        income += value
    def displace():
        ordinary = [s for s in wheel if s["kind"]=="ordinary"]
        eligible=[]
        for s in ordinary:
            remaining=[t for t in ordinary if t is not s]
            if len(remaining)>=5 and len({t["activity"] for t in remaining})>=3:
                eligible.append(s)
        assert eligible
        s=min(eligible,key=lambda t:t["weight"])
        if len(satchel)<cap and pay("storage",params["deactivate"]):
            satchel.append(s)
        wheel.remove(s)
    for event in range(params["events"]):
        validate(wheel,satchel,cap)
        if spins == 0:
            exhaustions += 1
            if defies and item_values and rng.random()<params["voluntary_probability"][strategy]:
                value=item_values.pop(0)
                gp=max(0,gp-value)
                spins += max(1,value//params["gp_per_spin"])
            else:
                defies += 1
                spins += params["defy_refill"]
                if defies==1:
                    if len(wheel)==24: displace()
                    wheel.append(new_slice("Sacrifice","sacrifice"))
                else:
                    next(s for s in wheel if s["kind"]=="sacrifice")["weight"] += .25
                if defies % 2:
                    taints=[s for s in wheel if s["kind"]=="taint"]
                    if len(taints)<3:
                        if len(wheel)==24: displace()
                        wheel.append(new_slice("Taint","taint"))
                    else:
                        for s in taints: s["weight"] += .25
        s=rng.choices(wheel,weights=[t["weight"] for t in wheel])[0]
        if s["kind"] != "ordinary":
            special_landings+=1
            if params["special_consumes_spin"]: spins-=1
            if s["kind"]=="taint":
                punishments+=1
                hours += rng.uniform(1,4)
                earn(params["punishment_fp"])
            else:
                forced+=1
                if not item_values:
                    no_items += 1
                    if not params.get("acquisition_recovery", False):
                        break  # Historical H1 only; current rule requires acquisition.
                    acquisition_recoveries += 1
                    duration = rng.uniform(*params["acquisition_hours"]) / params["efficiency"][strategy]
                    recovery_hours += duration
                    hours += duration
                    # Synthetic earned-item route: no vendor/GE unlock, FP or normal Fate reward.
                    # Duration and availability are hypotheses, not a verified legal OSRS route.
                    item_values.append(params["minimum_recovery_item_gp"])
                    gp += params["minimum_recovery_item_gp"]
                value=item_values.pop(0)
                gp=max(0,gp-value)
                spins += max(1,value//params["gp_per_spin"])
                hours += .15
            continue
        activity=s["activity"]
        if rng.random()<params["reject_probability"][strategy]:
            fp-=params["reject_penalty"]
            penalties+=params["reject_penalty"]
            spins-=1
            rejects+=1
            punishments+=1
            hours+=rng.uniform(1,4)
            earn(params["punishment_fp"])
            negative+=int(fp<0)
            continue
        if activity=="Questing":
            low,high=30+quest_tier*15,70+quest_tier*30
        elif activity=="Bossing":
            low,high=max(2,12-boss_tier*2),max(5,25-boss_tier*3)
        else:
            level=skill_level(xp[activity])
            _,lo,hi=next(b for b in BRACKETS if level<b[0])
            # Duration-scaled hypothesis, not the universal recovered combat table.
            rate=SKILLS[activity][0]+(SKILLS[activity][1]-SKILLS[activity][0])*((level-1)/98)
            rate *= params["efficiency"][strategy]
            low,high=max(100,int(rate*.4)//100*100),max(200,int(rate*.9)//100*100)
        targets=distinct_draws(rng,low,high,offer_count)
        offers=[]
        for target in targets:
            card=rng.choice(card_types)
            mult={"Standard":1,"Lesser":.6,"Greater":1.6,"Challenge":1.25,"Wilderness":1.25}[card]
            quantity=target if activity=="Questing" else max(1,round(target*mult))
            if activity=="Bossing":
                # Boss unknown at choice; reference estimate, fixed tier base FP.
                duration=quantity*[2,4,6,8,12,25][boss_tier]/60
                reward=round((90+boss_tier*20)*(1 if card=="Lesser" else mult))
            elif activity=="Questing":
                duration=target/60
                reward=round(70+quest_tier*30+duration*30)
            else:
                duration=quantity/rate
                reward=round(40+duration*80)
                if card in ("Challenge","Wilderness"): reward=round(reward*1.2)
            offers.append((quantity,max(.05,duration),reward,card))
        if strategy=="focused":
            chosen=max(offers,key=lambda o:o[2]/o[1])
        elif strategy=="balanced":
            chosen=max(offers,key=lambda o:o[2]-o[1]*70)
        else:
            chosen=rng.choice(offers)
        quantity,duration,reward,card=chosen
        if activity=="Bossing":
            # Random synthetic boss within eligible tier, after card selection.
                duration *= rng.uniform(.7,1.5)/(1+gear_tier*.1)
        hours+=duration+.08
        if activity in xp: xp[activity]+=quantity
        elif activity=="Questing":
            quests+=1
            quest_tier=min(4,quests//8)
            # Quest XP is permitted by-product proxy, not a verified real quest reward.
            skill=rng.choice(list(xp))
            xp[skill]+=rng.randint(1000,5000)
        spins-=1
        completions+=1
        earn(reward)
        if rng.random()<params["violation_probability"][strategy]:
            fp-=params["violation_penalty"]
            penalties+=params["violation_penalty"]
        negative+=int(fp<0)
        loot=max(0,round(duration*rng.uniform(10000,80000)*(1+boss_tier/2)))
        gp+=loot
        # Supply sink and purchased gear proxy, separate from Fate Points.
        supply_cost=round(duration*(12000 if activity=="Bossing" else 3000))
        gp=max(0,gp-supply_cost)
        if gear_tier<3 and gp>=params["gear_costs"][gear_tier]:
            gp-=params["gear_costs"][gear_tier]
            gear_tier+=1
        if rng.random()<.10:
            # Qualifying per-item value proxy; no cheap-stack eligibility.
            item_values.append(rng.randint(12000,150000))
        if rng.random()<.03:
            key=rng.randrange(100)
            if key not in bounty_claims:
                bounty_claims.add(key)
                earn(rng.choice([50,100,200,500]))
        # All optional spending respects sufficient FP.
        if bans and fp>=params["pardon"] and strategy!="inefficient":
            if pay("pardon",params["pardon"]):
                bans-=1; pardons+=1
                # Restored Locked, not Unlocked: no vendor increment.
        if vendors<6 and completions%12==0:
            if strategy=="inefficient":
                if rng.random()<.5: vendors+=1
                else: bans+=1
            elif pay("vendor",params["vendor_base"]*(vendors+1)):
                vendors+=1
        if not ge and completions>=params["ge_fates"] and pay("ge",params["ge"]): ge=True
        if "Lesser" not in card_types and pay("card",150): card_types.append("Lesser")
        elif "Greater" not in card_types and pay("card",350): card_types.append("Greater")
        elif offer_count==2 and completions>35 and pay("third_card",1000): offer_count=3
        available=[a for a in T1 if a not in unlocked]
        if len(unlocked.intersection(T1))>=5: available += [a for a in T2 if a not in unlocked]
        if available and (strategy!="inefficient" or rng.random()<.2):
            a=available[0] if strategy!="focused" else max(available,key=lambda a:SKILLS[a][1])
            cost=params["t1_unlock"] if a in T1 else params["t2_unlock"]
            if len(wheel)<24 and pay("unlock",cost+25):
                unlocked.add(a); wheel.append(new_slice(a))
        if completions>=15 and "Bossing" not in [t["activity"] for t in wheel] and len(wheel)<24:
            if pay("boss_access",250+25):
                wheel.append(new_slice("Bossing")); bosses=1
        if bosses and bosses<6 and completions%20==0 and pay("boss_tier",200+bosses*100):
            bosses+=1; boss_tier=min(5,bosses-1)
        mastery=len(unlocked.intersection(T2))>=4
        if mastery:
            selected=next(t for t in wheel if t["kind"]=="ordinary")
            if strategy=="focused" and selected["tier"]<4:
                price=[75,150,300,600][selected["tier"]]
                if pay("enhancement",price):
                    selected["tier"]+=1
                    selected["weight"]=[1,1.25,1.5,2,3][selected["tier"]]
            elif len(wheel)<24 and rng.random()<.3:
                a="Mining" if strategy!="balanced" else rng.choice(sorted(unlocked)+["Mining"])
                count=dup_count.get(a,0)
                price=params["duplicates"][min(count,6)]
                if count>=7: price=math.ceil(price*1.4**(count-6)/50)*50
                if pay("duplicate",price):
                    dup_count[a]=count+1; wheel.append(new_slice(a))
        if strategy=="inefficient" and fp>params["deactivate"]+params["reactivate"] and completions%10==0:
            # Inefficient churn exercises Satchel UUID/upgrade retention.
            if len(wheel)>5 and len(satchel)<cap:
                candidates=[t for t in wheel if t["kind"]=="ordinary"]
                moved=candidates[-1]
                remaining=[t for t in candidates if t is not moved]
                if len(remaining)>=5 and len({t["activity"] for t in remaining})>=3:
                    if pay("churn",params["deactivate"]+params["reactivate"]):
                        wheel.remove(moved); satchel.append(moved)
                        validate(wheel,satchel,cap)
                        satchel.remove(moved); wheel.append(moved)
        taints=[t for t in wheel if t["kind"]=="taint"]
        if strategy=="balanced" and taints and pay("cleansing",params["cleansing"]):
            wheel.remove(taints[0])
    validate(wheel,satchel,cap)
    special_share=sum(s["weight"] for s in wheel if s["kind"]!="ordinary")/sum(s["weight"] for s in wheel)
    return dict(fp=fp,income=income,spend=spend,penalties=penalties,hours=hours,completions=completions,
                defies=defies,exhaustions=exhaustions,rejects=rejects,forced=forced,
                punishments=punishments,no_eligible_item_stop=(no_items if not params.get("acquisition_recovery", False) else 0),
                acquisition_recoveries=acquisition_recoveries,recovery_hours=recovery_hours,negative_events=negative,
                unlocked=len(unlocked),max_level=max(map(skill_level,xp.values())),
                quests=quests,boss_tier=boss_tier,vendors=vendors,pardons=pardons,bans=bans,
                ge=int(ge),special_share=special_share,special_landings=special_landings,
                gp=gp,gear_tier=gear_tier,spent_by=spent_by)

def percentiles(values):
    v=sorted(values)
    return {str(q):round(v[round((len(v)-1)*q/100)],3) for q in (10,50,90)}

def main():
    parser=argparse.ArgumentParser()
    parser.add_argument("--accounts-per-strategy",type=int,default=4000)
    parser.add_argument("--output",default="results.json")
    parser.add_argument("--params",default="parameters.json")
    args=parser.parse_args()
    params=json.loads((BASE/args.params).read_text())
    assert args.accounts_per_strategy>0
    result={"seed":params["seed"],"accounts_per_strategy":args.accounts_per_strategy,
            "parameters":params,"model_sha256":hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),
            "strategies":{}}
    for index,strategy in enumerate(("focused","balanced","inefficient")):
        accounts=[simulate(params["seed"]+index*1_000_000+i,strategy,params)
                  for i in range(args.accounts_per_strategy)]
        metrics={}
        for key in accounts[0]:
            if key!="spent_by": metrics[key]=percentiles([a[key] for a in accounts])
        result["strategies"][strategy]={"percentiles":metrics,
            "item_stop_rate":sum(a["no_eligible_item_stop"]>0 for a in accounts)/len(accounts),
            "acquisition_rate":sum(a["acquisition_recoveries"]>0 for a in accounts)/len(accounts),
            "ge_rate":sum(a["ge"] for a in accounts)/len(accounts),
            "negative_account_rate":sum(a["negative_events"]>0 for a in accounts)/len(accounts),
            "spent_by":{k:round(sum(a["spent_by"].get(k,0) for a in accounts)/len(accounts),2)
                         for k in {k for a in accounts for k in a["spent_by"]}}}
    (BASE/args.output).write_text(json.dumps(result,indent=2,sort_keys=True)+"\n")
    print(json.dumps({s:{"fp_p50":v["percentiles"]["fp"]["50"],
                         "defies_p50":v["percentiles"]["defies"]["50"],
                         "completions_p50":v["percentiles"]["completions"]["50"],
                         "item_stop_rate":v["item_stop_rate"]} for s,v in result["strategies"].items()},indent=2))

if __name__=="__main__": main()
