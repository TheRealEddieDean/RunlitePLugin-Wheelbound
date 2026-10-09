# Bounties
Confirmed assignment: 100 curated permanent item bounties; three randomized daily item bounties; Combat Mastery; Achievement Diaries.
Permanent one-time rarity-based payouts; daily optional, not progression requirement. Combat Mastery rewards all CAs for a boss; diary region/tier rewards. Active legitimate event evidence required. Manual claim where specified; S3 says all manually claimed. Paused events never retroactively qualify.

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
