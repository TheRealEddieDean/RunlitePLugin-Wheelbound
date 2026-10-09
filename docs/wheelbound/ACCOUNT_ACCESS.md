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

## Attempt warning clarification and technical evidence
The user explicitly wants Trade with retained and a local Fate restriction popup on attempted trading (WB-D066). Do not remove or reorder the option. A warning alone allows the action; consuming the observed menu event prevents vanilla processing of that attempt. Current Hub-pinned Bronzeman Unleashed implements connected consume-and-message interception for outgoing Trade with and incoming chat Accept trade (WB-D067). This provides concrete precedent; it supersedes the earlier absence-of-precedent assessment, not the need for feature-specific Wheelbound review or full coverage tests.

Proposed path: preserve menu entries, evaluate active-run restriction at the observed click, consume the restricted attempt, then show a dismissible local notice. Dismiss must not replay the action or unlock anything. Locked/Banned vendor wording differs; player trading unlock policy remains OPEN. Pause disables interception immediately. No new FP penalty or advisory replacement for established locks is approved. Existing-open trade windows, final Accept and alternate request inputs remain coverage dependencies. See [POLICY_AND_FEASIBILITY](POLICY_AND_FEASIBILITY.md) for exact source pins, Coffer evidence and validation requirements.
