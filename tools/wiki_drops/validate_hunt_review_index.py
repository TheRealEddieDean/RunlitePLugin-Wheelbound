"""Validate source-backed all-tier display, NOT runtime eligibility."""
import collections,csv,hashlib,json,pathlib,sqlite3,sys
from fractions import Fraction
from challenge_tier_rules_v2 import scalar_rate,tier_for_probability
from render_split_hunt_tables import TABLES,esc
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
def validate(input_dir):
 r=json.load(open(D/'catalogs/challenge_hunt_review_index_v2.json'));v1=json.load(open(D/'catalogs/challenge_hunts_v1.json'));raw=json.load(open(input_dir/'catalog.json'));by={p['source_url']:p for p in raw['creatures']+raw['unclassified_pages']};db=sqlite3.connect('file:'+str((input_dir/'drops.sqlite').resolve())+'?mode=ro',uri=True);checks=0
 def check(c):
  nonlocal checks
  assert c;checks+=1
 check(set(x['hunt_id_v1'] for x in r['reviewed_hunts'])==set(x['id'] for x in v1['hunts']))
 check(len(r['reviewed_hunts'])==44)
 check(hashlib.sha256((D/'catalogs/challenge_hunts_v1.json').read_bytes()).hexdigest()==r['v1_catalog_sha256'])
 keys=set();new_raw=0
 for g in r['source_candidates']:
  key=(g['source_url'],g['item'],g['base_rate'],g['tier']);check(key not in keys);keys.add(key);check(g['v2_fp'] is None and not g['runtime_enabled'])
  check(tier_for_probability(Fraction(g['base_rate']))==g['tier'])
  for row in g['source_rows']:
   new_raw+=1;original=by[row['source_url']]['drops'][row['source_row_zero_based']];stored=json.loads(db.execute('select payload_json from drops where source_url=? and row_number=?',(row['source_url'],row['source_row_zero_based'])).fetchone()[0]);check(original==stored==row['raw_record']);check(scalar_rate(original['rarity'])==Fraction(g['base_rate']));check(row['source_sha256']==by[row['source_url']]['source_sha256'])
 for row in r['unresolved_rate_context_rows']:
  original=by[row['source_url']]['drops'][row['source_row_zero_based']];check(original==row['raw_record']);check(scalar_rate(original['rarity']) is None)
 check(new_raw+len(r['unresolved_rate_context_rows'])+r['outside_brackets_named_source_rows']==r['named_unreviewed_source_rows_screened'])
 tables={kind:(D/name).read_text() for kind,name in TABLES.items()}
 for md in tables.values():check(all('## '+name in md for name in ['Lesser Challenge','Standard Challenge','Greater Challenge','Superior Challenge']))
 taxonomy=json.load(open(D/'research/CHALLENGE_TAXONOMY_A60.json'));correction={x[1]:x[3] for x in taxonomy['corrections']}
 for row in r['reviewed_hunts']+r['source_candidates']+r['unresolved_rate_context_rows']:
  page=by[row['source_url']];kind=correction.get(row['source_url'].split('/w/',1)[1],page['classification'].upper());check(row['source_kind']==kind)
  prefix='| '+esc(row['creature'])+' | '+esc(row['item'])+' |'
  check(prefix in tables[kind]);check(prefix not in tables['MONSTER' if kind=='BOSS' else 'BOSS'])
 check(not (D/'CHALLENGE_HUNT_TABLE.md').exists() and not (D/'CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md').exists())
 with (D/'catalogs/challenge_hunt_review_index_v2.csv').open(newline='',encoding='utf-8') as f:rows=list(csv.DictReader(f))
 check(len(rows)==len(r['reviewed_hunts'])+len(r['source_candidates']))
 check(all(row['v2_fp']=='TBD' for row in rows))
 counts=collections.Counter(x['tier'] for x in r['reviewed_hunts']+r['source_candidates']);check(sum(counts.values())==len(rows))
 audit=json.load(open(D/'research/MONSTER_DROP_AUDIT_A67.json'))
 expected_monsters={url:p for url,p in by.items() if correction.get(url.split('/w/',1)[-1],p['classification'].upper())=='MONSTER'}
 check({p['source_url'] for p in audit['pages']}==set(expected_monsters))
 check({x['item'] for x in audit['item_decisions']}=={d['item_name'] for p in expected_monsters.values() for d in p['drops']})
 dispositions=collections.Counter();expected_candidates=set();expected_held=set()
 for page in audit['pages']:
  all_rows=[i for indices in page['row_dispositions'].values() for i in indices]
  check(sorted(all_rows)==list(range(len(expected_monsters[page['source_url']]['drops']))))
  for status,indices in page['row_dispositions'].items():
   dispositions[status]+=len(indices)
   if status=='CANDIDATE_FIXED_TIER':expected_candidates.update((page['source_url'],i) for i in indices)
   if status=='HELD_RATE_CONTEXT':expected_held.update((page['source_url'],i) for i in indices)
 check(dict(dispositions)==audit['disposition_counts'])
 check(sum(dispositions.values())==audit['drop_rows']==31757)
 actual_candidates={(z['source_url'],z['source_row_zero_based']) for g in r['source_candidates'] if g['source_kind']=='MONSTER' for z in g['source_rows']}
 actual_held={(z['source_url'],z['source_row_zero_based']) for z in r['unresolved_rate_context_rows'] if z['source_kind']=='MONSTER'}
 check(actual_candidates==expected_candidates);check(actual_held==expected_held)
 for name,item in [('Killerwatt','Air diamond'),('Molanisk','Earth emerald'),('Infernal Mage','Fire ruby'),('Sea Snake Hatchling','Water sapphire')]:
  check(any(g['creature']==name and g['item']==item and g['base_rate']=='1/64' and g['tier']=='standard' for g in r['source_candidates']))
 check(not any('Bloodthirst' in g['creature'] for g in r['source_candidates'] if g['source_kind']=='MONSTER'))
 check(not any('Superior Slayer tertiary' in z['raw_record']['section'] and g['item'] in ['Dust battlestaff','Mist battlestaff'] for g in r['source_candidates'] if g['source_kind']=='MONSTER' for z in g['source_rows']))
 out={'monster_audit_row_conservation':'PASS','monster_audit_source_cross_checks':audit['source_cross_checks'],'status':'PASS_SOURCE_DISPLAY_ONLY_NOT_RUNTIME','assertions':checks,'reviewed_v1_hunts':44,'additional_candidate_groups':len(r['source_candidates']),'by_tier_total_display_rows':dict(counts),'unresolved_rows':len(r['unresolved_rate_context_rows']),'table_sha256_by_kind':{kind:hashlib.sha256((D/name).read_bytes()).hexdigest() for kind,name in TABLES.items()},'by_source_kind':r['counts']['by_source_kind'],'index_sha256':hashlib.sha256((D/'catalogs/challenge_hunt_review_index_v2.json').read_bytes()).hexdigest(),'new_unlock_or_reward_approval':False}
 (D/'research/CHALLENGE_TABLE_VALIDATION_A67.json').write_text(json.dumps(out,indent=2)+'\n');print(out)
if __name__=='__main__':validate(pathlib.Path(sys.argv[1]))
