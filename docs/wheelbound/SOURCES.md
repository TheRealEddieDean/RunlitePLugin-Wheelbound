# Sources and provenance

Checked 2026-10-09. Current user approvals outrank master-assignment reconstruction, older assistant proposals and the condensed PDF. Full shared transcript is inaccessible; see OPEN_QUESTIONS. Personal Context returns partial excerpts, not a full export.

## Primary sources
- [Original Wheelbound Hub review 16702](https://github.com/runelite/plugin-hub/pull/16702): Gson packaging/build failure and forbidden KeyboardFocusManager recovered; preserve these actual blockers.
- [Jagex third-party guidance](https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1): Trade with removal/reordering restriction and similar-feature caveat.
- [RuneLite rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features): current source/review restrictions; peer acceptance does not approve new exact behavior.
- [Death Changes](https://secure.runescape.com/m=news/death-changes?oldschool=1): individual-item 10,000 GP minimum, 105% credit. UIM section describes different death mechanics; it does not itself prove current Coffer eligibility.
- [Poll 84](https://secure.runescape.com/m=news/poll-84-afk-timers--more?oldschool=1): GE Repeat/Modify routes and Radiant cost reduced to 2,500 aether runes per piece; retroactive CA updates.
- [Yama contracts](https://secure.runescape.com/m=news/yama-the-calm-before-the-contract-storm?oldschool=1): contract/sigil context, not generic boss-KC proof.
- RuneLite source commit 42a6f17a6a2e8e478aa763890ecd0181a59dad38; [source pins](research/API_SOURCE_PINS.json), detailed source paths and peer pins in TECHNICAL_SPECIFICATIONS and POLICY_AND_FEASIBILITY.
- Repository build files at dc76720971c3e15c2e831a27b1505dd25054267f; DEVELOPMENT_PIPELINE lists inspected configuration and distinguishes source-reviewed commands from executed builds.

## Source-confidence rules
Current game constants identify possible signals, not tested completion receipts. Historical RuneStar cache scripts are explicitly dated 2021; generic IF-variable mappings require live validation. Listed Hub peers demonstrate observed implementation precedent, not comprehensive transaction coverage or blanket policy approval. Unknown telemetry is not evidence of player misconduct. Wiki access failures must not be hidden behind guessed current facts.

## Catalog sources checked 2026-10-09

RuneLite ItemID blob 1bb88f6044f84f89cbe1a5e99ccc688d43ff631c verifies draft item identity; Quest blob b40a73187f514fa5abdfe93db0e76203c20c5acb supplies 213 metadata records. Repository BossDifficulty blob 50dd6bdfffa56f4f3b96274bcb2d07d92fe90141 supplies 67 normal-wheel category mappings. These do not verify loot ownership, drop rarity, main-quest eligibility or mode-tier approval.

- [Jagex Sailing launch](https://www.jagex.com/news/set-sail-in-old-school-runescape-explore-gielinor-like-never-before-in-massive-sailing-update-available-today), 2025-11-19.
- [RuneLite Skill API](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/Skill.html), SAILING checked 2026-10-09.
- [Jagex CA reward list](https://secure.runescape.com/m=news/combat-achievements-expansion-rewards?oldschool=1), 2022-11-01 current-rewards list identifies Master Hilt 5. New proposed rewards elsewhere on that page are not assumed accepted.

## S8 recovered shared conversation
Direct HTTPS fetch succeeded after the web-reader failed. [Extraction record](research/TRANSCRIPT_EXTRACTION.md) and [visible transcript](research/SHARED_CONVERSATION_TRANSCRIPT.md) preserve the selected chain with stable S8 message locators. 436 visible messages recovered; source reconciliation is in progress. Earlier “shared link inaccessible” claims describe the failed first attempt and are superseded by this actual extraction. Separate named conversations/attachments remain unproven.
