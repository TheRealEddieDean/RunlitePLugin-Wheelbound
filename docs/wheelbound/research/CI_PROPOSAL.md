# Reviewable CI evidence proposal

Status: proposal only,2026-10-10. Files remain under docs/wheelbound/research; no .github workflow, build.gradle, production source or test source was changed. The current existing workflow already succeeds. This proposal fills its missing report/version evidence rather than adding Wheelbound gameplay.

## Concrete files

[CI_PROPOSAL.yml](CI_PROPOSAL.yml) defines a designated-branch build/report workflow. [CI_ACTION_PINS.json](CI_ACTION_PINS.json) records exact release-tag-to-commit resolution and inspected action metadata from the four official action repositories. [dependency_manifest.init.gradle](dependency_manifest.init.gradle) is an opt-in diagnostic init script that emits group/module/version/classifier/extension/SHA-256 records for compileClasspath, testRuntimeClasspath and annotationProcessor. It records no artifact cache paths, credentials or account details.

The proposal has read-only repository permissions, branch-limited push/PR triggers,20-minute job timeout, per-ref concurrency cancellation, wrapper validation, no automatic build scans or dependency-graph submissions, and14-day test/diagnostic artifact retention. Action references use exact inspected commits; all four action metadata files specify Node24. This session did not activate or execute the workflow, validate its action runtime behavior or execute the Groovy script.

Current CI logs warn about older setup-java/Node20 actions. Official release/tag and action metadata inspection selected checkout v7.0.1, setup-java v6.0.1, gradle/actions v6.4.0 and upload-artifact v7.0.2 as candidate replacements. This source-level review does not assert they ran successfully with this repository. Review the official release notes and runner compatibility again when installing the proposal, rather than treating tags or this document as permanent latest versions.

## Exact evidence behavior

The existing clean build and dependency-manifest task run in the same Gradle invocation. This matters because latest.release can drift: metadata resolved in a separate later invocation cannot prove which artifact was compiled earlier. The init task reads the configurations resolved by that invocation and hashes the artifact files. Later human-readable dependencies/dependencyInsight output is supplementary; compare it against that same-invocation manifest before attributing a version to the successful build.

The standard jar is listed and hashed, not published as a plugin release. Test XML/HTML and diagnostic reports are uploaded even after failure when present. No shadowJar, game-client run, login, live route interception, code deployment or Hub submission appears in the proposal. Missing reports remain missing; upload-if-no-files warning cannot be called an accepted baseline.

Offline design/H2/H3/H4 fixture commands are explicit, so a future green workflow can establish that they actually ran. These checks still do not execute the current-client capture cases. Envelope fixtures need their jsonschema environment separately; the proposal does not pretend that checking a saved schema receipt equals live detector validation.

## Review and activation milestone

During the authorized pipeline session, inspect current branch/main/owner and reconcile newer CI edits. Replace the existing build.yml coherently rather than blindly adding a duplicate build workflow. Keep the diagnostic init script under its documented path or update the workflow path together. Do not touch main. After installation, observe the actual new run and inspect test XML, resolved versions/checksums, jar listing/hash and failures. Record that run's exact commit; only then mark the proposal runtime-tested.

Pin actual RuneLite dependency versions for reproducible development only through a separately reviewed build change. This report is a resolution manifest, not a lockfile, SBOM, Hub packaging scan or guarantee all transitive behavior is safe. Preserve normal-wheel regression coverage and keep future domain-state work separate from live punitive adapters.

## Validation actually completed

YAML parsed successfully with the installed YAML parser using string-preserving loading. Designated branch triggers, read permissions, timeout and all four action refs were checked against the inspected metadata/pins. Diagnostic file paths exist. Groovy runtime and action execution remain NOT_RUN because this session is documentation/preparation and the local wrapper network issue persists. The existing successful CI run remains the only build execution attributed to this work.
