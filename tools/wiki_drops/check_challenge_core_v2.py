"""Source/rank/quote/dedup regression checks for the finite design core."""
from copy import deepcopy
import json
from pathlib import Path
from build_challenge_core_v2 import compile_core, validate_core, render, D

core=compile_core();stored=json.loads((D/'catalogs/challenge_core_v2_draft.json').read_text())
assert core==stored
assert (D/'CHALLENGE_CORE_V2.md').read_text()==render(core)
mutations=[]
c=deepcopy(core);c['runtime_enabled']=True;mutations.append(c)
c=deepcopy(core);c['runtime_ready']=True;mutations.append(c)
c=deepcopy(core);c['creatures'].append(deepcopy(c['creatures'][0]));mutations.append(c)
c=deepcopy(core);c['hunts'].append(deepcopy(c['hunts'][0]));mutations.append(c)
c=deepcopy(core);h=deepcopy(c['hunts'][0]);h['id']='duplicate-new-id';c['hunts'].append(h);mutations.append(c)
c=deepcopy(core);c['hunts'][0]['rank']='superior';mutations.append(c)
c=deepcopy(core);c['hunts'][0]['fp_reward']+=25;mutations.append(c)
c=deepcopy(core);c['hunts'][0]['canonical_item_id']=9013;mutations.append(c)
c=deepcopy(core);c['hunts'][0]['accepted_drop_item_ids'].append(9013);mutations.append(c)
c=deepcopy(core);c['sources'][0]['sha256']='0'*64;mutations.append(c)
c=deepcopy(core);c['counts']['keys_per_rank']['lesser']=100;mutations.append(c)
c=deepcopy(core);c['hunts'][0]['creature_key']='name-only-new-source';mutations.append(c)
for mutation in mutations:
    try:validate_core(mutation)
    except (ValueError,KeyError):pass
    else:raise AssertionError('invalid core accepted')
print(json.dumps({'status':'PASS_OFFLINE_CORE_SOURCE_REPRODUCTION_AND_MUTATION_REJECTION','canonical_keys':14,'hunts':16,'invalid_mutations_rejected':len(mutations),'runtime_tests':'NOT_RUN'}))
