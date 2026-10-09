# RuneLite policy and Wheelbound feasibility audit

Checked 2026-10-09. Research only: no plugin code, game/account interaction, live acceptance test or reviewer contact. Keep Jagex gameplay rules, RuneLite Hub implementation rules, API availability and actual detector coverage distinct. This is an evidence-backed design assessment, not approval of the future mode.

## Original submission: actual blockers and resolution

[Plugin Hub PR 16702](https://github.com/runelite/plugin-hub/pull/16702) was merged on 2026-09-24, not permanently rejected. Its final manifest references Wheelbound commit c8cc38ac9a7ab74d9bc10125388da7cc75af99fa. The public discussion exposes two blockers; private reviewer Discord threads were not accessed and may contain additional context.

| Evidence | Finding | Future implementation requirement |
| --- | --- | --- |
| [2026-09-17 build comment](https://github.com/runelite/plugin-hub/pull/16702#issuecomment-5721886358), [failed build job](https://github.com/runelite/plugin-hub/actions/runs/35269399948/job/105364618393) | Packaging rejected fresh Gson construction. Java compilation ran; a generic build failure should not be misreported as a gameplay-rule violation. | Inject RuneLite's Gson; pass it into persistence helpers. Customize only through the injected instance's builder. |
| [2026-09-18 focus-manager comment](https://github.com/runelite/plugin-hub/pull/16702#issuecomment-5723404433), [fix commit](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound/commit/c8cc38ac9a7ab74d9bc10125388da7cc75af99fa) | KeyboardFocusManager was forbidden. Fix removed the global focus-owner check from WheelPopup.typing, leaving the event-component text-field check. | Never restore global focus-manager access. Scope local spin/dismiss keys to the plugin UI and respect text entry. |

The first Hub manifest commit f502125f7b2b0f626836fc8b5d1b735fa2262233 targeted a15e8949ad843733e82701ef019f4e64b4f99034. That CustomWheels source instantiated Gson in save/load. Accepted CustomWheels takes a supplied Gson instead. Accepted blob cdfe5c2fea1e87ef8fdcda25d84bed87aeb7a511; initial blob 33dedc43916d384989d595a37c4243d8b9d6f563. Hub commit b8dd1625688964638197c2ba480e42f5ec2ad522 updated the final manifest after the focus fix. Resource/icon changes occurred in the history, but the retrieved public comments/log do not establish them as additional rejection reasons.

Existing docs/RELEASE_AUDIT.md is a historical pre-submission audit. Its claims are not a replacement for the actual CI result or future mode review. Initial acceptance covers the activity randomizer at the submitted revision, not every later enforcement feature. [RuneLite review policy](https://github.com/runelite/runelite/wiki/Plugin-Hub-Review) covers security and gameplay rules; it does not establish correctness, performance or complete event coverage.

## Policy boundaries

Sources: [Jagex client guidelines](https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1), [current game rules](https://legal.jagex.com/docs/rules/rules-of-old-school-runescape.md), [macro/client restrictions](https://legal.jagex.com/docs/rules/macro-and-client-features-not-permitted.md), [RuneLite rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features).

- Do not generate gameplay clicks/keys, attacks, movement, donations, chat, or direct world communication. Plugin buttons change local Wheelbound state; players perform OSRS actions.
- Do not add combat assistance that predicts attacks, selects prayers, signals boss-mechanic timing or automatically marks safe/unsafe positions. A completion checklist is different from a live boss-solving overlay; that distinction is our interpretation, not advance approval.
- Player-menu removal/reordering, including Trade with, is explicitly prohibited. Consuming the click instead is not an established compliant workaround.
- Conditional NPC/shop/GE interception is review-sensitive. A consume API exists, but this specific challenge enforcement has no obtained reviewer approval.
- Avoid window/focus manipulation, reflection, native code, subprocesses and runtime executable downloads. Use Java, injected Gson/OkHttp, scoped Filepath and classpath resource streams. No credential access or localhost player-data server.
- Do not ship an encounter simulator. Offline design/economy tooling remains outside the plugin JAR.

Local FP/card/vendor randomness does not pay OSRS items or GP, accept player stakes, or enable transferable betting. That separates the proposed design from the item/GP wagering described in Jagex's games-of-chance rule; it is an inference. Do not introduce staking, purchasable FP for real money, operator item payments or OSRS prize pools. Actual sacrifice remains a player-performed donation to Death's Coffer.

## Capability and evidence matrix

SUPPORTED means a relevant API/source exists, not a live-tested detector or final Hub permission. REVIEW means proposed action needs policy clarification. INCOMPLETE means no sufficient receipt/coverage was established.

| System | Finding | Evidence/limits | Design consequence |
| --- | --- | --- | --- |
| Wheels/cards/tree/FP/spins | SUPPORTED local UI/domain state | Existing accepted randomizer and Swing/overlay structures | Weighted random choices, costs and obligations can remain client-local; no server actions from Spin. |
| Skill XP/combat bundle | SUPPORTED totals | StatChanged supplies skill, XP and levels | Compare account/session baselines; XP alone does not identify method, tool, source or incidental quest reward. |
| Challenge tool/area restrictions | PARTIAL | Equipped items, position and menus can supplement XP | Freeze predicates and validate per method; avoid penalizing ambiguous attribution or assuming last click proves every XP tick. |
| Quests | SUPPORTED state, PARTIAL eligibility | Quest.getState; accepted QuestPool reads journal scripts | Completion can be tracked; real items, route, shops and team requirements need separate catalogs. |
| CA tasks/Master tier | SUPPORTED candidate signals | Existing completion-varp task bitsets; CA_POINTS and CA_THRESHOLD_MASTER | Read transmitted threshold instead of inventing a fixed current point number. Test initialization and cache mappings. |
| Diaries/bounties | SUPPORTED candidate flags/events | Named diary-completion varbits; loot and container events | Completion versus reward claim must be distinguished. Item possession does not prove a fresh eligible drop. |
| Boss kills/drops | PARTIAL | NpcLootReceived, ServerNpcLoot and encounter-specific chat/cache signals | Count only verified assignment evidence; despawn is not a universal kill. Group, raids, chests and lootless kills need separate tests. |
| Awakened DT2 checklist | STRONG candidate counters | Four named awakened-kill varps | Baseline each account; credit qualifying positive deltas, not an owned Blood Torva piece or ordinary boss kill. Transmission timing still needs live tests. |
| Inferno/Colosseum/Radiant set | PARTIAL | Completion/reward data must be mapped individually | Do not use cape/quiver/set possession alone to prove a new qualifying completion. No prayer/position helper or encounter simulation. |
| Physical vendor visit | PARTIAL | NPC interaction, position and WidgetLoaded can correlate a shop | Nearby/rendered NPC alone is not proof of a visit. Duplicate names, transformations and quest shops need a stable catalog. |
| Locked/Banned vendor and GE access | REVIEW plus coverage work | MenuOptionClicked.consume can suppress vanilla handling; GE offer events observe state | No promise of comprehensive hard blocking. Existing offers/offline fills, alternate inputs and other plugins need handling. |
| Player trading restriction | POLICY CONFLICT | Jagex prohibits removing/reordering player options | Preserve the gameplay intent in docs; no implementation through menu deletion. Advisory alternative is PROPOSED, not silently approved. |
| Forced Sacrifice/Coffer | INCOMPLETE transaction proof | Container decrease is shared by drop/bank/consume/trade; no verified Coffer receipt found | Require correlated accepted donation and value evidence. No spin award for a click or unexplained disappearance. |
| Owned-item snapshot/Blessings | PARTIAL | Inventory/equipment observed; bank may be stale/unavailable | Unknown is not empty; validate bank evidence before mandatory acquisition. Snapshot policy is local protection, not server item locking. |
| Pause/reconnect/account storage | SUPPORTED adapters, untested durability | GameStateChanged, account identity, RS-profile configuration, Filepath | Reset baselines, reject old callbacks and persist receipts. No claim of tamperproof runs or server-enforced restrictions. |

GE offer events send initial EMPTY states on login before server offer information arrives. Never interpret those initialization events as completed transactions or new violations. A filled offer may predate the run or occur while paused; persistent baseline and eligibility rules must decide credit before any penalty is designed.

## Precise new detector leads

[VarPlayerID](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/VarPlayerID.java), blob 752f629d4638b20d2681482970f49633fe094c52: TOTAL_VARDORVIS_AWAKENED_KILLS=3971; TOTAL_WHISPERER_AWAKENED_KILLS=3972; TOTAL_LEVIATHAN_AWAKENED_KILLS=3973; TOTAL_DUKE_SUCELLUS_AWAKENED_KILLS=3974. These constants establish a candidate source, not when the server refreshes it. Record pre-attempt values and only settle a checklist receipt after a verified qualifying increase.

[VarbitID](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java), blob 0a90c1426122a9fbf32c3002fc66b77dac8d0fc4: CA_POINTS=14815; CA_THRESHOLD_MASTER=14813. Candidate tier predicate: initialized account points >= initialized Master threshold, with threshold >0 and correct run eligibility. Cache version, first-login defaults and changes to thresholds must be tested; threshold changes alone are not automatically an earned completion.

Representative diary flags include ARDOUGNE_DIARY_EASY_COMPLETE=4458 and medium/hard/elite 4459-4461. Do not infer reward collection or reconstruct every task from those four completion flags.

Additional API blob hashes are preserved in research/API_SOURCE_PINS.json. Core AttackStylesPlugin hides selected combat widgets; this is not blanket permission for new state-dependent restrictions. InterfaceID retrieval returned no content, so no shop/Coffer widget IDs were verified.

## Implementation gate and proposed route

1. Implement future observation/local progression independently from hard game controls. Keep Locked/Unlocked/Banned state and physical unlock rules; do not present them as server restrictions.
2. Exclude explicit forbidden APIs and gameplay assistance from every production path, including developer toggles. Java compile target remains 11 in current official example/standard Hub build; an IDE's Java 17 installation is not the production target.
3. Before coding restrictive menus, obtain maintainer clarification for the exact trade, NPC-shop and GE behavior. No messages or submissions were sent during this audit.
4. Proposed fallback for unsupported restrictions: visible warning and an evidence-based activity log. Any additional FP penalty or replacement for hard blocking needs a recorded gameplay decision; it is not authorized merely by this report.
5. Once implementation is authorized, run controlled live detector checks: login initialization, pause, reconnect, quest incidental XP, specific bosses/chests, awakened counter refresh, Master threshold refresh, vendor variants and Coffer donations. Until then retain technical TODOs instead of manufacturing successful test results.

Sources for API details: [StatChanged](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/StatChanged.html), [Quest](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/Quest.html), [GE offer changes](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/GrandExchangeOfferChanged.html), [container changes](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/ItemContainerChanged.html), [ServerNpcLoot](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/events/ServerNpcLoot.html), [Filepath](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/util/Filepath.html), [Hub README](https://github.com/runelite/plugin-hub), [example build](https://github.com/runelite/example-plugin/blob/master/build.gradle), [standard build](https://github.com/runelite/plugin-hub-tooling/blob/master/package/src/main/resources/net/runelite/pluginhub/packager/standard-build.gradle). Rejected-features policy blob a23eb4197062173387178b8a4f3e9e5809b9c077. Policy conclusions are date-specific; new features require their own review.
