# Initial balance candidate and tuning protocol

Status: DELEGATED working candidate / BALANCE_TBD, 2026-10-09. All numeric values below are new or retained hypotheses, not recovered historical approvals. Confirmed pricing structure and gameplay take precedence. This sheet makes first implementation configurable; it does not claim playtested balance.

## Local economy candidate
| Parameter | Candidate | Evidence and purpose |
| --- | --- | --- |
| Starting FP / spins | 0 / 100 | H2 baseline avoids forcing early acquisition recovery |
| Defy refill | 50 spins | Low-reserve sensitivity increases synthetic recovery burden substantially |
| Ordinary slice add | 25 FP | Distinct from underlying activity unlock |
| Tier I / Tier II unlock | 150 / 350 FP | Within original provisional ranges; unique unlocks count toward gates |
| Skill mastery node | 750 FP plus four Tier II unlocks | Configurable candidate; not yet represented as a separate H3 sink |
| Lesser / Greater / Challenge / Wilderness | 150 / 350 / 500 / 750 FP | Permanent respective eligible pools; not consumable draws |
| Third card | 1,000 FP | Two→three offerings; no guarantee a purchased type appears |
| Satchel expansion 3→6→10→15 | 300 / 750 / 1,500 FP | Capacity upgrades sequential; normal stored-slice fees remain |
| Deactivate / reactivate | 150 / 25 FP | Preserve individual enhancements; destroy free/no refund |
| Enhancements I–IV | 75 / 150 / 300 / 600 FP | Tier-only fixed prices across every slice |
| Duplicate purchases 1–7 per activity | 100 / 250 / 500 / 900 / 1,500 / 2,300 / 3,300 FP | Lifetime per-activity escalation survives destruction |
| Duplicate 8+ | Round up to 50: 3,300×1.4^(index−7) | Candidate tail; no global enhancement surcharge |
| Pardon / Cleansing | 2,000 / 2,500 FP | Unlimited fixed-price recovery; Pardon Banned→Locked |
| Additional Blessed slots | 3,000 then 6,000 FP | First free on first Defy; maximum three types |
| GE | 2,500 FP plus 25 completed ordinary Fates | Permanent one-time unlock; no gamble |
| Vendor category unlock | 100×(1+prior paid category unlocks) FP | Per-category counters; free first-shop interpretation remains UNVERIFIED |
| Reject / archived first audit fine hypothesis | 250 / 100 FP | BALANCE_TBD; 100 FP is a prior experiment assumption, not restored end-audit settlement; Reject forfeits completion reward |
| Punishment reward | 25 FP | Below ordinary reference, enough recovery income without replacing normal progression |

Special-task accounting is confirmed: Taint and Sacrifice consume exactly one spin; derived Punishment consumes no additional spin. Voluntary sacrifice receives no invented task debit. GP→spin formula remains consequential OPEN; do not finalize the floor from this sheet. Minimum 10k eligibility is a separate confirmed game/player rule.

## Reward and target candidates
Normal XP target brackets from the reconstruction seed remain reference bands, not every skill's universal table. For each enabled activity, freeze a target from level, validated rate range, method/resource cost and headroom. Produce at least two distinct legal starting Standard targets; at least three when the upgrade requires it. If a band is too narrow, repair generation before draw rather than repeat identical offers.

Proposed skilling Standard reward: round(40+80×estimated active hours), with an activity-specific cap/risk review. Lesser quantity 0.6× and lower reward; Greater quantity/reward 1.6×. Challenge premium 20% only when its restriction is supported; no premium for a label with no enforced measurable condition. H3 does not model all these card types or authentic player rates.

| Bossing tier | Standard target candidate | Base FP candidate |
| --- | --- | --- |
| Easy | 6–12 kills | 100 |
| Medium | 4–10 kills | 150 |
| Hard | 3–8 kills | 250 |
| Elite | 2–6 kills | 400 |
| Master | 1–4 kills | 550 |
| Grouped raids | 1–3 completions | 700 |

These new boss candidates have NOT been executed in H3. They replace neither the existing normal-wheel tiers nor the reviewed mode roster. Bossing Lesser reduces kills and retains base FP; Greater increases kills and reward. Before offering cards, validate the maximum selectable target against every boss in the committed eligible pool, including Slayer/task/key/supply requirements. Freeze reward before the boss spin; never reprice after a difficult result. Unknown team/instance access prevents offering that encounter rather than granting an implicit unlock.

## What the executed results support
H1/H2/H3 show FP debt can coexist with continued earnings, optional purchases need not require dailies, and low spin reserves increase corruption/recovery frequency. H3 minimal-bounty still progresses by construction, but it does not prove every real account route works. Much higher FP end balances with incomplete sinks do not justify global price inflation. Preserve Lesser Bossing's user-confirmed benefit; tune target bands/base rewards instead of removing it.

Do not interpret a p50 hour value as intended completion time. No target playthrough duration, forced grind count or artificial Grand Fate readiness gate is introduced.

## Next comparison before numbers become defaults
Run a new explicit config revision, leaving historical results immutable. Test per-tier boss candidates, genuine supply/entry costs, mastery/Blessing/Satchel sinks and daily/CA/diary income after those catalogs exist. Compare FP/hour, progression affordability, negative balance duration, forced recovery incidence and time tails across risk/quest/combat/minimal-bounty policies. Use matched seeds where branches allow; acknowledge randomized path divergence. Check analytical costs against ledgers. Do not optimize solely for median final FP or hide rare hardlocks.

Live playtest then validates rates and detector readiness. Promote a value to an implementation default only through a new delegated/approved decision and versioned config; never rewrite historical parameter files to make old simulations seem current.
