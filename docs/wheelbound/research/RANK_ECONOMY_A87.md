# Ordinary-income, ownership and source-access economy study — A87

A reproducible43-scenario/86,000-account **synthetic sensitivity study**,2,000 seeded accounts each, using the authored four-rank core content. It is not a real-account forecast or final economy. Model/source hashes, scenario parameters, summaries and representative ledgers are in automation/challenge_ranks/economy/RESULTS_A87.json. The separate269-assertion suite checks accounting/replay, unquoted horizon censoring, underfloor pools, shared target purchases and explicit lower-rank opt-in. No production code, API or live client was exercised.

## Assumptions, not adopted rules

Baseline:0 starting FP;100 ordinary FP and0.75 reference hours;100-completion spin window/100-hour observation horizon;25% Bossing activity share;2 offers; Challenge family q=0.2; middle candidate rank prices250/500/1000/2000; separate150FP target expansions;500FP ordinary prerequisite budget;25% discretionary income spending; all ranked contexts hypothetically accessible/adapter-supported. The earliest Lesser grant needs ordinary income first. These values derive from prior hypotheses/candidate schedules and are not newly approved starting spins, pricing, activity mix, reward bands or game-stage mappings.

The model buys predecessor ranks in order and sources separately; a rank can remain owned belowfloor. It explicitly opts into owned ranks at legal between-assignment boundaries. Available source lists are hypothetical account profiles, not fetched player data. Other strategies enable only Lesser or only the highest owned rank. Source draws are creature-first/item-second. Same-rank offers can repeat; no guaranteed ordinary escape if every offered card is Challenge.

Actual independent source-model drop opportunities are geometric, with no pity cap or actual-duration repricing. The clock crossing the observation horizon preserves an active assignment and pays nothing for it. Final progression purchases are not processed while that assignment is censored. The model does not implement ordinary rejection/audit/violation/Pause, Defy/Taint/Sacrifice/Grand, bounty income, GP/gear or real account progression. No full-mode completion claim is possible from these results.

## Selected results within the study window

| Scenario | Mean completed Fates | Mean final FP | Superior owned | Superior offering-ready | Active assignment at horizon |
| --- | ---: | ---: | ---: | ---: | ---: |
| baseline | 99.9 | 2205 | 99.9% | 99.8% | 1.1% |
| price_schedule=compact | 99.8 | 3722 | 100.0% | 100.0% | 1.6% |
| price_schedule=high | 99.9 | 128 | 99.5% | 13.2% | 0.8% |
| discretionary_fraction=0.5 | 99.9 | 1013 | 49.6% | 2.5% | 0.6% |
| enable_policy=lesser_only | 100.0 | 2127 | 100.0% | 0.0% | 0.0% |
| enable_policy=highest_only | 98.5 | 2455 | 99.1% | 98.9% | 8.5% |
| choice=highest_rank | 88.9 | 3505 | 96.0% | 95.0% | 52.5% |
| choice=highest_fp | 89.0 | 3529 | 96.0% | 95.0% | 51.9% |

These percentages describe simulated samples under invented comparison inputs, not confidence that a player will reach those outcomes. Two thousand seeds reduce simulation noise; they do not make unmeasured throughput, access or omitted rules true.

## Findings and recommendations

1. **Ownership and usable content must stay separate.** The high-price scenario ends with about99.5% Superior ownership but only13.2% offering readiness: buying source keys afterward can be the bottleneck. At50% discretionary spending, Superior ownership falls to49.6% and readiness to2.5%. Keep the existing cost/grant/access preview; do not promise that purchasing a rank guarantees an offer.
2. **Player choices change the accepted task mix.** Shortest-reference strategies mostly accept Lesser/Standard even when all ranks are owned. Highest-rank/highest-FP choices spend more time in long hunts and leave roughly52% of samples with an unfinished assignment at the100-hour horizon. That is study censoring, not a failure, refund, retirement or proof of a grind cap. Numeric per-slot q is not the accepted-rank distribution.
3. **Retained lower ranks are a real control.** Explicit Lesser-only strategy can retain higher ownership while never accepting higher ranks. This is consistent with the approved optional control, not an exploit to punish with a new rule. Buying ranks and completing them are different player goals.
4. **A flat per-Fate base rewards shorter tasks more per hour.** This follows the existing100FP base and one-spin-per-Fate semantics; it does not justify an unapproved reward multiplier, a forced higher-rank offer, reroll or normalized hourly payout. Retain the approved formula while comparing broader sources/spend/run windows.
5. **Keep candidate prices provisional.** Middle remains a useful trial baseline, not a final economy. Access, discretionary spending and card choice dominate apparently reassuring ownership figures. Actual ordinary XP/KC/quest reward bands and future source expansion prices require their own coherent packet; do not lock prices by treating one model as the game.

## Reproduce

```sh
python automation/challenge_ranks/economy/checks.py
python automation/challenge_ranks/economy/run.py 2000
```

The same model/config/seed stream restores identical account ledgers. Preserve the result version; substantive model/input changes need a new version, not reused success language. Empirical gameplay and actual-client receipt/durable transaction tests remain NOT_RUN.
