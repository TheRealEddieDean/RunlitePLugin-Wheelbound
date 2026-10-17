# UI/UX
Use polished RuneLite-compatible Swing/overlay style, local skill/boss icons, readable contrast and skip-animation control. S5 explicitly places wheels in centered game-canvas popup; sidebar holds selector, filters/results. Preserve this existing UI convention provisionally for Master/Grand/Punishment wheels; S3 sidebar-wheel wording is conflict C03, not authority to overwrite existing design.

Sidebar hierarchy: run mode/pause, FP/spins, active obligation and progress, Grand Fate checklist/banner, navigation to Tree/Builder/Shop/Bounties/Account. Use text + icon status, never color alone. Unclaimed indicator and search/filter bounty list.

Builder: 24-slot grid, weight and probability per instance, total activity odds, Satchel with capacity, price previews, draft undo/cancel before durable commit.
Tree: pan/zoom; square icons, hover name, click detail, root lit, free tutorial child, gold purchased paths, distinct AND/OR gates. Recovered WB-D095 confirms circular peer skill clusters within the broader progression tree; square icons and meaningful AND/OR remain. Exact unavailable reference artwork is not reconstructed.
Cards: result rises, eligible offerings appear, select then freeze; noneligible options explain dependencies.
Grand Fate: red/black wheel, spikes/skull/chains, seal outcome, multipart checklist.
Defy: preview slot displacement, Taint/Sacrifice probabilities and spin refill together. No extra slots.
Reject: show FP loss/debt, spin loss, reward forfeiture and mandatory punishment. Restart restores exact obligation.

## Card and Pardon correction
Bossing UI shows Standard kill count before card choice; each card previews count/reward, then selected card advances to random Bossing Wheel. Do not show Choose Boss or Reroll. Two starting Standard cards need distinct displayed targets, even with matching type labels; display three after upgrade. Pardon shows fixed price and Banned -> Locked preview, with no remaining-use counter. After restoration explain the separate unlock/gamble action.

## Layout and interaction contract
Use RuneLite sidebar container width with wrapping text. Top strip: mode/Pause, FP with debt text, spins. Next: obligation/quantity/restrictions/progress, Complete/Reject. Grand Fate collapsible checklist stays reachable. Navigation: Tree, Builder, Shop, Bounties, Account; earned-reward notification/history (automatic bounty payout; no Claim button).

Canvas popup shows spins using committed pool. Skip animation reveals same result; Escape closes view without cancelling assignment. Builder detail shows UUID, activity, current/projected weight and activity odds; draft validates five/three/24 together. Defy previews both first-Defy special inserts and stored/destroyed upgrade losses.

Keyboard labels include card type/target/reward/restrictions. Avoid KeyboardFocusManager/window-focus manipulation. Reduced-motion preference. Ban/debt/Taint use text/icons, never color alone. Pending verification includes reason/retry; no sacrifice candidate must not silently waive obligation.

## Confirmed recovery and Taint accounting
Empty forced-Sacrifice recovery must show: "Acquire an eligible item worth at least 10,000 GP to complete your Sacrifice." Show why known items are excluded; unknown bank state requests evidence refresh rather than acquisition. Keep the pending obligation visible and disable new ordinary assignments. Taint confirmation previews a one-spin cost; Punishment completion shows no additional spin cost. Sacrifice confirmation also previews a one-spin cost.

## Original rejection and access-policy implications
Keep event-component text-entry detection; never use KeyboardFocusManager to find the global focus owner. Avoid forcing focus/window state. Existing local popup key consumption is accepted at the old submitted revision, not permission to intercept ordinary gameplay globally.

Warn clearly when policy/coverage prevents guaranteeing an access restriction. WB-D066 requires retaining Trade with and showing a local Fate restriction notice on attempted trading. Proposed consume-and-notice enforcement has current Hub precedent (WB-D067), with review/coverage outstanding. Dismiss does not replay the action. Do not replace established locks or introduce penalties. Do not show successful donation, Grand Fate or unlock based on ambiguous evidence. Policy/source detail: [POLICY_AND_FEASIBILITY](POLICY_AND_FEASIBILITY.md).


## Detailed delegated specification

[UX_RECOVERY_FLOWS](UX_RECOVERY_FLOWS.md) expands this chapter with provisional numeric/UX detail (WB-D083). Explicit user decisions still prevail.

## Recovered onboarding and tree interactions
WB-D092/093 confirms protected Tutorial Island onboarding, Grand-first mainland flow, illuminated root, free introductory child and square icon nodes. Hover shows highlight/name; click opens a Fate-style detail card with cost/benefit and explicit AND/OR requirements. Unlock animation sends gold down the eligible path. Bounty list uses item sprites, name search, rarity/FP sorting and automatic FP-earned feedback/history (WB-D112 supersedes WB-D094 manual-claim indication). Ordinary Skilling target details explain bracket, skill modifier and clean increment (WB-D091). Specific starter FP amount/timing and eventual skill-branch layout remain provisional.

Gold milestone progress shows purchased count/required count separately from owned count/total choices. At five of nine skills, show milestone 5/5 and owned 5/9; later purchases light their nodes without extending an already-complete line. Quest tier purchase itself carries the milestone animation. Boss peers show equal progression value within a tier (WB-D095/097/099).

## Latest user interaction clarifications
Known-empty Questing landing keeps its visible slice, shows a small lower-area notice such as No eligible quests - Fate will spin again, and automatically respins (WB-D111). Do not silently redraw a valid boss/assignment. Bounties automatically pay once on supported qualifying active completion and show earned FP feedback; no manual Claim button. Pause disables new payout/claim processing while preserving existing FP/evidence (WB-D112/114). No active reward is erased because its assignment fails or is rejected.

WB-D116/117: label Bounties Shop as item goal→FP. Master CA Grand checklist reads Obtain Ghommal's hilt 5; do not show an active-only CA-point quota as an additional requirement. Other Grand entry/lifecycle rules remain. Separately retained CA/diary reward UI must not be conflated with this trophy goal.

WB-D121: Questing offers Store/Reactivate, not Destroy. Explain storage cost/room and final active minimum. During full-wheel special displacement, Questing is selectable only for legal storage; never suggest destroying it or grant extra capacity. Other legal ordinary choices remain player-selected.

WB-D122: Bossing Challenge card indicates an item-drop-only unique hunt (WB-D128), with exact source/item and authored reward when determined. Do not label it merely Greater KC or permit target choice/rerolls. WB-D135 fixes post-selection reveal; preselection offer shows rules/pool odds/reviewed reward range, not an invented final target.

WB-D123: show Challenge draw as its eligible boss/creature target pool, separate from normal Bossing outcomes. Display actual committed eligible odds when specified; never offer player target selection/rerolls or suggest card purchase silently unlocked every creature. WB-D135/136 now specify timing and true creature/item assignment odds.

WB-D124: render explicit Bossing Lesser AND Greater prerequisite connections to Challenge; show included starter target set, then downstream creature unlock branch. Owned versus currently eligible targets must be distinguishable. WB-D125 rare-drop targets describe the actual unique/source, not a generic rare-table goal. No undisclosed minimum-target gate or new target prices.

WB-D126: owned Challenge remains visible as unlocked; when below3 eligible distinct bosses/creatures, explain it is unavailable in new card offerings and show current eligible target count. No punishment, ownership loss/refund or forced paused status. Three targets is not a third-card upgrade. Included starter bundle remains displayed as part of purchase.

WB-D128: no CA family or family selector/draw; explain random target/item hunt and reveal committed objective without reroll. WB-D135/136 resolve reveal sequencing and uniform creature/item assignment odds; implementation remains unauthorized.

WB-D129: tell player Already-owned items do not count - obtain a fresh drop during this assignment. Do not offer turn-in/withdrawal or instant completion from inventory/collection log. Keep eligible target display even if item is owned. Show supported fresh acquisition/completion once; no duplicated FP feedback on replay.

WB-D130/131: show the exact target item/icon/name, assigned source and authored reward; other uniques do not count. Catalog rarity eligibility1/400–1/1500 is not a guarantee of a drop within1500 kills. Do not show pity completion/alternate-item claim; retain Pause/Reject under existing rules. Display rates only when reviewed with actual context.

WB-D133/134: use reviewed personal receiving probability for one specific item per authored encounter/loot opportunity. Freeze source/mode/context IDs, assumptions, formula/source revision and exact probability or fully in-band range. Unknown allocation/context/range is unavailable for new offers. Do not assume group chance/team size. Preserve committed hunt/item/reward on later context failure; explain unavailable qualification and retain existing Pause/Reject. Only supported fresh active personal assigned-source receipts qualify; variants do not multiply target diversity. Concrete source formulas/live proof remain gated.

WB-D135–137: follow [Challenge assignment contract](CHALLENGE_ASSIGNMENT_CONTRACT.md). Freeze eligible canonical pool/context/reward schedule and offer randomness before display; card selection precedes durable uniform creature-then-item result and exact reward reveal. Variants/contexts are not extra lottery entries. Challenge uses fresh-drop completion, no KC quota or item consumption. Receipt/restart/odds cases are planned NOT_RUN; actual catalog/quotes/source adapters remain gated.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.

WB-D140/141: prioritize repeatable task-independent starter contexts; hold finite-task-dependent hunts until supported continuation/rate/access review. Judge meaningful unique function without a new GE-price minimum; generic loot remains excluded. [A48 batch1 candidate review](research/CHALLENGE_CANDIDATE_REVIEW_B1.md) has seven provisional hunts, source freshness/access limitations and runtime-disabled records. Exact starter membership/size/tiers/prices/rewards remain unapproved; source excerpts do not enable eligibility. Planned acceptance NOT_RUN: task exhaustion not waived, price change alone not a target reroll, supported source/context required.

## Current Challenge unlock topology — WB-D147

[CHALLENGE_CARD_TIERS](CHALLENGE_CARD_TIERS.md) now confirms paid permanent Lesser→Standard→Greater→Superior Challenge rank unlocks, retaining lower ranks. Ordinary Bossing Lesser AND Greater is the first Lesser Challenge entry gate; no additional generic entry charge/free Lesser. This supersedes earlier monolithic-card/prerequisite ambiguity only. Preserve starter grant/target expansion, normal L/S/G and Skilling separation. Exact v2 content/prices, controls and offer integration remain follow-on; no implementation authorized.

## Current Challenge rank control — WB-D148

[Rank controls](CHALLENGE_RANK_CONTROL_CONTRACT.md) separate owned/enabled/ready. First Lesser confirmation enables Lesser; later purchases preserve settings and start disabled unless explicitly opted in. Owned-rank settings may change only with no assignment/obligation/pending offers/unresolved selection. All off is allowed and suppresses only new Challenge offerings. Pending/paused/restored work stays immutable; no target toggles or reroll. This supersedes older selector-proposed and automatic-pool-entry wording for Challenge ranks only.

## Current ranked Challenge offering — WB-D149

[Rank offering contract](CHALLENGE_RANK_OFFER_CONTRACT.md) preserves two/three total slots and one Challenge family. Each Challenge offer uniformly commits a ready enabled rank before display; player selects the visible rank card, then uniform canonical creature/item within it. Require≥3 actually eligible canonical keys **within that rank**, supported context and reviewed v2 quote. Extra ranks divide the existing family share; no fourfold family boost, extra slots, rank picker or forced distinct rank offers. Numeric family weights remain BALANCE_TBD. Frozen offers/settings/pools do not refresh on restore. This supersedes older rank/floor/offer-integration proposals only; production is disabled.

## Current selected Grand Fate style — WB-D156/157

[GF4 reference and RuneLite image constraints](design/GRAND_FATE_GF4.md) is the user-selected current Grand Fate baseline: restrained OSRS-like red slices, iron chain ring, perimeter skulls and spikes. No production asset or other wheel approved. Generated tainted ordinary activity wedges are not gameplay authority: Taint is its own special slice.
