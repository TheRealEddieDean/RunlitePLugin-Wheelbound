# Bossing Challenge assignment contract

Authority: [DECISIONS](DECISIONS.md), WB-D123–126/128–137. WB-D132 delegates current recommendations; WB-D133–137 were adopted under delegation and then explicitly approved2026-10-13 after the in-game explanation; they are not recovered historical approvals. Documentation only: no enabled catalog, current-client detector, production implementation or newly executed gameplay test.

## Eligibility and data

Challenge ownership requires Bossing Lesser AND Greater and includes the authored starter target bundle. At offer time, retain only owned/unlocked, actually accessible, supported creature/encounter keys with at least one qualifying item. Require at least three distinct keys. Ownership and starter grants remain if temporarily below that floor. Normal L/S/G pool stays separate.

Each canonical creature record groups equivalent NPC/source forms. Each logical item record includes explicit canonical IDs/accepted same-item variants, assigned-source identity, supported method/context IDs, personal allocation formula and source revision, exact probability or conservative reviewed range, supported active fresh-drop evidence and reviewed reward quote. Entire probability range must lie within[1/1500,1/400]. Shared rare-table/generic loot, unknown allocation and unsupported/unquoted contexts cannot qualify. Do not invent team-size division or actual rates. One encounter/loot opportunity must be explicitly defined; a multi-stage encounter cannot be presented as several independent kills without review.

Several qualifying contexts for one item are allowed where authored and observable; they do not add item/creature entries. Snapshot supported context IDs/predicates, not a fictitious player's future party contribution. If variable contribution cannot be bounded and observed conservatively, that context remains unavailable. Concrete item/source/formula and starter members are still separate research work.

## Selection and reveal

1. Freeze offer eligibility/catalog/context snapshot, canonical creature/item lists, reward schedule and version, and independent committed randomness for each offered card before showing the complete offers.
2. A Challenge offer explains one random specific-item hunt, eligible creatures and exact assignment odds. Show reviewed possible FP range across that pool rather than an invented fixed target reward. Do not preview one final item and then let the player choose among hidden-target Challenge cards by reopening them. Standard KC reference belongs to L/S/G only.
3. On choosing Challenge, deterministically draw one creature uniformly, then one logical eligible item uniformly on that creature. Persist selected offer/card/creature/item/source/contexts/quote and once-only existing allocation transaction before animation/reveal. A recoverable intent/outcome transaction must survive a crash between selection and reveal; no new random seed on retry.
4. Reveal creature wheel result and exact item/icon, supported source/method contexts, reviewed personal drop odds and fixed FP quote. Skipping/closing animation changes presentation only. No player creature/item picker or reroll; no additional KC completion quota.
5. Qualifying supported active personal fresh-drop receipt settles that assignment once. Keep the item. Independent qualifying Bounties Shop payment remains separate and once-only; earlier ownership never excludes the hunt or completes it.

Normal Bossing sequence, default two/upgraded three offers, type unlocks/offer frequency, ordinary allotted-spin rules, Pause/Reject and no-pity outcome remain unchanged. Collision rules for initial Standard objectives remain separate; multiple Challenge offers may share possible targets and use independent committed seeds, without promising distinct outcomes.

## Transparent assignment odds

If there are C eligible creatures and creature c has K_c eligible logical items, P(creature c)=1/C and P(item i on c)=1/(C*K_c). In a hypothetical three-creature pool with1/2/3 items, each creature has1/3; individual item odds are1/3,1/6 and1/9 respectively. This example asserts no actual OSRS source/drop rate. Sum over each creature's items equals1/3; all item probabilities total1. Additional variants or contexts do not affect odds. Master slice weight upgrades and normal boss weights are not Challenge weighting upgrades.

## Recovery and evidence limits

Restore the same pool, offer randomness, selected item/source/context and quote after restart. A server/catalog/access/context change must not silently swap item/reward or roll another creature. Preserve committed assignment and evidence, report qualification unavailable, and retain existing free Pause/Reject; broader Q6 postcommit recovery remains unresolved. Unknown is never verified-empty. No backfill of paused drops and no reward processing while paused. Delayed earning-versus-delivery evidence needs the existing reviewed receipt contract; this document does not validate it.

## Future acceptance packet

All cases below are planned NOT_RUN gameplay/implementation acceptance, not new Java or live results:

| Case | Required result |
| --- | --- |
|2 versus3 actual creatures | New Challenge type omitted versus eligible, permanent ownership preserved |
|1/2/3 items across3 creatures | Creature odds1/3 each; item odds1/3,1/6,1/9; total1 |
| Multiple same-item/source forms/contexts | No extra lottery entry or diversity count |
| Party1/500 but personal1/2000 (synthetic) | Context excluded |
| Fully in-band versus straddling/unknown range | Eligible subject to other gates versus excluded |
| Unreviewed reward for an otherwise valid item | Item excluded before offers; no fabricated FP |
| Crash before selection / during selected-result commit / after reveal | Same frozen offers or same selected result; no redraw or duplicate allocation |
| Standard KC shown then Challenge selected | Hunt completion uses drop, never reference KC |
| Context later unsupported | Preserve hunt/evidence, explain unavailable, existing Pause/Reject only |
| Teammate/bought/old/paused/unrelated item | No qualifying Challenge receipt |
| Fresh assigned personal item then disposal | Settle once; no consumption or clawback |
| Repeated hunt after paid permanent item bounty | Separate Challenge receipt, no repeated permanent bounty |

Before engineering: reviewed real target catalog/rewards, actual access/rate predicates, durable Java transaction implementation and current-client personal-source trace packet are still required. Existing synthetic experiments remain historical evidence.

## Concrete authored v1 pool and quotes

WB-D142/143 supplies [boss drop table](BOSS_CHALLENGE_HUNT_TABLE.md) and [monster drop table](MONSTER_CHALLENGE_HUNT_TABLE.md) / catalogs/challenge_hunts_v1.json:25 keys/44 exact-item hunts; six starter members. For design selection use only these explicit keys, supported source contexts, canonical dropped IDs and fixed fp_reward/reward_quote_version, filtered by actual owned/access/validated-adapter predicates. Extra variants and additional raw collector rows are not entries. Earlier missing concrete table/quote statements are superseded for v1 only; current-client proof/implementation gates remain. All new quantities are one fresh specific drop; inventory ownership is not an eligibility filter. No gameplay implementation or Q6 retirement approval.

## Current Challenge rarity/card supersession — WB-D144/145

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) controls future Challenge rank placement: Lesser reciprocal25–50, Standard>50–125, Greater>125–750, Superior>750–5000. Ignore displayed roll-count multiplier for tier classification; preserve raw rolls/personal encounter probability separately. This supersedes old single1/400–1/1500/aggregate-rate bracket statements in this chapter for future selection only. Fresh personal exact assigned-source completion, source/context review and existing settled assignments remain protected. WB-D147 confirms progressive Lesser→Standard→Greater→Superior purchases retaining lower ranks; the preserved ordinary Bossing Lesser AND Greater entry gate attaches to the first paid Lesser Challenge. Enabled-rank control/offer integration and expanded v2 roster/rewards remain follow-on work. Do not treat the preserved v1 roster/FP or lower-tier source leads as a ready four-tier pool. No runtime implementation/main change.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.
