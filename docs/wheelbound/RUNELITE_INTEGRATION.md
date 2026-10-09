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

## Additional primary sources checked 2026-10-09
| Area | Source | Evidence and implementation limit |
| --- | --- | --- |
| Quest completion | [Quest API](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/Quest.html) | getState(Client) exposes quest state; not complete material/team/access eligibility |
| NPC drops | [NpcLootReceived](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/events/NpcLootReceived.html) and [LootManager source](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/game/LootManager.java) | NPC/item association exists; group loot, raid reward chests and no-loot kills need separate detectors |
| Login lifecycle | [GameStateChanged](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/GameStateChanged.html) | Game-state event exists; account token and reconnect baselines still needed |
| CA points | [generated VarbitID](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java) | CA_POINTS constant found; thresholds and task/catalog mapping still require current cache validation |
| Durable local files | [Filepath API](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/util/Filepath.html) | Scoped plugin directory, file channels and moves available; cross-platform atomic persistence needs actual tests |
| Review constraints | [Rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features) | Use Java, injected Gson, scoped Filepath; avoid generated game inputs, focus manipulation and forbidden menuAction calls |

RuneLite policy checked after page edit 2026-10-06. It flags conditional menu removal and content simulation; do not assume voluntary challenge blocking is automatically accepted. Offline economy Python harness is design tooling, never a shipped in-client OSRS encounter simulator. Developer mode must manipulate local test fixtures only and must not issue server actions.

Pinned source blobs inspected: ConfigManager 6bdd1cf40c47252d35e05217ffc3089d569fc8b8; LootManager dcec3f84c3d5dc5eedcac695aef16bb1c289c5e1; VarbitID 0a90c1426122a9fbf32c3002fc66b77dac8d0fc4. Paths above resolve current master; use blob hashes to reproduce the inspected revision.

Official game updates show Coffer eligibility changes:
[Sailing Changes and Gem Bag Expansion](https://secure.runescape.com/m=news/sailing-changes-and-gem-bag-expansion?oldschool=1) reports unblocking Sailing items.
[More from the Getting Around Poll](https://secure.runescape.com/m=news/a=13/more-from-the-getting-around-poll?oldschool=1) adds named Deadman items.
[Bank Tags and Trouver Rework](https://secure.runescape.com/m=news/bank-tags-trouver-system-rework--more?oldschool=1) adjusts item values relevant to Coffer.
Therefore price threshold alone is not an authoritative eligibility detector; validate donation UI acceptance and actual receipt. No live Coffer verification was performed.

## Original Hub review and current capability audit
See [POLICY_AND_FEASIBILITY](POLICY_AND_FEASIBILITY.md) for PR 16702 comments, original build log root cause (fresh Gson), accepted focus-manager fix, current Jagex rules, and a per-system capability matrix. Research is complete at the source-inspection level; no live-client proof or approval of new mode enforcement was obtained.

New candidate signals: VarPlayerID awakened kill totals 3971-3974 and VarbitID CA_THRESHOLD_MASTER=14813 with CA_POINTS=14815. A zero/uninitialized threshold must not trigger win. Account/session/attempt eligibility and threshold changes need validation. Diary completion flags exist but do not prove reward claim. ServerNpcLoot supplements loot evidence; raid chests and lootless kills are not covered universally.

Preserve Trade with and show a local restriction notice on attempts (WB-D066). Current Hub-pinned Bronzeman Unleashed now provides connected consume-and-message precedent (WB-D067); retain feature-specific review and coverage checks, particularly existing-open trade/final Accept. Current InterfaceID identifies Coffer groups 670/671 and Confirm 0x029e000d. Historical cache 199.1 scripts map displayed balance to generic IF1=261, selection/quantity/quote to IF2-IF4; these are context-sensitive candidates, not live-verified permanent counters. Proposed donation proof correlates selection, Confirm, exact inventory delta and refreshed credited balance (WB-D068). Details and pinned sources are in POLICY_AND_FEASIBILITY.md.

