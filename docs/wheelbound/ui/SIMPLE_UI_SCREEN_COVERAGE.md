# Simple UI coverage — 36 screens and flows

Current user direction: cover as much approved functionality as practical using ordinary controls, with replaceable views for later upgrades. This is a screen-level acceptance expansion of the existing **43 UI tasks**, not 36 extra coding tasks or a claim of production implementation. [Simple UI direction](SIMPLE_UI_FIRST.md) wins over earlier visual mockups.

[Open the navigable layout prototype](previews/simple-screens.html). The [machine-readable screen manifest](previews/simple-screens.json) drives the same 36 local preview routes. All gameplay commands are disabled in the prototype; local navigation, row inspection, Back and preference selection are functional. No game account/session is accessed.

## Coverage index

| Group | Screen / flow | Existing implementation scope |
| --- | --- | --- |
| Core | [Run overview](#overview) | WB-UI-13/14/31 |
| Core | [Gameplay sidebar](#sidebar) | WB-UI-17 |
| Core | [Mode and account setup](#account) | WB-UI-12/13/49 |
| Core | [Save status and recovery](#save) | WB-UI-13/49/50 |
| Core | [Start-run eligibility](#start) | WB-UI-34 |
| Core | [Pause and resume](#pause) | WB-UI-16/33 |
| Fates | [Master Wheel access](#master) | WB-UI-15; WB-WHL-01/08 |
| Fates | [Fate offerings](#offers) | WB-UI-32 |
| Fates | [Owned Fate Cards](#cards) | WB-UI-32/42 |
| Fates | [Active Fate detail](#objective) | WB-UI-33 |
| Fates | [Quest choices](#quests) | WB-UI-37 |
| Fates | [Boss eligibility](#bosses) | WB-UI-32/38 |
| Fates | [Challenge item hunt](#hunt) | WB-UI-33/38 |
| Fates | [Completion and reward result](#complete) | WB-UI-16/33 |
| Fates | [Reject / failure consequences](#reject) | WB-UI-16/33 |
| Fates | [Off-task tolerance warning](#tolerance) | WB-UI-14/33/49 |
| Grand Fate | [Grand Fate reveal](#grand-draw) | WB-UI-34/35 |
| Grand Fate | [Grand Fate checklist](#grand-progress) | WB-UI-34 |
| Grand Fate | [Grand Fate attempt](#grand-attempt) | WB-UI-34 |
| Progression & shops | [Fate Tree — simple list](#progression) | WB-UI-18/19/20/29 |
| Progression & shops | [Upgrade and prerequisites](#upgrade) | WB-UI-26/28/29 |
| Progression & shops | [Wheel Builder](#builder) | WB-UI-39/40/41 |
| Progression & shops | [Satchel / stored slices](#satchel) | WB-UI-39/40 |
| Progression & shops | [Slice edit confirmation](#slice-edit) | WB-UI-40/41 |
| Progression & shops | [Shop — upgrades and products](#shop) | WB-UI-42/43 |
| Progression & shops | [Vendor unlocks](#vendors) | WB-UI-42/43/49 |
| Progression & shops | [Challenge ranks and readiness](#ranks) | WB-UI-38/42/43 |
| Progression & shops | [Bounties](#bounties) | WB-UI-44 |
| Progression & shops | [Shared purchase confirmation](#purchase) | WB-UI-16/28/41/43 |
| Risk & recovery | [Zero-spins decision](#zero-spins) | WB-UI-45 |
| Risk & recovery | [Defy Fate consequences](#defy) | WB-UI-45 |
| Risk & recovery | [Taint and Punishment obligation](#punishment) | WB-UI-46/47 |
| Risk & recovery | [Sacrifice / Coffer evidence](#sacrifice) | WB-UI-47 |
| Risk & recovery | [Fate’s Pardon](#pardon) | WB-UI-48 |
| Utilities | [Notifications and recent receipts](#receipts) | WB-UI-13/49 |
| Utilities | [Settings and help](#settings) | WB-UI-49 |

## Delivery order

1. Shared ordinary panels, buttons, snapshot/presenter contract, one window, account/save status and sidebar/overview. Existing live identity binding remains a real dependency.
2. Independent read-only screens: plain progression list/detail, actual owned cards, actual inventory/Satchel, shop/bounty catalogs, Grand checklist, supported receipts and help. Unsupported/missing data uses explanations, not dummy values.
3. Bind one service at a time: lifecycle actions, offers/objectives/quests, legal edits and purchases, Grand attempts, risk and settlement. The backend/evidence/quote requirements listed below remain mandatory.
4. Run the combined navigation/state/reopen/account-switch checks, then let the user test ordinary flows and revise layouts. Fancy tree, artwork and animation remain deferred.

A missing mutation service blocks that action, not unrelated read-only panels. One shell/presenter/component can serve multiple listed screens. Do not create 36 disconnected native windows or duplicate the backend per screen. No task count or design preview is a completion claim.

## Common acceptance for every initial screen

Standard Swing/RuneLite controls; current identity/generation/revision snapshot; no storage/client reads inside painters; EDT-safe rendering; asynchronous services; disabled action reason; loading/empty/unknown/paused/pending/error where relevant; wrap/resize; keyboard names; close/reopen cleanup; no accidental side effects; replaceable view over stable actions/data. Verify the original parent backend contract and focused tests before enabling anything. Preserved normal Master wheel appearance is never replaced with a primitive temporary wheel.


<a id="overview"></a>

## Run overview

- Existing task mapping: **WB-UI-13/14/31**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Run state, Fate Points, spins and the outstanding obligation.
- Controls/data: Run status → Not connected; Fate Points / debt → Unavailable; Remaining spins → Unavailable; Current Fate → No connected assignment
- Navigation: [View active Fate](#objective), [Open wheel](#master), [Grand Fate progress](#grand-progress), [Pause / resume](#pause).
- Required service/data boundary: Identity-bound lifecycle, ledger, spin state and obligation snapshot; absent values are not zero.
- Protected behavior / disabled reason: Connect a supported saved run before gameplay actions are available.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="sidebar"></a>

## Gameplay sidebar

- Existing task mapping: **WB-UI-17**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: A narrow status panel with the information needed during normal play.
- Controls/data: State → Not connected; FP / spins → Unavailable; Active objective → Unavailable; Progress / tolerance → Unavailable
- Navigation: [Open Wheelbound](#overview), [Current wheel](#master), [Fate Tree](#progression), [Bounties](#bounties).
- Required service/data boundary: Same immutable snapshot/presenter as Overview; host sidebar width wraps without horizontal scrolling.
- Protected behavior / disabled reason: Gameplay actions depend on the supported current account.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="account"></a>

## Mode and account setup

- Existing task mapping: **WB-UI-12/13/49**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Explicit opt-in, connected account identity and save availability.
- Controls/data: Wheelbound mode → Off until supported opt-in; Connected profile → Unavailable; Saved run → Not inspected
- Local form presentation: Keep normal wheel mode, Inspect supported Wheelbound save. Production settings still use their permitted service boundary.
- Primary command control: **Connect Wheelbound mode**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Inspect save status](#save), [Start eligibility](#start).
- Required service/data boundary: FoundationPreview/AsyncRunSession plus supported live identity adapter; no editing account IDs in UI.
- Protected behavior / disabled reason: Live identity/directory/session binding is required; no guessed account identity.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="save"></a>

## Save status and recovery

- Existing task mapping: **WB-UI-13/49/50**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Show readable save health and safe recovery choices.
- Controls/data: Save state → Not loaded; Schema / revision → Unavailable; Pending write → Unknown; Recovery action → Await supported inspection
- Navigation: [Account setup](#account), [Recent receipts](#receipts).
- Required service/data boundary: Typed codec/store inspection; preserve files and obligation, show uncertainty distinctly from empty/new save.
- Protected behavior / disabled reason: Do not reset, overwrite or mark a corrupt/newer save recovered from this screen.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="start"></a>

## Start-run eligibility

- Existing task mapping: **WB-UI-34**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Check the protected tutorial/mainland flow before creating a run.
- Controls/data: plain selectable rows with requirement, status.
- Primary command control: **Begin Grand Fate selection**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Grand Fate reveal](#grand-draw).
- Required service/data boundary: Authoritative initialization predicate, durable seal/staging and original-command replay.
- Protected behavior / disabled reason: Unknown eligibility blocks creation; this is not a free reset/new-run escape.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="pause"></a>

## Pause and resume

- Existing task mapping: **WB-UI-16/33**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Pause preserves the run and its outstanding obligations.
- Controls/data: Current state → Unavailable; Active obligation → Unavailable
- Primary command control: **Confirm pause / resume**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Back to overview](#overview).
- Required service/data boundary: Revision-checked lifecycle gateway; no view-only pause flag or automatic clearing of obligations.
- Protected behavior / disabled reason: Await a connected lifecycle and the permitted transition.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="master"></a>

## Master Wheel access

- Existing task mapping: **WB-UI-15; WB-WHL-01/08**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Open the existing wheel with its current appearance and behavior.
- Controls/data: Committed pool → Unavailable; Readiness → Not connected; Spin charge → From authoritative command
- Primary command control: **Open existing Master Wheel**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Fate offering screen](#offers), [Wheel Builder](#builder), [Zero-spins flow](#zero-spins).
- Required service/data boundary: Existing popup renderer plus mode commitment/readiness; skip animation reveals the same committed result.
- Protected behavior / disabled reason: This prototype does not draw, reroll or replace the existing Master Wheel.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="offers"></a>

## Fate offerings

- Existing task mapping: **WB-UI-32**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Standard target first, then frozen offers, then random target selection.
- Controls/data: plain selectable rows with Offering, Target / rules, Availability.
- Primary command control: **Select committed offering**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Boss eligibility](#bosses), [Challenge hunt details](#hunt).
- Required service/data boundary: Immutable durable offers, distinct starting Standard targets, two/three capacity and card-before-target sequence.
- Protected behavior / disabled reason: No boss picker or reroll. The UI never generates replacement offers.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="cards"></a>

## Owned Fate Cards

- Existing task mapping: **WB-UI-32/42**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Inspect permanently owned types and offering upgrades.
- Controls/data: plain selectable rows with Card family, Ownership, Effect.
- Navigation: [Shop upgrades](#shop), [Challenge ranks](#ranks), [New offering view](#offers).
- Required service/data boundary: Versioned card ownership/offer catalog; normal and Challenge families remain distinct.
- Protected behavior / disabled reason: Owning a card does not select a boss or make a target eligible.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="objective"></a>

## Active Fate detail

- Existing task mapping: **WB-UI-33**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: One objective panel reused for XP, combat, boss KC, quests and Slayer.
- Controls/data: Assignment / type → Unavailable; Target / source → Unavailable; Verified progress → Unavailable; Restrictions / tolerance → Unavailable; Reward quote → Unavailable
- Navigation: [Pause / resume](#pause), [Reject consequence review](#reject), [Completion result](#complete), [Tolerance warning](#tolerance).
- Required service/data boundary: Committed assignment snapshot, type-specific progress projection and atomic settlement capability.
- Protected behavior / disabled reason: No manual completion credit from this screen; only supported evidence may satisfy an objective.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="quests"></a>

## Quest choices

- Existing task mapping: **WB-UI-37**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Display the actual committed quest choices and qualification reasons.
- Controls/data: plain selectable rows with Quest, Qualification, State.
- Primary command control: **Select permitted quest**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Active Fate](#objective).
- Required service/data boundary: Resolved backend choice capacity/tier mapping; saved choices do not refresh on reopen.
- Protected behavior / disabled reason: Unknown data is not empty. Known-empty landing retries free with the slice retained.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="bosses"></a>

## Boss eligibility

- Existing task mapping: **WB-UI-32/38**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Read-only pool membership, unlock status and actual eligibility.
- Controls/data: plain selectable rows with Encounter, Owned / unlocked, Eligible reason.
- Navigation: [Fate offerings](#offers), [Vendor access](#vendors).
- Required service/data boundary: Actual committed eligible pool; eligibility filters affect only legal future selections.
- Protected behavior / disabled reason: Inspecting a boss does not choose the assignment or unlock its access requirements.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="hunt"></a>

## Challenge item hunt

- Existing task mapping: **WB-UI-33/38**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: The exact assigned item, creature and qualifying source context.
- Controls/data: Challenge rank → Unavailable; Assigned item → Reveal after committed selection; Assigned source / context → Unavailable; Reviewed reward → Unavailable; Qualifying fresh drop → No supported evidence
- Navigation: [Challenge readiness](#ranks), [Active Fate](#objective).
- Required service/data boundary: Frozen one-item/source/rank/reward assignment and supported fresh active personal drop receipt.
- Protected behavior / disabled reason: Old items, withdrawals and other uniques do not satisfy this assignment. No pity kill cap.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="complete"></a>

## Completion and reward result

- Existing task mapping: **WB-UI-16/33**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Show the saved settlement outcome after durable acknowledgment.
- Controls/data: Settlement → Not received; FP change → Unavailable; Spin / obligation result → Unavailable; Receipt → Unavailable
- Navigation: [Recent receipts](#receipts), [Back to overview](#overview).
- Required service/data boundary: Atomic settlement receipt and current snapshot; never award or debit from rendered UI state.
- Protected behavior / disabled reason: A proposed transition is not successful settlement; lost acknowledgment is reconciled using the same command ID.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="reject"></a>

## Reject / failure consequences

- Existing task mapping: **WB-UI-16/33**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Review exact consequences before a permitted Reject action.
- Controls/data: FP change / debt → From approved settlement quote; Spin consequence → From approved settlement quote; Mandatory follow-up → From current obligation rules
- Primary command control: **Confirm Reject**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Cancel and return](#objective).
- Required service/data boundary: Current identity/revision, approved consequence quote and atomic Reject/failure gateway.
- Protected behavior / disabled reason: No guessed numeric loss, punishment or recovery waiver.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="tolerance"></a>

## Off-task tolerance warning

- Existing task mapping: **WB-UI-14/33/49**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Explain off-task XP and the remaining allowance.
- Controls/data: Tracked off-task XP → Unavailable; Remaining allowance → From active Fate rules; Penalty / failure status → Await authoritative processing
- Navigation: [Active restrictions](#objective), [Pause / resume](#pause).
- Required service/data boundary: Supported XP attribution, active objective boundary and domain audit/settlement state.
- Protected behavior / disabled reason: The warning does not settle a penalty or invent a new tolerance value.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="grand-draw"></a>

## Grand Fate reveal

- Existing task mapping: **WB-UI-34/35**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Reveal the sealed initial destiny using the selected GF4 wheel when available.
- Controls/data: Sealed destiny → Unavailable; Original draw receipt → Unavailable; Initial reroll → Only if still permitted by sealed initialization state
- Primary command control: **Reveal sealed result**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Grand checklist](#grand-progress), [Initialization eligibility](#start).
- Required service/data boundary: Original Grand seal/staging and explicitly allowed one initial reroll transition; renderer never supplies randomness.
- Protected behavior / disabled reason: Five equal-weight approved goals; no new draw from reopening, no arbitrary extra reroll.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="grand-progress"></a>

## Grand Fate checklist

- Existing task mapping: **WB-UI-34**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Display the selected destiny and its retained verified components.
- Controls/data: plain selectable rows with requirement, status.
- Navigation: [Attempt unfinished component](#grand-attempt), [Grand Fate reveal](#grand-draw).
- Required service/data boundary: Identity-bound sealed checklist/progress; reference structure alone is not authenticated completion evidence.
- Protected behavior / disabled reason: Master goal is hilt 5; Awakened components retain independent progress; Radiant uses the approved full-set objective.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="grand-attempt"></a>

## Grand Fate attempt

- Existing task mapping: **WB-UI-34**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Begin a permitted attempt on an unfinished sealed component.
- Controls/data: Selected unfinished component → Unavailable; Active Fate / obligation gate → Unknown; Attempt readiness → Unknown
- Primary command control: **Begin Grand attempt**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Return to checklist](#grand-progress).
- Required service/data boundary: Current lifecycle/seal/progress plus revision-checked durable attempt service; no UI-only activation.
- Protected behavior / disabled reason: Trusted evidence, durable attempt commands and settlement remain required.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="progression"></a>

## Fate Tree — simple list

- Existing task mapping: **WB-UI-18/19/20/29**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Four branches in a plain upgrade list; the graphical tree can replace this view later.
- Controls/data: plain selectable rows with Branch, Content, State.
- Navigation: [Inspect an upgrade](#upgrade), [Owned Fate cards](#cards).
- Required service/data boundary: One production catalog and prerequisite projection shared with future tree renderer; no duplicated visual-only rules.
- Protected behavior / disabled reason: No custom graph renderer is required for this first list; shared catalog/rules remain authoritative.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="upgrade"></a>

## Upgrade and prerequisites

- Existing task mapping: **WB-UI-26/28/29**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Readable benefit, ALL/ANY/count requirements and a standard Unlock button.
- Controls/data: Selected upgrade → From actual catalog selection; Ownership / support → Unavailable; ALL / ANY requirements → From graph expression; Unique purchase milestone → Distinct purchased unlock count; FP cost / balance → Reviewed quote required
- Primary command control: **Review unlock purchase**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Purchase confirmation](#purchase), [Back to upgrade list](#progression).
- Required service/data boundary: Graph visitor/unique-count evaluator, catalog quote and atomic entitlement+debit+benefit transaction.
- Protected behavior / disabled reason: Unknown prices are not free. Higher Challenge ranks retain lower ownership.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="builder"></a>

## Wheel Builder

- Existing task mapping: **WB-UI-39/40/41**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: A simple list of active slice instances and available slot capacity.
- Controls/data: plain selectable rows with Active slice instance, Weight / activity odds, Allowed action.
- Navigation: [Open Satchel](#satchel), [Review slice edit](#slice-edit), [Existing wheel](#master).
- Required service/data boundary: Actual SliceInventory/ActiveWheel/WheelOdds read model; edits use real legal-boundary/transaction services.
- Protected behavior / disabled reason: Validate five ordinary slices, three distinct activities and 24 total slots together.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="satchel"></a>

## Satchel / stored slices

- Existing task mapping: **WB-UI-39/40**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Stored instances, capacity and legal reactivation.
- Controls/data: plain selectable rows with Stored instance, Enhancement / history, Capacity / action.
- Primary command control: **Review reactivation**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Active wheel inventory](#builder), [Review edit](#slice-edit).
- Required service/data boundary: Shared inventory instance identity, Satchel capacity and authoritative edit validation.
- Protected behavior / disabled reason: Reactivating the same instance does not count as a new purchase or new lifetime acquisition.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="slice-edit"></a>

## Slice edit confirmation

- Existing task mapping: **WB-UI-40/41**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Preview one legal add, store, reactivate, replace, enhance or destroy operation.
- Controls/data: Operation / selected instance → From local validated draft; Before / after active slots → Exact domain preview; Satchel capacity → Exact domain preview; Price / enhancement loss → Reviewed quote and consequence preview
- Primary command control: **Confirm legal slice edit**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Cancel draft](#builder).
- Required service/data boundary: Draft presenter, current edit boundary, versioned quote and atomic inventory transaction receipt.
- Protected behavior / disabled reason: In-memory inventory/purchase proposals alone are not durable successful edits.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="shop"></a>

## Shop — upgrades and products

- Existing task mapping: **WB-UI-42/43**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Ordinary product rows and normal buttons; no flashing sign.
- Controls/data: plain selectable rows with Section, Product data, Price.
- Navigation: [Owned cards](#cards), [Vendors](#vendors), [Fate’s Pardon](#pardon), [Purchase confirmation](#purchase).
- Required service/data boundary: Versioned product catalog, owned entitlements, availability and shared purchase gateway.
- Protected behavior / disabled reason: Unreviewed products cannot be purchased. No provisional numeric schedules become prices through the UI.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="vendors"></a>

## Vendor unlocks

- Existing task mapping: **WB-UI-42/43/49**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Vendor entitlement, actual access and restriction evidence are separate.
- Controls/data: plain selectable rows with Vendor, Permanent ownership, Current access / reason.
- Primary command control: **Review vendor unlock**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Purchase confirmation](#purchase), [Settings / help](#settings).
- Required service/data boundary: Vendor product gateway and independently supported interaction/location/policy adapter capability.
- Protected behavior / disabled reason: Do not claim unsupported access enforcement works. Retain Trade with and show the approved local notice.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="ranks"></a>

## Challenge ranks and readiness

- Existing task mapping: **WB-UI-38/42/43**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Owned, enabled and ready are distinct for each retained rank.
- Controls/data: plain selectable rows with Rank, Owned / enabled, Ready eligible targets.
- Navigation: [Purchase prerequisite detail](#upgrade), [Assigned hunt](#hunt).
- Required service/data boundary: D147–150 progressive ownership/shared bundle/target entitlements and future-offer rank settings capability.
- Protected behavior / disabled reason: Changes apply only at the approved safe boundary. All off is allowed; no per-target toggles or four extra offer slots.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="bounties"></a>

## Bounties

- Existing task mapping: **WB-UI-44**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Item icon, description and Fate Points in compact rows.
- Controls/data: plain selectable rows with Item icon / name, Objective / availability, Fate Points.
- Navigation: [Reward receipts](#receipts).
- Required service/data boundary: Actual bounty catalog, entitlements and independent once-only acquisition/reward receipts; no failure clawback.
- Protected behavior / disabled reason: No Claim button: qualifying active rewards pay automatically. Paused gains do not backfill.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="purchase"></a>

## Shared purchase confirmation

- Existing task mapping: **WB-UI-16/28/41/43**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: One confirmation pattern reused by upgrades, slices and shop products.
- Controls/data: Product / benefit → From selected catalog product; Quote version / price → Reviewed authoritative quote; Balance after purchase → Exact validated preview; Prerequisites / boundary → Current service validation
- Primary command control: **Confirm purchase**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Cancel and return to shop](#shop).
- Required service/data boundary: Typed revision/identity/quote-bound product gateway and idempotent atomic receipt.
- Protected behavior / disabled reason: Only durable acknowledgment shows success; never debit ledger separately from product grant.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="zero-spins"></a>

## Zero-spins decision

- Existing task mapping: **WB-UI-45**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: When no spins remain, show only the legal Grand or Defy routes.
- Controls/data: Remaining spins → From saved state; Mandatory obligation → From saved lifecycle
- Navigation: [Review Grand attempt](#grand-attempt), [Review Defy Fate](#defy).
- Required service/data boundary: Authoritative zero-spin capability and current Grand/Defy eligibility.
- Protected behavior / disabled reason: Do not grant spins, bypass an obligation or start a Grand attempt merely by navigating here.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="defy"></a>

## Defy Fate consequences

- Existing task mapping: **WB-UI-45**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Show the actual refill, special odds and displacement together.
- Controls/data: Spin refill → Approved grant; Taint / Sacrifice probabilities → Exact committed rules; Full-wheel displacement → Legal instance / storage consequences
- Primary command control: **Confirm Defy Fate**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Cancel](#zero-spins), [Mandatory obligation](#punishment).
- Required service/data boundary: Current wheel state, approved Defy rules, legal full-capacity selection and atomic grant/injection receipt.
- Protected behavior / disabled reason: No guessed grants, weights, extra slots or invented replacement rules.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="punishment"></a>

## Taint and Punishment obligation

- Existing task mapping: **WB-UI-46/47**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Plain pending-task details before special-wheel artwork is added.
- Controls/data: Mandatory task → From committed punishment; Restrictions / progress → Supported evidence only; Taint task cost → One spin where applicable; Punishment completion → No additional spin charge
- Navigation: [Pause / resume](#pause), [Sacrifice evidence](#sacrifice).
- Required service/data boundary: Committed punishment/taint state, trusted progress and mandatory settlement service; preserve Master/GF4/B3 themes.
- Protected behavior / disabled reason: Obligation survives close/restart. Opening a task does not count as completing it.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="sacrifice"></a>

## Sacrifice / Coffer evidence

- Existing task mapping: **WB-UI-47**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Explain pending donation eligibility and missing verification.
- Controls/data: Donation obligation → From saved state; Eligible item evidence → Unknown; Supported Coffer receipt → Unavailable; Task cost → One spin where applicable
- Navigation: [Obligation detail](#punishment), [Evidence / save status](#save).
- Required service/data boundary: Verified current-client Coffer adapter only; no guessed donation, forced death or invented obligation waiver.
- Protected behavior / disabled reason: Known no eligible item: acquire an eligible item worth at least 10,000 GP. Unknown bank evidence requires refresh, not that assumption.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="pardon"></a>

## Fate’s Pardon

- Existing task mapping: **WB-UI-48**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Review fixed-price taint-debt removal separately from normal unlocking.
- Controls/data: Taint debt / status → From saved state; Restored status → Banned → Locked where applicable; Cost → Reviewed fixed quote
- Primary command control: **Review Pardon purchase**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Purchase confirmation](#purchase), [Cancel](#shop).
- Required service/data boundary: Atomic taint-only restoration plus approved fixed-price debit and replay receipt.
- Protected behavior / disabled reason: No invented price or forgiveness of unrelated obligations/penalties.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="receipts"></a>

## Notifications and recent receipts

- Existing task mapping: **WB-UI-13/49**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Read-only durable events and concise notices.
- Controls/data: plain selectable rows with Event, Recorded result, State.
- Navigation: [Completion result](#complete), [Save / account status](#save).
- Required service/data boundary: Bounded persisted receipts/notifications with once-only acknowledgment display and no private account identifiers.
- Protected behavior / disabled reason: No fabricated prior-run history, replayed reward animation or claim action.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.

<a id="settings"></a>

## Settings and help

- Existing task mapping: **WB-UI-49**; implementation status **TODO / existing foundations reusable**, not complete because a layout exists.
- Initial behavior: Plain preferences and short instructions; richer visuals can be added later.
- Controls/data: Reduced motion → Local preference; Eligibility options → Only supported legal future selections; Challenge rank changes → Only at permitted boundary
- Local form presentation: Normal motion, Reduced motion. Production settings still use their permitted service boundary.
- Primary command control: **Apply gameplay settings**; disabled until the actual capability/quote/evidence requirements are satisfied.
- Navigation: [Challenge rank settings](#ranks), [Account setup](#account), [Objective / restrictions help](#objective).
- Required service/data boundary: View-only preferences separate from authoritative legal gameplay settings commands; help follows current decisions.
- Protected behavior / disabled reason: Preferences never refresh a committed offer or weaken evidence requirements.
- Verification: common acceptance above plus the mapped task’s domain-specific cases; confirm clicking navigation/inspection cannot mutate saved gameplay. Unavailable command remains disabled and explained; connected command requires exact durable result/replay checks.
- Later upgrade seam: replace only this view/row/confirmation rendering while retaining the same presenter, catalog IDs, commands and saved gameplay state.
