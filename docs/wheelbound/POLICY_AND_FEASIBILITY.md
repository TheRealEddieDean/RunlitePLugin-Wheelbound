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
- Player-menu removal/reordering, including Trade with, is explicitly prohibited. Consuming the click is technically different and now has a concrete current Hub precedent below. That is stronger evidence than API availability alone, but not feature-specific approval for Wheelbound.
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
| Player trading restriction | SUPPORTED interception, CURRENT HUB PRECEDENT; Wheelbound review outstanding | Bronzeman Unleashed preserves Trade with and consumes restricted clicks with a local message | Preserve menu entries; pursue attempt warning/interception, not menu deletion. See detailed evidence below. |
| Forced Sacrifice/Coffer | CONCRETE detector candidate; live proof outstanding | Current Coffer widget constants recovered; historical script maps displayed balance to IF1 | Correlate donation context, item/quantity, Confirm, inventory delta and refreshed balance. No reward for a click alone. |
| Owned-item snapshot/Blessings | PARTIAL | Inventory/equipment observed; bank may be stale/unavailable | Unknown is not empty; validate bank evidence before mandatory acquisition. Snapshot policy is local protection, not server item locking. |
| Pause/reconnect/account storage | SUPPORTED adapters, untested durability | GameStateChanged, account identity, RS-profile configuration, Filepath | Reset baselines, reject old callbacks and persist receipts. No claim of tamperproof runs or server-enforced restrictions. |

GE offer events send initial EMPTY states on login before server offer information arrives. Never interpret those initialization events as completed transactions or new violations. A filled offer may predate the run or occur while paused; persistent baseline and eligibility rules must decide credit before any penalty is designed.

## Precise new detector leads

[VarPlayerID](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/VarPlayerID.java), blob 752f629d4638b20d2681482970f49633fe094c52: TOTAL_VARDORVIS_AWAKENED_KILLS=3971; TOTAL_WHISPERER_AWAKENED_KILLS=3972; TOTAL_LEVIATHAN_AWAKENED_KILLS=3973; TOTAL_DUKE_SUCELLUS_AWAKENED_KILLS=3974. These constants establish a candidate source, not when the server refreshes it. Record pre-attempt values and only settle a checklist receipt after a verified qualifying increase.

[VarbitID](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java), blob 0a90c1426122a9fbf32c3002fc66b77dac8d0fc4: CA_POINTS=14815; CA_THRESHOLD_MASTER=14813. Candidate tier predicate: initialized account points >= initialized Master threshold, with threshold >0 and correct run eligibility. Cache version, first-login defaults and changes to thresholds must be tested; threshold changes alone are not automatically an earned completion.

Representative diary flags include ARDOUGNE_DIARY_EASY_COMPLETE=4458 and medium/hard/elite 4459-4461. Do not infer reward collection or reconstruct every task from those four completion flags.

Additional API blob hashes are preserved in research/API_SOURCE_PINS.json. Core AttackStylesPlugin hides selected combat widgets; this is not blanket permission for new state-dependent restrictions. The earlier file-content request returned no InterfaceID content. A subsequent Git blob fetch recovered the full current source: Coffer and trade IDs are now verified below. This corrects the earlier retrieval limitation.

## Implementation gate and proposed route

1. Implement future observation/local progression independently from hard game controls. Keep Locked/Unlocked/Banned state and physical unlock rules; do not present them as server restrictions.
2. Exclude explicit forbidden APIs and gameplay assistance from every production path, including developer toggles. Java compile target remains 11 in current official example/standard Hub build; an IDE's Java 17 installation is not the production target.
3. Before coding restrictive menus, obtain maintainer clarification for the exact trade, NPC-shop and GE behavior. No messages or submissions were sent during this audit.
4. Proposed fallback for unsupported restrictions: visible warning and an evidence-based activity log. Any additional FP penalty or replacement for hard blocking needs a recorded gameplay decision; it is not authorized merely by this report.
5. Once implementation is authorized, run controlled live detector checks: login initialization, pause, reconnect, quest incidental XP, specific bosses/chests, awakened counter refresh, Master threshold refresh, vendor variants and Coffer donations. Until then retain technical TODOs instead of manufacturing successful test results.

Sources for API details: [StatChanged](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/StatChanged.html), [Quest](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/Quest.html), [GE offer changes](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/GrandExchangeOfferChanged.html), [container changes](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/ItemContainerChanged.html), [ServerNpcLoot](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/events/ServerNpcLoot.html), [Filepath](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/util/Filepath.html), [Hub README](https://github.com/runelite/plugin-hub), [example build](https://github.com/runelite/example-plugin/blob/master/build.gradle), [standard build](https://github.com/runelite/plugin-hub-tooling/blob/master/package/src/main/resources/net/runelite/pluginhub/packager/standard-build.gradle). Rejected-features policy blob a23eb4197062173387178b8a4f3e9e5809b9c077. Policy conclusions are date-specific; new features require their own review.

## Follow-up: trade attempt warning and Death's Coffer verification

Checked 2026-10-09 following the user's clarification. These findings refine WB-D063 without discarding established access rules. No plugin implementation or live game test was performed.

### Trade with: preserve the option, intercept the attempt

The user explicitly wants Trade with to remain available in the menu and a Fate restriction popup when a restricted attempt occurs (WB-D066). This is not menu removal or reordering. MenuOptionClicked provides option, target, action type and widget context. Its consume method prevents vanilla processing of that particular event. A warning without consumption allows the request to proceed; a warning with consumption cancels the observed request. A popup alone is not a server restriction and cannot withdraw a request already sent.

A concrete, current Hub precedent exists:

- [Hub manifest: Bronzeman Unleashed](https://github.com/runelite/plugin-hub/blob/master/plugins/bronzeman-unleashed), inspected blob b9d818439cf13c0dec5abffec5dbc5773f265bce, pins 99a6b77ef9bbd20c84b72f014fd49144f982a865.
- [TradePolicy at that exact revision](https://github.com/elertan/bronzeman-unleashed/blob/99a6b77ef9bbd20c84b72f014fd49144f982a865/src/main/java/com.elertan/policies/TradePolicy.java), blob 2b3fbe5ee069309667995e67f7bbc7b7104d2ab1: identifies player Trade with actions, checks group policy, consumes disallowed attempts and calls its restriction-message service. Also handles the chat Accept trade option. Its trade-window Accept handler is commented out, so this source does not prove final-confirmation blocking.
- [BUPlugin](https://github.com/elertan/bronzeman-unleashed/blob/99a6b77ef9bbd20c84b72f014fd49144f982a865/src/main/java/com.elertan/BUPlugin.java) actually forwards subscribed MenuOptionClicked events to TradePolicy; this is connected functionality, not an unused helper.
- [BUChatService](https://github.com/elertan/bronzeman-unleashed/blob/99a6b77ef9bbd20c84b72f014fd49144f982a865/src/main/java/com.elertan/BUChatService.java) queues a formatted local message and plays a disabled sound. This proves a local message precedent, not the exact modal popup requested for Wheelbound. It does not send a public/private chat message to another player.
- [Hub update 17295](https://github.com/runelite/plugin-hub/pull/17295) merged 2026-09-29. The current manifest and pinned source, rather than an unreviewed fork or a README claim, establish the precedent. Public initial-review comments on [9396](https://github.com/runelite/plugin-hub/pull/9396) discuss networking and code review but do not provide an express blanket ruling on every form of trade cancellation.

Revised conclusion: technically feasible and supported by current Hub-listed implementation precedent. The earlier assessment had not found this evidence and was too pessimistic about practical interception. Jagex's published prohibition remains specifically menu removal/reordering; its examples also permit rejection of similar features. Do not label cancellation explicitly prohibited merely by equating it with deletion, or promise unconditional Jagex approval based on another plugin.

Proposed Wheelbound behavior: synchronously assess active-run access at the observed click; if restricted, consume only that action and queue a local dismissible notice. Suggested copy: “Fate has sealed player trading.” For a Banned NPC: “Fate has banned you from this vendor. Fate's Pardon restores them to Locked.” Locked and Banned messages must differ; player trading's exact unlock policy remains OPEN, so do not invent a player-specific Pardon. Dismiss only closes the notice; it must not replay the trade, invoke menuAction, send packets or unlock anything. Render on the appropriate UI thread; no blocking sleep, global keyboard hook, KeyboardFocusManager, focus stealing or minimize behavior. A nonmodal notice with a clear dismiss action is the proposed implementation choice; an input-capturing modal requires its own review and input tests.

| Interaction | Available evidence | Required coverage check |
| --- | --- | --- |
| Outgoing player Trade with | MenuOptionClicked; connected Bronzeman interception | Left/right click, player actions and target normalization; do not hardcode a single player-option slot |
| Incoming chat Accept trade | Existing Bronzeman option handler | Alternate request acceptance paths and configuration variants |
| Trade already open when restriction changes | TRADEMAIN=335; TRADECONFIRM=334 in current InterfaceID | First/final Accept, keyboard shortcuts, activation and pause/resume; no automatic decline/close packet |
| Restricted NPC vendor | NPC action plus shop identity | Duplicate names, transformations, alternate dialogue shops and packet-free warning; do not treat every NPC Trade option as player trading |
| GE | Offer and interface events | Existing offers, offline fills, history initialization and alternate purchase paths; player trade interception is not GE coverage |

Pause disables restrictions immediately under the confirmed rule. Unknown/corrupt state must not produce arbitrary new penalties. Logging an attempt is not proof that a trade completed. Existing notices must not duplicate or block the entire game when repeated clicks occur. Scope every restriction to the active character/run and clear transient UI on switch/shutdown.

### Donation verification: stronger evidence than disappearance

Current [InterfaceID source](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/InterfaceID.java), blob f4daad3ea3bc34403ad21f5c539110cfefd2e683, establishes:

| Symbol | Value | Meaning established by source |
| --- | --- | --- |
| DEATH_COFFER | 670 | Coffer interface group |
| DEATH_COFFER_SIDE | 671 | Coffer side group |
| DeathCoffer.LEFT | 0x029e0003 | Left parent; inspect dynamic children |
| DeathCoffer.DISPLAY | 0x029e0008 | Selection display parent |
| DeathCoffer.CONFIRM | 0x029e000d | Confirm widget |
| DeathCofferSide.ITEMS | 0x029f0000 | Side items widget |
| DEATH_OFFICE | 669 | Separate reclaim interface; not donation proof |

Names alone do not prove the content of a child or event order. This closes the earlier missing-ID research gap, not live verification.

The decompiled [RuneStar script snapshot](https://github.com/RuneStar/cs2-scripts/tree/7da6c1fbab51b7528ff13b15a532a44208bf75d6) is dated 2021-09-16, cache revision 199.1. Historical scripts 3479-3483 render Coffer balance from varp 261 and selected slot/quantity/unit quote from 262-264. Current VarPlayerID still names these IF1=261, IF2=262, IF3=263, IF4=264. They are generic interface variables, not permanent account-wide Death's Coffer counters. Read them only while the verified Coffer context is active; another interface can reuse them. A positive IF1 change elsewhere must never award a donation. The old mapping is a testable lead, not a verified 2026 transaction receipt.

[Historical initialization script](https://github.com/RuneStar/cs2-scripts/blob/7da6c1fbab51b7528ff13b15a532a44208bf75d6/scripts/%5Bclientscript%2Cdeath_coffer_init%5D.cs2) constructs balance text dynamically beneath a supplied parent and subscribes to varp 261. [Historical balance procedure](https://github.com/RuneStar/cs2-scripts/blob/7da6c1fbab51b7528ff13b15a532a44208bf75d6/scripts/%5Bproc%2Cdeath_coffer_setcontents%5D.cs2) renders empty, singular or comma-separated balance. Do not assume a fixed static text child. Historical selection procedure 3483 looks up the inventory slot, caps requested quantity to possession and displays quoted value. Button timers/animations are local presentation and cannot prove server acceptance.

Proposed evidence sequence, requiring current-client validation before automatic rewards:

1. Open the real Coffer interface. Wait for initialized balance/selection/inventory; zero is valid only when initialization is known, not a default at login.
2. Freeze pending obligation ID, account/run, raw and resolved item ID, exact quantity, Blessed protection, available inventory quantity, balance before and server-displayed quote. Distinguish base item value from the 105% credit; never reverse-engineer reward value by dividing an unverified balance delta.
3. Observe the genuine Confirm interaction and any subsequent OSRS confirmation stage. Mark attempted only. Player performs every game action; Wheelbound sends none.
4. Collect matching inventory reduction and refreshed Coffer balance increase in the same bounded transaction context. Require quote/quantity consistency and no competing transaction. A before/after increase establishes credited value; item identity comes from the frozen selection and matching inventory delta, not the balance by itself.
5. Settle the obligation and reward once in one durable receipt. Store before/after credit, item/quantity, source version and evidence. Spin conversion remains an unresolved balance decision; this detector does not approve new reward numbers.

Exact quote rounding, update order, confirmation route and timeout remain unverified. Observe multiple ticks rather than assume atomic event delivery. If the UI closes, IF1 resets or reconnect intervenes before an unambiguous receipt, retain pending verification; do not treat reset as a withdrawal or invent credit. Reopening can confirm a balance but cannot by itself identify a missing transaction. No manual fallback automatically becomes verified evidence. Even a strong receipt is local observation, not tamperproof server attestation.

A success chat line, if the current game supplies one with useful donation details, could supplement the receipt. No exact current success line was verified in this research, so no fabricated parser/string is specified. Accepting an item selection, clicking Confirm, a quote of at least 10k, inventory loss or interface closure alone is insufficient. Failed donation, full Coffer, ineligible/recently restricted items, insufficient quantity, Blessed item, changed selection, dropping/banking/alching/trading and another interface reusing IF1 must produce no reward.

### Other plugins: what the comparison actually establishes

| Plugin/source inspected | Useful finding | Limitation |
| --- | --- | --- |
| Bronzeman Unleashed, pinned Hub revision above | Connected consume-and-local-message trade policy | No implemented final-window Accept enforcement; no blanket popup/shop approval |
| Trade Tracker, Hub manifest pins botanicvelious/runelite-trade-tracker at 1d25f068b8085405f0356e94e36cbbdc0736794b | TradeManager combines trade widgets and offered-item containers with TRADE chat Accepted trade. before recording completion | Player trade receipt is separate from donation; do not use its message for Death |
| Death's Coffer Advisor, Hub pins Sintry1/deaths-coffer-advisor at 27f709792e701935069ad69ce6c48317d4adef6e | Bank scan estimates useful items, 10k threshold and 105% valuation | No donation/balance receipt handler; an advisory list is not authoritative eligibility |
| Dude Where's My Stuff, source 2f636ab8f75400219b6e35dd0412b42f510e528a | Coin storages and death-storage tracking; some other coffers parse specific chat receipts | Inspected coin registry includes other coffers, not a Death's Coffer tracker; death storage is not donation |
| Bronzeman ShopPolicy and GrandExchangePolicy at pinned revision | Shop item checkmark overlay and GE search-result restrictions | ShopPolicy does not block vendor purchases; GE search filtering does not prove comprehensive GE prevention |

Primary peer sources: [TradeManager](https://github.com/botanicvelious/runelite-trade-tracker/blob/1d25f068b8085405f0356e94e36cbbdc0736794b/src/main/java/org/asundr/trade/TradeManager.java), [Coffer Advisor](https://github.com/Sintry1/deaths-coffer-advisor/blob/27f709792e701935069ad69ce6c48317d4adef6e/src/main/java/com/deathscoffer/DeathsCofferPlugin.java), [coin registry](https://github.com/Thource/dude-wheres-my-stuff/blob/2f636ab8f75400219b6e35dd0412b42f510e528a/src/main/java/dev/thource/runelite/dudewheresmystuff/coins/CoinsStorageManager.java), [ShopPolicy](https://github.com/elertan/bronzeman-unleashed/blob/99a6b77ef9bbd20c84b72f014fd49144f982a865/src/main/java/com.elertan/policies/ShopPolicy.java), [GrandExchangePolicy](https://github.com/elertan/bronzeman-unleashed/blob/99a6b77ef9bbd20c84b72f014fd49144f982a865/src/main/java/com.elertan/policies/GrandExchangePolicy.java). Avoid transferring peer implementation quirks or old file/thread patterns into Wheelbound. Initial Wheelbound Gson/focus blockers remain mandatory constraints.
