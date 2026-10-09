# Executed simulation results

Status: current-rule H2 offline hypothesis model, not final OSRS account balance. Historical H1 results are preserved below and in their original JSON files.

## Current-rule H2 reproduction

`python3 docs/wheelbound/simulation/checks.py`
`python3 docs/wheelbound/simulation/model.py --accounts-per-strategy 4000 --output recovery_results.json`
`python3 docs/wheelbound/simulation/sensitivity.py --accounts-per-strategy 4000`

H2: 12,000 baseline accounts plus 36,000 matched-seed sensitivity accounts; 48,000 new trajectories, in addition to the 48,000 historical H1 trajectories. Seed 20261009, 300-event horizon. Both special tasks consume one spin; empty forced Sacrifice acquires a synthetic eligible 10,000 GP item then donates it. Acquisition duration is 1-3 hours divided by strategy efficiency; no normal Fate/FP reward or vendor unlock. Duration and guaranteed availability are hypotheses. Parameters/results are in recovery_results.json and recovery_sensitivity_results.json; source hashes match the executed model.

| Strategy | Completions p50 | Hours p50 | FP p50 | Defies p50 | Acquisition recovery incidence | No-item termination |
| --- | --- | --- | --- | --- | --- | --- |
| balanced | 280 | 304.76 | 13721 | 4 | 1.18% | 0.00% |
| focused | 276 | 196.556 | 9113 | 4 | 2.08% | 0.00% |
| inefficient | 249 | 308.716 | 4671 | 4 | 2.23% | 0.00% |

## H2 sensitivity

| Scenario | Strategy | FP p50 | Defies p50 | Acquisition incidence | Recovery hours p50 / p90 |
| --- | --- | --- | --- | --- | --- |
| higher_selected_prices | balanced | 1825 | 4 | 1.05% | 0.0 / 0.0 |
| higher_selected_prices | focused | 2480 | 4 | 1.88% | 0.0 / 0.0 |
| higher_selected_prices | inefficient | 2713 | 4 | 3.88% | 0.0 / 0.0 |
| low_reserve | balanced | 2769 | 10 | 69.60% | 7.416 / 26.423 |
| low_reserve | focused | 7863 | 10 | 82.90% | 9.446 / 23.267 |
| low_reserve | inefficient | 3808 | 10 | 83.90% | 20.661 / 50.93 |
| slow_acquisition | balanced | 13721 | 4 | 1.18% | 0.0 / 0.0 |
| slow_acquisition | focused | 9113 | 4 | 2.08% | 0.0 / 0.0 |
| slow_acquisition | inefficient | 4671 | 4 | 2.23% | 0.0 / 0.0 |

low_reserve uses 25 starting/25 refill spins; slow_acquisition uses a 3-9-hour base recovery duration; higher_selected_prices doubles the same selected H1 prices. Every H2 scenario retains the confirmed one-spin cost for both specials and minimum-item recovery. No scenario tests removing an approved cost.

## H2 findings and limits

- Empty candidates now enter mandatory acquisition recovery, rather than terminate. Zero modeled no-item terminations follow from guaranteed synthetic acquisition; they do not prove feasible real-game access, bank completeness or legal item routes.
- Low reserve raises recovery burden and Defy frequency substantially. Keep starting/refill numbers provisional until real acquisition routes and player duration data are validated.
- Slow acquisition changes time cost; this 300-event model does not feed fatigue/abandonment back into player decisions. It cannot establish player retention.
- FP surpluses remain under incomplete sinks/rewards. The tested prices and minimum-one-spin conversion remain hypotheses. A 10,000 GP recovery sacrifice nets zero spins under that conversion after its one-spin debit; the conversion itself is not approved.
- Seeded choice pools are sorted; replay passes across two process hash seeds. Slow acquisition preserves matched-seed FP/completion/Defy results exactly and changes only duration.
- Focused checks pass wheel invariants, seeded replay, ledger identity, debt earnings, empty-item recovery and reserve-exhaustion sensitivity. No RuneLite code, persistence or live game detectors were tested.

## Historical H1 evidence (superseded mechanics)

The following runs predate WB-D057/WB-D058. H1's no-item terminations and no-special-debit baseline are historical diagnostics, not current game rules. Original results.json and sensitivity_results.json are preserved. H1 used an unordered set for one seeded choice pool: its cross-process replay is not guaranteed. H2 fixes this ordering and adds a cross-process hash-seed check; H1 numbers remain historical diagnostics. To reproduce H1 exactly, use source and parameters from commit f5137e9ecdbf18ea8136907e36d17e7e7f464ee1; the commands below in the historical section describe that revision.


Status: executed offline hypothesis model, not final OSRS account balance.

## Reproduction

`python3 docs/wheelbound/simulation/checks.py`
`python3 docs/wheelbound/simulation/model.py --accounts-per-strategy 4000`
`python3 docs/wheelbound/simulation/sensitivity.py --accounts-per-strategy 4000`

Baseline: 12,000 account trajectories (4,000 per strategy). Three sensitivity scenarios: 36,000 more, total 48,000. Seed 20261009 with strategy offsets of 1,000,000. Horizon: 300 assignment events, early stop on no eligible sacrifice. No Grand Fate completion-time claim. Raw parameters/results and source hashes live in simulation/.

## Baseline H1

| Strategy | Ordinary completions p50 | Hours p50 | FP p10 / p50 / p90 | Defies p50 | No-item stop | Any FP debt |
| --- | --- | --- | --- | --- | --- | --- |
| balanced | 280 | 305.002 | 11825 / 13756 / 15621 | 4 | 0.75% | 73.05% |
| focused | 277 | 195.864 | 7800 / 9159 / 10609 | 4 | 1.27% | 52.83% |
| inefficient | 249 | 307.549 | 2360 / 4696 / 6920 | 4 | 1.60% | 98.22% |

| Strategy | Income p50 | Spend p50 | Unlock count p50 | Special weight share p50 | GE acquired |
| --- | --- | --- | --- | --- | --- |
| balanced | 34626 | 18650 | 15 | 7.1% | 99.95% |
| focused | 24820 | 14975 | 15 | 14.0% | 99.90% |
| inefficient | 27633 | 14350 | 14 | 15.2% | 98.95% |

## Matched-seed sensitivity

| Scenario | Strategy | FP p50 | Defies p50 | Special share p50 | No-item stop |
| --- | --- | --- | --- | --- | --- |
| higher_selected_prices | balanced | 1838 | 4 | 7.1% | 1.00% |
| higher_selected_prices | focused | 2546 | 4 | 14.0% | 1.40% |
| higher_selected_prices | inefficient | 2649 | 4 | 15.2% | 3.10% |
| low_reserve | balanced | 2141 | 8 | 11.5% | 58.65% |
| low_reserve | focused | 1778 | 6 | 19.3% | 73.83% |
| low_reserve | inefficient | 724 | 3 | 20.0% | 76.78% |
| special_debit | balanced | 13718 | 4 | 7.1% | 1.12% |
| special_debit | focused | 9102 | 4 | 14.0% | 2.08% |
| special_debit | inefficient | 4640 | 4 | 15.2% | 2.23% |

low_reserve changes initial spins and refill from 100/50 to 25/25. special_debit charges one spin on Taint/Sacrifice landing; H1 does not. higher_selected_prices doubles Tier I/II unlocks, vendor base, GE, Cleansing, Pardon and storage/return; fixed enhancement prices are unchanged. Scenario parameters are embedded in sensitivity_results.json.

## Findings and proposed adjustments

1. Do not finalize a small reserve/refill while forced Sacrifice can produce an empty candidate set. H1 low-reserve no-item stop rates are 58.65%-76.78%; these are synthetic model rates, not measured player outcomes. WB-D057 subsequently approved acquisition recovery with a 10,000 GP minimum; these historical stop rates describe entry into recovery, not deadlocks under the current rule. Recovery time/cost has not yet been simulated.
2. H1 produces large end-horizon FP surpluses. Income/spending sinks are incomplete (daily/CA/diary rewards and blessings not modeled). Raising prices alone is not enough evidence for final balance; keep numeric tables BALANCE_TBD.
3. Lesser Bossing pays base FP for fewer kills by confirmed rule. Optimal efficiency choice tends to select it when offered; preserve that benefit while examining unlock timing and target bands.
4. Special spin debit changes corruption/stop exposure. WB-D058 subsequently confirms Taint and Sacrifice each cost one spin. Model acquisition recovery with those charges before final spin recommendations.
5. FP debt does not itself prevent valid earnings in the model. No optional purchase creates debt. All trajectories repeatedly assert wheel five/three/24 constraints and normalized probabilities.

## Scope and limitations

This is a multi-system progression model, not a verified real-quest/gear/drop catalog. XP level curve is computed; skill rate endpoints, level-scaled targets, synthetic boss durations, GP loot, supply costs, gear prices, item availability, quest XP, rejection/violation rates and bounty chances are hypotheses. XP/time and FP estimates do not establish actual playthrough length.

Includes two distinct independent-proposal targets, same-type offers, unlocked card types, third offering, Lesser/Greater effects, finite spins, purchases, duplicates, fixed enhancements, Satchel churn/displacement, negative FP, GE/vendors/Pardon, Defy/Taint, voluntary/forced sacrifice, synthetic quest/boss tiers, equipment budget and permanent-item bounty proxy.

Not simulated: complete skill-method requirements; exact boss/quest catalogs; all 100 item bounty definitions; 50 punishments (generic duration proxy instead); daily, CA and diary payouts; blessings; Satchel upgrades; pause/logout/live event attribution; crash-safe receipts; Grand Fate learning/win probability; hard menu blocking. These require additional data/live tests. No Coffer acceptance is inferred from this script.

