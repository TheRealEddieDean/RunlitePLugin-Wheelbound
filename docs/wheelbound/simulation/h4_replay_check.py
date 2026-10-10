"""Cross-process verification of saved H4 results, never new balance trajectories."""
import argparse,csv,hashlib,json,os,subprocess,sys,tempfile
from pathlib import Path
HERE=Path(__file__).resolve().parent
H4=HERE/'h4'
FIELDS=['stop','fp','ordinary','special','audit_violations','penances','max_penance_hours','defies','forced','empty_recovery','debt_events']
def worker():
 sys.path.insert(0,str(H4));from model import Engine,CONFIG,verify_inputs
 verify_inputs();requests=json.loads(sys.stdin.read());out=[]
 for q in requests:
  scenario=next(x for x in CONFIG['scenarios'] if x['name']==q['scenario'])
  e=Engine(q['seed'],scenario);result=e.run()
  out.append({'scenario':q['scenario'],'seed':q['seed'],'metrics':{k:result[k] for k in FIELDS},'hours':round(result['hours'],6),'ledger_sha256':hashlib.sha256(json.dumps(e.ledger,sort_keys=True,separators=(',',':')).encode()).hexdigest(),'final_snapshot_sha256':hashlib.sha256(e.snapshot().encode()).hexdigest()})
 return out
def main():
 p=argparse.ArgumentParser();p.add_argument('--worker',action='store_true');p.add_argument('--output');a=p.parse_args()
 if a.worker:print(json.dumps(worker(),sort_keys=True));return
 assert a.output,'Choose a new report path';output=Path(a.output);assert not output.exists(),'Replay report overwrite forbidden'
 baseline=json.loads((H4/'results_v1.json').read_text())
 for name,sha in baseline['source_hashes'].items():assert hashlib.sha256((H4/name).read_bytes()).hexdigest()==sha
 rows=list(csv.DictReader((H4/'results_v1.csv').open()));assert len(rows)==4000
 assert hashlib.sha256((H4/'results_v1.csv').read_bytes()).hexdigest()==baseline['raw_csv_sha256']
 selected={}
 for scenario in baseline['scenarios']:
  r=[x for x in rows if x['scenario']==scenario]
  targets=r[:3]+[max(r,key=lambda x:float(x['max_penance_hours'])),min(r,key=lambda x:float(x['fp']))]+[x for x in r if x['stop']!='DRAW_HORIZON']
  for x in targets:selected[(scenario,int(x['seed']))]=x
 requests=[{'scenario':s,'seed':seed} for s,seed in sorted(selected)];payload=json.dumps(requests)
 runs=[]
 for hashseed in ['1','77','20261010','random']:
  env=os.environ.copy();env['PYTHONHASHSEED']=hashseed
  proc=subprocess.run([sys.executable,str(Path(__file__).resolve()),'--worker'],input=payload,text=True,capture_output=True,env=env,check=True,timeout=60)
  result=json.loads(proc.stdout);assert len(result)==len(requests)
  for q in result:
   original=selected[(q['scenario'],q['seed'])]
   for k,value in q['metrics'].items():assert str(value)==original[k],(q['scenario'],q['seed'],k,value,original[k])
   assert q['hours']==float(original['hours'])
  runs.append({'hash_seed':hashseed,'result_sha256':hashlib.sha256(json.dumps(result,sort_keys=True,separators=(',',':')).encode()).hexdigest(),'result':result})
 assert all(x['result']==runs[0]['result'] for x in runs),'Hash-seed dependent result/ledger/final snapshot'
 report={'status':'PASS','scope':'Replay verification of existing saved baseline, not new independent balance trajectories','baseline_sha256':hashlib.sha256((H4/'results_v1.json').read_bytes()).hexdigest(),'csv_sha256':baseline['raw_csv_sha256'],'checker_sha256':hashlib.sha256(Path(__file__).read_bytes()).hexdigest(),'cases':requests,'cases_per_process':len(requests),'processes':len(runs),'worker_replays':len(requests)*len(runs),'hash_seed_checks':[{k:v for k,v in x.items() if k!='result'} for x in runs],'verified':['exact saved per-seed CSV metrics','ledger identity assertions from Engine.run','same ledger hash across processes','same final frozen snapshot hash across processes','exhaustion/pending/severity-tail cases preserved'],'live_client_validation':False,'changes_to_model_or_inputs':False}
 output.write_text(json.dumps(report,indent=2)+'\n');print(json.dumps({k:report[k] for k in ['status','cases_per_process','processes','worker_replays']}))
if __name__=='__main__':main()
