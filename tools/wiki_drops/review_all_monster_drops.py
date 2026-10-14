"""Complete supplied-monster row audit. No new network collection or roster admission.

Every captured monster drop receives a disposition, including nonselected items.
The explicit item judgements cover the entire supplied inventory, not a retrieval
whitelist. New captures/items must be reviewed again; never silently auto-admit.
"""
import collections,hashlib,json,pathlib,re,sqlite3
from challenge_tier_rules_v2 import scalar_rate,tier_for_probability
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
# Authored leads after inspecting the complete in-range inventory. Grouping is
# functional, not a claim that each source/context is already approved.
LEADS={
 'signature_equipment_or_upgrade':'''Dragon knife|Dragon thrownaxe|Elemental talisman|Air diamond|Earth emerald|Fire ruby|Water sapphire|Alchemist's signet|Aquanite tendon|Barronite guard|Broken zombie helmet|Dust battlestaff|Mist battlestaff|Lava battlestaff|Elven signet|Enhanced crystal teleport seed|Frozen tear|Glacial temotli|Horn of Plenty (empty)|Monkey tail|Obsidian cape|Obsidian helmet|Obsidian platebody|Obsidian platelegs|Toktz-ket-xil|Toktz-mej-tal|Toktz-xil-ak|Toktz-xil-ek|Toktz-xil-ul|Tzhaar-ket-om|Xeric's talisman (inert)|Bottled storm|Broken dragon hook|Dragon cannon barrel|Dragon keel parts|Large dragon keel parts|Echo pearl|Swift albatross feather|Craw's bow (u)|Viggora's chainmace (u)|Thammaron's sceptre (u)|Amulet of avarice|Ancient crystal|Bracelet of ethereum (uncharged)|Godsword shard 1|Godsword shard 2|Godsword shard 3''',
 'source_family_armour_or_component':'''Bronze boots|Iron boots|Steel boots|Black boots|Mithril boots|Adamant boots|Rune boots|Mystic boots (dark)|Mystic boots (light)|Mystic gloves (dark)|Mystic gloves (light)|Mystic hat (dark)|Mystic hat (light)|Mystic robe bottom (dark)|Mystic robe bottom (light)|Mystic robe top (dark)|Mystic robe top (light)|Ancient ceremonial boots|Ancient ceremonial gloves|Ancient ceremonial legs|Ancient ceremonial mask|Ancient ceremonial top|Granite shield|Rock-shell boots|Rock-shell gloves|Rock-shell chunk|Rock-shell shard|Rock-shell splinter|Skeletal boots|Skeletal gloves|Spined boots|Spined gloves|Circular hide|Flattened hide|Stretched hide|Fremennik blade|Fremennik helm|Fremennik shield|Flippers|Mudskipper hat|Shaman mask''',
 'progression_component_context_required':'''Bronze defender|Iron defender|Steel defender|Black defender|Mithril defender|Adamant defender|Rune defender|Dragon defender|Bottom of sceptre|Top of sceptre|Left skull half|Right skull half|Skull piece|Ribcage piece|Fibula piece'''
}
# Explicitly visible holds: not automatic meaningful-unique or ordinary-hunt approval.
HOLDS={
 'cosmetic_significance':'''Chompy chick|Chinchompa tuft|Red chinchompa tuft|Perfect shell|Perfect snail shell|Large jerboa tail|Ray barbs|Scaly blue dragonhide|Tattered moon page|Tattered sun page|Tattered temple page|Araxyte head|Basilisk head|Cockatrice head|Crawling hand (item)|Kurask head|Bandana eyepatch (blue)|Bandana eyepatch (red)|Fremennik brown cloak|Fremennik brown shirt|Fremennik gloves|Gardening boots|Right eye patch|Ham boots|Ham cloak|Ham gloves|Ham hood|Ham logo|Ham robe|Ham shirt|Pink boots|Pink robe bottoms|Pink robe top|Pink skirt|Purple gloves|Red gloves|Teal gloves|Yellow gloves|Blue wizard hat|Druid's robe|Vampyre snelm''',
 'non_equipment_or_special_context':'''Araxyte venom sac|Brimstone key|Brittle key|Giant key|Grubby key|Larran's key|Mossy key|Zombie pirate key|Ecumenical key|Chewed bones|Mangled bones|Ancient emblem|Ancient totem|Ancient statuette|Ancient medallion|Ancient effigy|Ancient relic|Tortoise shell|Elkhorn frag|Pillar frag|Umbral frag|Dark fishing bait|Body core|Chaos core|Blood essence|Smouldering gland|Smouldering heart|Smouldering pile of flesh|Blood sac|Stymphike feather|Venator fang|Venator tooth|Tarnished 2h sword|Tarnished amulet|Tarnished battleaxe|Tarnished bracelet|Tarnished halberd|Tarnished longsword|Tarnished necklace|Tarnished ring|Tarnished spear|Baby kraken head|Baby sea kraken blood|Baby sea kraken tentacle|Deep sea kraken tentacle|Pygmy kraken tentacle|Sea monodon horn|Broken monodon horn (Catherby)|Broken monodon horn (Kharazi)|Broken monodon horn (Ogre)|Pristine monodon horn|Ogre monodon horn|Contract of Bloodied Blows|Contract of Divine Severance|Contract of Forfeit Breath|Contract of Glyphic Attenuation|Contract of Sensory Clouding|Cursed goblin bow|Cursed goblin hammer|Cursed goblin staff'''
}
SHARED='''Imbued heart|Eternal gem|Ancient shard|Dark totem base|Dark totem middle|Dark totem top|Shield left half|Tooth half of key|Loop half of key|Long bone|Curved bone|Dragon spear'''.split('|')

def source_exclusion(p):
    if re.search(r'Nightmare Zone|Deadman|\(Echo\)|Realm of Memories|Sailing alpha|Leagues|Last Man Standing',p['name'],re.I):return 'EXCLUDED_ALTERNATE_MODE'
    cats=' | '.join(p['categories'])
    if re.search(r'Deadman|Breach monsters|League|Sailing alpha',cats,re.I):return 'EXCLUDED_ALTERNATE_MODE'
    if re.search(r'Holiday events|Christmas.*event|Easter.*event|Birthday.*event|Discontinued content',cats,re.I):return 'HELD_DISCONTINUED_OR_EVENT_SOURCE'
    return None

def item_judgement(item,prior_names):
    for reason,items in LEADS.items():
        if item in items.split('|'):return 'LEAD',reason
    for reason,items in HOLDS.items():
        if item in items.split('|'):return 'HOLD',reason
    if item in SHARED or re.search(r'^(Clue scroll|Reward casket|Ensouled |Unidentified .*fossil)|champion scroll',item or '',re.I):return 'EXCLUDE','shared_table_or_secondary_objective_not_specific_unique'
    if item in prior_names:return 'LEAD','prior_signature_candidate_rechecked_against_full_inventory'
    # Remaining names were inspected as a complete inventory in A67. This is an
    # explicit batch judgement, persisted per item below, not a future default.
    return 'EXCLUDE','ordinary_supplies_generic_equipment_resources_or_quest_activity_loot'

def review(input_dir,pages,correction,prior_names,old_rows):
    assert hashlib.sha256((input_dir/'catalog.json').read_bytes()).hexdigest()=='aea6c2d92f396353d6c515db4fbe762c7405997c6983eebbb481c17c738db1f2', 'New input requires a new full inventory review'
    monsters=[p for p in pages if correction.get(p['source_url'].split('/w/',1)[-1],p['classification'].upper())=='MONSTER']
    inventory=sorted({r['item_name'] for p in monsters for r in p['drops']},key=lambda x:str(x).casefold())
    judgements={item:item_judgement(item,prior_names) for item in inventory}
    selected=set();held_significance=[];records=[];counts=collections.Counter();checks=0
    db=sqlite3.connect('file:'+str((input_dir/'drops.sqlite').resolve())+'?mode=ro',uri=True)
    for p in monsters:
        reason=source_exclusion(p);dispositions=collections.defaultdict(list);evidence=[]
        cache=input_dir/'html'/p['capture']['cache_file'].replace('\\','/').split('/')[-1]
        normalized=hashlib.sha256(cache.read_text(encoding='utf-8').encode()).hexdigest() if cache.exists() else None
        assert normalized==p['source_sha256'],p['name'];checks+=1
        assert p['capture']['origin']=='network_html' and not p['capture']['fixture'];checks+=1
        rows={n:json.loads(payload) for n,payload in db.execute('select row_number,payload_json from drops where source_url=?',(p['source_url'],))}
        assert len(rows)==len(p['drops']);checks+=1
        for i,row in enumerate(p['drops']):
            assert row==rows[i],(p['name'],i);checks+=1
            val=scalar_rate(row['rarity']);tier=tier_for_probability(val);judgement,why=judgements[row['item_name']]
            if reason:disposition=reason
            elif (p['source_url'],i) in old_rows:disposition='PRIOR_REVIEWED_ROW'
            elif re.search(r'pickpocket|hunter info|dialogue rewards',row['section'],re.I):disposition='HELD_NON_KILL_ACQUISITION_METHOD'
            elif 'Superior Slayer tertiary' in row['section'] and row['item_name'] in ['Dust battlestaff','Mist battlestaff','Imbued heart','Eternal gem']:disposition='EXCLUDED_SHARED_SUPERIOR_TABLE'
            elif re.search(r'impling|\(Hunter\)',p['name'],re.I):disposition='HELD_NON_KILL_ACQUISITION_METHOD'
            elif row['rarity']['kind']=='always':disposition='OUTSIDE_BRACKETS_GUARANTEED'
            elif val is not None and tier is None:disposition='OUTSIDE_BRACKETS_SCALAR'
            elif judgement=='LEAD':
                selected.add((p['source_url'],i));disposition='CANDIDATE_FIXED_TIER' if tier else 'HELD_RATE_CONTEXT'
            elif judgement=='HOLD':
                disposition='HELD_SIGNIFICANCE_OR_ACTIVITY_CONTEXT'
                held_significance.append({'creature':p['name'],'item':row['item_name'],'source_url':p['source_url'],'source_row_zero_based':i,'raw_rate':row['rarity']['raw'],'section':row['section'],'possible_scalar_tier':tier,'reason':why})
            else:disposition='EXCLUDED_NON_SIGNATURE_LOOT'
            dispositions[disposition].append(i);counts[disposition]+=1
        records.append({'creature':p['name'],'source_url':p['source_url'],'source_sha256':p['source_sha256'],'fetched_at':p['capture']['fetched_at'],'last_modified':p['capture'].get('last_modified'),'provenance':'network_html; fixture=false','html_normalized_sha256_verified':True,'sqlite_all_rows_equal':True,'drop_tables_status':p['drop_tables_status'],'warnings':p['warnings'],'row_count':len(p['drops']),'source_exclusion':reason,'row_dispositions':dict(dispositions)})
    # Persist complete item decisions (including rejected names), not just leads.
    audit={'version':'monster-drop-audit-a67','scope':'Every corrected MONSTER page and captured row in supplied archive; not all-game or crawl completeness; candidate display only','upload_sha256':'e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309','monster_pages':len(monsters),'drop_rows':sum(len(p['drops']) for p in monsters),'distinct_item_names':len(inventory),'disposition_counts':dict(counts),'source_cross_checks':checks,'item_decisions':[{'item':item,'judgement':judgements[item][0],'reason':judgements[item][1]} for item in inventory],'pages':records,'held_significance_or_activity_rows':held_significance,'runtime_enabled':False,'unanswered_design_proposals':'UNCHANGED_NOT_ADOPTED'}
    out=D/'research/MONSTER_DROP_AUDIT_A67.json';out.write_text(json.dumps(audit,ensure_ascii=False,indent=2)+'\n')
    return selected,audit
