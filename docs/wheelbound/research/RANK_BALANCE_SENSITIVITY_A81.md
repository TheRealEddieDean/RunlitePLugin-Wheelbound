# Candidate rank prices and family exposure — A81

Research only; **no rank/product prices or numeric family frequency adopted**. Reproduce with `python automation/challenge_ranks/balance_sensitivity.py`; exact fractions and64 assertions over32 exposure scenarios are in automation/challenge_ranks/BALANCE_SENSITIVITY_A81.json.

## Price schedules to compare

| Candidate | Lesser | Standard | Greater | Superior | Total |
| --- | ---: | ---: | ---: | ---: | ---: |
| Compact |150 |300 |600 |1200 |2250 |
| Middle trial |250 |500 |1000 |2000 |3750 |
| High |400 |800 |1600 |3200 |6000 |

The four draft Lesser quotes average137.5FP under a synthetic uniform four-source accessible pool. Total candidate costs equal16.36,27.27 or43.64 of that reward-equivalent respectively. These are **not projected completion counts, playtime, actual accepted assignment mix or preentry funding**: Lesser hunts cannot finance the first Lesser purchase before it is owned. Normal Fate income, item bounties, expenses, available spins/Defy and actual player card choices remain outside this bounded study. A three-key accessible subset has a different mean. Higher-rank source effort/rewards are not represented by the Lesser mean.

Recommendation: retain middle as a trial comparison baseline rather than freeze a production price. It gives a progressive cost shape without silently asserting affordability or net-positive global economy. The next balance packet needs actual ordinary reward/spend/run scenarios and a fuller authored rank roster. Missing reviewed price keeps the product unavailable; never expose a0FP placeholder.

## Family and rank exposure

For independent offer-slot family probability q, N=2/3 slots, and R ready enabled ranks: any Challenge appears with probability1-(1-q)^N; a particular rank appears with1-(1-q/R)^N. Conditional rank probability is1/R. Repeating rank offers is allowed. These describe offered cards, not player-accepted hunts or in-game drop rates.

At illustrative q=1/5, any Challenge is seen36% of two-offer sets or48.8% of three-offer sets. That total stays fixed as more ranks become ready, while each rank's share narrows. If each rank were naively given the old full family weight, q=1/4 and R=4 would raise Challenge-family share to4/7 instead of1/4; WB-D149 explicitly avoids that dilution of ordinary cards.

No numeric q is adopted. Compare1/10,1/5,1/4,1/3 with one through four ranks and two/three slots; preserve full fractions. A zero-ready-rank family is omitted under WB-D149, not represented by an invented q. No first-purchase grant, guaranteed offer, extra slot, refund, reroll or change to normal L/S/G.
