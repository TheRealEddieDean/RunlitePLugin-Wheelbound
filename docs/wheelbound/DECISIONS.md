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

- Status: PARTIALLY RESOLVED - Taint CONFIRMED; Sacrifice OPEN
- Authority: Explicit user approval, 2026-10-09, limited to Taint.
- Evidence: "yes a tainted task should eat up a spin".
- Decision: A Taint task consumes exactly one Master Wheel spin. Its resulting mandatory Punishment does not create a second spin charge. Sacrifice spin consumption has not been explicitly answered. Ordinary completion/rejection spends one and Grand Fate attempts none remain confirmed.
- Rationale: Taint spends the finite progression resource in addition to imposing a Punishment.
- Affects: DEFY_FATE, GAME_RULES, PUNISHMENTS, ECONOMY, PERSISTENCE, TESTING and SIMULATION_RESULTS.
- Exceptions/dependencies: Charge exactly once across restart/replay. Proposed debit timing is when the Taint outcome and obligation are durably committed. H1 charges neither special and the prior sensitivity charges both; neither is a final model of the newly confirmed mixed rule.

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

