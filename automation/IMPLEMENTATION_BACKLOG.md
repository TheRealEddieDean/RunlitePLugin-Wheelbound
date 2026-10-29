# Wheelbound implementation backlog Ã¢â‚¬â€ initial decomposition

> Planning inventory, not a claim that every item is unimplemented or independently PR-sized. Reconcile against the current source tree and merged PRs before execution. This file does not supersede approved decisions, implementation checkpoints, or evidence gates.

## Operating instructions

- Source of truth: `docs/wheelbound/DECISIONS.md` and subsystem specs; current code and merged CI evidence determine DONE status.
- Default each item to `PLANNED_REVIEW`: inspect code first, then mark DONE / PARTIAL / TODO / BLOCKED with evidence. Never redo completed functionality.
- Execute in dependency order: FND Ã¢â€ â€™ core WHL/FAT/ECON Ã¢â€ â€™ subsystems Ã¢â€ â€™ integrated REL. Independent UI/catalog work can proceed when safe.
- Split any item exceeding a 30-minute Codex session into bounded testable increments. One ID is not necessarily one PR.
- Never invent unresolved numeric prices, eligibility, source evidence, client APIs or policy approvals. Keep dependent behavior disabled until verified.
- Preserve legacy Master Wheel behavior and appearance; GF4 is Grand Fate and B3 is Bossing. No main/master merge or Plugin Hub submission.
- Use verified passing CI, reviewed diff and expected-head guard before PR merge into `wheelbound-mode`. Record actual completion in TASK_QUEUE and IMPLEMENTATION_CHECKPOINT.

## Reconciliation - 2026-10-28, local source audit

Statuses below describe Wheelbound mode, not similarly named shipped normal-wheel features.
DONE means the named bounded audit is complete; PARTIAL does not imply playable gameplay.
No Git operations were used. Merged receipts are taken from the existing implementation
checkpoint, without independently verifying remote history. Current user instructions
supersede historical branch/publication instructions. No fixed remaining-task count is claimed.

- WB-FND-01 DONE: inspected domain, persistence and plugin/config source inventory, focused
  test inventory and recorded I01a/b/c merged receipts. Lifecycle, schema-3 codec, atomic
  snapshots, lifecycle receipts and frozen weighted draws exist. No full gameplay aggregate.
- WB-FND-03/04/05/06/07 PARTIAL: FoundationPreview, AsyncRunSession and RunSession provide
  process opt-in, caller identity/token and baseline guards; no plugin config, live identity
  lifecycle, actual baseline sampling or preview UI wiring. FND-02 and FND-08 remain TODO.
- WB-FND-09/10/12/13/14 PARTIAL: AtomicRunStore/RunCodec/RunDocument and focused tests
  cover fail-closed pending/corrupt data, foundation schemas, revision/replay conflicts and
  platform-specific directory-force behavior. Explicit recovery, complete aggregate,
  gameplay migrations and verified platform/runtime coverage remain unfinished.
- WB-WHL-08 and WB-FAT-01/08 PARTIAL: DurableWheelDraws and RunLifecycle supply immutable
  commitments, pause/resume and stale-context guards; real Master spin debit/eligibility,
  offers, selection, objective processing and settlement are not implemented.
- WB-BOS-02/03/07 and WB-QST-01/03/04 PARTIAL: existing BossCatalog/BossData,
  WheelEligibility, QuestPool and normal-wheel filters/selections provide reusable work.
  Mode snapshots, durable subsystem draws and mode gates are unfinished. WHL-01 remains
  TODO for integrated mode regression acceptance; shipped rendering is left untouched.
- WB-TREE-02 PARTIAL_UNVERIFIED: ProgressionGraph now implements immutable nested AND/OR
  prerequisites, explicit roots, owned-node checks and dangling/cycle rejection. Catalog
  integration and executed tests remain. WB-D093 is authority; Challenge test examples
  follow WB-D124/147. This does not implement four branches, purchases or benefit grants.
- WB-UI-09 PARTIAL: mode foundations have no live plugin wiring and preview defaults off;
  incomplete gameplay remains disabled, but future UI integration still needs acceptance.
- WB-REL-01 through 07 PARTIAL: existing domain/storage/session and legacy tests cover
  foundation behavior only. Recorded earlier CI passes do not verify current local changes.
  Existing build reports are historical artifacts, not evidence of this session's test pass.
- BLOCKED rows require unresolved approval/source/current-client/policy evidence: economy
  candidate adoption, Coffer boundaries, daily/legacy reward scope, actual client traces,
  policy review and release acceptance. No unresolved decision is inferred from old plans.
- All remaining TODO rows lack a complete mode implementation in the inspected source;
  legacy wheel/catalog/filter support does not constitute implemented mode gameplay.

Selected increment: WB-TREE-02 prerequisite evaluator (S), independent of I01d and numeric
balance. Tests authored; Gradle failed before compilation because workspace Gradle 8.10
is absent and wrapper download returned Permission denied. No test pass claimed.
Next eligible gameplay increment: WB-WHL-02 immutable 24-slot active-wheel model after
reading its exact ordinary-count/activity/weight/special-slice decisions. Do not resume
optional I01d hardening absent a concrete failing test or required dependency.
## Reconciliation update - 2026-10-29

WB-WHL-02 now PARTIAL_UNVERIFIED: immutable ActiveWheel and three focused JUnit methods
cover persistent identities, shared 24 slots, ordinary five/three minimum, exact enhancement
weights and permutation-only reorder. Special weights are explicit; no Taint default adopted.
Java 11 production compilation passed for ActiveWheel and ProgressionGraph. Gradle failed
before compilation (missing distribution, blocked download); no JUnit pass. Catalog and
aggregate integration remain unfinished. Other audited statuses stand; recorded merged
receipts were not remotely reverified. WHL-07 odds UI and WHL-03 inventory remain TODO.
Next eligible unit: WB-WHL-03 owned-slice/Satchel inventory, reading WB-D011/118/121 first.
Do not select I01d without a concrete failing test or required dependency.

## Coding inventory

### Foundation and account lifecycle (14)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-FND-01 | Audit current I01 implementations and regression coverage | DONE |
| WB-FND-02 | Wire supported RuneLite plugin directory provider | TODO |
| WB-FND-03 | Add disabled-by-default mode opt-in config | PARTIAL |
| WB-FND-04 | Connect verified account identity lifecycle | PARTIAL |
| WB-FND-05 | Invalidate sessions on logout/hop/profile swap | PARTIAL |
| WB-FND-06 | Capture fresh baseline before observation admission | PARTIAL |
| WB-FND-07 | Build safe existing-save preview UI | PARTIAL |
| WB-FND-08 | Add explicit new-run creation workflow | TODO |
| WB-FND-09 | Add crash/pending-file recovery inspection | PARTIAL |
| WB-FND-10 | Implement complete versioned gameplay aggregate | PARTIAL |
| WB-FND-11 | Persist spin and Fate Point transactions atomically | TODO |
| WB-FND-12 | Add save migrations and future-schema refusal | PARTIAL |
| WB-FND-13 | Test concurrent writes and replay collisions | PARTIAL |
| WB-FND-14 | Verify Windows/Linux filesystem behavior | PARTIAL |

### Master Wheel and builder (12)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-WHL-01 | Preserve legacy normal-wheel rendering | TODO |
| WB-WHL-02 | Define immutable 24-slot active wheel model | PARTIAL |
| WB-WHL-03 | Implement slice ownership and storage inventory | PARTIAL |
| WB-WHL-04 | Implement paid slice add/remove transaction | TODO |
| WB-WHL-05 | Implement purchase escalation on reacquisition | TODO |
| WB-WHL-06 | Implement active-slot replacement at capacity | TODO |
| WB-WHL-07 | Implement weighted odds display and previews | TODO |
| WB-WHL-08 | Implement deterministic Master Wheel spin commitment | PARTIAL |
| WB-WHL-09 | Implement valid-empty landing free retry | TODO |
| WB-WHL-10 | Implement all-unavailable readiness stop | TODO |
| WB-WHL-11 | Implement Questing indestructible storage behavior | TODO |
| WB-WHL-12 | Implement slice mastery weight upgrades | TODO |

### Grand Fate (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-GFD-01 | Implement five-outcome equal-weight Grand Fate pool | TODO |
| WB-GFD-02 | Persist sealed Grand Fate at run start | TODO |
| WB-GFD-03 | Implement Grand Fate onboarding and reveal | TODO |
| WB-GFD-04 | Implement GF4 theme assets within RuneLite constraints | TODO |
| WB-GFD-05 | Implement Grand Fate completion evidence model | TODO |
| WB-GFD-06 | Implement Grand Fate activation obligation gate | TODO |
| WB-GFD-07 | Implement Grand Fate attempts without Master spin charge | TODO |

### Fate assignments and cards (14)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-FAT-01 | Implement standard Fate lifecycle state machine | PARTIAL |
| WB-FAT-02 | Implement Lesser/Standard/Greater card ownership | TODO |
| WB-FAT-03 | Implement card offering pool and count upgrades | TODO |
| WB-FAT-04 | Implement Fate card selection before target reveal | TODO |
| WB-FAT-05 | Implement XP tolerance and penalties | TODO |
| WB-FAT-06 | Implement completion and Reject settlement | TODO |
| WB-FAT-07 | Implement Fate assignment history and audit trail | TODO |
| WB-FAT-08 | Implement no-reroll and stale callback protection | PARTIAL |
| WB-FAT-09 | Implement rank-specific challenge cards and toggles | TODO |
| WB-FAT-10 | Implement challenge unlock AND prerequisites | TODO |
| WB-FAT-11 | Implement challenge target entitlement purchases | TODO |
| WB-FAT-12 | Implement eligible target diversity threshold | TODO |
| WB-FAT-13 | Implement durable challenge creature/item draws | TODO |
| WB-FAT-14 | Implement fresh specific-item drop completion | TODO |

### Bossing and combat (10)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-BOS-01 | Implement B3 stone-and-blue-gems Bossing UI | TODO |
| WB-BOS-02 | Implement boss eligibility and supported requirements | PARTIAL |
| WB-BOS-03 | Implement boss wheel weighted selection | PARTIAL |
| WB-BOS-04 | Implement bossing KC objective and card modifiers | TODO |
| WB-BOS-05 | Implement boss kill attribution and baseline | TODO |
| WB-BOS-06 | Implement combat XP objective slice | TODO |
| WB-BOS-07 | Implement eligible-only and open-CA boss filters | PARTIAL |
| WB-BOS-08 | Implement boss/monster Challenge catalogs separately | TODO |
| WB-BOS-09 | Implement supported personal drop-context validation | TODO |
| WB-BOS-10 | Implement bossing completion and receipt tests | TODO |

### Questing (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-QST-01 | Implement quest eligibility snapshot | PARTIAL |
| WB-QST-02 | Implement 1Ã¢â‚¬â€œ3 quest display and upgrade capacities | TODO |
| WB-QST-03 | Implement qualify-only toggle | PARTIAL |
| WB-QST-04 | Implement deterministic quest selection | PARTIAL |
| WB-QST-05 | Implement quest completion event evidence | TODO |
| WB-QST-06 | Implement empty-quest free respin | TODO |
| WB-QST-07 | Implement postcommit quest invalidation handling | TODO |

### Skilling and Slayer (6)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-SKL-01 | Implement skill slice tier unlocks | TODO |
| WB-SKL-02 | Implement skill XP objective assignment | TODO |
| WB-SKL-03 | Implement skill XP observation and tolerance | TODO |
| WB-SKL-04 | Implement skill mastery odds progression | TODO |
| WB-SKL-05 | Implement Slayer final-tier prerequisite | TODO |
| WB-SKL-06 | Implement Slayer task eligibility and tracking | TODO |

### Fate Tree and progression (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-TREE-01 | Implement four progression branches | TODO |
| WB-TREE-02 | Implement AND/OR prerequisite graph engine | PARTIAL |
| WB-TREE-03 | Implement progressive unlock retention | TODO |
| WB-TREE-04 | Implement tier-count unlock gates | TODO |
| WB-TREE-05 | Implement unlock purchases and immutable receipts | TODO |
| WB-TREE-06 | Implement mastery and card upgrade UI | TODO |
| WB-TREE-07 | Implement progression reset/recovery tests | TODO |

### Fate Points and shops (9)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-ECON-01 | Implement Fate Point ledger and balance checks | TODO |
| WB-ECON-02 | Implement FP earning from verified objectives | TODO |
| WB-ECON-03 | Implement FP cost and price versioning | TODO |
| WB-ECON-04 | Implement Fate Shop catalog and ownership | TODO |
| WB-ECON-05 | Implement Wheel Builder shop transactions | TODO |
| WB-ECON-06 | Implement vendor unlock shop and access gates | TODO |
| WB-ECON-07 | Implement Bounties Shop entitlement catalog | TODO |
| WB-ECON-08 | Implement purchase replay and refund prevention | TODO |
| WB-ECON-09 | Implement candidate economy schedules behind approval gates | BLOCKED |

### Defy Fate and taint (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-DEF-01 | Implement zero-spins decision flow | TODO |
| WB-DEF-02 | Implement Defy Fate additional spin grant | TODO |
| WB-DEF-03 | Implement distinct tainted slice injection | TODO |
| WB-DEF-04 | Implement full-wheel replacement rules | TODO |
| WB-DEF-05 | Implement Taint wheel selection and theme | TODO |
| WB-DEF-06 | Implement tainted landing punishment trigger | TODO |
| WB-DEF-07 | Implement tainted-task spin charge | TODO |
| WB-DEF-08 | Implement taint debt and Fate Pardon purchase | TODO |

### Punishments and sacrifice (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-PUN-01 | Implement punishment severity tier model | TODO |
| WB-PUN-02 | Implement punishment task catalog validation | TODO |
| WB-PUN-03 | Implement punishment wheel and selection | TODO |
| WB-PUN-04 | Implement punishment evidence and completion | TODO |
| WB-PUN-05 | Implement mandatory settlement and pause behavior | TODO |
| WB-PUN-06 | Implement Death's Coffer evidence adapter after verification | BLOCKED |
| WB-PUN-07 | Implement full/unsupported Coffer fail-closed handling | BLOCKED |
| WB-PUN-08 | Implement forced sacrifice ledger and audit | TODO |

### Bounties and rewards (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-BNT-01 | Implement permanent item bounty catalog | TODO |
| WB-BNT-02 | Implement bounty activation and availability | TODO |
| WB-BNT-03 | Implement item acquisition evidence attribution | TODO |
| WB-BNT-04 | Implement automatic independent qualifying FP rewards | TODO |
| WB-BNT-05 | Implement no rewards while paused | TODO |
| WB-BNT-06 | Implement duplicate-event and replay protection | TODO |
| WB-BNT-07 | Implement daily/diary receipt families only when approved | BLOCKED |
| WB-BNT-08 | Separate legacy boss CA reward scope from Bounties Shop | BLOCKED |

### Client UX and themes (9)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-UI-01 | Implement main Wheelbound Hub navigation | TODO |
| WB-UI-02 | Implement active Fate status and progress panel | TODO |
| WB-UI-03 | Implement owned-card and offer selection UI | TODO |
| WB-UI-04 | Implement GF4 compliant asset loading | TODO |
| WB-UI-05 | Implement B3 compliant asset loading | TODO |
| WB-UI-06 | Implement punishment and taint visual treatments | TODO |
| WB-UI-07 | Implement save and account state warnings | TODO |
| WB-UI-08 | Implement accessibility and scaling checks | TODO |
| WB-UI-09 | Ensure incomplete mechanics stay hidden/disabled | PARTIAL |

### Integration, tests and release (12)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-REL-01 | Build cross-system deterministic replay fixtures | PARTIAL |
| WB-REL-02 | Test pause/resume and logout across each activity | PARTIAL |
| WB-REL-03 | Test stale/reordered client event receipts | PARTIAL |
| WB-REL-04 | Test account separation and profile switching | PARTIAL |
| WB-REL-05 | Test corrupt/recovery scenarios and migrations | PARTIAL |
| WB-REL-06 | Run existing Master Wheel regression suite | PARTIAL |
| WB-REL-07 | Run full Gradle and GitHub CI matrix | PARTIAL |
| WB-REL-08 | Capture live-client event evidence packet | BLOCKED |
| WB-REL-09 | Complete policy review for restricted interactions | BLOCKED |
| WB-REL-10 | Audit dependency, security and asset licenses | TODO |
| WB-REL-11 | Validate RuneLite Plugin Hub requirements | BLOCKED |
| WB-REL-12 | Prepare opt-in beta acceptance and release checklist | TODO |

**Total candidate implementation units: 131.** These are a decomposition of approved scope plus explicit evidence-gated integration work, not 131 verified outstanding tasks. Reconcile status before deriving a remaining-work count.

## Decision and verification holds

- Balance numbers and catalog expansions remain candidate until their actual approval/evidence status is confirmed.
- RuneLite event attribution, Death's Coffer, vendor/GE/trade restrictions and real-client traces must not be inferred from offline fixtures.
- Separate historical CA rewards from the item Bounties Shop and Master hilt requirement.
- Previously deferred permanent-invalidation proposal stays deferred; do not implement a guessed fallback.
- No autonomous agent should treat a backlog row as permission to override `DECISIONS.md` or the protocol.

## Reconciliation update - owned inventory, 2026-10-29

WB-WHL-03 is PARTIAL_UNVERIFIED. SliceInventory and three focused JUnit methods now
cover immutable active/stored ownership, approved 3/6/10/15 Satchel capacities, ordinary-only
storage, unique cross-location IDs, retained unlocks and explicit lifetime acquisition
history independent of current copies. Starting baseline and paid duplicates are separate;
unlock-only activities are permitted. Stored Questing retains its instance/modifiers.
No movement/purchase/destruction transitions, replay receipts, FP transactions, catalog
admission or durable aggregate integration are implemented. WHL-04/05/11 remain TODO.
All other reconciled statuses stand; existing merged receipts were not remotely verified.
Java 11 production compilation passed. Gradle failed before test compilation because its
missing 8.10 distribution could not download (Permission denied); no JUnit pass claimed.
Next bounded unit: acquisition-history purchase transition/lifetime classification after
reading transaction contracts; prices stay unadopted. Paid wheel edits require FP and
atomic receipt prerequisites. Do not select I01d absent a concrete blocking dependency.
