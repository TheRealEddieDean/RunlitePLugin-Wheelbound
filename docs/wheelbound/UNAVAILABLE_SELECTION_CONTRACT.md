# Unavailable activity selection and recovery

Authority: [DECISIONS](DECISIONS.md), WB-D111/120/121 and delegated WB-D138/139. Documentation only; no current-client evidence or implementation claimed. This covers selection before a valid ordinary offering/result is committed. Existing committed Fates and mandatory obligations are preserved.

## Classify readiness without altering the wheel

| Outcome state | Evidence | Master selection behavior |
| --- | --- | --- |
| ASSIGNABLE ordinary | At least one supported accessible valid assignment with reviewed targets/rewards | Draw using full configured weights; commit normal offers/result |
| VERIFIED_EMPTY ordinary | Complete current activity eligibility is known, but there are no valid assignments | Keep slice/weight; if it lands, explain and auto-retry at no extra cost |
| UNKNOWN/UNSUPPORTED | Missing initialization/access/catalog/adapter support, incomplete evidence | Wait before a new selection; do not silently remove its weight |
| Supported Taint/Sacrifice path | Existing mandatory selection and recovery requirements can be represented | Draw normally; preserve obligation even if its next action needs recovery |
| Unsupported special path | No supported representation/recovery contract | Readiness blocker; never treat as a harmless empty ordinary landing |

An unavailable Bossing Challenge card alone does not make the entire Bossing activity empty. Check normal L/S/G too. One failed template is not a complete-pool proof. Existing item ownership does not remove fresh-drop hunts. Unknown bank/access/client evidence is not emptiness. Level99 alone is not XP exhaustion. These classifications require real reviewed eligibility predicates before runtime enablement.

## New selection

Preserve full displayed wheel odds: per-slice weight divided by total positive active weight. No automatic suspension/storage/destruction/normalization. Check readiness for every positive-weight outcome so an unknown slice is not invisibly skipped. If every outcome is known-empty ordinary, display no valid Fate and reasons before drawing. Keep FP/spins/ownership/run state; no empty-pool loss, Punishment or automatic Defy. Show existing legal edits/unlocks and free Pause; Grand attempt remains subject only to its existing entry conditions. All ordinary minimum/diversity/capacity/prices and Questing no-destruction rules stay intact.

With at least one assignable outcome and no unknown/unsupported outcome, draw normally. A known-empty ordinary landing has zero additional allocation, zero reward and zero Punishment, followed by an automatic Master retry. Only eventual valid ordinary/special outcome incurs its existing once-only allocation. Do not reroll a valid boss/item/card offer because it is undesirable. No item/XP credit is earned from the failed selection.

## Exact conditional odds

For a fixed fully known snapshot, let E be total verified-empty ordinary weight, A total assignable weight and W=A+E. When A>0, P(next raw draw is empty)=E/W; probability of eventually accepting assignable slice j is w_j/A. The expected number of raw draws until acceptance is W/A. These accepted-outcome probabilities differ from the configured raw wheel probabilities and must be labeled separately. Special outcomes belong to A when they have supported obligation/recovery paths; their weights cannot be skipped just because a demand is inconvenient.

Hypothetical equal-weight wheel: five ordinary slices, four empty and one assignable, no specials. Raw chance for each remains1/5; final accepted activity is the sole assignable activity, with an expected five raw draws. This is a mathematical example, not an approved roster or measured session duration. No finite number of retries guarantees a result.

## In-flight recovery and persistence

Persist transaction ID, frozen eligibility/catalog versions, random seed/cursor, each empty landing/reason and any valid committed offers/result. Resume the same unresolved selection after crash/repeated callbacks; do not regenerate a stream for a better landing. If evidence becomes unknown or readiness changes before commitment, suspend selection and preserve its history. Revalidate and explicitly reconcile a changed snapshot; do not silently recalculate and erase earlier attempts. The player may Pause immediately without a penalty; no paused reward/backfill.

Animation can yield/resume without consuming a new spin or drawing a replacement result. No arbitrary retry limit becomes a gameplay loss/pity completion. A special landing enters its mandatory path under existing rules, not this retry loop. Once valid offers/result are committed, broader postcommit invalidation recovery remains open; preserve them and existing Pause/Reject.

## Future acceptance — NOT_RUN

| Case | Required result |
| --- | --- |
| All ordinary known empty; no special | Stop before RNG/debit, no failure/reward, preserve wheel |
| One unknown positive-weight activity | Wait; no secretly normalized draw |
| Empty Questing or another empty ordinary, then valid activity | One eventual allocation; empty attempts zero rewards/Punishment |
| Challenge below3 but normal Bossing valid | Bossing stays assignable |
| Empty ordinary plus supported Sacrifice with no eligible owned item | Special remains selectable; existing acquisition/donation obligation persists |
| Missing special adapter/catalog support | Readiness blocked, no special skipping |
| Repeated callbacks/restart after empty landing | Same transaction/stream/history; no repeated debit |
| Readiness changes mid-selection | Suspend/reconcile existing selection, no silent reseed |
| Valid offers/result later unavailable | Preserve; no automatic redraw; postcommit policy still separate |
| Recover with Questing edit/full Satchel | Normal storage/price rules; cannot destroy Questing |
| Zero spins and empty pools | No spin refill or bypass of existing Defy/Grand gates |

Source/access/client proof, Java persistence and runtime tests remain future gated work. Historical simulations/oracles are not rewritten by these new policy decisions.
