# H4 recovered-rule core results

Executed 2026-10-10. Contract was committed before execution at 3d01536e830ef4d9f0e6733ea95b77c4f62fa5b8; model and meaningful fixtures at 3489346886b0d6debb53643f4fd6dca424da95fd. No production plugin code changed. H1-H3 inputs and outputs remain preserved.

## Actual execution and reproduction

80 smoke trajectories followed by 4,000 baseline trajectories (1,000 per scenario), seeds 2026101001-2026102000, approximately 302 seconds baseline execution. Historical 126,000 plus this 4,080 equals 130,080 recorded synthetic trajectories; 80 are smoke tests, not an additional independent balance study.

Run `python3 docs/wheelbound/simulation/h4/checks.py`. Reproduce the baseline with `python3 docs/wheelbound/simulation/h4/run.py --accounts 1000 --output <new-results-path.json>`; existing outputs are protected against overwrite. JSON records model/config/input SHA-256 hashes; CSV contains all 4,000 per-seed results. RESULT_VALIDATION.json records source/input/CSV hash checks, seed ranges, scenario stop counts and aggregate-to-CSV verification. Every executed account asserted FP and spin ledger identities. Fixtures cover crash-restored random assignments, separate card pools, completion audit, kill-XP exemption, Penance above two hours, random owned-item selection, unknown ownership, empty-item recovery and full-wheel displacement.

## Observation window results

The 400 Master-draw and 180-hour horizons are experimental observation limits, never a game deadline or Grand Fate estimate. Careful/risk labels are synthetic violation and card-selection policies, not measured player categories. Audit reward retain/withhold is an unresolved interaction, tested without adopting either.

| Scenario | FP median | Hours median | Ordinary completions median | Penance assignments median | Longest Penance observed | Stop counts |
| --- | --- | --- | --- | --- | --- | --- |
| careful_retain | 2410 | 77.32 | 370 | 22 | 2h | 1000 DRAW_HORIZON |
| careful_withhold | 1972 | 77.61 | 370 | 23 | 2h | 1000 DRAW_HORIZON |
| risk_retain | 210 | 137.85 | 344 | 115 | 16h | 1000 DRAW_HORIZON |
| risk_withhold | 84 | 134.37 | 341 | 114 | 32h | 996 DRAW_HORIZON; 3 BLOCKED_Q6_EXHAUSTED_QUEST; 1 PENDING_FATE_HORIZON |

## Findings for design review

Withholding audited rewards reduces median FP from 2,410 to 1,972 in the careful policy and from 210 to 84 in the risk policy. These are portfolio comparisons: matching seeds cease to produce identical assignments once affordability changes purchases. There is no causal claim that the reward-order change alone explains every difference. Risk-withhold mean boss-node spending falls from 16,437 to 11,100 FP compared with risk-retain; card and mastery purchases also decline. This justifies reviewing audit settlement before tuning prices, not choosing a final fine or reward order automatically.

106 of 1,000 accounts in each risk scenario receive Penance longer than two hours; one risk-withhold account reaches a 32-hour obligation. The old universal two-hour experiment cap would hide this tail. The 0.5-hour base and doubling are experiment candidates, not historically approved thresholds or a production recommendation. Clean completion resets consecutive severity. No automatic third-offense level-99 punishment was added.

Three risk-withhold trajectories stop at BLOCKED_Q6_EXHAUSTED_QUEST rather than inventing automatic slice removal or exemption from the five-ordinary/three-activity rule. Their finite 40 quest tokens are a synthetic proxy, not the real quest catalog. One stops with a pending Fate at the observation horizon; this is not a canceled assignment. The remaining 3,996 reach the draw horizon. There were 192 empty-item recovery events across the batch; no extra Master spin was charged for acquisition and donation beyond the special landing allocation.

## Scope and limitations

This is a recovered-rule core model, not a complete real-content account simulation. It includes separate card pools, Standard-before-boss ordering, random persistent boss selection, tier-only enhancement prices, activity duplicate counters, wheel displacement, end audits, consecutive Penance, Taint/Sacrifice allocation and random unblessed owned-item recovery. Purchase prices, XP/hour, kill duration, violation rates, supply availability and owned-item arrival rates are synthetic candidates.

Vendor/GE progression, Pardon purchasing, complete supply/cash budgets, real boss/quest access, real drop rates, bounty/daily/CA/diary income and Grand Fate completion are excluded. Blessed-type purchases are charged but their full ownership-protection benefit is not modeled. Bounty-family prices remain unset; Q10 rejection/bounty settlement is not resolved by omitting that income. Full/unsupported Coffer cases remain pending technical/policy design boundaries, not executable live donation guarantees. Frozen live receipt traces and real account observations are required before adopting balance numbers.

No material gameplay decision is inferred from these results. Keep audit settlement, Q6 pool exhaustion and Q10 bounty disposition open; use the core to select later experiments after those decisions or actual evidence become available.
