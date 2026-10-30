# Approved simple UI — implementation handoff

Approved by the user on 2026-10-29 America/Chicago, after reviewing the 36-screen plain-control prototype. Repository: TheRealEddieDean/RunlitePLugin-Wheelbound. Target branch: `wheelbound-mode`. Integration PR: [#58](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound/pull/58).

## Approval and scope

User direction: “lets just go super simple for the UI for now just normal buttons until i get a hold of it”; layouts may change after testing; “build everything with the intention that we will upgrade the UI later”. After the expanded prototype the user instructed: “this will work for now, please create a backlog for builing this out etc and then do a pr and merege everything and document with an md file”.

This approves the initial standard-control interface and explicitly authorizes merging PR #58 after verification. The reviewed prototype version is design commit `b47919e0e2bf27cb151fd38be21562a2b1522ad1`. This handoff supersedes older statements requiring approval of this planning merge and routine plain-control layouts. Future elaborate artistic designs remain separate. Gameplay prices, eligibility, evidence and atomic-transaction rules are not changed by visual approval.

The PR provides design/specification/backlog and review-only HTML/JSON, **not production Java screens or completed gameplay systems**. Implementation tasks stay TODO until real code and required verification exist. The existing autonomous implementation worker should select them through its normal workflow; this planning workstream does not take over its gameplay task or modify its scripts/schedule.

## Deliverables and counts

- [Detailed implementation backlog](UI_IMPLEMENTATION_BACKLOG.md): 43 stable task IDs, WB-UI-10 through WB-UI-52, with descriptions, parent mappings, dependencies, expected behavior, affected modules, acceptance, tests, complexity and merge/review rules.
- **32 initial implementation increments** using plain controls. **11 optional visual increments deferred**, not deleted or marked complete.
- [36-screen acceptance matrix](SIMPLE_UI_SCREEN_COVERAGE.md): 6 Core, 10 Fates, 3 Grand Fate, 10 Progression/shops, 5 Risk/recovery, 2 Utilities. A screen/flow is not necessarily a new native window or a separate coding task.
- [Plain-control prototype](previews/simple-screens.html) and [manifest](previews/simple-screens.json): navigable review examples; gameplay actions disabled. Prototype controls are not a production UI toolkit.
- [Architecture](UI_ARCHITECTURE.md), [simple direction](SIMPLE_UI_FIRST.md), [approval record](UI_REVIEW_CHECKPOINTS.md), [audit](UI_AUDIT.md), [verification](VERIFICATION.md).
- Additive discoverability records in `automation/TASK_QUEUE.md` and `automation/IMPLEMENTATION_BACKLOG.md`. Existing IDs, history and active gameplay continuation remain intact.

## Build order

This is a UI workstream order when a UI task is selected normally; do not abandon an in-flight unrelated task. Each milestone may require multiple bounded PRs. Missing backend integration holds only the relevant live action, not unrelated UI foundations.

| Milestone | Existing task IDs | Usable result / completion boundary |
| --- | --- | --- |
| 1. Shared ordinary UI | 10, 11, 12, 14, 15, 16 | Reusable buttons/labels/resources/errors/confirmation, immutable snapshot/capability model, one owned window, routing, pending/result presenter. No fake live state. |
| 2. Connected status | 13, 17, 31 | Supported account/session binding and actual saved-run sidebar/overview; display only data really present, missing objectives/spins shown unavailable. Normal mode and Master unchanged. |
| 3. Plain progression | 18, 19, 20, 26, 29, 28 | Traced production catalog and shared prerequisite evaluation, list/detail and ordinary Unlock confirmation. Purchase disabled until a reviewed atomic service exists. No custom graph required. |
| 4. Inventory and shop screens | 39, 40, 41, 42, 43, 48 | Actual active slices/Satchel, domain-validated edit drafts, shop/vendor/cards/Pardon products, shared atomic purchase binding. Unknown prices are non-purchasable. |
| 5. Fate and Grand flows | 32, 33, 37, 38, 34 | Frozen offerings, owned cards, objectives/quests/eligibility, fresh hunts, Challenge ranks and sealed Grand checklist/attempt capability. No view-generated evidence or randomness. |
| 6. Rewards and mandatory risk | 44, 45, 47 | Automatic bounty receipt feedback, zero-spins/Defy consequences and retained Taint/Punishment/Sacrifice obligations. Unsupported Coffer evidence stays unavailable. |
| 7. Testing and iteration | 49, 50, 52 | Settings/help/notifications/receipts, lifecycle/account/legacy regressions and complete 36-screen acceptance report; user tests layouts and feedback drives small changes. |

Numeric IDs above mean WB-UI-XX. Dependencies in individual task records remain mandatory for **live integration**. Four independent initial foundations are 10/11/12/18; 28 other initial tasks have dependencies. The former fifth immediate item, 22 pan/zoom geometry, is deferred. Backend/content/evidence tasks can be selected independently under the existing queue policy.

## Deferred presentation work

WB-UI-21/22/23/24/25/27/30/35/36/46/51 are retained for later: custom cluster layout, pan/zoom, tree canvas/glyph/path/hit/search polish, styled tree review, new GF4/B3 decoration integration, final special-wheel art and graphical performance benchmarks. They are not prerequisites for accepting the first plain interface. Existing wheel appearance/selections are preserved. Shared plain inspector WB-UI-26 and plain progression list WB-UI-29 do not depend on those deferred renderer tasks.

No marquee, flashing Shop sign, ornate chrome, animation polish or recreation of the earlier artistic mockups is required initially. No arbitrary visual-approval loop should block the approved ordinary buttons/lists. Bring later material design changes back for review as needed after user testing.

## Upgrade-ready implementation rules

1. Domain/persistence/evidence services contain the rules; Swing views render immutable identity/generation/revision-bound snapshots and emit explicit intents.
2. Use shared components and replaceable section panels behind stable routes/entity IDs. Future tree, item artwork or themed shop replaces presentation, not save formats or purchasing logic.
3. Catalog/ownership/prerequisite explanations feed both initial list and future graph. No second UI-only progression model or hardcoded reward/price data.
4. Validate current supported capability, quote and revision on commands. Show success only after durable acknowledgment; reconcile uncertain results with the same command ID.
5. Keep disk/client work off the EDT; prevent late account callbacks, retain mandatory obligations, clean subscriptions/timers on close/reopen/shutdown and keep keyboard/resize support.
6. Keep view preferences optional and separate from gameplay saves. Add only enough styling abstraction for ordinary shared controls; avoid a speculative theming framework.

## Acceptance and reporting

For each task report actual source changes, supported/disabled capabilities, focused tests executed and unresolved gates. Read-only scaffold is not a complete connected subsystem. A proposed transition or a class existing is not proof of durable gameplay behavior. Preserve Master regression behavior, approved empty retries, automatic bounties, Questing indestructibility, ranked Challenge retention and evidence requirements.

Before merging a coding checkpoint: review current diff, use the current expected head, run applicable checks and respect branch protections. No push directly to `wheelbound-mode`, force push, reset, main/master change or Plugin Hub submission. Existing autonomous merge authority applies to verified approved implementation; this user request supplies explicit authority for the planning PR merge.

Supervisor discovery is documented but not runtime-proven: the Windows supervisor and its prompt/parser are not tracked here. After merge, its normal refresh should encounter the queue pointers. Verify a normal run log discovers a ready WB-UI task before claiming consumption works. Do not stop/reconfigure the worker to achieve that; request approval for a concrete runner change only if necessary.

## Publication record

PR #58 is the authoritative approval/integration record and will expose the final merge commit/status. This Markdown is authored before the merge, so it does not fabricate a merge SHA or claim completed production implementation. The final user report records the verified merge result. Follow-up implementation starts from the newly merged source head, using the existing ownership/queue protocol.
