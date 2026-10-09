# Economy
100 FP is a reference Standard reward, not guaranteed flat income. Rewards should consider duration, difficulty, restrictions and boss risk, then freeze on assignment. Negative balances are permitted; no artificial upper cap.

Recovered provisional S3 prices (BALANCE_TBD, not final):
| Purchase | FP |
| --- | --- |
| Ordinary slice | 25 |
| Duplicate purchases 1-7 per activity | 100 / 250 / 500 / 900 / 1500 / 2300 / 3300 |
| Enhancement tiers I-IV | 75 / 150 / 300 / 600 |
| Satchel 6 / 10 / 15 | 300 / 750 / 1500 |
| Tier I / II / advanced unlock | 100-200 / 250-450 / 500-1000 |
| Lesser / Greater / Challenge / Wilderness | 150 / 350 / 500 / 750 |
| Third offering | 1000 |
| GE | 2500 plus completion gate |

Missing numeric tables: duplicate 8+, deactivation/reactivation, starting FP/spins, Defy refill, rejection/violation charges, cleansing/pardon/blessings, vendor escalation, GP-to-spin conversion, quest/boss gates and bounty payouts. Preserve as BALANCE_TBD.

Duplicate cost counters are per activity lifetime; enhancement prices independent of counters. Repeated Defy changes special probabilities, not fixed enhancement pricing. Bounty and sacrifice income must not be counted twice. Cash/gear budget must stay separate from FP. No fixed target playthrough duration.

See [SIMULATION_RESULTS](SIMULATION_RESULTS.md).

## User resolutions 2026-10-09
Bossing Lesser pays base FP despite reduced kills; Greater increases kills and FP. Include duration/reward differences in simulation rather than charge identical FP-per-hour. Standard starts unlocked and two independently generated different targets offer choice; model choice policy rather than one random target. Pardon has an unlimited supply at a single expensive fixed price; no lifetime cap, no escalation. Vendor class escalation remains a separate rule.

## Executable hypothesis set H1
All following numbers are PROPOSED/BALANCE_TBD, chosen under delegated design authority on 2026-10-09. They are NOT historical approvals. Machine-readable values are in simulation/parameters.json.

| Parameter | H1 value | Purpose |
| --- | --- | --- |
| Starting FP / spins | 0 / 100 | Tests pressure without forced early Defy |
| Defy refill | 50 | Tests recurring corruption rather than fixed run duration |
| Deactivate / reactivate | 150 / 25 FP | Meaningful storage versus affordable return |
| Tier I / Tier II representative unlock | 150 / 350 FP | Within recovered provisional ranges |
| Reject / audit penalty | 250 / 100 FP | Debt allowed; compare deliberate inefficient play |
| Punishment reward | 25 FP | Recovery income below ordinary reference |
| Cleansing / Pardon | 2500 / 2000 FP | Expensive fixed recovery; no escalating Pardon price |
| Additional Blessed slots | 3000 then 6000 FP | PROPOSED; not modeled by H1 |
| Vendor class base | 100 FP | H1 linear cost 100*(prior paid class unlocks+1) |
| GE | 2500 FP and 25 completed Fates | One-time, non-gamble |
| Sacrifice conversion | max(1,floor(verified item value/100000)) spins | H1 hypothesis; minimum-one rule NOT confirmed |
| Special landing spin debit | Historical H1: none | Current confirmed rule: Taint and Sacrifice each cost one spin. Old H1 lacks both charges |

H1 ordinary Skilling/Combat reward = round(40 + 80*estimated hours), Greater quantity 1.6x, Lesser 0.6x, Challenge quantity 1.25x with 20% reward premium. Boss reference FP = 90 + 20*eligible synthetic boss tier; Lesser keeps it, Greater pays 1.6x. Quest proxy FP = 70 + 30*tier + 30*hours. These formulas are trial economy tools, not normative final reward tables.

Reward estimates are frozen from the offered target and eligibility tier before boss outcome. Actual duration varies without repricing reward. Optional spending checks affordability and creates no debt; rejection/audit deductions may. Reward journal satisfies FP = starting FP + earned - spent - penalties.

Duplicate 8+ extrapolation is a new hypothesis: round up to nearest 50 of 3300*1.4^(purchaseIndex-7). Costs are monotonic per activity lifetime, not current slice count. Every enhancement still uses 75/150/300/600 by tier, independent of that counter.

## Balance acceptance and limitations
Compare FP per hour, earnings/spending and content-unlock counts across strategies, not just final balance. Watch Lesser Bossing's premium FP/hour: it is an intentional user-confirmed benefit; tune pool availability, counts and fixed base reward rather than remove the rule. Greater can favor XP/kills/progress even when FP/hour is lower.

H1 does not confirm real account progression duration. Quest chains, gear prerequisites, actual drop tables, diaries, CAs and skill methods are proxies. H1 models permanent-item bounty income only; daily/CA/diary schedules, blessings and full Satchel upgrade purchases need a catalog-backed second model. No simulation outcome promotes an unverified historical choice to CONFIRMED.

## Current-rule recovery hypothesis H2
Both special tasks spend one spin before the modeled reward. A missing item triggers synthetic acquisition of a 10,000 GP eligible item, with 1-3 hours divided by efficiency (3-9-hour sensitivity). Acquisition has no normal Fate/FP reward and grants no account unlock. Durations and guaranteed item availability are modeling hypotheses, not game rules. The H1 minimum-one-spin conversion remains unapproved: under that hypothesis the minimum recovery item nets zero spins after the Sacrifice debit; this must not be treated as an approved conversion formula.
