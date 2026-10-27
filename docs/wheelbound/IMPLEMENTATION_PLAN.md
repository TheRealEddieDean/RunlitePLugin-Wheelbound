# Implementation plan

## Current authority — WB-D158, 2026-10-27

The user permanently lifted the documentation/research-only and no-production-code restrictions and requested implementation from the beginning of the planned pipeline. Production Java, tests, build/CI and required existing-plugin integration are authorized. This supersedes older “no implementation authorized”, “no production changes” and “wheelbound-mode exclusively” statements as standing restrictions throughout the repository; historical statements about work actually performed remain historical evidence. Use isolated implementation branches based on wheelbound-mode and PRs targeting wheelbound-mode. Preserve master/main. This does not approve unresolved gameplay/balance rules, waive concrete client/policy evidence gates, authorize messaging others or authorize Plugin Hub submission. Do not repeatedly ask for implementation permission.

Current sequence: WB-I00 build evidence, WB-I01a lifecycle foundation, then WB-I01b durable account-separated aggregate/journal and replay fixtures. UI theme work follows the foundational sequence at the user’s request.


Current project delivery status: [workstreams](../../automation/WORKSTREAM_STATUS.md). Earlier completed checkpoint/checklist items describe their stated scope; they do not mean the mode is implemented. Latest DECISIONS and explicit supersessions control older question wording.
Implementation is authorized by WB-D158. Planned phases:
1. Feasibility and catalogs; validate API/policy/OSRS facts and identify advisory-only restrictions.
2. Account state/journal/schema migrations and crash fixtures.
3. Weighted Master wheel, edits, Satchel, duplicate/enhancement pricing.
4. Frozen skilling/combat/quest/boss generators/cards and audit attribution.
5. FP/shop/tree/vendor/bounty rules.
6. Defy/Taint/Sacrifice/Blessings/Punishment/Reject.
7. Grand Fate detectors/attempt/checklist/win.
8. RuneLite UX, performance, accessibility, live acceptance and Hub review.

Each task references decision IDs; unresolved core conflicts cannot be silently selected by coding agent. Simulation harness is design tooling, not plugin implementation.

## Preconditions before gameplay coding
Confirmed mechanics are implementable requirements; UNVERIFIED history needs source recovery, and PROPOSED details remain distinct. Implement the approved 10,000 GP acquisition recovery and one-spin Taint/Sacrifice rules in future design tooling; apply the confirmed one-spin Sacrifice charge before final balance acceptance. Import/approve item, punishment, quest and boss catalogs. Current technical report establishes API availability, not tested Coffer proof or policy acceptance.

Each phase ends with domain invariants plus live detector evidence where applicable. Do not claim Plugin Hub approval from existing code or available consume API. PDF generated from this directory is a checkpoint while major OPEN items remain.

## Research checkpoint 2026-10-09
Source/API and original-rejection audit is recorded in [POLICY_AND_FEASIBILITY](POLICY_AND_FEASIBILITY.md). Begin future implementation with passive observation/local domain state. Restrictive player menus have a policy conflict; shop/GE interception and Coffer proof remain unresolved. Obtain specific policy clarification before implementing controls. No code, live-client test, maintainer message or submission is included in this research phase. Final acceptance requires more than the existing randomizer's prior merge.


## Ranked Challenge integration — A69–A73

[Current readiness and continuation](CHALLENGE_RANK_READINESS.md) maps WB-D147 confirmed topology and WB-D148–150 delegated controls/offers/shared-target purchases to contracts, synthetic checks and remaining content/client gates. Run automation/challenge_ranks/check_contract.py for exact mathematical/transaction-model invariants; this is not client/durable-IO proof. V2 bundle/quotes/prices and source admission remain unfinished; production implementation is not authorized.
