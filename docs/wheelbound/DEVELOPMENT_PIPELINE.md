# Development pipeline

## Verified repository configuration
Source inspected at dc76720971c3e15c2e831a27b1505dd25054267f on 2026-10-09. No build executed in this documentation-only workspace.

- Temurin JDK 11; compiler release 11. Gradle wrapper 8.10-all with SHA-256 verification. Do not switch arbitrarily to Java 17 or Gradle 9.
- Java plugin; RuneLite client/jshell latest.release; Lombok 1.18.30; JUnit 4.13.2; Mockito 4.11.0.
- Existing CI uses checkout@v4, setup-java@v4 Java 11, setup-gradle@v4 and bash ./gradlew clean build on push/PR. No Checkstyle task configured; do not claim it runs.
- Launcher com.wheelbound.WheelboundPluginTest via run task with developer-mode/debug. shadowJar is a custom fat-jar task, not the Shadow plugin. Its jar is a development artifact, not a Plugin Hub approval.

## First-session commands
On an actual checkout, inspect status, fetch origin and switch wheelbound-mode without discarding user changes. Use an isolated feature branch/worktree for each ticket; main remains untouched. Record actual resolved RuneLite version because latest.release drifts.

Unix: bash ./gradlew --version; bash ./gradlew clean build; bash ./gradlew run (manual development client only). Windows: .\gradlew.bat clean build and .\gradlew.bat run. Commands are source-reviewed, not executed here. The launcher requires user-controlled legitimate login; never collect launcher tokens, credentials or Jagex session material.

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
| WB-I08 | I05; Grand Fate ledger | Five-way sealed outcome; fresh receipts; four awakened checks; existing-account policy resolved | Large |
| WB-I09 | I01–I08; UI | Accessibility, small layouts, error recovery and no focus hooks/game input generation | Medium |
| WB-I10 | All; packaging/manual review | Complete detector traces and route matrix, regression build, minimal dependency/package audit, explicit Hub review | Large; external |

No production implementation authorized in this assignment. Policy contact/submission and real-game detector tests require a later implementation/review session.

## Executed environment check, 2026-10-09
An isolated shallow wheelbound-mode checkout at a8526ef315dc8b32db2304cb28ee7734b4f0670b was obtained through Git. It contains 12 test source files and remained clean. Host is OpenJDK 17.0.20; compilation target is still release 11, and repo CI selects JDK11. Executed bash ./gradlew clean build: wrapper failed downloading Gradle 8.10 with java.net.SocketException: Network is unreachable, before configuration/compilation/tests. Configuring the existing noncredential proxy and IPv4 preference did not resolve Java connectivity. A curl HEAD reached the official distribution redirect, but that is not a successful Java build. No build/test pass is claimed. Run the first I00 ticket on an environment with the wrapper/dependency endpoints reachable and the recommended JDK11.
