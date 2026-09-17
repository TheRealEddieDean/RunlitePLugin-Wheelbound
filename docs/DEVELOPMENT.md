# Wheelbound development reference

A local RuneLite utility with five built-in wheels: **Bossing**, **Skilling**,
**Combat Achievements**, **Pet Hunting**, and **Questing**, plus your own **Custom**
wheels. Choose a wheel in the sidebar, adjust its filters and
checklist, then click **SPIN** in the center of the wheel. Selecting the Wheelbound
sidebar opens the current wheel on the game canvas. Switching modes updates the
popup; leaving the sidebar closes it and cancels any spin. The checklist expands
to fill available sidebar height, with scrolling for smaller windows.

**Wheels belong only in the centered game-canvas popup, never in the sidebar.**
The sidebar contains the wheel selector, filters, results, and editable lists.
The popup consumes game-canvas input while open; the game continues running.
The results box stays hidden until a spin finishes. Results appear in a compact, centered box below the wheel, sized to the encounter icon and name
(and an XP target when enabled). The wheel stays visible and its center returns
to **SPIN** for another roll. Escape or the close button dismisses it and cancels
the spin; select the sidebar again to reopen it. Space or Enter
spins an available wheel. Hover a wedge to see its name and probability.

## Bossing

- **Easy, Medium, Hard, Elite, Master, Grandmaster**, and **Unrated** replace
  All bosses. All start checked. Difficulty and account filters are independent.
  Ratings follow the [OSRS Wiki Bossing Ladder](https://oldschool.runescape.wiki/w/Guide:Bossing_Ladder),
  a progression guide rather than an official game classification. Solo God Wars
  ratings are used where listed; unlisted bosses remain available under Unrated.
- **Filter by Skill Level** defaults on. It checks real skill levels, existing
  combat-style recommendations, Slayer prerequisites, and reviewed quest/access
  requirements. Requires login. It does not measure player experience or equipment.
- **Include Slayer-task bosses** defaults on. Task-only bosses enter the pool only
  with a matching active assignment and permitted location. Turning this off
  excludes task-only bosses. Other bosses are unaffected; difficulty and skill
  filters still apply. The previous Match my Slayer task preference is retired
  because its unchecked behavior was different.
- **Include raids** includes all three raids and defaults on. The previous
  Exclude raids preference migrates to its inverse; difficulty filters still apply.
- **Include Mimic** defaults on. The previous Exclude Mimic preference migrates
  to its inverse. Mimic still needs its difficulty tier selected.
- **Included bosses** preserves individual selections through filter changes.
  Each eligible, checked encounter has equal odds.

Raid modes are filtered by tier before grouping. CoX challenge mode and ToB hard
mode use Grandmaster; ToA Expert remains Elite because its hiscore category starts
at 300 invocation and cannot identify the guide's 500+ category. Grouped raid
results name the raid, without prescribing a mode or invocation level.

The catalog includes every BOSS entry in the resolved RuneLite HiscoreSkill. Raid modes are grouped into one entry each for Chambers of Xeric, Theatre of Blood,
and Tombs of Amascut. Every eligible, checked boss or raid appears with equal
probability; there is no shortlist. Wheel labels use abbreviations such as CoX,
ToB, ToA, and shorter boss names; hover text, checklists, and results retain full names.
Mad Angel and Maggot King still lack reviewed level
recommendations and are excluded when the account option is on.

### Access checks and Slayer tasks

Reviewed quest gates cover Zulrah, Vorkath, Phantom Muspah, Gauntlet, Zalcano,
the Desert Treasure II bosses, Morytania encounters, Moons of Peril, Amoxliatl,
Doom of Mokhaiotl, Brutus (The Ides of Milk), Shellbane Gryphon, God Wars, Nex, and Tombs of Amascut. Grotesque Guardians
also requires the unlocked rooftop. Quest checks deliberately require completion
of the mapped quest, so access available partway through a quest can be excluded.

These are useful eligibility checks, not a complete guarantee of access: keys,
gear, supplies, teams, membership/world restrictions, planted Hespori, and every
possible diary or quest-stage exception are not verified. Turn off the account
option and use the checklist when making an exception.

Task matching covers Abyssal Sire, Kraken (and the CA cave kraken encounter),
Cerberus, Grotesque Guardians, Alchemical Hydra, Araxxor, Thermonuclear Smoke
Devil, and Shellbane Gryphon. It reads current Slayer varps and local database rows, including direct
boss assignments, Konar location restrictions, and Wilderness assignments.
Unknown or exhausted tasks exclude task-only encounters. One-off off-task exceptions (the Thermonuclear Smoke Devil diary kill and
Shellbane Gryphon elite clue kill) are not inferred. Bossing excludes these
task-only encounters when the include checkbox is off. No Slayer plugin dependency or chat parsing is
needed. Task changes and checkbox changes refresh the pool automatically,
rechecking quest/access changes and retrying unavailable account data. The included
entry count stays pinned to the bottom of the sidebar, outside the scrolling
content. The checklist uses the checkbox background color and padding below the rows.

## Skilling

- **Exclude combat skills** removes Attack, Strength, Defence, Hitpoints, Ranged,
  Magic, and Prayer. Slayer remains a training skill. Defaults off.
- **Exclude skills with 99** uses real levels and defaults on. Requires login;
  turn it off for an offline preview.
- **Include XP goal** retains the optional second weighted XP wheel. Defaults off.
- **Included skills** is a separate saved checklist covering all 24 skills,
  including Sailing, before filters and manual exclusions are applied.

The XP wheel appears after the skill wheel in the same popup and waits for the
user to click its center **SPIN** button (or press Space/Enter). Both retain
five or more rotations with 3.5-second quartic easing. Controls stay disabled
while waiting for the XP spin and until the entire sequence ends. Logout, profile changes, or plugin shutdown
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

XP colors progress from green (10k), blue (25k), yellow-green (50k), gold
(100k), orange (250k), red-orange (500k), to red (1m).
Weights determine both selection and wedge angles. XP targets are suggestions;
there is no goal tracking, reward, unlock, or saved challenge system.

## Combat Achievements

This wheel includes **bosses and ordinary monsters with unfinished CAs**, using
the local CA encounter/task catalog rather than limiting it to boss hiscores.
There is one equally weighted wedge per encounter, not per task. Raid modes are
filtered for unfinished tasks first, then grouped; any qualifying mode keeps the raid eligible. Shared CA
encounters such as Dagannoth Kings remain a single encounter.

- **Include bosses** includes boss encounters. Raids are a separate category.
- **Include raids** includes raid encounters, with all their modes grouped under one entry per raid.
- **Match my Slayer task** applies the task-only encounter rules above.
- **Pick a specific achievement** defaults off. When enabled, the result shows
  the encounter above a uniformly selected unfinished task and its tier. Tasks
  from qualifying raid modes are combined without changing encounter odds.
- Each of **Easy, Medium, Hard, Elite, Master, and Grandmaster** has an independent
  inclusion checkbox. All tiers start checked and included. Saved exclusion settings
  migrate to the equivalent include selection.
- **Included encounters** is its own saved checklist, independent of Bossing.

An encounter appears only if it has at least one unfinished, supported task in an
included tier. For example, if all your Easy through Hard tasks are complete and
only Master tasks remain, unchecking Include Master removes that encounter. Completed
encounters, excluded tiers, unsupported task IDs, and non-encounter General tasks
do not enter the wheel. Unchecking every tier gives an empty pool and disables SPIN.
Login is required; account completion state is never assumed while offline.

Mapped bosses retain their existing canonical sprites. Other encounters use
small thumbnails of the actual local CA models; these are static model previews,
not new boss artwork. They use flat face colors without textures or animation.
Missing assets use the existing initial-letter fallback and retry on later pool
updates. Live model appearance still needs verification in RuneLite.

## Pet Hunting

The pet wheel includes **71 huntable base pets**, originally mapped against RuneLite
1.12.38 data, covering bosses, raids, skills, and other activities. It uses one
entry per base pet: cosmetic morphs and alternate raid modes do not add wedges.
Every included pet has equal selection odds; these are wheel odds, not pet drop rates.

- **Include bosses**, **Include raids**, **Include skilling**, and **Include other
  activities** all default on. Skilling bosses such as Wintertodt and Tempoross
  are under bosses; Hunter activities are under skilling. Other activities cover
  master clues, Barbarian Assault, Soul Wars, Guardians of the Rift, and chompy hunting.
- **Exclude pets already owned** defaults on and requires login. Reads the local
  unlocked-pet flags used by Probita, including lost pets that can be reclaimed.
  Base forms and known item variations share ownership.
- **Included pets** is an independent saved checklist. Uncheck pets you do not
  want to hunt. Category changes preserve individual selections.
- The compact result box shows the **pet icon on the left**, the **pet name at the
  upper right**, and a **source icon and name below** (for example, Vorki / Vorkath,
  Heron / Fishing, or Olmlet / Chambers of Xeric). It stays hidden before a result.
- Pet artwork comes from local item sprites. Sources use boss/raid sprites,
  skill icons, or representative activity items. Unavailable artwork falls back
  to initials and retries on subsequent pool refreshes.

Pet Hunting does not apply the Bossing wheel's level, quest, or Slayer checks.
Turn off ownership filtering to use the list offline;
live item artwork requires game-cache data. Ordinary companions and cosmetic
pet transformations are not separate hunt targets. New pets require catalog updates.

Pet item IDs and names are defined in RuneLite's `gameval.ItemID`. Source mappings are maintained in `PetDefinition`; the
[OSRS Wiki pet catalog](https://oldschool.runescape.wiki/w/Pet) is a development
reference, not a runtime dependency.

## Questing

- **Novice, Intermediate, Experienced, Master, Grandmaster**, and **Special**
  difficulty checkboxes all default on, using local quest-journal metadata.
- Only **not-started and in-progress quests** are included; completed quests are
  always excluded. Released main quests appear once, including Recipe for Disaster
  as one parent quest. Miniquests and individual subquests are not separate wedges.
- **Exclude ineligible quests** defaults on. Uses the game's quest requirement
  check for real skills, prerequisite quests, quest points, combat level,
  membership/world access, and supported special conditions. These are the journal's
  eligibility checks, not a check for every item needed during a quest.
- **Included quests** has its own saved checklist. Every checked eligible quest
  has equal odds and uses the quest icon in its result.
- When all released main quests are complete, the quest-icon results box says
  **Congrats, you have completed all the quests!** SPIN is disabled. Empty filters,
  manual exclusions, missing data, and logout never produce that congratulations.

Login is required. Quest, pet, and access changes refresh the current pool through
varp/varbit events, batched to at most one pool refresh per game tick; no remote account requests are made. Missing quest or
pet metadata gives an unavailable-data message and can be retried with a checkbox.

## Custom wheels

1. Select **New custom wheel...** from the dropdown, enter a name, and click
   **Create** (or press Enter). Each saved wheel appears as **Custom: Name**
   in the same dropdown.
2. Type an item in **New Entry** above the scrollable list and click **Add**, or
   press Enter. New items start checked. Each row has a checkbox, the item's name,
   and a trash icon on the right.
3. Uncheck an entry to leave it saved while excluding it from spins. Click its
   trash icon to remove it. Each enabled entry has equal odds; duplicate item
   names are allowed and remain separate entries.
4. Spin in the usual centered popup. Empty lists and lists with every entry
   unchecked disable SPIN. Editing controls are disabled during a spin.
5. To remove an entire custom wheel, select it in the dropdown and click the red
   **Delete wheel** button at the bottom, beneath the list and entry box. Confirm
   the dialog to delete the wheel and all its entries, or cancel to keep it.
   After deletion, the first remaining custom wheel is selected; deleting the
   last custom wheel returns to Bossing.

Names, entry order, enabled states, and the selected custom wheel save automatically
in the current RuneLite settings profile and survive plugin restarts. Custom wheels
work while logged out and have no account, quest, or raid filters. Changing a list
clears its previous result. Names support Unicode and punctuation as plain text;
wheel names must be unique among custom wheels (up to 80 characters), and entries
can contain up to 200 characters. Whitespace-only and multiline names are rejected.
Typing in the entry boxes does not trigger the popup's spin shortcuts.

Custom wheels use versioned JSON under `customWheels`, with stable IDs for wheels
and entries. Unreadable saved data is retained and custom creation is disabled
until valid data is restored; built-in wheels remain usable.

## Settings and architecture

All controls live in the sidebar. The two former master configuration items have
been removed; their saved values provide defaults for the replacement sidebar
options until a new value is saved. Wheel choice, filters, and each wheel's manual
exclusions persist through ConfigManager. Old unfinished-only Bossing settings
are no longer used. RuneLite profile changes reload the sidebar preferences.

- `WheelType` registers built-in wheel names and the Custom mode.
- `CustomWheels` stores named lists and per-entry enabled states with stable IDs.
- `BossChecklist` supplies the shared scrollable list and custom-entry delete icons.
- `WheelFilter` declares each wheel's checkbox labels, sections, defaults, and
  persistence keys. The sidebar builds its cards and selector from this registry.
- `WheelboundPanel` coordinates the shared popup, checklists, and optional XP spin.
- `WheelEligibility` implements pure skill/boss/encounter filtering.
- `AccountAccess` handles reviewed quests and current Slayer assignment rules.
- `QuestPool` reads released quest rows and the journal requirement script (5989).
- `OwnedPets` reads pet enum 985 and the three 31-bit unlock masks.
- `BossDifficulty` holds the local Bossing Ladder mapping.
- `PetDefinition` registers base pets, source categories, and local icon IDs.
- `BossData`, `CaEncounter`, `CaTier`, and `CombatAchievementCache` separate static
  task metadata from account completion state and retain per-task tiers.
- `WheelIconProvider` and `MonsterThumbnail` load local artwork. The existing
  `WheelComponent`, `WheelStyle`, and popup wheel rendering are retained.

Adding a wheel means registering its type and filters and supplying its eligible
entries; it reuses the selector, saved checklist, spin, cancellation, and popup.
The selector scales without squeezing additional tabs into the sidebar width.

Task enums 3981-3986 supply the six tiers; task parameters 1306, 1308 and 1312 identify
the completion bit, task name and encounter, and enum 3971 supplies names. CA model enum
3987 provides encounter models and viewing angles. The 21 exposed completion
varps are snapshotted on login/startup, profile changes, completion events, and
checkbox changes. Bursts are coalesced. There is no game-tick CA polling, per-spin
CA read, remote task list, or remote account lookup. Unknown task IDs fail closed.
Logout, hopping, and connection loss invalidate account results. Pool updates
during animation wait until the spin sequence ends and preserve the last result.

Development references: [quest requirement script](https://github.com/runelite/cs2-scripts/blob/master/scripts/%5Bproc%2Cquest_requirement_check%5D.cs2),
[pet unlock flags](https://github.com/runelite/cs2-scripts/blob/master/scripts/%5Bproc%2Cpet_insurance_pets_get_flags%5D.cs2),
[RuneLite Slayer assignment reader](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/slayer/SlayerPlugin.java),
[CA task loader schema](https://github.com/ehubbartt/combat-achievements-tracker/blob/main/src/main/java/com/catracker/util/CombatAchievementsDataLoader.java),
and [CA model interface script](https://github.com/runelite/cs2-scripts/blob/master/scripts/%5Bproc%2Cca_boss_init_image%5D.cs2).
These are development references, not runtime dependencies. Game-cache schemas
and reviewed access rules can require maintenance after game updates.

## Build and launch

Use **Temurin JDK 11** with the official example plugin's Gradle 8.10 wrapper.
Compilation enforces **Java 11** APIs and bytecode through `options.release.set(11)`.
RuneLite dependencies use **latest.release**; reload Gradle dependencies after client updates.

```powershell
.\gradlew.bat clean build
.\gradlew.bat run
```

If your terminal defaults to Java 8, point `JAVA_HOME` at your JDK 11 installation
for the command. On Unix use `bash ./gradlew build` and `bash ./gradlew run`.
Close the development client before rebuilding/relaunching. `WheelboundPluginTest`
is the development launcher; `shadowJar` builds the optional development client jar.

Tests cover filtering, tiers and completion bits, quest rules, Slayer task/location
rules, independent checklist persistence, profile resets, stale callbacks, input
handling, cancellation, and sequential skill/XP spins. Swing review images are
written under `build/reports/`, including previews of the five built-in sidebars and custom wheels at two heights.
Mocked tests cover the new quest states, requirement results, difficulty filters,
pet bitmask boundaries, completion result, and Mimic migration. Custom-wheel tests
cover saved lists, duplicate labels, checkbox state, deletion confirmation, profile
changes, offline spins, keyboard input, and the fixed delete button. They do not verify
live CA models, quest scripts, or account game-cache data.

## Manual RuneLite checks

1. Switch among all five built-in wheels and saved custom wheels. Confirm filters fit, checkboxes retain the
   existing look, and no wheel appears in the sidebar. Check fixed/resizable clients.
2. In Bossing, toggle every difficulty tier, Filter by Skill Level, Include Mimic, and raids.
   Uncheck individual entries and verify they stay unchecked after switching wheels.
3. Compare a quest-locked boss with your account. Test a current Slayer assignment,
   a completed task, a direct boss assignment, and a location-restricted task.
4. In Skilling, check combat exclusions and level-99 exclusions separately and
   together. Verify Slayer/Sailing and the optional second XP spin.
5. In CAs, compare an encounter with the in-game CA list. Uncheck its only remaining
   tier and confirm it disappears. Check ordinary monster icons and raid categories.
6. Uncheck all tiers or all checklist entries. Confirm SPIN is disabled with an
   explanation. Reinclude an entry and confirm it can spin again.
7. In Pet Hunting, toggle each source category, uncheck a pet, and switch modes.
   Confirm selections persist and results show the pet and source icons with both names.
   Check live item sprites for boss, raid, skilling, and activity pets.
8. In Pet Hunting, compare owned pets against Probita (including a reclaimable pet
   and a morphed pet). Toggle ownership filtering, then hop and switch accounts.
9. In Questing, test not-started, started, and completed quests; toggle difficulty
   and eligibility. Compare skill, prerequisite, and special requirements with the
   quest journal. Check all-quests-complete separately from an empty filtered pool.
10. Create two custom wheels, add several entries, disable one and delete another.
    Switch between the wheels and restart the plugin; verify names and checkboxes
    persist. Try cancelling and confirming Delete wheel, including the last wheel.
    Add enough items to scroll, and check the input and red delete button stay
    in their intended positions in short and tall sidebars. Confirm Space/Enter work when typing.
11. Log out, hop, switch accounts/profiles, and disable the plugin during a spin.
   Confirm no stale account result or second XP spin survives cancellation.

## Privacy and migration

No analytics, third-party API, Wiki scraping, cloud account state, or Wheelbound
server is used at runtime. Artwork comes from local RuneLite/game resources.
Wheelbound never generates game inputs or handles credentials. Previous persistent
skilling goal/config keys remain inert. No Plugin Hub approval is implied.
