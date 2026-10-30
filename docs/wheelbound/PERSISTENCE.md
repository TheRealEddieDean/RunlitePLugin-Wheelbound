# Persistence
Per OSRS account, versioned run ID/schema; do not use settings profile alone as account identity. Save committed random results before animation. Journal transactions with IDs for assignment, completion, purchase, rejection, Defy, bounty claim and verified donation.

Persistent records: run mode/FP/spins/Grand Fate/Defy count; slices UUID/activity/weight/modifiers; Satchel; lifetime duplicate counts by activity; vendor status/counters; card unlocks; node unlocks; assignments and audit tolerance; bounty evidence/claims/daily seed; blessings; forced candidate snapshot; Grand Fate checklist.

Atomic mutation checks preconditions, records ledger and resulting state together. Startup reconcile pending receipt, never reroll or replay reward blindly. Retain last-good snapshot and corrupt original. Migration validates wheel five/three/24 invariant; do not destructively reset a run.

Pause stores obligation; resume takes new observed XP baselines and never imports pause progress. Logout/profile/account switch drains pending writes and rejects stale event callbacks. Account ID acquisition and atomic durability mechanics TECHNICAL_TBD.

## Corrected durable fields
Store full independently generated card offers (type, target, Standard reference, reward, seed/version), selected index, frozen boss eligibility pool and selected boss. Stage order Bossing: Standard target/offers committed -> card selected -> random boss committed -> active objective. Restore the correct stage on restart. Standard default is schema baseline; permanent pool unlocks and third-offer upgrade are separate. Pardon receipt records vendor Banned -> Locked and fixed-price debit; there is no cap counter or escalating-price counter. Preserve old Pardon count only as historical receipts, never as an eligibility restriction.

## Proposed durable envelope
Envelope fields: schemaVersion, runId, accountProfileKey, revision, lastTransactionId, catalogVersion, payloadChecksum, payload. Each receipt has operation ID, before/after revision, account token, random outcome when applicable, FP delta, spin delta and evidence keys. Checksums detect corruption, not player cheating.

Use RS-profile identity for account run data; user-facing preferences may remain settings-profile data. Unknown account/profile prevents new credit or mutations until identity resolves. RuneLite source currently maintains a separate RS profile store; synchronization conflict semantics still require validation.

For durable journal files, use Plugin.getPluginDirectory() and RuneLite Filepath APIs, not unrestricted java.io file paths. Write a temporary snapshot, flush through supported FileChannel, then move atomically where supported. Keep journal and last-good snapshot. API existence does not prove fsync/atomic moves across every platform; test actual crash boundaries before claiming exactly-once durability.

If configuration sync can reintroduce an older run revision, reconcile by retained receipts and run ID; do not choose greater FP or regenerate outcomes. Concurrent device mutation policy is TECHNICAL_TBD. No competitive integrity claim is added.

## Confirmed recovery and Taint accounting
WB-D057 recovery persists the pending Sacrifice ID and original evidence plus a separately validated post-trigger acquisition candidate; never overwrite the original snapshot. WB-D058 persists the Taint spin debit receipt with its mandatory Punishment ID; restart/replay must not spend a second spin. Atomic debit timing is a technical proposal, not a newly approved gameplay rule.

Sacrifice's confirmed one-spin debit is recorded separately from verified donation's spin award. Proposed sequence: commit special outcome, obligation and debit receipt; persist acquisition recovery if needed; freeze validated candidate; verify donation; settle the reward exactly once. Crash/replay cannot repeat either debit or reward. Voluntary sacrifice is an action rather than landing on a task; this approval does not add a voluntary-sacrifice spin charge.

## Detailed technical resolution checkpoint

[TECHNICAL_SPECIFICATIONS](TECHNICAL_SPECIFICATIONS.md) supplies current detector contracts, route coverage, initialization rules, source evidence and explicit live-validation gates (WB-D069–075). Its refined Coffer route uses visible initialized UI/server quote and balance as primary evidence; the old IF-variable mapping is optional pending validation. Method-specific templates require a supported catalog. Vendor inspection does not unlock transactions. New-account scope is confirmed by WB-D088; full Coffer recovery and exact restrictive policy acceptance remain in [OPEN_QUESTIONS](OPEN_QUESTIONS.md). No live test or production implementation is claimed.

## Current clarification2026-10-12
Persist known-empty Questing draw/retry provenance and eventual valid assignment without changing its owned slice or enabling general rerolls (WB-D111). WB-D120: persist empty retry as a zero-cost attempt; eventual valid Fate allocates one spin exactly once, including after restore/replay. WB-D112 uses automatic once-only active bounty payout, superseding manual claims/unclaimed UI; independently settled bounty FP survives failure/Reject. WB-D114 forbids payout while paused, preserving prior earned evidence and credited FP. Migrations from earlier qualified-unpaid records require explicit schema/evidence reconciliation and no double credit; exact migration is technical work, not retroactive paused earning. Existing pinned source/oracle receipts document the prior contract.

WB-D117 supersedes the Master Grand active-earned CA tally: persist the hilt5 item objective and once-only completion state without requiring underlying CA history. Preserve old saved evidence during reviewed schema migration; do not silently delete it or automatically mark completion from points. Exact item observation/version mapping remains technical work.

WB-D118: persist per-activity ever-acquired baseline and paid subsequent-purchase history independently of active/storage ownership. Starting granted slices establish baseline, not paid duplicates. Destroying all instances preserves history; return/replay does not increment. Migrations use retained original/receipt history and do not infer never-owned from an empty current slice list. Exact schema fields/migration are delegated technical work.

WB-D121: Questing type cannot be destroyed by direct edit, forced displacement, migration or destructive recovery. Store/return preserves UUID/modifiers/lifetime state. Do not infer mandatory active Questing; normal5/3 active validation applies. No extra storage/refund/reset.

WB-D122: save Bossing Challenge purchase separately from Skilling, item-drop objective kind, canonical item/source context (WB-D128 removes CA family), catalog/version, frozen reward and selected target. WB-D135/136 require frozen pool/context/quote versions, independent per-offer randomness and durable selected creature/item before reveal; Java schema remains future work; do not overwrite ordinary KC assignment semantics or use display-name matching to prove completion.

WB-D123: persist dedicated Challenge target catalog/version and selected boss/creature context; normal L/S/G pool remains separate. New creatures cannot leak into ordinary boss selection during migration/replay. Card-type purchase and target eligibility are separate fields/contracts; WB-D124 unlock structure retained; exact membership/tiers/prices remain pending.

WB-D124: persist Challenge starter bundle version/member grant receipts alongside card purchase and downstream target purchases. Include prerequisites and frozen quote. Preserve grant versus paid-target provenance; starter Challenge targets cannot be counted as purchased normal boss nodes or silently added to normal L/S/G pool. Migration/replay keeps complete bundled grant, not a partially unlocked card.

WB-D126: keep permanent Challenge card/bundle grants when eligible count falls below3; record source/version/target grouping with committed offer snapshots. No revocation/refund/new draw on restore. Reevaluate current eligibility only for new offerings; preserve prior failed/ambiguous evidence as required by existing recovery contract.

WB-D128: committed Challenge is an item hunt, never recomputed into another objective on resume. Persist source item/creature and quote/context with receipt; retries cannot redraw. Retain any earlier proposed CA payload during explicit reviewed schema recovery, without marking it complete or silently generating another target.

WB-D129: persist committed Challenge start/context, reviewed source/item objective and qualifying fresh receipt separately from possession history. Item ownership does not disable future hunts or instantly settle pending one. Once-only completion reward keys include assignment; permanent Bounties Shop keys remain independent and cannot reset on a repeated hunt. Preserve delayed/ambiguous evidence without fabricating acquisition or retrospective paused credit.

WB-D130/131: persist specific item ID/canonical equivalence/source and rate-context/catalog version with assignment, not an any-unique wildcard. Restore same exact hunt. Catalog rate updates cannot silently change target/reward or auto-complete at a kill threshold; explicit migration/recovery needed for invalidated content. No alternate-item acceptance or reward without fresh source evidence.

WB-D133/134: use reviewed personal receiving probability for one specific item per authored encounter/loot opportunity. Freeze source/mode/context IDs, assumptions, formula/source revision and exact probability or fully in-band range. Unknown allocation/context/range is unavailable for new offers. Do not assume group chance/team size. Preserve committed hunt/item/reward on later context failure; explain unavailable qualification and retain existing Pause/Reject. Only supported fresh active personal assigned-source receipts qualify; variants do not multiply target diversity. Concrete source formulas/live proof remain gated.

WB-D135–137: follow [Challenge assignment contract](CHALLENGE_ASSIGNMENT_CONTRACT.md). Freeze eligible canonical pool/context/reward schedule and offer randomness before display; card selection precedes durable uniform creature-then-item result and exact reward reveal. Variants/contexts are not extra lottery entries. Challenge uses fresh-drop completion, no KC quota or item consumption. Receipt/restart/odds cases are planned NOT_RUN; actual catalog/quotes/source adapters remain gated.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.

## Current Challenge rank control — WB-D148

[Rank controls](CHALLENGE_RANK_CONTROL_CONTRACT.md) separate owned/enabled/ready. First Lesser confirmation enables Lesser; later purchases preserve settings and start disabled unless explicitly opted in. Owned-rank settings may change only with no assignment/obligation/pending offers/unresolved selection. All off is allowed and suppresses only new Challenge offerings. Pending/paused/restored work stays immutable; no target toggles or reroll. This supersedes older selector-proposed and automatic-pool-entry wording for Challenge ranks only.

## Current ranked Challenge offering — WB-D149

[Rank offering contract](CHALLENGE_RANK_OFFER_CONTRACT.md) preserves two/three total slots and one Challenge family. Each Challenge offer uniformly commits a ready enabled rank before display; player selects the visible rank card, then uniform canonical creature/item within it. Require≥3 actually eligible canonical keys **within that rank**, supported context and reviewed v2 quote. Extra ranks divide the existing family share; no fourfold family boost, extra slots, rank picker or forced distinct rank offers. Numeric family weights remain BALANCE_TBD. Frozen offers/settings/pools do not refresh on restore. This supersedes older rank/floor/offer-integration proposals only; production is disabled.

## Current shared Challenge targets and purchase contract — WB-D150

[Rank purchases](CHALLENGE_RANK_PURCHASE_CONTRACT.md) grant one shared canonical Challenge target entitlement across reviewed rank contexts; no target repurchase per rank. The first paid Lesser entry includes one reviewed versioned bundle and explicit Lesser enable, atomically; later ranks grant ownership only unless separately opted in at a legal boundary. Target/access/rank ownership remain distinct. Historical v1 six starters/quotes are preserved and not a Lesser-ready bundle. V2 exact bundle/prices/quotes are unfinished; missing payload cannot be sold or labeled free. No normal roster change or blanket legacy four-rank grant.
# Implemented FP snapshot compatibility (2026-10-29)

Foundation RunCodec schema 4 adds `fatePoints`: explicit null means FP state is
unavailable; otherwise it contains `startingBalance` and ordered `entries` with
`kind` and positive `amount`. Decode validates every intermediate balance through
FatePointLedger, including discretionary affordability and exact arithmetic.
Schemas 1/2/3 remain readable with FP unavailable; re-encoding preserves that
absence and does not assign starting funds. Lifecycle and draw commands preserve
the ledger. This is a trusted snapshot representation only: typed debit receipts,
mixed-operation journal validation, durable debit/product mutation and migration
from legacy gameplay evidence remain unfinished. No paid gameplay is enabled.

### Schema-10 structural Grand evidence retention (2026-10-30)

The current foundation codec writes an explicit optional `grandJournal` full-payload
field. Schemas 1-9 restore absence, including schema-9 saves containing progress
references; references never synthesize source receipts. Present journals must resolve
exactly the aggregate progress and original seal at the current revision. Existing
lifecycle/debit/draw commands preserve payloads and existing-store writes reject journal
mutation until trusted evidence admission is implemented. This format does not provide
authentication, attempt settlement, completion awards or authorization to enable gameplay.

## Standalone current-attempt payload - 2026-10-30

GrandCurrentAttemptJson version 1 retains the full original activation receipt,
including its explicit command, selection, completed prefix and original seal.
Decoding requires independent history, current lifecycle and checklist, and exact
receipt equality with that history. Encoding also rejects stale lifecycle snapshots.
It never infers an attempt from the last history entry. Aggregate integration and
explicit legacy absence remain future work; RunCodec stays schema 11. This payload
does not establish trusted session admission, settlement or a durable activation.
Gson/JUnit restart tests are authored but remain unexecuted because the inherited
Gradle wrapper-cache lock is inaccessible.

## Local aggregate activation admission - 2026-10-30

GrandActivationAdmission is a read-only prerequisite to the future atomic command.
It requires explicit activation history and rejects request IDs used by the seal,
operation journal or Grand evidence references. It delegates exact replay and new
activation to GrandActivationProposal; replay preserves later pending obligations.
Missing legacy history is unavailable and cannot be synthesized by this boundary.
This API neither authenticates a session nor commits or publishes activation. The
eventual command must execute admission under its write lock and persist an operation
envelope with the entire resulting aggregate. Current generic store guards remain.

## Local schema-11 activation-history increment - 2026-10-30

The unwired foundation aggregate retains optional full original activation history.
Schemas 1-10 retain explicit absence; schema 11 writes null or versioned ordered
payloads. Exact seal/head, completed-prefix timing and operation/evidence command
collisions are checked structurally. Existing command copies retain history and ordinary
store writes cannot add, discard or replace it. This does not persist current attempt
state, infer activity from the last receipt, authenticate evidence, settle obligations,
activate gameplay or debit a Master spin. Atomic activation/current-attempt and trusted
acceptance remain unfinished. Dependency-free aggregate checks passed; Gson/JUnit/store
and Master Wheel checks were blocked before compilation by inherited wrapper-cache
lock access denial, so schema migration/restart tests remain unexecuted.

## Schema 12 explicit current Grand attempt - implementation checkpoint

RunCodec schema 12 writes `grandCurrentAttempt` as explicit null or the version-1
full original activation payload. Decode validates it against independently decoded
activation history, lifecycle and checklist. Schemas 1-11 restore absence, even with
populated history. Ordinary lifecycle/debit copies retain and reconcile explicit
activity to the new head; Master-style draws remain blocked by its obligation.
AtomicRunStore rejects adding, dropping or changing the original activation payload
on existing saves until an operation-specific activation/evidence command exists.
These guards run before pending snapshot creation. This is structural persistence,
not live activation or evidence admission; incomplete gameplay stays disabled.
Java 11 domain/aggregate fixtures pass. Gson/store/JUnit/migration and Master Wheel
regressions remain NOT_RUN: inherited Gradle wrapper-cache lock access was denied.

## Local structural activation journal validation - 2026-10-30

RunDocument accepts GRAND_ACTIVATION envelopes only when their complete original
payload equals the independently retained activation history entry. A retained
predecessor must be ACTIVE/NONE; ordinary chain and head checks still apply.
History activations after the first retained operation's before-revision require
matching typed envelopes. Older history prefixes remain supported. Arbitrary shared
command IDs are rejected; exact typed admission replay preserves current state and
never infers a current attempt from history. RunCodec schema 12 refuses documents
containing these envelopes before serialization, pending versioned payload support.
This is structural validation, not a durable activation command or session admission.
Existing store transition guards and disabled gameplay remain in force.

## Schema 13 Grand activation journal payload - 2026-10-30

RunCodec schema 13 retains each GRAND_ACTIVATION envelope's full version-1 original
activation payload. Decode checks the independent sealed Grand Fate, exact outer
command/revision/response fields, then existing aggregate history and operation-chain
invariants. Missing, extra, mismatched and pre-schema-13 activation envelopes fail
closed. Schemas 1-12 retain their prior layouts and explicit activity/history absence;
encoding a migrated history never synthesizes operation envelopes or current activity.
This supersedes the schema-12 encode refusal above. Existing store mutation guards
remain in place; atomic activation, session admission, evidence acceptance and live
client integration remain unfinished and disabled. No Master spin or FP charge added.
Focused Gson/JUnit/migration and Master Wheel checks remain NOT_RUN because the
inherited Gradle wrapper-cache lock was inaccessible before compilation.

## Local complete activation snapshot preparation - 2026-10-30

GrandActivationAdmission.prepare combines admission and the immutable domain proposal
into one complete RunDocument with a typed activation envelope, history and current
attempt. It preserves catalog, FP, inventory, seal, progress and full evidence payloads.
Exact replay returns the supplied document unchanged, even with explicit current-attempt
absence or a later paused head. This is preparation only: generic store writes still
reject activation mutations. The future atomic command must perform preparation under
its write lock, admit the session and publish success only after persistence. Java 11
executable fixtures pass; Gradle/JUnit and file durability remain unverified here.
