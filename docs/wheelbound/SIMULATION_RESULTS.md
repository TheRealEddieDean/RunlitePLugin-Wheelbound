# Executed simulation results

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

1. Do not finalize a small reserve/refill while forced Sacrifice can produce an empty candidate set. H1 low-reserve no-item stop rates are 58.65%-76.78%; these are synthetic model rates, not measured player outcomes. Fallback requires a core gameplay decision.
2. H1 produces large end-horizon FP surpluses. Income/spending sinks are incomplete (daily/CA/diary rewards and blessings not modeled). Raising prices alone is not enough evidence for final balance; keep numeric tables BALANCE_TBD.
3. Lesser Bossing pays base FP for fewer kills by confirmed rule. Optimal efficiency choice tends to select it when offered; preserve that benefit while examining unlock timing and target bands.
4. Special spin debit changes corruption/stop exposure; decide rule before final spin recommendations.
5. FP debt does not itself prevent valid earnings in the model. No optional purchase creates debt. All trajectories repeatedly assert wheel five/three/24 constraints and normalized probabilities.

## Scope and limitations

This is a multi-system progression model, not a verified real-quest/gear/drop catalog. XP level curve is computed; skill rate endpoints, level-scaled targets, synthetic boss durations, GP loot, supply costs, gear prices, item availability, quest XP, rejection/violation rates and bounty chances are hypotheses. XP/time and FP estimates do not establish actual playthrough length.

Includes two distinct independent-proposal targets, same-type offers, unlocked card types, third offering, Lesser/Greater effects, finite spins, purchases, duplicates, fixed enhancements, Satchel churn/displacement, negative FP, GE/vendors/Pardon, Defy/Taint, voluntary/forced sacrifice, synthetic quest/boss tiers, equipment budget and permanent-item bounty proxy.

Not simulated: complete skill-method requirements; exact boss/quest catalogs; all 100 item bounty definitions; 50 punishments (generic duration proxy instead); daily, CA and diary payouts; blessings; Satchel upgrades; pause/logout/live event attribution; crash-safe receipts; Grand Fate learning/win probability; hard menu blocking. These require additional data/live tests. No Coffer acceptance is inferred from this script.

