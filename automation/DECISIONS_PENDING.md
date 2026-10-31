## Current local continuation - aggregate-backed terminal reservation preparation, 2026-10-31

IDLE after bounded WB-WHL-08 preparation increment (S). Reconciled source/tests with
schema-18 continuation and WB-D013/018/058/120/138/139/155. Spin state, reservation,
terminal binding and codec already exist; reused them. No I01d. Git/merged state not
independently verified because user prohibits Git operations; supervisor owns publication.

MasterTerminalReservationPreparation prepares terminal history and reserved spin state
from one supplied RunDocument's explicit balance, without a caller balance override.
New append reuses actual pending-prefix binding and exact-head reservation validation.
Legacy absent balance, zero, stale spin revision, absent/shortened pending prefix and
paused new append fail closed. Exact historical replay requires retained terminal history
AND its original reservation; returns original accounting across Pause without rebuilding
or taking another spin. Changed command/payload fails closed. Source remains immutable.
This is a proposal only, NOT typed atomic publication, assignment, obligation, storage
admission or settlement. Master Wheel unchanged; incomplete gameplay unwired/disabled.

Fresh Java 11 source compilation passed into build/continuation-terminal-reservation,
including production dependencies and dependency-free fixtures (no cached fixture reuse).
MasterTerminalReservationPreparationChecks PASSED, including 0/1/513 empty prefixes,
1/7/Long.MAX_VALUE balances, replay, changed payload, unavailable state and immutability.
MasterSpinAggregateChecks, MasterTerminalPreparationChecks, MasterSpinStateChecks,
MasterResolutionBindingChecks and MasterResolutionAggregateChecks also PASSED.
Focused Gradle reservation/codec/JSON/legacy attempt failed before compilation: inherited
wrapper-cache lock Access is denied. JUnit/JSON/codec/migration/legacy NOT_RUN;
schema-18 integration remains IMPLEMENTED_UNVERIFIED. GRADLE_USER_HOME unchanged;
no cache workaround, external inspection or full-suite claim.

Reconciliation: aggregate-backed reservation preparation DONE offline; JUnit wrapper
IMPLEMENTED_UNVERIFIED. WB-WHL-08 PARTIAL; WB-WHL-09 PARTIAL (automatic retries
UNSTARTED); WB-WHL-10 PARTIAL_UNVERIFIED. Typed atomic terminal operation, actual
assignment/obligation publication and locked/session admission remain UNSTARTED.
Other audited statuses and evidence holds stand; no completed gameplay claim.

Next: typed atomic terminal operation publishing actual-prefix-bound history, reservation
and supported assignment/obligation together, with retained exact replay, before locked/
session admission. Reuse this preparation; never infer balance from history. Settlement,
grants, sampling, retry execution, suspension/revalidation and changed-snapshot recovery
remain unfinished. WB-D155 adopts no retirement rule. Run focused Gradle integration and
legacy checks when inherited dependencies become accessible. Local edits only;
supervisor handles commits, PRs, CI and merges.

## Current local continuation - schema-18 Master spin retention, 2026-10-31

IDLE after bounded WB-WHL-08 persistence increment (S). Reconciled source and focused
fixtures against WB-D013/018/058/120/138/139/155. Existing spin model, allocation restore,
aggregate binding and versioned JSON reused. No I01d. Git/merged state not independently
verified because current user prohibits Git operations; supervisor owns publication.

RunCodec schema 18 retains optional masterSpins using MasterSpinStateJson version 1.
Explicit null preserves legacy absence; explicit unreserved zero remains present.
Reserved original balance and full terminal receipt round-trip through model validation
and RunDocument retained-history binding. Schemas 1-17 keep strict original field sets
and restore absent spins; schema 17 rejects the new field. No inferred balance or new
allocation on decode. Updated old-schema migration fixtures to omit masterSpins and
replaced the prior encoding-refusal test with retention coverage.

Java 11 dependency-free domain/aggregate compilation PASSED into
build/continuation-spin-schema (existing dependency-free fixture classes reused).
MasterSpinAggregateChecks, MasterSpinStateChecks, MasterResolutionAggregateChecks,
MasterResolutionBindingChecks and MasterTerminalPreparationChecks PASSED. These checks
do not compile or execute Gson/RunCodec/JUnit integration.
Focused JUnit coverage authored for 0/1/513 empty prefixes, 1/7/Long.MAX_VALUE reserved
balances, unreserved zero/boundaries, exact replay, Pause persistence, schema-17 migration,
missing/malformed/foreign/future/inconsistent spin state and missing/mismatched terminal
history, preserving input bytes. Gradle focused codec/JSON/aggregate/RunCodec/legacy attempt
failed before compilation: inherited wrapper-cache lock Access is denied. Codec/JUnit/
migration/legacy NOT_RUN; schema-18 integration IMPLEMENTED_UNVERIFIED. GRADLE_USER_HOME
unchanged; no cache workaround or external inspection. No full-suite claim.

Reconciliation: structural spin model/aggregate and allocation restore remain DONE offline;
standalone JSON and schema-18 durable codec integration IMPLEMENTED_UNVERIFIED (integration
no longer UNSTARTED). Typed atomic terminal operation, assignment and locked/session
admission remain UNSTARTED. WB-WHL-08 PARTIAL; WB-WHL-09 PARTIAL (automatic retries
UNSTARTED); WB-WHL-10 PARTIAL_UNVERIFIED. Other audited statuses/evidence holds stand.
Master Wheel unchanged; incomplete gameplay remains unwired/default-disabled.

Next: run focused JSON/codec/migration and legacy tests when inherited dependencies are
accessible, then implement typed atomic actual-pending-prefix-bound terminal history,
reservation and obligation publication with exact retained replay before locked/session
admission. Settlement, grants, sampling, automatic retries, suspension/revalidation and
changed-snapshot reconciliation remain unfinished. WB-D155 adopts no retirement rule.
Local edits only; supervisor handles commits, PRs, CI and merges.

## Current local continuation - standalone Master spin JSON payload, 2026-10-31

IDLE after bounded WB-WHL-08 versioned payload increment (S). Reconciled current
spin-state/allocation, aggregate retention, schema-17 refusal and focused fixtures
against WB-D013/018/058/120/138/139/155. Reused existing terminal receipt codec.
Git/merged state not independently verified: user prohibits Git operations. No I01d.

Added MasterSpinStateJson version 1 with explicit identity, revision, allotted balance
and nullable version-1 reservation containing original spinsBefore and full terminal
receipt. Strict keys/numeric parsing, supported versions, identity, head revision and
model invariants fail closed. Allocation.restore retains original accounting without
inventing a current lifecycle or consuming another spin; it establishes no authority.
Explicit unreserved zero remains distinct from enclosing legacy absence. No derived
balance or redundant debit fields. This standalone codec is NOT wired into RunCodec:
schema 17 still refuses present spin state, so no durable-save support is claimed.

Java 11 dependency-free compilation PASSED in build/continuation-spin-json.
MasterSpinStateChecks (including restoration), MasterSpinAggregateChecks,
MasterTerminalPreparationChecks, MasterResolutionBindingChecks and
MasterResolutionAggregateChecks PASSED. New JSON JUnit coverage authored for ordinary/
Taint/Sacrifice, 0/1/513 empty prefixes, 1/7/Long.MAX_VALUE balances, exact replay,
explicit unreserved zero, revision boundaries, malformed/foreign/future/inconsistent
payloads and input immutability. Focused Gradle JSON/state/aggregate/RunCodec/legacy
attempt failed before compilation: inherited wrapper-cache lock Access is denied.
JSON/JUnit/codec/legacy NOT_RUN; new JSON code IMPLEMENTED_UNVERIFIED. No full-suite
claim. GRADLE_USER_HOME unchanged; no external inspection or cache workaround.

Reconciliation: structural aggregate state and allocation restore DONE offline;
standalone versioned JSON IMPLEMENTED_UNVERIFIED, no longer UNSTARTED. RunCodec
schema migration/integration remains UNSTARTED; typed atomic terminal operation and
assignment remain UNSTARTED. WB-WHL-08 PARTIAL; WB-WHL-09 PARTIAL (automatic retries
UNSTARTED); WB-WHL-10 PARTIAL_UNVERIFIED. Existing schema-17 and creation/store/session
integration remain IMPLEMENTED_UNVERIFIED. Other audited statuses/evidence holds stand.
Master Wheel unchanged; incomplete gameplay remains unwired/default-disabled.

Next: integrate optional masterSpins into a new RunCodec schema, preserving strict
schemas 1-17 and legacy absence. Replace refusal only with lossless model/aggregate
validation; add migration, reserved/unreserved round-trip and corruption coverage,
updating older migration fixtures to omit the field. Run focused JSON/codec/legacy
checks when inherited dependencies are accessible. Then typed atomic actual-pending-
prefix-bound history/reservation/obligation publication and retained exact replay before
locked/session admission. Settlement, grants, sampling, automatic retries, suspension/
revalidation and changed-snapshot reconciliation remain unfinished. WB-D155 adopts no
retirement rule. Supervisor handles commits, PRs, CI and merges; local edits only.

## Current local continuation - aggregate Master spin retention, 2026-10-31

IDLE after bounded WB-WHL-08 structural aggregate increment (S). Reconciled source/tests
with spin-state, terminal preparation, schema-17 and WB-D013/018/058/120/138/139/155.
Existing allocation/history binding reused; no I01d. Git/merged state not independently
verified because Git is prohibited. Local edits only; supervisor owns publication.

RunDocument optionally retains explicit MasterSpinState, preserving legacy absence.
Validates identity/revision; reservations require exact retained terminal receipts and
exclude concurrent pending Master selection/Grand attempt. Lifecycle, creation,
activation, draw and FP-debit production copy paths preserve state. Pause/Resume retain
original accounting without consuming spins. New creation conflicts with a reservation;
exact historical replay remains available. Structural in-memory retention only: no
assignment, settlement, storage authority or terminal command admission. Schema-17
encoding explicitly refuses present spin state to prevent silent loss; absent legacy
encoding unchanged. No schema bump, Master Wheel change or gameplay enablement.

Java 11 compilation PASSED in build/continuation-spin-aggregate.
MasterSpinAggregateChecks PASSED: 0/1/513 prefixes, 1/7/Long.MAX_VALUE balances,
Pause/Resume, replay, absent/mismatched history, foreign/future state, new-selection
conflict, explicit zero, creation-copy preservation and source immutability.
MasterSpinStateChecks, MasterTerminalPreparationChecks, MasterResolutionBindingChecks
and MasterResolutionAggregateChecks PASSED. JUnit wrapper/codec-refusal test authored.
Focused Gradle aggregate/spin-state/RunCodec/legacy attempt failed before compilation:
inherited wrapper-cache lock Access is denied. JUnit/codec/legacy NOT_RUN. Inherited
GRADLE_USER_HOME unchanged; no cache workaround or external inspection. No full-suite claim.

Reconciliation: structural aggregate spin retention DONE offline; JUnit/codec refusal
IMPLEMENTED_UNVERIFIED. Durable versioned spin/allocation JSON and migrations UNSTARTED;
typed atomic terminal operation/assignment UNSTARTED. WB-WHL-08 PARTIAL; WB-WHL-09
PARTIAL (automatic retries UNSTARTED); WB-WHL-10 PARTIAL_UNVERIFIED. Schema-17 and
creation/store/session integration remain IMPLEMENTED_UNVERIFIED. Other audited statuses
and evidence holds stand. Incomplete gameplay remains unwired/default-disabled.

Next: versioned spin-state/allocation JSON and RunCodec migration coverage; replace
encoding refusal only with lossless validation. Preserve legacy absence, never infer
balance. Then typed atomic actual-pending-prefix-bound history/reservation/obligation
publication and exact retained replay before locked/session admission. Settlement,
grants, sampling, automatic retries, suspension/revalidation and changed-snapshot
reconciliation remain unfinished. WB-D155 adopts no retirement rule. No playable claim.
Supervisor handles commits, PRs, CI and merges.

## Current local continuation - versioned Master spin reservation state, 2026-10-31

IDLE after bounded WB-WHL-08 spin-state model increment (S). Reconciled source/tests
with terminal preparation, schema-17 history and WB-D013/018/058/120/138/139/155.
Existing allocation and aggregate-prefix binding reused; no I01d. Git/merged state
not independently verified because user prohibits Git operations. Local edits only.

MasterSpinState version 1 structurally retains explicit identity/revision, allotted
balance and an optional outstanding MasterSpinAllocation. Reserving preserves allotted
spins and reduces available spins by exactly one; it does not consume the eventual
completion/Reject or special charge. Restore validates identity, revision and original
balance. Exact replay returns the retained allocation without subtracting again.
Unknown versions, negative values, foreign/stale/occupied state and mismatched balances
fail closed. No balance is inferred from history or initialized for legacy absence.
This is an immutable in-memory state model, NOT durable retention or publication.

Java 11 source compilation PASSED in build/continuation-spin-state. New
MasterSpinStateChecks PASSED; MasterTerminalPreparationChecks,
MasterResolutionBindingChecks, MasterResolutionAggregateChecks and extended
MasterSelectionResolutionChecks PASSED. Coverage includes ordinary/Taint/Sacrifice,
0/1/513 empty prefixes, 1/7/Long.MAX_VALUE balances, zero exhaustion, duplicate
reservation, exact/changed/foreign/stale replay, explicit restore, unsupported versions,
inactive/occupied heads and source immutability. JUnit wrapper authored.
Focused Gradle spin-state/terminal/RunCodec/legacy attempt failed before compilation:
inherited wrapper-cache lock Access is denied. JUnit/codec/legacy NOT_RUN;
GRADLE_USER_HOME unchanged, no cache workaround or external inspection.

Reconciliation: structural spin-state/reservation model DONE offline; JUnit
IMPLEMENTED_UNVERIFIED. Durable spin/allocation aggregate retention, versioned JSON
and typed atomic terminal operation/assignment remain UNSTARTED. WB-WHL-08 PARTIAL;
WB-WHL-09 PARTIAL (automatic retries UNSTARTED); WB-WHL-10 PARTIAL_UNVERIFIED.
Schema-17 and creation/store/session integration remain IMPLEMENTED_UNVERIFIED.
Other audited statuses and decision/client/catalog evidence gates stand. Master Wheel
unchanged; incomplete gameplay remains unwired/default-disabled. No playable claim.

Next: retain optional spin state in RunDocument, preserving explicit legacy absence
across copy paths; validate reservation against retained terminal history, then add
versioned codec/migration coverage. Do not admit terminal commands until typed operation,
actual pending-prefix binding, reservation/history/obligation publication and exact
retained replay are atomic. Settlement, grants, sampling, automatic retry execution,
suspension/revalidation and changed-snapshot reconciliation remain unfinished.
WB-D155 adopts no retirement rule. Supervisor handles commits, PRs, CI and merges.

# Major decisions pending

Current A101 next task: A102 ordinary quantity/reward table draft under existing recommendation delegation. Numeric schedules remain draft; coherent economy work is active next priority. Do not re-ask A56 (WB-D155). A99/A100 correct a research-model information boundary without a new gameplay decision; all62 client cases remain NOT_RUN.

## Current A98 — permanent-invalidation proposal deferred by user

WB-D155 records the user's answer: “This is very niche and i dont think will come up, move on”. A56 is DEFERRED_BY_USER, not approved or rejected. Stop presenting it as the next blocking question; reopen only at the user's request or if a concrete case requires it. Existing immutable assignments, Pause/Reject and no-reroll rules remain. No retirement, refund, free spin or new fallback is adopted. Changed-unselected-snapshot reconciliation remains separately open.

Next authorized work is coherent progression/economy planning using existing candidates, with source/client holds preserved. No implementation authorization.


## Current A97 continuation — read before historical notes

Rank topology WB-D147 is confirmed and WB-D148–154 integration/finite content authoring is complete within documented scope. No resolved rank question should be re-asked. A89 core has14 keys/16 hunts; A93 has62 future client cases, all NOT_RUN. Numeric prices/family schedule remain unadopted; candidate source rows and reference throughput are not actual-client support. PDF version10 is a saved derivative of135eed73, with later A93–A96 planning/evidence supplements outside that snapshot.

The next **material user decision remains A56 no-fault retirement for externally proven permanent impossibility of an unresolved selected ordinary Fate**. Recommendation remains yes with its narrow evidence/ordinary-only/no-reward/no-spin-minting limits, but it is PROPOSED and NOT_ADOPTED. Ask that one question and await the answer before applying it. Committed-unselected snapshot reconciliation, task continuation, Coffer/policy evidence and separate legacy CA reward scope are independent. Safe independent next source packet: Gargoyle primary rate image/top-floor finish-to-loot map or Shaman canyon access/personal allocation; Turoth sword estimate and necklace conflict remain held. Do not restart full inventory or treat checkpoint counts as a project total.

The older pending/next descriptions below are preserved chronological records, superseded only by their explicit later decisions and current navigation above.


Authority: docs/wheelbound/DECISIONS.md. Latest gameplay approvals2026-10-12 extend through WB-D131; current2026-10-13 delegation is WB-D132 and adopted recommendations WB-D133–137. Ask one choice at a time, discuss its answer, and automatically present the next single choice until the user says stop. Do not repeat approved rules.

## Resolved current answers

- Q6 Questing: retain slice and spin normally; known-empty Questing landing gives brief notice and automatically respins (WB-D111). Suspension proposal rejected for this behavior. Broader activity recovery, unknown evidence, WB-D121 resolves Questing as removable to normal storage but indestructible. WB-D120 confirms no extra empty-retry cost; eventual valid Fate one spin.
- Q10: automatic independent qualifying bounty FP survives Fate failure/Reject; no manual claim, escrow or clawback (WB-D112 supersedes WB-D094 manual flow and historical forfeiture).
- Bossing: L/S/G requirements reaffirmed (WB-D113). WB-D122 confirms fourth Challenge; WB-D128 narrows it to item hunts only.
- PAUSE-CLAIM: no reward/claim processing while paused; preserve prior paid FP/active evidence; no paused earning backfill (WB-D114).
- Workflow: one design choice at a time (WB-D115).

## Still pending

| ID | Narrow unresolved boundary | Work permitted |
| --- | --- | --- |
| Separate legacy CA reward scope | Historical per-boss reward is separate from item Bounties Shop and Master Grand | Preserve evidence; no mixed-CA gate on hilt5 Grand; do not silently delete or invent a separate reward |
| Duplicate first/reacquisition / choice6 — RESOLVED | WB-D118 first-instance baseline includes starting grants; later new purchases escalate despite destruction | Same-instance Satchel return/replay does not increment |
| Q9 existence — RESOLVED / Challenge pool pending | WB-D122 confirms fourth card: significant unique item-drop hunt only (WB-D128) | WB-D123 pool and WB-D124 AND gate/starter bundle/individual expansion confirmed; exact members/gates/prices/order/catalog pending; random/no reroll remains |
| Q6 residual | WB-D138/139 resolve other verified-empty ordinary pools and all-unavailable precommit readiness; postcommit invalidation/snapshot reconciliation details remain | Persist verified-empty retry provenance; unknown is not empty; no invented cost/penalty/free edits |
| Q2 | Genuinely full/unsupported Coffer forced donation | Actual current-client boundary evidence first; no forced death or invented escape |
| Q5 conditional | Fallback only if exact restriction fails specific policy review | Preserve Trade with; route/review packet remains unsent |

Balance numbers remain delegated candidate research, not arbitrary isolated approval questions. Q1 new-account scope, Q7 random forced sacrifice and Q8 no active Fate/obligation for Grand remain resolved.

Earlier Q6/Q10 analysis artifacts and A24 manual-claim oracle inputs remain immutable historical evidence. Their options/statuses are superseded where WB-D111/112/114 apply; they are not current implementation acceptance or automatic runtime enablement. Source pins identify prior inputs, not permission to ignore latest replies.

## Latest clarification2026-10-12 — WB-D116/117
Bounties means item objectives in the Bounties Shop. Master CA Grand Fate requires obtaining Ghommal's hilt5; no plugin-maintained active-only CA tally or anti-cheese audit. Earlier mixed-CA choice is no longer a Master Grand condition. Any historical separate boss-mastery/diary reward feature is separate and its scope is not deleted/invented by this clarification. Do not ask the same conflated question again. One-choice-at-a-time pacing remains; automatically present the next single choice until stopped.

Questing voluntary edit question RESOLVED by WB-D121: removable through normal storage, never destroyable. Not mandatory active; no special storage waiver. Continue next single choice automatically until stopped.

WB-D123 resolves pool separation: Challenge randomly draws eligible bosses/selected creatures from a dedicated target pool; L/S/G keeps normal Bossing pool. Remaining next choice: WB-D124 target unlock policy resolved: Lesser AND Greater→Challenge with starter bundle→individual paid expansion. WB-D126 fixes actual eligible-count floor at3; exact starter set remains delegated catalog work.

WB-D124 resolves target unlock structure: owned Bossing Lesser AND Greater, then paid Challenge card including starter unlock bundle, then further individual target purchases. WB-D125 restricts drops to meaningful creature-specific uniques, excluding shared rare table. WB-D126 resolves anti-narrowing offering rule: require≥3 distinct actually eligible boss/creature targets; card/bundle remains owned belowfloor.

WB-D126 eligible-target diversity resolved: minimum3 distinct actually eligible bosses/creatures before new Challenge offerings, card/bundle retained belowfloor. Starter bundle reaffirmed. WB-D128 removes CA objectives and family draw; only random unique item hunts remain. CA incomplete/repeat question is obsolete. Target/item weighting/reveal remains technical work.

WB-D128: Bossing Challenge is item-drop-only; CA family and random-family decision superseded, incomplete-versus-repeat question obsolete for this card. Bundle/AND tree/dedicated random pool/≥3 supported drop targets retained. WB-D129 resolves ownership eligibility: owned item can still be assigned, but fresh active assigned-source drop during assignment required; old items never satisfy.

WB-D129 fresh-drop rule confirmed. WB-D130 resolves one specific item. WB-D131 fixes inclusive1/400–1/1500 specific-item band; no any-unique substitute or guaranteed kill cap.

WB-D133/134 resolve rate policy under current delegation: personal receiving probability in explicit supported contexts; entire reviewed probability range must lie inside the band. Whole-party chance and unknown allocation cannot qualify. Actual formulas/source/current-client proof remain gated; do not re-ask the policy. WB-D135/136 now resolve generation/reveal and creature/item weighting; WB-D137 confirms player keeps the drop. Next bounded work: separate reviewed starter/expansion catalog candidate research, with real primary source data and no runtime enablement.

Current workflow WB-D132 supersedes ask-before-adoption for context-supported delegated recommendations. Remaining questions require one-at-a-time discussion only where approved intent/evidence cannot resolve them. No implementation authorized.

Challenge generation/reveal/weights RESOLVED under WB-D135/136 delegation. Exact starter set, concrete target/source/rate/reward/price/tier tables remain research/balance work; no arbitrary isolated approval question. Broader all-unavailable/postcommit recovery, evidence-dependent Coffer/policy and separate legacy CA reward scope remain independent unresolved boundaries.

User explicitly confirmed all five WB-D133–137 policies2026-10-13 after in-game explanation (“Yep this is all good”). Hypothetical probabilities were illustrative, not source evidence. Next requested work: two context-supported remaining decisions under WB-D132 delegation; no implementation.

Q6 precommit ordinary-empty/all-unavailable policy RESOLVED under WB-D138/139 delegation. Retain full wheel; verified-empty ordinary retries are free; unknown/unsupported positive-weight outcomes block readiness; all-empty ordinary wheel stops without failure/debit. Do not skip mandatory special outcomes. Remaining Q6: postcommit invalidation and specific changed-snapshot reconciliation details, no automatic reroll. See UNAVAILABLE_SELECTION_CONTRACT in gameplay specs. Concrete target catalog/rewards and Q2/Q5 evidence gates remain separate.

A48 batch1 complete (bounded/source-limited): WB-D140 task-independent starter authoring and WB-D141 no dynamic GE-price floor adopted. Exact starter members/count remain candidates, not enabled roster. Task-dependent expansion continuation remains unresolved and held. Next independent deliverable: another bounded candidate/source revalidation batch; direct current Wiki reading blocked, indexed excerpts not current revision proof.

## Next single question — Q6 permanent ordinary invalidation (A56)

[Recovery boundary/matrix](decision_analysis/POSTCOMMIT_RECOVERY_A56.md) is planning only. Recommend no-fault retirement of an unresolved selected ordinary Fate only when a reviewed external game change eliminates every qualifying completion route: no completion reward, FP penalty, Punishment or consumed ordinary spin; release actual reservation idempotently, never blanket +1. Temporary/unknown/self-caused changes and mandatory specials excluded. **PROPOSED, NOT ADOPTED; await user answer.** Existing Pause/Reject/no-reroll applies until answered. Committed-but-unselected offer reconciliation remains separately open.

## Catalog v1 authoring complete — WB-D142/143,2026-10-14

Supersedes earlier pending **concrete v1 members/rates/access/receipt definitions/reward table** status: CHALLENGE_HUNT_TABLE and catalogs/challenge_hunts_v1 now hold25 creature keys/44 specific-item hunts/six starter members and numeric FP. Taxonomy/full-corpus screen/source/identity/conditions reviewed. No broader task/group/stateful contexts silently adopted: excluded from v1. Task-dependent expansion policy, purchase prices, wider empirical balance and actual runtime/current-client validation remain separate. Do not repeat completed intake or ask for approval of routine delegated table authoring. A56 retirement proposal remains PROPOSED/NOT_ADOPTED; no answer inferred.

## Current next choice — Challenge rank unlock topology (A63)

WB-D144 explicitly replaces single future Challenge rarity with four unlockable ranks and unmultiplied per-roll classification. WB-D145 resolves edge overlaps as delegated convention. CHALLENGE_CARD_TIERS/current rulesv2 supersedes older band paragraphs above; v1 roster/FP remains historical. The exact question is progressive Lesser→Standard→Greater→Superior versus independent rank purchases after a common entry gate. Recommend progressive, retain lower ranks. **PROPOSED, NOT_ADOPTED** until answered because this changes WB-D124's old monolithic prerequisite contract. Optional enabled-rank selector/offer integration/prices and per-rank actual eligibility need coherent follow-on, no invented four extra slots or free Lesser. Expanded v2 roster is not yet complete. A56 retirement remains unrelated/unanswered.

## A68 review-only continuation

Current one-at-a-time topology question is preserved pending an answer; the2026-10-14 request for context-supported recommendations is used for safe bounded catalog review, not inferred approval of the explicitly pending prerequisite change. See research/LOWER_TIER_CONTEXT_REVIEW_A68.md for seven lower-tier contexts and conservative holds. No new gameplay decision, source admission, reward or per-rank readiness rule adopted. Progression recommendation still Lesser→Standard→Greater→Superior, lower ownership retained; exact enable controls and offer integration are separate.

## A69 current supersession — topology resolved

WB-D147 CONFIRMED2026-10-15: progressive paid Lesser→Standard→Greater→Superior, lower ranks retained. Earlier “current next choice” and A68 topology-pending paragraphs are historical and no longer a question. First Lesser preserves WB-D124 ordinary Bossing Lesser AND Greater entry, without a second generic charge. Next delegated integration is owned-rank controls and immutable offers; catalog/FP/source holds remain.

## A70 — owned-rank control resolved under delegation

WB-D148 adopts the future-offer owned-rank selector and safe edit boundary, including explicit first Lesser enable and higher-rank opt-in defaults. No more approval question for these controls. Next integration: family offering/rank choice and per-rank readiness; content/quotes and current-client proof remain unfinished.

## A71 — offer/rank readiness integration resolved

WB-D149 DELEGATED: one Challenge family, same two/three slots, uniform ready enabled rank committed before display, per-rank≥3 canonical eligible keys and unchanged creature-first/item-second draw. Numeric family schedule, actual v2 members/quotes and client proof remain unfinished; no isolated price/frequency invented. Next shared target/starter purchase integration is A72.

## A72 — target/purchase integration resolved

WB-D150 DELEGATED: shared canonical target ownership across ranks; one initial bundle on first paid Lesser, no repeat higher-rank bundle or per-rank source repurchase. V2 exact starter members, product price/quote payload and actual source readiness remain content work. Historical v1 bundle is not a Lesser-ready v2 bundle. Next A73 checks the complete synthetic contract and saves the exact unfinished activation gates.

## Current A73 boundary — five checkpoints complete

WB-D147–150 resolves topology, enable controls, family/rank offerings, per-rank diversity and shared-target/bundle purchase integration. Older pending statements about these fields are superseded. No answer is requested for routine delegated continuation. Still unfinished: actual v2 starter/source/context/identity/receipt/reward/price content and numeric family-frequency balance. A56 permanent-invalidation, changed-snapshot reconciliation, task continuation, Coffer/policy and separate legacy CA reward scope remain independent; none is approved by these checkpoints. Next bounded task A74 is Killerwatt/Molanisk source identity/access/receipt completion, without whole-inventory rereview.

## Current A83 content and decision boundary

WB-D151 DELEGATED future initial Lesser bundle content (four Stronghold sources) and WB-D152 DELEGATED source-specific draft quotes125/125/150/150 now supersede missing-v2-bundle/quote statements for those four only. No further routine content approval question is needed to continue the documented source tasks. Numeric rank/product price and family frequency remain unadopted candidates, not answered by the sensitivity study. Exact estimated-gem evidence, water-snake access/receipt/effort, expanded Greater/Superior content and client proof remain unfinished. A56/task/Coffer/policy/reconciliation/legacy CA scope remain separately unapproved.

## A88 current workstream/status interpretation

WORKSTREAM_STATUS.md/.json distinguishes completed research from unfinished catalogs/progression/pricing, actual client/policy evidence, production implementation and release. Checkpoint count is not a fixed-total estimate. A48 boundedB1 is complete within its preserved source-limited scope; broader continuation is A64/W02. No new decision/source admission is inferred. Core ranked design is authored, but software/client release remains unimplemented and unauthorized.
