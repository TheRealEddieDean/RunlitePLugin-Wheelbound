"""Render all reviewed hunts and named unique-item source candidates by current tier.
Raw output is not a roster. No gameplay activation or new unlock/reward adoption.
python tools/wiki_drops/build_hunt_table_by_tier.py PATH_TO_EXTRACTED_OUTPUT
"""
import collections,csv,hashlib,json,pathlib,re,sys
from fractions import Fraction
from challenge_tier_rules_v2 import scalar_rate,tier_for_probability,TIERS
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
# Explicit review scope: signature equipment/components, not a filter admitting all generic loot.
# Inclusion here is a research lead, NOT final uniqueness/access/receipt/reward approval.
CANDIDATE_NAMES='''Abyssal whip|Abyssal dagger|Ancient hilt|Ancient icon|Antler guard|Aranea boots|Araxyte fang|Arcane sigil|Archers ring|Armadyl chainskirt|Armadyl chestplate|Armadyl crossbow|Armadyl helmet|Armadyl hilt|Ballista limbs|Ballista spring|Bandos boots|Bandos chestplate|Bandos hilt|Bandos tassets|Basilisk jaw|Bellator vestige|Berserker ring|Black mask (10)|Blood shard|Bottomless compost bucket|Brine sabre|Broken zombie axe|Burning claw|Chromium ingot|Claws of Callisto|Crimson kisten|Crystal tool seed|Dark bow|Draconic visage|Dragon 2h sword|Dragon axe|Dragon boots|Dragon chainbody|Dragon defender|Dragon harpoon|Dragon hunter wand|Dragon limbs|Dragon metal lump|Dragon metal sheet|Dragon metal slice|Dragon pickaxe|Dragon sword|Dragon warhammer|Dragonbone necklace|Drake's claw|Drake's tooth|Earthbound tecpatl|Eternal crystal|Elder chaos hood|Elder chaos robe|Elder chaos top|Elder venator fang|Eldritch orb|Elysian sigil|Executioner's axe head|Eye of the Duke|Fang of the hound|Fangs of Venenatis|Fire element staff crown|Frozen tear|Glacial temotli|Granite boots|Granite helm|Granite legs|Granite longsword|Granite maul|Hallowfell|Harmonised orb|Heavy frame|Holy elixir|Horn of Plenty (empty)|Hueycoatl hide|Hydra leather|Hydra tail|Hydra's claw|Ice element staff crown|Inquisitor's great helm|Inquisitor's hauberk|Inquisitor's mace|Inquisitor's plateskirt|Kraken tentacle|Leaf-bladed battleaxe|Leaf-bladed sword|Leviathan's lure|Light frame|Malediction shard 1|Malediction shard 2|Malediction shard 3|Mask of Ranul|Mud battlestaff|Necklace of Fangs|Nightmare staff|Nihil horn|Noxious blade|Noxious point|Noxious pommel|Oathplate chest|Oathplate helm|Oathplate legs|Occult necklace|Odium shard 1|Odium shard 2|Odium shard 3|Pegasian crystal|Pendant of Ates (inert)|Primordial crystal|Ring of the gods|Sarachnis cudgel|Saradomin hilt|Saradomin sword|Saradomin's light|Scurrius' spine|Seercull|Seers ring|Serpentine visage|Siren's staff|Skeletal visage|Skull of Vet'ion|Smoke battlestaff|Smouldering stone|Soulflame horn|Spectral sigil|Spirit shield|Staff of the Dead|Steam battlestaff|Sulphur blades|Tanzanite fang|Magic fang|Tome of Earth (empty)|Tormented synapse|Torva full helm (damaged)|Torva platebody (damaged)|Torva platelegs (damaged)|Treasonous ring|Trident of the Seas (full)|Tyrannical ring|Ultor vestige|Uncharged trident|Unsired|Venator fang|Venator shard|Venator tooth|Venator vestige|Virtus mask|Virtus robe bottom|Virtus robe top|Voidwaker blade|Voidwaker gem|Voidwaker hilt|Volatile orb|Warrior ring|Warped sceptre (uncharged)|Zamorak hilt|Zamorakian spear|Zaryte vambraces|Zenyte shard'''.split('|')
def escape(x):return str(x).replace('|','\\|').replace('\n',' ')
def render(input_dir):
 raw=json.load(open(input_dir/'catalog.json'));pages=raw['creatures']+raw['unclassified_pages'];byurl={p['source_url']:p for p in pages};old=json.load(open(D/'catalogs/challenge_hunts_v1.json'));rules=json.load(open(D/'catalogs/challenge_tier_rules_v2.json'));oldhunt={h['id']:h for h in old['hunts']};creatures={c['key']:c for c in old['creatures']};taxonomy=json.load(open(D/'research/CHALLENGE_TAXONOMY_A60.json'));correction={x[1]:x[3] for x in taxonomy['corrections']};reviewed=[];oldpairs=set()
 for m in rules['existing_hunt_mapping']:
  h=oldhunt[m['hunt_id_v1']];oldpairs.add((h['source_url'],h['item_name']));reviewed.append({'status':'V1_REVIEWED_CONTEXT_RECLASSIFIED_V2_NOT_ADMITTED','tier':m['challenge_tier'],'creature':creatures[h['creature_key']]['name'],'item':h['item_name'],'source_url':h['source_url'],'base_rate':str(Fraction(**m['bracket_probability'])),'raw_rates':[m['raw_rate']],'historical_v1_fp':h['fp_reward'],'v2_fp':None,'hunt_id_v1':h['id'],'source_rows':[h['reviewed_row_zero_based']],'access_requirements':creatures[h['creature_key']]['route_requirements'],'receipt_contract':'challenge-personal-drop-v1; current-client gate unchanged'})
 groups={};unresolved=[];outside=0;selected_raw=0
 for p in pages:
  path=p['source_url'].split('/w/',1)[-1];kind=correction.get(path,p['classification'].upper())
  if kind not in ['MONSTER','BOSS']:continue
  if re.search(r'Nightmare Zone|Deadman|\(Echo\)|Realm of Memories|Sailing alpha|Leagues',p['name'],re.I):continue
  for i,row in enumerate(p['drops']):
   if row['item_name'] not in CANDIDATE_NAMES or (p['source_url'],row['item_name']) in oldpairs:continue
   selected_raw+=1;pval=scalar_rate(row['rarity']);tier=tier_for_probability(pval)
   base={'creature':p['name'],'item':row['item_name'],'source_url':p['source_url'],'source_sha256':p['source_sha256'],'fetched_at':p['capture']['fetched_at'],'source_row_zero_based':i,'raw_record':row,'runtime_enabled':False}
   if pval is None:
    unresolved.append(dict(base,status='UNRESOLVED_RATE_CONTEXT_NO_FIXED_TIER'));continue
   if tier is None:outside+=1;continue
   key=(p['source_url'],row['item_name'],tier,str(pval))
   if key not in groups:groups[key]={'tier':tier,'creature':p['name'],'item':row['item_name'],'source_url':p['source_url'],'base_rate':str(pval),'raw_rates':[],'v2_fp':None,'status':'SOURCE_CANDIDATE_NOT_ADMITTED','requirements':'Meaningful-unique/source/mode/task or allocation, actual access, exact IDs, receipt and FP review required.','source_rows':[],'runtime_enabled':False}
   g=groups[key];g['source_rows'].append(base)
   if row['rarity']['raw'] not in g['raw_rates']:g['raw_rates'].append(row['rarity']['raw'])
 candidates=sorted(groups.values(),key=lambda x:([t[0] for t in TIERS].index(x['tier']),x['creature'].casefold(),x['item'].casefold(),x['base_rate']))
 result={'version':'challenge-hunt-review-index-v2-2026-10-14','purpose':'Comprehensive tier display for all44 prior reviewed hunts and all qualifying scalar source/item/rate groups in the explicit named-signature candidate scan. Not an admitted v2 roster.','raw_archive_sha256':rules['v1_catalog_sha256'],'raw_archive_sha256_note':'This field pins the historical v1 catalog; original upload hash separately below.','upload_sha256':'e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309','candidate_name_scope':CANDIDATE_NAMES,'reviewed_hunts':reviewed,'source_candidates':candidates,'unresolved_rate_context_rows':unresolved,'outside_brackets_named_source_rows':outside,'named_unreviewed_source_rows_screened':selected_raw,'excluded_sources':'Corrected non-creature/unresolved/component pages and alternate-mode pages; no generic-RDT promotion.','runtime_enabled':False,'unlock_topology':'UNANSWERED_PROPOSAL_NOT_ADOPTED','v2_rewards':'NOT_AUTHORED; historical v1 quotes labeled separately','counts':{'reviewed':len(reviewed),'candidate_groups':len(candidates),'unresolved_rows':len(unresolved),'reviewed_by_tier':dict(collections.Counter(x['tier'] for x in reviewed)),'candidate_by_tier':dict(collections.Counter(x['tier'] for x in candidates))}}
 # Correct pin naming: historical catalog and original uploaded archive are different artifacts.
 result['v1_catalog_sha256']=result.pop('raw_archive_sha256');result.pop('raw_archive_sha256_note')
 # Source-kind partition is explicit and conserved; never infer boss from combat level.
 for entry in result['reviewed_hunts']+result['source_candidates']+result['unresolved_rate_context_rows']:
  source=byurl[entry['source_url']];path=entry['source_url'].split('/w/',1)[-1];kind=correction.get(path,source['classification'].upper())
  assert kind in ['BOSS','MONSTER'],(source['name'],kind)
  entry['source_kind']=kind
  entry['classification_evidence']=source['classification_evidence']
 result['counts']['by_source_kind']={kind:{'reviewed':sum(x['source_kind']==kind for x in result['reviewed_hunts']),'candidate_groups':sum(x['source_kind']==kind for x in result['source_candidates']),'unresolved_rows':sum(x['source_kind']==kind for x in result['unresolved_rate_context_rows']),'by_tier':dict(collections.Counter(x['tier'] for x in result['reviewed_hunts']+result['source_candidates'] if x['source_kind']==kind))} for kind in ['MONSTER','BOSS']}
 index=D/'catalogs/challenge_hunt_review_index_v2.json';index.write_text(json.dumps(result,indent=2,ensure_ascii=False)+'\n')
 with (D/'catalogs/challenge_hunt_review_index_v2.csv').open('w',newline='',encoding='utf-8') as f:
  w=csv.writer(f,lineterminator="\n");w.writerow(['source_kind','tier','creature','item','base_rate_for_tier','raw_rates','status','historical_v1_fp','v2_fp','source_url']);w.writerows([x['source_kind'],x['tier'],x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),x['status'],x.get('historical_v1_fp',''),'TBD',x['source_url']] for x in reviewed+candidates)
 from render_split_hunt_tables import render_tables
 render_tables(result,old)
 return result
if __name__=='__main__':
 r=render(pathlib.Path(sys.argv[1]));print(r['counts'])
