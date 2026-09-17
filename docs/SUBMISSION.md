# Plugin Hub submission - Wheelbound 1.0.0

## Prepared metadata

- Name: Wheelbound
- Author: TheRealEddieDean
- Version: 1.0.0
- Build: standard (no custom production dependencies)
- Entry point: com.wheelbound.WheelboundPlugin
- Repository: https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound
- License: BSD-2-Clause
- Listing artwork: icon.png, exported from the same Classic Wheel used by the client

## Pull request draft

Title: Add Wheelbound

Wheelbound helps players choose their next activity with Bossing, Skilling,
Combat Achievements, Pet Hunting and Questing wheels, plus named custom wheels.
Players filter the pool, toggle individual entries, and manually spin a centered
wheel. Skilling includes an optional XP-target spin. Preferences and custom lists
are stored through RuneLite ConfigManager.

The plugin reads local game/account progress and renders suggestions. It does not
perform game actions, send chat, access credentials, launch subprocesses, or call
external services. Artwork is bundled; some game icons come from RuneLite assets.
A structured challenge/progression mode is outside this release.

Reviewer notes: the custom-wheel name form is an owned Swing dialog opened by
user action; it requests focus for text entry. The wheel overlay consumes inputs
while open and supports Escape to dismiss it. Quest filtering uses quest-status
and requirement client scripts. Please review these behaviors under current policy.
Development used AI assistance, including custom branding artwork.

## Submission steps

1. Complete the live-client smoke checks below.
2. Commit the intended working-tree changes and push them to the public repository.
   There are stale staged additions for GoalOverlay.java, SkillingActivity.java
   and SkillingGoal.java that are deleted in the working tree. Do not submit an
   index-only commit that accidentally brings those unfinished features back.
3. Get the full release commit using `git rev-parse HEAD` after committing.
4. In a fork of runelite/plugin-hub, add `plugins/wheelbound` containing:

   ```properties
   repository=https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound.git
   commit=REPLACE_WITH_FULL_RELEASE_COMMIT
   ```

   This is a template, not a ready-to-submit manifest: the current uncommitted
   changes do not yet have a release commit hash.
5. Open the pull request using the draft above, then address Hub CI/reviewer feedback.
6. Tag the final release commit `v1.0.0` and update README availability after acceptance.

Do not distribute the development shadow JAR as a replacement RuneLite client.
The Hub builds the submitted source commit itself. Nothing has been pushed or submitted
by this preparation step.

## Validation completed

- Clean online build passed on Temurin JDK 11 with Java 11 API/bytecode enforcement.
- `latest.release` resolved to RuneLite 1.12.39 during this audit.
- All 68 automated tests passed; the development launcher is not counted as a test.
- wheelbound-1.0.0.jar contains production classes and both branding PNGs.
- No development launcher, cleanup scripts or unfinished challenge classes are in the plugin JAR.
- Listing icon is 32x33 pixels, within the Hub limit.
- Personal security review notes and development credentials are ignored by Git.
- The personal login-cleanup script has been removed from the release working tree.
- See [the release audit](RELEASE_AUDIT.md) for scope, fixes, remaining reviewer
  considerations and the exact changed-file list.

Gradle 8.10 is aligned with the official example plugin and supports Java 11.
Its warning about Java 16 and older being unsupported in Gradle 9 is expected;
it does not indicate a Java 11 compilation or runtime incompatibility here.

## Live-client smoke checks

- Open each built-in wheel; confirm account filters and current game icons load.
- Enable specific Combat Achievements, exclude Master, and verify the selected
  unfinished task against the in-game list, including grouped raid modes.
- Spin a skill and its optional XP wheel; cancel with Escape.
- Create, edit, delete and reload a custom wheel; check names and saved settings.
- Check the sidebar/header and popup in fixed and resizable windows.
- Log out, hop, switch settings profiles and disable the plugin during a spin.
- Check the naming dialog opens/closes cleanly and does not leave an orphan window.

Automated tests cannot confirm live cache mappings or acceptance under game rules.

## References

- https://github.com/runelite/plugin-hub#submitting-a-plugin
- https://github.com/runelite/runelite/wiki/Plugin-Hub-Review
- https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features
