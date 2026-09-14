# Wheelbound

A local RuneLite utility for two questions: **What boss should I do?** and
**What skill should I train?** Open the Wheelbound sidebar and choose **Bossing**
or **Skilling**, click **Open wheel**, then click the **SPIN** center in the popup.
Space or Enter also spins while the popup is open. Hover a wedge for its name
and probability. Escape or the close button dismisses it and cancels any spin.

**UI constraint: wheels belong only in the centered game-canvas popup, never in
the sidebar.** The sidebar contains controls, an Open wheel button, and results.
The popup restores the charcoal-and-gold card and dimmed game backdrop. It consumes
game-canvas input while open; the game itself continues running.

## Bossing

- **All bosses** and **Only bosses with incomplete Combat Achievements** are
  complementary choices. Selecting either deselects the other; there is always
  one pool mode. All bosses still respects the raid and master level settings.
- **Include raids** defaults off and includes all six supported hiscore raid modes
  when enabled. Gauntlet is not classified as a raid.
- **Limit bosses to my level**, in RuneLite's main Wheelbound configuration,
  defaults on. It checks real stats, relevant combat styles, Slayer requirements,
  and a few additional skill prerequisites. These are tunable recommendations,
  not an assurance that your account can access or defeat an encounter.
- Empty pools disable the hub and explain how to adjust the filters. Account
  filters require login; turn them off for an offline preview.

The catalog follows **every BOSS entry in RuneLite 1.12.38 HiscoreSkill**, including
separate raid modes. Each has its canonical name and sprite plus Wheelbound
metadata. Bosses without reviewed stat recommendations are excluded with the
level limit enabled: **Mad Angel, Maggot King, and Shellbane Gryphon**. They can
appear with the master limit disabled. No requirements have been guessed for them.

The centered wheel includes **every eligible boss**, with equal probability;
there is no sampled shortlist. Staggered icon positions use the larger canvas
space for the full 71-entry catalog. The popup scales to fixed and resizable
clients; icons are smaller on fixed-size clients, with names available on hover.

## Skilling and XP targets

All 24 trainable RuneLite skills are included, including combat skills, Slayer
as a skill, and Sailing. There is no separate Slayer/activity wheel.

**Exclude level 99 skills**, in the main configuration, defaults on and checks
real levels. With it off, maxed skills can appear. There are no virtual-level,
200m, training-activity, membership, or quest filters in the skill wheel.

**Include XP goal** defaults off. With it enabled, the skill wheel finishes and a
second actual wheel automatically appears and spins in the same centered popup. The final result shows,
for example, **Mining — Gain 50,000 XP**. Both animations run five or more rotations
with the prototype's 3.5-second quartic easing. Controls stay disabled throughout
both spins; logout, profile changes, or plugin shutdown cancel outstanding spins.

| XP target | Probability / wedge share |
| --- | ---: |
| 10,000 | 35% |
| 25,000 | 27% |
| 50,000 | 20% |
| 100,000 | 10% |
| 250,000 | 5% |
| 500,000 | 2% |
| 1,000,000 | 1% |

The same integer weights determine selection and rendered wedge angles. Tiny
wedges remain tiny; hover them or read the compact legend for their amounts.
XP targets are suggestions only: no progress tracking, saved challenges, overlays,
account mode, rewards, or unlock system is implemented.

## Local Combat Achievement state

`BossData` retains the existing local game-cache schema: tier enums 3981–3986
contain task structs; parameter 1306 is the task ID, parameter 1312 identifies its
encounter, and enum 3971 supplies encounter names. It builds the local immutable
encounter-to-task-ID mapping once per plugin lifetime. This is static game
metadata, not player completion data or a downloaded task list. Explicit aliases
in `BossCatalog` connect canonical hiscore entries to CA encounters. Shared
encounters such as Dagannoth Kings apply to each supported member; raid modes
are matched separately rather than borrowing normal-mode tasks.

`CombatAchievementCache` snapshots the 21 exposed `CA_TASK_COMPLETED_0` through
`CA_TASK_COMPLETED_20` varps. Task ID / 32 selects the completion word and ID % 32
selects its bit, including the signed high bit. A boss is open if **any mapped,
supported task is incomplete**. Unmapped encounters and unknown task IDs are
excluded by the incomplete-CA filter rather than treated as unfinished.

A full snapshot refresh is queued on startup (including already logged in),
LOGGED_IN, RuneScape profile changes, RuneLite profile changes, and
`VarbitChanged` events identifying those completion varps. Event bursts are
coalesced. There is **no game-tick polling, per-spin CA read, chat parsing, or
remote account lookup**. Cache identity includes account hash and RS profile key;
logout, hopping, connection loss, and profile changes invalidate state and pending
UI responses. Completion changes refresh the next eligible pool without erasing
the previous displayed result. Updates received during animation are deferred
until the complete spin sequence ends.

RuneLite does not supply a stable typed boss-to-CA-task catalog API. The local
cache schema and explicit encounter aliases can need maintenance after game
updates. If loading fails, the CA filter safely returns an empty pool with an
explanation; All bosses works independently. This version does not verify quest
unlocks, keys, gear, active Slayer tasks, teams, or actual encounter access.

Schema reference: the existing approach was checked against the
[CA Tracker loader](https://github.com/ehubbartt/combat-achievements-tracker/blob/main/src/main/java/com/catracker/util/CombatAchievementsDataLoader.java).
Canonical sprite use follows RuneLite's
[Hiscore panel](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/hiscore/HiscorePanel.java).
These are development references, not runtime dependencies.

## Architecture

- `BossDefinition`, `BossCatalog`, `BossProfile`: canonical boss identity, raid
  classification, CA aliases, and tunable style-specific stat recommendations.
- `BossData`, `CombatAchievementCache`: static local task metadata and separate
  account-local completion/eligibility snapshots.
- `WheelEligibility`: pure boss and skill filtering; no Swing or client calls.
- `WheelEntry`, `WheelSelection`, `XpGoal`: immutable weighted presentation data,
  random selection and landing geometry.
- `WheelIconProvider`: client-thread boss sprite loading through `SpriteManager`
  and skill artwork through `SkillIconManager`. Every catalog boss has a RuneLite
  sprite mapping; **no custom icon resources are needed**. An initial stands-in
  letter is used if a sprite is not yet loaded; subsequent pool updates retry it.
- `WheelComponent`, `WheelStyle`, `WheelPopup`: one reusable icon/weighted renderer, interactive
  hub, tooltips, animation, and cancellation.
- `WheelboundPanel`: normal RuneLite Swing controls, two views, results, and
  skill-to-XP sequencing. Uses RuneLite ColorScheme, FontManager, and installed
  look and feel; no additional UI framework.
- `WheelboundPlugin`: lifecycle, events, client-thread reads, and guarded EDT
  updates. Side-panel preferences use ConfigManager; only the two master settings
  appear in the main configuration.

## Build and launch

Use **JDK 17** for the Gradle 9.6 wrapper. Java compilation targets **Java 11**.
RuneLite dependencies are pinned to **1.12.38** for reproducible API compatibility.

```powershell
.\gradlew.bat build
.\gradlew.bat run
```

On Unix use `bash ./gradlew build` and `bash ./gradlew run`.
`WheelboundPluginTest` remains the development launcher. The optional existing
`shadowJar` task builds a standalone development client jar. CI uses JDK 17 and
the same wrapper. Close the development client before rebuilding/relaunching.

Automated tests cover filters and combinations, raid modes, style/Slayer levels,
local metadata loading and failure recovery, CA completion words, account reset,
event routing/coalescing, all 24 skills, empty pools, exact weighted probabilities,
animation landing, sampled-pool fairness, hub interaction, cancellation, stale
UI responses, and automatic skill/XP sequencing. Non-pixel Swing smoke tests also
write review images under `build/reports/`; boss layout previews in tests use skill
icons as stand-ins because the live game sprite cache is unavailable in unit tests.

## Manual RuneLite checks

1. Enable Wheelbound and click Open wheel in either view. Confirm the wheel is
   centered over the game and never displayed in the sidebar. Try fixed and
   resizable clients. Hover and press the center; double-click repeatedly and verify
   that only one spin runs. Check wedge tooltips and final pointer alignment.
2. Log in and confirm real boss sprites load. Toggle All bosses / incomplete CAs
   and Include raids. Compare eligible pools with the master level limit on/off.
3. Compare a known boss's CA eligibility against the in-game CA interface. Finish
   a CA (especially the last for a boss) and verify the next pool changes while
   the previous result remains displayed.
4. Log out and switch accounts or RS profiles. Verify old CA results disappear,
   account filters wait for login, and the second account uses its own completion.
   Also try hopping, reconnecting, and disabling/re-enabling during a spin.
5. Check a level-99 skill is absent with the master exclusion enabled and present
   when disabled. Check all-maxed and low-level/empty-pool cases if available.
6. Enable Include XP goal. Verify two sequential animations, a skill and XP result,
   unequal wedge sizes, and no third spin or tracked goal. Disable the option and
   verify only the skill wheel runs. Confirm side-panel choices survive restart.

## Privacy and migration

No analytics, telemetry, third-party API, Wiki scraping, cloud state, or Wheelbound
server is used. Sprites come from the RuneLite/game cache and skill icons from
RuneLite resources. Only normal RuneLite/Gradle dependency retrieval is needed
for development. Wheelbound never generates game inputs or handles credentials.

The previous activity categories, activity catalog, persistent skilling
goals, and progress overlay have been removed. Existing saved goal/config keys
are left inert, not read or resumed. The login-cleanup batch/PowerShell scripts
remain unchanged and are unrelated to plugin operation. No Plugin Hub approval
is implied.
