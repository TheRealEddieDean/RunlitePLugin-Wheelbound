# UI implementation backlog

> **Current direction: [simple UI first](SIMPLE_UI_FIRST.md).** Use normal buttons and functional lists for initial testing; decorative styling and complex tree presentation are deferred. Earlier mockups are exploratory.

Audit base `223950c`; tasks are implementation specifications, not completed code. New IDs WB-UI-10 onward are child increments of existing parent scope. No existing task is removed or renamed. [Audit](UI_AUDIT.md), [architecture](UI_ARCHITECTURE.md), [visual system](UI_DESIGN_SYSTEM.md), [tree](FATE_TREE_DESIGN.md) and [inventory](UI_SCREEN_INVENTORY.md) apply to every row. Canonical [decisions](../DECISIONS.md) and backend specs remain authority.

Each task is XS/S/M (2–30 minute bounded increment); split further if actual scope exceeds 30 minutes. UI scaffolding must not wait for full gameplay; live actions must wait for authoritative services. New task implementation status is TODO (not started), except existing reusable foundations identified separately in the audit. Delivery readiness below is distinct from implementation status. After implementation use PARTIAL, PARTIAL_UNVERIFIED, DONE only with evidence; BLOCKED for unmet evidence/dependencies and AWAITING_VISUAL_APPROVAL for a review build whose final merge lacks approval. Existing class presence alone cannot satisfy DONE.

Category A permits autonomous merging of that bounded task after required checks under existing authority, once this planning PR is approved/merged. Category B permits isolated technical scaffolding but **does not permit final visual implementation merge without recorded user approval**. This planning PR itself must not be merged automatically. No new permission to submit to Plugin Hub or adopt prices.

## External prerequisites

| Key | Existing scope / exact completion condition |
| --- | --- |
| EXT-IDENTITY | WB-FND-02–08: supported live account/profile identity and generation, directory and session binding verified; no guessed account ID |
| EXT-TREE-TX | WB-TREE-01/03/05 + ECON-03/08: traced production catalog/retention and authoritative atomic unlock+debit+benefit receipts, reviewed prices |
| EXT-OFFERS | WB-FAT-01–07: durable two/three offers and freeze/select sequence, no UI rerandomization |
| EXT-OBJECTIVES | WB-FAT-04–09 + BOS/SKL/QST: trusted progress/evidence and atomic settlement capabilities |
| EXT-GRAND | WB-GFD-01–07 + fresh-account predicate/evidence: sealed staging/activation, attempts, receipts and eligible initialization |
| EXT-WHEEL-THEME | Existing reusable wheel theme framework inspected/reconciled; if absent create in WB-WHL-01/GFD/BOS scope without changing Master |
| EXT-QUEST | WB-QST-01–07: resolved authoritative choice count/tier mapping and committed quests/known-empty retry |
| EXT-CHALLENGE | WB-FAT-10–14: actual ranked catalog/offer/evidence services and safe settings boundary |
| EXT-EDIT-TX | WB-WHL-03–12: complete legal edit/atomic inventory transaction and receipt service, not immutable proposal alone |
| EXT-PRICES | WB-ECON-03/09: adopted versioned product prices, not sensitivity candidates |
| EXT-CATALOG | WB-ECON-04/06/07: reviewed products/ownership/cost/prerequisites; unreviewed products disabled |
| EXT-SHOP-TX | WB-ECON-08 + relevant product command: atomic entitlement+debit/replay with reviewed quote |
| EXT-BOUNTIES | WB-BNT-01–06: supported fresh acquisition, independent once-only active reward receipts, paused safeguards |
| EXT-DEFY | WB-DEF-01–07: approved numeric grants/odds and committed legal displacement command |
| EXT-PUNISHMENT | WB-PUN-01–08: supported mandatory settlement/evidence; Coffer adapter stays blocked until proof |
| EXT-PARDON | WB-DEF-08 + ECON-03/08: reviewed fixed quote and atomic taint-only restoration |
| EXT-LIVE | WB-REL-08/09: actual client/evidence/policy checks for each enabled action; no blanket enablement |

## Task index

| ID | Parent | Title | Priority / size | Delivery readiness | Category |
| --- | --- | --- | --- | --- | --- |
| WB-UI-10 | WB-UI-08 | Shared tokens and text primitives | P0/S | IMMEDIATE | A |
| WB-UI-11 | WB-UI-01 | Owned window controller | P0/M | IMMEDIATE | A |
| WB-UI-12 | WB-UI-09 | Immutable UI snapshot and capability contract | P0/S | IMMEDIATE | A |
| WB-UI-13 | WB-UI-02 | Read-only durable state projection | P0/M | DEPENDENCIES | A |
| WB-UI-14 | WB-UI-02 | Common objective/resource/error components | P0/S | DEPENDENCIES | A |
| WB-UI-15 | WB-UI-01 | Route container and navigation scaffolding | P0/M | DEPENDENCIES | A |
| WB-UI-16 | WB-UI-09 | Command pending/result presenter | P0/M | DEPENDENCIES | A |
| WB-UI-17 | WB-UI-02 | Read-only gameplay sidebar integration | P0/M | DEPENDENCIES | B |
| WB-UI-18 | WB-TREE-01 | Progression catalog and decision trace contract | P0/S | IMMEDIATE | A |
| WB-UI-19 | WB-TREE-02 | Read-only prerequisite expression visitor | P0/M | DEPENDENCIES | A |
| WB-UI-20 | WB-TREE-04 | Unique purchased-count gate projection | P0/M | DEPENDENCIES | A |
| WB-UI-21 | WB-TREE-01 | Deterministic cluster layout model | P0/M | DEPENDENCIES | A |
| WB-UI-22 | WB-TREE-06 | Pan and zoom geometry | P0/S | IMMEDIATE | A |
| WB-UI-23 | WB-TREE-06 | Tree rendering surface and HiDPI layering | P0/M | DEPENDENCIES | A |
| WB-UI-24 | WB-TREE-06 | Node states and path semantics | P0/M | DEPENDENCIES | A |
| WB-UI-25 | WB-TREE-06 | Tree hit testing and keyboard selection | P0/S | DEPENDENCIES | A |
| WB-UI-26 | WB-TREE-06 | Node inspector and hover tooltips | P0/M | DEPENDENCIES | A |
| WB-UI-27 | WB-TREE-06 | Search, branch focus and prerequisite routes | P0/M | DEPENDENCIES | A |
| WB-UI-28 | WB-TREE-05 | Tree confirmation and durable purchase binding | P0/M | DEPENDENCIES | A |
| WB-UI-29 | WB-UI-08 | Accessible progression list alternative | P0/S | DEPENDENCIES | A |
| WB-UI-30 | WB-TREE-06 | Fate Tree visual review build | P1/M | DEPENDENCIES | B |
| WB-UI-31 | WB-UI-01 | Hub and sidebar visual review build | P1/M | DEPENDENCIES | B |
| WB-UI-32 | WB-UI-03 | Fate offer/card presentation | P1/M | DEPENDENCIES | B |
| WB-UI-33 | WB-UI-02 | Objective activation and settlement details | P1/M | DEPENDENCIES | B |
| WB-UI-34 | WB-GFD-03 | Grand onboarding and sealed checklist | P1/M | DEPENDENCIES | B |
| WB-UI-35 | WB-UI-04 | GF4 classpath decoration integration | P1/M | DEPENDENCIES | A |
| WB-UI-36 | WB-UI-05 | B3 classpath decoration integration | P1/M | DEPENDENCIES | A |
| WB-UI-37 | WB-QST-02 | Quest choices and empty-retry notice | P1/M | DEPENDENCIES | B |
| WB-UI-38 | WB-FAT-10 | Challenge ranks and eligibility inspector | P1/M | DEPENDENCIES | B |
| WB-UI-39 | WB-WHL-03 | Builder inventory and slot inspector | P1/M | DEPENDENCIES | B |
| WB-UI-40 | WB-WHL-11 | Builder legal edit draft presenter | P1/M | DEPENDENCIES | A |
| WB-UI-41 | WB-WHL-04 | Builder paid change confirmation | P1/M | DEPENDENCIES | A |
| WB-UI-42 | WB-ECON-04 | Shop catalog/ownership presentation | P1/M | DEPENDENCIES | B |
| WB-UI-43 | WB-ECON-08 | Shop transaction presenter binding | P1/M | DEPENDENCIES | A |
| WB-UI-44 | WB-BNT-04 | Bounties list and automatic reward feedback | P2/M | DEPENDENCIES | B |
| WB-UI-45 | WB-DEF-01 | Zero-spins and Defy consequence review | P2/M | DEPENDENCIES | B |
| WB-UI-46 | WB-UI-06 | Taint/Punishment wheel design checkpoint | P2/M | DEPENDENCIES | B |
| WB-UI-47 | WB-PUN-05 | Punishment and sacrifice pending objective | P2/M | DEPENDENCIES | B |
| WB-UI-48 | WB-DEF-08 | Pardon debt restoration confirmation | P2/M | DEPENDENCIES | A |
| WB-UI-49 | WB-UI-07 | Settings, help and evidence notifications | P2/M | DEPENDENCIES | A |
| WB-UI-50 | WB-UI-08 | UI lifecycle and legacy regression acceptance | P2/M | DEPENDENCIES | A |
| WB-UI-51 | WB-UI-08 | Tree performance and visual acceptance | P2/M | DEPENDENCIES | A |
| WB-UI-52 | WB-REL-12 | Complete mode UI visual acceptance milestone | P2/M | DEPENDENCIES | B |

## WB-UI-10 — Shared tokens and text primitives

- Parent: **WB-UI-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Implement local tokens and wrapping labels; preserve WheelStyle and global LookAndFeel.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: immediately implementable.
- Dependencies (must be verified complete for live integration): None.
- Likely files/modules (proposed paths unless already in source): `ui/theme/*`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Contrast/long-label component checks at 100/150/200%. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-11 — Owned window controller

- Parent: **WB-UI-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: One modeless owner-bound shell, dispose/reopen and subscription cleanup; no forced focus.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: immediately implementable.
- Dependencies (must be verified complete for live integration): None.
- Likely files/modules (proposed paths unless already in source): `ui/WindowController; WheelboundPlugin lifecycle`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Open twice, close/reopen, shutdown while pending; native owner check. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-12 — Immutable UI snapshot and capability contract

- Parent: **WB-UI-09**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Represent identity/generation/revision and optional values; distinguish unsupported, loading, absent and zero.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: immediately implementable.
- Dependencies (must be verified complete for live integration): None.
- Likely files/modules (proposed paths unless already in source): `ui/model/*; persistence read boundary`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Account A late result after B; unknown FP never zero. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-13 — Read-only durable state projection

- Parent: **WB-UI-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Project actual saved lifecycle, ledger, inventory and sealed Grand data on worker; no synthetic live state or game sampling.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-12.
- Likely files/modules (proposed paths unless already in source): `ui/presenter/*; RunDocument/AsyncRunSession`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Load real codec fixture; missing fields unavailable; stale token rejected. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-14 — Common objective/resource/error components

- Parent: **WB-UI-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Objective cards, counters, evidence status, debt and retry render snapshots without domain mutation.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-10, WB-UI-12.
- Likely files/modules (proposed paths unless already in source): `ui/components/*`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Pending/paused/debt/empty/long restriction text and accessible names. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-15 — Route container and navigation scaffolding

- Parent: **WB-UI-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: CardLayout routes and capped back stack reuse single window; placeholders explicitly preview.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-11, WB-UI-14.
- Likely files/modules (proposed paths unless already in source): `ui/HubPanel; ui/RouteController`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Route round trip, bounded history and teardown; no native window accumulation. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-16 — Command pending/result presenter

- Parent: **WB-UI-09**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Typed intent envelope, same-ID reconciliation and stale-state messaging; no invented service implementation.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-12.
- Likely files/modules (proposed paths unless already in source): `ui/actions/*`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Double click, out-of-order result, quote change and lost acknowledgment. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-17 — Read-only gameplay sidebar integration

- Parent: **WB-UI-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Actual connected account state, active obligation and resource values; keep unsupported actions disabled; preserve normal panel.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-13, WB-UI-14; EXT-IDENTITY.
- Likely files/modules (proposed paths unless already in source): `ui/ModeSidebar; WheelboundPlugin`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Real session disconnect/reconnect and legacy wheel regressions; final layout review. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-18 — Progression catalog and decision trace contract

- Parent: **WB-TREE-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Author schema/trace matrix for four branches; validate stable IDs/approved nodes; unresolved gates and prices explicitly held, no invented nodes.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: immediately implementable.
- Dependencies (must be verified complete for live integration): None.
- Likely files/modules (proposed paths unless already in source): `domain progression catalog; catalogs metadata`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Every proposed catalog entry linked to canonical decision; unknowns non-purchasable. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-19 — Read-only prerequisite expression visitor

- Parent: **WB-TREE-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Expose immutable ROOT/OWNED/ALL/ANY traversal and IDs from same expression used by canUnlock; no duplicate visual graph.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-18.
- Likely files/modules (proposed paths unless already in source): `domain/ProgressionGraph`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Visitor/evaluator parity on nested groups; mutation attempts rejected; cycles retained. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-20 — Unique purchased-count gate projection

- Parent: **WB-TREE-04**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Implement approved 5/9 and 4/6 unique-purchased gates shared by evaluation and explanation; starting/duplicate slices excluded.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-19.
- Likely files/modules (proposed paths unless already in source): `domain progression rules; ui projection`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: 5/9 milestone vs 5/9 owned; repeated IDs, all/any nesting, lower-rank retention. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-21 — Deterministic cluster layout model

- Parent: **WB-TREE-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Compute four branch lanes, vertical tiers and circular square-node peer clusters keyed by production IDs; annotations not products.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-19, WB-UI-20.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeLayout`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Determinism, labels non-overlap, missing node IDs fail; no dependency from coordinates. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-22 — Pan and zoom geometry

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Pure invertible transform, cursor anchored wheel zoom, clamped scale, Fit/Reset and resize behavior.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: immediately implementable.
- Dependencies (must be verified complete for live integration): None.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeViewTransform`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Round trips, extreme coordinates, cursor anchor, zero-size viewport and invalid values. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-23 — Tree rendering surface and HiDPI layering

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Paint edges behind square glyphs, preserve default device transform and cull viewport; real catalog only in connected view.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-21, WB-UI-22.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeCanvas`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Headless 1x/2x bounds and clip renders; no game/storage calls while painting. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-24 — Node states and path semantics

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Separate ownership, unlock prerequisites, affordability, support and selection; purchased paths gold with text/glyph equivalents.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-23, WB-UI-13.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeStateProjection`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: State matrix includes owned but unavailable, unknown price, debt and retained lower ranks. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-25 — Tree hit testing and keyboard selection

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Inverse hit testing, click/drag threshold, directional selection and Enter detail; no purchase on drag or hover.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-23.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeInteraction`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Boundaries at zoom extremes, drag release over button, Tab/arrows/Escape. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-26 — Node inspector and hover tooltips

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Readable benefit/quote/reason and ALL/ANY/count explanations; tooltip screen clamping and keyboard parity.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-24, WB-UI-25.
- Likely files/modules (proposed paths unless already in source): `ui/tree/NodeInspector`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Long nested expressions, missing prices and viewport edges; no unescaped text. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-27 — Search, branch focus and prerequisite routes

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Search actual metadata, center selection, dim others and highlight alternatives; never infer cheapest route without prices.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-26.
- Likely files/modules (proposed paths unless already in source): `ui/tree/TreeSearch`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Duplicate names/stable IDs, no results, AND/OR alternatives and hidden branch restore. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-28 — Tree confirmation and durable purchase binding

- Parent: **WB-TREE-05**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Preview exact approved quote/benefit/balance; commit through atomic progression service, publish only ack; no direct ledger debit.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-16, WB-UI-26; EXT-TREE-TX.
- Likely files/modules (proposed paths unless already in source): `ui/tree/UnlockPresenter; progression command gateway`.
- Priority / estimated complexity: **P0 / M**; split at a reviewable boundary if needed.
- Testing requirements: Stale revision/price, insufficient FP, double submit, failure/restart/exact replay. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-29 — Accessible progression list alternative

- Parent: **WB-UI-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Keyboard/screen-reader list mirrors selected node and graph state; no separate rules or purchases.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-26.
- Likely files/modules (proposed paths unless already in source): `ui/tree/ProgressionList`.
- Priority / estimated complexity: **P0 / S**; split at a reviewable boundary if needed.
- Testing requirements: Every catalog node reachable; focus/selection parity and long labels. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-30 — Fate Tree visual review build

- Parent: **WB-TREE-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Produce actual catalog review at overview/normal/detail scale with inspector; test-only fixtures clearly labeled.
- Approved authority and proposed UI specification: FATE_TREE_DESIGN.md + PROGRESSION.md (D093/095/124/147–150); canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-27, WB-UI-29.
- Likely files/modules (proposed paths unless already in source): `ui/tree/*; review screenshots`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Manual readability/resize/mixed DPI plus recorded approval on exact revision before final visuals merge. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-31 — Hub and sidebar visual review build

- Parent: **WB-UI-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Review hybrid navigation and real-state status at narrow/default widths; no fake objective controls.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-17.
- Likely files/modules (proposed paths unless already in source): `ui/HubPanel; ModeSidebar`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Screenshots of active/paused/unsupported/error; preserve Master regression. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-32 — Fate offer/card presentation

- Parent: **WB-UI-03**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Standard KC before choice, two distinct initial targets/three after upgrade; rank visible, target reveal only after committed choice.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14, WB-UI-16; EXT-OFFERS.
- Likely files/modules (proposed paths unless already in source): `ui/fate/OfferPanel`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Frozen restore, no reroll/boss picker, distinct same-type targets; final layout review. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-33 — Objective activation and settlement details

- Parent: **WB-UI-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Skilling/combat/KC/quest/hunt progress and restrictions; Complete only if permitted by service; failure/Reject consequence summary.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14, WB-UI-16; EXT-OBJECTIVES.
- Likely files/modules (proposed paths unless already in source): `ui/fate/ObjectiveDetail`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Unsupported evidence, stale result, paused/restored and pending settlement; no manual reward mint. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-34 — Grand onboarding and sealed checklist

- Parent: **WB-GFD-03**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Protected tutorial/mainland gate, five-goal sealed outcome and one reroll only where allowed; hilt5/full Radiant/Awakened subgoals.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-16; EXT-GRAND.
- Likely files/modules (proposed paths unless already in source): `ui/grand/*`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Restore same seal, unfinished components, attempt gate/one reroll, unknown fresh-account predicate disabled. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-35 — GF4 classpath decoration integration

- Parent: **WB-UI-04**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Use selected GF4 geometry and small licensed assets; docs PNG remains reference, preserve equal goals and stationary pointer.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-10; EXT-WHEEL-THEME.
- Likely files/modules (proposed paths unless already in source): `wheel theme/resource loader`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Packaged JAR load, transparency/clipping, skip same result and unchanged Master; approved GF4 baseline preserved. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-36 — B3 classpath decoration integration

- Parent: **WB-UI-05**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Simple stone rim/four blue gems; no ornate first concept, baked categories or Grand skulls/chains.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-10; EXT-WHEEL-THEME.
- Likely files/modules (proposed paths unless already in source): `wheel theme/resource loader`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Packaged JAR, scaling, pointer and mode-specific theme; compare selection spec, no claimed missing PNG provenance. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-37 — Quest choices and empty-retry notice

- Parent: **WB-QST-02**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Use authoritative choice-capacity service, eligibility reasons and known-empty free retry notice; unknown not empty.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14; EXT-QUEST.
- Likely files/modules (proposed paths unless already in source): `ui/fate/QuestChoices`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Retain slice/weights, no extra debit, restored choices unchanged; do not hardcode conflicting historical counts. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-38 — Challenge ranks and eligibility inspector

- Parent: **WB-FAT-10**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Owned/enabled/ready separately, retained lower ranks, per-rank 3-target floor; legal-boundary settings, separate bosses/monsters.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14, WB-UI-16; EXT-CHALLENGE.
- Likely files/modules (proposed paths unless already in source): `ui/challenge/*`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: All off, underfloor owned, no extra slots, no target choice; fresh exact drop instruction. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-39 — Builder inventory and slot inspector

- Parent: **WB-WHL-03**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Read-only 24-slot grid, active/Satchel, UUID/weight/activity odds; explain distinct permanent unlock vs slice instance.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-14.
- Likely files/modules (proposed paths unless already in source): `ui/builder/*; SliceInventory projection`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Actual inventory fixture, capped slots, long names, special slices and stale account clear; final layout review. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-40 — Builder legal edit draft presenter

- Parent: **WB-WHL-11**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Local draft with undo/cancel validated by domain; five ordinary/three activities/24; Questing Store/Reactivate never Destroy.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-39, WB-UI-16; EXT-EDIT-TX.
- Likely files/modules (proposed paths unless already in source): `ui/builder/BuilderDraft`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Minimums, full Satchel, enhanced loss preview, no mutation on cancel, stale boundary discards draft. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-41 — Builder paid change confirmation

- Parent: **WB-WHL-04**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Frozen quote/classification, capacity/displacement preview, single durable product+debit command; do not persist proposal alone.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-40; EXT-PRICES.
- Likely files/modules (proposed paths unless already in source): `ui/builder/PurchasePresenter`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Reacquisition/lifetime-tier, exact replay, write failure and lost ack; no guessed price. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-42 — Shop catalog/ownership presentation

- Parent: **WB-ECON-04**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Sections for Fate upgrades/vendor/Challenge targets, current reviewed quote or unavailable, prerequisites and existing ownership.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-14; EXT-CATALOG.
- Likely files/modules (proposed paths unless already in source): `ui/shop/*`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Unknown price disabled, bought but inaccessible, search/filter; layout review before merge. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-43 — Shop transaction presenter binding

- Parent: **WB-ECON-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Explicit product, quote version, resulting balance/entitlements; atomic gateway only; preserve pending offers.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-16, WB-UI-42; EXT-SHOP-TX.
- Likely files/modules (proposed paths unless already in source): `ui/shop/PurchasePresenter`.
- Priority / estimated complexity: **P1 / M**; split at a reviewable boundary if needed.
- Testing requirements: Double click/replay, stale quote, debt, unsupported vendor evidence and logout during commit. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-44 — Bounties list and automatic reward feedback

- Parent: **WB-BNT-04**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Item-goal search/rarity/reward sort, purchased eligibility/status and automatic paid receipt feedback; no Claim button.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-14; EXT-BOUNTIES.
- Likely files/modules (proposed paths unless already in source): `ui/bounty/*`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Paid once, paused no backfill, failure preserves reward, unsupported acquisition not successful. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-45 — Zero-spins and Defy consequence review

- Parent: **WB-DEF-01**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Grand/Defy choices only at legal boundary; preview exact spin refill/special odds and legal full-wheel displacement.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14, WB-UI-16; EXT-DEFY.
- Likely files/modules (proposed paths unless already in source): `ui/risk/DefyPanel`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: No extra slots, restored pending choice, Questing storage-only, no guessed numeric refill. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-46 — Taint/Punishment wheel design checkpoint

- Parent: **WB-UI-06**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Produce restrained distinct special-slice and punishment style studies; keep Master/GF4/B3 unchanged; no gameplay enablement.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14.
- Likely files/modules (proposed paths unless already in source): `ui/risk/review artifacts`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Review reference before final assets merge; static outcome art never changes odds. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-47 — Punishment and sacrifice pending objective

- Parent: **WB-PUN-05**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Persistent mandatory obligation, Taint/Sacrifice one-spin disclosure, no extra punishment spin; safe acquisition/evidence reasons.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-14, WB-UI-16; EXT-PUNISHMENT.
- Likely files/modules (proposed paths unless already in source): `ui/risk/ObligationPanel`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Restart obligation, paused processing, unknown bank distinct from empty; unsupported Coffer stays disabled. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.


## WB-UI-48 — Pardon debt restoration confirmation

- Parent: **WB-DEF-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Fixed expensive reviewed cost, no use cap, only taint debt Banned→Locked; explain separate unlock/gamble after restoration.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-16, WB-UI-42; EXT-PARDON.
- Likely files/modules (proposed paths unless already in source): `ui/risk/PardonPresenter`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Unaffordable/unknown quote, no ordinary unlock or obligation erasure, exact replay. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-49 — Settings, help and evidence notifications

- Parent: **WB-UI-07**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Reduced motion, legal eligibility settings, versioned help; bounded receipt notices and restore/account warnings.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-15, WB-UI-14.
- Likely files/modules (proposed paths unless already in source): `ui/settings/*; ui/notifications/*`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Focus/local shortcuts, unknown evidence retry and no reward duplication or sensitive IDs. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-50 — UI lifecycle and legacy regression acceptance

- Parent: **WB-UI-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Add targeted session/disposal regression harness, existing Master suite, worker/EDT safety and profile-switch checks.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-11, WB-UI-13, WB-UI-16.
- Likely files/modules (proposed paths unless already in source): `src/test ui and existing wheel tests`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: 50 reopen cycles; pending callbacks after dispose; execute actual focused tests, record blocked runs honestly. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-51 — Tree performance and visual acceptance

- Parent: **WB-UI-08**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Measure recorded hardware/scale, 500-node test fixture, repaint/hit/search budgets and responsive resize.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-23, WB-UI-25, WB-UI-29.
- Likely files/modules (proposed paths unless already in source): `ui/tree benchmarks; review capture`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: p95 timing/memory/listener report at 1x/2x; synthetic stress graph never shipped as production catalog. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category A; component inspection, no new major composition approved**.
- Autonomous merge permitted: **Yes for this bounded Category A task after checks, once planning PR merges**. Never infer approval from this TODO record.


## WB-UI-52 — Complete mode UI visual acceptance milestone

- Parent: **WB-REL-12**; existing partial code is reuse input, not completed child scope.
- Description / expected behavior / acceptance: Review every connected major screen and failure state; final Category B implementation only after exact-revision user approval.
- Approved authority and proposed UI specification: UI_ARCHITECTURE.md + UI_DESIGN_SYSTEM.md + parent subsystem specification; canonical DECISIONS overrides unresolved historical wording. This packet's visual compositions remain proposed.
- Current implementation status: **TODO — not started**. Readiness: blocked by the dependencies below.
- Dependencies (must be verified complete for live integration): WB-UI-10, WB-UI-11, WB-UI-12, WB-UI-13, WB-UI-14, WB-UI-15, WB-UI-16, WB-UI-17, WB-UI-18, WB-UI-19, WB-UI-20, WB-UI-21, WB-UI-22, WB-UI-23, WB-UI-24, WB-UI-25, WB-UI-26, WB-UI-27, WB-UI-28, WB-UI-29, WB-UI-30, WB-UI-31, WB-UI-32, WB-UI-33, WB-UI-34, WB-UI-35, WB-UI-36, WB-UI-37, WB-UI-38, WB-UI-39, WB-UI-40, WB-UI-41, WB-UI-42, WB-UI-43, WB-UI-44, WB-UI-45, WB-UI-46, WB-UI-47, WB-UI-48, WB-UI-49, WB-UI-50, WB-UI-51; EXT-LIVE.
- Likely files/modules (proposed paths unless already in source): `review evidence and beta checklist`.
- Priority / estimated complexity: **P2 / M**; split at a reviewable boundary if needed.
- Testing requirements: Actual client screenshots/manual routes, backend evidence gates, no misleading stubs; no Plugin Hub submission. Run focused existing parent regressions where affected; record exact command/result and distinguish headless from actual-client checks.
- Visual review: **Category B; review artifact and exact revision approval required before final visual merge**.
- Autonomous merge permitted: **No for final visuals; isolated nonvisual scaffolding may merge under Category A with final styling disabled**. Never infer approval from this TODO record.
