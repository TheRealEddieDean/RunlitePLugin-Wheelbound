# Exact build-artifact acceptance supplement

WB-A29,2026-10-11. Planning only; no new Java build, downloaded jar, workflow activation, dependency lock, Hub approval or live trace. This supplement turns the remaining WB-I00 reporting gap into independently executable future work. Gameplay authority remains docs/wheelbound/DECISIONS.md. Baseline build success remains established; artifact completeness remains NOT_VERIFIED.

## Source observations and newly identified export gap

At source2a0e44eaeeae31f74ea56973ccb1f0823ccd2a38, build.gradle applies the Java plugin, uses release11, and does not customize standard jar inputs. settings.gradle names wheelbound; version1.0.0 yields the expected standard output build/libs/wheelbound-1.0.0.jar. The custom shadowJar pulls in main output, test output and testRuntimeClasspath, sets the test launcher Main-Class and names wheelbound-1.0.0-all.jar. These two outputs have different purposes.

The existing unactivated proposal uploads XML/HTML and build/reports/wheelbound, including a jar entry listing and SHA-256. It does **not** upload the jar itself. A listing and hash cannot independently verify class-file versions, archive member bytes, embedded metadata or forbidden shaded classes. During later pipeline work either export the standard jar as a non-release diagnostic artifact or run an inspectable byte-level auditor in that same job and export its report. This is a proposed evidence improvement, not an activated workflow change.

runelite-plugin.properties is at repository root. No current build.gradle sourceSets/processResources rule places it into the standard jar. Its absence from the jar must not automatically be called a packaging defect: first establish whether the chosen Hub/build route consumes root metadata separately. Verify root metadata and compiled plugin entrypoint independently. Do not silently insert root files into resources or invent a Hub requirement to justify a build change.

[Source manifest](SOURCE_MANIFEST.json) pins these three inspected files, the proposal, test inventory and reporting plan. Known baseline source and local scratch copies match the current Git tree. No actual archive bytes exist in this packet.

## Required evidence chain

Use the exact producing source commit, run ID, job ID, workflow revision, event/ref and observed task outcomes. Record JDK and Gradle versions, resolved dependency records from the compilation invocation, and every exported member's byte length and SHA-256. Record artifact ID/name and download time separately. A filename, matching XML identity, local timestamp or self-authored source SHA does not authenticate CI execution.

Reconcile the producing run's headSHA against the checkout used to build. PR merge refs and branch heads can differ; do not attribute a merge build to a branch commit without the checkout record. Respect the designated branch and inspect concurrency/ownership before later activation. Do not overwrite older successful baseline evidence with an incomplete new job.

Missing exports, expired artifacts, cancelled jobs, missing XML or diagnostic tasks that never ran are evidence gaps. Do not fill them with synthetic fixtures or a later independent latest.release resolution. Failed-build reports are useful diagnostics, not a successful baseline. Source/toolchain/dependency changes require a new evidence record; an old source inventory is not automatically valid for new tests.

## Acceptance matrix

The following are future engineering acceptance checks. Every row is currently NOT_VERIFIED against actual new artifacts. They do not replace specific policy or live-client gates.

| ID | Inspect | Acceptance condition | Failure or limited conclusion |
| --- | --- | --- | --- |
| PA01 | Producing job and checkout | Actual clean build succeeded at identified checkout; source/run/job linked | Green unrelated job, cancelled build or missing checkout proof cannot close I00 |
| PA02 | Toolchain | Actual JDK11 and wrapper8.10, compiler release11 recorded | Do not substitute host Java17 or guessed versions |
| PA03 | Resolved dependencies | Same-compilation-invocation configurations and artifact hashes identify RuneLite client/jshell and transitive artifacts | Dynamic selector text is not resolved version; later dependencies output is supplementary |
| PA04 | Test reports | All real suite XML reconciled with a source-matching inventory; pass/failure/error/skip/missing/extra/duplicate explicit |70 declarations alone are not70 executions; synthetic XML never counts |
| PA05 | Archive identity | Standard jar bytes or reviewed same-job byte report; length/hash match exported record | Listing/hash alone proves neither members nor package correctness |
| PA06 | Main output | All expected compiled main classes/resources present; permitted generated/inner classes accounted for | Compare build/classes/java/main and processed main resources; do not guess a fixed class count from Java files |
| PA07 | Test separation | Test launcher and test-only outputs absent; no JUnit/Mockito/test-runtime class inclusion | Any unexpected inclusion requires explanation before acceptance; do not blanket-ban legitimate main names ending Test |
| PA08 | Dependency separation | No unexplained RuneLite/Gson/Guava/Lombok/other dependency class trees or nested jars | Absence of obvious package prefixes does not prove absence of relocated/shaded dependencies; compare class inventory/build inputs |
| PA09 | Class compatibility | Every ordinary class member's major version≤55, mapped to intended Java11 target | Unexpected multi-release layout or newer bytecode needs explicit review; do not ignore META-INF/versions classes |
| PA10 | Archive structure | No duplicate member names, absolute/traversal paths, unexplained executable payloads or unaccounted files | Inspect safely without launching/extracting arbitrary entries; filename-only scan is insufficient |
| PA11 | Metadata/entrypoint | Root plugin metadata matches actual com.wheelbound.WheelboundPlugin class and intended packaging route; launcher manifest absent from standard output | Main-Class pointing to WheelboundPluginTest identifies development launch packaging; metadata placement remains route-specific |
| PA12 | Regression and scope | Existing normal-wheel reports retained; new artifact audit distinguished from Hub/policy/live acceptance | Clean package does not authorize restrictive controls or prove detector semantics |

Class-major≤55 is a target compatibility check, not a bytecode behavior/policy audit. Lombok-generated methods can legitimately appear in plugin classes without embedding Lombok classes. Host-provided Gson references are different from bundling its implementation or constructing an unsupported private instance. Archive review alone cannot establish either runtime behavior; preserve the earlier source audit and exact future review separately.

## Bounded future WB-I00 subtasks

| Subtask | Estimate | Dependencies | Concrete result |
| --- | --- | --- | --- |
| I00-E1 | S | Pipeline authorization, branch ownership, reachable dependencies | Inspect coherent proposal against current workflow; decide byte-export versus same-job auditor; install only when authorized |
| I00-E2 | S | E1 successful actual run | Download/export run-linked XML, same-invocation dependency manifest, toolchain and standard package evidence; immutable length/hash index |
| I00-E3 | S | E2 source-matching reports | Reconcile actual test results using automation/test_reports; publish actual counts and missing/extra/skip/failure explanations |
| I00-E4 | S | E2 actual standard jar or complete byte audit | Execute PA05–PA11; preserve reviewed archive inventory and reasons for every unexplained entry |
| I00-E5 | XS | E3/E4 and task outcomes | Record verified/failed/blocked status per PA row; close reporting only when evidence supports it |

No extra real-client login is needed for these reporting subtasks. No main update, plugin deployment or Hub submission is implied. Current local connectivity and missing exports prevent execution here. The existing production code remains unchanged.

## What can start after explicit coding authorization

I01 pure-domain receipt/state/RNG work can use synthetic inputs and the saved offline oracle expectations. It need not solve Coffer or exact trade policy first. Keep fresh-account activation integration gated until its real predicate is validated; do not silently broaden initial support to existing accounts. Preserve unresolved Q6/Q10/PAUSE-CLAIM/CA-MIXED boundaries without adopting an outcome. Q9 additional Bossing Challenge stays disabled. A synthetic domain test proves domain behavior only; no adapter is enabled by it.

After I00 reporting, the smallest coding milestone remains account-separated state and committed deterministic selection with five ordinary/three activities/24 total, replayed outcome preservation, free pause and idempotent receipts. Any future live adapter needs its own current-client evidence, and restrictive controls additionally need exact policy review. No production coding is authorized by this supplement.

## Review checklist and limitations

Cross-check this supplement against research/BUILD_BASELINE, research/CI_PROPOSAL, actual build.gradle/settings.gradle/root metadata, automation/test_reports and DEVELOPMENT_PIPELINE. Verify pinned Git blobs and local bytes. Keep all PA rows NOT_VERIFIED until real artifact review; no source-only claim may become an artifact PASS. This task adds a plan, not new tests, progression trials, balance values, historical approvals or policy findings.
