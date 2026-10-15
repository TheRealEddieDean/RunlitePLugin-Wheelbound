# Frozen Challenge source/version handoff — A94

Planning supplement for future I01/I02 and Challenge engineering. Existing WB-D129/133–137/144–150 control; this introduces **no new recovery permission**, current-client detector or production schema. It closes a handoff gap: the finite core, rank contract, source fragments and actual assignment snapshot have different responsibilities and cannot be interchanged at restore time.

## Exact input packet

[Machine handoff](FROZEN_SOURCE_HANDOFF_A94.json) pins the current rank rules, four-rank compiled core and its three content fragments, plus assignment/selection/control/offer/purchase/state contracts. It links the A93 future trace plan. The old v1 roster and its numeric quotes stay historical; there is no blanket migration granting all four ranks or converting its six starter targets into the new Lesser bundle.

| Identity/version | Owns | Does not establish |
| --- | --- | --- |
| Run schema + account/run/revision | Save shape and mutation identity | Source availability or gameplay migration approval |
| Rank rules and enabled-settings revision | Progressive ownership, opt-in, legal edit boundary, rank floor/family contract | Actual content access or a replacement for the offered rank |
| Core/catalog hash + source fragment/context versions | Authored hunt identity, source/rate and reference quote inputs | Actual client form/recipient/timing support or current account readiness |
| Frozen eligible per-rank canonical pool + ordered sampling representation | Exact universe and probabilities used by the committed offering | A fresh live-catalog query at restore |
| Offer-set ID + per-offer random commitment/state | Complete two/three-slot set, visible committed rank and independent future draw | Permission to reseed, add a slot or promise rank diversity |
| Selected offer/assignment + specific hunt/context/item IDs | One selected creature/item and supported frozen source context | Old possession, another unique or a changed source completing the hunt |
| Frozen quote version + integer FP + allocation receipt | Exact reviewed completion payment and linked ordinary allocation | Actual-duration repricing or blanket spin refund |
| Derived evidence/occurrence journal + settlement operation | Proven earning/recipient/context and once-only effect | A stable event ID exposed by an API, a passed client trace or durable storage proof |

The compiler's top-level version string alone is insufficient: a retained version must resolve to its original bytes and source identities. At minimum retain the original supported snapshot or an immutable local versioned payload with verified hash and a validated reference. A hash without recoverable data cannot reconstruct an old pool. Do not require an online catalog server, transmit account state or assume a current HTML page proves a past receipt.

## Restore and update order

1. Resolve account/run/schema and verify the existing durable command/evidence journal. Unknown future schema, checksum error, unresolved identity or persistence failure stays read-only/pending with original bytes retained. No automatic save deletion or account-name merge.
2. Determine the actual stage from stored state. A precommit unresolved selection, displayed committed offers, selected result, active receipt, pending settlement and settled receipt need different treatment. Pause is an outer lifecycle state, not a reason to reset that stage.
3. Load the original committed catalog/context/settings/pool/randomness/quote/allocation references. Restore their stored ordered representation before any draw/reveal. Do not rebuild from today's rows, row order, duplicate aliases or current enabled ranks. A presentation label can resolve separately without becoming the authoritative identity.
4. Review any current evidence that affects whether a new offer is eligible or a frozen context can still produce supported completion evidence. Unknown stays unknown. New readiness uses actual owned/enabled/supported/access predicates and the per-rank three-key floor; a failed rank alone does not make ordinary Bossing empty.
5. Apply only already-approved stage-specific behavior below. Record an unresolved reconciliation need with original state preserved where policy/evidence is open. Do not implement A56 retirement, compensate spins, replace a displayed card or admit an alternate context by treating this handoff as approval.

## Update impact matrix

| Change or event | New selection before valid commitment | Already committed offers, not selected | Selected/active assignment or supported pending receipt |
| --- | --- | --- | --- |
| New source/item added | Consider only reviewed, owned, actually supported contexts; recompute canonical pools | Preserve original offer snapshot/randomness; no pool expansion | Preserve selected source/item/quote; new item is not a substitute |
| Current catalog row removed or parser fails | Unknown/unsupported, not verified game impossibility | Preserve; explicit changed-snapshot reconciliation remains open | Preserve; source absence is not permanent invalidation; existing Pause/Reject |
| Source personal-rate/formula/context changes | Version and review before eligibility/quote use; exact tier classifier | Preserve original facts and flag affected compatibility; no redraw | No silent new rate/context or quote; supported matching frozen context/evidence requires review |
| Rarity boundary/rank rule changes | Explicit prospective migration only; apply current reviewed rule | No automatic visible-rank replacement | Existing selected rank/source/item/quote remains recorded |
| Live reward/throughput/product price changes | Only reviewed versioned future quote/product; no invented zero price | Preserve offered pool/reward schedule and purchase quotes | Preserve selected FP; lucky/dry duration never reprices |
| Rank disabled/settings edit requested | Only legal with no assignment/obligation/offers/unresolved selection | Reject edit; retain existing settings revision and offers | Reject edit; retain stored work even when paused |
| Current eligible count falls below three | Rank unavailable for new offers; ownership retained | Do not reroll/remove a card; specific reconciliation remains open | Floor change alone never replaces the selected hunt |
| Source form transformed or name changed | Require supported canonical/context mapping; no name matching | Preserve canonical set and sampling commitments | Match reviewed frozen source/earning context, not a current display name or unproved alias |
| Same payload callback or crash replay | Restore same unresolved selection/command | Same offers/rank/seeds, no extra allocation | Same journal/settlement once; no second FP or spin effect |
| Genuine external permanent impossibility asserted | Separate reviewed readiness evidence | Unselected reconciliation still open | A56 retirement remains proposed; no refund/retire operation inferred |
| Item/bounty already owned or paid | Ownership does not exclude a fresh-drop hunt | Preserve assignment odds | Fresh qualifying drop can settle Challenge; permanent bounty receipt stays separate |
| Supported active earning delivered while paused | No paused earning/reward | No paused reward processing | Preserve proved active evidence; no paused payout or paused-earning backfill |

Changes that only rename a human label can still matter to presentation, but no label is used as a primary key. Preserve the original committed data and expose current labels only as display metadata. Byte equality, semantic equality and sampling-order equality are separate checks; never use a JSON reserialization to justify a new random commitment.

## Migration review boundaries

| Existing payload | Required future disposition |
| --- | --- |
| Current ranked snapshot with all references present | Validate then restore exactly; no new grant, draw or payment |
| Historical monolithic v1 ownership/assignment | Retain payload/history and require explicit reviewed version-specific migration. Do not guess new rank ownership, source bundle, target, FP or debit compensation |
| Proposed/obsolete CA Challenge payload | Retain for reviewed recovery; do not convert into a random unique-item hunt |
| Missing old catalog/pool/context/quote bytes | Preserve original; report exact missing reference and hold affected mutation. Current catalog is not a reconstruction of the original |
| New save schema not understood | Read-only, original preserved; no reset or downgrade write |
| Saved supported receipt with earning-time/context ambiguity | Preserve journal pending evidence; no assumed payout, permanent failure or backfill |

I01/I02 may implement only after explicit authorization and their own schema/durability review. Acceptance must include crash points between intent, outcome, debit/reservation, journal, settlement and UI acknowledgment. A genuine source retirement/offer-reconciliation decision may later add operations, but must version its own evidence, stage limits and receipt accounting rather than reusing a generic “refresh” button.

## Completion and validation scope

This handoff turns existing rules into a concrete field/responsibility and compatibility review packet. It does not settle open postcommit policy. The machine packet records every current hunt's exact item/rank/source/quote identity and14 canonical context/form sets for the future restore review; these are source references, not a user's saved run or sample proven client receipt. All future runtime/client/migration tests remain NOT_RUN.

Validate pinned inputs and coverage with `python tools/wiki_drops/check_frozen_source_handoff.py`. No runtime design oracle, Java mutation or live client is introduced by this planning-only checker. This later supplement does not change the gameplay rules/rates/quotes in PDF version10/source135eed73, and is not claimed to be included in that publication.
