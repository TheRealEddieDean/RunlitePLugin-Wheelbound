"""Focused offline rule checks; does not test RuneLite persistence or gameplay."""
import copy
import json
import random
from pathlib import Path
from model import distinct_draws, simulate, validate

params=json.loads((Path(__file__).parent/"parameters.json").read_text())
wheel=[dict(id=i,kind="ordinary",activity=a,weight=1,tier=0)
       for i,a in enumerate(["Mining","Mining","Fishing","Fishing","Questing"])]
validate(wheel,[],3)
bad=copy.deepcopy(wheel)
bad[-1]["activity"]="Mining"
try:
    validate(bad,[],3)
    raise RuntimeError("invalid two-activity wheel accepted")
except AssertionError:
    pass
full=copy.deepcopy(wheel)+[
    dict(id=i,kind="ordinary",activity="Mining",weight=3,tier=4) for i in range(5,22)]
full+= [dict(id=22,kind="taint",activity="Taint",weight=1),
         dict(id=23,kind="sacrifice",activity="Sacrifice",weight=1)]
validate(full,[],3)
for seed in range(100):
    assert len(set(distinct_draws(random.Random(seed),1,3,3)))==3
for strategy in ("focused","balanced","inefficient"):
    first=simulate(20261009,strategy,params)
    assert first==simulate(20261009,strategy,params)
    assert first["fp"]==params["starting_fp"]+first["income"]-first["spend"]-first["penalties"]
debt=copy.deepcopy(params); debt["starting_fp"]=-1000; debt["events"]=25
out=simulate(10,"focused",debt)
assert out["completions"]>0 and out["income"]>0
print("PASS: diversity, capacity incl specials, distinct offers, seeded replay, ledger identity, FP-debt earnings")

# A dense special wheel at a short reserve exercises empty-item acquisition recovery.
short=copy.deepcopy(params); short["starting_spins"]=1; short["defy_refill"]=1
recovered=[simulate(i,"inefficient",short) for i in range(20)]
assert any(a["acquisition_recoveries"]>0 for a in recovered)
assert all(a["no_eligible_item_stop"]==0 for a in recovered)
assert all(a["recovery_hours"]>0 for a in recovered if a["acquisition_recoveries"])
uncharged=copy.deepcopy(short); uncharged["special_consumes_spin"]=False
assert any(simulate(i,"inefficient",short)["defies"] != simulate(i,"inefficient",uncharged)["defies"] for i in range(20))
print("PASS: acquisition recovery continues progression; special debit changes reserve exhaustion")

# Cross-process hash randomization must not reorder a seeded choice pool.
import os
import subprocess
code = "import json; from model import simulate; print(json.dumps(simulate(20261009,'balanced',json.load(open('parameters.json'))),sort_keys=True))"
outputs=[subprocess.check_output([__import__('sys').executable,'-c',code],cwd=Path(__file__).parent,env={**os.environ,'PYTHONHASHSEED':str(seed)}) for seed in (1,2)]
assert outputs[0]==outputs[1]
print("PASS: reproducible across process hash seeds")
