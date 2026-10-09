# Persistence
Per OSRS account, versioned run ID/schema; do not use settings profile alone as account identity. Save committed random results before animation. Journal transactions with IDs for assignment, completion, purchase, rejection, Defy, bounty claim and verified donation.

Persistent records: run mode/FP/spins/Grand Fate/Defy count; slices UUID/activity/weight/modifiers; Satchel; lifetime duplicate counts by activity; vendor status/counters; card unlocks; node unlocks; assignments and audit tolerance; bounty evidence/claims/daily seed; blessings; forced candidate snapshot; Grand Fate checklist.

Atomic mutation checks preconditions, records ledger and resulting state together. Startup reconcile pending receipt, never reroll or replay reward blindly. Retain last-good snapshot and corrupt original. Migration validates wheel five/three/24 invariant; do not destructively reset a run.

Pause stores obligation; resume takes new observed XP baselines and never imports pause progress. Logout/profile/account switch drains pending writes and rejects stale event callbacks. Account ID acquisition and atomic durability mechanics TECHNICAL_TBD.

## Corrected durable fields
Store full independently generated card offers (type, target, Standard reference, reward, seed/version), selected index, frozen boss eligibility pool and selected boss. Stage order Bossing: Standard target/offers committed -> card selected -> random boss committed -> active objective. Restore the correct stage on restart. Standard default is schema baseline; permanent pool unlocks and third-offer upgrade are separate. Pardon receipt records vendor Banned -> Locked and fixed-price debit; there is no cap counter or escalating-price counter. Preserve old Pardon count only as historical receipts, never as an eligibility restriction.
