# Testing acceptance
No plugin tests were run or code changed for recovery checkpoint.

Required invariants: five ordinary/three activities/24 including specials; weights normalized; fixed enhancements independent of lifetime counts; duplicate counters survive destruction; store retains UUID; Defy odd schedule and post-cleansing refill; first Defy two inserts; negative FP recovery; Reject consumes one and no reward.

Transaction crash tests before/after journal and snapshot writes, replay completion/claim/donation, account switch and stale callbacks. Pause must exclude XP/drop/quest/CA/bounty progress and remove restrictions. Grand Fate attempts no spin debit, preserve checklist, no readiness gate.

Live tests: current quest/CA mappings, all covered shop/GE/trade inputs, Death's Coffer deposits/values, bank snapshots, disconnect/reconnect. Developer testing only in dev builds.

Simulate >=10,000 seeded accounts across focused/balanced/inefficient strategies, including varied XP/time/rewards, purchase policies, finite spins, Defy, Taint, sacrifice, negative FP and impossible-access cases. Report assumptions and missing gear/quest realism honestly.
