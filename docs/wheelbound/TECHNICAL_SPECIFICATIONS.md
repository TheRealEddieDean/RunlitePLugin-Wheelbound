# Technical specifications and design consequences

Research checkpoint 2026-10-09. This file expands every feature in the user's feasibility table. It specifies evidence, event handling, coverage and concrete design consequences. No plugin code or live-client test was performed. SOURCE-CONFIRMED identifies inspected source; SELECTED DESIGN identifies a new choice under the user's delegated design authority; VALIDATION GATE identifies a future runtime/reviewer check, not an unanswered design proposal. Existing confirmed gameplay rules still take precedence.

## Evidence contract used by all systems

Every observation carries character identity, run ID, session generation, catalog version, tick/time, assignment or attempt ID and source. Separate raw observations, qualifying evidence, committed local state and claimed rewards. One real completion may generate chat, item, varp and loot events; merge them into one encounter/transaction receipt rather than pay four times. Duplicate identical successive completions still need separate occurrence identities; item ID alone or a timestamp rounded to one second is not an event key.

States: initializing, ready, observing, evidence pending, verified, settled, unavailable. Unavailable is never successful, empty, abandoned or cheating. A catalog/version mismatch disables the affected detector and warns before accepting a new obligation. Preserve existing assignments and receipts. Do not clear a missing bank to an empty bank or erase a pending donation to repair state.

Activation, resume, login and world hop need fresh account-scoped baselines. Do not import unobserved XP, drops or completions. Switching account/session invalidates old callbacks. Pause remains immediate, free and penalty-free; it retains the assignment and grants no paused progress. Observations collected while paused may update the baseline but never the reward ledger. Do not apply retrospective FP penalties based on a counter difference spanning unobserved play.

## Local wheels, cards, FP, tree and Fate Shop

Assessment: SOURCE-CONFIRMED feasible local state/UI. The Fate Shop is Wheelbound UI; OSRS NPC shop restriction is a different adapter.

Commit random offers, wheel result, card selection, prices, FP/spin changes and resulting obligations durably before animation. Button animation is presentation, not a second random draw. Use local transaction preconditions/revision IDs and disable repeat clicks while committing. Validate five ordinary slices/three distinct activities and 24 total slots including Taint/Sacrifice in every wheel mutation. Tier-only enhancement prices and per-activity duplicate escalation remain unchanged. Pardon stays unlimited at its fixed price, Banned -> Locked.

No server-side FP, official hiscores, mobile-client synchronization or anti-cheat guarantee exists. Keep authoritative run/journal data local to the character and use RuneLite configuration for preferences/identity as appropriate. Config synchronization is not an atomic distributed transaction. SELECTED DESIGN: one actively writable run on one device at a time; do not merge conflicting wallets by taking the greater FP/revision. Retain conflict copies and present recovery. This requires persistence crash/conflict tests during implementation, not changes to gameplay costs.

## XP, methods, tools and restricted cards

Assessment: XP totals supported; universal method attribution unavailable. StatChanged supplies skill, total XP and levels. Core XpTrackerPlugin ignores login XP synchronization while initializing, then offsets offline changes instead of treating them as active gains. Wheelbound needs its own baselines; it must not depend on another plugin being enabled or its user-reset session totals.

Ordinary XP objective: snapshot baseline, accumulate qualifying positive deltas while the assignment is active, stop once target reached and settle once. A real level change does not regenerate the chosen target. Level 99 is not the XP ceiling: use actual remaining XP headroom. Do not offer an XP objective if remaining headroom cannot accommodate two distinct legal starting-card targets. Do not silently substitute collection tasks or manufacture impossible XP above the game cap.

SELECTED DESIGN (WB-D069): restricted Challenge/Wilderness/Punishment templates come from a maintained, versioned detector catalog. Each specifies allowed method, tool family, location/plane/instance semantics, game prerequisites, source signals, by-products, ambiguity conditions and negative cases. A purchased card type permanently remains unlocked, but only currently supported/eligible templates enter its draw pool. Unknown detector availability cannot silently turn Challenge into unrestricted Standard after selection.

| Template class | Candidate detector | Design/coverage rule |
| --- | --- | --- |
| Plain assigned-skill XP | StatChanged deltas | Feasible without method inference; do not invent lamp/quest exclusions absent an approved rule |
| Area-only XP | XP plus historical position samples | Define polygon, plane, instance mapping and when the action earns XP; no last-click-only proof |
| Woodcutting tool restriction | Named tool-specific animations, item/equipment state, harvesting messages and XP | Core has bronze through advanced axe animations; forestry/chest/minigame XP must be distinguished |
| Mining tool restriction | Local animation, tool state, mining chat/product/XP | Core MiningPlugin recognizes activity chat; generic “mining session” does not prove pickaxe tier or exact ore |
| Exact product/resource count | Source-specific success event plus item/output context | A container gain may be bank withdrawal or trade; no blanket event-to-item conversion |
| Wilderness challenge | Area predicate plus supported method evidence | Define wilderness membership including cave/instance behavior; no PvP menu/combat assistance |
| Farming/passive/batched/minigame XP | Dedicated adapter and acquisition timing | Do not assign a restriction based merely on current equipment/position at delayed XP delivery |
| Combat XP bundle | Selected skill/style and mapped by-products | Legitimate HP exemption persists; shared/defensive style needs explicit mapping rather than broad exemption |

A tool merely equipped or present does not prove it was used. Another usable tool in the inventory, charged variants and special activities can matter. Animation identifies action context, not necessarily successful output; combine it with actual XP/source evidence. Avoid copying core session heuristics as strict punitive detectors.

Keep a short action-context history across event ordering. Associate XP with verified action intervals; queue ambiguous batches pending more evidence. Do not invent a universal two-tick grace rule or charge FP from an ambiguous location/tool sample. Known out-of-assignment skill XP can use the existing tolerance mechanism; method-specific violations need positive evidence. This does not change the 1,000-XP tolerance proposal or approve a new penalty formula.

For a catalog break during an active restricted Fate, retain the obligation and show detection unavailable with free Pause. Before release each offered template must pass positive/negative runtime traces; unsupported templates are withheld before assignment, not rolled and then waived. This is a bounded content scope, not a promise to infer every OSRS training method.

## Quest completion and eligibility

Assessment: completion supported; “can complete this now” needs authored access requirements. Quest.getState and the existing QuestPool use released quest DB rows/journal state. Existing source excludes miniquests/subquests and represents Recipe for Disaster as a parent; changing that is a separate content decision.

SELECTED DESIGN (WB-D070): separate game start requirements from Wheelbound access feasibility. Label the filter “Meets game requirements” unless the complete route has been curated. Skill/quest checks do not prove needed items, partner availability, route access, membership, quest purchases or affordable vendor unlocks. Quest Helper demonstrates structured requirements including boostable skills and item/bank/zone checks; inspect its metadata as a catalog aid, not a required runtime plugin or exhaustive authoritative access oracle.

Each offered quest needs a supported completion mapping and dependency entry. Gate quests with unavoidable currently inaccessible purchases until their required vendor/access unlock or a documented legal acquisition route is available. Do not exempt quest vendors automatically, unlock shops for free or promise no deadlocks from the journal check. Preserve the physical vendor visit requirement.

Record not-started/in-progress/complete baseline. A new completion transition while assigned and active settles once. Completed on login, completed while paused, or a chat congratulations line without the corresponding assigned quest state does not qualify as fresh completion. RFD multipart progression needs its explicit parent/subquest policy; do not claim a subquest finishes the parent. Quest-reward XP exemptions need the assigned quest's mapped reward context; a broad “questing is active” flag must not exempt unrelated training forever.

## CAs and all five Grand Fates

Assessment: concrete passive adapters identified. Do not use combat phase, attack, prayer, safe-tile or boss-target signals to help play the fight. A receipt/checklist after completion is the intended scope.

CA metadata: tier enums 3981-3986, task structs with task ID 1306, name 1308, description 1309, encounter 1312, encounter-name enum 3971. Completion uses task ID / 32 to select the named CA_TASK_COMPLETED_n varp and task ID % 32 for its bit. Current source has indices 0-20; an inspected peer DataLoader stops at 19. SELECTED DESIGN: validate the highest task ID against the entire supported current varp array; do not copy an older array and misclassify new tasks as incomplete/complete. Cache names/structs by version; account flags refresh on initialized login and relevant changes.

Master CA: CA_POINTS=14815 versus CA_THRESHOLD_MASTER=14813. Require initialized positive threshold and a qualifying progression context. The tier is a point threshold, not “all Master-difficulty tasks.” Ghommal's hilt possession is not a fresh tier receipt. A threshold reduction or retroactive game update is not proof the player earned an active task. Reconcile task transitions and points; do not reward an unexplained initialization delta.

| Grand Fate | Selected passive evidence path | Do not accept |
| --- | --- | --- |
| Inferno | Encounter-specific server completion/KC message in an active Inferno attempt, corroborated by fresh Infernal Cape reward inventory evidence | Zuk HP=0/despawn, an old cape, collection-log complete flag, historical hiscore lookup |
| Fortis Colosseum | Qualifying completed encounter plus fresh Dizana's Quiver reward/claim context; observe COLOSSEUM_REWARDS container and actual claim | Any reward chest opening, cashing out earlier, lifetime highest-wave/glory alone, old quiver |
| Awakened DT2 | Positive deltas in four awakened kill totals 3971-3974 within eligible active observation; persist per-boss receipts | Normal boss KC, Blood Torva ownership, a counter difference spanning paused/offline play |
| Master CAs | Initialized points/threshold and eligible task progression; preserve evidence separately from manually claiming an in-game hilt | Hilt withdrawal, paused points, generic threshold default or changed threshold alone |
| Radiant Oathplate | Fresh supported Yama sigil/contract evidence and three component creation receipts for helm/chest/legs; preserve multipart evidence | Normal Yama KC, one sigil fragment, tradeable base Oathplate, pre-owned radiant pieces or bank withdrawals |

Useful constants: PLAYER_IN_INFERNO=11878 is context only. COLOSSEUM_HIGHEST_WAVE=11410 and COLOSSEUM_KILLTIME_LATEST=9812 are corroborating state, not universal completion counters. Colosseum core LootTracker reads group COLOSSEUM_REWARD_CHEST_2=864 and InventoryID.COLOSSEUM_REWARDS; the older InventoryID alias names chest container 843. Reward/claim UI includes group 866. Do not assume any chest loot equals a final encounter. Use the actual versioned event/reward contract rather than an arbitrary hardcoded wave-count rule.

Radiant IDs: 30777 helm, 30779 chest, 30781 legs; Purifying Sigil 30793 and fragments 30783/30785/30787/30789/30791. YAMA_CONTRACT_FIGHT=914 exposes CONTRACT_NAME=0x03920003 as context. TOTAL_YAMA_KILLS=4701 is normal aggregate, not proof of a required contract. No per-contract permanent completion counter was established. Selected approach retains signed-contract identity and a fresh qualifying sigil reward/creation transaction rather than claiming the aggregate verifies all variants.

Jagex's 2025-06-04 update explicitly reduced Aether Runes to 2,500 per radiant piece, or 7,500 for three pieces. It also retroactively completed Contractually Unbound for some existing item owners. This resolves the old 10,000/2,500 conflict and shows why a CA transition alone may not prove a fresh Yama run. The current confirmed goal remains full Radiant armour, not merely five contract kills. Do not change it to a generic Yama kill-count goal.

VALIDATION GATES: exact current server completion strings, normal/variant encounter identities, counter transmission timing, quiver claim routes and radiant creation/fragment receipt sequence. Source availability supports this design but no runtime trace was captured. Item/loot and chat must join to one receipt; if only part survives a disconnect, keep evidence pending without awarding or accusing the player.

Recovered S8-M0327 confirms new-account mode scope (WB-D088), so existing-Master grandfathering is outside initial support. The exact fresh-account activation predicate remains TECH_TBD; technical adapters must not invent Grand level/playtime gates, exclude sealed outcomes or auto-win. S8-M0321 requires no active Fate for Grand activation (WB-D089).

## Physical vendor visit and unlock transaction

Assessment: supported correlated visit design, catalog required. MenuOptionClicked identifies action/target/type; an NPC action identifier is not automatically the permanent vendor ID. Resolve the actual NPC instance/composition on the client thread and map transformed/variant IDs to a catalog vendor identity.

SELECTED DESIGN (WB-D072): allow the player to open a Locked/Banned vendor's shop for inspection/identity, but gate purchases and sales through the transaction adapter. Opening a shop alone is not an unlock. This permits real visit evidence and a local unlock/Tempt/Pardon panel without remotely buying access or hiding NPC menu options.

Visit proof requires the selected vendor's real interaction, the player reaching its valid catalog location, and the expected opened shop/dialogue context. Clicking a distant NPC while pathing is attempted interaction, not arrival. Sample location when the interface actually opens. A reused shop title, NPC name, proximity or unrelated WidgetLoaded cannot unlock the nearest guessed vendor.

Catalog entry: vendor key, variant/transformed NPC IDs, locations/instance semantics, shop/dialogue group, title/stock corroboration, vendor class, currency, quest changes, shared stock versus shared unlock decision, and supported buy/sell routes. Stable vendor identity and stock group are separate. Two vendors sharing stock do not automatically share Wheelbound unlock status.

Normal shop group SHOPMAIN=300 and Shopmain.ITEMS=0x012c0010 are confirmed. Omnishop and dialogue/nonstandard interfaces require their own entries. Invalidate transient visit context on closing/leaving, unrelated interaction, world hop and character switch. Recheck it at the FP purchase/Tempt confirmation; persist the result before enabling transactions. Visiting a Banned vendor does not restore them. Pardon remains Banned -> Locked; its own proximity rule is still historically UNVERIFIED.

Unknown shop context: show catalog unavailable; do not debit FP, associate it with an arbitrary vendor, or silently label it Unlocked. Do not offer a quest that depends on an unsupported unavoidable vendor route. Viewing/examining and closing remain available; no forced close packet.

## Shop, GE and player-trade interception

Assessment: individual menu/widget operations can be intercepted; server enforcement and universal routes cannot be promised. Jagex prohibits player menu removal/reordering. Preserve Trade with and the user's Fate notice requirement. Current Hub-pinned Bronzeman Unleashed and Another Bronzeman Mode consume Trade with/Accept trade and display local messages. Those are concrete precedents, not advance approval of every Wheelbound widget/input restriction.

SELECTED DESIGN (WB-D073): guard transaction actions, not entire world interaction. Use exact action/widget/group plus known access state. Never intercept every option called Accept or Trade indiscriminately. No injected donation/buy/decline/cancel action, no automatic replay after unlock, no focus manager, and no new gameplay penalty on attempts.

| Surface | Required guard coverage | Keep available |
| --- | --- | --- |
| Normal shop | Buy/sell variants, selected-quantity left click, right click, X dialog final path, shop side inventory | Inspect/value/examine/close, local unlock/Tempt flow |
| Dialogue/omnishop | Catalog-specific purchase/confirmation actions | Non-purchase quest dialogue; no universal string matching |
| GE new offers | New buy/sell, item choice and final setup confirm | Viewing existing state and deliberate player cleanup as below |
| GE alternate offers | Repeat/history shortcut, Modify Offer and final modified confirmation | Cannot rely only on search-result filtering |
| Player trade | Outgoing player option, incoming Accept trade, first Accept 0x014f000a, final TRADE2ACCEPT 0x014e000d | Original menu option/order, manual decline/close; pause immediately releases restriction |

MenuOptionClicked covers left/right menu actions; it does not establish every keyboard/client-script path. A delayed WidgetLoaded warning cannot undo a packet already sent. Treat first/final widget guards as required coverage, not proven merely by constants. Bronzeman's final-window handler is commented out. Search-filter hiding in two peers does not cover GE repeat/modify/sell routes; peers' shop overlay is visual, not vendor blocking.

SELECTED DESIGN, provisional access consequence: before starting/resuming with GE locked, inspect fully initialized slots and require player-performed cancellation/cleanup of live offers. Allow local cleanup/viewing rather than trap their existing assets. Pause is still immediate/free; resuming active restricted play waits for the required market-state check, with no FP loss/Assisted label. The plugin must never cancel offers on behalf of the player. This is a new proposal under delegated authority, not recovered historical approval; any choice to grandfather existing offers instead must explicitly revise this policy.

Offer-event initialization emits EMPTY for all slots before actual server offers arrive. Do not treat the initial eight EMPTY events as proof there are no offers. Require reliable initialized exchange state; if not determinable without opening the GE, ask the player to open it for the readiness check. No arbitrary timeout makes unknown offers empty. Bind offer identity to slot+observed lifecycle; slot index alone is reusable. Once GE is unlocked, fills can be observed but their items are not automatically eligible drop bounties.

Existing server offers can fill while paused/offline and the plugin cannot prevent that. Cleanup at activation reduces this problem, but does not enforce against another client, disabling the plugin or unobserved activity. This mode remains voluntary and locally observed. Do not silently penalize or retrofit a transaction the plugin never observed. Feature-specific Hub review remains an external release gate; no reviewer messages were sent.

## Death's Coffer: current viable verification design

Assessment: visible transaction evidence is available; exact event order/credit refresh still needs runtime validation. Current groups 670/671 and Confirm 0x029e000d are known. Historical 2021 scripts map displayed balance to shared IF1=261, selection/quantity/quote to IF2-IF4. This is a candidate acceleration, not a permanent Coffer variable.

SELECTED DESIGN (WB-D068 refined): use the actual Coffer UI's displayed initialized balance, selected item/quantity and server quote as primary observation. The historical variable mapping is optional corroboration; do not make the design depend on its being unchanged. Read dynamic children in the verified group, not a guessed global text child. Display changes can break a parser, so version/health-check it before allowing an irreversible donation as a Wheelbound task.

Ready transaction requires obligation/account/run, inventory selection identity and quantity, Blessed eligibility, current game acceptance/quote, initialized before balance and sufficient credit headroom. Then player confirms in OSRS. Require matching inventory decrease and refreshed positive Coffer credit in the same isolated transaction context. Confirm alone is attempted. Correlate actual credit against actual quote; base GE value versus credited 105% amount and rounding must be mapped before any FP/spin conversion. Do not replace the game's quote with a live-market price API.

Reject failed/full/ineligible donation, changed selection, insufficient quantity, dropping/banking/alching/player trading and shared IF-variable changes elsewhere. If the Coffer closes/reset occurs before reliable after-balance capture, retain pending evidence; reopening may reconcile but cannot invent the item identity/transaction. Do not require an unverified success chat string. Do not automatically waive mandatory sacrifice or charge another spin because evidence is pending.

Official Death Changes confirms per-item >=10,000 GP and 105% credit; the threshold is not a sum of cheap items. Later Jagex updates change excluded item categories, so threshold alone is insufficient. The user's acquisition exception remains >=10k and the game must accept the item. A fully funded Coffer or unsupported account type is an eligibility limitation, not a reason to manufacture a donation receipt. Never tell the player to die to create headroom. Ultimate Ironman Coffer support needs explicit validation and an alternative/account-scope decision before offering that mode variant; no universal all-account support is claimed.

A minimum-price donation-to-spin loop remains a balance issue, not detector evidence. No proposed conversion was approved by this research. Keep the one-spin Taint/Sacrifice costs and Pardon rules unchanged.

## Bounded release and remaining gates

Research decisions now give specific implementation paths. They cannot produce runtime test results or Jagex/RuneLite approval without implementation/review. The user still prohibits plugin coding, and no authenticated game surface was supplied. Remaining gates are therefore named executable checks, not another vague brainstorming pass:

- Verify every offered restricted task's positive/negative event traces; exclude unsupported templates before drawing cards.
- Exercise normal/alternate shop/GE/trade routes including repeat/modify, X, final confirmations, other menu plugins and pause/resume.
- Observe all five Grand Fate receipt sequences and Coffer before/after quote/credit in a controlled current client; do not claim old cache scripts settle current transmission behavior.
- Test identity initialization, crash journal replay, negative XP resets, account switch, cross-device conflict and paused/unobserved deltas.
- Submit exact restrictive implementation for Hub review after coding is authorized. Source precedent cannot bypass that review.

No new artificial Grand Fate level/playtime gate, automatic OSRS action, menu deletion, Assisted pause status, penalty, enhancement price escalation or Pardon limit is introduced. Significant gameplay decisions still separate from detector work: pre-existing Grand Fate credit; incompatible/full Coffer recovery/account scope; and any fallback if reviewers reject restrictive action cancellation. Each needs an explicit recorded resolution rather than being hidden in implementation.

## Primary source record

RuneLite inspected commit 42a6f17a6a2e8e478aa763890ecd0181a59dad38:
- [XP Tracker](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/xptracker/XpTrackerPlugin.java), blob 7a0a64c1646c54cb157d31b977ee52fa0e635171.
- [Woodcutting](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/woodcutting/WoodcuttingPlugin.java), [Mining](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/mining/MiningPlugin.java).
- [Chat Commands](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/chatcommands/ChatCommandsPlugin.java): game KC parsing is a source-specific lead, not historical receipt import.
- [Loot Tracker](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/loottracker/LootTrackerPlugin.java): Colosseum reward container and ServerNpcLoot.
- [InterfaceID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/InterfaceID.java), [VarPlayerID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/VarPlayerID.java), [VarbitID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java), [ItemID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/ItemID.java).
- [WorldPoint](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/coords/WorldPoint.java), blob 36c3e7ea98ebd87a5b90ef91717a24da51db90f8: instance mapping; [GE event](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/events/GrandExchangeOfferChanged.java), blob 8a7ff3ffc0f6727179db113ae581808f043a133c: initialization caveat.

Peer/catalog sources:
- [Another Bronzeman Mode](https://github.com/CodePanter/another-bronzeman-mode/blob/be73e5e4f1d07a00f551985475eeeef860f02633/src/main/java/codepanter/anotherbronzemanmode/AnotherBronzemanModePlugin.java), same revision pinned in Hub manifest: trade consumption and GE search filtering; do not copy grandfathered fresh Gson/file patterns.
- [Quest Helper SkillRequirement](https://github.com/Zoinkwiz/quest-helper/blob/a52646118f0e5ea63a6b3331cefa98087a7b4d6c/src/main/java/com/questhelper/requirements/player/SkillRequirement.java), current Hub-pinned revision: real/boosted distinction; authored requirements are still needed.
- [CA DataLoader](https://github.com/ehubbartt/combat-achievements-tracker/blob/4328d91bbc61e217e499be6ec5fd8fb394ebebc4/src/main/java/com/catracker/util/CombatAchievementsDataLoader.java): schema and flags; inspected metadata example, not claimed current Hub-pinned acceptance.
- Existing Wheelbound BossData/CombatAchievementCache/QuestPool at dc76720971c3e15c2e831a27b1505dd25054267f. Those establish existing implementation shape, not tests of future mode.

Game/policy sources: [Poll 84, AFK Timers and More, 2025-06-04](https://secure.runescape.com/m=news/poll-84-afk-timers--more?oldschool=1), [Death Changes, 2020-06-25](https://secure.runescape.com/m=news/death-changes?oldschool=1), [Yama contracts, 2025-05-22](https://secure.runescape.com/m=news/yama-the-calm-before-the-contract-storm?oldschool=1), [Jagex client guidelines](https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1), [RuneLite rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features). Check these policies again at submission. Prior actual Wheelbound Gson and KeyboardFocusManager blockers remain documented in POLICY_AND_FEASIBILITY.md.
