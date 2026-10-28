# Wheelbound implementation backlog — initial decomposition

> Planning inventory, not a claim that every item is unimplemented or independently PR-sized. Reconcile against the current source tree and merged PRs before execution. This file does not supersede approved decisions, implementation checkpoints, or evidence gates.

## Operating instructions

- Source of truth: `docs/wheelbound/DECISIONS.md` and subsystem specs; current code and merged CI evidence determine DONE status.
- Default each item to `PLANNED_REVIEW`: inspect code first, then mark DONE / PARTIAL / TODO / BLOCKED with evidence. Never redo completed functionality.
- Execute in dependency order: FND → core WHL/FAT/ECON → subsystems → integrated REL. Independent UI/catalog work can proceed when safe.
- Split any item exceeding a 30-minute Codex session into bounded testable increments. One ID is not necessarily one PR.
- Never invent unresolved numeric prices, eligibility, source evidence, client APIs or policy approvals. Keep dependent behavior disabled until verified.
- Preserve legacy Master Wheel behavior and appearance; GF4 is Grand Fate and B3 is Bossing. No main/master merge or Plugin Hub submission.
- Use verified passing CI, reviewed diff and expected-head guard before PR merge into `wheelbound-mode`. Record actual completion in TASK_QUEUE and IMPLEMENTATION_CHECKPOINT.

## Coding inventory

### Foundation and account lifecycle (14)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-FND-01 | Audit current I01 implementations and regression coverage | PLANNED_REVIEW |
| WB-FND-02 | Wire supported RuneLite plugin directory provider | PLANNED_REVIEW |
| WB-FND-03 | Add disabled-by-default mode opt-in config | PLANNED_REVIEW |
| WB-FND-04 | Connect verified account identity lifecycle | PLANNED_REVIEW |
| WB-FND-05 | Invalidate sessions on logout/hop/profile swap | PLANNED_REVIEW |
| WB-FND-06 | Capture fresh baseline before observation admission | PLANNED_REVIEW |
| WB-FND-07 | Build safe existing-save preview UI | PLANNED_REVIEW |
| WB-FND-08 | Add explicit new-run creation workflow | PLANNED_REVIEW |
| WB-FND-09 | Add crash/pending-file recovery inspection | PLANNED_REVIEW |
| WB-FND-10 | Implement complete versioned gameplay aggregate | PLANNED_REVIEW |
| WB-FND-11 | Persist spin and Fate Point transactions atomically | PLANNED_REVIEW |
| WB-FND-12 | Add save migrations and future-schema refusal | PLANNED_REVIEW |
| WB-FND-13 | Test concurrent writes and replay collisions | PLANNED_REVIEW |
| WB-FND-14 | Verify Windows/Linux filesystem behavior | PLANNED_REVIEW |

### Master Wheel and builder (12)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-WHL-01 | Preserve legacy normal-wheel rendering | PLANNED_REVIEW |
| WB-WHL-02 | Define immutable 24-slot active wheel model | PLANNED_REVIEW |
| WB-WHL-03 | Implement slice ownership and storage inventory | PLANNED_REVIEW |
| WB-WHL-04 | Implement paid slice add/remove transaction | PLANNED_REVIEW |
| WB-WHL-05 | Implement purchase escalation on reacquisition | PLANNED_REVIEW |
| WB-WHL-06 | Implement active-slot replacement at capacity | PLANNED_REVIEW |
| WB-WHL-07 | Implement weighted odds display and previews | PLANNED_REVIEW |
| WB-WHL-08 | Implement deterministic Master Wheel spin commitment | PLANNED_REVIEW |
| WB-WHL-09 | Implement valid-empty landing free retry | PLANNED_REVIEW |
| WB-WHL-10 | Implement all-unavailable readiness stop | PLANNED_REVIEW |
| WB-WHL-11 | Implement Questing indestructible storage behavior | PLANNED_REVIEW |
| WB-WHL-12 | Implement slice mastery weight upgrades | PLANNED_REVIEW |

### Grand Fate (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-GFD-01 | Implement five-outcome equal-weight Grand Fate pool | PLANNED_REVIEW |
| WB-GFD-02 | Persist sealed Grand Fate at run start | PLANNED_REVIEW |
| WB-GFD-03 | Implement Grand Fate onboarding and reveal | PLANNED_REVIEW |
| WB-GFD-04 | Implement GF4 theme assets within RuneLite constraints | PLANNED_REVIEW |
| WB-GFD-05 | Implement Grand Fate completion evidence model | PLANNED_REVIEW |
| WB-GFD-06 | Implement Grand Fate activation obligation gate | PLANNED_REVIEW |
| WB-GFD-07 | Implement Grand Fate attempts without Master spin charge | PLANNED_REVIEW |

### Fate assignments and cards (14)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-FAT-01 | Implement standard Fate lifecycle state machine | PLANNED_REVIEW |
| WB-FAT-02 | Implement Lesser/Standard/Greater card ownership | PLANNED_REVIEW |
| WB-FAT-03 | Implement card offering pool and count upgrades | PLANNED_REVIEW |
| WB-FAT-04 | Implement Fate card selection before target reveal | PLANNED_REVIEW |
| WB-FAT-05 | Implement XP tolerance and penalties | PLANNED_REVIEW |
| WB-FAT-06 | Implement completion and Reject settlement | PLANNED_REVIEW |
| WB-FAT-07 | Implement Fate assignment history and audit trail | PLANNED_REVIEW |
| WB-FAT-08 | Implement no-reroll and stale callback protection | PLANNED_REVIEW |
| WB-FAT-09 | Implement rank-specific challenge cards and toggles | PLANNED_REVIEW |
| WB-FAT-10 | Implement challenge unlock AND prerequisites | PLANNED_REVIEW |
| WB-FAT-11 | Implement challenge target entitlement purchases | PLANNED_REVIEW |
| WB-FAT-12 | Implement eligible target diversity threshold | PLANNED_REVIEW |
| WB-FAT-13 | Implement durable challenge creature/item draws | PLANNED_REVIEW |
| WB-FAT-14 | Implement fresh specific-item drop completion | PLANNED_REVIEW |

### Bossing and combat (10)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-BOS-01 | Implement B3 stone-and-blue-gems Bossing UI | PLANNED_REVIEW |
| WB-BOS-02 | Implement boss eligibility and supported requirements | PLANNED_REVIEW |
| WB-BOS-03 | Implement boss wheel weighted selection | PLANNED_REVIEW |
| WB-BOS-04 | Implement bossing KC objective and card modifiers | PLANNED_REVIEW |
| WB-BOS-05 | Implement boss kill attribution and baseline | PLANNED_REVIEW |
| WB-BOS-06 | Implement combat XP objective slice | PLANNED_REVIEW |
| WB-BOS-07 | Implement eligible-only and open-CA boss filters | PLANNED_REVIEW |
| WB-BOS-08 | Implement boss/monster Challenge catalogs separately | PLANNED_REVIEW |
| WB-BOS-09 | Implement supported personal drop-context validation | PLANNED_REVIEW |
| WB-BOS-10 | Implement bossing completion and receipt tests | PLANNED_REVIEW |

### Questing (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-QST-01 | Implement quest eligibility snapshot | PLANNED_REVIEW |
| WB-QST-02 | Implement 1–3 quest display and upgrade capacities | PLANNED_REVIEW |
| WB-QST-03 | Implement qualify-only toggle | PLANNED_REVIEW |
| WB-QST-04 | Implement deterministic quest selection | PLANNED_REVIEW |
| WB-QST-05 | Implement quest completion event evidence | PLANNED_REVIEW |
| WB-QST-06 | Implement empty-quest free respin | PLANNED_REVIEW |
| WB-QST-07 | Implement postcommit quest invalidation handling | PLANNED_REVIEW |

### Skilling and Slayer (6)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-SKL-01 | Implement skill slice tier unlocks | PLANNED_REVIEW |
| WB-SKL-02 | Implement skill XP objective assignment | PLANNED_REVIEW |
| WB-SKL-03 | Implement skill XP observation and tolerance | PLANNED_REVIEW |
| WB-SKL-04 | Implement skill mastery odds progression | PLANNED_REVIEW |
| WB-SKL-05 | Implement Slayer final-tier prerequisite | PLANNED_REVIEW |
| WB-SKL-06 | Implement Slayer task eligibility and tracking | PLANNED_REVIEW |

### Fate Tree and progression (7)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-TREE-01 | Implement four progression branches | PLANNED_REVIEW |
| WB-TREE-02 | Implement AND/OR prerequisite graph engine | PLANNED_REVIEW |
| WB-TREE-03 | Implement progressive unlock retention | PLANNED_REVIEW |
| WB-TREE-04 | Implement tier-count unlock gates | PLANNED_REVIEW |
| WB-TREE-05 | Implement unlock purchases and immutable receipts | PLANNED_REVIEW |
| WB-TREE-06 | Implement mastery and card upgrade UI | PLANNED_REVIEW |
| WB-TREE-07 | Implement progression reset/recovery tests | PLANNED_REVIEW |

### Fate Points and shops (9)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-ECON-01 | Implement Fate Point ledger and balance checks | PLANNED_REVIEW |
| WB-ECON-02 | Implement FP earning from verified objectives | PLANNED_REVIEW |
| WB-ECON-03 | Implement FP cost and price versioning | PLANNED_REVIEW |
| WB-ECON-04 | Implement Fate Shop catalog and ownership | PLANNED_REVIEW |
| WB-ECON-05 | Implement Wheel Builder shop transactions | PLANNED_REVIEW |
| WB-ECON-06 | Implement vendor unlock shop and access gates | PLANNED_REVIEW |
| WB-ECON-07 | Implement Bounties Shop entitlement catalog | PLANNED_REVIEW |
| WB-ECON-08 | Implement purchase replay and refund prevention | PLANNED_REVIEW |
| WB-ECON-09 | Implement candidate economy schedules behind approval gates | PLANNED_REVIEW |

### Defy Fate and taint (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-DEF-01 | Implement zero-spins decision flow | PLANNED_REVIEW |
| WB-DEF-02 | Implement Defy Fate additional spin grant | PLANNED_REVIEW |
| WB-DEF-03 | Implement distinct tainted slice injection | PLANNED_REVIEW |
| WB-DEF-04 | Implement full-wheel replacement rules | PLANNED_REVIEW |
| WB-DEF-05 | Implement Taint wheel selection and theme | PLANNED_REVIEW |
| WB-DEF-06 | Implement tainted landing punishment trigger | PLANNED_REVIEW |
| WB-DEF-07 | Implement tainted-task spin charge | PLANNED_REVIEW |
| WB-DEF-08 | Implement taint debt and Fate Pardon purchase | PLANNED_REVIEW |

### Punishments and sacrifice (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-PUN-01 | Implement punishment severity tier model | PLANNED_REVIEW |
| WB-PUN-02 | Implement punishment task catalog validation | PLANNED_REVIEW |
| WB-PUN-03 | Implement punishment wheel and selection | PLANNED_REVIEW |
| WB-PUN-04 | Implement punishment evidence and completion | PLANNED_REVIEW |
| WB-PUN-05 | Implement mandatory settlement and pause behavior | PLANNED_REVIEW |
| WB-PUN-06 | Implement Death's Coffer evidence adapter after verification | PLANNED_REVIEW |
| WB-PUN-07 | Implement full/unsupported Coffer fail-closed handling | PLANNED_REVIEW |
| WB-PUN-08 | Implement forced sacrifice ledger and audit | PLANNED_REVIEW |

### Bounties and rewards (8)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-BNT-01 | Implement permanent item bounty catalog | PLANNED_REVIEW |
| WB-BNT-02 | Implement bounty activation and availability | PLANNED_REVIEW |
| WB-BNT-03 | Implement item acquisition evidence attribution | PLANNED_REVIEW |
| WB-BNT-04 | Implement automatic independent qualifying FP rewards | PLANNED_REVIEW |
| WB-BNT-05 | Implement no rewards while paused | PLANNED_REVIEW |
| WB-BNT-06 | Implement duplicate-event and replay protection | PLANNED_REVIEW |
| WB-BNT-07 | Implement daily/diary receipt families only when approved | PLANNED_REVIEW |
| WB-BNT-08 | Separate legacy boss CA reward scope from Bounties Shop | PLANNED_REVIEW |

### Client UX and themes (9)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-UI-01 | Implement main Wheelbound Hub navigation | PLANNED_REVIEW |
| WB-UI-02 | Implement active Fate status and progress panel | PLANNED_REVIEW |
| WB-UI-03 | Implement owned-card and offer selection UI | PLANNED_REVIEW |
| WB-UI-04 | Implement GF4 compliant asset loading | PLANNED_REVIEW |
| WB-UI-05 | Implement B3 compliant asset loading | PLANNED_REVIEW |
| WB-UI-06 | Implement punishment and taint visual treatments | PLANNED_REVIEW |
| WB-UI-07 | Implement save and account state warnings | PLANNED_REVIEW |
| WB-UI-08 | Implement accessibility and scaling checks | PLANNED_REVIEW |
| WB-UI-09 | Ensure incomplete mechanics stay hidden/disabled | PLANNED_REVIEW |

### Integration, tests and release (12)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-REL-01 | Build cross-system deterministic replay fixtures | PLANNED_REVIEW |
| WB-REL-02 | Test pause/resume and logout across each activity | PLANNED_REVIEW |
| WB-REL-03 | Test stale/reordered client event receipts | PLANNED_REVIEW |
| WB-REL-04 | Test account separation and profile switching | PLANNED_REVIEW |
| WB-REL-05 | Test corrupt/recovery scenarios and migrations | PLANNED_REVIEW |
| WB-REL-06 | Run existing Master Wheel regression suite | PLANNED_REVIEW |
| WB-REL-07 | Run full Gradle and GitHub CI matrix | PLANNED_REVIEW |
| WB-REL-08 | Capture live-client event evidence packet | PLANNED_REVIEW |
| WB-REL-09 | Complete policy review for restricted interactions | PLANNED_REVIEW |
| WB-REL-10 | Audit dependency, security and asset licenses | PLANNED_REVIEW |
| WB-REL-11 | Validate RuneLite Plugin Hub requirements | PLANNED_REVIEW |
| WB-REL-12 | Prepare opt-in beta acceptance and release checklist | PLANNED_REVIEW |

**Total candidate implementation units: 131.** These are a decomposition of approved scope plus explicit evidence-gated integration work, not 131 verified outstanding tasks. Reconcile status before deriving a remaining-work count.

## Decision and verification holds

- Balance numbers and catalog expansions remain candidate until their actual approval/evidence status is confirmed.
- RuneLite event attribution, Death's Coffer, vendor/GE/trade restrictions and real-client traces must not be inferred from offline fixtures.
- Separate historical CA rewards from the item Bounties Shop and Master hilt requirement.
- Previously deferred permanent-invalidation proposal stays deferred; do not implement a guessed fallback.
- No autonomous agent should treat a backlog row as permission to override `DECISIONS.md` or the protocol.
