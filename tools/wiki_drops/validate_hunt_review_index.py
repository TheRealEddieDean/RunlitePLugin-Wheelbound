"""Validate source-backed all-tier display, NOT runtime eligibility."""
import collections,csv,hashlib,json,pathlib,sqlite3,sys
from fractions import Fraction
from challenge_tier_rules_v2 import scalar_rate,tier_for_probability
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
 md=(D/'CHALLENGE_HUNT_TABLE.md').read_text();check(all('## '+name in md for name in ['Lesser Challenge','Standard Challenge','Greater Challenge','Superior Challenge']))
 with (D/'catalogs/challenge_hunt_review_index_v2.csv').open(newline='',encoding='utf-8') as f:rows=list(csv.DictReader(f))
 check(len(rows)==len(r['reviewed_hunts'])+len(r['source_candidates']))
 check(all(row['v2_fp']=='TBD' for row in rows))
 counts=collections.Counter(x['tier'] for x in r['reviewed_hunts']+r['source_candidates']);check(sum(counts.values())==len(rows))
 out={'status':'PASS_SOURCE_DISPLAY_ONLY_NOT_RUNTIME','assertions':checks,'reviewed_v1_hunts':44,'additional_candidate_groups':len(r['source_candidates']),'by_tier_total_display_rows':dict(counts),'unresolved_rows':len(r['unresolved_rate_context_rows']),'table_sha256':hashlib.sha256((D/'CHALLENGE_HUNT_TABLE.md').read_bytes()).hexdigest(),'index_sha256':hashlib.sha256((D/'catalogs/challenge_hunt_review_index_v2.json').read_bytes()).hexdigest(),'new_unlock_or_reward_approval':False}
 (D/'research/CHALLENGE_TABLE_VALIDATION_A65.json').write_text(json.dumps(out,indent=2)+'\n');print(out)
if __name__=='__main__':validate(pathlib.Path(sys.argv[1]))
