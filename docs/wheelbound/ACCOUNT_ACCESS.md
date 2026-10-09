# Account access
Vendors Locked, Unlocked, Banned. Physically visit NPC to pay FP or Tempt Fate; directory is read-only for remote unlock. Tempt 50/50 unlock/ban, explicitly warned. Fate's Pardon is infinitely purchasable at a very expensive fixed FP price, with no per-run or per-account limit and no price escalation (WB-D051). Each purchase restores one Banned vendor to Locked, never directly Unlocked. Players must then pay the ordinary vendor unlock price or attempt Tempt Fate again. The earlier three-Pardon cap is superseded; C02 is resolved. The numeric fixed price remains BALANCE_TBD.

Classes in S3: General; Rune/Magic; Food/Drink; Weapons/Armour/Tools; Other. Price escalation within class, not enhancements. Exact price counters and whether gamble advances count OPEN.

GE: one-time large FP plus completed-Fate gate, no gamble/ban/escalation. Vendor quests do not bypass restrictions. Player trading block intent recovered, exact unlock policy OPEN.

Hard menu blocking is intent, not verified full enforcement. Client cannot enforce server restrictions. Document each covered interaction and limitation after research; locked/banned messages distinct. Pause disables all restrictions immediately.

## Proposed transaction specification
Normal unlock requires confirmed nearby vendor identity, Locked state, current class-price quote and sufficient FP. Commit debit, Unlocked state and paid-class-unlock counter in one receipt. Display the result before allowing covered trade menus.

Tempt Fate requires the same physical visit and Locked state. Persist 50/50 result before animation, then transition to Unlocked or Banned. Proposed accounting: only paid vendor unlocks advance class price count; gambling does not. This is NEW PROPOSED detail, not an earlier approval. A Banned vendor cannot be gambled or normally purchased until restored.

Pardon requires Banned state and affordable fixed price. Receipt restores Locked without changing paid-class counts. Pardon access itself need not imply remote vendor unlocking; whether restoration must also happen beside the vendor is unverified. UI may prepare the purchase remotely, but keep actual unlock/Tempt strictly in-world.

Detect vendor with stable NPC/shop identity, region/instance and shop widget association; NPC display name alone is insufficient where duplicated. Unknown vendor remains unavailable-data until catalog resolved, not silently Unlocked. Warn about quest-essential vendors before committing an assignment. No free vendor exemption is introduced.

## Policy constraint discovered 2026-10-09
The gameplay intent above remains recorded. Jagex explicitly prohibits removing/reordering player-based menu options, including Trade with. An alternate cancellation mechanism is not established as compliant. NPC-shop/GE interception must receive feature-specific review and live coverage checks; a consume method alone is insufficient.

Proposed policy-compatible fallback: keep local Locked/Unlocked/Banned state, physical vendor unlock and visible restriction warnings with a verified activity log. This is not an approved replacement for hard blocking. No new trading/shop FP penalty is introduced. Preserve this unresolved design dependency rather than promise impossible or unapproved enforcement. See [POLICY_AND_FEASIBILITY](POLICY_AND_FEASIBILITY.md).

