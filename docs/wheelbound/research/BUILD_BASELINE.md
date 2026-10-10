# Verified existing build baseline

Checked 2026-10-10 against wheelbound-mode commit e6d445c8c27847050abb07f843cfe9c9d4ab476b. This verifies the existing plugin; it does not authorize or implement Wheelbound production mechanics.

## Actual successful CI execution

[GitHub run38025748732](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound/actions/runs/38025748732), job114136109526, completed successfully on2026-10-10 at04:56:47UTC. Existing workflow selected Temurin11; logs resolve11.0.32+1 and Gradle8.10. Build step executes `bash ./gradlew clean build`; logs show compileJava, jar, compileTestJava, test, check and build, then BUILD SUCCESSFUL in59s. No test failure is reported. This is stronger than a green status alone and supersedes the blanket claim that no compilation/test pass is available.

The earlier local Java17 wrapper download failure remains real and unresolved locally. It does not invalidate remote CI success. The stale repo/ checkout was not modified: all48 production/test/configuration files used for this read-only inspection were hash-verified against the current remote source, despite its older documentation modifications.

Structured evidence: BUILD_BASELINE.json; SHA-pinned BUILD_SOURCE_MANIFEST.json; minimal noncredential BUILD_LOG_EXCERPT.txt. Full CI logs were read but not republished because they include irrelevant environment and runner metadata.

## Existing regression coverage

12 test-source files contain70 declared JUnit test methods. WheelboundPluginTest is a manual launcher, not a test case. No Ignore/Assume skip annotations/calls were found in this inventory. The workflow exports no artifacts, so the actual executed test count, XML case results and skipped counts cannot be independently reconstructed from the available terse log. Do not advertise “70 tests executed” from source counts.

| Test source | Declared methods | Coverage visible in source |
| --- | --- | --- |
| BossDataTest.java | 2 | CA metadata load once, incomplete load withheld/retry |
| CustomWheelsTest.java | 12 | Client-provided Gson roundtrip, profiles, malformed saves, text/input isolation |
| MonsterThumbnailTest.java | 2 | Missing model retries, original model geometry unchanged |
| PetHuntingTest.java | 4 | Existing pet identity/category/icon/popup behavior |
| QuestAndAccountFiltersTest.java | 7 | Existing quest requirements/completion and account/pet filters |
| SkillingFlowTest.java | 7 | Popup/spin lifecycle, XP target commitment, small layouts |
| WheelFiltersTest.java | 10 | CA tiers, grouped raids and filtered existing pools |
| WheelPopupTest.java | 2 | Popup bounds and mouse/keyboard cancellation |
| WheelPresentationTest.java | 6 | Fallback artwork, equal grouped odds, filters and render layout |
| WheelboundBehaviorTest.java | 13 | Existing eligibility/catalog/CA/cache/normal-wheel behavior |
| WheelboundEventsTest.java | 5 | Event bursts, EDT lifecycle, login/logout/profile stale work |
| WheelboundPluginTest.java | 0 | Manual RuneLite launcher; no JUnit methods |

These regressions do not cover the planned Wheelbound FP/spin ledger, forced Coffer receipts, punitive controls or Grand Fate state. Existing normal-wheel recommendation filters must not silently become new mode stat restrictions.

## Packaging and original rejection boundaries

Current build declares RuneLite client compileOnly and Lombok compileOnly/annotationProcessor; test dependencies are JUnit4.13.2, Mockito4.11.0 and RuneLite client/jshell latest.release. The standard jar task includes main output, not the test runtime. Custom shadowJar intentionally combines main, test and test-runtime dependencies for development launch; it was not run by clean build and is not an approved Hub artifact. Do not submit that fat jar as evidence that serialization/dependency policy is satisfied.

Production WheelboundPlugin injects the host-provided Gson and passes it into CustomWheels; a production search found no `new Gson`, GsonBuilder, or KeyboardFocusManager references. WheelPopup uses RuneLite KeyManager/MouseManager registration. Those are source observations consistent with addressing the original PR blockers, not a fresh Plugin Hub approval. Current dependencies are transitive/dynamic; without dependency metadata and archive inspection, do not claim full package inventory or absence of embedded forbidden libraries.

No direct HTTP/socket/process/Robot API references were found in the inspected main Java files; the only URL match was a source attribution comment. This scoped source search does not audit host services, transitive dependencies or all possible networking behavior. Asset requests use RuneLite sprite/item/skill/model APIs; that is not a third-party upload review.

## Remaining pipeline work, without production changes

1. Export successful job test XML/HTML and dependency-resolution metadata as a future CI hardening task; it is documented, not activated here.
2. In a reachable environment run `bash ./gradlew --version`, `bash ./gradlew dependencies --configuration testRuntimeClasspath`, and `bash ./gradlew dependencyInsight --dependency net.runelite:client --configuration testRuntimeClasspath`. Record exact resolved artifacts/checksums. Do not guess a RuneLite version from API source HEAD or latest.release declaration.
3. Inspect the actual standard jar with `jar tf build/libs/wheelbound-1.0.0.jar`; verify classes/resources/properties, absence of test-launcher/test dependencies, and compare original normal-wheel behavior. No jar was downloaded in this review.
4. Separately review workflow action deprecation warnings. Logs report setup-java v4 and Node20 action warnings; they did not fail this build. Record current warning evidence and defer any action-version change to the authorized pipeline ticket.
5. Existing CI does not execute Python documentation/simulation checks. A green Java job must not be used to claim those checks ran remotely; local design/H4 checks have their own receipts.

## Gate update

A18 is verified for the existing remote clean-build task, with dependency-version and test-report export still incomplete. WB-I00 therefore has a partial actual baseline: compilation and test task succeeded on a known toolchain, while reproducible artifact/version reporting remains. Local environment and live-client proof remain separate blockers. Production implementation still requires explicit authorization.
