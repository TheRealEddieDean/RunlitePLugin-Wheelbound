# Q6 exhausted activity: exact trade-off supplement

WB-A27. This is a new options analysis under the autonomous research assignment, not an adopted design decision. Existing gameplay authority remains `docs/wheelbound/DECISIONS.md`. Q6 is still unresolved. The current gameplay PDF/source remains284799b1; this later automation supplement does not alter that Markdown specification.

Confirmed constraints used: at least five ordinary active slices, three distinct activities,24 total slots including specials; paid storage preserves instances/modifiers; free destruction gives no refund or counter reset; enhancements depend on tier; activity lifetime duplicate counts persist; pause is free; optional purchases cannot create debt. Nothing below waives these constraints.

Run `python3 automation/decision_analysis/q6_analysis.py --verify`. Saved results contain six explicit hypothetical wheels and32,116 count compositions. These are exact arithmetic/topology checks, not simulated accounts, balance trials or live eligibility evidence. Existing130,080 synthetic progression trajectories are unchanged. Special weights of1 in the examples are test premises, not newly adopted balance constants.

## What suspension does to probabilities

Let total active weight be W and unavailable weight be U. If selection excludes that weight and normalizes the remainder, every retained slice changes from w/W to w/(W-U), multiplied by W/(W-U). This applies to Taint and Sacrifice as well as ordinary outcomes. Free retry-until-eligible produces the same conditional distribution; it does not preserve the original odds merely because slice ownership is retained. Such behavior still needs an explicit Q6 rule.

| Hypothetical wheel | Unavailable Questing odds before | Retained-slice odds multiplier | Combined Taint/Sacrifice odds before→after | Minimum new ordinary copies before removing every Questing instance |
| --- | --- | --- | --- | --- |
| Original five different activities, equal ordinary weights |1/5 =20% |5/4 =1.25 |0→0 |1; an already unlocked eligible activity copy can suffice |
| Five ordinary: Mining1, Fishing1, Questing3 |3/5 =60% |5/2 =2.5 |0→0 |3, including at least one new eligible activity type |
| Same five ordinary plus two specials of weight1 |3/7 ≈42.86% |7/4 =1.75 |2/7 ≈28.57%→1/2 =50% |3, including one new eligible type |
| Full24: Mining1, Fishing1, Questing20, two specials of weight1 |5/6 ≈83.33% |6 |1/12 ≈8.33%→1/2 =50% |3, including one new eligible type |
| Original five, Questing enhanced to weight3, others weight1 |3/7 ≈42.86% |7/4 =1.75 |0→0 |1 |
| Full24: Mining20, Fishing1, Questing1, two specials of weight1 |1/24 ≈4.17% |24/23 ≈1.0435 |1/12 ≈8.33%→2/23 ≈8.70% |1 new eligible activity type; a prior capacity edit can also be necessary |

Suspension can therefore increase undesirable special outcomes substantially. It is not uniformly a free benefit or uniformly a harmless display detail. Keeping the original percentages after excluding Questing would be mathematically false.

## Paid replacement: necessary lower bounds, not a guaranteed escape

After deleting/storing all unavailable ordinary instances, let e be the retained ordinary count and d the retained distinct activity count. At least `max(0,5-e,3-d)` new eligible ordinary copies are needed, including at least `max(0,3-d)` new activity types. These lower bounds assume the required activity types are actually unlocked/eligible and can be acquired. They do not estimate FP price, unlock prerequisites, Satchel capacity, or the cost of losing upgraded instances.

The composition enumeration covers three or four ordinary activity types, all represented at least once, ordinary counts5 through24 minus occupied specials, with the last activity unavailable. With exactly three represented types, removing that whole activity always requires a new eligible type. With four types, the distinct-activity minimum survives, but the five-ordinary minimum can still fail. No composition is treated as a real account or a proof of affordability.

At full capacity, a lower bound of one purchase does not mean purchase-first is possible. In the final example, remove/store one Mining duplicate while retaining Mining/Fishing/Questing and at least five ordinary slices; buy the new eligible activity; then remove Questing. Every step can preserve topology. Storing needs its normal fee and space; destruction is free but permanently loses that instance's upgrades without refund. The sequence is illustrative player-selected editing, not an automated replacement policy.

For the Questing-heavy full wheel, enough unavailable duplicates can be removed while retaining five ordinary and three types to create space for three replacement purchases. Then remaining Questing instances can be removed. Storage alone may exceed the maximum Satchel capacity; free destruction is not equivalent to cost-free preservation. A negative-FP player still cannot fund discretionary purchases by merely deleting slices.

## Options awaiting the existing Q6 decision

| Option | What it preserves | Material trade-off or dependency |
| --- | --- | --- |
| Explicit documented suspension with retained ownership | Bought instances survive; avoids assigning nonexistent content | Renormalizes every retained outcome; needs displayed odds, eligibility reasons, restoration timing and clarity whether5/3 is structural or available-selection diversity |
| Player-paid replacement using ordinary storage/destruction rules | Normal edit costs and instance semantics; no automatic odds waiver | Requires affordability/access and valid intermediate topology; can strand a minimum or debt-bound wheel; cannot promise a guaranteed escape |
| Preserve the selected result pending a legitimate availability repair | No silent free redraw or automatic deletion | Permanent exhaustion cannot be repaired by waiting/pausing alone; needs an explicit recovery policy if editing is unaffordable or cannot restore that activity |

This supplement does not select among them or override OPEN_QUESTIONS' existing proposed recommendation. There is no invented skip reward, extra spin loss, automatic Punishment, grandfathered diversity waiver, new sale refund, or warning-only enforcement replacement.

## Availability evidence and implementation boundaries

Distinguish permanent exhaustion, temporary actual game prerequisites, uninitialized/stale account evidence and an unsupported detector/catalog. Unknown is not empty. Unsupported content stays gated; it must not generate a fictional completion receipt. A permanent purchased Bossing unlock cannot be excluded just because a recommendation says the player's stats are low. Legitimate server/content prerequisites remain separate.

Before a draw, show the exact blocked reason and any current legal edit costs without claiming that proximity, equipment possession or an old cache proves eligibility. After an outcome is committed, do not silently generate another outcome during restore. Previewing an edit must compute the ordinary and special odds together and validate five/three/24 for every committed intermediate state. None of these observations authorize a new live adapter or production code.

The question that remains is narrow: **when an owned ordinary activity has no eligible assignment, which explicit recovery/selection policy applies, including when normal paid replacement cannot be afforded?** Resolving that is required before this branch of the state machine can be enabled. Free pause and the established wheel rules remain confirmed.
