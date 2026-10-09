# Game rules
Confirmed: optional self-imposed mode; no official highscores or integrity labels. Preserve normal wheels outside mode. Pause freely without FP penalty or Assisted status. XP, drops, quests and bounty events while paused receive no later credit.

Finite spins: ordinary Fate completion consumes one; rejection consumes the same one with no completion reward. Reserve it on assignment so double-spinning is impossible; debit semantics are a new technical proposal pending validation. No ordinary Fate timer (S3, UNVERIFIED historical approval).

Fate audit (S3): cumulative 1,000 unauthorized XP per Fate tolerance; legitimate incidental HP and unavoidable by-products exempt. Exact allowed-XP attribution, charge frequency and escalating penalty formula are UNVERIFIED/BALANCE_TBD. Do not turn every XP event beyond tolerance into an unbounded penalty. Negative FP permits earning, but optional purchases need sufficient FP.

Grand Fate has no artificial level/playtime gate. Real game prerequisites still apply. No server-state manipulation or automation.

Reject ordinary Fate: explicit warning -> FP loss (can become negative) -> consume spin -> no reward -> mandatory committed punishment -> complete before another normal Fate. Restart never rerolls outcomes. See [PUNISHMENTS](PUNISHMENTS.md).

## Proposed audit formula
Maintain allowed skill/method/location predicates and one cumulative unauthorized XP bucket. Ignore only verified permitted by-products. At first crossing 1000 unauthorized XP, propose one 100 FP charge times min(3,1+prior violation-Fate streak). Record further XP without charging every tick. Legitimate completion resets streak. Pause creates no charge or credit. This NEW PROPOSED calculation differs from H1's flat per-Fate probabilistic charge; attribution/streak not simulated.
