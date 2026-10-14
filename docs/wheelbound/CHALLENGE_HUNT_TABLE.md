# Challenge hunt table — authored catalog v1

**44 specific-item hunts, 25 distinct creature/encounter keys, six starter creatures.** This is the completed design selection table with source-reviewed rates, fixed FP quotes, access predicates and completion evidence. Runtime implementation and current-client validation remain separate; no gameplay code was authorized or enabled.

Machine-readable [JSON](catalogs/challenge_hunts_v1.json), flat [CSV](catalogs/challenge_hunts_v1.csv), [source context packet](research/CHALLENGE_CONTEXT_EVIDENCE_A61.json), [taxonomy overlay](research/CHALLENGE_TAXONOMY_A60.md). Use the explicit roster, not raw collector labels. Unlisted sources/items/contexts are not selectable. Source corpus is the user-supplied 2026-10-14 snapshot; no all-game completeness or independent live verification claim.

## Selection table

One line means **obtain that exact item from that assigned creature**, not any unique. Starter means the target unlock is included with the gated Challenge card; skills/quests/actual access are still required. Expansion members use existing individual target unlocks; this table does not set purchase prices. Grouped ordinary forms count once. At least three actually eligible creature keys are required before offering Challenge; then uniform creature-first and item-second draw. Previously owned items remain eligible.

| Creature | Specific fresh drop | Personal rate / full kill | Fixed FP | Unlock |
| --- | --- | --- | ---: | --- |
| Brine rat | Brine sabre | 1/512 | 450 | starter |
| Turoth | Leaf-bladed sword | 1/500 | 400 | starter |
| Cave horror | Black mask (10) | 1/512 | 450 | starter |
| Sulphur Nagua | Sulphur blades | 1/450 | 325 | starter |
| Kurask | Leaf-bladed battleaxe | 1/1026 | 975 | starter |
| Skeletal Wyvern | Granite legs | 1/512 | 850 | starter |
| Abyssal demon | Abyssal whip | 1/512 | 400 | expansion |
| Dark beast | Dark bow | 1/512 | 850 | expansion |
| Spitting Wyvern | Granite longsword | 1/512 | 975 | expansion |
| Adamant dragon | Dragon limbs | 1/1000 | 1,775 | expansion |
| Rune dragon | Dragon limbs | 1/800 | 1,900 | expansion |
| Demonic gorilla | Ballista limbs | 1/500 | 1,225 | expansion |
| Demonic gorilla | Ballista spring | 1/500 | 1,225 | expansion |
| Demonic gorilla | Light frame | 1/750 | 1,775 | expansion |
| Demonic gorilla | Heavy frame | 1/1500 | 3,450 | expansion |
| Vyrewatch Sentinel | Blood shard | 1/1500 | 1,775 | expansion |
| Tormented Demon | Burning claw | 499/250000 | 1,775 | expansion |
| Tormented Demon | Tormented synapse | 1/500 | 1,775 | expansion |
| General Graardor | Bandos hilt | 1/508 | 2,650 | expansion |
| Kree'arra | Armadyl hilt | 1/508 | 2,925 | expansion |
| Commander Zilyana | Armadyl crossbow | 1/508 | 2,650 | expansion |
| Commander Zilyana | Saradomin hilt | 1/508 | 2,650 | expansion |
| K'ril Tsutsaroth | Staff of the Dead | 1/508 | 2,650 | expansion |
| K'ril Tsutsaroth | Zamorak hilt | 1/508 | 2,650 | expansion |
| Zulrah | Tanzanite fang | 1/1024 to 1/512 | 4,375 | expansion |
| Zulrah | Magic fang | 1/1024 to 1/512 | 4,375 | expansion |
| Zulrah | Serpentine visage | 1/1024 to 1/512 | 4,375 | expansion |
| Vorkath | Dragonbone necklace | 1/1000 | 4,275 | expansion |
| Corporeal Beast | Spectral sigil | 1/1365 | 13,750 | expansion |
| Corporeal Beast | Arcane sigil | 1/1365 | 13,750 | expansion |
| Phosani's Nightmare | Nightmare staff | 69/35000 | 8,575 | expansion |
| Phosani's Nightmare | Inquisitor's great helm | 1/700 | 11,775 | expansion |
| Phosani's Nightmare | Inquisitor's hauberk | 1/700 | 11,775 | expansion |
| Phosani's Nightmare | Inquisitor's plateskirt | 1/700 | 11,775 | expansion |
| Phosani's Nightmare | Inquisitor's mace | 31/35000 | 18,925 | expansion |
| Artio | Claws of Callisto | 1/618 | 2,175 | expansion |
| Artio | Tyrannical ring | 1/716 | 2,500 | expansion |
| Artio | Voidwaker hilt | 1/912 | 3,150 | expansion |
| Spindel | Fangs of Venenatis | 1/618 | 2,175 | expansion |
| Spindel | Treasonous ring | 1/716 | 2,500 | expansion |
| Spindel | Voidwaker gem | 1/912 | 3,150 | expansion |
| Calvar'ion | Skull of Vet'ion | 1/618 | 1,650 | expansion |
| Calvar'ion | Ring of the gods | 1/716 | 1,900 | expansion |
| Calvar'ion | Voidwaker blade | 1/912 | 2,400 | expansion |

## Access requirements by creature

All entries require membership, initialized correct account/run/normal-world state, existing lawful region/shop/travel/combat permissions, sustainable combat supplies and verified personal solo loot. Unknown access excludes before offers; selection grants no unlocks or tolerance exceptions. Every stat minimum below is an authored conservative **unboosted** gate; recommended gear/skills do not become unstated mandatory requirements. GWD common dungeon-entry route must be verified in addition to each listed faction room; necessary faction KC must already be permitted. Consumable entry fees/supplies need legal replenishment, not an infinite inventory or a finite task/key as the sole route.

| Creature | Minimum authored skills | Supported route and conditions |
| --- | --- | --- |
| Brine rat | 47 Slayer | Complete Olaf's Quest; enter Brine Rat Cavern with a usable spade route; no active task required. |
| Turoth | 55 Slayer | Fremennik Slayer Dungeon; owned usable leaf-bladed weapon, broad ammunition or Magic Dart and supplies; off-task route. |
| Cave horror | 58 Slayer | Cabin Fever complete; Mos Le'Harmless Cave; functioning light or permanent light; usable witchwood icon or sustainable Protect from Melee; off-task route. |
| Sulphur Nagua | 48 Slayer | Perilous Moons progressed far enough to enter Neypotzli Ancient Prison; repeatable off-task access. |
| Kurask | 70 Slayer | Fremennik Slayer Dungeon; usable leaf-bladed weapon, broad ammunition or Magic Dart; off-task route. Iorwerth/Wilderness/superior contexts not selected. |
| Skeletal Wyvern | 72 Slayer | Asgarnian Ice Dungeon end chamber; repeatable off-task route; owned usable icy-breath protection and sustainable combat supplies. No task-only upper-floor route. |
| Abyssal demon | 85 Slayer | Ordinary Slayer Tower upper-floor or ordinary Catacombs route; no Wilderness Slayer Cave or task-only basement; no superior/Sire credit. |
| Dark beast | 90 Slayer | Mourning's End Part II complete and accessible Mourner Tunnels route; off-task ordinary dark beasts only. No Night beast credit. |
| Spitting Wyvern | 66 Slayer | Bone Voyage complete; Fossil Island Wyvern Cave off-task area; owned usable icy-breath protection and sustainable supplies. Other wyvern species remain distinct, unselected sources. |
| Adamant dragon | No additional numeric gate; actual combat readiness | Dragon Slayer II complete; repeatable western Lithkren Vault route; post-quest only, dragonfire/poison combat readiness. |
| Rune dragon | No additional numeric gate; actual combat readiness | Dragon Slayer II complete; repeatable eastern Lithkren Vault route; post-quest only, dragonfire/shock combat readiness. |
| Demonic gorilla | No additional numeric gate; actual combat readiness | Monkey Madness II complete; repeatable Crash Site Cavern route; post-quest ordinary gorillas, sustainable two-style combat; no tortured gorilla or quest fight credit. |
| Vyrewatch Sentinel | 38 Slayer | Sins of the Father complete; Darkmeyer route and owned usable Blisterwood flail; combat only. Pickpocketed blood shards and venator drops never qualify. |
| Tormented Demon | No additional numeric gate; actual combat readiness | While Guthix Sleeps complete; repeatable Ancient Guthixian Temple route; post-quest combat, sustainable style switching. No quest-instance credit. |
| General Graardor | 70 Strength | Verified normal God Wars Dungeon entry route; hammer; Bandos Stronghold; earn ordinary 40 faction KC each entry. Solo boss kills, no team contribution or bodyguard-drop credit. |
| Kree'arra | 70 Ranged | Verified normal God Wars Dungeon entry route; usable crossbow and mithril grapple; Armadyl Eyrie; ordinary 40 faction KC each entry; solo boss kills only. |
| Commander Zilyana | 70 Agility | Verified normal God Wars Dungeon entry; unboosted 70 Agility, ropes installed for route; Saradomin Encampment, ordinary 40 faction KC each entry; solo boss kills only. |
| K'ril Tsutsaroth | 70 Hitpoints | Verified normal God Wars Dungeon entry; 70 Hitpoints river crossing; Zamorak Fortress; ordinary 40 faction KC each entry; solo boss kills only. |
| Zulrah | No additional numeric gate; actual combat readiness | Regicide complete as conservative authored access gate; sacrifice dialogue completed, repeatable boat access from Zul-Andra; solo normal encounter with sustainable combat supplies. |
| Vorkath | No additional numeric gate; actual combat readiness | Dragon Slayer II complete; Torfinn/Ungael repeatable route; post-quest solo instance only; no quest Vorkath. Dragonbone necklace is a one-roll tertiary, not a two-roll main-table item. |
| Corporeal Beast | No additional numeric gate; actual combat readiness | Repeatable lair access via permitted games-necklace route; sustainable solo strategy and supplies. No combat-level/quest access requirement invented; Wilderness walking alternative not selected. Entire kill solo, no group damage/loot allocation. |
| Phosani's Nightmare | No additional numeric gate; actual combat readiness | Sisterhood Sanctuary/Pool of Nightmares reachable through a permitted repeatable route; Drink-from solo encounter. A prior normal Nightmare kill is a warning recommendation, not a hard prerequisite. No ordinary Nightmare or historical competition-mode credit. |
| Artio | No additional numeric gate; actual combat readiness | Wilderness access explicitly unlocked; hard Wilderness diary complete (no finite-task bypass); repeatable Hunter's End entry and fee funding; solo singles-plus encounter. Player-killer risk included in authored throughput, no death exemption. |
| Spindel | No additional numeric gate; actual combat readiness | Wilderness access explicitly unlocked; hard Wilderness diary complete (no finite-task bypass); repeatable Web Chasm entry and fee funding; solo singles-plus encounter. Venenatis and its spiderlings do not count. |
| Calvar'ion | No additional numeric gate; actual combat readiness | Wilderness access explicitly unlocked; hard Wilderness diary complete (no finite-task bypass); repeatable Skeletal Tomb entry and fee funding; solo full two-phase encounter. Vet'ion, hellhounds and interrupted first phases do not count. |

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
