# Detailed player journeys and recovery UX

Status: DELEGATED UI specification, preserving current approved rules. No production UI implemented.

## Sidebar organization
Keep the existing normal-wheel selector and appearance. Optional Wheelbound status shows lifecycle, FP (including negative), remaining spins and current obligation. Main navigation: Fate, Tree, Fate Shop, Account Access, Satchel, Bounties, Audit. Wheels remain the centered game-canvas popup consistent with existing release design; sidebar holds summaries, controls and navigation. Tree can open a larger pan/zoom surface. Do not change normal-wheel behavior.

Always display actual odds based on all active weights, including Taint/Sacrifice. Before a wheel edit, show old/new probability, price and affected minimum/diversity invariant. Rearrangement has no price or odds effect. Keyboard shortcuts work only when an eligible local control owns focus; no global focus hooks or generated game actions.

## Fate lifecycle
| State | Primary content/action | Recovery and constraints |
| --- | --- | --- |
| Not Started | Explain self-imposed local observation and sealed five-way Grand Fate | New-account scope confirmed; validate fresh-account onboarding without Grand level/playtime gates |
| Tutorial Island | Onboard and seal Grand Fate when appropriate | No ordinary mainland task started prematurely |
| Active, no obligation | Spin Master Wheel | Zero spins shows Defy/eligible Grand Fate options, not run failure |
| Combat selected | Choose Attack/Strength/Defence/Ranged/Magic | Choice before target generation; HP by-product displayed |
| Card offers committed | Show type, objective, Standard reference, reward and restriction | Resume same offers after restart; no refresh/reroll button |
| Boss card selected | Spin Boss Wheel | Boss remains random; no choose-boss list or reroll |
| Ordinary assignment active | Progress, permitted by-products, Audit tolerance, Reject | Pause always available; Reject previews FP loss, one spin, lost reward and mandatory Punishment |
| Taint/Reject pending | Show supported Punishment wheel and exact eligible outcome count | Fewer than twelve legitimate candidates allowed; no unsupported fillers |
| Punishment active | Target/restriction/progress/reward | No reject or silent relaxation; pause remains free |
| Sacrifice acquisition | Explain eligible item ≥10k and permitted acquisition route | Pending obligation persists; no automatic shop/GE unlock or new task reward |
| Sacrifice selection | Snapshot candidate, quantity, Blessed exclusions and server quote | Verify detector health/headroom before irreversible player action |
| Donation pending | “Waiting for matching Coffer credit” with evidence status | No award from disappearance; no prompt to donate again after ambiguity |
| Completed | Completion receipt and preserved run history | Existing OSRS progress unchanged; no automatic abandon/delete |

## Pause, account swap and observation failures
Pause is immediate, unrestricted and penalty-free. Explain “Wheelbound progress is paused; OSRS progress continues.” No Assisted flag, guilt warning, penalty or delayed pause transaction. Retain assignment and earned unclaimed receipts; resume snapshots fresh baselines with no backfill. If the proposed GE readiness check is adopted, show pending offer cleanup as a resume prerequisite with no FP/status penalty; never trap assets or cancel offers automatically.

Account change shows a distinct account/run summary and rejects callbacks from the old session. Missing identity, missing initialized account data and an actual empty pool need different messages. “Data unavailable—retry after initialization” must not become “you have no eligible items/quests” or a player violation. Save failure shows a pending operation and retry/recovery guidance; disable new mutations without erasing the original.

## Account Access
Sidebar browse-only: vendor states, fixed Pardon price and category costs. Unlock/Tempt Fate buttons require correlated physical visit and verified vendor/shop identity. Shop inspection may establish identity without permission to buy/sell. Locked copy: “This vendor is Locked. Visit them to unlock or Tempt Fate.” Banned copy: “Fate has banned this vendor. Pardon restores them to Locked.” Pardon confirmation shows Banned→Locked and remaining normal unlock requirement.

Retain Trade with menu. Restricted observed attempt triggers a dismissible local notice; no packet replay, auto-decline or game input. Exact cancellation routes/modal behavior remain policy/coverage-gated. GE/shop messages identify the blocked transaction and rule, rather than falsely claiming universal server prevention.

## Progression and purchases
Tree nodes show name/icon, FP price, allOf/anyOf/countOf prerequisites, actual OSRS access conditions and permanent unlock effect. Layout alone never communicates a gate. Gold unlocked paths and clear disabled explanations. Price previews distinguish underlying activity unlock, add-slice cost, duplicate lifetime price and fixed enhancement tier price.

At 24 total slices, forced Taint displacement lists only ordinary removable candidates preserving five/three; player selects store (normal fee/capacity) or destroy (free/permanent/no refund). Show Satchel expansion when needed, but never require unaffordable storage when free valid destruction exists. If selection would violate invariants, explain it before confirmation.

## Accessibility and error copy
Use RuneLite-native colors and explicit text/state icons so color is not the sole signal. Controls have focus indication and keyboard activation where supported. Target minimum sidebar width and short-height scrolling without losing active status/primary action. Long item/vendor names wrap; balances are readable and never clipped. Destructive actions name the specific slice/run. No user-facing stack traces, enum symbols, varp numbers or internal transaction hashes unless an explicit diagnostic export is requested.

Test loading, empty, unavailable, pending, saved and failed states independently; fixed/resizable clients; rapid repeated clicks; modal dismissal; profile swap during animation; crash between every receipt/animation boundary; screen-reader labels for custom controls where practical. No focus stealing or global KeyboardFocusManager.
