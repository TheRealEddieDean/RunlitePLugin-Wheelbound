# Decision register

Sources S1-S5 and access limits are defined in [README](README.md). Approval provenance is separate from mechanic status. UNVERIFIED is not a rejected rule; do not implement it as historically approved. Rationales labelled inferred are reconstruction explanations, not quotes. IDs never reused; revisions append supersession notes.

## WB-D001 - Optional challenge; no rankings or integrity labels

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §4; S4 user 2026-09-30
- Decision: Optional challenge; no rankings or integrity labels.
- Rationale: Player-controlled challenge.
- Affects: [GAME_RULES](GAME_RULES.md).
- Exceptions/dependencies: Normal wheels remain usable.

## WB-D002 - Unrestricted pause; no penalty/Assisted status; no retroactive XP/drop/quest/bounty credit

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §4; S4 2026-09-30
- Decision: Unrestricted pause; no penalty/Assisted status; no retroactive XP/drop/quest/bounty credit.
- Rationale: Escape bugs/hardlocks without policing.
- Affects: [GAME_RULES](GAME_RULES.md), [PERSISTENCE](PERSISTENCE.md), [BOUNTIES](BOUNTIES.md).
- Exceptions/dependencies: Pause restrictions removed immediately.

## WB-D003 - Seal one equally weighted five-outcome Grand Fate, no reroll

- Status: CONFIRMED
- Authority: Explicit user in attached confirmed context
- Evidence: S2 §6; S4 2026-09-18
- Decision: Seal one equally weighted five-outcome Grand Fate, no reroll.
- Rationale: End-goal-first identity.
- Affects: [GRAND_FATES](GRAND_FATES.md).
- Exceptions/dependencies: Real OSRS requirements researched separately.

## WB-D004 - Grand attempts no artificial level/playtime or Master-spin charge, between ordinary Fates

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §6
- Decision: Grand attempts no artificial level/playtime or Master-spin charge, between ordinary Fates.
- Rationale: Player chooses readiness.
- Affects: [GRAND_FATES](GRAND_FATES.md), [GAME_RULES](GAME_RULES.md).
- Exceptions/dependencies: Mandatory-obligation interaction OPEN.

## WB-D005 - 24 active total including Taint/Sacrifice; no extra slots

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §7
- Decision: 24 active total including Taint/Sacrifice; no extra slots.
- Rationale: One shared probability pool.
- Affects: [WHEELS](WHEELS.md), [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: First Defy inserts two specials.

## WB-D006 - Five ordinary active, at least three activities

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §7
- Decision: Five ordinary active, at least three activities.
- Rationale: Preserve diversity and commitment.
- Affects: [WHEELS](WHEELS.md).
- Exceptions/dependencies: Duplicates do not add distinct activities.

## WB-D007 - Start Mining/Fishing/Woodcutting/Combat/Questing; initial instances unprotected

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §7
- Decision: Start Mining/Fishing/Woodcutting/Combat/Questing; initial instances unprotected.
- Rationale: Flexible wheel building within minimum.
- Affects: [WHEELS](WHEELS.md).
- Exceptions/dependencies: Underlying activity keys remain distinct.

## WB-D008 - Per-slice persistent ID and modifiers; relative weights; free reordering

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §7
- Decision: Per-slice persistent ID and modifiers; relative weights; free reordering.
- Rationale: Persistent individual upgrades.
- Affects: [WHEELS](WHEELS.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Order not probability.

## WB-D009 - Duplicate costs escalate by activity lifetime purchases; deletion does not reset

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §7
- Decision: Duplicate costs escalate by activity lifetime purchases; deletion does not reset.
- Rationale: Avoid cheap destructive reacquisition exploit.
- Affects: [WHEELS](WHEELS.md), [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: First/reacquisition counting OPEN.

## WB-D010 - Enhancement ladder 1/1.25/1.5/2/3; fixed prices only by tier

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §7
- Decision: Enhancement ladder 1/1.25/1.5/2/3; fixed prices only by tier.
- Rationale: Predictable upgrade pricing.
- Affects: [WHEELS](WHEELS.md), [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: Numeric prices still provisional.

## WB-D011 - Satchel 3/6/10/15; paid deactivate/reactivate preserves upgrade; free destruction loses upgrade

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §7
- Decision: Satchel 3/6/10/15; paid deactivate/reactivate preserves upgrade; free destruction loses upgrade.
- Rationale: Storage versus permanent loss tradeoff.
- Affects: [WHEELS](WHEELS.md), [FATE_SHOP](FATE_SHOP.md).
- Exceptions/dependencies: No refunds; capacity/affordability checks.

## WB-D012 - Taint full-wheel displacement player selected ordinary; store or destroy; retain minimum

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §10
- Decision: Taint full-wheel displacement player selected ordinary; store or destroy; retain minimum.
- Rationale: Agency within constrained corruption.
- Affects: [WHEELS](WHEELS.md), [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Both room and FP needed for storage.

## WB-D013 - Finite ordinary spins; completion consumes one

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §5; S4 2026-10-01
- Decision: Finite ordinary spins; completion consumes one.
- Rationale: Pressure toward endgame/Defy.
- Affects: [GAME_RULES](GAME_RULES.md), [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: Grand attempts excluded.

## WB-D014 - First Defy unlocks Taint/Sacrifice/voluntary Coffer/one free Blessed slot

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §10
- Decision: First Defy unlocks Taint/Sacrifice/voluntary Coffer/one free Blessed slot.
- Rationale: Introduce costly recovery systems together.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Refill amount unknown.

## WB-D015 - Odd Defies introduce Taint up to three; later qualifying events increase weights

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §10
- Decision: Odd Defies introduce Taint up to three; later qualifying events increase weights.
- Rationale: Escalating corruption.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Exact increment from S3 not direct approval.

## WB-D016 - Sacrifice +0.25 each subsequent Defy, permanent; Taint only Cleansing

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §10
- Decision: Sacrifice +0.25 each subsequent Defy, permanent; Taint only Cleansing.
- Rationale: Persistent consequences.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: No ordinary special removal.

## WB-D017 - Blessed types protect forced sacrifice, maximum three

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §10
- Decision: Blessed types protect forced sacrifice, maximum three.
- Rationale: Limited equipment protection.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Reassignment timing from S3 unverified.

## WB-D018 - Reject ordinary Fate: FP loss/debt, spin consumed, no completion reward, mandatory punishment

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §11
- Decision: Reject ordinary Fate: FP loss/debt, spin consumed, no completion reward, mandatory punishment.
- Rationale: Escape unwanted task at real cost.
- Affects: [PUNISHMENTS](PUNISHMENTS.md), [GAME_RULES](GAME_RULES.md).
- Exceptions/dependencies: Grand Fate excluded; restart no reroll.

## WB-D019 - Vendor unlock/Tempt requires physical visit; Locked/Unlocked/Banned

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S1; S2 §12; S4 2026-09-30
- Decision: Vendor unlock/Tempt requires physical visit; Locked/Unlocked/Banned.
- Rationale: In-world access progression.
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md).
- Exceptions/dependencies: No remote purchases.

## WB-D020 - Tempt Fate 50/50 unlock or ban; Pardon returns ban to Locked

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §12
- Decision: Tempt Fate 50/50 unlock or ban; Pardon returns ban to Locked.
- Rationale: Risk/recovery access choice.
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md).
- Exceptions/dependencies: Pardon cap conflict OPEN.

## WB-D021 - GE permanent special unlock with completion count and FP gate

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §12
- Decision: GE permanent special unlock with completion count and FP gate.
- Rationale: Separate major access milestone.
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md).
- Exceptions/dependencies: No gambling per S3 historical approval unverified.

## WB-D022 - Four progression branches and separate boss wheel/tier unlocks

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §8
- Decision: Four progression branches and separate boss wheel/tier unlocks.
- Rationale: Content progression versus modifiers.
- Affects: [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: Exact boss list/gates missing.

## WB-D023 - Combat skill selected before target; XP scales with level then freezes

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §8
- Decision: Combat skill selected before target; XP scales with level then freezes.
- Rationale: Appropriate player choice and committed difficulty.
- Affects: [FATE_CARDS](FATE_CARDS.md).
- Exceptions/dependencies: HP exemption S3 evidence.

## WB-D024 - Permanent card pools; two eligible offerings upgraded to three

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §9
- Decision: Permanent card pools; two eligible offerings upgraded to three.
- Rationale: Purchases improve opportunity.
- Affects: [FATE_CARDS](FATE_CARDS.md).
- Exceptions/dependencies: Initial two-type availability unresolved.

## WB-D025 - 100 permanent items, three daily, Combat Mastery and Diary bounties

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §13
- Decision: 100 permanent items, three daily, Combat Mastery and Diary bounties.
- Rationale: Optional progress rewards.
- Affects: [BOUNTIES](BOUNTIES.md).
- Exceptions/dependencies: Historical exact catalog unavailable.

## WB-D026 - 100 FP reference, all prior prices provisional; simulate >=10,000 accounts

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §14
- Decision: 100 FP reference, all prior prices provisional; simulate >=10,000 accounts.
- Rationale: Evidence-based balance.
- Affects: [ECONOMY](ECONOMY.md), [SIMULATION_RESULTS](SIMULATION_RESULTS.md).
- Exceptions/dependencies: Not fixed duration or flat reward proof.

## WB-D027 - Tier I nine skills 5/9; Tier II six 4/6; Mastery

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §7
- Decision: Tier I nine skills 5/9; Tier II six 4/6; Mastery.
- Rationale: Staged unlock progression inferred from summary.
- Affects: [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: Exact approval unavailable; Sailing current facts unverified.

## WB-D028 - Quest choices three/four/five, difficulty/completion/FP gates

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §6
- Decision: Quest choices three/four/five, difficulty/completion/FP gates.
- Rationale: Agency after quest roll.
- Affects: [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: Older memory 1-3 superseded in summary, actual approval missing.

## WB-D029 - Dedicated boss path kill target before wheel, no cards/rerolls/selection

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §6, conflicts §5
- Decision: Dedicated boss path kill target before wheel, no cards/rerolls/selection.
- Rationale: Commit boss difficulty, inferred rationale.
- Affects: [FATE_CARDS](FATE_CARDS.md), [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: C01 cannot settle chronology by section order.

## WB-D030 - Default 1000 XP tolerance/Fate, unavoidable by-products exempt, penalties escalate

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §10; memory
- Decision: Default 1000 XP tolerance/Fate, unavoidable by-products exempt, penalties escalate.
- Rationale: Tolerate incidental gameplay.
- Affects: [GAME_RULES](GAME_RULES.md).
- Exceptions/dependencies: Penalty formula and permitted attribution OPEN.

## WB-D031 - No Fate timer; rewards/objectives freeze

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §5
- Decision: No Fate timer; rewards/objectives freeze.
- Rationale: Avoid shifting tasks on level-up/restart.
- Affects: [FATE_CARDS](FATE_CARDS.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Freeze choice draw before animation proposed.

## WB-D032 - Taint later adds 0.25 each at cap; Cleansing removes one fixed expensive price

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §9
- Decision: Taint later adds 0.25 each at cap; Cleansing removes one fixed expensive price.
- Rationale: Escalation persists through cleansing.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Refill cleared slices before weight escalation.

## WB-D033 - Forced sacrifice ~12 snapshot candidates; player chooses; stacks vs single copy

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §9
- Decision: Forced sacrifice ~12 snapshot candidates; player chooses; stacks vs single copy.
- Rationale: Meaningful value/diversity cost.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Incomplete bank, no candidates fallback missing.

## WB-D034 - Blessings reassign outside forced event; locked at trigger

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §9
- Decision: Blessings reassign outside forced event; locked at trigger.
- Rationale: Prevent protecting after candidates revealed.
- Affects: [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: No direct approval recovered.

## WB-D035 - Punishment wheel ~12 from ~50, cannot reject, small FP reward

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §10
- Decision: Punishment wheel ~12 from ~50, cannot reject, small FP reward.
- Rationale: Committed punishment obligation.
- Affects: [PUNISHMENTS](PUNISHMENTS.md).
- Exceptions/dependencies: Catalog/payout unknown.

## WB-D036 - Daily 24h refresh; manual claims and one-time permanent bounties

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §11
- Decision: Daily 24h refresh; manual claims and one-time permanent bounties.
- Rationale: Optional visible rewards.
- Affects: [BOUNTIES](BOUNTIES.md).
- Exceptions/dependencies: Reset anchor/expiry/overlap missing.

## WB-D037 - Vendor class escalating prices, fixed Pardon; GE no gamble/ban/escalation

- Status: UNVERIFIED
- Authority: Historical authority unknown
- Evidence: S3 §8
- Decision: Vendor class escalating prices, fixed Pardon; GE no gamble/ban/escalation.
- Rationale: Different access cost mechanisms.
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md), [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: C02 limited restoration versus repeat purchase.

## WB-D038 - Polished native RuneLite style, boss icons and functional spin

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S4 user 2026-09-12
- Decision: Polished native RuneLite style, boss icons and functional spin.
- Rationale: Match existing plugin UX.
- Affects: [UI_UX](UI_UX.md).
- Exceptions/dependencies: Wheel placement C03 unresolved.

## WB-D039 - Grand red spikes/skull style and punishing Defy

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S4 user 2026-09-18
- Decision: Grand red spikes/skull style and punishing Defy.
- Rationale: Dramatic identity.
- Affects: [UI_UX](UI_UX.md), [DEFY_FATE](DEFY_FATE.md).
- Exceptions/dependencies: Old CP and +2/+5/+10 proposals not final.

## WB-D040 - Per-account persistence, committed outcomes, exactly-once transactions

- Status: CONFIRMED
- Authority: Explicit user for safe persistence; technical details delegated
- Evidence: S2 §12/15; S3 §2
- Decision: Per-account persistence, committed outcomes, exactly-once transactions.
- Rationale: Prevent restart rerolls/double rewards.
- Affects: [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Account/profile distinction must validate API.

## WB-D041 - Dev-only controlled testing mode and honest advisory enforcement

- Status: CONFIRMED
- Authority: Explicit user
- Evidence: S2 §15
- Decision: Dev-only controlled testing mode and honest advisory enforcement.
- Rationale: Test without production bypass.
- Affects: [RUNELITE_INTEGRATION](RUNELITE_INTEGRATION.md), [TESTING](TESTING.md).
- Exceptions/dependencies: No server restrictions possible.

## WB-D042 - All numeric prices/rates/refills not finalized

- Status: BALANCE_TBD
- Authority: Explicit user provisional status
- Evidence: S2 §14; S3 §13/19
- Decision: All numeric prices/rates/refills not finalized.
- Rationale: Avoid false certainty.
- Affects: [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: PDF sample prices preserved only as hypotheses.

## WB-D043 - OSRS exact rewards/Coffer threshold/Yama/CA/Sailing and detectors

- Status: TECHNICAL_TBD
- Authority: Explicit research request
- Evidence: S2 §6/15; S3 §19
- Decision: OSRS exact rewards/Coffer threshold/Yama/CA/Sailing and detectors.
- Rationale: Game changes/API coverage.
- Affects: [RUNELITE_INTEGRATION](RUNELITE_INTEGRATION.md), [GRAND_FATES](GRAND_FATES.md).
- Exceptions/dependencies: Do not claim current facts from memory.

## WB-D044 - Atomic final-wheel validation and reserve assignment spin

- Status: PROPOSED
- Authority: Assistant under delegated technical authority
- Evidence: New recovery specification, 2026-10-09
- Decision: Atomic final-wheel validation and reserve assignment spin.
- Rationale: Avoid invalid intermediate states/double debit.
- Affects: [WHEELS](WHEELS.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: No extra consumed spin; validate crash boundary.

## WB-D045 - Existing normal release wheels retained; old two-wheel removal plan not reapplied

- Status: PROPOSED
- Authority: Assistant conservative preservation
- Evidence: S5 current release versus S4 older scope
- Decision: Existing normal release wheels retained; old two-wheel removal plan not reapplied.
- Rationale: No coding/removal authorized.
- Affects: [UI_UX](UI_UX.md).
- Exceptions/dependencies: User may later explicitly rescope.

## Contradictions and superseded proposals

C01 Bossing cards versus no cards: unresolved historical chronology; dedicated no-card path provisionally preserved with UNVERIFIED status, not claimed latest approval.
C02 Pardon restoration cap: S4 roughly three/account versus S3 fixed-price purchase, cap unverified. Do not assume unlimited or exactly three.
C03 Wheel sidebar versus canvas popup, and top-down tree versus skill ring: latest explicit confirmation unavailable; preserve existing renderer provisionally.
C04 Prior one-card offering versus current two-to-three: S2 current confirmed overrides old memory.
C05 Old 1-3 quest choices versus S3 three-to-five: later summary retained as UNVERIFIED, not falsely confirmed.

Superseded/rejected under S1: lifetime enhancement-price escalation; special slots beyond 24; protected original slice instances; system-selected Taint replacement; pause penalty/Assisted labels; artificial Grand Fate readiness gates; rejecting without punishment. Never reintroduce as confirmed.
S4 earlier CP terminology, difficulty-based spin caps, +2/+5/+10 curses and 50% CP defy price are proposals, not final FP design.
