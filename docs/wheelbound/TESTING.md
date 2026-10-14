# Testing acceptance
No plugin tests were run or code changed for recovery checkpoint.

Required invariants: five ordinary/three activities/24 including specials; weights normalized; fixed enhancements independent of lifetime counts; duplicate counters survive destruction; store retains UUID; Defy odd schedule and post-cleansing refill; first Defy two inserts; negative FP recovery; Reject consumes one and no reward.

Transaction crash tests before/after journal and snapshot writes, replay completion/claim/donation, account switch and stale callbacks. Pause must exclude XP/drop/quest/CA/bounty progress and remove restrictions. Grand Fate attempts no spin debit, preserve checklist, no readiness gate.

Live tests: current quest/CA mappings, all covered shop/GE/trade inputs, Death's Coffer deposits/values, bank snapshots, disconnect/reconnect. Developer testing only in dev builds.

Simulate >=10,000 seeded accounts across focused/balanced/inefficient strategies, including varied XP/time/rewards, purchase policies, finite spins, Defy, Taint, sacrifice, negative FP and impossible-access cases. Report assumptions and missing gear/quest realism honestly.

## Correction acceptance
Bossing Lesser/Standard/Greater: selected before random boss, no boss choice/reroll, Lesser retains base FP, Greater increases count/reward. Assert starting Standard default, two distinct targets, same-type permitted, purchased pool persists, third upgrade yields three. Restore pending offers/card/boss across crash boundaries. Assert repeated Pardons at same price with no account/run cap, Banned -> Locked only, and independent vendor unlock/gamble afterward.

## Confirmed recovery and Taint accounting
Verify empty-set Sacrifice enters acquisition recovery, accepts a newly obtained eligible item worth at least 10,000 GP, rejects below-minimum/ineligible/protected items, and blocks ordinary progression until verified donation. Unknown bank contents must not trigger empty-set recovery. Verify one Taint spin charge across completion/restart/replay, with no second Punishment debit. Verify one Sacrifice spin charge before its spin reward, including acquisition recovery and restart; donation completion must not charge another spin.

Current offline simulation replay must also agree across process hash seeds, not merely two calls in one interpreter. Preserve historical results with their source revisions; do not relabel them as current-rule runs.

## Submission regression and future live verification
Static production review must catch fresh Gson/OkHttp construction, KeyboardFocusManager, forbidden input/server actions, restricted scripts, unsafe persistence and unexpected packaged dependencies. Reproduce the Hub standard packaging scan, not merely local Java compilation: the initial Gson failure happened during packaging. Current official example/standard target is Java 11; align actual Hub tooling when implementation is authorized.

Future controlled live cases: CA points/threshold initialization and change; four awakened counters on ordinary versus awakened kills; paused/pre-run/reconnected completions; NPC loot/server loot/chests/no-loot; vendor identity and bank completeness; Coffer donation receipt versus drop/bank/consume. GE initial EMPTY states must not create penalties. Do not test a prohibited menu-removal workaround in production. No such live tests were performed now.


## Focused future detector acceptance cases (WB-D066-068)
Trade: outgoing left/right click; incoming chat and alternate acceptance; first/final window Accept; already-open trade when activation or restriction changes; pause/resume; character switch; unknown target; duplicate attempts; interaction with other menu plugins. Verify option text/order stays intact, restricted request is not sent on covered routes, notice does not steal focus, and dismiss sends no action. Warning-only mode, if used for testing, must clearly say it does not block. Do not introduce gameplay penalties from attempt logs.

Coffer: initialized zero/nonzero balance; one item, stack, noted variants; quantity 1/5/X/All; selected slot changes; actual final confirmation; failed/full/ineligible donation; quote/base-value/credit rounding; Blessed exclusion; inventory delta before/after balance event; UI closure; login/reconnect and shared IF1 reuse; repeated callbacks; two identical successive donations. Capture current group/widget tree, varp values, server-displayed quote and event order. Expected: only a matched confirmed donation settles once; drop/bank/consume/player trade and ambiguous reconnect do not award. No live results are claimed.

## Detailed technical resolution checkpoint

[TECHNICAL_SPECIFICATIONS](TECHNICAL_SPECIFICATIONS.md) supplies current detector contracts, route coverage, initialization rules, source evidence and explicit live-validation gates (WB-D069–075). Its refined Coffer route uses visible initialized UI/server quote and balance as primary evidence; the old IF-variable mapping is optional pending validation. Method-specific templates require a supported catalog. Vendor inspection does not unlock transactions. New-account scope is confirmed by WB-D088; full Coffer recovery and exact restrictive policy acceptance remain in [OPEN_QUESTIONS](OPEN_QUESTIONS.md). No live test or production implementation is claimed.

## Catalog drafting checkpoint

See [CONTENT_CATALOGS](CONTENT_CATALOGS.md) and [CATALOG_ACCEPTANCE](CATALOG_ACCEPTANCE.md) for newly authored drafts (WB-D079/080), distinct from missing historical catalogs. Machine-readable 100 bounty candidates, 50 punishments, 67 repository boss categories and 213 quest enum records are in catalogs/. Source metadata is not runtime eligibility. All unvalidated bounty/punishment entries remain disabled. H3 exercises named bounty/reward and plain-XP punishment drafts in synthetic trials only; exact quest/boss access remains unmodeled.

## Acquisition-recovery pause case

WB-D085: trigger empty-candidate recovery, pause, acquire eligible item, resume and validate donation. Paused acquisition awards no XP/drop/bounty credit; active verified donation settles once. Reject using this exception to overwrite a nonempty frozen candidate list. Forced-item demand is confirmed random (WB-D033); Q7 is resolved. Empty-set acquisition recovery is a scoped exception and cannot overwrite an existing random demand.

## Actual build evidence and planned trace packet

Remote CI run38025748732 actually executed compileJava,compileTestJava,test and build successfully; source has70 declared test methods but the workflow exports no executed test-case count. See [build baseline](research/BUILD_BASELINE.md). [Capture protocol](research/TRACE_CAPTURE_PROTOCOL.md) defines46 NOT_RUN live cases and source-pinned evidence requirements. Schema/order/origin fixtures validate offline envelopes only; they do not create client observations or approve restrictive controls.

## Executed offline receipt acceptance, WB-A24

Run `python3 automation/receipt_safety/checks.py --verify` from repository root. Saved results cover44 expected traces plus32 operation/write-boundary combinations;10 intentional guarantee violations are detected and6 malformed packets rejected. Each main case is replayed for deterministic interpreter agreement. Source hashes and exact results are saved in that directory. This is zero production/client/disk-I/O tests and adds zero balance trajectories. Actual Java/OS persistence, concurrent ownership, migration and live adapter acceptance still require their separate tickets. A claim while paused of evidence earned earlier while active remains unspecified, not rejected as backfill.

## New required fixtures from WB-D111–114 (NOT_RUN)
Known-empty Questing landing shows notice, keeps IDs/weights/modifiers and automatically retries; unknown pool does not retry; saved automatic retry/result cannot become a player reroll. WB-D120: empty Questing attempts allocate zero additional spins; eventual valid Fate allocates one, including across restart/repeated callbacks. Qualifying active bounty automatically credits once without panel/claim; duplicate event/restart cannot repay; failing/rejecting assignment cannot erase it. Paused processing yields no new FP even for earlier active evidence, which remains preserved. L/S/G objective ordering applies to both Skilling and Bossing, with previous reward distinctions and random boss/no reroll intact. Mixed CA aggregate remains unanswered; no fixture may assume choice5. Old A24 manual-claim/Q10 gates are prior-contract evidence, not new Java coverage.

WB-D116/117 new acceptance (NOT_RUN): Master Grand uses actual hilt5 item, not CA-points-only completion. Mixed active/paused prerequisite CA history does not disqualify it; no anti-cheese task audit. Wrong hilt/name-only/unknown state cannot prove the item objective; paused processing grants no completion. Bounties Shop item→FP is tested separately from Grand and any retained CA/diary reward system.

WB-D118 planned acceptance (NOT_RUN): granted starting Mining makes next bought Mining duplicate1; never-owned activity first paid slice is base then duplicate1; destroy all copies retains subsequent price tier; moving/returning same instance does not increment; failed/cancelled/replayed purchase cannot increment twice; activity B unaffected by activity A history. Migration cannot infer never-owned from no current slices.

WB-D120 planned acceptance (NOT_RUN): N positive spins→several known-empty Questing landings→valid ordinary/special outcome yields N−1 total, not one decrement per animation. Empty-only pending attempts have no extra cost or reward. Lost acknowledgement/restart at an empty attempt or after valid commitment cannot double debit/reroll a valid outcome. Zero initial spins still require ordinary exhaustion flow before a new draw.

WB-D121 new planned acceptance (NOT_RUN): ordinary Questing store with funds/room/final5/3 succeeds; direct/compound/forced-displacement destruction fails without debit or loss; no space/FP blocks its storage rather than enabling destruction; return retains instance/modifiers/history; other valid non-Questing destruction remains free. Migration cannot erase protected Questing to repair capacity.

WB-D122/128 planned acceptance (NOT_RUN): permanent Bossing Challenge unlock; item-hunt objectives only; CA excluded, unrelated item possession/source excluded; assigned supported fresh drop completes once; restart preserves target; L/S/G unchanged. Rates/rewards/catalog evidence remain reviewed candidates.

WB-D123 planned acceptance (NOT_RUN): reviewed eligible non-boss creature appears only in Challenge target draw; ordinary L/S/G catalog unchanged; no choosing/reroll; invalid/unsupported targets excluded; restart restores same pool/version/target; stale changes cannot resample. Card-type unlock is not assumed equivalent to granting every target.

WB-D124/125 planned cases (NOT_RUN): both Bossing prerequisite keys required; Skilling counterparts cannot substitute; insufficient FP/persistence failure leaves no partial card/bundle; replay grants starter once and no normal boss-pool mutation; expansion purchase grants its target once. Shared rare-table/common generic target excluded despite rarity; supported specific unique/CA sources accepted only via reviewed evidence. Bundled-owned but inaccessible targets are not counted eligible; WB-D126 minimum3 distinct actually eligible targets now confirmed.

WB-D126 planned acceptance (NOT_RUN):2 actual targets blocks new Challenge offering;3 allows eligible owned type; three tasks on one creature still1; unknown/inaccessible/unsupported targets excluded. Eligibility loss retains card/bundle, ordinary L/S/G offerings and existing committed targets. Restored eligibility reenables new offerings without purchase/debit. No effect on initial2/upgrade3 card count. Starter bundle grant remains atomic.

WB-D128 replaces prior family tests: no CA family/task offered, no family draw/selector, no target picker/reroll; injected random provider selects eligible creature/item; restore same objective; three-target floor counts only valid item-hunt targets. Unsupported/unknown/CA-only entries cannot qualify.

WB-D129 planned acceptance (NOT_RUN): owned item still eligible; positive prior possession/collection log/preassignment drop never completes; bank withdrawal/buy/trade/drop-and-repick rejected; fresh active assigned-source drop completes once; unrelated-source or paused earning cannot; duplicated delivery/restart cannot repay. Repeated Challenge does not reset an already paid permanent bounty. Full-inventory/delayed delivery review remains a live evidence gate, not automatic failure.

WB-D130/131 planned acceptance (NOT_RUN): exact selected logical unique versus different unique on same source;1/400 and1/1500 included,1/399 and1/1501 excluded; equivalent fractions normalized; any-unique/subtable probability cannot stand in for item rate. Unknown/shared-table/out-of-band targets not enabled;≥3 distinct targets count only valid hunts. No automatic completion at1500 kills; fresh assigned active selected item still required. WB-D133/134 personal-rate/context cases below are planned, not executed.

WB-D133/134 planned acceptance (NOT_RUN): personal1/2000 versus group1/500 excludes; reviewed personal1/500 passes other gates; teammate loot/trade fails. Fully in-band range passes; straddling/unknown range fails. Unknown context during hunt preserves assignment without reroll/reward; later valid evidence resumes existing hunt. Paused earning never backfills. No actual encounter formula or detector verified.

WB-D135–137: follow [Challenge assignment contract](CHALLENGE_ASSIGNMENT_CONTRACT.md). Freeze eligible canonical pool/context/reward schedule and offer randomness before display; card selection precedes durable uniform creature-then-item result and exact reward reveal. Variants/contexts are not extra lottery entries. Challenge uses fresh-drop completion, no KC quota or item consumption. Receipt/restart/odds cases are planned NOT_RUN; actual catalog/quotes/source adapters remain gated.

WB-D138/139: [unavailable selection contract](UNAVAILABLE_SELECTION_CONTRACT.md) extends free automatic Master retry only to verified-empty ordinary activity pools before valid offer/result commitment. Keep full wheel weights; unknown/unsupported positive-weight outcomes block readiness, not normalize out. All verified-empty ordinary outcomes stop before drawing/debit without failure/reward; use existing legal recovery/Pause/eligible Grand. Supported special paths retain mandatory obligations and cannot be skipped. Persist unresolved selection stream/history; no valid target reroll. Postcommit invalidation recovery remains separate; all cases planned NOT_RUN.
