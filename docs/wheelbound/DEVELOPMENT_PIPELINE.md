# Development pipeline

## Verified repository configuration
Source inspected at dc76720971c3e15c2e831a27b1505dd25054267f on 2026-10-09. Source review is complete. The earlier local wrapper attempt failed; successful remote CI at e6d445c8 is now verified in [build baseline](research/BUILD_BASELINE.md). These are distinct environments.

- Temurin JDK 11; compiler release 11. Gradle wrapper 8.10-all with SHA-256 verification. Do not switch arbitrarily to Java 17 or Gradle 9.
- Java plugin; RuneLite client/jshell latest.release; Lombok 1.18.30; JUnit 4.13.2; Mockito 4.11.0.
- Existing CI uses checkout@v4, setup-java@v4 Java 11, setup-gradle@v4 and bash ./gradlew clean build on push/PR. No Checkstyle task configured; do not claim it runs.
- Launcher com.wheelbound.WheelboundPluginTest via run task with developer-mode/debug. shadowJar is a custom fat-jar task, not the Shadow plugin. Its jar is a development artifact, not a Plugin Hub approval.

## First-session commands
On an actual checkout, inspect status, fetch origin and switch wheelbound-mode without discarding user changes. Use an isolated feature branch/worktree for each ticket; main remains untouched. Record actual resolved RuneLite version because latest.release drifts.

Unix: bash ./gradlew --version; bash ./gradlew clean build; bash ./gradlew run (manual development client only). Windows: .\gradlew.bat clean build and .\gradlew.bat run. The clean-build command was attempted in the isolated checkout and failed at wrapper download; version/run commands are proposed follow-up checks, not successful executions here. The launcher requires user-controlled legitimate login; never collect launcher tokens, credentials or Jagex session material.

Offline design checks: python3 docs/wheelbound/simulation/checks.py. Reproduce model runs using commands in SIMULATION_RESULTS. Keep synthetic assumptions separate from live acceptance.

## Proposed CI hardening
Future pipeline ticket: explicit permissions contents:read, timeout-minutes:15, concurrency cancellation per feature branch, wrapper integrity validation and dependency-resolution metadata artifact. Pin third-party actions to reviewed SHAs when implementing. Run existing clean build plus deterministic offline checks, upload failure reports only, and redact account identifiers. No deployment, secrets or write token needed. This document proposes changes; it does not activate a workflow.

## First vertical slice
Ticket WB-I01: account-separated local run state and deterministic wheel engine behind a disabled-by-default Wheelbound mode, preserving all shipped wheels. First implement pure domain types, seeded selection and atomic journal replay; then a local preview UI. No live shop interception or Coffer completion in this slice.

Acceptance: five ordinary/three activities; 24 total including specials; exact displayed probabilities; persisted result cannot reroll on restart; pause immediate/free; paused observations award zero; duplicate receipts settle once; account/profile swap clears stale callbacks; old normal-wheel tests pass. Schema migration retains unreadable original payload and disables writes until recovery. Tests use synthetic events and crash points, not OSRS inputs.

## Sequenced tickets
| Ticket | Dependencies and modules | Done criteria and tests | Risk/relative size |
| --- | --- | --- | --- |
| WB-I00 | Actual checkout, build/CI | JDK11 wrapper build succeeds; report resolved dependency version and all existing regressions | Small; environment |
| WB-I01 | I00; domain/store/RNG | Vertical slice above, crash/replay and account swap fixtures | Medium; state integrity |
| WB-I02 | I01; wheel/satchel/shop ledger | Invariant-preserving edits; tier-only enhancements; per-activity lifetime duplicate costs; transaction rollback tests | Medium |
| WB-I03 | I01/I02; assignment/card catalog | Distinct targets; permanent eligible pools; Bossing sequence/random boss; empty-pool recovery | Medium; content |
| WB-I04 | I03; progression/economy | AND/OR gates, nonnegative discretionary funds, negative penalty balances, no mandatory dailies | Medium |
| WB-I05 | I03; telemetry/audit | Initialized XP/quest/CA deltas; pause/offline no backfill; supported method templates only | Large; client evidence |
| WB-I06 | I04/I05; access adapters | Exact transaction route tests and specific policy gate before controls; physical visit proof | Large; review |
| WB-I07 | I02/I05; defiance/punishments/coffer | One spin exactly once, 24-slot displacement, no fake donation receipt, full-Coffer policy resolved | Large; irreversible action |
| WB-I08 | I05; Grand Fate ledger | Five-way sealed outcome; fresh receipts; four awakened checks; new-account scope and validated activation predicate | Large |
| WB-I09 | I01–I08; UI | Accessibility, small layouts, error recovery and no focus hooks/game input generation | Medium |
| WB-I10 | All; packaging/manual review | Complete detector traces and route matrix, regression build, minimal dependency/package audit, explicit Hub review | Large; external |

No production implementation authorized in this assignment. Policy contact/submission and real-game detector tests require a later implementation/review session.

## Executed environment check, 2026-10-09
An isolated shallow wheelbound-mode checkout at a8526ef315dc8b32db2304cb28ee7734b4f0670b was obtained through Git. It contains 12 test source files and remained clean. Host is OpenJDK 17.0.20; compilation target is still release 11, and repo CI selects JDK11. Executed bash ./gradlew clean build: wrapper failed downloading Gradle 8.10 with java.net.SocketException: Network is unreachable, before configuration/compilation/tests. Configuring the existing noncredential proxy and IPv4 preference did not resolve Java connectivity. A curl HEAD reached the official distribution redirect, but that is not a successful Java build. No build/test pass is claimed. Run the first I00 ticket on an environment with the wrapper/dependency endpoints reachable and the recommended JDK11.

## Recovered-rule acceptance review (WB-A13)

These are future implementation fixtures, not tests already executed against a client. [Catalog authority](CATALOG_AUTHORITY.md) supplies version/enable gates. Synthetic design checks validate only the documented model and static records. WB-I00 now has verified remote compilation and test-task success; exact resolved dependency metadata and exported test reports remain. Local dependency connectivity is still blocked; all production tickets await implementation authorization.

| Ticket | Required positive and failure fixtures | Blocking condition |
| --- | --- | --- |
| I01 state/RNG | Account/profile-separated journal; persisted draw before animation; crash preserves offers and outcomes; free pause preserves obligations and adds no penalty; no offline backfill; original malformed save retained | Fresh-account activation predicate needs actual onboarding evidence |
| I02 slices/storage | Every edit/migration retains five ordinary slices, three activities and 24 total; Taint/Sacrifice consume capacity; player-selected ordinary displacement to storage/destruction; same-instance modifications survive storage; free destruction has no refund/counter reset; forced candidate/result cannot reroll | Q6 affects exhausted pools; preserve slices while reporting unavailable assignments |
| I03 assignments/cards | Two independent distinct Standard targets by default; same-type offers valid; third upgrade yields three; separate permanent Skilling/Bossing purchases; Standard KC displayed before card selection and random boss draw; no boss choice/reroll; Lesser base FP retained; Greater increased KC/FP; eligible quest not already completed | Q9 additional Bossing Challenge disabled; exact target/reward scaling is versioned candidate data |
| I04 progression/economy | Unique purchased skill unlocks advance 5/9 then 4/6 gates; duplicate slices do not count; AND/OR gates retain node benefits; boss peers need no artificial order/stat requirement; permanent purchased boss pool survives slice destruction; tier-only enhancement prices; activity duplicate lifetime escalation; voluntary costs cannot incur debt; penalties can | Exact balance constants are candidates; no passive FP, compulsory daily income or automatic refund |
| I05 audit/receipts | Completion-time audit uses assigned objective context; legitimate kill/Slayer combat by-products exempt; unrelated supported progress counted; clean assigned completion resets consecutive Penance; unsupported method/tool evidence stays disabled; paused/stale/uninitialized observations rejected | Live trace gates remain unsatisfied; audit thresholds and reward ordering not invented |
| I06 vendor/trade/GE | Physical interaction/location/shop correlation; proximity alone fails; Banned→Locked unlimited fixed-price Pardon; normal unlock/Tempt still needed; current and next class quotes visible; Trade with retained with requested attempt notice; outgoing/incoming/already-open/repeat routes reviewed individually | Exact interception policy and coverage gate; Q5 only if that review fails |
| I07 obligations/Coffer | One spin exactly once for each landed Taint/Sacrifice; derived Punishment adds none; random forced ownership snapshot versus voluntary choice; empty candidate requires acquiring ≥10k eligible item without extra Master spin; donation confirmation/quote/item/credit correlation; disappearance alone fails; crash cannot request duplicate donation | Q2 full/unsupported Coffer escape; live evidence required; 120m draft cap not global Penance limit |
| I08 Grand | Activation only at obligation NONE, including unresolved mandatory settlement; zero remaining Master spins allowed; no artificial level/playtime gate; failed attempt preserves component checklist; each Awakened boss counted separately; no Blood Torva ownership requirement; Master CA threshold uses initialized current values | Shares I01 obligation gate and I05 evidence contract; does not require full Coffer adapter to implement pure-domain gating |
| I09 UI | Vertical progression with peer skill clusters; meaningful progress counts and gold AND/OR links; visible finite bounty targets, search/filter/FP sort, unclaimed notification and manual claim; current/next vendor quotes; actual committed odds; small layouts; no global focus manager | Q10 bounty disposition must not be hidden behind generic completion-reward wording |
| I10 release | Real successful build/regressions; every enabled adapter has positive/negative/paused/relogin/order traces; minimal packaging with supported RuneLite serialization; original Hub Gson/focus-manager rejection addressed; policy approval distinguished from peer precedent | No Hub submission authorized; missing build/live traces prohibit readiness claim |

Reject commits its FP/spin/mandatory Punishment transaction without inventing disposition of independently earned bounty receipts. Preserve provenance and leave affected reward settlement gated by Q10. Completing a synthetic fixture is not permission to enable a production detector.

## Verified remote baseline,2026-10-10

Run38025748732 at e6d445c8c27847050abb07f843cfe9c9d4ab476b executed clean build successfully on Temurin11.0.32+1/Gradle8.10, including compileJava,compileTestJava,test,check and build. See [baseline evidence](research/BUILD_BASELINE.md).70 declared methods are a source count, not exported executed-case count. No artifact or exact latest.release version was exported. The older local attempt still failed before compilation; do not conflate it with this remote pass. No production implementation or new workflow was added.

## Concrete evidence workflow proposal

[CI proposal](research/CI_PROPOSAL.md) supplies exact inspected action pins, report-producing YAML and opt-in dependency manifest diagnostic under docs only. No workflow or production build was changed. YAML/source checks passed; action/Groovy runtime remains untested. The proposal resolves exact artifacts in the same Gradle invocation as compilation to avoid attributing a later latest.release resolution to an earlier build.
