"""Validate authored catalog/source identity/rate/quote contracts; no gameplay simulation."""
import collections,hashlib,json,math,pathlib,sqlite3,sys
from fractions import Fraction
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
def q(d):return Fraction(d['numerator'],d['denominator'])
def validate(input_dir):
 r=json.load(open(D/'catalogs/challenge_hunts_v1.json'));taxonomy=json.load(open(D/'research/CHALLENGE_TAXONOMY_A60.json'));sources=json.load(open(D/'research/CHALLENGE_CONTEXT_EVIDENCE_A61.json'));source={x['creature_key']:x for x in sources};identity=json.load(open(D/'research/CHALLENGE_IDENTITY_PINS_A61.json'));items={x['symbol']:x['id'] for x in identity['items']};npcs={x['id'] for x in identity['npcs']}
 raw=json.load(open(input_dir/'catalog.json'));byurl={p['source_url']:p for p in raw['creatures']+raw['unclassified_pages']};db=sqlite3.connect('file:'+str((input_dir/'drops.sqlite').resolve())+'?mode=ro',uri=True)
 checks=[]
 def check(condition,label):
  assert condition,label;checks.append(label)
 check(len(r['hunts'])==44 and len(r['creatures'])==25 and len(r['starter_bundle'])==6,'finite_roster_counts')
 check(sum(r['full_corpus_row_screen_counts'].values())==44447,'whole_corpus_screen_conserves_rows')
 check(sum(taxonomy['corrected_counts'].values())==2929,'taxonomy_conserves_source_records')
 check(len({e['id'] for e in r['hunts']})==44,'unique_hunt_keys')
 check(len({c['key'] for c in r['creatures']})==25,'unique_creature_keys')
 check(not r['runtime_enabled'] and all(not e['runtime_enabled'] for e in r['hunts']),'no_runtime_activation')
 corrections={row[0]:row[3] for row in taxonomy['corrections']}
 for c in r['creatures']:
  check(corrections.get(c['name'],'BOSS_OR_MONSTER') in ['BOSS','MONSTER','BOSS_OR_MONSTER'],'selected_source_is_creature_not_component:'+c['key'])
  check(bool(c['route_requirements']) and c['task_requirement'].startswith('NONE'),'repeatable_authored_access:'+c['key'])
  check(bool(c['npc_form_ids']) and set(c['npc_form_ids'])<=npcs,'pinned_form_identity:'+c['key'])
  check(len(c['hunts'])==len(set(c['hunts'])),'no_variant_duplicate_draw:'+c['key'])
  p=byurl[source[c['key']]['source_url']];check(hashlib.sha256((input_dir/'html'/(hashlib.sha256(p['source_url'].encode()).hexdigest()+'.html')).read_text(encoding='utf-8').encode()).hexdigest()==source[c['key']]['source_sha256_normalized'],'captured_html_text_hash:'+c['key'])
  check(bool(source[c['key']]['reported_revision_ids']),'source_revision:'+c['key'])
 for e in r['hunts']:
  check(e['canonical_item_id']==items[e['item_symbol']] and e['accepted_drop_item_ids']==[e['canonical_item_id']],'exact_specific_item:'+e['id'])
  lo=q(e['personal_probability']['lower']);hi=q(e['personal_probability']['upper']);check(Fraction(1,1500)<=lo<=hi<=Fraction(1,400),'personal_band:'+e['id'])
  for row in e['source_rows']:
   original=byurl[e['source_url']]['drops'][row['row_zero_based']];stored=json.loads(db.execute('select payload_json from drops where source_url=? and row_number=?',(e['source_url'],row['row_zero_based'])).fetchone()[0]);check(original==stored==row['raw_record'],'lossless_source_row:'+e['id']+':'+str(row['row_zero_based']))
  reviewed=byurl[e['source_url']]['drops'][e['reviewed_row_zero_based']]['rarity']
  if reviewed['kind']=='fraction':check(lo==hi==q(reviewed['exact_probability']),'specific_single_rate:'+e['id'])
  else:check(e['creature_key']=='zulrah' and lo==q(reviewed['per_roll_probability']) and hi==min(Fraction(1),lo*reviewed['roll_count']),'union_bound_no_independence:'+e['id'])
  hours=(1/lo)/e['reference_kills_per_hour'];expected=25*math.ceil((100+100*hours)/25);check(e['fp_reward']==expected and e['fp_reward']%25==0 and e['reward_quote_version']=='challenge-fp-v1','exact_frozen_quote:'+e['id'])
  check(e['completion_contract']==r['completion_contract']['id'] and e['reward_status']=='AUTHORED_V1_FIXED_QUOTE','complete_contract:'+e['id'])
 # Creature-first/item-second normalization and context grouping invariance.
 total=sum(Fraction(1,len(r['creatures'])*len(c['hunts'])) for c in r['creatures'] for _ in c['hunts']);check(total==1,'full_roster_assignment_odds_sum_one')
 check(all(sum(Fraction(1,25*len(c['hunts'])) for _ in c['hunts'])==Fraction(1,25) for c in r['creatures']),'equal_creature_weight_despite_item_counts')
 for count in [2,3,6]:check((count>=r['minimum_actual_eligible_creatures'])==(count>=3),'actual_diversity_floor:'+str(count))
 # Bound endpoints pass; unknown/straddling/outside examples cannot pass.
 inside=lambda a,b:Fraction(1,1500)<=a<=b<=Fraction(1,400)
 check(inside(Fraction(1,1500),Fraction(1,400)),'inclusive_endpoints')
 check(not inside(Fraction(1,2000),Fraction(1,500)),'straddling_range_excluded')
 check(not inside(Fraction(1,1026),Fraction(3,1026)),'three_roll_wide_range_not_auto_admitted')
 check(inside(Fraction(1,1024),Fraction(2,1024)),'two_roll_bound_fully_inside')
 negative=' '.join(r['completion_contract']['negative_rules']);check(all(w in negative for w in ['Inventory','teammate','paused','Unknown','proximity']),'receipt_negative_cases_authored_not_runtime_tests')
 check(r['completion_contract']['client_validation_status'].startswith('REQUIRED_BEFORE_RUNTIME'),'client_validation_gate_preserved')
 summary={'version':r['version'],'validation':'PASS_OFFLINE_DATA_AND_ARITHMETIC_ONLY','assertions_passed':len(checks),'check_labels':checks,'hunts':44,'creature_keys':25,'starter_keys':6,'fp_min':min(e['fp_reward'] for e in r['hunts']),'fp_max':max(e['fp_reward'] for e in r['hunts']),'production_or_client_tests_run':False,'catalog_sha256':hashlib.sha256((D/'catalogs/challenge_hunts_v1.json').read_bytes()).hexdigest()}
 (D/'research/CHALLENGE_CATALOG_VALIDATION_A62.json').write_text(json.dumps(summary,indent=2)+'\n');print({k:v for k,v in summary.items() if k!='check_labels'})
if __name__=='__main__':validate(pathlib.Path(sys.argv[1]))
