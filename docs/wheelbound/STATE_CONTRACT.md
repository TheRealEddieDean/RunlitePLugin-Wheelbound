# Proposed state and transaction contract

Status DELEGATED implementation-neutral architecture, 2026-10-09. This is a specification, not production implementation. Follow the existing RuneLite supported serialization/file APIs; do not instantiate a private Gson or global focus manager.

## Aggregate
RunEnvelope: schemaVersion (positive integer), revision (monotonic integer), runId (UUID), accountKey (resolved RuneLite account/profile identity), catalogVersion, runState, snapshot, journalHead, checksum. Do not store credentials, login tokens or remote account telemetry.

RunState: NOT_STARTED, ACTIVE, PAUSED, ABANDONED, COMPLETED. Snapshot contains integer FP (can be negative), integer spins (nonnegative), permanent Grand Fate outcome, checklist, ordinary/special slices, Satchel, lifetime activity purchase counters, card unlocks, progression nodes, vendors, blessings, bounty receipts and current obligation.

ObligationStage: NONE, OFFERS_COMMITTED, CARD_SELECTED, BOSS_COMMITTED, ACTIVE_OBJECTIVE, PUNISHMENT_PENDING, SACRIFICE_ACQUISITION, SACRIFICE_CANDIDATES_COMMITTED, SACRIFICE_DEMAND_COMMITTED, SACRIFICE_RECEIPT_PENDING, SETTLEMENT_PENDING. Store stage-specific fields, not contradictory booleans such as both complete and awaiting boss. Stage transitions do not change the outer paused status; pause retains every obligation.

Slice: unique id, kind, activity, enhancementTier, weight, createdTransactionId. Ordinary weights derive from the confirmed tier ladder. Specials follow Defy history; do not reuse ordinary enhancement pricing. Satchel accepts ordinary slices only. Validate five ordinary/three activities and total 24 after every mutation, including migrations.

Assignment: id, creationRevision, activity, committed random seed/outcome, pool/catalog version, Standard target, card offers, selected card, selected boss/quest, allowed evidence context, XP baselines, permitted by-products, reward quote and debit receipt id. A result selected before animation remains selected after a crash.

## Transaction boundary
Every command carries commandId, expectedRevision, runId, accountKey and operation payload. Validate identity, lifecycle, affordability and invariants before calculating the proposed state. Discretionary costs cannot create debt; penalties may. Persist receipt and resulting state together through the supported durable journal strategy. Publish success only after persistence acknowledgement.

Receipt: transactionId, commandId, beforeRevision, afterRevision, operation, integer fpDelta, spinDelta, outcome payload, evidenceKeys, sourceSessionId, previousReceiptHash. Hashes detect accidental corruption, not cheating. Store the frozen quote/config version; changing prices cannot alter a pending operation.

Duplicate commandId returns its existing result. Duplicate evidence key cannot pay twice. Stale revision prompts refresh; it does not reroll. Stale account/session observations are discarded. Failed persistence keeps the command pending and disables additional writes until reconciliation. Never debit in a UI callback and reward independently without an associated durable receipt.

## Crash fixtures
| Crash boundary | Required restore |
| --- | --- |
| Before durable draw | No committed draw; new command permissible |
| After durable draw, before animation | Same outcome and assignment, no second debit |
| After Reject commit, before Punishment display | Rejected reward forfeited; same mandatory Punishment; no extra spin |
| After Taint debit, before Punishment completion | One debit, pending Punishment retained |
| After donation attempt, before corroborating evidence | Pending receipt, no fabricated award or instruction to donate again |
| After donation settlement, before UI update | One award reconstructed from receipt |
| After Pardon commit | Vendor Locked; fixed price paid once |
| During migration | Original payload retained; no partial transformed state accepted |

## Migration and recovery
Version-specific migration is a pure transformation on a copy; validate schema, IDs, ledger sum and gameplay invariants before replacing a snapshot. Retain the original on any failure. Unknown future schemas are read-only. Never delete a save to resolve an error, import paused progress or merge accounts by display name. Single writable session is the delegated policy; synchronization support is not promised.

Manual recovery/export may include local game/run state, never credentials. A self-attested action, if later approved, must carry a distinct evidence classification and cannot be presented as automatically verified.

## Recovered draw and audit constraints
Forced Sacrifice freezes candidate identities/quantities and then durably commits a random Death’s Wheel outcome before animation (WB-D033). The player cannot select the forced possession. Persist candidates, outcome and donation receipt separately; restart never draws again. Voluntary item/quantity selection remains explicit. Grand activation requires ObligationStage NONE, with no pending mandatory settlement (WB-D089). Card unlocks are keyed by their separate Skilling/Bossing purchase pool, not a global shared name (WB-D090).

Audit evidence accumulates during active observation and is judged at Fate completion (WB-D087). Persist the consecutive violation streak and any resulting Penance obligation atomically; a clean completion resets it. Exact reward/FP ordering is not yet recovered, so no adapter may invent completion forfeiture or tick-by-tick charges.

Boss unlocks permanently join the separate Bossing pool; storing/destroying an ordinary Bossing Master slice does not erase those unlocks or pool membership (WB-D100). WB-D117 makes obtaining hilt5 the Master Grand objective; do not maintain a separate active-earned CA tally as its win gate. Other Grand components require their active attempt context (WB-D104). Bounty receipts retain acquisition/assignment provenance; WB-D112 resolves Q10: automatic independently qualified bounty settlement survives failure/Reject; no receipt deletion, escrow or clawback.

## Acceptance boundary and dependency contract

The I01 obligation domain must expose a single Grand-activation predicate covering pending mandatory settlement; I08 need not depend on a functioning live Coffer adapter to test this predicate. The client adapter can report unavailable/ambiguous evidence without mutating the committed outcome or discarding ownership snapshots. Freeze catalog and quote versions with every assignment; additional unsupported card/templates stay outside eligible runtime pools. Independent bounty settlement follows WB-D112; any separately scoped historical CA aggregate qualification remains gated; not the item-shop or hilt5 Grand, with receipts preserved. Detailed future failure fixtures are in [DEVELOPMENT_PIPELINE](DEVELOPMENT_PIPELINE.md).

## Offline acceptance supplement and unresolved boundary details

[Receipt safety supplement](../../automation/receipt_safety/README.md) executes44 expected traces and32 operation/crash combinations against an in-memory reference oracle, with10 detected defective variants and6 rejected malformed fixture packets. It does not exercise Java, real file durability or live receipts. Mock sealed records are a test premise, never evidence that RuneLite persistence is atomic. All numeric quotes are placeholders.

Keep command identity/payload-collision handling, torn-tail classification/checkpoint rollover, exact replay response shape and source evidence namespaces explicit in the eventual versioned implementation contract. Preserve failed originals and committed outcomes; do not silently truncate or regenerate. WB-D114 forbids reward processing while paused; active-earned evidence is retained, and WB-D112 removes normal manual claiming. A separately retained CA-aggregate reward may still need qualification reconciliation; this is not a hilt5 Grand gate. The earlier offline oracle is historical and does not implement these new clarifications.

## Latest confirmed transaction boundaries
WB-D111 requires persisted empty-Questing retry provenance without modifying wheel ownership/weights or allowing arbitrary valid-assignment rerolls. WB-D120: empty retries add no debit/reward/Punishment; eventual valid Fate allocates one spin exactly once. WB-D112 automatically settles independently qualified active bounties once, separately from Fate completion/failure/Reject. WB-D114 blocks new FP reward credit while paused and preserves prior evidence/balances. Current-source item qualification, any separate historical CA reward and delayed-event timing still require their own contract; no live proof is claimed.

WB-D118 duplicate transaction: classify from lifetime activity baseline/history, freeze quote, create new instance and increment only the applicable successful paid duplicate count in the same durable mutation. Starting grants establish baseline; first-ever non-granted activity acquisition uses first-instance price. Cancellation/failure/replay and same-instance Satchel return produce no extra increment. Destroying all slices never resets price history.

WB-D121: validate Questing destruction prohibition before all direct/compound ordinary edits. Questing removal means normal paid storage, with room and valid final5/3/24. Preserve the same instance. A failing destruction plan creates no debit, deleted instance or history reset; forced special insertion cannot bypass the rule.

WB-D122 Challenge assignments need typed unique-item-source objectives (WB-D128; CA family removed) and frozen catalog/context/receipt identity, not just KC. Restore the same committed target after restart; no valid target reroll or assumption a possession snapshot proves assigned acquisition. Dedicated item-hunt Challenge pool is confirmed; exact generator/reveal timing still needs specification; normal L/S/G sequence unchanged.

WB-D123: record targetPoolKind/version/eligible snapshot with a Challenge commitment, distinct from the Bossing card unlock pool and ordinary L/S/G target pool (field names delegated). Restore selected target, not current recalculated pool. No target choice/reroll. WB-D124 confirms AND-gated card purchase with bundled starters and individual expansion; exact member/price details pending; WB-D135/136 fix assignment draw/reveal order; I01 does not invent them.

WB-D124: gated Challenge purchase validates Bossing Lesser AND Greater ownership and quoted affordability, then commits card and bundled target unlocks together. Replay/lost acknowledgement cannot omit/duplicate a starter grant or charge twice. Skilling counterpart keys do not satisfy gate. Record bundle catalog/version and grant provenance; no normal boss pool change. WB-D126 confirms≥3 distinct actually eligible targets before generating new Challenge offerings; card/bundle purchase remains owned below the floor.

WB-D126: validate new-offer Challenge type eligibility against a frozen reviewed target snapshot containing≥3 distinct valid bosses/creatures. Unknown/inaccessible/unsupported targets do not count; multiple objectives/forms for one target do not inflate diversity. Count changes affect future offering eligibility, not already committed outcomes or ownership. Family-choice/generation policy and postcommit access-change recovery still need specification.

WB-D128: persist item-drop-only Challenge target, canonical item/source, eligibility/catalog/quote and full committed receipt. No CA family selector/draw or target override. Restore same item hunt; legacy proposed CA payload retained for reviewed recovery, not silently rerolled into an item.

WB-D129: Challenge objective evidence must qualify after its committed start and during active earning, with assigned source/context and once-only event identity. Prior item ownership is not a completion receipt or target exclusion. Keep original ownership observations separate from new acquisition. Duplicate completion/restore cannot credit twice; old bank/buy/trade/re-pick event cannot satisfy. Exact delayed receipt boundaries remain validated technical work.

WB-D130/131: freeze one specific logical item/source, reviewed probability/context/version and reward quote with the Challenge assignment. Other unique types cannot settle. Only supported in-band specific hunts contribute eligibility; ownership still irrelevant to fresh-drop qualification. Probability gate is catalog/assignment context, not a1500-kill completion counter or reroll/pity reward. WB-D133/134 require personal receiving probability and frozen supported context versions; unknown or straddling probability ranges cannot enter new pools.

WB-D133/134: use reviewed personal receiving probability for one specific item per authored encounter/loot opportunity. Freeze source/mode/context IDs, assumptions, formula/source revision and exact probability or fully in-band range. Unknown allocation/context/range is unavailable for new offers. Do not assume group chance/team size. Preserve committed hunt/item/reward on later context failure; explain unavailable qualification and retain existing Pause/Reject. Only supported fresh active personal assigned-source receipts qualify; variants do not multiply target diversity. Concrete source formulas/live proof remain gated.

WB-D135–137: follow [Challenge assignment contract](CHALLENGE_ASSIGNMENT_CONTRACT.md). Freeze eligible canonical pool/context/reward schedule and offer randomness before display; card selection precedes durable uniform creature-then-item result and exact reward reveal. Variants/contexts are not extra lottery entries. Challenge uses fresh-drop completion, no KC quota or item consumption. Receipt/restart/odds cases are planned NOT_RUN; actual catalog/quotes/source adapters remain gated.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.
