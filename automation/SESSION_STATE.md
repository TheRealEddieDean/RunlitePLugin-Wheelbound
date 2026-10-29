## Current local continuation - validated debit history restoration, 2026-10-29

IDLE after bounded WB-ECON-08 prerequisite increment (S). Reconciled existing domain,
persistence and focused test sources against the backlog and recorded merged I01a/b/c
receipts; no Git operations or remote merge verification. FND-01 remains DONE;
TREE-02, WHL-02/03/05, ECON-01/08 remain PARTIAL_UNVERIFIED. FND-11 remains TODO.
Other PARTIAL/TODO/BLOCKED rows retain their status. This supersedes older next-task
text. I01d was not selected. No complete gameplay purchase is claimed.

FatePointDebits.restore reconstructs a contiguous debit-only proposal stream from its
trusted original lifecycle/ledger seed and typed receipts. It validates each revision,
affordability and recorded resulting balance, rejects duplicate command IDs, and restores
original-response replay after later debits. Receipt construction validates local fields.
The original seed must precede these receipts; a post-debit ledger is not a seed. Mixed
lifecycle/draw/reward histories still require complete aggregate validation. Account/run
identity comes from the trusted enclosing seed; receipts are not independently portable
identity evidence. No save schema, file durability, prices, products or live wiring added.
Authority: WB-D105 and STATE_CONTRACT. Master Wheel source unchanged; gameplay disabled.

Two focused JUnit tests added for restoration/continuation/replay and corrupt balances,
reordering, repeated IDs, inactive seed, insufficient seed funds and invalid revisions.
javac --release 11 compiled all domain production sources. A dependency-free executable
smoke check passed for restoration, original-response replay and corrupt/duplicate rejection.
Gradle focused debit/ledger and legacy WheelboundBehaviorTest attempt failed before test
compilation: missing Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran; supervisor must run focused and legacy suites. Smoke is not JUnit/CI.

Next remaining task: integrate FP ledger and typed receipts into the versioned RunDocument,
RunCodec and AtomicRunStore. Define explicit legacy-schema compatibility without inventing
starting FP; preserve FP through lifecycle/draw operations; reject cross-operation command
ID reuse; test failed writes, lost acknowledgements and restart replay. Then couple reviewed
debits to slice instance/lifetime-history mutation. No external paths, credentials, secrets,
workflows or Git accessed. Supervisor handles publication, CI and merges.

## Current local continuation - FP debit proposals, 2026-10-29

IDLE after bounded WB-ECON-08 / WB-FND-11 prerequisite increment (S).
Reconciled domain, persistence and test inventory with recorded merged I01a/b/c receipts.
RunDocument still persists only lifecycle/draw state; no complete gameplay aggregate or
FP serialization exists. Recorded merges were not remotely verified; no Git operations.
FND-01 remains DONE. TREE-02, WHL-02/03/05 and ECON-01 remain PARTIAL_UNVERIFIED.
ECON-08 is now PARTIAL_UNVERIFIED for in-memory debit replay only. FND-11 remains TODO
for atomic durable FP/spin integration. Other partial/TODO/evidence-blocked rows stand.
This supersedes historical next-task text. I01d was not selected or changed.

FatePointDebits supplies immutable proposed lifecycle revision, ledger and debit receipts.
Commands bind account/run identity, UUID, original revision, quote key/version and amount.
Exact replay returns the original receipt after later debits without changing current state;
changed payload/identity, stale revision, inactive runs, invalid amounts, insufficient funds
and revision overflow fail closed. Existing obligations survive successful proposals.
No reviewed prices are adopted: a quote reference does not prove quote authorization.
Trusted callers must validate product/legal-boundary/quote authority and persist complete
product changes, ledger, lifecycle and receipts atomically before publishing success.
This is not durable replay protection: the seed constructor accepts trusted state only,
not restored receipts. No purchases, rewards, evidence deduplication, refunds, crash recovery,
serialization or live plugin wiring are implemented. Authority: WB-D105 and STATE_CONTRACT.
Master Wheel source is unchanged; incomplete gameplay remains unwired/default-disabled.

Three focused JUnit methods authored for original-response replay after later commands,
quote/amount/revision collisions, foreign account/run, inactive/stale requests, insufficient
funds, invalid input, immutability and overflow. javac --release 11 compiled all domain
sources successfully. Gradle focused debit/ledger and legacy WheelboundBehaviorTest attempt
failed before compilation: missing Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran; supervisor must execute focused and legacy suites.

Next bounded unit: integrate FP state and typed debit receipts into the versioned run
aggregate/codec and AtomicRunStore with explicit schema compatibility and replay restoration.
Preserve FP state through lifecycle/draw commands, reject cross-operation command-ID reuse,
and test failed writes/lost acknowledgements/restarts before claiming durable transactions.
Then couple approved debit to new-instance/lifetime-history mutation for WHL-04/05.
No external paths, credentials, secrets, workflow access or Git operations were used.
Supervisor handles publication and CI.

## Current local continuation - Fate Point ledger arithmetic, 2026-10-29

IDLE after bounded WB-ECON-01 increment (S). Reconciled domain and test inventories,
plugin/config wiring and recorded I01a/b/c merged receipts; no Git or remote verification.
FND-01 stays DONE. TREE-02, WHL-02/03/05 and ECON-01 are PARTIAL_UNVERIFIED;
other audited PARTIAL/TODO/BLOCKED statuses stand. Historical next-task text below
is superseded by this continuation. I01d was not selected or changed.

FatePointLedger implements immutable starting balance and ordered positive-amount
EARNING/DISCRETIONARY_COST/PENALTY entries. Earnings recover debt; penalties may
create debt; discretionary costs require full funds. Restoring history validates each
intermediate balance, rejecting spend-before-earn even when final funds would suffice.
Exact long arithmetic rejects overflow without wrapping/clamping; no gameplay cap is
adopted. Authority: WB-D105, ECONOMY and STATE_CONTRACT. No prices, reward eligibility,
paused processing, command deduplication, receipts or durable settlement are implemented.
Trusted caller authorization remains required; ledger append is only arithmetic/history.

Three focused JUnit methods authored for debt/recovery/exact funds, insufficient funds,
restoration ordering, defensive copies, invalid inputs and both overflow boundaries.
Java 11 production compilation passed for ledger, inventory, wheel and graph.
Gradle focused ledger/inventory and legacy WheelboundBehaviorTest attempt failed before
compilation: absent Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran. Supervisor must run focused and legacy suites before verification.

Next eligible bounded task: revision-checked replay-safe FP command/receipt integration
with the durable run aggregate, before paid slice/history transitions (FND-11/ECON-08).
Read existing receipt/store and frozen-quote contracts first; keep product amounts and
catalog payloads gated on reviewed authority. ECON-01 remains partial until durable
integration and executed tests. WHL-03 movement, WHL-04/05 purchases and WHL-11
Questing edit enforcement remain unfinished. Legacy Master Wheel source unchanged;
incomplete gameplay remains unwired/default-disabled. No Git, external-path, credential,
secret or workflow access. Supervisor handles publication and CI.

## Current local continuation - lifetime purchase classification, 2026-10-29

IDLE after bounded WB-WHL-05 classification increment (XS/S). Reconciled domain,
persistence, plugin/config and test inventories against backlog and recorded I01a/b/c
merge receipts. No Git operations or remote verification. WB-FND-01 remains DONE;
TREE-02/WHL-02/WHL-03 remain PARTIAL_UNVERIFIED. WHL-05 is PARTIAL_UNVERIFIED.
Other PARTIAL/TODO/BLOCKED statuses stand; no complete gameplay purchase is claimed.
I01d was not selected. Current user restrictions supersede publication instructions.

SliceInventory.classifyNewPurchase implements WB-D118 lifetime classification only.
Unlocked activities without acquisition evidence use FIRST_INSTANCE; a starting baseline
uses PAID_DUPLICATE ordinal 1; subsequent paid history uses count + 1, even with no
current copies. Stored copies do not change classification. Locked/malformed keys and
counter overflow fail closed. Calls never advance history or mutate ownership. This is
not a quote, receipt, readiness check, purchase transition or price adoption. Caller must
provide trustworthy history; empty ownership cannot establish never-owned status.
Read WB-D011/106/118/121, WHEELS and STATE_CONTRACT/PERSISTENCE before implementation.

Two focused JUnit methods added for first/starting/duplicate/reacquisition/storage cases,
repeat-call immutability, malformed/locked/null keys and long boundary overflow.
javac --release 11 compiled SliceInventory, ActiveWheel and ProgressionGraph successfully.
Attempted Gradle SliceInventoryTest, ActiveWheelTest, ProgressionGraphTest and legacy
WheelboundBehaviorTest using workspace-local Gradle home, offline/no-daemon options.
Missing Gradle 8.10 attempted download and failed Permission denied before compilation.
No JUnit tests ran; supervisor must execute focused and legacy suites.

Next eligible bounded dependency: WB-ECON-01 immutable FP ledger/balance validation,
reading approved negative-balance, penalty and discretionary-cost rules first. Then
implement revision-checked, replay-safe durable debit/new-instance/history receipts
before WHL-04/05 purchase transitions. Frozen reviewed prices/catalog payloads remain
required; no candidate amounts may be activated. WHL-03 inventory transitions, WHL-11
Questing edit enforcement and aggregate persistence remain unfinished. Legacy Master
Wheel source unchanged; incomplete gameplay remains unwired/default-disabled. No external
path, credential, secret or workflow access. Supervisor handles Git/PR/CI/merge.

## Current local continuation - owned slice inventory, 2026-10-29

IDLE after bounded WB-WHL-03 snapshot increment (S). Reconciled source/test inventory
against backlog and existing merged receipts; no Git operations or remote verification.
WB-FND-01 remains DONE; TREE-02/WHL-02 remain PARTIAL_UNVERIFIED; WHL-03 is now
PARTIAL_UNVERIFIED. Other PARTIAL/TODO/BLOCKED classifications remain unchanged.
I01d was not selected. Current user restrictions supersede historical publication rules.

SliceInventory validates an immutable active-wheel/Satchel ownership snapshot. Approved
Satchel tiers hold 3/6/10/15 ordinary instances independently of the 24 active slots.
UUIDs must be unique across both locations; Taint/Sacrifice cannot enter storage.
Ordinary instances require retained unlock and explicit acquisition history. Baseline
presence is distinct from paid duplicate count, and survives zero current copies;
unlock-only activities need no acquisition record. Stored instances retain IDs and
individual enhancements, including Questing. No paid movement, purchases, destruction,
refunds, grants, price selection, persistence or gameplay activation is implemented.
Snapshot restoration must supply trustworthy history and canonical catalog keys; this
model cannot establish historical evidence from current ownership. WB-D011/106/118/121
and WHEELS govern this increment. Questing destruction remains a future edit rejection.

Three JUnit methods authored for capacities, defensive copies, retained acquisition
history, stored Questing identity/upgrades, duplicate IDs, special storage and malformed
ownership. javac --release 11 successfully compiled SliceInventory, ActiveWheel and
ProgressionGraph into build/wheelbound-domain-check. This is not a JUnit pass.
Attempted Gradle SliceInventoryTest, ActiveWheelTest, ProgressionGraphTest and legacy
WheelboundBehaviorTest with workspace-local Gradle home, offline/no-daemon options.
Missing Gradle 8.10 triggered download; Permission denied stopped before compilation.
No JUnit tests ran. Supervisor must execute focused and legacy suites.

Next eligible bounded gameplay work: WB-WHL-03 acquisition-history purchase transition
and WB-WHL-05 lifetime classification, with immutable replay-safe receipt semantics;
read approved transaction contracts first. Do not adopt unapproved numeric prices.
Paid storage/reactivation and WB-WHL-04 require FP ledger/atomic transaction prerequisites;
WHL-11 destruction enforcement belongs in those edits. Complete aggregate persistence,
catalog admission and executed tests remain required before WHL-03 can be DONE.
Legacy Master Wheel unchanged; mode remains unwired/default-disabled. No external-path,
credential, secret or workflow access. Supervisor handles Git/PR/CI/merge operations.

## Current local continuation - active wheel snapshot, 2026-10-29

IDLE after bounded WB-WHL-02 (S). Reconciled current domain/persistence/plugin and test
inventories with recorded merged receipts; no remote verification or Git operations.
Existing backlog classifications stand except WHL-02 is now PARTIAL_UNVERIFIED.
I01d was not selected or modified. This supersedes historical next-task directions.

ActiveWheel provides immutable UUID slice identities, ordinary activity keys and individual
weights, separate Taint/Sacrifice kinds, shared 24-slot capacity and minimum five ordinary
slices across three activities. Exact ordinary quarter weights 4/5/6/8/12 implement
1/1.25/1.5/2/3. Special weights are explicit inputs; no unresolved Taint default adopted.
Reordering requires an exact identity permutation and preserves instances and modifiers.
Duplicate IDs, invalid weights and total overflow fail closed. Canonical activity catalog
admission remains a caller responsibility. Authority: WB-D005/006/008/010/096/157,
WHEELS.md and DEFY_FATE.md. No purchasing, Defy progression or spin authorization.

Three JUnit methods added for capacity/diversity, weights/reorder/immutability and malformed
snapshots/overflow. javac --release 11 compiled ActiveWheel and ProgressionGraph successfully
into build/wheelbound-domain-check. Production compilation is not a JUnit pass.
Attempted gradlew.bat --gradle-user-home .gradle-local test --offline --no-daemon
--console=plain with ActiveWheelTest, ProgressionGraphTest and WheelboundBehaviorTest.
Missing Gradle 8.10 and blocked download (SocketException / Permission denied) prevented
compilation; no focused or legacy JUnit tests ran. Supervisor must execute these suites.

WHL-02 remains PARTIAL pending executed tests and catalog/aggregate integration. WHL-07
odds UI remains TODO. Next eligible gameplay increment: WB-WHL-03 immutable owned-slice
and Satchel inventory after reading WB-D011/118/121 and storage capacity decisions.
Paid edits, counters and durable spins remain separate prerequisites. Legacy Master Wheel
unchanged; incomplete mode remains unwired/default-disabled. No external path, credential,
secret or workflow access, publication or background work. Supervisor handles Git/CI.

## Current local continuation - progression prerequisites, 2026-10-28

IDLE after WB-FND-01 source/backlog audit and one bounded WB-TREE-02 increment.
Current user constraints supersede older next-task/publication instructions below.
All 131 backlog rows now distinguish DONE / PARTIAL / TODO / BLOCKED with a scoped
reconciliation. Recorded merged I01a/b/c receipts remain historical evidence; no Git or
remote verification performed. I01d was not selected or modified.

Production ProgressionGraph implements immutable nested AND/OR ownership prerequisites,
explicit roots, unknown-reference and cycle validation, and rejection of already-owned
unlock candidates. Evaluating prerequisites never purchases, grants benefits or adds
wheel slices. No production catalog, prices, client wiring or gameplay activation added.
Authority: WB-D093; Challenge entry/rank test examples follow WB-D124 and WB-D147.
Three focused JUnit methods cover the complete nested boolean truth table, exact
Bossing-vs-Skilling identity, predecessor ownership, defensive copies, malformed rules,
unknown ownership and cycles (including an alternative root that must not hide a cycle).

PARTIAL_UNVERIFIED: attempted gradlew.bat --gradle-user-home .gradle-local test --offline
--no-daemon --console=plain --tests com.wheelbound.domain.ProgressionGraphTest
--tests com.wheelbound.WheelboundBehaviorTest. Wrapper attempted missing Gradle 8.10
and failed with SocketException / Permission denied before compilation. No tests ran.
Static inspection completed; old build reports do not validate current source.

Next: supervisor runs focused and legacy tests in an available build environment.
Next eligible implementation: WB-WHL-02 immutable 24-slot active-wheel model, reading
exact approved ordinary minimum/activity/special/weight rules first. WB-TREE-02 remains
partial until tests and catalog integration; purchasing/benefits remain separate tasks.
Legacy Master Wheel source unchanged; incomplete gameplay remains disabled. No Git,
external-path/credential/secret/workflow access, publication or background work.
## Latest local continuation - skip superseded queued opens, 2026-10-28

One bounded I01d increment: AsyncRunSession checks request generation when a queued
worker begins and returns false before storage access if disconnected or superseded.
Reads already in progress still use the existing publication guard; no IO cancellation
is claimed. Future completion stays outside the new generation-check lock. Cancelling
only the returned future retains its documented non-disconnect semantics.

Two focused JUnit tests use corrupt snapshots to distinguish skipped reads from read
failures: disconnect before worker execution; replacement session publishing before an
old corrupt request executes. They also check preserved corrupt bytes, fresh token
admission and exceptional failure/admission closure for a current corrupt request.
Static review covered the early-return and existing post-read generation guard.

PARTIAL_UNVERIFIED: attempted AsyncRunSessionTest, FoundationPreviewTest and legacy
WheelboundBehaviorTest with gradlew.bat --gradle-user-home .gradle-local test --offline
--no-daemon --console=plain and the three --tests filters. Gradle 8.10 was absent;
wrapper download failed with SocketException / Permission denied before compilation.
No tests executed or new pass claimed.

Next: execute focused and legacy tests in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile boundaries
and actual client baseline sampling. Preview remains unwired/default-disabled; incomplete
gameplay stays disabled and legacy Master Wheel unchanged. No Git operations, external
path inspection, credential/secret access or workflow access performed. No publication
or background work. IDLE. Current user restrictions supersede historical instructions.

## Latest local continuation - non-reentrant baseline sampling, 2026-10-27

One bounded I01d hardening increment closes reentrant baseline admission during
FoundationPreview.sampleBaseline. Direct acknowledgement and nested sampling now
return false while sampling; nested callbacks never run. A finally block clears the
sampling guard and baseline on all exits, including Errors, before successful sampling
may acknowledge its original token. Session replacement still cannot admit a stale token.
Two focused JUnit tests cover reentrant acknowledgement/nested refusal and failure
cleanup followed by retry. Preview remains unwired and disabled by default; no gameplay
activation or legacy Master Wheel changes.

PARTIAL_UNVERIFIED: attempted FoundationPreviewTest, AsyncRunSessionTest and legacy
WheelboundBehaviorTest with workspace-local --gradle-user-home .gradle-local test
--offline --no-daemon --console=plain. Gradle 8.10 was unavailable; wrapper download
failed with SocketException / Permission denied before compilation. No tests executed
or new pass claimed. Static review checked normal, exceptional and reentrant exits.

Next: execute focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile boundaries
and actual client baseline sampling. No Git operations, external-path inspection,
credentials, secrets or workflows accessed. No publication or background work. IDLE.
Current user restrictions supersede historical publication instructions below.

## Latest local continuation - baseline sampling callback, 2026-10-27

One bounded I01d increment adds FoundationPreview.sampleBaseline(token, sampler).
It checks the current token before invoking sampling, clears prior observation admission,
and acknowledges only after successful sampling against the same session. Sampling
exceptions propagate with admission closed; a session replacement during sampling cannot
admit the replacement. Caller must use its serialized client callback path and replace
old samples without awarding progress. This helper remains unwired and cannot verify
sample contents or the caller thread. Three focused JUnit tests added for sampling order,
stale token refusal, failure/retry and replacement-session isolation.

PARTIAL_UNVERIFIED: attempted FoundationPreviewTest, AsyncRunSessionTest and legacy
WheelboundBehaviorTest using gradlew.bat --gradle-user-home .gradle-local test with
--offline --no-daemon --console=plain. Wrapper failed before compilation because local
Gradle 8.10 is absent and download returned SocketException / Permission denied.
No tests executed or new pass claimed. Static review checked admission and reentrant
session-change paths. Legacy Master Wheel files unchanged; gameplay remains disabled.

Next: execute focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile events
and actual client baseline sampling. No Git operations, external-path inspection,
credentials/secrets or workflows accessed. No publication or background work. IDLE.
Current user restrictions supersede historical publication instructions below.

## Latest local continuation - fresh baseline admission, 2026-10-27

IDLE after one bounded I01d increment: FoundationPreview now requires explicit fresh
baseline acknowledgement before acceptsObservation admits callbacks. Raw session token
capture/acceptance remains separate. Reconnect, disable and disconnect invalidate the
baseline; stale tokens cannot initialize a replacement session. Three focused tests added.
PARTIAL_UNVERIFIED: focused and legacy Gradle attempt failed before compilation because
workspace-local Gradle 8.10 is absent and its download was blocked. No test pass claimed.
Next: execute tests, then supported directory-provider wiring, opt-in UI/config and verified
identity/client baseline sampling. Gameplay disabled; legacy Master Wheel unchanged.
No Git operations or external-path/credential/secret/workflow inspection performed.
No background work. This continuation supersedes older next-task navigation below.

# Wheelbound session checkpoint and ownership

## Latest local continuation - directory injection, 2026-10-27

IDLE after one bounded I01d increment: FoundationPreview can now bind a caller-supplied
plugin directory and codec and open an existing save using a verified identity. No
filesystem work occurs during construction or disabled connects; reads stay on the
injected worker. Invalid identity closes prior admission. Four focused tests added.
Status PARTIAL_UNVERIFIED: focused Gradle attempt failed before compilation because
the workspace-local Gradle 8.10 distribution is absent and download was blocked.
Next: run focused and legacy tests, then preview UI/config, verified account/profile
events and fresh observation baselines. Actual RuneLite directory-provider wiring is
still unfinished. Gameplay disabled; legacy Master Wheel untouched. No Git operations,
external-path/credential/secret/workflow inspection or background work performed.


## Latest local continuation - opt-in gate, 2026-10-27

IDLE after one bounded I01d increment: unwired FoundationPreview now gates existing-save
opens behind process-local opt-in, disabled by default. Disable invalidates queued reads
and tokens; enable alone never reconnects. Four focused tests added. Focused test attempt
failed before compilation because the workspace-local Gradle distribution is unavailable
and download is blocked; no new test pass claimed. See IMPLEMENTATION_CHECKPOINT.md.
Next: run focused/legacy tests, then plugin-directory injection, preview UI/config,
verified identity events and fresh observation baselines. Gameplay remains disabled and
legacy Master Wheel unchanged. No Git operations or external-path/workflow inspection;
no commit/PR/merge or background work claimed. Older continuation below is historical.

## Latest local continuation - 2026-10-27

IDLE after bounded I01d asynchronous opener increment. `AsyncRunSession` now guards
worker read publication against disconnect and superseding account/profile requests.
Four focused tests added; execution blocked before compilation by unavailable Gradle
distribution with workspace-local cache. No test pass claimed for this increment.
See IMPLEMENTATION_CHECKPOINT.md for scope and validation limits. Next: run tests,
then opt-in preview/live identity wiring and fresh observation baselines. Gameplay
remains disabled; legacy Master Wheel unchanged. No Git operations or external source
inspection performed; no commit/PR/merge claimed. No background work.

Current engineering continuation: [implementation checkpoint](IMPLEMENTATION_CHECKPOINT.md). Its four-PR batch supersedes historical next-task navigation below. WB-D158/159 authorize production implementation and verified PR integration.

## Current integration authority Ã¢â‚¬â€ WB-D159

User permanently authorized implementation under WB-D158 and now permits opening and merging verified implementation PRs into wheelbound-mode without per-PR approval, until revoked. PR1 code and checkpoint builds passed at head252ae08c3005eaa3349d88e8874ee6747b594ffd (runs40599875895/40599868156). This authority-only follow-up must pass CI before the guarded PR merge. After merge, verify source HEAD and continue I01b from that head. master/main stay protected; no background task implied.


## Verified implementation receipt Ã¢â‚¬â€ 2026-10-27

WB-D158 permanently authorizes production implementation. Code commit 938a0d8840373e027ac6ca993670f982737d647d passed GitHub PR run 40599594587 (merge tree e94dc79666267532a4395cfcf6d4c14f1a270558): clean build and dependency-manifest task successful, 79 JUnit tests, zero failures/errors/skips, including nine RunLifecycle tests. Artifact 12778275353 exports XML/HTML and dependency hashes. Resolved Gradle8.10/JDK11.0.32.1/RuneLite client+api+jshell1.13.4. I00 build/test/dependency evidence and I01a lifecycle foundation complete within this scope; full packaging/live-client validation and I01b persistence are unfinished.

Initial CI exposed the existing unsupported-profile list missing The Fractured Archive in the drifting latest.release dependency; the exact explicit test expectation was updated without creating a speculative boss profile. Legacy design_checks.py fails its hard-coded eight-catalog assertion on BOTH starting b55a540 and this branch (manifest now has18); initial document/link/hash checks pass. This is a separate pre-existing documentation-validator defect, not a Java test failure. Local Java wrapper networking and absence of javac prevent local compilation; remote CI supplies the actual pass.

Next: I01b versioned durable aggregate/journal, preserved invalid originals, duplicate command/payload collision and crash/replay fixtures using supported RuneLite serialization. Current immediate-observation guards do not implement durable receipt replay, session tokens, persistence or live account adapters. UI themes remain queued. PR1 targets wheelbound-mode; source and master/main untouched. Owner released; no background task.


## Current implementation checkpoint Ã¢â‚¬â€ WB-I01a, 2026-10-27

- State: IDLE after this bounded milestone; no background task.
- Source verified: b55a5404ce450267b8d9acbe477a593bbe2729dc.
- Work branch: implementation/run-state-foundation; PR target wheelbound-mode. No master/main/source branch mutation.
- Authority: WB-D158 permanently authorizes implementation; old design-only restrictions are superseded.
- Produced: immutable RunIdentity and RunLifecycle Java foundation; nine JUnit tests cover all obligation stages across pause/resume, stale revisions, account/profile/run isolation, terminal states, invalid inputs and overflow. No live adapter, run creation, persistence or settlement is implemented in this slice.
- Validation: local wrapper clean build failed before compilation (Java network unreachable); local javac unavailable. Full Gradle/JUnit validation is delegated to this branch's existing GitHub CI. See PR/CI for the exact final result; no local pass claimed.
- Next: WB-I01b durable versioned aggregate/journal using supported RuneLite serialization, preserved invalid originals, command/payload replay and atomic acknowledgement/crash fixtures; then persisted deterministic selection. Do not treat the immediate-observation revision guard as a durable receipt/session protocol.
- Existing normal-wheel rendering, gameplay and resources are unchanged. UI themes remain queued behind initial engineering foundation. Outstanding design/client/policy evidence holds remain.


- Session ID: wb-20261014-challenge-tiers
- State: IDLE Ã¢â‚¬â€ UI-GF4 selection saved in owning checkpoint; owner released.
- Branch: wheelbound-mode only.
- Starting remote head:c62d381e4392845bce76d0738fc176af13374661; guarded claimbcaa9a874626d2b1ea85a03b23d85c9f412cac83.
- Protected main:c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.
- Owning A63 checkpoint contains current rules/tables/release; resolve own SHA from branch history and verify head/tree/main before saved claim. Commit cannot embed itself.
- Scope: design/docs/research/standalone tools/wiki_drops only; no production gameplay/main/workflow activation authorized.

## Current UI-GF4 selection checkpoint

Verified predecessor/claim049316fdf2510fb596af1a806b0d04fca1b24ac9 from a6e1d0d533c7e33191932890d87008fba42a16ac; protected main c8cc38ac9a7ab74d9bc10125388da7cc75af99fa. WB-D156 records user selection of GF4 for now; exact original PNG and hash/provenance under docs/wheelbound/design. WB-D157 preserves explicit Taint-as-own-slice clarification, superseding generated ordinary-activity taint depictions. Current official Hub48Ãƒâ€”72 listing-icon/resource-JAR rules distinguished from recommendations. No runtime image/code, root icon or main changes; no implementation authorization implied.

Owner wb-ui-gf4 released in owning checkpoint. Verify remote commit/tree/main before saved claim. Next: discuss/revise selected Grand style if user requests, otherwise await their next wheel choice; do not select other wheels automatically. Independent design queue still begins A102 ordinary quantity/reward draft as previously recorded. No background work. PDF remains older derived checkpoint. Prior A101 continuation below remains relevant to independent economy work.

## Historical A101 release

Session wb-a98-a100 began at8337c82416486dbdd0f51c37915a11124c2482a9; protected main c8cc38ac9a7ab74d9bc10125388da7cc75af99fa. Guarded claim c7a37944e146ce13c9c96e7b83146a91967acb12, A98 user deferral83aa7f58b37bc16740b63dbc104cd22566a119b6, A99 audit21ddc97fa07fe35ba5dfa9fa0c7df7833b76b973, A100 corrected studyce39c550145d2d7b421932e5c471a0536b2f4079 verified remotely. Shell push lacks credentials; connected GitHub guarded tree/commit/ref publication works.

Completed: WB-D155 explicit user direction to move on from niche A56 permanent invalidation. It is DEFERRED_BY_USER, not approved/rejected, not a blocking next question; preserve Pause/Reject. A99 found A87 hidden-target choice leakage and marked historical interpretation limits. A100 separate visible-rank model chooses first, reveals selected creature/item afterward;43 scenarios/86,000 synthetic windows and1,050 checks. A10130 candidate budget sums and combined economy plan with A102Ã¢â‚¬â€œA107 bounded follow-ons. No numeric prices, q or new gameplay rule adopted; no full-mode simulation/live test performed.

Validation:43,336 rank assertions;12 core mutations and14 trace-plan mutations rejected;22 collector tests;1,050 A100 checks;18 authority hashes; historical A87 model/results and v1/source display/core/production/build/workflow bytes preserved against starting remote. All62 client cases remain NOT_RUN. See A100 validation receipt and A101 final receipt for exact executed scope. Derived PDF remains version10/source135eed73, not refreshed for this block.

Exact next action: Recheck remote/ownership, then A102 ordinary XP/KC/quest quantity/reward table draft per research/FULL_ECONOMY_PLAN_A101.md. Use context-supported recommendations; no production coding. Follow with explicit normal-bossÃ¢â€ â€™Challenge entitlement reuse/product/gate mapping before a combined economy model. Source expansion remains independent (Gargoyle primary image/Shaman canyon packet; Turoth conflicts held). Do not restart inventories, re-ask resolved ranks or re-ask deferred A56. Separate Coffer/policy/task/reconciliation/legacy CA evidence/scope remains untouched.

Owner released; no active/background task. Owning A101 commit must be resolved from history and exact head/tree/main verified before claiming remotely saved. Uncommitted work should be empty after verification. All older next paragraphs below are historical where superseded by this continuation.

## Historical A97 continuation Ã¢â‚¬â€ superseded next-question navigation

Latest completed A97: End-of-hour validation, current navigation and ownership release. Verified predecessor `29b0e18626e30c17097158e5ed66e584e48ea6b4`. Completed A84Ã¢â‚¬â€œA96 bounded evidence/content/economy/core/publication/trace/version/build/source packets and final A97 navigation/validation. Finite core14 keys/16 hunts;43,336 rank assertions,329 historical-partial source assertions,269 economy checks,22 collector tests,12 core and14 plan mutations rejected; full-core62 real-client cases all NOT_RUN.18 authority hashes valid; original v1/source display files and production/build/workflow bytes preserved. PDF240pages saved version10 from exact135eed73/62 input hashes, later evidence/planning supplements explicitly separate. Fresh existing-plugin CI passed; local Gradle distribution network failure and missing CI artifacts persist. No open material policy adopted, no implementation or main change.

Exact next action: Ask the one A56 permanent ordinary invalidation question and wait before adopting. Independent next source packet: Gargoyle primary table-image/top-floor finishing-to-loot map (image web read blocked) or Shaman canyon access/personal allocation/effort. Preserve held Turoth sword estimate/necklace discrepancy; then coherent price/progression/full-economy work. Recheck remote/ownership first; no repeated rank/inventory research.

Existing approved rules and explicit supersessions remain controlling. No source estimate silently certified, no unrelated retirement/task/Coffer/policy approval inferred. Owner released; no background task. Previous Ã¢â‚¬Å“nextÃ¢â‚¬Â paragraphs are historical where superseded. Owning commit requires final remote head/tree/main verification.

## Latest explicit user change Ã¢â‚¬â€ WB-D144/145

User2026-10-14 specifies four separately unlockable item-hunt Challenge ranks: Lesser1/25Ã¢â‚¬â€œ1/50; Standard1/50Ã¢â‚¬â€œ1/125; Greater1/125Ã¢â‚¬â€œ1/750; Superior1/750Ã¢â‚¬â€œ1/5000. These final structured ranges supersede the same message's preliminary1/60/max1000 and the prior single1/400Ã¢â‚¬â€œ1/1500 band. Ignore the displayed2Ãƒâ€” in2Ãƒâ€”1/75 **for bracket placement**; classify as1/75. Preserve raw counts/conditions/personal encounter probability separately; never call per-roll rate exact per-kill rate.

WB-D145 delegated shared-edge convention: reciprocal denominator D=1/p; Lesser25Ã¢â€°Â¤DÃ¢â€°Â¤50, Standard50<DÃ¢â€°Â¤125, Greater125<DÃ¢â€°Â¤750, Superior750<DÃ¢â€°Â¤5000. Shared boundaries go to easier rank, no duplicate same-context entries. Non-unit fractions compared exactly. Alternatives/task/party/approximation need explicit context review; no convenient-rate fallback.

Personal fresh active exact assigned-source drop, meaningful unique/source filtering, previous ownership allowed, non-consuming/once-only completion, no reroll/pity and normal Bossing L/S/G mechanics remain. New Challenge ranks are distinct from ordinary KC-card names.

## A63 completed artifacts and validation

- docs/wheelbound/CHALLENGE_CARD_TIERS.md: adopted brackets, roll-count examples, migration counts, lower-tier source leads and explicitly unadopted unlock/control recommendation.
- catalogs/challenge_tier_rules_v2.json: four rules,44 historical hunt mappings, original raw rates, unchanged v1 personal/FP receipts marked historical, full44,447-row classification statistics NOT roster counts, lower-tier cached leads NOT admitted.
- tools/wiki_drops/challenge_tier_rules_v2.py and five new tests: exact classifier/derivation; existing17 collector tests plus5 new =22 offline tests pass.
-44 old hunts map29 Greater/15 Superior; zero Lesser/Standard. New lower-tier source leads: Scurrius spine1/33 (two contexts, not two creatures), Hespori bucket1/35, Corp spirit shield8/512=1/64. Access/receipt/FP reviews still required; no promotion from raw data.
- Endpoint/outside/nonunit/ignored-multiplier/unknown-alternative checks,44 mapping and44,447-row conservation, lead JSON/SQLite equality and unchanged v1 hash pass. Authority manifestv5 has10 hash-pinned records; current Challenge rules point to v2.
- Prior44-hunt v1 JSON/CSV unchanged as historical/source receipts; old human table carries a supersession banner. Old builders/validator reproduce historical policy, not new future pool construction. V1 FP is not automatically a new tier reward.
- Expanded v2 four-tier roster/rewards/offer integration are explicitly **not complete**. Do not repeat the old v1-complete status as current four-tier readiness. No live/client/gameplay tests performed.

## Topology question resolved Ã¢â‚¬â€ WB-D147

User2026-10-15 explicitly approves progressive ranks retaining lower ranks. See current continuation at top; do not ask this again. A69Ã¢â‚¬â€œA73 integration is delegated. Older pending topology notes below are superseded history.

## Historical evidence and preserved unrelated proposals

Original output.zip user attachment libfile_12514dfedef0819184279d6d84d380ff, SHA256e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309.2,929 parsed pages/44,447 raw rows; partial discovery, no all-game completeness claim. Corrected taxonomy1,437 monsters/161 bosses/855 non-creatures/32 components/444 unresolved. No raw edits/bulk cache commit or source recrawl; prepare attachment again if scratch expires.

A62 historical checkpointc62d381e4392845bce76d0738fc176af13374661/treeb119090f3919616ad482e5fe613075bc3a67efee completed old finite25-key/44-hunt v1 with six starter unlocks/325Ã¢â‚¬â€œ18,925 FP under old rate policy. A54/A55/A58/A59/A60/A61 evidence and143 prior decisions remain preserved except explicit WB-D144/145 supersessions. PDF stale.

A56 Q6 permanent ordinary invalidation stays PROPOSED/NOT_ADOPTED; no answer inferred from tiers. Existing Pause/Reject/no-reroll applies. Committed-but-unselected reconciliation, task-continuation, Coffer Q2, policy Q5 and separate legacy CA rewards remain independent. Current-client identity/composition/ownership/timing traces and engineering remain future activation gates. No background execution/quota/production completion claim. Owner released.

A65 claim 2026-10-14T18:55:13.612274+00:00 expires 2026-10-14T20:55:13.612274+00:00; expected head05706461ff2c5dcb327a2d83d2d5c227b383d5fe. User requests CHALLENGE_HUNT_TABLE include entries under each tier. Display existing reviewed hunts plus explicit source candidates without admitting unresolved contexts or changing unlock topology/rewards.

## Latest A65 Ã¢â‚¬â€ requested CHALLENGE_HUNT_TABLE update complete

Starting remote05706461ff2c5dcb327a2d83d2d5c227b383d5fe; guarded claim888b915ef744accf16a4adb6511927b098b5903e. User explicitly selected GitHub and asked to update that file to include entries in every tier. CHALLENGE_HUNT_TABLE.md now has all four tier sections, every44 prior reviewed hunt reclassified, and202 new candidate source/item/rate groups from a named-signature item scan. Display totals: Lesser6, Standard12, Greater157 (29 prior+128 candidates), Superior71 (15 prior+56 candidates);45 unresolved source rows held separately. These are display groups, NOT eligible creature/roster counts.

Full raw-row/URL/hash/timestamp preserving index and CSV in catalogs/challenge_hunt_review_index_v2; candidate name scope explicit, not all-game/every-item completeness. Wrong source types/encounter adds/alternate modes filtered with A60 overlay; no generic raw-drop auto-admission. V2 FP stays TBD; old v1 quotes labeled historical. Old human table preserved byte-for-byte at CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md; old v1 JSON/CSV unchanged. No new membership/access/receipt/reward/unlock topology approval and no production/main changes.

Validation:1,319 offline source/bounds/count/display/JSON-SQLite assertions pass,22 existing tests pass, all11 manifest hashes/145 unique decisions valid. Render reproducibility and whitespace/scope checks required before guarded owning publication. A65 owning commit requires exact remote head/tree/main verification before saved claim. Owner released; no background work.

Current next material question remains A64 progressive-versus-independent card unlock topology; the file update is not its answer. Expanded v2 candidate admission/rewards/offer integration still pending. Do not call candidate display membership a ready catalog.

A66 acquisition 2026-10-14T19:07:20.956355+00:00 expires 2026-10-14T21:07:20.956355+00:00; expected remote98ae9450bb51ed8ed9b8c8234b88210c06ce3cd4. Explicit user clarification: two current monster/boss drop tables, both new tiers; rename old current/archive files and fix content/source classification/links. No new unlock topology, roster, reward or production approval.

## Historical A66 Ã¢â‚¬â€ monster and boss documents separated

Starting98ae9450bb51ed8ed9b8c8234b88210c06ce3cd4; guarded claim37bf1aeaaebe5b053d43056debf0a18c60cfd622. User clarified the two intended current files: ordinary monster drop rates expanded to new tiers, and boss-only drop rates. MONSTER_CHALLENGE_HUNT_TABLE.md now has90 entries (18 prior reviewed/72 candidates); BOSS_CHALLENGE_HUNT_TABLE.md has156 (26 prior/130 candidates). Both are current four-tier drop-rate tables, NOT an archived monster list. Old CHALLENGE_HUNT_TABLE.md and CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md removed/renamed from active tree; historical originals remain accessible in Git history.

Partition by corrected A60 taxonomy/source evidence, not combat level/name guessing. Shared index/CSV carries source_kind and explicit evidence; no row lost or cross-file duplication. Monster tiers1/1/53/35; boss tiers5/11/104/36. Unresolved rates33 monster/12 boss held separately. Raw notation/base tier rate, prior FP and source-specific old access/receipt facts retained; v2 FP/candidate status unchanged.

Active relative links/manifest pointers and generator/validator now target both new names. WB-D146 confirms documentation split ONLY, not separate paid monster/boss card unlock families. Rank unlock topology and v2 admission/reward/offer integration remain unanswered/pending; do not infer answer from file organization.

A66 source/display validator2,194 assertions and22 offline tests pass. Regeneration,11 manifest hashes/146 decisions, no active old relative links, whitespace/scope and exact remote tree/head/main verification required before final saved claim. Owning commit records release; resolve SHA from history. No production/main/raw source changes or rescrape; owner released. Latest source/docs paths above supersede earlier A65 single/archive descriptions.

## A67 ownership claim

Owner wb-a67; acquired 2026-10-14T19:20:30.706356+00:00; expires 2026-10-14T21:20:30.706372+00:00. Expected head bf213a6bc4f2c34a6693fc42bbaebcae3ffdc12a. Review every supplied corrected monster and every drop; replace monster name whitelist with explicit complete row dispositions, expand meaningful candidates, preserve held conditions. No boss expansion or gameplay policy adoption.

## Current A67 Ã¢â‚¬â€ every supplied corrected monster and drop audited

Starting bf213a6bc4f2c34a6693fc42bbaebcae3ffdc12a; guarded claim3f849d6d458afff67e2d3f12734cdf69bc320bc0. Explicit request after missing Killerwatt/Air diamond: review all supplied monsters and their drops. Completed1,437 corrected MONSTER captures/31,757 rows/1,803 distinct names, with explicit per-item judgements and per-page exhaustive row-index dispositions. No silent monster retrieval whitelist; pinned input change requires new inventory review. Corrected taxonomy unchanged;161 bosses/855 non-creatures/32 components/444 unresolved not silently relabeled.

MONSTER_CHALLENGE_HUNT_TABLE.md now327 tiered rows:18 prior contexts+309 candidates; tiers22/34/164/107. Compared with prior72 candidates:252 added,15 removed,57 retained. Includes Killerwatt Air diamond1/64 Standard and other elemental gems.171 unresolved rate rows and401 significance/activity leads remain visible, not admitted. Source-specific sections, raw quantities/rolls/alternatives/annotations and captured context retained; no favorable conditional probabilities or aggregated tier rates. Shared Superior Slayer tertiary staff outcomes excluded; League/Deadman/discontinued/event sources and non-kill methods audited separately. Boss membership stays156.

research/MONSTER_DROP_REVIEW_A67.md + MONSTER_DROP_AUDIT_A67.json contain report/full ledger. All1,437 network/nonfixture HTML normalized hashes checked; all31,757 SQLite payloads equal captured records (36,068 source assertions).1,038 captures have parsed rows;399 NO_SUPPORTED_TABLES are missing data, not no-drop proof. Existing partial-crawl errors/source scope unchanged; no rescrape. Captured first3 introductions/access infobox facts/all footnotes/NPC IDs and receipts for displayed monster sources preserved in review index.

Validation:6,487 index/display/coverage assertions and22 existing offline tests pass; exact regeneration and all12 manifest hashes/146 decision IDs checked before guarded publication. Historical v1 JSON/CSV/FP unchanged; all runtime flags false. Owning checkpoint releases owner; final remote head/tree/main verification required before saved claim. No production/main changes.

Next: contextual admission/access/identity/receipt/FP review for visible candidates; prioritize normal task-independent sources. Do not present327 display groups as eligible creature count or ready v2 catalog.399 empty captures/444 unresolved classifications remain explicit gaps. Rank unlock topology and A56 permanent-invalidation proposals remain UNANSWERED/NOT_ADOPTED. This completed review supersedes A65/A66 monster-scan/count descriptions, not their historical evidence. No active owner or background task.

## A68 ownership claim

Owner wb-a68; acquired 2026-10-15T01:03:34.921312+00:00; expires 2026-10-15T03:03:34.921312+00:00. Expected remote 173092c2c742c059481c8e69b5bb7e960f1fde39. Scope: seven lower-tier source contexts using committed A67 evidence; document access/identity/receipt holds and a reproducible review packet. No admission, new gameplay decision, FP quote, raw recrawl or production change. Rank topology remains pending.

## Latest A68 Ã¢â‚¬â€ bounded lower-tier context review complete

Verified starting remote 173092c2c742c059481c8e69b5bb7e960f1fde39; guarded claim 1408fc07e595de3195ef86f6258a0dbbd1383448. Prior main c8cc38ac9a7ab74d9bc10125388da7cc75af99fa preserved. Current user requests approximately30 minutes of context-supported work while preserving one-at-a-time material decisions. Unlock topology remains unanswered; no inferred approval.

Seven-context source/access/identity/receipt packet: Killerwatt Air diamond, Molanisk Earth emerald, Infernal Mage Fire ruby, Sea Snake Hatchling/Young Water sapphire, Wilderness Jelly Mithril boots, shore Mogre Mudskipper hat. Five Standard/two Lesser review contexts, NOT five/two eligible creatures. Exact captured rows/context and input/archive hashes preserved. Ten receipt cases planned NOT_RUN; conditional opportunity quantiles checked exactly, no invented kill/hour/FP quotes. Jelly Lesser held finite-task; Infernal page-level estimated rates held despite scalar row parse; sea-snake Slayer40 is task-only; family grouping and Mogre shore miniquest stage unresolved.

Artifacts research/LOWER_TIER_CONTEXT_REVIEW_A68.md/.json and tools/wiki_drops/validate_lower_tier_review.py. Validator passes committed index/row/metadata hashes, classifications, quantiles and all13 manifest hashes;22 existing offline tests pass. No source recrawl, full-cache/SQLite revalidation, table membership/historical v1 edit, gameplay adoption or implementation. Canonical item IDs/personal current-client traces/throughput/v2 FP remain absent and explicitly held. No claim of ready lower-tier pool.

Owning commit contains release and validated artifacts; resolve its SHA in history, verify exact remote head/tree/main before saved claim. No uncommitted work intended and no active/background owner after publication. Next single material question remains progressive LesserÃ¢â€ â€™StandardÃ¢â€ â€™GreaterÃ¢â€ â€™Superior versus independent rank purchases. Recommendation progressive with lower-rank ownership retained; controls/entry gate/prices/offer integration remain separate. Await answer before adopting topology. Next safe source packet: Killerwatt/Molanisk canonical item/form and exact access/receipt evidence, using additional original source only if required; do not restart A67 inventory.

## A69Ã¢â‚¬â€œA73 ownership and authorized plan

Owner wb-a69-a73; acquired 2026-10-15T14:38:01.100370+00:00; expires 2026-10-15T17:38:01.100370+00:00. Starting verified remote91d402137912bd0266c09c5b648c81f0dec1cf53; main c8cc38ac9a7ab74d9bc10125388da7cc75af99fa. User2026-10-15 confirms progressive ranks retaining lower ranks and delegates recommended continuation for five checkpoints. No production implementation.

Plan: A69 P0/S record exact approved topology and supersession; A70 P1/S owned-rank controls/persistence; A71 P1/S offer-family/per-rank readiness integration; A72 P1/S target ownership/starter grant and purchase previews; A73 P1/M executable synthetic acceptance and readiness handoff. Each depends on preceding remotely verified checkpoint. No actual candidate admission, unsupported Wiki fact, arbitrary price or retired-Fate approval inferred. Release after A73.

## A74Ã¢â‚¬â€œA83 ownership and plan

Owner wb-a74-a83; acquired 2026-10-15T14:56:39.939215+00:00; expires 2026-10-15T18:56:39.939215+00:00. Starting verified remote bf4d5206deb556e88f5f4de6ed108f857893f9d8; main unchanged. Current user requests ten more checkpoints, continuing prior recommendation delegation.

Planned sequential bounded deliverables (S/M each): A74 Killerwatt/Molanisk full-source identity/access review; A75 Lesser source-context screening from existing capture; A76 elemental source identity/rate caveat packet; A77 selected task-independent Lesser context definitions; A78 proposed/versioned initial bundle content with explicit readiness holds; A79 source-specific authored effort/reward sensitivity; A80 purchase-price/family-frequency sensitivity study (no empirical claims); A81 receipt/adapter trace acceptance packet; A82 integrated four-rank content-gap and source validation; A83 final consistency/continuation/release. Adjust later bounded deliverables to actual evidence rather than admit unsupported sources or invent game facts. Each requires predecessor remote verification; no recrawl, production/main changes or unrelated retirement approval.

## Hour work-block ownership2026-10-15

Owner wb-hour-a84; acquired 2026-10-15T16:28:41.240949+00:00; expires 2026-10-15T18:28:41.240949+00:00. User requests approximately one hour of authorized design/documentation/research work. Verified start ea63bc89da766595d549ef4aedeea1bf379c4f2a; protected main c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.

Priority/size/dependencies: A84 S targeted exact/range gem evidence check from captured links and current primary lookup, stop at source limitation; A85 M water-snake stage/access/receipt/effort packet; A86 M bounded Greater/Superior semantic source/context/quote packet; A87 M ordinary-income/spin/expense price sensitivity; A88 S honest project workstream/backlog/readiness summary so checkpoint counts are not a completion percentage. Each bounded deliverable publishes/verifies before successor. Reserve final work for validation/checkpoint/release. No production/main changes, no repeated whole-inventory crawl, no arbitrary source proof or silent unrelated decision adoption. Scope may finish fewer or more bounded deliverables according to actual evidence/time. No background work promised.
