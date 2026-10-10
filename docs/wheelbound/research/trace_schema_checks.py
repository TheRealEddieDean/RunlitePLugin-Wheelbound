import importlib.util,json,copy
from jsonschema import ValidationError
from pathlib import Path
p=Path(__file__).resolve().parent/'validate_trace.py';s=importlib.util.spec_from_file_location('trace_validator',p);m=importlib.util.module_from_spec(s);s.loader.exec_module(m)
a={'schema_version':1,'case_id':'WB-TC-C01','origin':'SYNTHETIC','captured_at_utc':'2026-10-10T15:00:00Z','source_commit':'e6d445c8c27847050abb07f843cfe9c9d4ab476b','client_version':None,'game_revision':None,'capture_adapter_sha256':None,'account_alias':'account-example','account_mode':'UNKNOWN','policy_gate':'OBSERVE_ONLY','events':[{'sequence':0,'monotonic_ms':0,'session_epoch':0,'run_state':'ACTIVE','event':'ExampleOnly','evidence_kind':'SYNTHETIC','payload':{'snapshot_initialized':False}}],'limitations':['Synthetic schema fixture only; no client observations'],'assessment':'REVIEW_REQUIRED'}
assert m.validate(a,True)['runtime_enabled'] is False
bad=[];bad.append((a,False,'synthetic not live'))
b=copy.deepcopy(a);b['events'][0]['sequence']=1;bad.append((b,True,'missing sequence'))
b=copy.deepcopy(a);b['events'][0]['payload']['username']='not-allowed';bad.append((b,True,'unapproved identity field'))
b=copy.deepcopy(a);b['origin']='LIVE_CAPTURE';bad.append((b,True,'missing live client/version evidence'))
b=copy.deepcopy(a);b['events'].append(copy.deepcopy(b['events'][0]));bad.append((b,True,'duplicate callback sequence'))
b=copy.deepcopy(a);b['captured_at_utc']='2026-10-10T09:00:00-06:00';bad.append((b,True,'non-UTC envelope'))
for b,allow,label in bad:
 try:m.validate(b,allow)
 except (ValueError,ValidationError):continue
 raise AssertionError(label)
(Path(__file__).resolve().parent/'TRACE_SCHEMA_CHECKS.json').write_text(json.dumps({'status':'PASS','scope':'Offline schema/order/origin/privacy-field fixtures only','positive_synthetic_envelope':1,'negative_fixtures':6,'live_trace_count':0,'runtime_enabled':False,'negative_cases':[x[2] for x in bad]},indent=2)+'\n')
print('PASS: planned capture schema; synthetic-origin gate and six invalid envelope fixtures; no live traces')
