"""Validate future test-plan coverage; this does not run client acceptance."""
from copy import deepcopy
import json
from build_core_trace_plan import build, validate, D, OUTPUT, CORE

core=json.loads((D/CORE).read_text());plan=build()
assert plan==json.loads((D/OUTPUT).read_text())
mutations=[]
p=deepcopy(plan);p['runtime_enabled']=True;mutations.append(p)
p=deepcopy(plan);p['client_tests_executed']=1;mutations.append(p)
p=deepcopy(plan);p['cases'][0]['client_status']='PASS';mutations.append(p)
p=deepcopy(plan);p['cases'][0]['observed_trace']='invented';mutations.append(p)
p=deepcopy(plan);p['cases'][0]['item_ids']=[9013];mutations.append(p)
p=deepcopy(plan);p['cases'][0]['frozen_fp']+=25;mutations.append(p)
p=deepcopy(plan);p['cases'][0]['frozen_quote_version']='live-latest';mutations.append(p)
p=deepcopy(plan);p['source_matrix'][0]['composition_leads']+=[999999];mutations.append(p)
p=deepcopy(plan);p['sources'][0]['sha256']='0'*64;mutations.append(p)
p=deepcopy(plan);p['cases'].pop(0);p['case_count']-=1;mutations.append(p)
p=deepcopy(plan);p['cases'].append(deepcopy(p['cases'][0]));p['case_count']+=1;mutations.append(p)
p=deepcopy(plan);p['source_matrix'].pop();mutations.append(p)
p=deepcopy(plan);p['cases']=[c for c in p['cases'] if c['id']!='WB-CORE-water-young-held'];p['case_count']-=1;mutations.append(p)
p=deepcopy(plan);p['derived_fields_are_not_promised_api_fields']=False;mutations.append(p)
for p in mutations:
    try:validate(p,core)
    except (ValueError,KeyError):pass
    else:raise AssertionError('invalid client-plan mutation accepted')
print(json.dumps({'status':'PASS_OFFLINE_PLAN_COVERAGE_NOT_CLIENT_EXECUTION','case_count':plan['case_count'],'invalid_mutations_rejected':len(mutations),'client_tests':'NOT_RUN'}))
