"""Build lossless taxonomy overlay from supplied catalog; does not edit raw data."""
import collections,hashlib,json,pathlib,re,sys
PARENTS={
'Summoned Soul':'Cerberus',"Parasite (Phosani's Nightmare)":"Phosani's Nightmare",'Scion':'Araxxor','Totem (The Nightmare)':'The Nightmare',"Totem (Phosani's Nightmare)":"Phosani's Nightmare",'Snakeling':'Zulrah','Giant crypt rat':'Barrows','Spawn':'Venenatis','Dark energy core':'Corporeal Beast','Husk':'The Nightmare',"Husk (Phosani's Nightmare)":"Phosani's Nightmare",'Fire elemental (Royal Titans)':'Royal Titans','Crypt spider':'Barrows','Skeleton (Barrows)':'Barrows','Araxyte':'Araxxor','Bloodworm':'Barrows','Mirrorback Araxyte':'Araxxor','Imp (Yama)':'Yama','Respiratory system':'Abyssal Sire','Void Flare':'The Whisperer','Giant crypt spider':'Barrows','Crypt rat':'Barrows','Ur-maggot larvae':'Doom of Mokhaiotl','Giant rat (Scurrius)':'Scurrius','Ice elemental (Royal Titans)':'Royal Titans','Golem (Zalcano)':'Zalcano','Ruptura Araxyte':'Araxxor','Parasite':'The Nightmare','Zombified Spawn':'Vorkath','Sleepwalker (monster)':'The Nightmare','Tentacle (Abyssal Sire)':'Abyssal Sire','Acidic Araxyte':'Araxxor',"Sleepwalker (Phosani's Nightmare)":"Phosani's Nightmare"}
# Parent links are taxonomy only: killing an add NEVER satisfies its parent's hunt.
def key(name):return re.sub(r'[^a-z0-9]+','-',name.lower()).strip('-')
def build(c):
    corrections=[];counts=collections.Counter();grouped=[];variants=[]
    for p in c['creatures']+c['unclassified_pages']:
        strong=any(re.search(r'combat level|hitpoints|slayer level',m['field'],re.I) for m in p['infobox_fields'])
        direct_boss=any(re.fullmatch(r'Boss(?:es)?',cat,re.I) for cat in p['categories'])
        item='Items' in p['categories'];scenery='Scenery' in p['categories'];pet='Pets' in p['categories']
        if item or scenery or pet:
            result='NON_CREATURE';reason='explicit item/scenery/pet category; relational monster category is not entity identity'
        elif not strong:
            result='UNRESOLVED_ENTITY';reason='no combat/Slayer infobox evidence; never promote category relationship to creature'
        elif p['name'] in PARENTS:
            result='ENCOUNTER_COMPONENT';reason='named parent component; no separate Challenge diversity entry'
        elif direct_boss:result='BOSS';reason='direct Bosses category plus combat infobox'
        else:result='MONSTER';reason='combat/Slayer infobox; ancestry alone cannot make a boss'
        counts[result]+=1
        record={'name':p['name'],'source_url':p['source_url'],'raw_label':p['classification'],'corrected_kind':result,'reason':reason,'npc_ids_reported':p['reported_npc_ids']}
        if result in ['NON_CREATURE','UNRESOLVED_ENTITY','ENCOUNTER_COMPONENT'] or result.lower()!=p['classification']:corrections.append(record)
        if p['name'] in PARENTS:grouped.append({'source':p['name'],'parent_encounter':PARENTS[p['name']],'independent_challenge_target':False,'parent_drop_credit':False})
        if re.search(r'Nightmare Zone|Deadman|\(Echo\)|Realm of Memories|Sailing alpha|Leagues',p['name'],re.I):variants.append({'name':p['name'],'source_url':p['source_url'],'disposition':'EXCLUDE_V1_ALTERNATE_MODE_OR_SIMULATION','merge_into_normal_drop_context':False})
    return {'version':'taxonomy-review-v1','archive_sha256':'e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309','raw_modified':False,'total_page_records':sum(counts.values()),'corrected_counts':dict(counts),'correction_columns':['name','wiki_path','raw_label','corrected_kind'],'corrections':[[r['name'],r['source_url'].split('/w/',1)[1],r['raw_label'],r['corrected_kind']] for r in corrections],'grouped_components':grouped,'alternate_modes':variants,'rules':['One authored canonical encounter key per selectable creature; NPC forms and location/rate contexts do not multiply odds.','Never infer normal-world equivalent drops for quest/Nightmare Zone/Leagues/alpha variants.','Only explicit curated catalog members are selectable; index is not a roster.','NPC-ID overlaps are flags, not automatic equivalence; preserve Arianwyn/Nightmare-Zone and totem contexts.','No component drops complete parent hunts; no automatic add inclusion.']}
if __name__=='__main__':
 c=json.load(open(sys.argv[1]));r=build(c);pathlib.Path(sys.argv[2]).write_text(json.dumps(r,ensure_ascii=False,separators=(',',':'))+'\n');print(r['corrected_counts'],len(r['corrections']),len(r['grouped_components']))
