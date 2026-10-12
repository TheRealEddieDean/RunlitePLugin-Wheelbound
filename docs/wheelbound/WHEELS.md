# Wheels and Satchel
Maximum 24 active slices including Taint and Sacrifice. At least five ordinary slices across three distinct activity keys. Duplicates count toward five, but not additional distinct activities. Combat bundle is one activity.

Start: Mining, Fishing, Woodcutting, Combat, Questing. WB-D121: Questing instances are indestructible but may be stored; other original ordinary instances have no permanent protection. Each slice has UUID, activity, active/storage state and individual weight/modifiers.
Probability(slice)=weight/sum(active weights); activity probability sums its copies. Empty slots have no weight. Free reordering never changes probabilities.

Ordinary weight ladder: 1 -> 1.25 -> 1.5 -> 2 -> 3. Sequential enhancement prices depend only on destination tier, never purchase history or activity. Duplicate prices escalate by lifetime purchases per activity; destruction does not reset the counter. WB-D118 resolves first/reacquisition: first-ever instance establishes ownership; starting grants count as that first instance; every later new purchased instance follows the lifetime duplicate ladder even after all copies are destroyed. Satchel return does not increment it.

Satchel: 3 -> 6 -> 10 -> 15 capacities. Deactivation costs FP and preserves upgrades; reactivation costs a small FP amount; destruction of ordinary non-Questing instances is free, permanent and refunds nothing. Questing cannot be destroyed (WB-D121). Activity unlock survives destruction. Full Satchel rejects storage, not free destruction.

All edits validate the final configuration atomically. Full-wheel Taint requires player-selected ordinary displacement, store with room/FP or destroy an eligible non-Questing ordinary. Questing displacement requires legal storage; it has no destruction fallback. First Defy adds two specials: if 23/24 occupied, one/two displacements are required respectively. Do not grant extra slots. With only five ordinary slices there is spare capacity, so normal special insertion does not require invalid removal. Every displacement preserves five/three.

Special slices cannot be stored or ordinarily destroyed. Taint only removed by Cleansing; Sacrifice permanent. See [DEFY_FATE](DEFY_FATE.md), [ECONOMY](ECONOMY.md).

## Confirmed empty-Questing retry — WB-D111
Questing remains on the wheel when its eligible pool is verifiably empty. Spin normally; if Questing lands, show a brief no-eligible-quest notice and automatically respin the Master Wheel. Keep slice IDs, weights and enhancements unchanged. Raw draw odds include Questing; final accepted outcomes are conditioned by retries, so do not falsely present raw odds as accepted-assignment odds. Unknown data is not empty. This is not an ordinary boss reroll or automatic wheel edit. WB-D120: known-empty Questing attempts have no extra spin cost/reward/Punishment; eventual valid Fate consumes one spin. All-unavailable recovery and extension to other activities remain unverified. Preserve retry provenance/result across restart. WB-D121 resolves voluntary edits: Questing may be stored/removed, but not destroyed.

## Lifetime duplicate accounting — WB-D118
Track the activity baseline separately from its subsequent paid duplicate purchases (field names delegated). Granted starting Mining establishes baseline with zero paid duplicates; the next new Mining purchase uses duplicate tier1. A never-owned newly unlocked activity uses its first-slice price once, then duplicate tier1 for the next new instance. Destruction of all copies does not restore first-slice pricing. Committed successful purchases advance once; cancel/fail/replay/storage/reactivation do not. No balance amounts or enhancement price changes.

## Questing action exception — WB-D121
Questing is an ordinary active slice for5/3/24 counting, and can leave the wheel for the normal paid Satchel route. It remains the same preserved instance and may return. Destruction is always unavailable for Questing, including special-insertion displacement. Full/unaffordable storage does not waive this rule or create extra capacity. Other legal ordinary displacement choices remain available; player selects.
