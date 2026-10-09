"""Three scenario sweeps, 4000 accounts per strategy per scenario by default."""
import argparse
import copy
import hashlib
import json
from pathlib import Path
from model import simulate, percentiles

base=Path(__file__).parent
parser=argparse.ArgumentParser()
parser.add_argument("--accounts-per-strategy",type=int,default=4000)
args=parser.parse_args()
params=json.loads((base/"parameters.json").read_text())
variants={}
low=copy.deepcopy(params); low["starting_spins"]=25; low["defy_refill"]=25
variants["low_reserve"]=low
slow=copy.deepcopy(params); slow["acquisition_hours"]=[3,9]
variants["slow_acquisition"]=slow
prices=copy.deepcopy(params)
for key in ("t1_unlock","t2_unlock","ge","vendor_base","cleansing","pardon","deactivate","reactivate"):
    prices[key]*=2
variants["higher_selected_prices"]=prices
output={"accounts_per_strategy":args.accounts_per_strategy,
        "model_sha256":hashlib.sha256((base/"model.py").read_bytes()).hexdigest(),"scenarios":{}}
for scenario,config in variants.items():
    groups={}
    for index,strategy in enumerate(("focused","balanced","inefficient")):
        # Matched seed streams enable paired scenario comparisons.
        accounts=[simulate(params["seed"]+index*1_000_000+i,strategy,config)
                  for i in range(args.accounts_per_strategy)]
        groups[strategy]={
            "fp":percentiles([a["fp"] for a in accounts]),
            "hours":percentiles([a["hours"] for a in accounts]),
            "defies":percentiles([a["defies"] for a in accounts]),
            "completions":percentiles([a["completions"] for a in accounts]),
            "special_share":percentiles([a["special_share"] for a in accounts]),
            "acquisition_rate":sum(a["acquisition_recoveries"]>0 for a in accounts)/len(accounts),
            "recovery_hours":percentiles([a["recovery_hours"] for a in accounts]),
            "item_stop_rate":sum(a["no_eligible_item_stop"]>0 for a in accounts)/len(accounts),
            "negative_rate":sum(a["negative_events"]>0 for a in accounts)/len(accounts),
        }
    output["scenarios"][scenario]={"parameters":config,"strategies":groups}
    print(scenario, json.dumps({s:round(v["item_stop_rate"],4) for s,v in groups.items()}),flush=True)
(base/"recovery_sensitivity_results.json").write_text(json.dumps(output,indent=2,sort_keys=True)+"\n")
