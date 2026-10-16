# Visible-card economy sensitivity — A100

Separate correction of [A87](RANK_ECONOMY_A87.md) following the [A99 audit](ECONOMY_INFORMATION_AUDIT_A99.md). Executed43 scenarios ×2,000 seeds =86,000 **synthetic account windows**, not actual accounts, full playthroughs or measured game progression. No numeric price, family frequency, reward, UI change or new gameplay policy is adopted.

## Corrected information boundary

A Challenge card shows its rank. The strategy can calculate a rank's expected effort/reward from its known frozen pool odds: average eligible items within each canonical creature, then average creatures. It cannot inspect a future specific hunt. The model chooses using only that public summary, then resolves the selected creature/item from independently committed per-card randomness. Same-rank identical cards tie by first slot; this is a simulation strategy, not a forced player/UI rule. Unselected target outcomes are never evaluated.

`shortest` now means shortest public **expected** effort; `highest_fp` means highest public expected reward; `highest_rank` means visible rank with public expected reward as tie-breaker. No actual duration is known when choosing. Expected effort is a model player's calculation, not a requirement to promise an estimated finish in the UI. The ordinary proxy retains its synthetic visible target quantity/duration assumption; real ordinary activity/card generation remains omitted.

## Executed results

| Scenario | Mean completed | Mean final FP | Superior owned | Superior ready | Unfinished at horizon |
| --- | ---: | ---: | ---: | ---: | ---: |
| baseline | 99.7 | 2226 | 99.8% | 99.8% | 1.5% |
| price_schedule=high | 99.9 | 127 | 99.6% | 17.7% | 0.9% |
| discretionary_fraction=0.5 | 99.9 | 866 | 57.7% | 2.5% | 0.7% |
| enable_policy=highest_only | 97.8 | 2545 | 98.4% | 97.9% | 11.4% |
| choice=highest_rank | 88.7 | 3490 | 96.3% | 95.2% | 54.4% |
| choice=highest_fp | 88.9 | 3517 | 96.4% | 95.2% | 53.6% |

Baseline retains A87's hypotheses:0 starting FP;100 ordinary FP/0.75 reference hours;100-completion cap/100-hour horizon;25% Bossing share;2 offers; family q=0.2; rank prices250/500/1000/2000;500 prerequisite FP;150 per target;25% optional spend; hypothetical full source access. Shortest-reference choices are the baseline strategy. The same43 scenario configurations and source catalogs are preserved. Every numeric environment/price/frequency value remains unadopted.

Owning Superior but lacking source purchases still occurs: high-price scenario99.6% owned versus17.7% ready (see exact JSON for unrounded values). The result supports inspecting the spending path; it does not establish that those prices are affordable. A high-rank/high-reward strategy still leaves many long hunts unfinished at the observation boundary. Those hunts preserve frozen reward and earn nothing yet; they are not failure/retirement/refund events.

A87 and A100 consume randomness differently because targets move behind card choice and get independent streams. Compare scenario-level descriptive results only; matching seed numbers do not produce a paired causal estimate of the information leak. Historical A87 output/model remain unchanged. Do not add both batches together and call them independent evidence for a final economy.

## Verification and limits

1,050 standalone checks cover deterministic replay, FP ledger/affordability, horizon censoring, lower-rank control, shared target grants, creature-first expectation weighting, same-rank ties and hidden-seed noninterference through the complete choice/reveal path. A malformed unselected hidden source is not evaluated. Pool/choice checks do not prove production persistence, live receipts or real access.

Reproduce from repository root:

```sh
python automation/challenge_ranks/economy_visible/checks.py
python automation/challenge_ranks/economy_visible/run.py 2000
```

[Results](../../../automation/challenge_ranks/economy_visible/RESULTS_A100.json) pin inputs/model/runner; [validation receipt](../../../automation/challenge_ranks/economy_visible/VALIDATION_A100.json) pins the check source and output. All runtime flags remain false.

Inherited limitations still matter: ordinary rewards/time and source throughput/access are assumptions; purchases prioritize ranks/sources; ordinary boss-unlock reuse and specific target pricing are not fully represented; the simulation's500FP prerequisite budget does not prove real boss/tree reachability. No Reject/audit/debt/Pause, Defy/Taint/Sacrifice/Grand, bounty income, GP/gear or actual skill/quest progression. It cannot determine a full-mode price schedule. Next work must integrate these spend/income paths from current decisions rather than merge old H1/H4 outputs as if their rules were current.
