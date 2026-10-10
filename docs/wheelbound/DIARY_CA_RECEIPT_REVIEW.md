# Diary and Combat Mastery receipt review

WB-A10 is PARTIAL: source mappings for all 48 region/difficulty diary objectives are recorded, but live value/timing semantics, task-to-boss Combat Achievement grouping, reward amounts and the remaining daily/sink review are unfinished. No production adapter is enabled.

## Diary mapping resolved

The [48-entry source map](research/DIARY_COMPLETION_SOURCE_MAP.json) pins RuneLite commit 42a6f17a6a2e8e478aa763890ecd0181a59dad38. Generated VarbitID contains 45 region-and-tier DIARY_*_COMPLETE symbols. Early Karamja tiers instead use ATJUN_EASY_DONE = 3578, ATJUN_MED_DONE = 3599 and ATJUN_HARD_DONE = 3611. The legacy Varbits DIARY_KARAMJA_EASY/MEDIUM/HARD aliases identify the same numbers. This resolves the apparent three-entry gap without substituting counts or reward-claim flags.

Primary sources: [generated VarbitID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/VarbitID.java), [legacy aliases](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/Varbits.java). Symbol names and matching IDs prove source mapping, not live completion timing or exact value encoding. Validate initialized transitions on the current client before awarding credit; an unread value is not an incomplete diary.

A diary payout key includes run/account, region, difficulty and authored catalog version. Persist a qualifying completion receipt and unclaimed status before manual payout. Never grant one payout per task, per login, per reward collection or per repeated positive snapshot. Reward-claim flags, diary-start flags and task counts are different signals. The first live trace must establish whether the completion symbol changes upon the final task or another interaction; the earning boundary cannot be guessed from a name.

Active permitted completion qualifies under WB-D101. Paused completion does not backfill. A previously completed positive snapshot establishes baseline, not a new receipt. Reconnecting while an initialized prior baseline was absent must not manufacture a transition; report unverified completion history. A persisted active receipt remains claimable without earning it again. Duplicate claims must settle once. Q10 controls disposition if the associated Fate was rejected; preserve evidence pending that rule.

## Combat Mastery differs from Master Grand Fate

Combat Mastery pays for all CAs of an authored boss, not collection-log green completion and not a generic CA-point threshold. Master Grand Fate uses the current Master-tier points threshold and is already specified separately. Its threshold cannot prove that one boss's complete task set is done.

The pinned generated VarPlayerID exposes ACHIEVEMENT_TASK_COMPLETED_* fields; this is candidate packed completion state, not a reviewed task-to-boss catalog. Source: [VarPlayerID](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-api/src/main/java/net/runelite/api/gameval/VarPlayerID.java). Next research must identify current task metadata, bit packing, boss/variant grouping, any hidden or retired rows, and supported initialization. Do not infer all tasks from hiscore kill counts or the number of a packed field. The 64 mode nodes group raids; determine the authored mastery grouping explicitly before modeling income.

Receipt fixtures: final qualifying active CA completes the boss set; paused final CA; pre-complete baseline; reordered packed updates; task metadata unavailable; seasonal/retired/changed task set; grouped normal versus challenge raid variant; unrelated boss task; manual claim twice; account swap; local crash after evidence before payout. Existing source inspection does not satisfy those live fixtures.

## Economy and remaining work

Diary/CA payouts remain independently balanced candidates and are absent from current H3 income. Do not introduce fixed rarity payouts or assume these rewards are required to keep spins solvent. Dailies remain optional, with three targets and no offline backlog under the delegated rolling-board proposal. Keep Blessing, Satchel, mastery and vendor sinks distinct from mandatory income assumptions. Finish the 18 daily source/access reviews and sink definitions before versioning a faithful next-model economy.
