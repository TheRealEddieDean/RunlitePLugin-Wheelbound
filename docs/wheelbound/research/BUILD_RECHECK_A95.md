# Fresh existing-plugin build evidence — A95

Read-only recheck2026-10-15 of source commit492be499960f3732bd6a08033ef6d521eb2b9e79. [Structured receipt](BUILD_RECHECK_A95.json) and [minimal log excerpt](BUILD_RECHECK_A95_EXCERPT.txt) preserve evidence without publishing the full runner log. No workflow, build dependency or production source changed.

## Verified remote result

[GitHub run38774803140](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound/actions/runs/38774803140), build job116559560554, completed successfully2026-10-15 at17:09:30UTC. Actual decoded job logs show Temurin11.0.32+1, Gradle8.10, `bash ./gradlew clean build`, compileJava, jar, compileTestJava, test, check and build, then **BUILD SUCCESSFUL in58s**. This is fresh evidence beyond the earlier October10 baseline.

The run exports **zero artifacts**. Successful `test` execution does not disclose exact executed/skipped case counts, XML case results, resolved RuneLite dependency versions/checksums or standard JAR contents. Those reporting/package gates remain open. Do not turn declared source test methods into an executed-case count, or the standard jar task into an inspected package. The development fat shadowJar is not run by clean build and is not an approved Hub submission.

Current logs explicitly warn that setup-java v4 is deprecated, recommend v5, and report Node20-targeted checkout/setup-java/setup-gradle actions forced to Node24. They also show Gradle deprecation warnings. These did not fail this run; no action update or pipeline/reporting change is authorized or applied by this research checkpoint.

## Exact local blocker

Local OpenJDK17.0.20 is available, but `bash ./gradlew --version` fails while fetching the pinned Gradle8.10 distribution from services.gradle.org: `java.net.SocketException: Network is unreachable`. Gradle itself, dependency resolution, Java compilation and local test execution never start. Do not label this a failed regression test or erase the actual remote success. No alternate distribution, dependency override, wrapper hash bypass or network escalation was used.

## Remaining bounded engineering/pipeline task

After separately authorized pipeline work, export actual test XML/HTML, resolved dependency metadata/checksums and the standard JAR inventory from a clean supported build. Review current action warnings in that ticket. Existing clean-build success does not validate newly designed Wheelbound state/FP/receipt/migration mechanics, actual-client traces, full mode balance or Plugin Hub policy. Current W10 stays PARTIAL_BASELINE.

This later evidence supplement does not change approved gameplay/source rates/quotes in PDF version10/source135eed73 and is not claimed included in that snapshot.
