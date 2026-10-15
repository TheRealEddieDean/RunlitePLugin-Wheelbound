# Personal source/receipt acceptance packet — A82

This is a concrete future trace/test plan, **not executed client tests**. [Machine cases](RECEIPT_TRACE_PLAN_A82.json) bind to the exact partial v2 catalog SHA256; all observed_trace fields are null and client_status is NOT_RUN. No gameplay implementation, dependency activation or collection of a user's live client data occurred.

## What a future adapter must establish

Account/run/assignment, exact canonical source and accepted composition/context/region/method/world, logical item/quantity and local recipient, earning-time active status separately from delivery/processing active status, source occurrence identity, frozen source/catalog/quote and persistent dedup/settlement operation. These are required *derived receipt* fields, not a claim that one RuneLite API event exposes all of them. Existing ServerNpcLoot/NpcLootReceived are candidate inputs only; validate actual payload/ordering/ownership in separately authorized engineering.

Passive NPC interaction, inventory delta, kill count, source constant, chat text and item ownership are insufficient independently. Correlate supported source receipt; unknown evidence waits without fabricated payout or verified-empty status. No logging/replay parser may use display names as canonical identity. Avoid resetting a committed quote/pool when live catalog changes.

## Trace session packet

For each case record client build, pinned adapter/source version, initial run/assignment/catalog/settings/quote, relevant context/composition observations, event order, earning-versus-delivery active timeline, exact evidence journal and mutation/restore result. Include matched positive/negative controls. Run only in an independently authorized development/test session; this document does not authorize production behavior changes. Existing ordinary normal wheels remain outside the authored adapter.

Positive cases cover each of four Stronghold components. Negative/ambiguous cases cover duplicates/reloads, old/traded/banked/combined items, paused earning versus paused processing, wrong account/run/assignment/NPC/location/world, teammate/unknown recipient, missing adapter/quote, save failure, catalog/settings mutations, Killerwatt orb versus combat, Molanisk bell, sea-snake mother, disposal and independent permanent bounty. Expected outcomes derive from current locked rules rather than new gameplay exceptions.

Paused delivery with supported prior active earning preserves evidence but performs no paused payout; it is distinct from prohibited paused-earning backfill. A legitimate settled item may later be disposed without clawback. Failure or Reject does not revoke independent bounty FP. Actual crash IO/atomicity and earning-time proof remain unvalidated; API declarations do not demonstrate them.

## Entry to engineering remains gated

No ready Standard pool, full v2 four-rank roster, reviewed rank/product prices, complete client trace suite or source migration is supplied here. A56 no-fault retirement/committed-unselected reconciliation stays open; task, Coffer and policy boundaries remain independent. Receipt test planning fills the handoff, not those decisions.
