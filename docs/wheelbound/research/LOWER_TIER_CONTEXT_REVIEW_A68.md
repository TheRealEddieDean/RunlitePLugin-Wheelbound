# Lower-tier access and receipt review — A68

Seven source contexts reviewed against the committed A67 evidence at `173092c2c742c059481c8e69b5bb7e960f1fde39`; guarded ownership claim `1408fc07e595de3195ef86f6258a0dbbd1383448`. This is a bounded review packet, **not seven admitted hunts, seven eligible creatures, starter grants, or an FP schedule**. No design answer is inferred from the work.

The [machine packet](LOWER_TIER_CONTEXT_REVIEW_A68.json) preserves exact captured rows, full committed source context, reported NPC IDs, capture timestamps/hashes, access drafts, explicit holds, ten future receipt cases and conditional effort arithmetic. All runtime flags remain false; canonical item IDs and v2 FP stay null. No normal or boss-table memberships change.

## Findings and recommended next review

| Source and specific item | Captured tier rate | Access draft from captured evidence | Admission hold / next action |
| --- | --- | --- | --- |
| Killerwatt — Air diamond | Standard, 1/64 | Members, Slayer 37, Ernest the Chicken, Killerwatt plane portal | Best first Standard context to finish: identify dropped item and combat form; prove personal active source receipt and off-task route. Insulated boots mitigate damage rather than a proven hard attack gate; cannon placement is prohibited in the capture. |
| Molanisk — Earth emerald | Standard, 1/64 | Members, Slayer 39, Death to the Dorgeshuun, South Dungeon, Slayer bell | Verify wall-to-combat identity and captured NPC ID 1; review light-source/access route and permitted bell acquisition. Do not assume an unusual reported ID is a valid adapter mapping. |
| Infernal Mage — Fire ruby | Standard, displayed 1/64 | Members, Slayer 45, accessible Slayer Tower route | **Page-level rate-estimate hold:** introduction says rates are estimated unless cited; this item row has no citation annotation. Scalar parser exactness is not exact source probability proof. Retain display, obtain cited rate or reviewed supported bounds before effort/FP admission. |
| Sea Snake Hatchling — Water sapphire | Standard, 1/64 | Members, actual Royal Trouble dungeon-access stage | Slayer 40 is a task requirement, **not a kill requirement**. Exact quest stage and source/item identity remain to review. |
| Sea Snake Young — Water sapphire | Standard, 1/64 | Members, actual Royal Trouble dungeon-access stage | Same access distinction. Shared water sapphire does not authorize cross-credit or counting both forms toward the three-creature floor; author canonical grouping explicitly. |
| Jelly — Mithril boots, Wilderness context | Lesser, 2/64 = 1/32 | Members, Slayer 52, Wilderness Slayer Cave, active matching task | **Held task-dependent** under WB-D140. Rate comes from the Wilderness table; regular dungeon 1/128 is Greater. No cheaper-rank shortcut by merging the sections or crediting normal kills. |
| Mogre — Mudskipper hat, shore context | Lesser, 5/128 | Members, Slayer 32, Mudskipper Point, fishing explosives interaction | Shore miniquest requirement is not conclusively resolved by captured introduction's sea-encounter exemption. Review exact shore stage and explosive replenishment permissions; boat/cannon sea encounters are separate. |

These are source-review judgments within WB-D140/141/144/145, not new gameplay decisions. Recommendations about route/gear are distinguished from hard access predicates. Source introductions do not prove every off-task route or every underlying restriction. Future adapters must observe real access; a purchased hunt unlock never grants account, region, shop or supply permissions.

## Why the easier-tier display is not a ready pool

Jelly's attractive Lesser rate is tied to an unresolved finite-task context. Mogre's shore source needs a precise access/consumable route. The four gem names span five source pages, but sea-snake canonical grouping is unfinished and Infernal Mage's page-level uncertainty survives the row parser. Therefore this packet makes **no eligible-creature readiness claim for either rank**. A per-rank minimum or enabled-rank selector is still a proposed integration detail, not silently adopted here.

NPC IDs are preserved source leads, not verified current-client composition identity. None of the seven captured rows provides canonical dropped-item IDs. An empty list is missing information, not permission to match item display names. Names, inventory ownership, trade, bank movement, another source's same item and ambiguous party receipts cannot settle the assignment.

Use the existing fresh personal assigned-source contract: active earning time, local recipient, exact item/context/source, assignment/run/account key and receipt deduplication. Keep the item; settle once; independent permanent item bounty remains separate. The ten packet cases also cover paused evidence, restart/duplicates, sea-snake cross-form, shore/sea Mogre, normal/Wilderness Jelly, task exhaustion and Molanisk wall/combat transformation. These are **planned review requirements, not executed gameplay tests**. Existing Pause/Reject/no-reroll applies; no retirement, automatic task reacquisition, access waiver or reroll adopted.

## Conditional effort sensitivity, without invented FP

These figures assume independent qualifying opportunities with the displayed probability. They are arithmetic, **not verified personal per-kill rates, measured throughput, guaranteed caps or proposed completion quotas**. Page-level estimates, roll opportunity definition and personal allocation still require review.

| Displayed probability | Mean opportunities | Median | 90% receive by | 95% receive by |
| --- | ---: | ---: | ---: | ---: |
| 1/64 | 64 | 45 | 147 | 191 |
| 1/32 | 32 | 22 | 73 | 95 |
| 5/128 | 25.6 | 18 | 58 | 76 |

Mean is `1/p`; percentile is the smallest integer n with `1-(1-p)^n` reaching the stated probability. Validation uses exact rational powers to check each threshold and its preceding integer. These counts cannot be converted to hours/FP without reviewed personal opportunity semantics and source-specific throughput. No tier multiplier or reuse of historical v1 quotes occurs.

## Evidence and verification boundaries

A68 uses the committed source-preserving index; its SHA256 and original archive SHA256 are pinned in the packet. Existing network captures date to 2026-10-14 and remain distinguishable from fixtures. This session does not claim a fresh Wiki fetch, rerun the A67 full-cache/SQLite audit, or reopen the uploaded archive. Full-cache checks recorded by A67 remain historical verified evidence, not newly executed checks.

Run `python tools/wiki_drops/validate_lower_tier_review.py` to verify all seven contexts/rows/metadata against the pinned index, exact tier classification, rate/percentile arithmetic, hold coverage, disabled status and authority manifest hashes. Run the existing collector/tier tests separately. Unlock topology, enabled-rank/offer integration, exact canonical grouping, item IDs, personal traces and v2 FP remain unfinished.

Next bounded packet: finish Killerwatt and Molanisk exact item/source IDs and access/receipt source evidence, then quote effort only after personal probability/throughput review. Obtain the original archive only if additional full-page facts are necessary; do not restart the completed full-monster inventory audit. Rank topology remains the next single material user question.
