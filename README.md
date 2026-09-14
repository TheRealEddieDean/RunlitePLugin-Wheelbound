# Wheelbound

A local RuneLite utility with three wheels: **Bossing**, **Skilling**, and
**Combat Achievements**. Choose a wheel in the sidebar, adjust its filters and
checklist, then press the green **SPIN** button. The existing centered popup,
boss sprites, wheel animation, and result celebration are retained.

**Wheels belong only in the centered game-canvas popup, never in the sidebar.**
The sidebar contains the wheel selector, filters, results, and editable lists.
The popup consumes game-canvas input while open; the game continues running.
Escape or the close button dismisses it and cancels the spin. Space or Enter
spins an available wheel. Hover a wedge to see its name and probability.

## Bossing

**Account**

- **Account for skill level** defaults on. It checks real skill levels using the
  existing combat-style recommendations, Slayer and other skill prerequisites,
  and reviewed quest/access requirements. Login is required for this option.
- **Match my Slayer task** defaults on. Task-only encounters are excluded unless
  the current assignment, remaining count, and task location permit them. This
  is independent of the level/quest option and leaves other bosses unaffected.

**Boss pool**

- **All bosses** selects the whole boss list and clears raid, Mimic, and manual
  exclusions. Account options still apply. Unchecking it deselects the current
  eligible list; individual bosses can then be checked again.
- **Exclude raids** removes all six supported hiscore raid modes.
- **Exclude Mimic** removes Mimic.
- **Included bosses** lets you manually remove or restore eligible bosses.
  Unchecked bosses stay in the checklist. Filtering a boss out temporarily does
  not erase its saved selection.

Both category exclusions default off for new settings. An existing Include raids
preference is migrated to its inverse. Bossing no longer uses CA completion as a
filter; that now has its own wheel.

The catalog includes every BOSS entry in RuneLite 1.12.38 HiscoreSkill. Every
eligible, checked boss appears with equal probability; there is no shortlist.
Mad Angel, Maggot King, and Shellbane Gryphon still lack reviewed level
recommendations and are excluded when the account option is on.

### Access checks and Slayer tasks

Reviewed quest gates cover Zulrah, Vorkath, Phantom Muspah, Gauntlet, Zalcano,
the Desert Treasure II bosses, Morytania encounters, Moons of Peril, Amoxliatl,
Doom of Mokhaiotl, Brutus, God Wars, Nex, and Tombs of Amascut. Grotesque Guardians
also requires the unlocked rooftop. Quest checks deliberately require completion
of the mapped quest, so access available partway through a quest can be excluded.

These are useful eligibility checks, not a complete guarantee of access: keys,
gear, supplies, teams, membership/world restrictions, planted Hespori, and every
possible diary or quest-stage exception are not verified. Turn off the account
option and use the checklist when making an exception.

Task matching covers Abyssal Sire, Kraken (and the CA cave kraken encounter),
Cerberus, Grotesque Guardians, Alchemical Hydra, Araxxor, and Thermonuclear Smoke
Devil. It reads current Slayer varps and local database rows, including direct
boss assignments, Konar location restrictions, and Wilderness assignments.
Unknown or exhausted tasks exclude task-only encounters. The one-off off-task
Thermonuclear Smoke Devil diary exception is not inferred; the task checkbox
can be turned off for that case. No Slayer plugin dependency or chat parsing is
needed. Task changes refresh the pool automatically. **Refresh list** rechecks
quest/access changes and retries unavailable account data.

## Skilling

- **Exclude combat skills** removes Attack, Strength, Defence, Hitpoints, Ranged,
  Magic, and Prayer. Slayer remains a training skill. Defaults off.
- **Exclude skills with 99** uses real levels and defaults on. Requires login;
  turn it off for an offline preview.
- **Include XP goal** retains the optional second weighted XP wheel. Defaults off.
- **Included skills** is a separate saved checklist covering all 24 skills,
  including Sailing, before filters and manual exclusions are applied.

The XP wheel automatically follows the skill wheel in the same popup. Both retain
five or more rotations with 3.5-second quartic easing. Controls stay disabled
until the entire sequence ends. Logout, profile changes, or plugin shutdown
cancel outstanding spins.

| XP target | Probability |
|-----------|------------:|
| 10,000    |         35% |
| 25,000    |         27% |
| 50,000    |         20% |
| 100,000   |         10% |
| 250,000   |          5% |
| 500,000   |          2% |
| 1,000,000 |          1% |

Weights determine both selection and wedge angles. XP targets are suggestions;
there is no goal tracking, reward, unlock, or saved challenge system.

## Combat Achievements

This wheel includes **bosses and ordinary monsters with unfinished CAs**, using
the local CA encounter/task catalog rather than limiting it to boss hiscores.
There is one equally weighted wedge per encounter, not per task. Shared CA
encounters such as Dagannoth Kings remain a single encounter.

- **Exclude bosses** removes boss encounters. Raids are a separate category.
- **Exclude raids** removes raid encounters, including their individual modes.
- **Match my Slayer task** applies the task-only encounter rules above.
- Each of **Easy, Medium, Hard, Elite, Master, and Grandmaster** has an independent
  exclusion checkbox. All tiers start included.
- **Included encounters** is its own saved checklist, independent of Bossing.

An encounter appears only if it has at least one unfinished, supported task in an
included tier. For example, if all your Easy through Hard tasks are complete and
only Master tasks remain, excluding Master removes that encounter. Completed
encounters, excluded tiers, unsupported task IDs, and non-encounter General tasks
do not enter the wheel. Excluding every tier gives an empty pool and disables SPIN.
Login is required; account completion state is never assumed while offline.

Mapped bosses retain their existing canonical sprites. Other encounters use
small thumbnails of the actual local CA models; these are static model previews,
not new boss artwork. They use flat face colors without textures or animation.
Missing assets use the existing initial-letter fallback and retry on later pool
updates. Live model appearance still needs verification in RuneLite.

## Settings and architecture

All controls live in the sidebar. The two former master configuration items have
been removed; their saved values provide defaults for the replacement sidebar
options until a new value is saved. Wheel choice, filters, and each wheel's manual
exclusions persist through ConfigManager. Old unfinished-only Bossing settings
are no longer used. RuneLite profile changes reload the sidebar preferences.

- `WheelType` registers wheel names and their independent checklist keys.
- `WheelFilter` declares each wheel's checkbox labels, sections, defaults, and
  persistence keys. The sidebar builds its cards and selector from this registry.
- `WheelboundPanel` coordinates the shared popup, checklists, and optional XP spin.
- `WheelEligibility` implements pure skill/boss/encounter filtering.
- `AccountAccess` handles reviewed quests and current Slayer assignment rules.
- `BossData`, `CaEncounter`, `CaTier`, and `CombatAchievementCache` separate static
  task metadata from account completion state and retain per-task tiers.
- `WheelIconProvider` and `MonsterThumbnail` load local artwork. The existing
  `WheelComponent`, `WheelStyle`, and popup wheel rendering are retained.

Adding a wheel means registering its type and filters and supplying its eligible
entries; it reuses the selector, saved checklist, spin, cancellation, and popup.
The selector scales without squeezing additional tabs into the sidebar width.

Task enums 3981-3986 supply the six tiers; task parameters 1306 and 1312 identify
the completion bit and encounter, and enum 3971 supplies names. CA model enum
3987 provides encounter models and viewing angles. The 21 exposed completion
varps are snapshotted on login/startup, profile changes, completion events, and
manual refresh. Bursts are coalesced. There is no game-tick CA polling, per-spin
CA read, remote task list, or remote account lookup. Unknown task IDs fail closed.
Logout, hopping, and connection loss invalidate account results. Pool updates
during animation wait until the spin sequence ends and preserve the last result.

Development references: [RuneLite Slayer assignment reader](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/slayer/SlayerPlugin.java),
[CA task loader schema](https://github.com/ehubbartt/combat-achievements-tracker/blob/main/src/main/java/com/catracker/util/CombatAchievementsDataLoader.java),
and [CA model interface script](https://github.com/runelite/cs2-scripts/blob/master/scripts/%5Bproc%2Cca_boss_init_image%5D.cs2).
These are development references, not runtime dependencies. Game-cache schemas
and reviewed access rules can require maintenance after game updates.

## Build and launch

Use **JDK 17** for the Gradle 9.6 wrapper. Compilation targets **Java 11**.
RuneLite dependencies are pinned to **1.12.38**.

```powershell
.\gradlew.bat build
.\gradlew.bat run
```

If your terminal defaults to Java 8, point `JAVA_HOME` at your JDK 17 installation
for the command. On Unix use `bash ./gradlew build` and `bash ./gradlew run`.
Close the development client before rebuilding/relaunching. `WheelboundPluginTest`
is the development launcher; `shadowJar` builds the optional development client jar.

Tests cover filtering, tiers and completion bits, quest rules, Slayer task/location
rules, independent checklist persistence, profile resets, stale callbacks, input
handling, cancellation, and sequential skill/XP spins. Swing review images are
written under `build/reports/`, including a preview for each of the three sidebars.
Mocked tests do not verify live CA models, quest scripts, or account game-cache data.

## Manual RuneLite checks

1. Switch among all three wheels. Confirm filters fit, checkboxes retain the
   existing look, and no wheel appears in the sidebar. Check fixed/resizable clients.
2. In Bossing, toggle account checks, raid/Mimic exclusions, and All bosses.
   Uncheck individual entries and verify they stay unchecked after switching wheels.
3. Compare a quest-locked boss with your account. Test a current Slayer assignment,
   a completed task, a direct boss assignment, and a location-restricted task.
4. In Skilling, check combat exclusions and level-99 exclusions separately and
   together. Verify Slayer/Sailing and the optional second XP spin.
5. In CAs, compare an encounter with the in-game CA list. Exclude its only remaining
   tier and confirm it disappears. Check ordinary monster icons and raid categories.
6. Exclude all tiers or all checklist entries. Confirm SPIN is disabled with an
   explanation. Reinclude an entry and confirm it can spin again.
7. Log out, hop, switch accounts/profiles, and disable the plugin during a spin.
   Confirm no stale account result or second XP spin survives cancellation.

## Privacy and migration

No analytics, third-party API, Wiki scraping, cloud account state, or Wheelbound
server is used at runtime. Artwork comes from local RuneLite/game resources.
Wheelbound never generates game inputs or handles credentials. Previous persistent
skilling goal/config keys remain inert. Login-cleanup scripts are unrelated and
unchanged. No Plugin Hub approval is implied.
