# Fate assignments and cards

## Current confirmed rules
Standard is unlocked by default. Fresh players receive two independently generated Standard offerings with different objective targets. Cards may share a type. This is a NEW user-approved design decision on 2026-10-09 (WB-D050), not recovered historical approval.

Purchased types permanently enter their respective eligible pools; an unlock does not guarantee that type in a draw. The third-card upgrade changes two offerings to three. Skilling pools support Lesser/Standard/Greater/Challenge/Wilderness Challenge as unlocked and eligible. Bossing pool supports Lesser/Standard/Greater/Challenge. WB-D122 explicitly confirms the fourth type; catalog/detector/generation details still require validation.

## Skilling XP target generation — WB-D091
Ordinary Skilling objectives use XP, not mandatory material quotas. Determine current level bracket, apply the skill-specific requirement modifier, randomize and round to a documented clean increment. Explain resulting range and modifiers. Exact tables/increments remain BALANCE_TBD; the old illustrative brackets are not approved constants. There is no second XP wheel. The current two independently generated Standard targets remain distinct and committed before display. A future faithful model must represent skill modifiers explicitly; prior synthetic experiments are not proof of these rates.

## Assignment commitment
Snapshot real skill level and eligibility. Independently generate each offered objective. Persist the complete offering set before display. Different targets are mandatory for the two starting Standard cards; collision resolution must not resample the whole set or reroll an accepted objective. A technical proposal is independently seeded draws followed by rejection sampling of duplicate targets; this conditions the final distribution on uniqueness and must be disclosed in simulations. Use at least two legal target values or report catalog error without consuming a completed-Fate spin.

Selection freezes objective, reward and restrictions. Restart restores pending offers or accepted assignment. Combat player selects Attack/Strength/Defence/Ranged/Magic before XP target generation; unavoidable legitimate HP XP is exempt.

S3 provisional combat Standard ranges: 1-29: 1,000-3,000; 30-49: 4,000-10,000; 50-69: 12,000-30,000; 70-79: 35,000-65,000; 80-89: 75,000-130,000; 90-99: 140,000-250,000. Not universal skill tables. Lesser skilling reduces XP and FP; Greater increases both. Restrictions must be achievable and observable.

## Bossing sequence - WB-D049
Master Wheel selects Bossing -> generate/display Standard kill count -> offer eligible Bossing Fate Cards -> player selects card -> randomly spin unlocked eligible Bossing Wheel -> complete assignment.

Each offering carries a Standard reference target; fresh duplicate Standard offerings use different independently generated target counts. The initially displayed Standard count is the first offer's reference; the second card clearly displays its own reference. This display interpretation is PROPOSED technical detail, not an additional recovered approval.

Lesser reduces kill count and retains base FP. Standard uses normal count/reward. Greater increases count and FP. Exact multipliers and boss-tier target bands are BALANCE_TBD. Boss is not chosen or rerolled by player. Card/target choice occurs before boss selection. Boss wheel eligibility must be frozen before offers so changing access midway cannot manipulate the random pool; commit the selected boss durably before animation.

Quest choices are distinct quests, separate from same-type card offerings. C01 is RESOLVED: current user confirmation supersedes prior contradictory PDF text. See [PROGRESSION](PROGRESSION.md), [PERSISTENCE](PERSISTENCE.md).

## Catalog drafting checkpoint

See [CONTENT_CATALOGS](CONTENT_CATALOGS.md) and [CATALOG_ACCEPTANCE](CATALOG_ACCEPTANCE.md) for newly authored drafts (WB-D079/080), distinct from missing historical catalogs. Machine-readable 100 bounty candidates, 50 punishments, 67 repository boss categories and 213 quest enum records are in catalogs/. Source metadata is not runtime eligibility. All unvalidated bounty/punishment entries remain disabled. H3 exercises named bounty/reward and plain-XP punishment drafts in synthetic trials only; exact quest/boss access remains unmodeled.

## Recovered purchase-pool separation
S8-M0227 explicitly locks separate Skilling and Bossing Fate Card purchases/pools, with shared names permitted and different artwork suggested. Buying a Skilling card does not automatically buy its Bossing namesake. S8-M0231 explicitly confirms qualification-before-generation and reliable completion verification for Challenge cards. Current default Standard/two distinct targets/third offering approvals remain controlling. WB-D122 resolves card existence; WB-D128 narrows it to significant unique-drop hunts only. Runtime content stays gated until supported/validated.

WB-D098 confirms five Combat primary skills and Hitpoints by-product-only. Slayer permits combat XP only while legitimately completing its assigned objective; this is not a blanket other-training exemption. Use explicit observable permission profiles. WB-D096 preserves the distinction between choosing an activity through wheel odds and eligible card offerings after selection.

WB-D103 confirms Wilderness as purchased opt-in card content. Historical broad CA Challenge slice concept is confirmed at S8-M0219–0221 (WB-D047), but exact library, slot/minimum classification and reward rules are unverified. WB-D047 broad Challenge-slice rules remain separate. WB-D128 now removes CA objectives from Bossing Challenge; do not reintroduce that earlier two-family concept into this card.

## Current clarification2026-10-12
WB-D113 reaffirms Lesser/Standard/Greater for Skilling XP and Bossing KC.5/10 kills is illustrative, not an approved multiplier. Prior Bossing Lesser base FP, Greater increased FP, separate pools and random boss/no reroll remain. WB-D122 subsequently explicitly confirms a fourth named Challenge card; earlier Q9 uncertainty is superseded. WB-D111 adds automatic Master retry after a known-empty Questing landing, not boss rerolls or regeneration of already committed valid offers.

WB-D120: repeated known-empty Questing landings are zero-cost Master retries; only the eventual valid Fate consumes one allotted spin. No reward/Punishment for empty attempts; no valid boss/offer rerolls. Existing zero-spin gate and durable replay apply.

## Current Bossing Challenge — WB-D128

Fourth Bossing card, in addition to Lesser/Standard/Greater. Objective: obtain a significant creature/encounter-specific unique item drop from the randomly assigned boss/creature. **No Combat Achievement objectives and no CA-versus-drop family draw.** WB-D128 supersedes those parts of WB-D122/127. User example: Tormented Demons→Tormented Synapse; exact source/rate metadata remains catalog-validation work.

### Purchase and expansion — WB-D124

Owned Bossing Lesser AND Bossing Greater → purchase Bossing Challenge with included authored starter target unlock bundle → further individual creature target unlocks. Skilling card purchases cannot satisfy this prerequisite. Starter grants apply to Challenge targets only, not free normal L/S/G boss unlocks. Normal boss unlocks reused where applicable for expansion; exact starter list/count/tier/prices remain delegated candidate work.

### Target pool and eligibility — WB-D123/125/126

Dedicated reviewed Challenge pool contains bosses and selected creatures with supported unique-drop objectives. Normal L/S/G target pool remains unchanged. Creature-specific significant uniques are eligible candidates; shared rare-drop-table and generic loot excluded. WB-D131 fixes inclusive1/400–1/1500 eligibility for the specific target item; more common/rarer items excluded. WB-D133/134 fix personal probability and explicit supported rate contexts; actual source formulas/roster still need review. Review actual access, source identity, expected hunt time and reward candidates without adding a universal time/kill cap.

Require owned Challenge and≥3 distinct actually eligible boss/creature target keys before new Challenge offerings. Multiple item objectives/NPC forms on one creature count once. CA-only objectives cannot count. Unknown/inaccessible/unsupported targets do not count. Belowfloor card/bundle stays owned; omit type from new offers and explain reason. This is not a purchase ban, refund, penalty, arbitrary level/Grand gate or third-card upgrade; default2/upgrade3 offerings remain. Preserve already committed results rather than rerolling after eligibility changes.

### Assignment and completion

Player selects Challenge card; Fate randomly selects its eligible boss/creature and authored unique-drop objective, with no player target/item picker or valid-result reroll. No objective-family coin flip remains. Freeze target/item/source, relevant pool/catalog/eligibility, reward quote and complete selected result durably before relevant reveal. WB-D135/136 fix post-card-selection durable reveal and equal-creature then equal-logical-item draws; no family coin flip or normal roster expansion.

Require supported active fresh acquisition from assigned source; existing possession, buying/trading, unrelated identical item or repeated callback cannot complete it. WB-D129 confirms already-owned items may still be assigned; require a fresh active assigned-source drop during the assignment. Old inventory/bank/collection-log possession, withdrawals, buy/trade or drop-and-repick never completes it. A qualifying drop can independently earn item Bounties Shop FP under its own rules; Reject never revokes a qualified independent bounty. No paused reward credit. Keep Challenge/normal KC state and separate purchase pools distinct. Catalog/source/client proof still gated; no runtime adapter enabled.

## Specific item and rarity bounds — WB-D130/131
Select one specific unique item, clearly shown and frozen with its source/reward/context. Other eligible uniques from that creature do not substitute. Require reviewed specific-item probability1/1500≤p≤1/400 (inclusive), not total chance of any unique or conditional subtable chance. Unknown/unverified/out-of-band/shared-table items cannot enable an entry. This bounds rarity, not actual kills/time: no guaranteed drop after1500 or completion without the fresh item. WB-D133/134 use personal receiving probability in reviewed supported contexts, not group chance; concrete source formulas and weights/time/FP review remain gated.

WB-D133/134: use reviewed personal receiving probability for one specific item per authored encounter/loot opportunity. Freeze source/mode/context IDs, assumptions, formula/source revision and exact probability or fully in-band range. Unknown allocation/context/range is unavailable for new offers. Do not assume group chance/team size. Preserve committed hunt/item/reward on later context failure; explain unavailable qualification and retain existing Pause/Reject. Only supported fresh active personal assigned-source receipts qualify; variants do not multiply target diversity. Concrete source formulas/live proof remain gated.

WB-D135–137: follow [Challenge assignment contract](CHALLENGE_ASSIGNMENT_CONTRACT.md). Freeze eligible canonical pool/context/reward schedule and offer randomness before display; card selection precedes durable uniform creature-then-item result and exact reward reveal. Variants/contexts are not extra lottery entries. Challenge uses fresh-drop completion, no KC quota or item consumption. Receipt/restart/odds cases are planned NOT_RUN; actual catalog/quotes/source adapters remain gated.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.

WB-D140/141: prioritize repeatable task-independent starter contexts; hold finite-task-dependent hunts until supported continuation/rate/access review. Judge meaningful unique function without a new GE-price minimum; generic loot remains excluded. [A48 batch1 candidate review](research/CHALLENGE_CANDIDATE_REVIEW_B1.md) has seven provisional hunts, source freshness/access limitations and runtime-disabled records. Exact starter membership/size/tiers/prices/rewards remain unapproved; source excerpts do not enable eligibility. Planned acceptance NOT_RUN: task exhaustion not waived, price change alone not a target reroll, supported source/context required.

## Concrete Bossing Challenge table v1

WB-D142/143 completes [boss drop table](BOSS_CHALLENGE_HUNT_TABLE.md) and [monster drop table](MONSTER_CHALLENGE_HUNT_TABLE.md), with six included starter creatures and source-specific access/rates/fixed FP/receipt contracts. Use the finite versioned table for future pool construction; never raw Wiki labels. Actual accessibility/ownership/validated adapters and≥3-key floor still apply. All entries stay runtime-disabled pending engineering; no normal L/S/G change or broadened Challenge objectives.

## Current Challenge rarity/card supersession — WB-D144/145

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) controls future Challenge rank placement: Lesser reciprocal25–50, Standard>50–125, Greater>125–750, Superior>750–5000. Ignore displayed roll-count multiplier for tier classification; preserve raw rolls/personal encounter probability separately. This supersedes old single1/400–1/1500/aggregate-rate bracket statements in this chapter for future selection only. Fresh personal exact assigned-source completion, source/context review and existing settled assignments remain protected. WB-D147 confirms progressive Lesser→Standard→Greater→Superior purchases retaining lower ranks; the preserved ordinary Bossing Lesser AND Greater entry gate attaches to the first paid Lesser Challenge. Enabled-rank control/offer integration and expanded v2 roster/rewards remain follow-on work. Do not treat the preserved v1 roster/FP or lower-tier source leads as a ready four-tier pool. No runtime implementation/main change.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.

## Current Challenge rank control — WB-D148

[Rank controls](CHALLENGE_RANK_CONTROL_CONTRACT.md) separate owned/enabled/ready. First Lesser confirmation enables Lesser; later purchases preserve settings and start disabled unless explicitly opted in. Owned-rank settings may change only with no assignment/obligation/pending offers/unresolved selection. All off is allowed and suppresses only new Challenge offerings. Pending/paused/restored work stays immutable; no target toggles or reroll. This supersedes older selector-proposed and automatic-pool-entry wording for Challenge ranks only.

## Current ranked Challenge offering — WB-D149

[Rank offering contract](CHALLENGE_RANK_OFFER_CONTRACT.md) preserves two/three total slots and one Challenge family. Each Challenge offer uniformly commits a ready enabled rank before display; player selects the visible rank card, then uniform canonical creature/item within it. Require≥3 actually eligible canonical keys **within that rank**, supported context and reviewed v2 quote. Extra ranks divide the existing family share; no fourfold family boost, extra slots, rank picker or forced distinct rank offers. Numeric family weights remain BALANCE_TBD. Frozen offers/settings/pools do not refresh on restore. This supersedes older rank/floor/offer-integration proposals only; production is disabled.
