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

WB-D122: save Bossing Challenge purchase separately from Skilling, item-drop objective kind, canonical item/source context (WB-D128 removes CA family), catalog/version, frozen reward and selected target. Exact new pool/order schema pending; do not overwrite ordinary KC assignment semantics or use display-name matching to prove completion.

WB-D123: persist dedicated Challenge target catalog/version and selected boss/creature context; normal L/S/G pool remains separate. New creatures cannot leak into ordinary boss selection during migration/replay. Card-type purchase and target eligibility are separate fields/contracts; unlock mapping pending.

WB-D124: persist Challenge starter bundle version/member grant receipts alongside card purchase and downstream target purchases. Include prerequisites and frozen quote. Preserve grant versus paid-target provenance; starter Challenge targets cannot be counted as purchased normal boss nodes or silently added to normal L/S/G pool. Migration/replay keeps complete bundled grant, not a partially unlocked card.

WB-D126: keep permanent Challenge card/bundle grants when eligible count falls below3; record source/version/target grouping with committed offer snapshots. No revocation/refund/new draw on restore. Reevaluate current eligibility only for new offerings; preserve prior failed/ambiguous evidence as required by existing recovery contract.

WB-D128: committed Challenge is an item hunt, never recomputed into another objective on resume. Persist source item/creature and quote/context with receipt; retries cannot redraw. Retain any earlier proposed CA payload during explicit reviewed schema recovery, without marking it complete or silently generating another target.

WB-D129: persist committed Challenge start/context, reviewed source/item objective and qualifying fresh receipt separately from possession history. Item ownership does not disable future hunts or instantly settle pending one. Once-only completion reward keys include assignment; permanent Bounties Shop keys remain independent and cannot reset on a repeated hunt. Preserve delayed/ambiguous evidence without fabricating acquisition or retrospective paused credit.
