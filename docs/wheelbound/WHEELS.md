# Wheels and Satchel
Maximum 24 active slices including Taint and Sacrifice. At least five ordinary slices across three distinct activity keys. Duplicates count toward five, but not additional distinct activities. Combat bundle is one activity.

Start: Mining, Fishing, Woodcutting, Combat, Questing. Original instances have no permanent protection. Each slice has UUID, activity, active/storage state and individual weight/modifiers.
Probability(slice)=weight/sum(active weights); activity probability sums its copies. Empty slots have no weight. Free reordering never changes probabilities.

Ordinary weight ladder: 1 -> 1.25 -> 1.5 -> 2 -> 3. Sequential enhancement prices depend only on destination tier, never purchase history or activity. Duplicate prices escalate by lifetime purchases per activity; destruction does not reset the counter. First acquisition versus reacquisition counter semantics are OPEN.

Satchel: 3 -> 6 -> 10 -> 15 capacities. Deactivation costs FP and preserves upgrades; reactivation costs a small FP amount; destruction is free, permanent and refunds nothing. Activity unlock survives destruction. Full Satchel rejects storage, not free destruction.

All edits validate the final configuration atomically. Full-wheel Taint requires player-selected ordinary displacement, store with room/FP or destroy. First Defy adds two specials: if 23/24 occupied, one/two displacements are required respectively. Do not grant extra slots. With only five ordinary slices there is spare capacity, so normal special insertion does not require invalid removal. Every displacement preserves five/three.

Special slices cannot be stored or ordinarily destroyed. Taint only removed by Cleansing; Sacrifice permanent. See [DEFY_FATE](DEFY_FATE.md), [ECONOMY](ECONOMY.md).
