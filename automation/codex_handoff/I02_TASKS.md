# Wheel editing and pricing Codex packet

> Current authority2026-10-12: WB-D111–115 supersede prior Questing suspension/retry prohibition, Q10/manual bounty claiming and PAUSE-CLAIM uncertainty. Known-empty Questing automatically retries with retained slice; supported active bounty automatically pays once and survives failure/Reject; paused payout forbidden. Old fixture dispositions/pins remain prior-contract evidence, not current acceptance of superseded behavior. CA-MIXED and extra Bossing Challenge remain unanswered. Read current DECISIONS before coding.

WB-A31,2026-10-12. Future implementation planning only; no coding authorization, adopted prices or shipped transaction. Use after I01's actual state/receipt/store boundary exists and implementation is explicitly authorized. Canonical rules are docs/wheelbound/WHEELS, FATE_SHOP, DEFY_FATE, ECONOMY and DECISIONS. Do not replace them with these planning summaries.

## Dependency-ordered assignments

| Task | Depends on | Proposed modules | Size | Reviewable result |
| --- | --- | --- | --- | --- |
| I02-C0 | I01 acceptance; current catalogs | Versioned shop quote and edit command contract | S | Quote payloads, invariant validation and unresolved price/counter boundaries explicit |
| I02-C1 | C0 | Slice/Satchel identity and edit proposal | S | Same-instance moves preserve modifiers; active/stored sets disjoint |
| I02-C2 | C1 | Store, reactivate, destroy, reorder transactions | S | Prices/state commit together; destructive/invalid/full-capacity cases tested |
| I02-C3 | C0/C1 | Per-activity duplicate ledger | S | Frozen quote, successful purchase advances correct lifetime count once |
| I02-C4 | C0/C1 | Per-slice enhancement transaction | S | Sequential destination-tier price and ordinary weights; no lifetime-price coupling |
| I02-C5 | C1/C2 | Planned special insertion/displacement | S | Player-selected storage/destruction plan validates final wheel before any commit |
| I02-C6 | C2–C5 | Probability preview and retry integration | S | Preview equals committed odds; lost acknowledgements cannot duplicate edits |
| I02-C7 | C2–C6 | Actual transaction acceptance evidence | S | State/FP/counter conservation, invalid-plan rollback and actual reports |

C3 and C4 can be developed independently once C0/C1 are stable. C5 tests special insertion plans without implementing full Defy, refill, Blessings, Punishment or live sacrifice; those belong to I07. Calling a pure plan validator is not permission to publish partially implemented Defy behavior.

## Contract requirements

Each edit uses account/run/generation/revision/command identity and frozen quote/catalog version. Review the complete proposed state and ledger delta before durable commit. UI reads immutable previews and emits intents; it never debits independently. Reject stale quotes with the documented contract or honor a valid frozen quote as documented, rather than silently reprice after confirmation; choose the implementation-neutral quote validity policy in C0. Do not alter a committed price during replay.

Activity unlocks and purchased Bossing pool membership survive slice destruction. Do not reuse a slice UUID after destruction or substitute a newly bought slice for a stored instance. Satchel accepts ordinary slices only. Stored enhancements/modifiers persist with their original instance. Ordinary weights1/1.25/1.5/2/3 follow enhancement tier; specials have separate Defy-derived weights. No protected starting slice instance or general slice-protection upgrade exists.

Capacity ladder3/6/10/15 is documented delegated design; exact prices remain balance candidates. Deactivation costs FP, reactivation costs FP, destruction is free/no refund, and rearrangement is free with no odds change. Discretionary costs cannot create debt. Full storage prevents storing another slice, not legal free destruction. The final wheel must retain≥5 ordinary across≥3 activities and≤24 total, counting every special. Stored slices never satisfy active minimums.

Duplicate prices depend on the activity's lifetime duplicate purchases. Enhancement prices depend only on destination tier. Neither destruction nor storage resets duplicate history. WB-D118 resolves classification: first-ever instance establishes baseline, including starting grants. First paid acquisition of a never-owned activity uses base price; all later purchased new instances use the next paid duplicate tier even after destruction. Same-instance Satchel return does not increment. Port these as actual Java transaction tests; numeric prices remain versioned candidates.

## Required acceptance examples

These are future Java tests, not executed fixtures. Use symbolic/versioned placeholder costs; never label test amounts approved balance.

| ID | Scenario | Required result |
| --- | --- | --- |
| WE01 | Store an enhanced ordinary when resulting active wheel remains5/3 and storage has room | Same UUID/modifiers move; quoted cost once; preview matches result |
| WE02 | Store would leave four ordinary | No edit/debit/new receipt |
| WE03 | Destroy sole instance of third represented activity | Refused if resulting wheel has only two activities; no refund/counter change |
| WE04 | Satchel full; legal destruction requested instead | Storage fails; independent legal destruction succeeds free |
| WE05 | Reactivate stored slice with24 active | Refused without a separately valid compound plan; no hidden25th slot |
| WE06 | Move same enhanced slice out and back | Tier/weight/identity retained; frozen move quotes charged once each |
| WE07 | Buy duplicate of activity A | Only A's classified lifetime duplicate counter advances once; B untouched |
| WE08 | Destroy duplicate then purchase another of A | History retained; no cheaper quote from destruction |
| WE09 | Enhance different slices to same destination tier | Same versioned tier price regardless of activity or prior enhancement purchases |
| WE10 | Attempt skipped tier, enhance special or enhance beyond tier ladder | Refused without partial debit or weight change |
| WE11 | Reorder active slice display | Same instance set, total weights and probabilities; zero FP change |
| WE12 | First Defy insertion plan at23 ordinary active | One player-selected displacement makes room for two specials; final state remains valid |
| WE13 | First Defy insertion plan at24 ordinary active | Two selected ordinary displacements; no extra special capacity |
| WE14 | Displacement selects sole instance of third activity | Refused unless the entire resulting atomic plan restores approved diversity without a waiver |
| WE15 | Store selected displacement without space or FP | Plan not committed; offer available legal alternatives without automatically choosing destruction |
| WE16 | Cancel insertion/displacement before commit | No new special, edit, cost, refill or history change |
| WE17 | Persist edit then lose UI acknowledgement | Same slice set, FP delta and counters replay; no duplicate purchase/enhancement |
| WE18 | Storage fails before durable commit | No acknowledged edit; retain/reconcile original according to I01 store contract |
| WE19 | Activity slice destroyed while bosses already purchased | Mode boss unlock pool retained; normal-wheel suggestions do not become artificial mode gates |
| WE20 | Exhausted or unknown ordinary assignment pool | Known-empty Questing retains slice and retries under WB-D111; unknown evidence/other activity/extra spin accounting stay gated |

For WE12/WE13 use an explicit valid fixture configuration with at least three activity keys. The earlier five ordinary minimum is not a requirement to preserve all initial instances. Two special arrivals consume two real slots. All chosen displacement instances must be distinct, ordinary and owned/active. A candidate storage/destruction selection is a preview until the complete mutation commits; retry must restore the same plan.

C5 consumes an explicit special insertion request generated by a later system. It does not invent Taint starting weight, Defy refill size, Cleansing price or transition timing. WE16's absence of refill/history change means cancelled full Defy must be unchanged when later integrated, not that I02 already owns Defy implementation.

## Probability and conservation review

For every accepted edit, recompute slice odds from the exact committed weights and activity odds by summing copies. Empty capacity has no probability. A reorder changes neither denominator nor odds. A duplicate/enhancement/storage/destruction can change all retained odds; the UI must show that actual result, including special probabilities. Do not show activity-only or normalized ordinary odds as whole-wheel odds.

Compare before/after state, FP receipt, instance ownership and affected counters together. A retry with the same committed command adds nothing; a stale/foreign command adds nothing. A failed validation cannot consume a UUID from durable ownership, increment a lifetime counter or leave an orphan storage entry. Assertions should target these conserved outcomes rather than mirror internal method calls.

## Exit and later milestones

Record actual tests/commit/toolchain and review failures separately from this20-case plan. Preserve old normal-wheel behavior. No adapter, live donation, policy control or new price is enabled here. I03 card/assignment work follows its own catalog/Q6/Q9 gates; I07 later composes these edit transactions into full Defy atomically. User-selected displacement and existing wheel rules must survive that integration.
