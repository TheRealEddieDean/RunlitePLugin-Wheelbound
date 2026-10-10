# Diary and Combat Mastery receipt review

WB-A10 is PARTIAL: source mappings for all 48 region/difficulty diary objectives are recorded, but live value/timing semantics, task-to-boss Combat Achievement grouping, reward amounts and the remaining daily/sink review are unfinished. No production adapter is enabled.

## Diary mapping resolved

The [48-entry source map](research/DIARY_COMPLETION_SOURCE_MAP.json) pins RuneLite commit 42a6f17a6a2e8e478aa763890ecd0181a59dad38. Generated VarbitID contains 45 region-and-tier DIARY_*_COMPLETE symbols. Early Karamja tiers instead use ATJUN_EASY_DONE = 3578, ATJUN_MED_DONE = 3599 and ATJUN_HARD_DONE = 3611. The legacy Varbits DIARY_KARAMJA_EASY/MEDIUM/HARD aliases identify the same numbers. This resolves the apparent three-entry gap without substituting counts or reward-claim flags.

Primary sources: [generated VarbitID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java), [legacy aliases](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/Varbits.java). Symbol names and matching IDs prove source mapping, not live completion timing or exact value encoding. Validate initialized transitions on the current client before awarding credit; an unread value is not an incomplete diary.

A diary payout key includes run/account, region, difficulty and authored catalog version. Persist a qualifying completion receipt and unclaimed status before manual payout. Never grant one payout per task, per login, per reward collection or per repeated positive snapshot. Reward-claim flags, diary-start flags and task counts are different signals. The first live trace must establish whether the completion symbol changes upon the final task or another interaction; the earning boundary cannot be guessed from a name.

Active permitted completion qualifies under WB-D101. Paused completion does not backfill. A previously completed positive snapshot establishes baseline, not a new receipt. Reconnecting while an initialized prior baseline was absent must not manufacture a transition; report unverified completion history. A persisted active receipt remains claimable without earning it again. Duplicate claims must settle once. Q10 controls disposition if the associated Fate was rejected; preserve evidence pending that rule.

## Combat Mastery differs from Master Grand Fate

Combat Mastery pays for all CAs of an authored boss, not collection-log green completion and not a generic CA-point threshold. Master Grand Fate uses the current Master-tier points threshold and is already specified separately. Its threshold cannot prove that one boss's complete task set is done.

The earlier candidate ACHIEVEMENT_TASK_COMPLETED_* wording is superseded: direct current source and two implementations identify **CA_TASK_COMPLETED_0–20** as Combat Achievement completion words. The generic achievement fields must not be used for CA progress. [The source contract](research/CA_COMPLETION_SOURCE_CONTRACT.json) records the explicit 21 ordered varp IDs, six task-tier enums, boss-name enum and struct parameters.

Existing repository BossData maps tier enums 3981–3986 to structs, task ID parameter 1306, boss parameter 1312 and boss-name enum3971, excluding General from encounter groups. Current source was verified on the Wheelbound branch. Independently inspected [CA Exporter source blob](https://api.github.com/repos/cdfisher/ca-export/git/blobs/3605aa60d12109894a4f14dcc202ed3314ad5d73) uses task ID/32 to select the completion word and task ID%32 for its bit. This is concrete read-only source precedent; no client execution or complete cached task list is claimed.

The ordered IDs are noncontiguous after the initial range: never calculate a varp as base plus index. Validate task ID nonnegative, word index within the explicit array, bit index0–31, available structs and nonempty task-tier metadata. A signed integer with bit31 set is valid flag data, not an unknown/negative completion counter. An out-of-range new task disables the affected aggregate until the mapping is reviewed; do not silently omit it and claim boss mastery. Task metadata loads on the client thread; remote services or display names are not required for local game-cache mapping.

Build two views: current initialized game completion state for display and supported active earning observations for Wheelbound credit. First initialized snapshot is baseline, never a newly earned payout. For mixed active/paused boss task histories, do not silently assume final-task completion alone authorizes every paused component; preserve per-task provenance and report the reward policy unresolved if it matters. The existing no-paused-backfill rule remains authoritative. Freeze the authored boss task set/catalog version for a pending claim; changes in the game's task catalog require explicit migration, not repeating an old payout.

Receipt fixtures: final qualifying active CA completes the boss set; paused final CA; pre-complete baseline; reordered packed updates; task metadata unavailable; seasonal/retired/changed task set; grouped normal versus challenge raid variant; unrelated boss task; manual claim twice; account swap; local crash after evidence before payout. Existing source inspection does not satisfy those live fixtures.

## Economy and remaining work

Diary/CA payouts remain independently balanced candidates and are absent from current H3 income. Do not introduce fixed rarity payouts or assume these rewards are required to keep spins solvent. Dailies remain optional, with three targets and no offline backlog under the delegated rolling-board proposal. Keep Blessing, Satchel, mastery and vendor sinks distinct from mandatory income assumptions. Finish the 18 daily source/access reviews and sink definitions before versioning a faithful next-model economy.

## Daily/sink review completion

[All18 daily conditional records](research/DAILY_REVIEW_A10_ALL.json) pin the unchanged input and require source-action/new-quantity evidence, actual access and frozen manual claim keys. FishingSpot's SHRIMP grouping includes other species and SALMON includes trout/pike: a fishing-spot label or animation alone cannot decide the target. Require the actual authored species receipt. Logs/ores need positive gather evidence; combat items need owned loot; bronze bars/bow strings need actual authored processing receipts, not buying outputs. No additional broad off-task permission is granted by an optional bounty.

Sink contract: activity unlock and slice add are separate purchases; duplicate costs escalate per activity lifetime; enhancement quote depends only on next tier; card pools separate; third offer upgrade applies to offered count; Satchel expansions sequential; Cleansing and Pardon unlimited fixed expensive prices; Pardon returns Locked; Blessed slots first free then two sequential paid slots; GE uses completion count+FP, no mandatory bounty/diary/daily gate. Freeze current purchase quotes and journal debits atomically. Debt may receive earned/manual-claim FP but cannot fund discretionary purchases. All numbers in BALANCE_CANDIDATE remain delegated candidates.

A10's documentary receipt/sink review is complete with conditional real-source gates; actual client traces, current cached CA task instances, live diary timing and numeric reward balancing remain outside this completion claim. A mixed paused/active CA-set reward policy remains unverified; next model excludes CA/daily/diary income rather than inventing that answer.
