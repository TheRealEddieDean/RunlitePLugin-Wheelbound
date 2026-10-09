# Proposed state and transaction contract

Status DELEGATED implementation-neutral architecture, 2026-10-09. This is a specification, not production implementation. Follow the existing RuneLite supported serialization/file APIs; do not instantiate a private Gson or global focus manager.

## Aggregate
RunEnvelope: schemaVersion (positive integer), revision (monotonic integer), runId (UUID), accountKey (resolved RuneLite account/profile identity), catalogVersion, runState, snapshot, journalHead, checksum. Do not store credentials, login tokens or remote account telemetry.

RunState: NOT_STARTED, ACTIVE, PAUSED, ABANDONED, COMPLETED. Snapshot contains integer FP (can be negative), integer spins (nonnegative), permanent Grand Fate outcome, checklist, ordinary/special slices, Satchel, lifetime activity purchase counters, card unlocks, progression nodes, vendors, blessings, bounty receipts and current obligation.

ObligationStage: NONE, OFFERS_COMMITTED, CARD_SELECTED, BOSS_COMMITTED, ACTIVE_OBJECTIVE, PUNISHMENT_PENDING, SACRIFICE_ACQUISITION, SACRIFICE_SELECTION, SACRIFICE_RECEIPT_PENDING, SETTLEMENT_PENDING. Store stage-specific fields, not contradictory booleans such as both complete and awaiting boss. Stage transitions do not change the outer paused status; pause retains every obligation.

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
