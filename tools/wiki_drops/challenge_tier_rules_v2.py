"""Exact per-roll Challenge bracket classification; research/design only.
Does not change gameplay or admit raw collector rows to a roster.
"""
import collections,hashlib,json,pathlib,sys
from fractions import Fraction
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
TIERS=[('lesser','Lesser Challenge',25,50,True),('standard','Standard Challenge',50,125,False),('greater','Greater Challenge',125,750,False),('superior','Superior Challenge',750,5000,False)]
def scalar_rate(r):
    if r['kind']=='fraction':return Fraction(**r['exact_probability'])
    if r['kind']=='multiple_rolls' and not r.get('approximate',False):return Fraction(**r['per_roll_probability'])
    return None

def tier_for_probability(p):
    if p is None or p<=0:return None
    reciprocal=1/p
    for key,_,lo,hi,inclusive in TIERS:
        if (lo<=reciprocal if inclusive else lo<reciprocal) and reciprocal<=hi:return key
    return None

def run(input_dir):
    v1=json.load(open(D/'catalogs/challenge_hunts_v1.json'));raw=json.load(open(input_dir/'catalog.json'));pages=raw['creatures']+raw['unclassified_pages'];mapping=[]
    for h in v1['hunts']:
        record=next(row['raw_record'] for row in h['source_rows'] if row['row_zero_based']==h['reviewed_row_zero_based']);p=scalar_rate(record['rarity']);key=tier_for_probability(p);assert key
        mapping.append({'hunt_id_v1':h['id'],'creature_key':h['creature_key'],'item':h['item_name'],'raw_rate':record['rarity']['raw'],'bracket_probability':{'numerator':p.numerator,'denominator':p.denominator},'reciprocal_denominator':str(1/p),'challenge_tier':key,'ignored_roll_count_for_bracket':record['rarity'].get('roll_count',1),'v1_personal_probability_preserved':h['personal_probability'],'v1_fp_preserved_for_history_only':h['fp_reward'],'v2_reward_status':'NOT_YET_REQUOTED; tier name is not a reward multiplier','runtime_enabled':False})
    # New easier-tier source leads, not automatic catalog admission.
    leads=[]
    for source,item in [('Scurrius',"Scurrius' spine"),('Hespori','Bottomless compost bucket'),('Corporeal Beast','Spirit shield')]:
        page=next(p for p in pages if p['name']==source)
        for i,row in enumerate(page['drops']):
            if row['item_name']!=item:continue
            p=scalar_rate(row['rarity']);leads.append({'source':source,'item':item,'source_url':page['source_url'],'row_zero_based':i,'source_sha256':page['source_sha256'],'fetched_at':page['capture']['fetched_at'],'raw_record':row,'challenge_tier':tier_for_probability(p),'rate_for_display':str(p),'status':'SOURCE_LEAD_ONLY_ACCESS_CONTEXT_RECEIPT_REWARD_NOT_ADMITTED','runtime_enabled':False})
    counts=collections.Counter()
    for p in pages:
        for row in p['drops']:
            key=tier_for_probability(scalar_rate(row['rarity']));counts[key or 'not_scalar_or_outside']=counts[key or 'not_scalar_or_outside']+1
    result={'version':'challenge-tier-rules-v2-2026-10-14','authority':'WB-D144 explicit user brackets/per-roll policy; WB-D145 delegated boundary convention','supersedes':'Single1/400–1/1500 future Challenge eligibility bracket and encounter-aggregate classification; v1 data/quotes remain historical.','runtime_enabled':False,'tier_metric':'Unmultiplied reported specific-item per-roll probability in reviewed source/personal context; reciprocal denominator handles non-unit fractions.','tiers':[{'key':key,'display_name':name,'reciprocal_min':lo,'reciprocal_max':hi,'min_inclusive':inc,'max_inclusive':True} for key,name,lo,hi,inc in TIERS],'raw_rolls_preserved':True,'conditional_policy':'Do not choose a favorable semicolon/task/group alternative. Review explicit context first; unresolved/approximate expressions stay held. Personal fresh-source completion remains mandatory.','personal_encounter_probability':'Keep separately for truthful expected effort/reward estimates; it does not choose the bracket under the new user rule. Never label per-roll as exact per-kill probability.','unlock_structure_proposal':{'status':'PROPOSED_NOT_ADOPTED','recommended':'Progressive Lesser → Standard → Greater → Superior separate permanent card unlocks; exact prerequisites/prices and inherited old entry gate pending answer.','alternative':'Independent tier purchases after a common entry gate.','control_recommendation':'Retain lower tiers; opt in/out of owned tiers between assignments before new offers. Exact tier visible before target draw.','offer_integration':'Existing two/three offer slots, type frequencies and any generic-Challenge tier picker are not changed without a separate settled contract.','minimum_creatures':'Existing≥3 actual target diversity rule preserved; application to each tier needs concrete pool review, not automatic underfloor offers.'},'v1_retier_counts':dict(collections.Counter(x['challenge_tier'] for x in mapping)),'existing_hunt_mapping':mapping,'new_lower_tier_source_leads_NOT_ADMITTED':leads,'corpus_row_classification_counts_NOT_ROSTER_COUNTS':dict(counts),'v1_catalog_sha256':hashlib.sha256((D/'catalogs/challenge_hunts_v1.json').read_bytes()).hexdigest(),'v2_catalog_status':'Tier rule and migration tables complete; expanded four-tier roster/rewards/unlock integration not yet authored.'}
    (D/'catalogs/challenge_tier_rules_v2.json').write_text(json.dumps(result,indent=2,ensure_ascii=False)+'\n')
    return result
if __name__=='__main__':
 r=run(pathlib.Path(sys.argv[1]));print('Existing44:',r['v1_retier_counts']);print('Easier source leads:',[(x['source'],x['item'],x['rate_for_display'],x['challenge_tier']) for x in r['new_lower_tier_source_leads_NOT_ADMITTED']])
