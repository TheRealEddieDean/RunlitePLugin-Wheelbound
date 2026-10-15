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

## Confirmed progressive unlocks — WB-D147

The user explicitly confirmed progressive purchases on2026-10-15: **Lesser Challenge → Standard Challenge → Greater Challenge → Superior Challenge**, retaining every lower rank. Each step is a permanent paid rank unlock; no automatic free rank or separate generic-entry charge.

| Purchase | Required permanent ownership | Retained ranks |
| --- | --- | --- |
| Lesser Challenge | Ordinary Bossing Lesser AND Greater (preserved WB-D124 entry) | Lesser |
| Standard Challenge | Lesser Challenge | Lesser, Standard |
| Greater Challenge | Standard Challenge | Lesser, Standard, Greater |
| Superior Challenge | Greater Challenge | All four |

Buying Skilling namesakes cannot satisfy Bossing gates. Rank ownership does not unlock normal bosses, bypass actual source access, settle a hunt or grant a ready catalog. The existing starter-target grant belongs to the first Challenge purchase; exact v2 members/prices remain unfinished. Four ranks do not create four extra offer slots. Rank controls, offer integration and per-rank diversity are follow-on work; do not infer them from topology alone.

## Validation and status

The classifier runs offline on the supplied cache. Exact endpoints, out-of-band cases, non-unit fractions, ignored multipliers and all44 historical mappings are checked. Source lead row payloads match original JSON/SQLite. No source recrawl, production code or runtime activation. Current-client validation remains future work. The tier rules/migration tables are complete; the expanded v2 catalog and final unlock/offer/FP integration are explicitly unfinished, not falsely marked ready.

## All-tier hunt/candidate presentation — A65

[boss drop table](BOSS_CHALLENGE_HUNT_TABLE.md) and [monster drop table](MONSTER_CHALLENGE_HUNT_TABLE.md) now displays all44 prior reviewed hunts and the complete qualifying scalar-rate groups from the explicit named unique-item review scope, split into four tiers. New candidates and unresolved expressions are visibly held, not admitted; historical FP is separate from TBD v2 quotes. The source-preserving review index/CSV defines the exact scan scope and counts. This file update does not answer unlock topology or complete a production-ready v2 catalog.

## Current two-table presentation — WB-D146 / A66

Use [monster drops](MONSTER_CHALLENGE_HUNT_TABLE.md) for non-boss sources and [boss drops](BOSS_CHALLENGE_HUNT_TABLE.md) for boss-only sources. Both include all four new tiers and keep drop rates/conditions/status. The former mixed file and historical-archive filename have been renamed/replaced; historical original content remains in Git commits. The shared index/CSV now has source_kind. This display split does not decide card-family unlock topology or admit candidates.

## Full supplied-monster inventory review — A67

The [monster table](MONSTER_CHALLENGE_HUNT_TABLE.md) now contains327 tiered source/item/rate rows (22 Lesser,34 Standard,164 Greater,107 Superior):18 prior contexts and309 candidates. The full1,437-monster/31,757-row source inventory was reviewed, replacing the monster retrieval whitelist.171 unresolved signature-rate rows and401 significance/activity leads are held visibly;399 captures have no supported drop rows. See the [coverage report](research/MONSTER_DROP_REVIEW_A67.md) and [complete row audit](research/MONSTER_DROP_AUDIT_A67.json). Boss table membership stays156. These supersede A66 display counts only, not approved tier/unlock/completion policies. V2 admission/rewards remain unfinished.

## Lower-tier context review — A68

The [seven-context review](research/LOWER_TIER_CONTEXT_REVIEW_A68.md) advances access/identity/receipt preparation for elemental gems and two Lesser contexts without admitting candidates. Jelly Lesser is task-dependent; Infernal Mage has page-level estimated-rate evidence; sea-snake source grouping and Mogre shore access need precise review. No table membership, FP quote, rank topology or runtime enablement changes.

## Current Challenge rank control — WB-D148

[Rank controls](CHALLENGE_RANK_CONTROL_CONTRACT.md) separate owned/enabled/ready. First Lesser confirmation enables Lesser; later purchases preserve settings and start disabled unless explicitly opted in. Owned-rank settings may change only with no assignment/obligation/pending offers/unresolved selection. All off is allowed and suppresses only new Challenge offerings. Pending/paused/restored work stays immutable; no target toggles or reroll. This supersedes older selector-proposed and automatic-pool-entry wording for Challenge ranks only.

## Current ranked Challenge offering — WB-D149

[Rank offering contract](CHALLENGE_RANK_OFFER_CONTRACT.md) preserves two/three total slots and one Challenge family. Each Challenge offer uniformly commits a ready enabled rank before display; player selects the visible rank card, then uniform canonical creature/item within it. Require≥3 actually eligible canonical keys **within that rank**, supported context and reviewed v2 quote. Extra ranks divide the existing family share; no fourfold family boost, extra slots, rank picker or forced distinct rank offers. Numeric family weights remain BALANCE_TBD. Frozen offers/settings/pools do not refresh on restore. This supersedes older rank/floor/offer-integration proposals only; production is disabled.

## Current shared Challenge targets and purchase contract — WB-D150

[Rank purchases](CHALLENGE_RANK_PURCHASE_CONTRACT.md) grant one shared canonical Challenge target entitlement across reviewed rank contexts; no target repurchase per rank. The first paid Lesser entry includes one reviewed versioned bundle and explicit Lesser enable, atomically; later ranks grant ownership only unless separately opted in at a legal boundary. Target/access/rank ownership remain distinct. Historical v1 six starters/quotes are preserved and not a Lesser-ready bundle. V2 exact bundle/prices/quotes are unfinished; missing payload cannot be sold or labeled free. No normal roster change or blanket legacy four-rank grant.

## A79 current starter-content supersession — WB-D151

[Starter v2 draft](CHALLENGE_STARTER_V2.md) now authors four Stronghold source keys/four specific component hunts for the future initial Lesser bundle. Earlier exact-v2-members-NOT_AUTHORED wording is superseded for this partial bundle only. Prices, effort quotes(A80), source/client validation and full four-rank content remain separate. Historical v1 grants/quotes untouched; no activation or guarantee of three actually eligible keys.

## Current A74–A83 content checkpoint

[Current content status](CHALLENGE_CONTENT_STATUS.md) supersedes older missing-identity/member/A80-quote descriptions: four Stronghold Lesser draft sources and FP are authored; elemental identities are pinned, three gem probability holds remain. Price/frequency candidates are unadopted, client tests NOT_RUN, full four-rank content unfinished and runtime disabled. Read that status before selecting the next task; do not repeat A67 inventory or resolved rank design.
