# Fate assignments and cards

## Current confirmed rules
Standard is unlocked by default. Fresh players receive two independently generated Standard offerings with different objective targets. Cards may share a type. This is a NEW user-approved design decision on 2026-10-09 (WB-D050), not recovered historical approval.

Purchased types permanently enter their respective eligible pools; an unlock does not guarantee that type in a draw. The third-card upgrade changes two offerings to three. Skilling pools support Lesser/Standard/Greater/Challenge/Wilderness Challenge as unlocked and eligible. Bossing pool supports Lesser/Standard/Greater. Do not silently introduce Bossing Challenge cards.

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
