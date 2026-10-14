# Challenge Fate cards — four rarity tiers

Current rule authority: WB-D144 (explicit user2026-10-14), WB-D145 (delegated boundary convention). This supersedes the old single1/400–1/1500 Challenge rarity filter and encounter-aggregated bracket classification. The four final ranges below supersede the preliminary1/60/max1000 wording in the same user message. These are item-drop Challenge cards, separate from ordinary Bossing Lesser/Standard/Greater KC cards.

## Adopted brackets

For a probability p, use the reciprocal denominator D=1/p. This handles fractions such as499/250000 without rounding. Each shared endpoint belongs to the easier tier so no item appears in two tiers for the same context.

| Challenge card | Reciprocal denominator D | Examples at boundaries | Intended rarity band |
| --- | --- | --- | --- |
| Lesser Challenge |25 ≤ D ≤50 |1/25,1/33,1/50 | More common unique drops |
| Standard Challenge |50 < D ≤125 |1/60,1/75,1/100,1/125 | Moderate rarity |
| Greater Challenge |125 < D ≤750 |1/128,1/300,1/512,1/750 | Rare hunts |
| Superior Challenge |750 < D ≤5000 |1/800,1/1000,1/1500,1/5000 | Very rare hunts |

Below1/25 rarity (e.g.1/20) and beyond1/5000 rarity (e.g.1/6000) are outside these brackets. Exact endpoint convention is a new delegated clarification, not an invented user quotation. A later explicit preference can move shared endpoints with a versioned change.

## Roll-count rule — explicit user change

| Captured raw expression | Rate used for tier | Tier |
| --- | --- | --- |
|2 ×1/75 |1/75 | Standard |
|1/75 |1/75 | Standard |
|3 ×1/1026 |1/1026 | Superior |
|2 ×1/1024 |1/1024 | Superior |
|499/250000 | Exact reciprocal D=250000/499≈501.002 | Greater |
|1/50 |1/50 | Lesser |
|1/125 |1/125 | Standard |
|1/750 |1/750 | Greater |

Ignore the roll-count multiplier **for bracket placement**. Keep the original source notation and number of rolls. Do not multiply, sum, or use at-least-one union bounds to select a tier. A per-roll1/75 label is not an assertion of exactly1/75 per completed kill. Preserve actual personal encounter probability separately where supported for truthful effort/FP review. This does not authorize erasing task conditions, choosing a favorable alternative, unknown party allocation or unsupported approximate rates.

Meaningful source-specific item, actual access, fresh active personal assigned-source drop, one specific item, player keeps item, once-only settlement and no reroll/pity cap remain. Prior ownership does not disqualify a hunt. No generic RDT/generic loot is admitted merely by its fraction.

## Existing44-hunt migration

[Full machine table](catalogs/challenge_tier_rules_v2.json) maps every prior exact hunt/raw rate to its new bracket. The original v1 JSON/CSV and source/FP receipts stay byte-identical historical inputs.

| Tier | Previously authored hunts now mapping here |
| --- | ---: |
| Lesser |0 |
| Standard |0 |
| Greater |29 |
| Superior |15 |

The old roster was selected from a narrow rare-drop band, so it naturally lacks easier-tier targets. This is not a claim that the new Lesser/Standard pools are ready. Expanded four-tier source/access/receipt/reward authoring is a new v2 deliverable; raw collector records never become admitted entries by classification alone. V1 FP values are preserved for history, not newly quoted tier rewards.

Examples from supplied cached data for easier-tier review:

| Source | Specific item | Reported base rate | Bracket | Readiness |
| --- | --- | --- | --- | --- |
| Scurrius | Scurrius' spine |1/33 | Lesser | Source lead; solo/room/loot context, access, receipts and reward need review |
| Hespori | Bottomless compost bucket |1/35 | Lesser | Source lead; repeatable seed/farming/access route and timing/permissions need review |
| Corporeal Beast | Spirit shield |8/512=1/64 | Standard | Source lead; solo route exists in v1, specific-item/receipt/reward review still required |

Two source rows for Scurrius are contexts, not two creatures. Rate rarity does not guarantee time: a slow1/35 encounter can take longer than a fast1/500 monster. Reward quotes need source-specific effort, not a tier-name multiplier invented here.

## Unlock/control recommendation — proposed, not adopted

The user wants separately unlockable Challenge ranks and control over the hunt being accepted. Four rank identities are adopted; exact prerequisite/offer integration is a material new choice because it changes the old single-card unlock contract.

Recommended progression:

| Unlock step | Card gained | Retains |
| --- | --- | --- |
|1 | Lesser Challenge | Lesser pool |
|2 | Standard Challenge | Lesser and Standard |
|3 | Greater Challenge | Lesser, Standard and Greater |
|4 | Superior Challenge | All four available ranks |

**PROPOSED:** purchase ranks in that order, keep lower ranks, and let the player opt in/out of owned ranks between assignments before new offers. Higher ownership would not force Superior hunts. Offer labels show the exact rank/range before the creature/item reveal. Creature/item still random within the accepted rank; no choosing an exact item or changing a committed assignment. Changes to enabled ranks cannot refresh persisted offers/randomness or become a reroll.

The old Bossing Lesser AND Greater entry gate, first-card bundled targets, expansion purchases, rank prices, selection controls, per-rank actual≥3 diversity readiness and the existing two/three offer slots need a coherent final integration. No new free Lesser unlock, automatic prerequisite chain, four extra offer slots, generic-card tier picker, changed offer frequencies or underfloor pool has been adopted. Existing general gates remain until explicitly superseded; do not implement conflicting old and proposed paths together.

Alternative to ask about: independent rank purchases after a common entry gate. **Next single question: unlock ranks progressively in order, or purchase each rank independently?** Recommendation: progressive, while retaining/allowing opt-out of owned ranks. Ask/wait before adopting the prerequisite topology. A56 retirement proposal remains unrelated and unapproved.

## Validation and status

The classifier runs offline on the supplied cache. Exact endpoints, out-of-band cases, non-unit fractions, ignored multipliers and all44 historical mappings are checked. Source lead row payloads match original JSON/SQLite. No source recrawl, production code or runtime activation. Current-client validation remains future work. The tier rules/migration tables are complete; the expanded v2 catalog and final unlock/offer/FP integration are explicitly unfinished, not falsely marked ready.

## All-tier hunt/candidate presentation — A65

[boss drop table](BOSS_CHALLENGE_HUNT_TABLE.md) and [monster drop table](MONSTER_CHALLENGE_HUNT_TABLE.md) now displays all44 prior reviewed hunts and the complete qualifying scalar-rate groups from the explicit named unique-item review scope, split into four tiers. New candidates and unresolved expressions are visibly held, not admitted; historical FP is separate from TBD v2 quotes. The source-preserving review index/CSV defines the exact scan scope and counts. This file update does not answer unlock topology or complete a production-ready v2 catalog.

## Current two-table presentation — WB-D146 / A66

Use [monster drops](MONSTER_CHALLENGE_HUNT_TABLE.md) for non-boss sources and [boss drops](BOSS_CHALLENGE_HUNT_TABLE.md) for boss-only sources. Both include all four new tiers and keep drop rates/conditions/status. The former mixed file and historical-archive filename have been renamed/replaced; historical original content remains in Git commits. The shared index/CSV now has source_kind. This display split does not decide card-family unlock topology or admit candidates.
