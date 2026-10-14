"""Freeze authored quotes, contracts and human/CSV table. No live gameplay code."""
import csv,hashlib,json,math,pathlib
from fractions import Fraction
ROOT=pathlib.Path(__file__).resolve().parents[2];DOC=ROOT/'docs/wheelbound';CAT=DOC/'catalogs/challenge_hunts_v1.json'
def q(d):return Fraction(d['numerator'],d['denominator'])
def ftext(x):return f'{x.numerator}/{x.denominator}'
def finalize():
 r=json.loads(CAT.read_text());sources=json.loads((DOC/'research/CHALLENGE_CONTEXT_EVIDENCE_A61.json').read_text());bykey={x['creature_key']:x for x in sources}
 for c in r['creatures']:
  c['loot_composition_alias_ids']=bykey[c['key']]['reported_npc_ids']
  c['alias_policy']='Source-reported server composition aliases are scoped to this observed normal encounter, never global NPC-name matching; current-client mapping traces required before activation.'
 for e in r['hunts']:
  prob=e['personal_probability'];low=q(prob['lower']);high=q(prob['upper']);kph=e['reference_kills_per_hour']
  # Conservative reward anchor: worst supported published-source probability, not independent-roll arithmetic.
  expected_kills=1/low;hours=expected_kills/kph;unrounded=100+100*hours;reward=25*math.ceil(unrounded/25)
  e['fp_reward']=reward;e['reward_status']='AUTHORED_V1_FIXED_QUOTE';e['reward_quote_version']='challenge-fp-v1';e['reward_reference']={'formula':'25 * ceil((100 + 100 * reference_expected_hours) / 25)','base_fp':100,'fp_per_reference_hour':100,'round_up_to_fp':25,'probability_anchor':'lower supported source-model bound; conservative expected effort','reference_expected_kills':ftext(expected_kills),'reference_expected_hours':ftext(hours),'reference_expected_hours_decimal':round(float(hours),3),'supported_effort_hours_range':[round(float(1/high/kph),3),round(float(1/low/kph),3)],'throughput_status':'AUTHORED_NOT_MEASURED; includes repeat entry/travel/supply downtime in one reference estimate','payment':'Once on supported fresh active personal drop; no KC cap, partial/time-based payout or guaranteed completion.','not_market_price_based':True,'actual_duration_reprices_quote':False}
 r['design_status']='COMPLETE_AUTHORED_V1_CATALOG';r['reward_schedule_version']='challenge-fp-v1';r['runtime_status']='NOT_IMPLEMENTED_OR_CLIENT_VALIDATED';r['runtime_enabled']=False
 r['access_contract']={'id':'challenge-access-v1','all_required':['Correct initialized account/run identity and normal persistent game world; exclude Leagues/Deadman/beta/quest reenactments.','Own Challenge through Lesser AND Greater; own starter grant or individual target unlock.','At least three distinct unlocked accessible supported creature keys after all filtering.','Membership, authored unboosted minimum skills and completed quest/diary/stage predicates; actual required regions and entry routes legally available.','Required usable equipment and supplies are owned or legally renewable under existing access. Do not demand infinite stock, but a finite task/key-only route is not sustainable.','Source-specific repeatable route and personal solo allocation are feasible; instance fees and legal replenishment route supported.','Necessary travel/vendor/faction-KC actions are already permitted; hunt selection grants no new access or tolerance exemption.','Current validated source/loot/composition/context adapter for the live client. Unknown predicates exclude before offers.'],'on_later_failure':'Preserve frozen assignment/evidence/quote; show context unavailable; existing Pause/Reject only. No automatic reroll or adoption of pending retirement proposal.'}
 r['completion_contract']={'id':'challenge-personal-drop-v1','event_sources':['RuneLite ServerNpcLoot composition + items (candidate authoritative local loot source)','NpcLootReceived NPC + items only when ownership/source correlation is separately validated; never double-award both streams'],'required_receipt_fields':['run_id','account_profile_key','assignment_id','encounter_key','context_id','context_version','catalog_hash','quote_version','world_type','encounter_instance_id','server_or_validated_session_receipt_key','encounter_completed_at_active_interval','local_recipient_proof','source_composition_or_npc_id','fresh_item_id','quantity'], 'positive_rules':['Assignment was durably selected before earning encounter and loot; run/account matches.','One completed source encounter within supported location/mode/phase and solo context; frozen item/context match.','Fresh local personal assigned-source loot receipt lists exact accepted dropped item ID and quantity >=1. No pickup/turn-in/consumption prerequisite added; owning an old item is irrelevant.','Earning occurs while active; no paused-drop backfill or payout processing while paused. Ambiguous delayed earning/delivery retains evidence without automatic credit until existing timing policy can prove qualification.','Atomic once-only assignment completion/FP journal transaction; duplicate adapter streams, reconnect/replay/repeated pickup cannot pay twice.','Player keeps the drop; disposal afterward does not revoke legitimate completion. Independent first-time permanent item bounty is separately keyed and can pay once.'],'negative_rules':['Inventory/bank increase alone, collection-log history, GE/shop/trade, teammate item, floor pickup without personal-source receipt, transformed old item, minion/other creature, quest/alternate mode or paused earning never qualify.','Unknown source/ownership/context/earning time holds evidence without inventing credit.','No proximity-only ownership; no exact string-name-only source or item mapping.'],'client_validation_status':'REQUIRED_BEFORE_RUNTIME; specification complete, positive/negative current-client traces NOT_RUN.'}
 CAT.write_text(json.dumps(r,indent=2,ensure_ascii=False)+'\n')
 bycreature={c['key']:c for c in r['creatures']}
 rows=[]
 for e in r['hunts']:
  c=bycreature[e['creature_key']];p=e['personal_probability'];rate=ftext(q(p['lower'])) if p['lower']==p['upper'] else f"{ftext(q(p['lower']))} to {ftext(q(p['upper']))}"
  rows.append([c['name'],e['item_name'],rate,e['fp_reward'],c['bundle'],c['route_requirements'],e['completion_contract'],round(float(Fraction(e['reward_reference']['reference_expected_hours'])),2),e['reference_kills_per_hour'],e['canonical_item_id'],e['id']])
 with (DOC/'catalogs/challenge_hunts_v1.csv').open('w',newline='',encoding='utf-8') as f:
  w=csv.writer(f);w.writerow(['creature','specific_item','personal_probability_per_complete_kill','fixed_fp','bundle','supported_access','completion_contract','authored_reference_hours','authored_reference_kph','drop_item_id','hunt_id']);w.writerows(rows)
 text='''# Challenge hunt table — authored catalog v1

**44 specific-item hunts, 25 distinct creature/encounter keys, six starter creatures.** This is the completed design selection table with source-reviewed rates, fixed FP quotes, access predicates and completion evidence. Runtime implementation and current-client validation remain separate; no gameplay code was authorized or enabled.

Machine-readable [JSON](catalogs/challenge_hunts_v1.json), flat [CSV](catalogs/challenge_hunts_v1.csv), [source context packet](research/CHALLENGE_CONTEXT_EVIDENCE_A61.json), [taxonomy overlay](research/CHALLENGE_TAXONOMY_A60.md). Use the explicit roster, not raw collector labels. Unlisted sources/items/contexts are not selectable. Source corpus is the user-supplied 2026-10-14 snapshot; no all-game completeness or independent live verification claim.

## Selection table

One line means **obtain that exact item from that assigned creature**, not any unique. Starter means the target unlock is included with the gated Challenge card; skills/quests/actual access are still required. Expansion members use existing individual target unlocks; this table does not set purchase prices. Grouped ordinary forms count once. At least three actually eligible creature keys are required before offering Challenge; then uniform creature-first and item-second draw. Previously owned items remain eligible.

| Creature | Specific fresh drop | Personal rate / full kill | Fixed FP | Unlock |
| --- | --- | --- | ---: | --- |
'''
 for x in rows:text+=f'| {x[0]} | {x[1]} | {x[2]} | {x[3]:,} | {x[4]} |\n'
 text+='''
## Access requirements by creature

All entries require membership, initialized correct account/run/normal-world state, existing lawful region/shop/travel/combat permissions, sustainable combat supplies and verified personal solo loot. Unknown access excludes before offers; selection grants no unlocks or tolerance exceptions. Every stat minimum below is an authored conservative **unboosted** gate; recommended gear/skills do not become unstated mandatory requirements. GWD common dungeon-entry route must be verified in addition to each listed faction room; necessary faction KC must already be permitted. Consumable entry fees/supplies need legal replenishment, not an infinite inventory or a finite task/key as the sole route.

| Creature | Minimum authored skills | Supported route and conditions |
| --- | --- | --- |
'''
 for c in r['creatures']:
  skills=', '.join(f'{v} {k}' for k,v in c['minimum_skills'].items()) or 'No additional numeric gate; actual combat readiness'
  text+=f"| {c['name']} | {skills} | {c['route_requirements']} |\n"
 text+='''
## Completion evidence for every row

Use the `challenge-personal-drop-v1` contract embedded in JSON. Record account/run, assignment, catalog/context/quote version, completed encounter/instance, active earning interval, local recipient proof and exact source/item IDs. A new supported personal loot receipt from the assigned source settles the hunt once and pays the frozen FP. It does not require handing in or consuming the drop. A second unique of a different type does not complete the hunt. An old owned item never completes it.

ServerNpcLoot exposes source composition and item stacks; NpcLootReceived exposes an NPC and item stacks. These APIs are candidate evidence inputs, not by themselves proof of every context or ownership edge case. Correlate the local awarded loot with the observed supported encounter and active earning interval. Never infer ownership from proximity/inventory alone. Deduplicate both event streams and reconnect/replay against one durable completion journal. Item identity, region, phase and solo context must match; known same-name pets, quest actors and other modes are excluded. Source-reported composition aliases are scoped to the observed encounter and require current-client trace validation before activation.

Negative evidence cases: GE/shop/trade/bank/old possession, teammate drop, another creature/minion, pickpocketed shard, wrong mode or post-pause earning cannot settle. Ambiguous delayed loot is held as evidence without invented payout; existing paused/delivery rules remain. Player keeps the item and may dispose of it after legitimate completion. An independently qualifying permanent bounty remains separate and once-only.

## FP schedule and effort limits

Version `challenge-fp-v1`: **25 × ceil((100 + 100 × reference expected hours) / 25)**. Reference expected hours = 1 / (conservative lower supported source-model probability × authored kills/hour). The fixed 100 FP base and 100 FP/reference-hour are new delegated v1 balance choices, not recovered historical approvals or measured income. Kills/hour includes an authored allowance for travel, repeat entry, resupply and risk; each value and exact rational computation are in JSON/CSV. It is not live telemetry or a player speed requirement.

Zulrah's dependency-free range is 1/1024–1/512; the quote conservatively uses 1/1024. Single-roll tertiary Vorkath necklace stays 1/1000 despite two ordinary loot rolls. Exact non-unit fractions remain intact. The entire supported personal range must fit 1/1500–1/400, with no independence assumption, team-rate division or choice of a convenient task alternative.

Long late-game hunts deliberately pay more because rate rarity alone does not capture kill duration. These authored quotes need future playtest calibration against the wider provisional economy; there is no claim of measured balance. Quote freezes with the offer/catalog and final assignment. Actual fast or dry luck never changes it; there is no KC/time cap, pity completion, passive/partial FP or market-price floor. Table membership is not altered by price or ownership. Negative FP and separate bounty rewards follow existing rules.

## Explicit v1 exclusions

Task-only/finite-task routes (including Kraken, Cerberus, Smoke devil and Alchemical Hydra), superior shared-table hunts, task-only boosted Basilisk/Drake/Hydra contexts, finite-key-only encounters, raids/Nex/unknown group allocation, DT2 hidden vestige progress, ordinary Nightmare and larger multi-recipient Wilderness boss contexts are not in v1. They are not silently fixed by free tasks, unrelated training, dividing party chance, or rerolling on task exhaustion. Jars/heads/pets/generic and shared RDT outcomes are not selected. Items outside the band are excluded in that reported context. Further distinct Fossil Island wyvern species are unselected, not extra variants silently merged into Spitting Wyvern.

This completes the finite authored catalog, not every possible future hunt. Future additions use a new reviewed version. The outstanding permanent-invalidation and task-continuation proposals remain unadopted; on later context loss preserve assignment and existing Pause/Reject behavior.

## Evidence and validation boundary

Primary API references: [ServerNpcLoot](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/events/ServerNpcLoot.html), [NpcLootReceived](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/events/NpcLootReceived.html). Identity constants pinned to RuneLite commit `42a6f17a6a2e8e478aa763890ecd0181a59dad38` in the evidence packet. API signatures and identity constants do not establish current-client timing/ownership/phase behavior. Original cached source fractions/footnotes and revision receipts are retained separately from authored access/reward judgment.

Offline validation checks all 44 hunt IDs/quotes/bounds/source rows, source identities, six starter keys/25 diversity keys, exact specific drop IDs, grouping invariance, source-hash/SQLite matches, dependency-free multi-roll arithmetic, selection odds and negative receipt-contract requirements. Current-client traces, Java adapters and gameplay settlement tests are **NOT_RUN and remain activation gates**. No production gameplay or main changes.
'''
 (DOC/'CHALLENGE_HUNT_TABLE.md').write_text(text)
 return r
if __name__=='__main__':
 r=finalize();print('Completed',len(r['hunts']),'hunts;',min(e['fp_reward'] for e in r['hunts']),'to',max(e['fp_reward'] for e in r['hunts']),'FP')
