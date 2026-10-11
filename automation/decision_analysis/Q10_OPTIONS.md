# Q10 Reject Fate: reward settlement comparison

WB-A28. All four alternatives here are hypothetical. No rule is adopted, no existing reward revoked, and no user approval inferred. Historical S8-M0402's assistant-selected bounty forfeiture wording conflicts with the unqualified always-active/manual-claim interpretation; the latest explicit user instruction confirms completion-reward forfeiture but does not clearly settle independently earned bounty receipts. The canonical Q10 remains open.

All options preserve confirmed Reject FP loss, one consumed assignment spin total, no Fate completion reward and mandatory Punishment. None gives permission to pursue an unrelated bounty, credit paused activity, claim mere possession, reroll a boss or reject the mandatory punishment. Raw acquisition/achievement evidence is retained for audit even in an option that disqualifies a payout.

## The claim-timing problem

Let B be a legitimately qualified current-Fate bounty reward and C the Reject penalty. Ignoring other transactions, the following values are changes in FP after Reject and any attempted bounty claim. They exclude the forfeited completion reward, punishment duration/reward, assignment time and drop probability. Numeric examples in `Q10_COMPARISON.json` are placeholders, not balance recommendations or account simulations.

| Hypothetical policy | Claim requested before Reject | Claim attempted after Reject | Consequence |
| --- | --- | --- | --- |
| Preserve independently qualified bounty reward; forfeit Fate completion only | B−C | B−C | Claim timing does not change payout; rejection can retain bounty income despite forfeiting completion FP and owing Punishment |
| Void only unclaimed receipts linked to rejected Fate | B−C | −C | A player can preserve B by claiming first; UI timing changes economic outcome |
| Escrow current-Fate reward until assignment settlement | −C; claim request is provisional | −C | Avoids timing difference but delays spendable FP and changes existing manual-claim behavior |
| Explicitly claw back already credited current-Fate bounty FP | −C | −C | Avoids timing difference but creates reversal/debt rules and requires explicit approval |

The unclaimed-only policy has a claim-timing difference of exactly B. Increasing the Reject penalty alone does not remove that difference. Making the claim button harder to reach, or hoping players refrain from claiming, is not a settlement rule. Paused claiming of earlier active-earned evidence is separately unresolved; it cannot be used as a shortcut to select Q10.

The saved comparison enumerates72 exact ledger examples across three hypothetical rewards, three hypothetical penalties, four policies and two claim timings. It adds zero balance trajectories and proves no strategy is profitable after time/punishment/access costs. For the preserve-reward option, B>C is merely positive FP before those omitted costs, not evidence of a free exploit.

## Already spent FP makes clawback consequential

Example test inputs: start100 FP, claim500 qualifying FP, spend600 through an otherwise legal voluntary purchase, then Reject with100 penalty. An explicitly approved500-FP clawback would leave−600 FP. The purchase was affordable when made. This hypothetical does not decide whether its unlock/upgrade remains, is reversed, or is handled through debt. Automatically revoking progression or silently inventing a refund is a separate material rule, not an implementation detail supplied by this comparison.

Escrow avoids spending the contingent reward, but changes when bounty FP becomes usable. That can affect access purchases, card upgrades, Cleansing, Pardon and debt recovery during an assignment. Do not quietly convert all manual claims into escrow while presenting it as faithful implementation of existing approval.

## Provenance needed whichever policy is selected

Record acquisition/qualification separately from claim and ledger settlement: account/run, source observation/evidence key, active/paused classification, earning assignment/context, reward key/catalog version, quoted FP, qualification timestamp/revision, claim command/receipt and the selected settlement-rule version. Preserve all raw records and distinguish ineligible, qualified-unclaimed, provisional, claimed, rejected-related and ambiguous states; these are analysis classifications, not new production enum commitments.

In particular, earning during Fate A and claiming during Fate B must not be conflated with earning during B. Before enabling a forfeiture option, specify the earning-context scope explicitly. A failed or repeated claim must never pay twice; rejecting an assignment must not erase the evidence needed to explain the decision later.

For each chosen rule, test both sides of claim→Reject and Reject→claim, lost acknowledgement and restart, prior-Fate receipts claimed during this Fate, purchased rewards already spent, distinct permanent/daily reward keys sharing an event, and active receipts followed by pause. Normal zero-spin/negative-FP behavior remains as already confirmed. The current offline receipt suite deliberately gates final Q10 settlement.

## Subcases the Q10 answer must cover

- Whether permanent item, daily item, per-boss Combat Mastery and diary rewards all follow one rule or have explicit exceptions.
- For aggregate CA/diary qualifications spanning several Fates, whether attribution follows the final qualification event or constituent evidence. Do not pretend the final task proves the entire set was active; CA-MIXED remains independent.
- If an unclaimed receipt is disqualified, whether a later distinct legitimate acquisition can qualify that still-unclaimed goal again. Preserve once-only claim keys; do not invent permanent bounty bans or reward duplicate pickup of the same item as a fresh source event.
- Whether achievements lacking a new observed completion signal can be requalified. Re-earning cannot be promised without detector support.
- If clawback is chosen, how already spent FP and permanent purchases are treated; no rollback is authorized by the existing text.

The existing proposed recommendation in OPEN_QUESTIONS is to preserve independently qualified bounty receipts and forfeit Fate completion rewards. This comparison explains its trade-offs and alternatives but does not promote it to a confirmed rule. Q10 needs an explicit choice before this reward branch is enabled. Other state/identity/provenance work can proceed independently after implementation authorization.
