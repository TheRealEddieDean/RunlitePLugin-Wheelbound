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
- Exceptions/dependencies: Pardon unlimited fixed-price purchases confirmed by WB-D051; earlier cap superseded.

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
- Exceptions/dependencies: WB-D050 confirms two distinct Standard targets; card types need not differ.

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

- Status: CONFIRMED choice counts; UNVERIFIED gate thresholds
- Authority: Historical authority unknown
- Evidence: S3 §6; S4 retrieved user 2026-10-01 17:16:22Z
- Decision: Quest choices three/four/five, difficulty/completion/FP gates.
- Rationale: Agency after quest roll.
- Affects: [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: Older memory 1-3 superseded in summary, actual approval missing.

## WB-D029 - Historical dedicated boss path

- Status: SUPERSEDED by WB-D049; retain only as provenance
- Authority: Historical authority unknown
- Evidence: S3 §6, conflicts §5
- Decision: Historical no-card proposal is superseded. Current Bossing includes cards before random boss selection, with no boss choice/reroll.
- Rationale: Commit boss difficulty, inferred rationale.
- Affects: [FATE_CARDS](FATE_CARDS.md), [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: C01 resolved by explicit current user confirmation, WB-D049.

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
- Exceptions/dependencies: C02 resolved by WB-D051; repeat fixed-price purchase, unlimited.

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

C01 RESOLVED 2026-10-09: user confirms Bossing Lesser/Standard/Greater and target -> cards -> card selection -> random boss sequence. Contradictory PDF no-card wording superseded (WB-D049).
C02 RESOLVED 2026-10-09: unlimited fixed-price Pardon; no run/account cap or price escalation. Earlier three-Pardon limit superseded (WB-D051).
C03 Wheel sidebar versus canvas popup, and top-down tree versus skill ring: latest explicit confirmation unavailable; preserve existing renderer provisionally.
C04 Prior one-card offering versus current two-to-three: S2 current confirmed overrides old memory.
C05 Old 1-3 quest choices superseded by retrieved user 2026-10-01 17:16:22Z: confirmed three -> four -> five. Gate numbers remain unverified.

Superseded/rejected under S1: lifetime enhancement-price escalation; special slots beyond 24; protected original slice instances; system-selected Taint replacement; pause penalty/Assisted labels; artificial Grand Fate readiness gates; rejecting without punishment. Never reintroduce as confirmed.
S4 earlier CP terminology, difficulty-based spin caps, +2/+5/+10 curses and 50% CP defy price are proposals, not final FP design.

## WB-D046 - Bossing card types in dated user evidence

- Status: CONFIRMED historical and current by WB-D049; C01 resolved.
- Authority: Explicit user, retrieved summary rather than full transcript.
- Evidence: S4 user 2026-10-01 17:16:22Z.
- Decision: Bossing supports Lesser/Standard/Greater category-specific cards; purchased Skilling cards; Questing starts three options upgraded four/five.
- Rationale: Category-specific difficulty choices (reconstructed explanation).
- Affects: [FATE_CARDS](FATE_CARDS.md), [PROGRESSION](PROGRESSION.md).
- Exceptions/dependencies: Current user explicitly preserves Bossing cards; historical no-card PDF statement superseded.

## WB-D047 - General challenge slice avoids dwindling boss CA pool

- Status: CONFIRMED concept; detailed implementation UNVERIFIED.
- Authority: Explicit user, retrieved summary.
- Evidence: S4 user 2026-10-01 17:19:57Z.
- Decision: General challenge slice concept accepted; challenge must not depend on a single boss's remaining incomplete CAs.
- Rationale: Avoid shrinking challenge availability.
- Affects: [PROGRESSION](PROGRESSION.md), [FATE_CARDS](FATE_CARDS.md), [BOUNTIES](BOUNTIES.md).
- Exceptions/dependencies: Slice location, pool and card interactions not recovered.

## WB-D048 - Preserve unresolved history before final balance

- Status: RESOLVED by WB-D049, WB-D050, WB-D051.
- Authority: User instruction requires latest explicit approval precedence and input for core contradictions.
- Evidence: S1 contradiction rule; S2 §2 core-gameplay stop rule.
- Decision: Initial blockers have authoritative resolution; continue remaining research/design/simulation autonomously.
- Rationale: Choosing silently would reinterpret previously established mechanics.
- Affects: [TASKS](TASKS.md), [SIMULATION_RESULTS](SIMULATION_RESULTS.md), [FATE_CARDS](FATE_CARDS.md), [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md).
- Exceptions/dependencies: Independent technical research can proceed; no plugin implementation authorized.

## WB-D049 - Bossing Fate Cards and assignment sequence

- Status: CONFIRMED - established rule reaffirmed
- Authority: Explicit user current message
- Evidence: S6, current user resolution, 2026-10-09.
- Decision: Bossing selects Standard target, offers Lesser/Standard/Greater, card selected before random Bossing Wheel. Lesser reduces kills with base FP; Standard normal; Greater increases kills and FP. No boss choice/reroll.
- Rationale: Preserve category-specific card choice while boss remains random. (design explanation; not quoted historical rationale).
- Affects: [FATE_CARDS](FATE_CARDS.md), [PROGRESSION](PROGRESSION.md), [ECONOMY](ECONOMY.md), [UI_UX](UI_UX.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Supersedes WB-D029 no-card proposal and C01; multipliers BALANCE_TBD.

## WB-D050 - Fresh Standard offerings

- Status: CONFIRMED - NEW DESIGN DECISION, 2026-10-09
- Authority: Explicit user; new approval, not historical recovery
- Evidence: S6, current user resolution, 2026-10-09.
- Decision: Standard unlocked by default; two independently generated Standard offerings with different targets, same-type allowed. Purchased types permanently enter respective pools. Third offering upgrade yields three.
- Rationale: Makes two-offer starting experience meaningful before additional card unlocks. (design explanation; not quoted historical rationale).
- Affects: [FATE_CARDS](FATE_CARDS.md), [FATE_SHOP](FATE_SHOP.md), [ECONOMY](ECONOMY.md), [UI_UX](UI_UX.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Collision handling and legal target-domain size must validate; don't relabel historical approval.

## WB-D051 - Unlimited fixed-price Fate's Pardon

- Status: CONFIRMED - established rule reaffirmed
- Authority: Explicit user current message
- Evidence: S6, current user resolution, 2026-10-09.
- Decision: Infinitely purchasable at very expensive fixed FP price, no per-run/account cap or price escalation. Banned -> Locked; separate paid unlock or Tempt Fate afterward.
- Rationale: Maintain expensive recovery without permanent vendor lockout. (design explanation; not quoted historical rationale).
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md), [FATE_SHOP](FATE_SHOP.md), [ECONOMY](ECONOMY.md), [UI_UX](UI_UX.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Earlier three-Pardon limit superseded; numeric price BALANCE_TBD.

## WB-D052 - H1 economy and progression parameters

- Status: PROPOSED / BALANCE_TBD
- Authority: Assistant under delegated design authority
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Adopt H1 solely as an executable test hypothesis; parameters.json and ECONOMY.md document starting reserve/refill, fees, penalties, reward formulas, conversion and duplicate 8+ extrapolation.
- Rationale: Test interacting systems without claiming recovered approval.
- Affects: [ECONOMY](ECONOMY.md), [SIMULATION_RESULTS](SIMULATION_RESULTS.md).
- Exceptions/dependencies: No numeric final balance acceptance. Actual catalogs/rates unverified.

## WB-D053 - Distinct target generation and durable card stages

- Status: PROPOSED technical detail
- Authority: Assistant under delegated technical authority
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Independently propose targets, resample target collisions within legal domain, persist complete offers; initial Bossing displayed Standard reference is first offer, second shows its own. Freeze eligibility before selecting random boss after card choice.
- Rationale: Respect distinct starting targets and avoid restart/pool manipulation.
- Affects: [FATE_CARDS](FATE_CARDS.md), [PERSISTENCE](PERSISTENCE.md), [UI_UX](UI_UX.md).
- Exceptions/dependencies: Rejection sampling conditions independence on uniqueness; undersized target domain gives error.

## WB-D054 - Audit charge schedule

- Status: PROPOSED / BALANCE_TBD
- Authority: Assistant under delegated design authority
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: One charge per Fate at tolerance crossing, base100 times capped prior violation streak; legitimate completion resets. Extra ticks don't incur extra charges.
- Rationale: Avoid unbounded event-driven fines.
- Affects: [GAME_RULES](GAME_RULES.md), [ECONOMY](ECONOMY.md).
- Exceptions/dependencies: Attribution must be observable; H1 only flat per-Fate probabilistic charge, not full proposed audit.

## WB-D055 - Vendor price counters and transactions

- Status: PROPOSED detail
- Authority: Assistant under delegated authority
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Paid class unlocks advance class-price counter; gamble does not. Normal unlock/Tempt in-world; Pardon Locked transition never grants access or increments paid counter.
- Rationale: Separate access purchases from recovery/gambling.
- Affects: [ACCOUNT_ACCESS](ACCOUNT_ACCESS.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Pardon location policy and exact vendor catalog unverified.

## WB-D056 - Daily board schedule and overlap claims

- Status: PROPOSED detail
- Authority: Assistant under delegated authority
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Rolling24h board from first generation; three distinct eligible targets, no offline backlogs, pre-expiry evidence remains claimable. One event may satisfy permanent and daily keys once each.
- Rationale: Predictable optional rewards and durable evidence.
- Affects: [BOUNTIES](BOUNTIES.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: Not recovered historical approval; H1 excludes daily economy.

## WB-D057 - Acquire an item when forced Sacrifice has no eligible candidate

- Status: CONFIRMED - new explicit user approval, 2026-10-09
- Authority: User explicitly selected post-trigger acquisition; not recovered historical approval.
- Evidence: "if they do not have an eligible item they will have to get one"; minimum "something worth at least 10k".
- Decision: A genuinely empty eligible set keeps the mandatory Sacrifice pending. The player must acquire an eligible item worth at least 10,000 GP and sacrifice it. No replacement Punishment or waived obligation.
- Rationale: Preserve the cost of Sacrifice while providing a route out of an empty candidate set.
- Affects: DEFY_FATE, EDGE_CASES, GAME_RULES, UI_UX, PERSISTENCE, TESTING, ECONOMY and SIMULATION_RESULTS.
- Exceptions/dependencies: Explicit exception to the frozen ownership snapshot: permit a newly acquired eligible item in this recovery case, validate and freeze it before donation. Unknown bank contents are not an empty set. Blessed protections and actual Coffer eligibility still apply. Game acceptance/value detector and GP-to-spin conversion remain technical/balance work. The acquisition route must respect existing account access; no automatic vendor/GE unlock is approved.

## WB-D058 - Special slice spin accounting

- Status: CONFIRMED - Taint and Sacrifice each consume one spin
- Authority: Explicit user approval, 2026-10-09, separately confirming Taint and then Sacrifice.
- Evidence: "yes a tainted task should eat up a spin", followed by "yes" to "does landing on Sacrifice also consume one spin?".
- Decision: A Taint task consumes exactly one Master Wheel spin. Its resulting mandatory Punishment does not create a second spin charge. Sacrifice likewise consumes exactly one spin, explicitly confirmed by the user's subsequent "yes" to that specific question. Ordinary completion/rejection spends one and Grand Fate attempts none remain confirmed.
- Rationale: Taint spends the finite progression resource in addition to imposing a Punishment.
- Affects: DEFY_FATE, GAME_RULES, PUNISHMENTS, ECONOMY, PERSISTENCE, TESTING and SIMULATION_RESULTS.
- Exceptions/dependencies: Charge exactly once across restart/replay. Proposed debit timing is when the Taint outcome and obligation are durably committed. H1 charges neither special and the prior sensitivity charges both; the all-special-debit sensitivity models the approved charge rule but lacks approved acquisition recovery.

## WB-D059 - RuneLite API availability versus feasibility

- Status: TECHNICAL_TBD; evidence-backed API availability
- Authority: Primary source inspection, not live test
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Use per-account RS profile API, StatChanged, quest state, NPC loot/container/menu events and scoped Filepath. Avoid claiming full menu prevention or verified Coffer donation; use injected Gson and review-sensitive scripts only.
- Rationale: Respect current platform limits and honest detector coverage.
- Affects: [RUNELITE_INTEGRATION](RUNELITE_INTEGRATION.md), [ARCHITECTURE](ARCHITECTURE.md), [PERSISTENCE](PERSISTENCE.md).
- Exceptions/dependencies: No gameplay spike/Hub acceptance. Offline economy harness not in-client content simulation.

## WB-D060 - Checkpoint PDF and missing catalogs

- Status: PROPOSED documentation process
- Authority: Assistant following source-of-truth mandate
- Evidence: new work dated 2026-10-09; see referenced specs and recorded sources.
- Decision: Generate checkpoint PDF from committed Markdown, clearly retaining unresolved core rules and missing original catalogs. Do not label it final implementation-ready design.
- Rationale: Preserve recoverable design without inventing prior approvals.
- Affects: [README](README.md), [TASKS](TASKS.md), [IMPLEMENTATION_PLAN](IMPLEMENTATION_PLAN.md).
- Exceptions/dependencies: Final PDF/catalog economy deferred until significant OPEN decisions resolved.


## WB-D061 - Current-rule recovery simulation H2

- Status: PROPOSED modeling assumptions; not approved gameplay numbers
- Authority: Assistant under delegated simulation authority, 2026-10-09.
- Decision: Charge one spin for both Taint and Sacrifice, following WB-D058. Model WB-D057 as acquiring an eligible 10,000 GP item before donation. Use a synthetic earned-item route with no vendor unlock, normal Fate reward or FP reward; acquisition duration is 1-3 hours divided by strategy efficiency, with 3-9 hours in a sensitivity. Preserve H1 results as historical evidence. Sort unlocked activities before seeded choice so output is reproducible across process hash seeds.
- Rationale: Test recovery burden without inventing automatic account unlocks or treating an empty set as a waived obligation.
- Affects: SIMULATION_RESULTS, ECONOMY, TESTING and offline simulation files.
- Exceptions/dependencies: Duration, item availability and legal acquisition route are unvalidated hypotheses. Real access, skill/tool prerequisites, Blessed exclusions and acquisition XP tolerance require catalog/detector validation. Zero simulated stops follow from guaranteed synthetic acquisition and do not prove real-account deadlock freedom. GP-to-spin conversion still uses H1's unapproved minimum-one hypothesis.

## WB-D062 - Preserve the actual original Hub review blockers

- Status: CONFIRMED historical technical evidence, checked 2026-10-09
- Authority: Public Hub comments, CI log and accepted source; not a user gameplay approval.
- Decision: Initial build rejected fresh Gson construction; later reviewer rejected KeyboardFocusManager. Keep injected Gson and event-component text-input checks. PR 16702 merged on 2026-09-24 at c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.
- Rationale: Prevent repeating real blockers or mistaking a failed packaging scan for rejection of Wheelbound's basic wheel concept.
- Affects: POLICY_AND_FEASIBILITY, RUNELITE_INTEGRATION, ARCHITECTURE, UI_UX and TESTING.
- Exceptions/dependencies: Private reviewer discussions inaccessible. Existing acceptance does not approve future mode features. CI/source details and links are preserved in POLICY_AND_FEASIBILITY.md.

## WB-D063 - Access enforcement has unresolved policy constraints

- Status: TECHNICAL CONSTRAINT; later evidence/clarification in WB-D066/067 supersedes absence-of-precedent assessment
- Authority: Jagex client guidelines and RuneLite rules; assistant assessment under delegated research authority.
- Decision: Do not remove/reorder player Trade with options. API availability alone does not approve click consumption; current Hub precedent was subsequently recovered in WB-D067. Shop/GE state-dependent interception has API support but needs specific policy and coverage review. Preserve established access-lock intent; do not silently replace it with new penalties.
- Rationale: API availability is not permission or comprehensive enforcement.
- Affects: ACCOUNT_ACCESS, GAME_RULES, UI_UX, ARCHITECTURE, RUNELITE_INTEGRATION and implementation plan.
- Exceptions/dependencies: The earlier advisory replacement remains unapproved; user instead clarified retain-menu attempt warning in WB-D066. No maintainer contact or new submission authorized/performed. Exact interception behavior and alternate routes need review.

## WB-D064 - Grand Fate candidate counters and thresholds

- Status: API evidence CONFIRMED; detector design PROPOSED; live verification outstanding
- Authority: Primary RuneLite generated source, inspected 2026-10-09.
- Decision: Explore four awakened kill varps (3971-3974) for the DT2 checklist and CA_POINTS (14815) versus CA_THRESHOLD_MASTER (14813) for Master tier. Use account/session baselines and qualifying deltas; do not assume a fixed threshold or pre-owned items prove a fresh completion.
- Rationale: Specific transmitted account signals are stronger candidates than generic item possession or NPC despawn.
- Affects: GRAND_FATES, BOUNTIES, RUNELITE_INTEGRATION and TESTING.
- Exceptions/dependencies: Transmission timing, initial zeros, pause/pre-run credit and threshold updates require live/catalog validation. This does not settle gameplay credit for existing progress.

## WB-D065 - Keep future mode within local observation and explicit player actions

- Status: CONFIRMED platform constraints; assistant implementation boundary
- Authority: Current Jagex rules and RuneLite Hub policy, not a new user gameplay approval.
- Decision: Do not automate OSRS actions, add boss attack/prayer/safe-position assistance, simulate encounters in-client, restore forbidden focus/Gson behavior or access credentials. Use Java-compatible Hub build, scoped persistence and injected services. Offline design tools stay outside the production JAR.
- Rationale: Preserve player control and reviewable plugin behavior.
- Affects: ARCHITECTURE, RUNELITE_INTEGRATION, UI_UX and IMPLEMENTATION_PLAN.
- Exceptions/dependencies: Passive completion tracking is a policy interpretation pending specific review; accepted existing plugins are not blanket precedent. Coffer, trade and method attribution remain incomplete. FP randomness does not authorize OSRS staking or real-money sales.


## WB-D066 - Retain Trade with and show Fate restriction on attempts

- Status: CONFIRMED new user clarification; exact implementation/coverage pending
- Authority: User explicitly requested the menu option remain and a popup when trading is attempted, 2026-10-09. Not a claim of historical approval or Jagex approval.
- Decision: Preserve player Trade with text/order. Show a local Fate restriction notice when an active Wheelbound access rule disallows an observed trading attempt. Preserve the hard-restriction intent rather than silently substituting an advisory-only rule.
- Rationale: Communicate Fate's restriction at the action without deleting the menu entry.
- Affects: ACCOUNT_ACCESS, UI_UX, POLICY_AND_FEASIBILITY, RUNELITE_INTEGRATION and TESTING.
- Exceptions/dependencies: Warning alone does not block; consume-and-notice is the proposed technical route. Exact modality and all acceptance routes need review/testing. Pause disables restrictions. Player trading unlock policy remains OPEN; vendor Banned/Locked states and Pardon semantics remain unchanged. No new FP loss or gameplay penalty approved.

## WB-D067 - Current Hub trade interception precedent recovered

- Status: CONFIRMED source/manifest evidence; Wheelbound feature-specific approval outstanding
- Authority: Assistant technical research, inspected 2026-10-09; current Hub manifest, pinned peer source and merged update.
- Decision: Bronzeman Unleashed at Hub-pinned 99a6b77ef9bbd20c84b72f014fd49144f982a865 consumes restricted Trade with/Accept trade events and issues local restriction messages, with BUPlugin forwarding the event. Revise the earlier unsupported-interception assessment to feasible with concrete current Hub precedent. Do not classify click cancellation as explicitly prohibited menu deletion.
- Rationale: Ground the feasibility assessment in actual connected, listed plugin behavior instead of API existence or README claims alone.
- Affects: WB-D063, ACCOUNT_ACCESS, UI_UX, policy audit and integration/tests.
- Exceptions/dependencies: Peer final-window Accept handler is commented out. Local chat/sound precedent is not exact modal-popup approval. Jagex removal/reordering prohibition and similar-feature caveat still apply. No reviewer contact performed. ShopPolicy is visual only; peer GE search filtering does not prove comprehensive GE coverage.

## WB-D068 - Correlated Death's Coffer receipt candidate

- Status: Current widget constants CONFIRMED; historical mapping and detector PROVISIONAL; live verification required
- Authority: Assistant delegated technical research, 2026-10-09, current RuneLite InterfaceID/VarPlayerID and explicitly dated 2021 RuneStar cache script dump.
- Decision: Investigate Coffer groups 670/671, Confirm 0x029e000d and dynamic display children. Historical scripts use generic IF1=261 for displayed balance and IF2-IF4 for slot/quantity/unit quote. Proposed completion requires frozen eligible selection, actual confirm attempt, matching inventory reduction and refreshed credit increase in one account/run/obligation context, then one durable settlement receipt.
- Rationale: Disappearance or a click cannot distinguish donation from banking, dropping, consuming or trading. A matching credited balance supplies evidence of acceptance while the selection/delta identifies the item.
- Affects: PUNISHMENTS/Sacrifice, WHEELS, ECONOMY, PERSISTENCE, RUNELITE_INTEGRATION, policy audit and TESTING.
- Exceptions/dependencies: IF variables are shared interface state, not permanent Coffer counters. Current mapping, rounding, quote meaning, update order, confirmation route, eligibility and overflow/failure require live verification. No exact success chat line verified. Ambiguous/reconnected transactions remain pending; no invented reward or manual verified fallback. One-spin cost and >=10k acquisition rule remain confirmed; GP-to-spin conversion remains OPEN.

## WB-D069 - Restrict method-specific content to verifiable templates

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Challenge and Punishment templates require a versioned method/tool/location detector; possession or animation alone is insufficient. Unsupported templates are excluded before assignment, not waived after selection.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: XP, cards, punishments, audit.
- Exceptions/dependencies: No new penalties for unknown telemetry; live traces required.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D070 - Quest eligibility includes an authored access route

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Use current quest state and prerequisite checks plus a route compatible with account access. Journal eligibility alone does not prove all required vendor interactions are permitted.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Questing, account access, incidental XP.
- Exceptions/dependencies: Do not invent a universal quest exemption.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D071 - Use fresh correlated Grand Fate receipts

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Use active-run encounter/reward evidence, four independent awakened counters and initialized Master point thresholds. Radiant completion requires full-set creation receipts; current official cost is 2,500 aether runes per piece.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Grand Fates, CA, persistence.
- Exceptions/dependencies: Existing-account credit policy remains OPEN; retrospective CA changes are not fresh gameplay.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D072 - Vendor inspection is separate from transaction access

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Permit observing shop identity while Locked/Banned; guard purchases and sales. Unlock/Tempt Fate still requires resolved vendor interaction, actual location and matching interface.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Account access, shop UI.
- Exceptions/dependencies: No proximity-only unlock or shared-stock automatic unlock.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D073 - Guard exact transactions and account for existing GE offers

- Status: PROPOSED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Cover shop quantity routes, GE new/repeat/modify offers, incoming/outgoing player trade and both acceptance screens. Recommend manual cancellation/cleanup of live GE offers before entering or resuming a GE-locked active run.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Account access, pause, UI.
- Exceptions/dependencies: Pause stays immediate and free. Resume prerequisite is provisional; no automatic cancellation, server guarantee or new punishment. Specific policy review required.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D074 - Gate irreversible actions on observation readiness

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Validate initialized Coffer selection, server quote, balance and headroom before donation; correlate confirmation, inventory reduction and credited balance. Treat ambiguous receipts as pending.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Sacrifice, persistence, UI.
- Exceptions/dependencies: Historical IF1–IF4 mapping is optional evidence, not a hard dependency. Full Coffer and account scope remain OPEN.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D075 - One writable session per run

- Status: DELEGATED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: Use one writable account/run session with idempotent transaction receipts and explicit conflict recovery. Never merge conflicting saves by taking the greater FP balance.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: Persistence, architecture.
- Exceptions/dependencies: Local trust boundary only; cloud synchronization is not implied.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D076 - Weekend assignment and inaccessible shared conversation

- Status: CONFIRMED
- Authority/source: Current weekend master assignment and delegated technical research, 2026-10-09; source links and detector evidence in TECHNICAL_SPECIFICATIONS.md. WB-D076 is direct user instruction; other selections are not historical user approval.
- Decision: User authorizes documentation, reproducible simulations, research and pipeline planning, without production plugin behavior changes or Plugin Hub submission. Canonical branch remains wheelbound-mode. Shared conversation fetch failed; preserve missing history.
- Rationale: Preserve confirmed gameplay while defining honest, implementable observation boundaries.
- Affects: All specifications and handoff.
- Exceptions/dependencies: Routine choices delegated; major questions collected for return. No automatic restart claimed.
- Supersession: Refines WB-D059/063/064/068 where applicable; does not supersede explicit gameplay approvals.
- Acceptance criteria: Pass the relevant detector and invariant cases in TECHNICAL_SPECIFICATIONS.md and TESTING.md before release.

## WB-D077 - Specify aggregate and crash boundaries before implementation

- Status: DELEGATED
- Authority/source: Weekend master assignment, 2026-10-09; STATE_CONTRACT.md and existing persistence decisions.
- Decision: One versioned account/run aggregate, explicit obligation stages, revision-checked commands and durable idempotent receipts. Distinguish committed outcome from animation; ambiguous irreversible actions remain pending.
- Rationale: Remove implementation ambiguity around repeated debit/reward and account/session swaps.
- Affects: PERSISTENCE, ARCHITECTURE, TESTING, DEVELOPMENT_PIPELINE.
- Exceptions/dependencies: Supported filesystem durability still requires cross-platform crash tests. No production implementation or anti-cheat guarantee.
- Supersession: Refines proposed persistence contract; changes no confirmed gameplay.
- Acceptance criteria: Pass every crash fixture in STATE_CONTRACT before accepting persistence as durable.

## WB-D078 - Compare conversion hypotheses without locking balance

- Status: BALANCE_TBD; delegated experiment
- Authority/source: Weekend master assignment, 2026-10-09; simulation/weekend_sweep.py.
- Decision: Compare 25k, 100k and 250k GP per spin with the existing minimum-one-spin floor across six documented parameter profiles. Preserve all confirmed special-task charges and minimum-item acquisition rules.
- Rationale: Separate economy sensitivity from a user-approved conversion schedule.
- Affects: ECONOMY, SIMULATION_RESULTS, OPEN_QUESTIONS.
- Exceptions/dependencies: Guaranteed synthetic acquisition, simplified access and incomplete sinks remain model assumptions. Profiles are parameter variants, not new quest/combat-rush decision policies. Floor/denominator are not approved.
- Supersession: None; historical H1 and corrected H2 outputs retained.
- Acceptance criteria: Record exact count, seed, source hashes and ledger assertions; publish only actually executed results.

## WB-D079 - New content drafts remain separate from historical recovery

- Status: DELEGATED
- Authority/source: Weekend master assignment authorizes new gameplay/content design; 2026-10-09 source inspection of RuneLite ItemID/Quest and repository BossDifficulty.
- Decision: Author a new 100-entry item bounty draft and 50 punishment templates; preserve stable WB-B/WB-P IDs, proposed rewards and detector gates. Retain 67 repository boss category mappings and 213 quest enum records as source metadata, not an approved mode roster or main-quest pool.
- Rationale: Continue independent work without inventing the missing historical catalogs or misclassifying runtime metadata.
- Affects: CONTENT_CATALOGS, CATALOG_ACCEPTANCE, BOUNTIES, PUNISHMENTS, PROGRESSION, TRACEABILITY.
- Exceptions/dependencies: Item identities are source-checked; rarity, source routes and live detectors require entry-level review. All unvalidated entries runtime-disabled. New FP/duration bands BALANCE_TBD. No claim that new content is historically approved.
- Supersession: None; missing original catalogs remain documented.
- Acceptance criteria: Unique stable IDs; source identity checks; runtime gates; positive/negative/paused traces before enabling an entry.

## WB-D080 - Punishment pool can contain fewer than twelve valid outcomes

- Status: DELEGATED
- Authority/source: Master assignment says roughly twelve eligible outcomes; new catalog eligibility analysis 2026-10-09.
- Decision: Show up to twelve distinct eligible templates, using a smaller honest pool where necessary. Unsupported methods and impossible routes do not fill the wheel. Zero eligibility is a generator failure and pending obligation, not a player penalty.
- Rationale: Early starting skills alone do not guarantee twelve verifiable distinct templates; prevent artificial repetition and deadlocks.
- Affects: PUNISHMENTS, WHEELS, UI_UX, TESTING, H3 simulation.
- Exceptions/dependencies: No reroll or automatic committed-outcome relaxation. Pause remains immediate/free. Exact recovery from an already committed defective template remains a significant unresolved choice.
- Supersession: Refines roughly-twelve display count, not fifty-template intent or spin costs.
- Acceptance criteria: Test pool sizes zero, one, eleven, twelve and above twelve; persist chosen candidate set and outcome; never enable an unverified detector to inflate size.

## WB-D081 - Correct current Sailing and Master CA facts

- Status: CONFIRMED source evidence, not a new gameplay approval
- Authority/source: Jagex Sailing launch statement 2025-11-19; RuneLite Skill API checked 2026-10-09; Jagex Combat Achievements reward list 2022-11-01.
- Decision: Sailing is live and exposed as Skill.SAILING. Master CA reward is Ghommal's Hilt 5; Hilt 4 is Elite. Use current client threshold, not an old hardcoded points total.
- Rationale: Resolve obsolete future-skill and incorrect reward-number wording through primary sources.
- Affects: PROGRESSION, GRAND_FATES, SOURCES and integration catalog.
- Exceptions/dependencies: Sailing methods, by-products and access routes still require per-template validation. Hilt possession alone is not a fresh Master-tier receipt.
- Supersession: Any older future-Sailing/Hilt-4 interpretation is superseded by checked facts; Grand Fate goal unchanged.
- Acceptance criteria: Client supports Sailing enum; Master threshold detector distinguishes preexisting reward, active earned progress and retroactive task updates.

## WB-D082 - H3 catalog-aware policy comparison

- Status: DELEGATED experiment; BALANCE_TBD
- Authority/source: Weekend master assignment; catalog_model.py and catalog_trials.py executed 2026-10-09.
- Decision: Execute 12,000 synthetic trajectories across eight distinct policies using named bounty/reward and plain-XP punishment catalogs. Preserve H1/H2/sweep outputs, source hashes and explicit limitations.
- Rationale: Test minimal-bounty, combat/quest preferences and catalog generation independently of missing original history.
- Affects: ECONOMY, SIMULATION_RESULTS, TESTING and catalog acceptance.
- Exceptions/dependencies: Acquisition/drop rates, supply routes, boss/quest durations and tier advancement remain proxies. No full real-content or Grand Fate progression claim; no live detector is enabled. H3 duration bands differ from H2, so comparisons are not single-variable effects.
- Supersession: None; no final balance promoted.
- Acceptance criteria: Exact count/seed/hash recorded; ledger and replay checks pass; no-bounty policy has zero bounty income; unsupported punishment families excluded.

## WB-D083 - Provisional balance and recovery journeys

- Status: DELEGATED specification; numerical values BALANCE_TBD
- Authority/source: Weekend master assignment, 2026-10-09; H2/H3 evidence and preserved confirmed pricing structures.
- Decision: Publish BALANCE_CANDIDATE and UX_RECOVERY_FLOWS with explicit provisional costs/targets, local-only trust boundary, irreversible-action readiness and honest unavailable/pending states.
- Rationale: Supply a configurable implementation starting point without overfitting incomplete models or concealing telemetry failures.
- Affects: ECONOMY, UI_UX, FATE_CARDS, ACCOUNT_ACCESS, PERSISTENCE, TESTING.
- Exceptions/dependencies: New boss target/reward candidates not yet run in H3; GP conversion, exhausted activity policy and full-Coffer escape remain OPEN. Current confirmed pause and Pardon rules unchanged.
- Supersession: No approved mechanics overwritten; provisional table does not rewrite historical simulation parameters.
- Acceptance criteria: Display correct price/odds and state-specific recovery; never fabricate detection, backfill paused credit, replay a trade or ask for another ambiguous donation.

## WB-D084 - Distinct repeatable daily pool and exchange evidence

- Status: DELEGATED; quantities/rewards BALANCE_TBD; live validation required
- Authority/source: Weekend master assignment, 2026-10-09; ItemID source and RuneLite LootTracker Font/Unsired handler.
- Decision: Author 18 repeatable daily candidates separately from rare permanent bounty targets. Preserve proposed rolling board/once-only claim rules, no offline backlog and no mandatory daily gate. Rare reward exchange uses a distinct adapter and proposed eligible parent-drop quantity provenance.
- Rationale: Avoid infeasible one-day rare grinds and false acquisition from inventory movement or old-item conversion.
- Affects: ACQUISITION_EVIDENCE, BOUNTIES, PERSISTENCE, TESTING, catalogs/daily_bounties.json.
- Exceptions/dependencies: All runtime templates disabled pending real routes/traces. Daily income not simulated in H3. Unknown provenance is not a violation; no retroactive paused credit. Parent-provenance rule is scoped to rare-drop exchanges, not universally imposed on all crafting.
- Supersession: Refines WB-D056 daily-board proposal; no historical approval claimed.
- Acceptance criteria: Three distinct offers, boundary/rollback/offline tests, source ownership, duplicate settlement, manual claims and no daily progression requirement.

## WB-D085 - Paused item availability and empty-candidate recovery

- Status: DELEGATED clarification of CONFIRMED WB-D002/057 interaction
- Authority/source: Current user rules: pause freely, paused gains remain available without Wheelbound credit; acquire a new eligible ≥10k item when no forced-sacrifice candidate exists.
- Decision: In that empty-candidate exception, a paused-acquired eligible item may be validated after resume and actively donated. No acquisition XP/drop/bounty credit is backfilled; the active verified donation settles the already pending obligation.
- Rationale: Do not reinterpret free pause or actual item availability into an unapproved item-source restriction or impossible access recovery.
- Affects: DEFY_FATE, UX_RECOVERY_FLOWS, TECHNICAL_SPECIFICATIONS, EDGE_CASES, TESTING.
- Exceptions/dependencies: Scope limited to WB-D057, not arbitrary replacement of a nonempty frozen list. Blessed exclusions, per-item ≥10k eligibility, one debit and actual credit receipt remain required. No automatic account unlock.
- Supersession: Refines earlier proposed recovery attribution; historical blanket paused-item exclusion is not introduced.
- Acceptance criteria: Paused acquisition gives zero XP/drop/bounty FP, then one active verified donation settles once; nonempty snapshot cannot be overwritten through the exception.

