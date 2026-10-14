"""Render separate monster and boss tier tables from the source-classified review index."""
import collections,pathlib
from fractions import Fraction
from challenge_tier_rules_v2 import TIERS
ROOT=pathlib.Path(__file__).resolve().parents[2];D=ROOT/'docs/wheelbound'
TABLES={'MONSTER':'MONSTER_CHALLENGE_HUNT_TABLE.md','BOSS':'BOSS_CHALLENGE_HUNT_TABLE.md'}
def esc(x):return str(x).replace('|','\\|').replace('\n',' ')
def render_tables(index,v1):
    old={h['id']:h for h in v1['hunts']};creatures={c['key']:c for c in v1['creatures']}
    for kind,filename in TABLES.items():
        label='Monster' if kind=='MONSTER' else 'Boss';other='BOSS' if kind=='MONSTER' else 'MONSTER';otherlabel='boss' if other=='BOSS' else 'monster';reviewed=[x for x in index['reviewed_hunts'] if x['source_kind']==kind];candidates=[x for x in index['source_candidates'] if x['source_kind']==kind];held=[x for x in index['unresolved_rate_context_rows'] if x['source_kind']==kind];counts=collections.Counter(x['tier'] for x in reviewed+candidates)
        text=f'''# {label} Challenge hunt table

**{label.upper()} DROPS ONLY.** {'Non-boss creatures belong here; bosses are in the separate boss table.' if kind=='MONSTER' else 'Only boss sources belong here; ordinary monster drops are in the separate monster table.'}

[Open the {otherlabel} drop table]({TABLES[other]}) · [Four Challenge tier rules](CHALLENGE_CARD_TIERS.md) · [Source-preserving review index](catalogs/challenge_hunt_review_index_v2.json) · [CSV, filter by source_kind](catalogs/challenge_hunt_review_index_v2.csv)

This is a current creature/drop table expanded to the four tiers, not an archived old-range list. Every row retains its source creature, specific item and drop rate. Source classification uses the corrected taxonomy/evidence, not combat level or the word “monster” in an item category. Encounter adds, wrong entity types and alternate-mode captures are not promoted into independent hunts.

**Tier rule:** use the unmultiplied per-roll probability: `2 × 1/75` is Standard at `1/75`. The full original notation remains visible. Shared endpoints belong to the easier tier:1/50 Lesser,1/125 Standard,1/750 Greater,1/5000 Superior. Keep supported personal per-kill probability separate; per-roll labels are not exact per-kill claims.

**Review status:** “Reviewed v1 context” preserves prior source/access/receipt work, but is not automatic v2 admission. “Candidate” still needs full source/mode/access/personal-receipt/reward review. V2 FP is TBD; any prior FP shown is historical only. All entries remain runtime-disabled. These are source/item/rate review rows, not counts of distinct eligible creatures. The supplied crawl is partial. Monster coverage now audits every captured corrected-monster row; boss coverage remains the explicit named-item scan. This is not every drop in OSRS.

## Tier overview

| Challenge tier | Base drop-rate bracket | Reviewed v1 rows | Candidate rows | Total listed |
| --- | --- | ---: | ---: | ---: |
'''
        for key,name,lo,hi,inc in TIERS:
            bracket=f'1/{lo} through 1/{hi}' if inc else f'Rarer than 1/{lo}, through 1/{hi}'
            text+=f"| [{name}](#{key}-challenge) | {bracket} | {sum(x['tier']==key for x in reviewed)} | {sum(x['tier']==key for x in candidates)} | {counts[key]} |\n"
        text+=f'\n**{len(reviewed)+len(candidates)} listed rows: {len(reviewed)} previously reviewed + {len(candidates)} candidates.** Another {len(held)} unresolved source-rate rows are held below, not forced into a tier.\n'
        for key,name,_,_,_ in TIERS:
            text+=f'\n## {name}\n\n'
            rr=sorted([x for x in reviewed if x['tier']==key],key=lambda x:(x['creature'].casefold(),x['item'].casefold()));cc=[x for x in candidates if x['tier']==key]
            text+='### Previously reviewed contexts\n\n'
            if not rr:text+='No previously reviewed v1 contexts in this tier.\n'
            else:
                text+='| Creature / encounter | Specific fresh drop | Rate used for tier | Original source notation | Historical v1 FP | V2 FP |\n| --- | --- | --- | --- | ---: | --- |\n'
                for x in rr:text+='| '+' | '.join(esc(v) for v in [x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),f"{x['historical_v1_fp']:,}",'TBD'])+' |\n'
            text+='\n### Additional candidates — not admitted\n\n'
            if not cc:text+='No additional in-range scalar-rate candidates in the current source review.\n'
            else:
                text+='| Creature / encounter | Item candidate | Rate used for tier | Original source notation | Review / V2 FP |\n| --- | --- | --- | --- | --- |\n'
                for x in cc:
                    sections='; '.join(dict.fromkeys(z['raw_record']['section'] for z in x['source_rows']))
                    note='Candidate; FP TBD'+(' — '+sections if kind=='MONSTER' else '')
                    text+='| '+' | '.join(esc(v) for v in [x['creature'],x['item'],x['base_rate'],'; '.join(x['raw_rates']),note])+' |\n'
        text+='''
## Conditional, approximate or unparsed rates — held

These rows have no fixed tier yet. Do not choose a favorable task/party/variant alternative or turn an approximate expression into exact eligibility. Full raw rows and source receipts remain in the index/original upload.

| Creature / encounter | Item | Captured raw rate | Source section | Status |
| --- | --- | --- | --- | --- |
'''
        for x in sorted(held,key=lambda x:(x['creature'].casefold(),x['item'].casefold(),x['source_row_zero_based'])):text+='| '+' | '.join(esc(v) for v in [x['creature'],x['item'],x['raw_record']['rarity']['raw'] or '(unparsed)',x['raw_record']['section'],'Context/rate review; FP TBD'])+' |\n'
        if kind=='MONSTER':
            audit=__import__('json').load(open(D/'research/MONSTER_DROP_AUDIT_A67.json'))
            text+='\n## Additional item leads — significance or activity review held\n\nThese are visible review leads, not approved meaningful uniques. Some are cosmetics, keys, supplies, or special activities; ordinary loot is not automatically a hunt. Rates and methods stay unaltered.\n\n| Creature | Item | Raw rate | Source section | Hold reason |\n| --- | --- | --- | --- | --- |\n'
            for x in audit['held_significance_or_activity_rows']:
                text+='| '+' | '.join(esc(x[k]) for k in ['creature','item','raw_rate','section','reason'])+' |\n'
            text+='\n## Full supplied-monster coverage\n\n[Review report](research/MONSTER_DROP_REVIEW_A67.md) · [Every monster and row disposition](research/MONSTER_DROP_AUDIT_A67.json). All '+str(audit['monster_pages'])+' corrected monster captures / '+str(audit['drop_rows'])+' rows were checked against SQLite and cached HTML hashes. No re-scrape. Captured source introductions, infobox facts and footnotes for displayed candidates are in `monster_source_contexts` in the review index. They are source evidence, not completed access/FP approval.\n'
        text+='''
## Preserved access and receipt details for previously reviewed creatures

These are existing v1 supported contexts, not permissions automatically granted to new candidates. Actual membership, levels, quest/stage/diary, legal routes, owned usable equipment/supplies and supported personal loot still apply. A tier label does not grant access or a finite-task continuation waiver.

| Creature | Authored minimum skills | Existing supported route / conditions |
| --- | --- | --- |
'''
        seen=set()
        for x in sorted(reviewed,key=lambda x:x['creature'].casefold()):
            h=old[x['hunt_id_v1']];c=creatures[h['creature_key']]
            if c['key'] in seen:continue
            seen.add(c['key']);skills=', '.join(f'{v} {k}' for k,v in c['minimum_skills'].items()) or 'No additional numeric gate; actual readiness required'
            text+='| '+' | '.join(esc(v) for v in [c['name'],skills,c['route_requirements']])+' |\n'
        text+='''
## Completion, FP and policy boundaries

Every supported hunt requires one fresh active personal exact-item drop from its assigned source, matched to the initialized account/run/assignment/source/mode/context and a supported recipient receipt. Old possession, trade/bank changes, teammate loot, another source, or paused earning cannot settle it. Keep the item; no turn-in or consumption. Once-only completion and the independently keyed permanent bounty remain separate. Tier selection does not change the actual drop probability.

Historical [v1 machine records](catalogs/challenge_hunts_v1.json) preserve source rows, personal encounter probabilities and FP quote calculations. [Original human table at its historical commit](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound/blob/c62d381e4392845bce76d0738fc176af13374661/docs/wheelbound/CHALLENGE_HUNT_TABLE.md) preserves the full old presentation. Current rows are grouped by the new bracket, not the old1/400–1/1500 range. New tier rewards and candidate access/receipt reviews remain unfinished; do not silently recycle old FP as v2 quotes.

Task-only, finite-key/seed, group allocation and stateful contexts remain held where required. Splitting this documentation into monster and boss tables does **not** approve separate paid monster/boss card families, unlock prerequisites, offer controls or additional slots. The progressive-versus-independent unlock question remains unanswered. No production gameplay, runtime enablement, main changes or source recrawl.
'''
        (D/filename).write_text(text)
    return {kind:sum(x['source_kind']==kind for x in index['reviewed_hunts']+index['source_candidates']) for kind in TABLES}
