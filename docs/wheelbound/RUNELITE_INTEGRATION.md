# RuneLite integration
Recovery checkpoint: technical feasibility unverified. Existing S5 documents local ConfigManager, client-thread reads, Stat/varbit refresh, quest journal scripts, CA cache and local icons; those are repository claims, not fresh live-client validation.

Research required: account hash and RS-profile storage; StatChanged XP; quest state and requirement data; CA task bitsets/thresholds; boss kill/drop attribution; shop widget/NPC linkage; MenuOptionClicked consumption coverage; GE/trade coverage; inventory/equipment versus unopened bank; Death's Coffer actual donation/value evidence; disconnect and migration behavior.

For every detector record API/source URL, retrieved date, known coverage, unresolved live scenarios, policy constraints. Event availability alone is not proof of reliable challenge enforcement. No automated gameplay actions, packets or credential use. Developer-only mode excluded from production builds. Hub acceptance requires reviewer/live evidence.

## Primary-source evidence checked 2026-10-09
| Area | Evidence | What is established | Remaining work |
| --- | --- | --- | --- |
| Per-account storage | [ConfigManager](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/config/ConfigManager.html) | getRSProfileKey/get/setRSProfileConfiguration exist | Cross-settings-profile run continuity, flush durability and crash consistency |
| Account identifier | [OAuthApi](https://static.runelite.net/runelite-api/apidocs/com/jagex/oldscape/pub/OAuthApi.html) | Account hash method, unavailable sentinel -1 | Login/account-switch lifecycle and storage mapping |
| XP | [StatChanged](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/StatChanged.html) | Skill, total XP, real and boosted levels supplied | Delta baseline/session ordering; event does not encode training method |
| Items | [ItemContainerChanged](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/ItemContainerChanged.html) | Container changes reported for withdrawals, pickup, drops | Source attribution, unopened bank completeness and Coffer donation proof |
| Menu prevention | [MenuOptionClicked source](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/events/MenuOptionClicked.java) | consume prevents vanilla processing of that menu event | Full vendor/GE/trade coverage, alternate inputs and policy acceptance |
| Hub review | [Official review policy](https://github.com/runelite/runelite/wiki/Plugin-Hub-Review) | Review concerns security/game rules; does not validate correctness/performance | Live testing and actual review remain required |

API documentation currently presents 1.13.1; repository's older 1.12.39 audit is not current release evidence. No API spike or live game testing performed. These are API availability findings, not full technical feasibility confirmation.

OSRS Wiki direct access was blocked by robots.txt. Search excerpts for Radiant Oathplate conflict on 2,500 versus 10,000 aether runes per piece. Keep exact rune cost unresolved rather than pick an excerpt. Coffer excerpts support per-item 10,000-or-more and 105% GE valuation, but live exclusions and boundary still need reliable current verification. No game-fact correction promoted to confirmed from blocked/stale pages.
