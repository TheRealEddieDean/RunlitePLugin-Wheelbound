# UI/UX
Use polished RuneLite-compatible Swing/overlay style, local skill/boss icons, readable contrast and skip-animation control. S5 explicitly places wheels in centered game-canvas popup; sidebar holds selector, filters/results. Preserve this existing UI convention provisionally for Master/Grand/Punishment wheels; S3 sidebar-wheel wording is conflict C03, not authority to overwrite existing design.

Sidebar hierarchy: run mode/pause, FP/spins, active obligation and progress, Grand Fate checklist/banner, navigation to Tree/Builder/Shop/Bounties/Account. Use text + icon status, never color alone. Unclaimed indicator and search/filter bounty list.

Builder: 24-slot grid, weight and probability per instance, total activity odds, Satchel with capacity, price previews, draft undo/cancel before durable commit.
Tree: pan/zoom; square icons, hover name, click detail, root lit, free tutorial child, gold purchased paths, distinct AND/OR gates. Earlier skill ring vs PDF top-down tree layout is UNVERIFIED; do not claim latest approval.
Cards: result rises, eligible offerings appear, select then freeze; noneligible options explain dependencies.
Grand Fate: red/black wheel, spikes/skull/chains, seal outcome, multipart checklist.
Defy: preview slot displacement, Taint/Sacrifice probabilities and spin refill together. No extra slots.
Reject: show FP loss/debt, spin loss, reward forfeiture and mandatory punishment. Restart restores exact obligation.

## Card and Pardon correction
Bossing UI shows Standard kill count before card choice; each card previews count/reward, then selected card advances to random Bossing Wheel. Do not show Choose Boss or Reroll. Two starting Standard cards need distinct displayed targets, even with matching type labels; display three after upgrade. Pardon shows fixed price and Banned -> Locked preview, with no remaining-use counter. After restoration explain the separate unlock/gamble action.

## Layout and interaction contract
Use RuneLite sidebar container width with wrapping text. Top strip: mode/Pause, FP with debt text, spins. Next: obligation/quantity/restrictions/progress, Complete/Reject. Grand Fate collapsible checklist stays reachable. Navigation: Tree, Builder, Shop, Bounties, Account; unclaimed text badge.

Canvas popup shows spins using committed pool. Skip animation reveals same result; Escape closes view without cancelling assignment. Builder detail shows UUID, activity, current/projected weight and activity odds; draft validates five/three/24 together. Defy previews both first-Defy special inserts and stored/destroyed upgrade losses.

Keyboard labels include card type/target/reward/restrictions. Avoid KeyboardFocusManager/window-focus manipulation. Reduced-motion preference. Ban/debt/Taint use text/icons, never color alone. Pending verification includes reason/retry; no sacrifice candidate must not silently waive obligation.

## Confirmed recovery and Taint accounting
Empty forced-Sacrifice recovery must show: "Acquire an eligible item worth at least 10,000 GP to complete your Sacrifice." Show why known items are excluded; unknown bank state requests evidence refresh rather than acquisition. Keep the pending obligation visible and disable new ordinary assignments. Taint confirmation previews a one-spin cost; Punishment completion shows no additional spin cost. Sacrifice confirmation also previews a one-spin cost.
