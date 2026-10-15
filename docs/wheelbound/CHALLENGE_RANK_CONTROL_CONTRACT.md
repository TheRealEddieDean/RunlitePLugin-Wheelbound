# Challenge rank controls and durable settings

Authority: WB-D147 confirmed topology; WB-D148 delegated integration2026-10-15. Documentation only; no runtime implementation.

## Player behavior

The Fate Tree owns rank purchases. A separate Challenge ranks panel shows four ranks with **Owned**, **Enabled for future offers**, and **Currently ready** as distinct states. A higher purchase retains lower ownership and settings. Lesser is explicitly enabled by the first purchase confirmation; later ranks start disabled unless the player explicitly opts in. A disabled rank can still satisfy the next rank's permanent ownership prerequisite.

All owned ranks may be disabled. This suppresses Challenge offerings while normal Bossing cards keep their existing behavior. An inaccessible/underfloor rank remains owned and retains its enable setting; readiness is recalculated only for new eligible pools. No refund, forced Pause, ordinary boss unlock or free target is implied.

## Edit boundary

| Current state | View controls | Commit changes |
| --- | --- | --- |
| No assignment, obligation, offer set or unresolved selection | Yes | Yes, for owned ranks only |
| Pending card offers, even before a card is selected | Yes | No |
| Selected/active Fate or mandatory special obligation | Yes | No |
| Paused or logged out with persisted work | Yes when UI is available | No |
| Unknown account/profile or unresolved save state | Explain unavailable | No |

Controls do not discard offers or create a new seed. Existing completion or legal Reject follows its own rules before editing becomes available. This contract does not settle A56 no-fault retirement or committed-but-unselected reconciliation.

## Persisted intent, not adapter implementation

Record owned rank IDs, enabled owned subset, settings revision and operation ID with the account/run. Purchase records contain the quoted opt-in action so replay cannot accidentally enable a newly bought rank. A settings mutation requires the current revision and editable boundary; commit old/new enabled set together. Offer snapshot retains settings revision and enabled ranks alongside catalog/access/quote versions and existing per-offer RNG commitments. These are conceptual requirements; Java schema/storage proof is future work.

The display does not offer creature/item toggles. Enabling Lesser and Standard allows the authored random mechanism only within those ranks; an already committed hunt is immutable. Unsupported source/rate/receipt/reward evidence remains a readiness hold.

## Planned acceptance

A third-party/bought/banked item cannot settle a hunt; owned items remain assignable. Settings edits do not touch receipt journals, FP or spin reservations. Repeated clicks/restart restore the same setting or committed offer. Unowned rank enable attempt fails. A purchase replay uses its saved opt-in intent. These are planned NOT_RUN client cases; the A73 synthetic model will cover only logical subset/boundary invariants.
