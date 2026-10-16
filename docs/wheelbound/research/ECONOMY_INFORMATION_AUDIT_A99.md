# Economy study information boundary audit — A99

Confirmed against predecessor83aa7f58b37bc16740b63dbc104cd22566a119b6. This is an audit of a standalone research model, not a production bug or a new game rule.

The A87 model's `card()` draws a specific creature and item and places the final hunt's `reward` and `mean_hours` in each offer. The shortest and highest-FP choice policies inspect those exact values. Highest-rank also uses the hidden reward as its tie-breaker. This gives the simulated player information unavailable under WB-D135/136/149: the player chooses a visible Challenge rank card, then the creature/item is drawn and revealed. Two cards of the same rank cannot be compared by their future hidden target.

A87's43 scenarios/86,000 synthetic trials were executed, but their choice-dependent numeric outcomes must not be interpreted as compliant player forecasts or used to freeze prices. Its accounting/replay checks validate their stated historical model, not this missing information boundary. Preserve source/results unchanged so the error and correction remain reviewable. Exact hashes and finding are in [audit record](../../../automation/challenge_ranks/economy/AUDIT_A99.json).

The approved ownership-versus-readiness distinction and optional lower-rank controls stand independently. The model's chosen prices, ordinary income, activity share and access remain assumptions. No empirical throughput, actual account progression or full-mode validation follows from the correction.

Next A100: create a separate version with a visible rank/public pool summary, choose using only that information, and draw the creature/item from independently committed per-offer randomness after selection. A player may estimate average effort/reward from published pool odds, but cannot inspect the realised target. Test hidden-randomness noninterference and same-rank ties, then execute a bounded comparison with unchanged price hypotheses. Do not add UI requirements or change the gameplay contract to fit the simulator.
