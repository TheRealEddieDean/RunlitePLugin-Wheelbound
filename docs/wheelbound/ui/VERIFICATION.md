# Verification and limitations

Prepared 2026-10-29 America/Chicago; source audit base `223950ca4088fb69cb3ca918376a52140eca630d`.

## Checks performed

- Read-only `python docs/wheelbound/ui/validate_ui_packet.py`: 43 unique task sections/index IDs, required task fields, existing parent references, acyclic child dependencies, queue discovery rows, local Markdown targets and 10 parseable SVG boards. The validator is planning support only; it is not a supervisor/parser test or a gameplay test.
- `git diff --check`: whitespace validation. Changed scope is new `docs/wheelbound/ui/**` plus appended sections of TASK_QUEUE and IMPLEMENTATION_BACKLOG. Original queue bytes/history preserved; no Java, resource, build, script, schedule, global session ownership or canonical gameplay decision changes.
- Inkscape rendered all 10 SVGs to 1200×800 PNGs in temporary QA storage; inspected Fate Tree at full scale and all boards in a contact sheet. No clipping or overlapping content found at that preview size. These are static layout wireframes, not connected UI, real sprites or final OSRS artwork. Warning from GTK RecentManager did not prevent successful exports.
- GitHub API verified merged PRs #56 and #57 and matching merge commits; local git reviewed corresponding source diffs. Earlier/full CI execution is not independently asserted from PR-body claims.

## Backlog counts

| System | New child tasks |
| --- | ---: |
| Shared foundations/sidebar | 8 |
| Fate Tree | 13 |
| Hub visual integration | 1 |
| Cards, objectives, Grand, wheel assets, quests, Challenge | 7 |
| Builder | 3 |
| Shops and bounties | 3 |
| Defy, taint, punishment, Pardon | 4 |
| Settings/help | 1 |
| Lifecycle/performance/final acceptance | 3 |
| Total | 43 |

5 immediately implementable independent foundations (10,11,12,18,22); 38 have dependencies. 28 Category A / 15 Category B final-visual gates. Visual gates overlap dependency counts. All new implementation tasks remain TODO; preview designs await review. Category A permission takes effect after this planning PR is approved/merged and required checks pass; Category B technical scaffolding does not approve final visuals.

## Not verified / remaining work

No Java/Gradle tests were run for this documentation-only change. No production UI was implemented or live client launched; no FPS, memory, focus, HiDPI, purchase, evidence detector or complete gameplay pass is claimed. Future targeted tests are specified per task. Runtime supervisor scripts and Windows prompt are not present, so automatic consumption cannot be verified; append-only discoverability links follow the documented queue workflow. Verify a normal-refresh discovery log after merge before calling that operational. No supervisor changes or interruption were attempted.

Final Taint/Punishment wheel art, whole-catalog four-branch Tree review and connected-client screenshots remain outstanding. GF4 reference stays unchanged; B3 original selected image is absent in repository and its committed selection text is used. Prices/catalog/access/current-client and atomic transaction holds remain as documented.

Planning branch must be reviewed and approved before merge. No auto-merge enabled or merge performed by this workstream.

## Simple UI coverage expansion

36 unique screen/flow routes now map to the existing 43 implementation tasks: 6 Core, 10 Fates, 3 Grand Fate, 10 Progression/shops, 5 Risk/recovery and 2 Utilities. These are review prototypes and expanded acceptance, not production screens. Ordinary host controls replace decorative presentation for the initial testable version; WB-UI-29 no longer depends on the custom canvas. All mutation/evidence/price gates remain.

Validated the manifest’s unique IDs, navigation destinations, task mappings, embedded-data parity and command-disabled defaults, plus Node JavaScript syntax. Existing planning ID/link/dependency checks still pass. Chromium is not installed in this workspace, so browser interaction/layout rendering was not executed and no desktop/client pass is claimed for the new prototype. Earlier PNG inspection applies to the earlier wireframes only. Independent gameplay work remains untouched; planning PR remains unmerged.

## Approved handoff and scope reconciliation

User approved the initial simple UI and instructed merging PR #58. Added UI_IMPLEMENTATION_HANDOFF.md with the approval, 36-screen scope, seven milestone groups, action-service/evidence boundaries, later upgrade seams and normal supervisor discovery limits. Existing 43 task IDs remain: 32 initial plain-control increments, 11 explicitly DEFERRED optional visual/art increments; four independent initial foundations, 28 initial tasks with dependencies. No active initial task depends on a deferred visual task. Original Category B counts above are historical for the earlier styled concepts, not approval holds on ordinary initial controls.

Planning validator checks those counts/dependency exclusions as well as the existing ID/link/schema/preview assertions. Added queue discovery records preserve task history. No production Java, runtime assets, gameplay values, supervisor script/schedule or active session lease changed. Existing design-head Build Wheelbound workflow run 40993839944 completed successfully; the updated handoff commit must obtain its own current successful CI before merge. Browser/client testing remains outstanding as previously documented.
