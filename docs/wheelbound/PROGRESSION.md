# Progression
Four branches: Skilling, Questing, Combat/Bossing, Fate Manipulation. Free introductory node beneath illuminated root (S8-M0081–0083; WB-D093). AND/OR connections explicit (S8-M0063–0065): nodes have their own benefits and FP alone cannot bypass prerequisites. Unlocks persist for run. Skill prerequisites and FP purchase both required.

Recovered groups now CONFIRMED at S8-M0099–0111 (WB-D027):
Tier I: Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, Prayer. Unlock five of nine for Tier II.
Tier II: Slayer, Herblore, Farming, Construction, Sailing, Hunter. Unlock four of six for Mastery.
Starting Mining/Fishing/Woodcutting/Combat/Questing are outside the listed purchased tier-node sets; count distinct purchased skill unlocks, never duplicates or active slices.
Mastery permits advanced duplicates/enhancements; exact prerequisites and whether low-level duplication is possible are OPEN. No Favored Skill system explicitly confirmed at S8-M0121 (WB-D096).

Questing: CONFIRMED by retrieved user message 2026-10-01 17:16:22Z: three distinct choices -> upgrades four/five; Low/Medium/Hard/Master/Grandmaster progression via completed quest Fates and FP milestones. Eligibility toggle applies current requirements. These Wheelbound names require a mapping to OSRS quest difficulties; exact mapping/gates OPEN. No eligible choices requires recovery flow, not an impossible assignment.

Bossing: separate eligible boss wheel; individual boss unlocks and peer gates. S3 pool counts Easy 9, Medium 13, Hard 22, Elite 11, Master 6, Raids/Grandmaster 3 (ToA/CoX/ToB). Counts/grouping are now explicitly sourced to S8-M0204–0205; mode_boss_roster.json maps 64 nodes from the pinned normal source. Peer thresholds and live eligibility remain unfinished. Retrieved user 2026-10-01 17:19:57Z approved a general Challenge slice concept so challenges do not depend on a boss's dwindling incomplete CAs. Exact placement, pool and interaction with cards are UNVERIFIED. CA mastery rewards in Bounties rather than boss-roll eligibility.

No assignment can unlock its own access prerequisites implicitly. See [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md).

## Confirmed Bossing card sequence
WB-D049 resolves earlier conflicting summaries: generate/show Standard kills, offer eligible Lesser/Standard/Greater, select a card, then randomly determine the unlocked eligible boss. No player boss choice or reroll. A card unlock never unlocks a boss. Standard default/two distinct targets and third offering follow WB-D050.

## Proposed prerequisite representation
Node = stable ID, unlock, FP quote, prerequisite expression, real game prerequisites, purchase state. Explicit allOf/anyOf/countOf expressions; screen placement never implies an undocumented prerequisite. Skill count gates count unique purchased skill unlocks, not duplicate slices.

Proposed quest mapping: Low=Novice, Medium=Intermediate, Hard=Experienced, Master=Master, Grandmaster=Grandmaster; Special individually reviewed. H1 uses eight completions per tier proxy, not real quest-chain eligibility. Exact purchased gate prices remain BALANCE_TBD.

Boss pools require a reviewed versioned catalog. Do not manufacture encounters to reach historical counts. H1's six synthetic tiers are not the historical 64-entry catalog. Task-only bosses require enough remaining assignment kills for the selected target.

Completed quests/inaccessible bosses cannot become valid merely to keep a slice usable. Eligibility conditioning changes wheel odds and must be explicit. WB-D138/139 retain exhausted ordinary slices and weights with verified-empty landing retries/readiness stop; no free suspension/storage/edit.

## Catalog drafting checkpoint

See [CONTENT_CATALOGS](CONTENT_CATALOGS.md) and [CATALOG_ACCEPTANCE](CATALOG_ACCEPTANCE.md) for newly authored drafts (WB-D079/080), distinct from missing historical catalogs. Machine-readable 100 bounty candidates, 50 punishments, 67 repository boss categories and 213 quest enum records are in catalogs/. Source metadata is not runtime eligibility. All unvalidated bounty/punishment entries remain disabled. H3 exercises named bounty/reward and plain-XP punishment drafts in synthetic trials only; exact quest/boss access remains unmodeled.

WB-D081 source check: Sailing is live (Jagex launch 2025-11-19) and exposed in RuneLite Skill.SAILING. Keep the proposed Tier II placement; method-specific assignments still need access/by-product evidence.

## First historical batch provenance
S8-M0087 explicitly approves the four branches; S8-M0089 explicitly separates permanent content unlock from cheap wheel addition. S8-M0099 moves Prayer to Tier I and Hunter to Tier II; this placement is explicitly approved, not merely PDF inference. Earlier 5/9 and 4/6 gate proposals were balance targets in S8-M0096/0098; later explicit approvals S8-M0107/0111 confirm the current working gates (WB-D027). Preserve that proposal-to-approval evolution. Starting FP grant, free-child name and first-completion timing remain provisional.

## Recovered cluster, Mastery, quest and boss architecture
WB-D095 resolves skill layout: Tier-I and Tier-II peer skills form circular clusters within the wider vertical progression tree. The gold Master Progression Line tracks progress to the next gate, not overall cluster completion; five/nine owned is a complete five-of-nine milestone. Other skills remain purchasable.

WB-D096 separates Mastery’s per-slice odds/duplicates from post-selection Fate Manipulation card offerings. WB-D097 places Questing milestones in the tier purchase itself: X preceding-tier assigned Quest Fates plus FP. Do not add filler gate nodes or silently implement highest-tier preference, which was asked but not answered in the reviewed exchange.

WB-D099 confirms bosses are peer unlocks within each tier, with any-X purchase gates and a final raids tier. Boss purchase adds the encounter to the separate Bossing pool, not another Master activity slice. Historical raid regrouping/counts will be reconciled in A04; the current normal-plugin 67-entry snapshot remains source metadata.

WB-D100 makes boss purchases a permanent pool commitment: do not let ordinary Master-slice storage/destruction remove an individual boss from the Bossing pool. An expensive boss-erasure idea was not finalized. FP/tier prerequisites determine purchase permission; the player judges readiness, with actual game access still necessary for assignment. No extra plugin combat-level gate. The pinned normal 67-category roster is preserved alongside the separately recovered 64-node mode roster.

WB-D111: if no eligible quests remain, a Questing Master landing retains its slice, shows a brief notice and automatically respins; tier/access rules do not become free unlocks. Unknown data is not empty. WB-D120 confirms no extra spin cost for empty retry; eventual valid Fate one spin. WB-D138/139 resolve wider precommit ordinary-pool/readiness policy; postcommit invalidation remains separate.

WB-D122 adds Bossing Challenge to the separate permanent Bossing card unlock pool, alongside L/S/G. Its price/prerequisite and boss/creature eligible pool mapping remain specification/balance work; do not automatically add all rare-drop creatures as normal purchased boss nodes. Existing permanent boss membership/no artificial stat gates remains.

WB-D123: dedicated Challenge target pool may include selected non-boss creatures; normal L/S/G boss roster unchanged. WB-D124 now confirms individual expansion nodes after a bundled starter set. Exact starter members/tiers/prices remain pending; do not grant all targets or alter normal boss nodes.

## Confirmed Bossing Challenge subtree — WB-D124

Bossing Lesser AND Bossing Greater → purchase Bossing Challenge + automatic authored starter target bundle → expanded individual creature target unlock nodes. This is an explicit allOf gate, not either/or; Skilling card counterparts do not satisfy it. Standard is default. Reveal the downstream expansion branch after purchase; prerequisite previews may explain its benefits beforehand without granting access.

Starter targets enter only the Challenge pool; normal L/S/G boss unlocks/64-node grouping are unchanged. Non-starter boss targets reuse existing boss unlocks where applicable; non-boss expansion targets have individual FP unlocks. Exact starter members/count, tiers and prices remain delegated catalog/balance work. Actual access/support gates remain. Ownership variety can differ from eligible variety; WB-D126 requires at least three distinct actually eligible bosses/creatures before new Challenge offers, without revoking card/bundle ownership.

WB-D128: Bossing Challenge subtree targets support unique item-drop hunts only. Preserve Lesser AND Greater gate, included starter grants and expansion nodes; do not add CA-only target nodes to satisfy actual3-target floor. Existing normal boss/CA filters and other historical CA features are separate, not deleted by this card change.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.

WB-D140/141: prioritize repeatable task-independent starter contexts; hold finite-task-dependent hunts until supported continuation/rate/access review. Judge meaningful unique function without a new GE-price minimum; generic loot remains excluded. [A48 batch1 candidate review](research/CHALLENGE_CANDIDATE_REVIEW_B1.md) has seven provisional hunts, source freshness/access limitations and runtime-disabled records. Exact starter membership/size/tiers/prices/rewards remain unapproved; source excerpts do not enable eligibility. Planned acceptance NOT_RUN: task exhaustion not waived, price change alone not a target reroll, supported source/context required.

## Challenge starter bundle v1 — WB-D142

The gated Challenge card includes Brine rat, Turoth, Cave horror, Sulphur Nagua, Kurask and Skeletal Wyvern target unlocks. See [boss drop table](BOSS_CHALLENGE_HUNT_TABLE.md) and [monster drop table](MONSTER_CHALLENGE_HUNT_TABLE.md). Actual source access/skills remain mandatory; require≥3 distinct actually supported accessible keys. Six owned grants are not a promise of six eligible targets. Other v1 creatures are individual expansions; no new numeric purchase price adopted. Dedicated pool/card-before-target and Lesser AND Greater prerequisites remain. These authored members supersede prior unfilled starter-list status, without changing normal L/S/G roster or licensing task-dependent routes.

## Current Challenge rarity/card supersession — WB-D144/145

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) controls future Challenge rank placement: Lesser reciprocal25–50, Standard>50–125, Greater>125–750, Superior>750–5000. Ignore displayed roll-count multiplier for tier classification; preserve raw rolls/personal encounter probability separately. This supersedes old single1/400–1/1500/aggregate-rate bracket statements in this chapter for future selection only. Fresh personal exact assigned-source completion, source/context review and existing settled assignments remain protected. WB-D147 confirms progressive Lesser→Standard→Greater→Superior purchases retaining lower ranks; the preserved ordinary Bossing Lesser AND Greater entry gate attaches to the first paid Lesser Challenge. Enabled-rank control/offer integration and expanded v2 roster/rewards remain follow-on work. Do not treat the preserved v1 roster/FP or lower-tier source leads as a ready four-tier pool. No runtime implementation/main change.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.

## Current shared Challenge targets and purchase contract — WB-D150

[Rank purchases](CHALLENGE_RANK_PURCHASE_CONTRACT.md) grant one shared canonical Challenge target entitlement across reviewed rank contexts; no target repurchase per rank. The first paid Lesser entry includes one reviewed versioned bundle and explicit Lesser enable, atomically; later ranks grant ownership only unless separately opted in at a legal boundary. Target/access/rank ownership remain distinct. Historical v1 six starters/quotes are preserved and not a Lesser-ready bundle. V2 exact bundle/prices/quotes are unfinished; missing payload cannot be sold or labeled free. No normal roster change or blanket legacy four-rank grant.
