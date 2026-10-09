# Testing acceptance
No plugin tests were run or code changed for recovery checkpoint.

Required invariants: five ordinary/three activities/24 including specials; weights normalized; fixed enhancements independent of lifetime counts; duplicate counters survive destruction; store retains UUID; Defy odd schedule and post-cleansing refill; first Defy two inserts; negative FP recovery; Reject consumes one and no reward.

Transaction crash tests before/after journal and snapshot writes, replay completion/claim/donation, account switch and stale callbacks. Pause must exclude XP/drop/quest/CA/bounty progress and remove restrictions. Grand Fate attempts no spin debit, preserve checklist, no readiness gate.

Live tests: current quest/CA mappings, all covered shop/GE/trade inputs, Death's Coffer deposits/values, bank snapshots, disconnect/reconnect. Developer testing only in dev builds.

Simulate >=10,000 seeded accounts across focused/balanced/inefficient strategies, including varied XP/time/rewards, purchase policies, finite spins, Defy, Taint, sacrifice, negative FP and impossible-access cases. Report assumptions and missing gear/quest realism honestly.

## Correction acceptance
Bossing Lesser/Standard/Greater: selected before random boss, no boss choice/reroll, Lesser retains base FP, Greater increases count/reward. Assert starting Standard default, two distinct targets, same-type permitted, purchased pool persists, third upgrade yields three. Restore pending offers/card/boss across crash boundaries. Assert repeated Pardons at same price with no account/run cap, Banned -> Locked only, and independent vendor unlock/gamble afterward.

## Confirmed recovery and Taint accounting
Verify empty-set Sacrifice enters acquisition recovery, accepts a newly obtained eligible item worth at least 10,000 GP, rejects below-minimum/ineligible/protected items, and blocks ordinary progression until verified donation. Unknown bank contents must not trigger empty-set recovery. Verify one Taint spin charge across completion/restart/replay, with no second Punishment debit. Verify one Sacrifice spin charge before its spin reward, including acquisition recovery and restart; donation completion must not charge another spin.

Current offline simulation replay must also agree across process hash seeds, not merely two calls in one interpreter. Preserve historical results with their source revisions; do not relabel them as current-rule runs.

## Submission regression and future live verification
Static production review must catch fresh Gson/OkHttp construction, KeyboardFocusManager, forbidden input/server actions, restricted scripts, unsafe persistence and unexpected packaged dependencies. Reproduce the Hub standard packaging scan, not merely local Java compilation: the initial Gson failure happened during packaging. Current official example/standard target is Java 11; align actual Hub tooling when implementation is authorized.

Future controlled live cases: CA points/threshold initialization and change; four awakened counters on ordinary versus awakened kills; paused/pre-run/reconnected completions; NPC loot/server loot/chests/no-loot; vendor identity and bank completeness; Coffer donation receipt versus drop/bank/consume. GE initial EMPTY states must not create penalties. Do not test a prohibited menu-removal workaround in production. No such live tests were performed now.


## Focused future detector acceptance cases (WB-D066-068)
Trade: outgoing left/right click; incoming chat and alternate acceptance; first/final window Accept; already-open trade when activation or restriction changes; pause/resume; character switch; unknown target; duplicate attempts; interaction with other menu plugins. Verify option text/order stays intact, restricted request is not sent on covered routes, notice does not steal focus, and dismiss sends no action. Warning-only mode, if used for testing, must clearly say it does not block. Do not introduce gameplay penalties from attempt logs.

Coffer: initialized zero/nonzero balance; one item, stack, noted variants; quantity 1/5/X/All; selected slot changes; actual final confirmation; failed/full/ineligible donation; quote/base-value/credit rounding; Blessed exclusion; inventory delta before/after balance event; UI closure; login/reconnect and shared IF1 reuse; repeated callbacks; two identical successive donations. Capture current group/widget tree, varp values, server-displayed quote and event order. Expected: only a matched confirmed donation settles once; drop/bank/consume/player trade and ambiguous reconnect do not award. No live results are claimed.
