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
 index=D/'catalogs/challenge_hunt_review_index_v2.json';index.write_text(json.dumps(result,indent=2,ensure_ascii=False)+'\n')
 with (D/'catalogs/challenge_hunt_review_index_v2.csv').open('w',newline='',encoding='utf-8') as f:
  w=csv.writer(f);w.writerow(['tier','creature','item','base_rate_for_tier','raw_rates','status','historical_v1_fp','v2_fp','source_url']);w.writerows([x['tier'],x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),x['status'],x.get('historical_v1_fp',''),'TBD',x['source_url']] for x in reviewed+candidates)
 text='''# Challenge hunt table — all four tiers

Current bracket rules: [Challenge card tiers](CHALLENGE_CARD_TIERS.md), WB-D144/145. **Tier placement uses the unmultiplied per-roll rate:** `2 × 1/75` is Standard at `1/75`. Original rolls, alternatives and conditions remain in the evidence. This is not an exact per-kill probability claim.

This page contains every previously reviewed hunt, plus the complete in-range scalar-rate results of the explicit named signature-item candidate scan across the supplied corpus. It is **not every drop in OSRS**: generic loot, unknown entities, encounter adds and alternate modes are not promoted to hunts. Conditional/approximate/unparsed rates are listed separately rather than guessed into a tier. The input crawl remains partial.

**Status legend:** **Reviewed v1** means its prior context/access/evidence was authored, now reclassified; it is not automatically admitted to a new four-tier pool. **Candidate** means captured source evidence exists but full source/access/receipt/reward review remains. **V2 FP is TBD for every row**; historical v1 FP is shown only for continuity, not silently reused as a tier reward. All entries remain runtime-disabled. Unlock topology/offer controls/prices and per-rank eligibility remain unanswered proposals.

[Full source-preserving JSON index](catalogs/challenge_hunt_review_index_v2.json) · [Flat CSV](catalogs/challenge_hunt_review_index_v2.csv) · [Historical v1 table/access/FP archive](CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md)

## Brackets and totals

Shared endpoints belong to the easier tier. For non-unit fractions use exact reciprocal D=1/p; do not round into a different bracket.

| Tier | Base rate bracket | Previously reviewed hunts | Additional candidate source/item/rate groups |
| --- | --- | ---: | ---: |
'''
 for key,name,lo,hi,inc in TIERS:
  bracket=f'1/{lo} through 1/{hi}' if inc else f'Rarer than 1/{lo}, through 1/{hi}'
  text+=f"| [{name}](#{key}-challenge) | {bracket} | {sum(x['tier']==key for x in reviewed)} | {sum(x['tier']==key for x in candidates)} |\n"
 text+=f"\n**Total: {len(reviewed)} previously reviewed hunts + {len(candidates)} candidate groups; {len(unresolved)} unresolved source rows held separately.** Groups/rows are not counts of distinct eligible creatures; repeated source methods/forms do not increase selection odds.\n"
 for key,name,lo,hi,inc in TIERS:
  text+=f'\n## {name}\n\n'
  rr=sorted([x for x in reviewed if x['tier']==key],key=lambda x:(x['creature'].casefold(),x['item'].casefold()));cc=[x for x in candidates if x['tier']==key]
  text+='### Previously reviewed hunts\n\n'
  if not rr:text+='No previously reviewed v1 hunts in this tier. Candidate rows below still need review.\n'
  else:
   text+='| Creature / encounter | Specific fresh item | Rate used for tier | Original rate notation | Historical v1 FP | V2 FP |\n| --- | --- | --- | --- | ---: | --- |\n'
   for x in rr:text+='| '+' | '.join(escape(v) for v in [x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),f"{x['historical_v1_fp']:,}",'TBD'])+' |\n'
  text+='\n### Additional source candidates — not admitted\n\n| Creature / encounter | Specific item candidate | Rate used for tier | Original notation | Status / V2 FP |\n| --- | --- | --- | --- | --- |\n'
  for x in cc:text+='| '+' | '.join(escape(v) for v in [x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),'Candidate; FP TBD'])+' |\n'
 text+='''
## Conditional, approximate or unsupported rate expressions — held

No convenient alternative is selected below. These rows have no fixed tier until source/method/task/allocation or approximation is reviewed. A candidate may already have a different supported context elsewhere; rows do not create extra lottery entries.

| Source | Item | Captured raw expression | Section | Status |
| --- | --- | --- | --- | --- |
'''
 for x in sorted(unresolved,key=lambda x:(x['creature'].casefold(),x['item'].casefold(),x['source_row_zero_based'])):text+='| '+' | '.join(escape(v) for v in [x['creature'],x['item'],x['raw_record']['rarity']['raw'] or '(unparsed)',x['raw_record']['section'],'Context/rate review; FP TBD'])+' |\n'
 text+='''
## Access, completion and selection safeguards

For the44 reviewed hunts, the [historical access table](CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md#access-requirements-by-creature) and [source/context packet](research/CHALLENGE_CONTEXT_EVIDENCE_A61.json) preserve specific levels, quests, routes, equipment, solo allocation and source IDs. They do not grant newly listed candidates those same permissions. Every additional candidate needs its own route, exact item/source mapping, supported personal receipt and reward review. Task-only hunts, finite keys/seeds, group allocation and stateful drops remain held where existing policies require it. Candidate inclusion is a review lead, never a task/region/shop/access waiver.

Completion remains one fresh active personal exact-item drop from the assigned source, with matched run/account/assignment/context, supported recipient proof and once-only journal settlement. Old ownership/trade/bank movement/paused earning/another creature cannot complete. Keep the item; no hand-in or consumption. Existing qualifying permanent bounty remains independent and once-only. Tier labels do not multiply FP or guarantee duration. No roster/offer/control/unlock-topology approval is inferred from this table update.

Source scan uses the explicit item-name list in the JSON index, not an assertion that all named items or every source is suitable. Names only identify research leads; final selection uses reviewed canonical IDs. Source rows, raw rolls/rates/notes, timestamps and hashes are retained in the index/original upload. Duplicate identical source/item/rate rows are grouped, while different rates/contexts remain distinct review groups. The old v1 raw catalog/CSV/FP and frozen assignments are unchanged.

This page replaces the old mixed44-row presentation. It does not claim the expanded four-tier catalog is ready for runtime. Current-client adapter tests and the unanswered rank-unlock/offer/FP integration remain separate. No production gameplay/main changes or source recrawl.
'''
 (D/'CHALLENGE_HUNT_TABLE.md').write_text(text)
 return result
if __name__=='__main__':
 r=render(pathlib.Path(sys.argv[1]));print(r['counts'])
