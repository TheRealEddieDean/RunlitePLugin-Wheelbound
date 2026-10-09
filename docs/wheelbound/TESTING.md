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
