# Wheelbound 1.0.0 release audit

Audited the complete release working tree based on
`35e3a46230ecba78edd0b473b033b67fca41787d`, including the previously requested
specific Combat Achievement option. No submission, publication, tag, history
rewrite or challenge-mode implementation was performed.

## Assessment

The source and local build are ready for Plugin Hub review, subject to the
live-client smoke checks and preparation of a clean release commit. This is not
Plugin Hub approval. The existing staged-only challenge classes must not enter
the submitted commit; see SUBMISSION.md.

## Official requirements checked

- [Plugin Hub instructions](https://github.com/runelite/plugin-hub): Java 11,
  `latest.release`, `build=standard`, metadata, licensing, classpath resources,
  and listing icon maximum 48 by 72 pixels.
- [Official example build](https://github.com/runelite/example-plugin/blob/master/build.gradle)
  and [wrapper](https://github.com/runelite/example-plugin/blob/master/gradle/wrapper/gradle-wrapper.properties):
  Java 11 release enforcement and Gradle 8.10. Wrapper files match the official
  example; the wrapper JAR checksum was verified against Gradle's published hash.
- [Standard Hub build](https://github.com/runelite/plugin-hub-tooling/blob/master/package/src/main/resources/net/runelite/pluginhub/packager/standard-build.gradle):
  production code requires only RuneLite-provided dependencies and Lombok.
  JUnit and Mockito remain local test dependencies, not runtime dependencies.
- [Review policy](https://github.com/runelite/runelite/wiki/Plugin-Hub-Review),
  [rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features),
  and [Jagex guidelines](https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1).

## Fixes made in this pass

- Replaced the pinned RuneLite version with `latest.release`; aligned wrapper and
  CI with the official example and Temurin 11. Kept version 1.0.0, BSD 2-Clause,
  `build=standard`, and `options.release.set(11)`.
- Removed `scripts/cleanup-jagex-login.ps1` and its documentation reference.
  Expanded ignore rules for local credentials, environment files and key files.
- Fixed custom entries starting with `Gain ` being interpreted as XP enum names,
  which could throw during rendering.
- Escaped user text in the reusable wheel's Swing tooltip, complementing the
  existing checklist/result/selector protections.
- Closed the custom-wheel naming dialog during reset, including shutdown and
  account changes; disconnected its former panel's refresh callback at shutdown.
- Bound queued profile callbacks to their originating panel instance, so an old
  lifecycle cannot reset a newly enabled plugin. Added missing panel guards.
- Batched ordinary varbit/stat changes to one requested pool refresh per game
  tick. This is event-driven dirty-state flushing, not game-state polling.
  Completion-varp snapshots retain their existing coalescing.
- Added a local fallback for missing/unreadable branding artwork.
- Corrected release-facing punctuation and updated development/submission notes.

## Findings by area

| Area | Result |
| --- | --- |
| Java compatibility | The entire main tree compiles on Temurin 11 with `--release 11`; all 52 production class files have major version 55. |
| Forbidden behavior | No direct reflection, JNI/JNA, Unsafe, subprocesses, dynamic class loading, executable downloads, Java object streams, credential access, telemetry or network calls in production code. |
| Persistence | RuneLite ConfigManager stores filters and versioned custom-wheel JSON per settings profile. Gson is already supplied by RuneLite; there is no Java object-stream serialization or external database. Stable IDs preserve duplicate entry names and punctuation. Invalid custom JSON is retained and creation disabled rather than overwritten. Deletion removes the wheel and entries from the stored JSON. |
| Gameplay | Boss/Slayer/CA/pet/quest eligibility reads local account/cache state and suggests an activity. No clicks, movement, prayers, attacks, menu actions, chat, packets or combat predictions are generated. |
| Threading | Eligibility and game-data reads use ClientThread; sidebar changes and animation timers use Swing EDT. Volatile popup snapshots bridge rendering and input. Session/generation checks reject stale pool responses; reset cancels both wheel timers and clears results. |
| Input/lifecycle | Input is handled only while the popup is open, with text-field shortcut exemption. Busy state prevents overlapping spins and disables wheel selection/editing during a spin. Dismiss/deactivate/reset cancel animation. Shutdown unregisters all three listeners and removes navigation/overlay. |
| Odds | Built-in entries have weight 1. Raid modes group after eligibility without summing weights. CA task counts do not change encounter odds; the optional task is uniformly selected within that encounter's remaining eligible tasks. XP weights are 35/27/20/10/5/2/1 and drive both selection and wedge size. |
| Pets/quests | One base pet per catalog entry; ownership uses local unlock flags and known variations, with unavailable data failing closed. Quest completion is independent of filtering; miniquests/subquests/unreleased rows are excluded. Live cache correctness remains a manual check. |
| Custom wheels | Empty/single-entry wheels, duplicate labels, JSON punctuation, 200-character labels and a 1,001-entry rendering case checked. Dense wheel labels may be omitted when they cannot fit; selection still includes all enabled entries. No unlimited-size performance guarantee is made. |
| Resources | Both branding images are referenced and loaded with classpath streams. They are distinct assets, about 1 MB each. Root icon is 32 by 33 pixels and needs no resize. Missing branding now falls back gracefully; unavailable game icons retain their fallback behavior. |
| Metadata | README, descriptor, properties, license, credits and development/submission docs reviewed. No acceptance claim or structured challenge-mode implementation. |

The EDT's `WheelPopup.creationAnchor` uses `Client.getCanvas()` only to obtain
the AWT owner/placement anchor for the user-opened dialog; it does not read game
or account state. Canvas dimensions are read by the overlay renderer.

## Reviewer considerations retained without redesign

- `WheelNameDialog.show`: creates an owned modeless Swing dialog, positions it
  relative to the canvas, uses `toFront()` when reopened, and requests focus for
  text input. The rejected-feature policy includes client-window/focus
  manipulation. This is a user-invoked plugin form rather than gameplay control,
  but the reviewer should explicitly assess that distinction. Existing behavior
  was preserved and is disclosed in the submission draft.
- `WheelPopup` mouse/key handlers: consume game-canvas controls while open and
  expose only local spin/dismiss actions. The game continues running; README
  documents this. Verify interaction with other plugins in a live client.
- `QuestPool.read`: invokes the local quest status and requirement scripts and
  reads their result stack; it does not submit gameplay actions. These scripts
  and the CA/pet cache schemas can change with game updates.

No definite forbidden gameplay feature was found. Final policy acceptance rests
with RuneLite reviewers.

## Security scope

Automated value-redacting pattern checks covered tracked working files, the
index, and all reachable Git-history blobs (136 distinct objects at scan time).
No credential/token/private-key/JWT or literal-secret matches were found.
Tracked and historical filenames were also checked for security artifacts.
Historical login-cleanup scripts exist, but no sensitive content was discovered.
Ignored local security notes and credential files were not opened. Actual secret
values were not displayed or copied. This is a best-effort audit, not a proof
that arbitrary secret formats cannot exist. Git history was not modified.

## Verification

- `gradlew.bat clean build` passed on Temurin **11.0.32.1**.
- `latest.release` resolved to RuneLite **1.12.39**.
- **68 tests passed**, zero failures/errors. Five tests were added in this pass:
  custom `Gain ` and dense/long-label rendering, missing artwork, dialog reset,
  custom tooltip escaping, and batched varbit refreshes.
- Existing coverage includes raid grouping, exact XP ticket weights, custom
  persistence/deletion, profile resets, eligibility and animation cancellation.
  The two CA filtering/grouping tests from the preceding request remain included.
- `WheelboundPluginTest` is explicitly documented as a development launcher.
- Production JAR contains the main classes and both PNGs, with no test launcher,
  cleanup utility, credentials, or unfinished challenge classes.
- `git diff --check` passed.
- The only Gradle deprecation warning is that running Gradle on Java 16 or older
  will be unsupported in Gradle 9. Gradle 8.10 supports this Java 11 build;
  the warning was inspected and not suppressed.

## Changed files

This pass changed:

- Build: `build.gradle`, `.github/workflows/build.yml`, `gradlew`, `gradlew.bat`,
  `gradle/wrapper/gradle-wrapper.properties`, `gradle/wrapper/gradle-wrapper.jar`.
- Repository/docs: `.gitignore`, `README.md`, `CHANGELOG.md`,
  `docs/DEVELOPMENT.md`, `docs/SUBMISSION.md`, this audit; removed
  `scripts/cleanup-jagex-login.ps1`.
- Production: `WheelStyle.java`, `WheelComponent.java`, `WheelboundPanel.java`,
  `WheelboundPlugin.java` under `src/main/java/com/wheelbound`.
- Tests/launcher: `CustomWheelsTest.java`, `WheelPresentationTest.java`,
  `WheelboundEventsTest.java`, `WheelboundPluginTest.java` under the test package.

The previous CA request additionally modified `BossData.java`, `CaEncounter.java`,
`CombatAchievementCache.java`, `WheelEntry.java`, `WheelFilter.java`,
`WheelPopup.java`, `WheelboundPanel.java`, `WheelboundPlugin.java`, README,
`BossDataTest.java` and `WheelFiltersTest.java`. Those edits were retained.

## Before submission

Run the live checks in [SUBMISSION.md](SUBMISSION.md), especially account/profile
changes during either spin stage, CA task/tier accuracy, quest requirements,
pet ownership, fixed/resizable layout, native-dialog cleanup, and restart
persistence. No running game/account was used for this audit.

Review/stage the intended final tree, excluding the stale staged challenge
classes, and commit it. Push the reviewed commit and make the repository publicly
accessible only when you decide to submit. Use that new full commit hash in the
Hub manifest; the base hash above does not include these changes. Hub CI and
review remain outstanding. Do not submit the development shadow JAR.
