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
S8-M0227 explicitly locks separate Skilling and Bossing Fate Card purchases/pools, with shared names permitted and different artwork suggested. Buying a Skilling card does not automatically buy its Bossing namesake. S8-M0231 explicitly confirms qualification-before-generation and reliable completion verification for Challenge cards. Current default Standard/two distinct targets/third offering approvals remain controlling. WB-D122 resolves historical Q9 existence: Bossing Challenge is confirmed, with difficult CA or significant rare-drop objectives; runtime content stays gated until supported/validated.

WB-D098 confirms five Combat primary skills and Hitpoints by-product-only. Slayer permits combat XP only while legitimately completing its assigned objective; this is not a blanket other-training exemption. Use explicit observable permission profiles. WB-D096 preserves the distinction between choosing an activity through wheel odds and eligible card offerings after selection.

WB-D103 confirms Wilderness as purchased opt-in card content. Historical broad CA Challenge slice concept is confirmed at S8-M0219–0221 (WB-D047), but exact library, slot/minimum classification and reward rules are unverified. WB-D047 broad Challenge-slice rules remain separate. WB-D122 now expressly permits difficult CA objectives in the opt-in Bossing Challenge card; build a feasible, observable eligible catalog rather than treating all remaining CAs as automatically assignable.

## Current clarification2026-10-12
WB-D113 reaffirms Lesser/Standard/Greater for Skilling XP and Bossing KC.5/10 kills is illustrative, not an approved multiplier. Prior Bossing Lesser base FP, Greater increased FP, separate pools and random boss/no reroll remain. WB-D122 subsequently explicitly confirms a fourth named Challenge card; earlier Q9 uncertainty is superseded. WB-D111 adds automatic Master retry after a known-empty Questing landing, not boss rerolls or regeneration of already committed valid offers.

WB-D120: repeated known-empty Questing landings are zero-cost Master retries; only the eventual valid Fate consumes one allotted spin. No reward/Punishment for empty attempts; no valid boss/offer rerolls. Existing zero-spin gate and durable replay apply.

## Confirmed Bossing Challenge — WB-D122

Objective families: difficult Combat Achievement OR significant rare drop from an assigned boss/creature. User example: Tormented Demons→Tormented Synapse. The roughly rarer-than1/400 phrase is catalog guidance, not a locked threshold or verified rate. Do not silently turn it into a1/400 numeric eligibility test. Lesser/Standard/Greater remain XP/KC-scaled cards; Challenge is a distinct completion objective.

Purchase is in the separate Bossing card pool under existing permanent-unlock rules. WB-D123 confirms a dedicated eligible Challenge target pool containing bosses and selected creatures, separate from normal L/S/G target pool. WB-D124 requires Bossing Lesser AND Greater before Challenge, grants a starter target bundle, and then exposes individual target expansion. Exact members/count, generation order, CA difficulty, reward/time tuning and evidence still need specification. Preserve random boss/creature selection/no rerolls; do not make Challenge a player target chooser or automatically add creatures to all normal Bossing nodes. Cards and exact selected objectives must be committed before their relevant display/acceptance, with frozen catalog/evidence context; final sequence for this objective family awaits objective-generation/unlock clarification; target-pool separation is confirmed by WB-D123.

Rare-drop completion needs qualifying assigned-source acquisition, not possession/purchase/trade or an unrelated identical item. CA objective completion needs supported task evidence, not generic CA points or another achievement. A drop may also independently qualify an item Bounties Shop reward under its own rules; that bounty remains independent if the Fate is rejected. Overlap payout keys/rewards are not duplicated by callbacks and no numeric payout is selected here. No live validation or production adapter enabled.

## Dedicated Bossing Challenge targets — WB-D123
Do not confuse card unlock pool (permanently bought Bossing types) with target pool (eligible bosses/creatures). Challenge randomly selects from its dedicated reviewed eligible target pool; L/S/G randomly selects from the existing normal Bossing pool. Non-boss Challenge creatures do not enter normal KC draws merely by appearing here. No player choices/rerolls. Freeze the relevant target catalog/eligibility/version and selected outcome durably; no restart resampling. WB-D124 fixes AND prerequisite/starter-bundle/individual expansion structure; exact bundle/price/gates and target-generation timing remain pending.

WB-D124: purchasing Bossing Challenge requires owned Bossing Lesser AND Greater and atomically grants card plus starter target unlock set. Do not offer a one-target purchased pool by making the player individually buy the starter entries. Further target expansion follows the authored tree/individual unlocks. WB-D125: rare-drop family uses meaningful creature-specific uniques and excludes shared rare-drop-table results;1/400-ish rarity screen still needs final reviewed catalog definition. CA family remains separate. WB-D126 confirms at least three distinct actually eligible bosses/creatures before offering Challenge; permanently owned card/bundle is retained below that floor.

## Actual Challenge diversity gate — WB-D126
Before generating new offerings, count distinct reviewed eligible Challenge boss/creature target keys with at least one supported valid objective. Require≥3 and owned Bossing Challenge. Multiple CAs/drops/forms on one creature do not increase this count. Owned but inaccessible/unsupported/unknown is not eligible. Below3, keep card/bundle ownership and omit only this card type from new offers, with a clear reason; L/S/G rules and default2/upgrade3 offer counts unchanged. Preserve already committed offers/results; no resampling or purchase refund. Exact objective-family selection/reveal policy remains pending.
