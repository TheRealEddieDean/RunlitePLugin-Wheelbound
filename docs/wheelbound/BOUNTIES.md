# Bounties
Confirmed assignment: 100 curated permanent item bounties; three randomized daily item bounties; Combat Mastery; Achievement Diaries.
Permanent one-time independently balanced payouts with rarity presentation; daily optional, not progression requirement. Combat Mastery rewards all CAs for a boss; diary region/tier rewards. Active legitimate event evidence required. Awards are manually claimed (S8-M0057/WB-D094); evidence is retained as unclaimed before ledger credit. Paused events never retroactively qualify.

Claim transaction validates evidence and claim key before ledger credit; rerun does not repay. Historical 100 item catalog and reward tiers missing. Imbued heart +500 FP was an earlier example in memory, not final payout.

Daily refresh every 24h in S3; rolling interval versus calendar reset, timezone, offline catch-up, expiry and duplicate-selection rules OPEN. Do not silently substitute UTC midnight. Pre-owned items, acquisition from GE/trading, repeated drops, group loot, simultaneous permanent/daily eligibility and CA/diary completion across pause need detailed policy.

## Proposed evidence/claim contract
Evidence = account/run, bounty/catalog version, unique event key, source activity, item variation key/quantity, active-mode/assignment permission and observed time. Existing possession/container movement alone is not qualifying acquisition. NPC drops, raid chests, skilling uniques and crafting require source-specific adapters. Persist evidence once; later manual claim credits ledger once.

Permanent key = runId+bountyId; daily key includes immutable daily offer ID. NEW PROPOSED overlap: one legitimate acquisition may satisfy permanent and active daily entries, each with separate once-only keys.

NEW PROPOSED schedule: rolling 24h epoch anchored at first board creation, seeded by run/epoch; three distinct eligible targets. Offline time creates no accumulated boards. Pre-expiry qualifying evidence remains claimable afterward; expired board receives no new progress. This is delegated design, not historical approval.

Historical 100-item catalog and payouts remain missing. Do not generate 100 speculative items and call them recovered. Import the original catalog or review a separately labelled new one before implementation. Diary/CA events need active permitted evidence; paused completion is not retroactive progress.

## Catalog drafting checkpoint

See [CONTENT_CATALOGS](CONTENT_CATALOGS.md) and [CATALOG_ACCEPTANCE](CATALOG_ACCEPTANCE.md) for newly authored drafts (WB-D079/080), distinct from missing historical catalogs. Machine-readable 100 bounty candidates, 50 punishments, 67 repository boss categories and 213 quest enum records are in catalogs/. Source metadata is not runtime eligibility. All unvalidated bounty/punishment entries remain disabled. H3 exercises named bounty/reward and plain-XP punishment drafts in synthetic trials only; exact quest/boss access remains unmodeled.

[ACQUISITION_EVIDENCE](ACQUISITION_EVIDENCE.md) specifies the separate 18-entry repeatable daily draft, owned receipt deduplication, reward-exchange provenance and clock/expiry cases (WB-D084). Rare permanent targets are not implicitly daily candidates. H3 does not include daily payouts.

## Recovered presentation and activation rule — WB-D094
Eligible bounties are monitored automatically while the mode is active; visiting the catalog is not a prerequisite for credit. Persist unclaimed evidence, show sidebar/category indication and completion animation/message, and require manual claim. Use actual item sprites, name search, rarity filters and FP sorting. Common/Uncommon/Rare/Epic/Legendary are the historically accepted provisional labels; balance each reward independently rather than forcing a fixed FP amount by label. Paused progress remains excluded.

## Recovered final bounty identity — WB-D101
All goals are visible. Hidden/surprise bounties were rejected at S8-M0239, superseding the earlier keep-hidden-as-option discussion. Permanent targets are finite and cross-activity; include grouped any-third-age and any-gilded goals. Combat Masteries mean all CAs for a boss, not collection-log completion. Diaries have region/difficulty categories and independent substantial rewards. Existing H3 item draft does not contain all these required families and must not be presented as final; revise in a new catalog version. Curated daily candidates are delegated content hygiene, and daily availability never overrides an active Fate.

Bounded catalog review and exact continuation: [BOUNTY_CATALOG_REVIEW](BOUNTY_CATALOG_REVIEW.md).

[Diary/CA source and receipt review](DIARY_CA_RECEIPT_REVIEW.md) maps all 48 diary tier signals, including legacy Karamja aliases; live semantics and boss task-set mapping remain gated.

The separate [finite-100 normative candidate](catalogs/item_bounties_normative_v1.json) restores grouped third-age/gilded intent while preserving H3 inputs. It is a new delegated candidate, not an approved final member/reward list. All runtime entries remain disabled.
