"""Execute reproducible H4 batches; refusing accidental result overwrite."""
import argparse,csv,hashlib,json,statistics,time
from collections import Counter
from pathlib import Path
from model import Engine,CONFIG,HERE,ROOT,verify_inputs

def quantile(values,fraction):
    ordered=sorted(values);return ordered[round((len(ordered)-1)*fraction)]

def main():
    parser=argparse.ArgumentParser();parser.add_argument('--accounts',type=int,default=CONFIG['accounts_per_scenario']);parser.add_argument('--output',required=True)
    args=parser.parse_args();assert args.accounts>0
    output=Path(args.output);csvpath=output.with_suffix('.csv');assert not output.exists() and not csvpath.exists(), 'Choose a new output version; no overwrite'
    verify_inputs();start=time.monotonic();summaries={};raw=[]
    for scenario in CONFIG['scenarios']:
        rows=[]
        for n in range(args.accounts):
            seed=CONFIG['seed']+n;r=Engine(seed,scenario).run();assert r['ledger_fp_identity'] and r['ledger_spin_identity'];rows.append(r)
            raw.append({'scenario':scenario['name'],'seed':seed,'stop':r['stop'],'fp':r['fp'],'hours':round(r['hours'],6),'ordinary':r['ordinary'],'special':r['special'],'audit_violations':r['audit_violations'],'penances':r['penances'],'max_penance_hours':r['max_penance_hours'],'defies':r['defies'],'forced':r['forced'],'empty_recovery':r['empty_recovery'],'debt_events':r['debt_events']})
        metrics={}
        for metric in ['fp','hours','ordinary','special','penances','max_penance_hours','defies','forced','special_share']:
            values=[r[metric] for r in rows];metrics[metric]={'p10':quantile(values,.1),'p50':quantile(values,.5),'p90':quantile(values,.9),'max':max(values),'mean':statistics.mean(values)}
        summaries[scenario['name']]={'accounts':len(rows),'config':scenario,'stops':dict(Counter(r['stop'] for r in rows)),'metrics':metrics,'accounts_with_debt':sum(r['debt_events']>0 for r in rows),'accounts_penance_over_2h':sum(r['max_penance_hours']>2 for r in rows),'accounts_penance_at_least_24h':sum(r['max_penance_hours']>=24 for r in rows),'empty_recoveries':sum(r['empty_recovery'] for r in rows),'mean_spend_by_sink':{key:statistics.mean(r['spent'].get(key,0) for r in rows) for key in sorted({k for r in rows for k in r['spent']})}}
        print(scenario['name'],len(rows),'completed',flush=True)
    output.parent.mkdir(parents=True,exist_ok=True)
    with csvpath.open('w',newline='') as f:
        writer=csv.DictWriter(f,fieldnames=list(raw[0]));writer.writeheader();writer.writerows(raw)
    sources={name:hashlib.sha256((HERE/name).read_bytes()).hexdigest() for name in ['model.py','run.py','checks.py','CONTRACT.md','parameters.json','INPUT_MANIFEST.json']}
    result={'version':'H4-core-v1','scope':'Synthetic recovered-rule core; no full real-content account or Grand completion-time estimate','seed_start':CONFIG['seed'],'seed_end':CONFIG['seed']+args.accounts-1,'accounts_per_scenario':args.accounts,'total_trajectories':len(raw),'elapsed_seconds':time.monotonic()-start,'source_hashes':sources,'input_hashes':json.loads((HERE/'INPUT_MANIFEST.json').read_text())['hashes'],'raw_csv_sha256':hashlib.sha256(csvpath.read_bytes()).hexdigest(),'scenarios':summaries}
    output.write_text(json.dumps(result,indent=2)+'\n');print('TOTAL',len(raw),'SECONDS',round(result['elapsed_seconds'],2))
if __name__=='__main__':main()
