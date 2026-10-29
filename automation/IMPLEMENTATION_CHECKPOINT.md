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

## I01d fresh observation baseline gate - 2026-10-27

FoundationPreview now exposes establishBaseline(token) and acceptsObservation(token).
An existing-save open only establishes a storage session. Future adapters must capture
its token, sample fresh client baselines on their serialized client callback path, then
acknowledge that token before admitting subsequent observations. The acknowledgement
stores no sample and awards no progress. Raw capture/accepts retain session-only meaning.
Reconnect (including invalid input), disable and disconnect clear baseline admission;
stale or foreign tokens cannot establish it. Failed stale acknowledgement does not replace
a valid current baseline. No live adapter calls this API yet, and actual fresh sampling
remains unfinished; the API cannot itself verify caller-provided sampling occurred.

Three focused JUnit tests cover open-without-baseline refusal, fresh token acceptance,
same-revision reconnect and stale-token refusal, and disable/disconnect invalidation.
Attempted: gradlew.bat --gradle-user-home .gradle-local test
--tests com.wheelbound.persistence.FoundationPreviewTest
--tests com.wheelbound.persistence.AsyncRunSessionTest
--tests com.wheelbound.WheelboundBehaviorTest --offline --no-daemon --console=plain.
FAILED BEFORE COMPILATION: Gradle 8.10 distribution absent; wrapper download blocked
with SocketException / Permission denied. No tests executed or new pass claimed.
Static review checked synchronized admission and invalidation paths. PARTIAL_UNVERIFIED.

Next: run focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified identity events and actual
client baseline sampling. Gameplay remains disabled and legacy Master Wheel untouched.
No Git operations, external paths, credentials, secrets or workflows accessed. No background
work or publication. Current user restrictions override historical publication directions.

# Current implementation checkpoint

## I01d caller-supplied preview directory slice - 2026-10-27

Added a FoundationPreview constructor binding an injected plugin directory and RunCodec,
plus identity-only connect. No default/global directory lookup or construction-time IO.
Disabled connects submit no work; enabled opens use AsyncRunSession and existing-save
semantics. Invalid identity invalidates previous tokens before rejecting the request.
The existing explicit-store API remains available. This is an unwired injection seam;
no claim of RuneLite directory-provider or live identity integration is made.

Four focused JUnit tests cover delayed filesystem access/default-disabled behavior,
missing-save refusal without snapshot creation, existing-save opening/profile isolation,
exclusive use of the injected directory and invalid-input token invalidation.
Attempted FoundationPreviewTest and AsyncRunSessionTest via gradlew.bat with
--gradle-user-home .gradle-local test --offline --no-daemon --console=plain.
FAILED BEFORE COMPILATION: Gradle 8.10 distribution unavailable; wrapper download
blocked with SocketException / Permission denied. No new tests executed or pass claimed.
Static review checked worker delegation, directory use and fail-closed invalid input.

Next: execute focused and legacy suites in an available build environment; then wire
the supported RuneLite directory provider, opt-in preview UI/config and verified identity
events with fresh baselines. No run creation, gameplay, detector/reward or recovery UI
activation. Legacy Master Wheel unchanged. Current request forbids Git operations and
external paths/secrets/workflows; none inspected. No background work or publication.


## I01d opt-in preview gate slice - 2026-10-27

Added unwired `FoundationPreview`, a read-only wrapper over `AsyncRunSession`.
Opt-in starts false for each instance and is not persisted. Disabled connects return
false without submitting storage work. Disabling invalidates tokens and pending reads;
re-enabling does not reconnect. Disconnect retains opt-in but requires a fresh explicit
connect with caller-verified identity. This API neither creates runs nor enables gameplay.

Four focused JUnit tests added for disabled startup, disable/re-enable during a queued
read, published-token invalidation, and disconnect/reconnect with process-local opt-in.
Attempted focused FoundationPreviewTest and AsyncRunSessionTest using the workspace-local
Gradle cache (`--gradle-user-home .gradle-local test --offline --no-daemon --console=plain`).
The wrapper failed before compilation: Gradle 8.10 distribution absent and download
blocked with SocketException / Permission denied. No tests executed or new pass claimed.
Static review checked gate/session locking and pending-read invalidation behavior.

Next: execute focused and legacy tests in an available build environment, then add
plugin-directory injection, opt-in UI/config integration, verified account/profile event
wiring and fresh observation baselines. Preview remains unwired; no first-run creation,
gameplay, reward/detector or recovery UI activation. Legacy Master Wheel untouched.
No Git operations, external-path inspection, secrets or workflows accessed. No background
work. Current user restrictions supersede historical publication instructions below.

## I01d asynchronous opener slice - 2026-10-27

Added unwired `AsyncRunSession`, with an injected storage worker executor and a fresh
generation per open/disconnect. Workers load private sessions; only the latest request
can publish. Disconnect immediately closes admission without waiting for IO. Failed
reads/submissions never restore prior admission or initialize missing runs. Future
completion is not observation evidence; capture a fresh token. Cancellation is not
disconnect, as documented on the API.

Four focused JUnit tests added: disconnect during queued read, reversed profile reads
and token invalidation, missing-run failure without creation, rejected submission.
Tests NOT EXECUTED: default wrapper failed creating its external cache lock; retry with
workspace-local `--gradle-user-home .gradle-local` could not obtain Gradle 8.10 because
download was blocked. The prior 117-test receipt applies only to the predecessor.

Next: run new tests and legacy suite in an available build environment, then implement
opt-in preview, plugin-directory injection, verified account/profile events and fresh
observation baselines. No live event/UI wiring, commands, run creation or gameplay
activation in this slice. Legacy Master Wheel files unchanged. No Git operations,
remote verification, PR or merge performed; current user restrictions supersede older
publication instructions below.

## I01d session coordinator slice â€” 2026-10-27

Implemented `RunSession` behind the disabled mode boundary. It opens existing saves only,
issues opaque per-session/revision callback tokens, invalidates them on disconnect and
lifecycle commands, and closes observation admission after failed reads or commands.
Historical command replay refreshes from the actual persisted head rather than the old
receipt. Pause retains the committed obligation; reconnect never backfills observations.
Callers must perform storage work on a worker thread and disconnect on logout, hopping,
profile changes and shutdown. This coordinator is not yet wired to RuneLite events.

Validation: local Gradle 8.10 `test --offline --no-daemon --console=plain`, using the
existing Gradle cache and installed JDK 17 with Java release 11 compilation, passed
117 tests with zero failures/errors/skips. Four new tests exercise actual temporary-file
storage, same-revision reconnect, foreign-session tokens, pause/resume, historical replay,
failed commands/open and lost acknowledgement. This is not JDK 11 CI or live-client proof.

Next: wire opt-in foundation preview to supported plugin-directory injection and verified
account/profile events, with asynchronous stale-result guards and reinitialized observation
baselines. No first-run activation, gameplay, detector, recovery UI or Hub release is enabled.
Changes remain on the isolated `agent/i01d-session-adapter-20261027-222408` branch pending
review/CI; no PR or merge is claimed for this slice.

## Current verified batch release â€” 2026-10-27

WB-D158/159 authorize coding and verified PR integration. This release checkpoint belongs to PR5; verify its exact final head, CI and source merge before reporting merged. No background work after the owning merge.

| PR | Bounded feature | Verified result |
| --- | --- | --- |
| 2 | Versioned, validated foundation save codec | Merged393a2fe699b8382001c3d9da14324114805f67bb; final codef4bc723f15f42a6b43637dee4f54d2021ebadd31; runs40602782885/40602775250 passed |
| 3 | Account-separated atomic snapshot files | Merged8d987651bb0c90a0053e122dea4acb73741a975a; code38a7b81f8a3a52dbd27dc0c2cd119beabec82093; runs40603093612/40603089365 passed |
| 4 | Durable lifecycle command replay and history retention | Mergeddfcef5cbbc8b1cdc47e323a0ab9b7cd894a437fc; final coded81df900f3b897edbd33461277856e2f8192db98; runs40603506804/40603501702 passed |
| 5 | Persisted frozen weighted draws | Code031a8f3557f78b7cc3b9a276be53ced87489f908 passed runs40603884999/40603879444; this checkpoint is the documentation-only follow-up before merge |

Actual exported XML from artifact12780340983:109 tests, zero failures/errors/skips. New batch tests: codec6, file storage8, lifecycle transactions7, weighted draws9 (30 total), plus prior domain9 and existing70 regressions. Gradle8.10/JDK11.0.32.1/RuneLite client1.13.4 resolved in the successful build invocation. Old design_checks.py fixed-eight-catalog assertion remains a separately verified pre-existing issue. Local Java wrapper network remains blocked; remote build/test evidence is authoritative.

Acceptance limits: these are foundation snapshots/transactions, not full playable-mode persistence. No live event adapter, first-run activation, actual content admission, gameplay charges/rewards, full-mode migration, journal rollover or pending-file recovery UI is enabled. The1MiB envelope limit refuses further growth rather than truncating receipts. Interrupted pre-replace writes retain pending/original files and block writes for explicit recovery. Atomic moves and directory force are tested on CI Linux only; unsupported operations fail closed. Existing shipped wheel rendering, configuration and gameplay paths remain unchanged; no Hub submission.

Exact next bounded task: I01d opt-in foundation preview/account-session adapter, with stale login/profile callback invalidation, reinitialized observation baselines and supported plugin-directory injection; retain legacy wheel regression tests. Complete full aggregate/spin/FP/Fate transactions before enabling actual mode gameplay. Gameplay catalog/balance/client/policy gates remain unaffected. Older prepared/next paragraphs below are historical work-in-progress navigation superseded by this section.


Authority: WB-D158 permits implementation; WB-D159 permits verified PR merges into wheelbound-mode. Protected main remains c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.

Four-PR batch starts from verified 0407530abef2ebd79eef5a78207c1c880225d2bf (PR1 merged).

1. I01b.1: foundation snapshot codec. Versioned lifecycle/account/run/catalog envelope, SHA-256 corruption check, explicit constructor validation, foreign/future/unknown payload refusal; injected Gson. Six new JUnit methods include 55 lifecycle/obligation round trips. No full-mode payload or auto-migration is claimed.
2. I01b.2 next: account-separated atomic file snapshots and conflict/corruption/failure tests.
3. I01b.3 planned: durable pause/resume command receipts and collision/replay tests.
4. I01c planned: durable weighted draw commitments using frozen explicit pools; no gameplay quotas/prices or live admission rules invented.

Local wrapper Java networking remains unavailable; full Gradle/JUnit checks run in GitHub CI before each merge. Existing obsolete documentation validator catalog-count defect remains separately recorded. Check each PR's exact head and CI result before marking merged. No live client/runtime activation or Hub submission in this batch. Checkpoints on a PR describe prepared scope until its CI/merge is verified.

## I01b.2 prepared â€” atomic snapshots

PR2 merged at393a2fe699b8382001c3d9da14324114805f67bb; final code f4bc723f15f42a6b43637dee4f54d2021ebadd31 passed runs40602782885/40602775250. Gson parser compatibility corrected against actual RuneLite dependency.

AtomicRunStore uses a caller-supplied plugin directory, opaque identity paths, per-run file locks, exact expected revisions, forced temporary writes and atomic replacement with last-good backup. Corrupt/foreign/future data and interrupted temporary files fail closed without deletion. Eight JUnit cases include injected pre/post-replace IO failures and real temporary-file/lock tests. No non-atomic fallback; unsupported directory force/atomic moves fail rather than claiming platform support. This is local foundation storage, not a complete journal or live plugin adapter. Pending files require explicit recovery; no destructive automatic recovery is implemented.

Next: durable lifecycle command receipts/replay; no additional gameplay rule decisions needed.

## I01b.3 prepared â€” durable lifecycle commands

PR3 merged at8d987651bb0c90a0053e122dea4acb73741a975a, code38a7b81f8a3a52dbd27dc0c2cd119beabec82093; runs40603093612/40603089365 passed. Real filesystem tests include exclusive locking and pre/post-replace failure injection.

Schema2 persists lifecycle command receipts and state together; schema1 foundation documents can be decoded into an empty-journal document without changing their original bytes. This is not a full-mode migration. New PAUSE/RESUME commands enforce identity/revision, retain obligations, and publish only after persistence. Exact command replay returns its original receipt even after later commands; changed-operation/revision collisions fail. New commands already at their requested state are rejected without changing revision or creating receipts. No money/spin/reward logic exists in this service. Seven additional tests cover receipt-history retention, restart replay, collisions, stale/foreign contexts, lost acknowledgements, no-op/uninitialized input, legacy decode and journal invariants.

Next: frozen weighted draw commitments with their own immutable request/result journal and restart/no-reroll fixtures. Live activation remains disabled/unwired; current foundation snapshot is not a complete gameplay save.

## I01c foundation prepared â€” immutable weighted draw commitments

PR4 merged atdfcef5cbbc8b1cdc47e323a0ab9b7cd894a437fc; final coded81df900f3b897edbd33461277856e2f8192db98 passed runs40603506804/40603501702. Lifecycle receipts and snapshot commit together; storage rejects history removal/rewrite.

WeightedPool retains ordered positive-weight canonical IDs and its explicit catalog version. DrawCommitment freezes a validated sampled ticket and therefore exact result/odds. DurableWheelDraws persists that frozen request/result before returning it; it rejects foreign/stale/paused requests and further draws while a commitment is pending. Exact retries return the original draw without RNG, including after pause/resume/restart or a lost acknowledgement. Pending lifecycle stage is OFFERS_COMMITTED; no settle/reset path is invented. Schema3 supports typed draw journal entries and reads schema1/2 foundations without dropping prior lifecycle receipts. Nine new JUnit methods cover ticket boundaries, immutable pools, invalid inputs, restart, request collisions, pending commitments, lifecycle interaction and legacy/corrupt restore.

Scope: foundation transaction only, NOT a live Master spin, reviewed eligibility generator, spin charge, card offering or complete gameplay save. Caller-supplied synthetic pools test the protocol; they do not admit real content. Existing shipped wheel paths are unchanged. Live preview/account-session adapter, complete aggregate fields, spin/FP transactions, explicit pending-file recovery and cross-platform durability remain next work. Unsupported atomic/directory-force operations fail closed; no platform-wide power-loss guarantee is claimed. No Hub submission.

Batch target: four additional PRs (2â€“5), merged sequentially only after full CI success. Resolve exact final PR5 head/run/merge remotely before claiming completion. Latest merged receipts are carried in subsequent checkpoints and this session's final report.
