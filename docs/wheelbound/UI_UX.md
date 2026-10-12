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

WB-D122: Bossing Challenge card indicates an item-drop-only unique hunt (WB-D128), with exact source/item and authored reward when determined. Do not label it merely Greater KC or permit target choice/rerolls. Objective-generation order is pending; no misleading fully specified offer before target data exists.

WB-D123: show Challenge draw as its eligible boss/creature target pool, separate from normal Bossing outcomes. Display actual committed eligible odds when specified; never offer player target selection/rerolls or suggest card purchase silently unlocked every creature. Exact offer/objective reveal timing awaits specification.

WB-D124: render explicit Bossing Lesser AND Greater prerequisite connections to Challenge; show included starter target set, then downstream creature unlock branch. Owned versus currently eligible targets must be distinguishable. WB-D125 rare-drop targets describe the actual unique/source, not a generic rare-table goal. No undisclosed minimum-target gate or new target prices.

WB-D126: owned Challenge remains visible as unlocked; when below3 eligible distinct bosses/creatures, explain it is unavailable in new card offerings and show current eligible target count. No punishment, ownership loss/refund or forced paused status. Three targets is not a third-card upgrade. Included starter bundle remains displayed as part of purchase.

WB-D128: no CA family or family selector/draw; explain random target/item hunt and reveal committed objective without reroll. Exact reveal sequencing/weights remain technical specification.

WB-D129: tell player Already-owned items do not count - obtain a fresh drop during this assignment. Do not offer turn-in/withdrawal or instant completion from inventory/collection log. Keep eligible target display even if item is owned. Show supported fresh acquisition/completion once; no duplicated FP feedback on replay.
