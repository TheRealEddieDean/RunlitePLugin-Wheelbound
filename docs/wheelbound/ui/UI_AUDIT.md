# Source audit and integration evidence

## Scope and evidence

Inspected local clone of remote `wheelbound-mode` at `223950c` on 2026-10-29 (Chicago). Read current headers plus operative sections of SESSION_STATE, PROTOCOL, TASK_QUEUE, DECISIONS_PENDING and IMPLEMENTATION_BACKLOG; DECISIONS and relevant subsystem/design specs; all production source inventory, concrete Swing entry points, progression evaluator and test/build inventory. Historical continuation headers are repeated and sometimes contradictory: current code and latest explicit decisions take precedence.

GitHub API confirmed PR #57 merged at `223950c` and #56 at `f7cb92e`. Local diffs show #57 added Grand attempt success proposals and checks; #56 added monotonic progress extension. Their short PR bodies say tests passed, while committed notes record unavailable Gradle runs. These are not independent proof of full-suite success. No current Java or live-client pass is claimed by this documentation PR.

| System | Concrete source evidence | Current mode status / missing work |
| --- | --- | --- |
| Legacy normal wheels | WheelboundPanel, WheelComponent, WheelStyle, WheelPopup, WheelSelection | Implemented legacy selection/filter/checklist/result flow. Master appearance preserved; mode readiness/debits not established by this UI. |
| Popup / navigation | NavigationButton and PluginPanel in WheelboundPlugin; canvas Overlay in WheelPopup | Working legacy overlay, not a Swing viewport replacement. No central mode Hub. |
| Small owned window | WheelNameDialog | Modeless JDialog owner derived from anchor; headless form support. Reuse lifecycle ideas, not its existing toFront behavior. |
| Assets | classic-wheel.png, wheelbound-header.png, root icon.png, WheelIconProvider, MonsterThumbnail | Existing local artwork and sprite loading. No new fonts needed. GF4 PNG is docs-only; B3 selected simplified reference PNG is absent from repository. |
| Lifecycle / account | RunLifecycle, RunIdentity, RunSession, AsyncRunSession, FoundationPreview | Partial foundations; process opt-in/default-disabled preview, identity/generation and revision checks. No production plugin binding to these classes. |
| Persistence / FP | RunDocument, RunCodec schema 9, AtomicRunStore, DurableFatePointDebits, FatePointLedger | Real storage/debit primitives. Complete purchase/evidence/objective transaction services remain unfinished. In-memory proposals must never be presented as committed purchases. |
| Wheel Builder | ActiveWheel, SliceInventory, WheelOdds, SlicePurchaseProposal/Receipt | Domain inventory/edit proposals and odds exist. No Builder screen or complete durable paid edit integration. |
| Fate Tree | ProgressionGraph; ProgressionGraphTest | Nested ROOT/OWNED/ALL/ANY evaluator, cycle/reference validation and canUnlock. No countOf, public graph traversal, node metadata, price catalog, layout or renderer. Test-only graph construction; no production instantiated progression tree. |
| Grand Fate | GrandFatePool, SealedGrandFate, Checklist, Progress, Attempt | Pool/seal/checklist/progress models and transition proposals; persistent references with guard against unauthorized progress changes. No trusted evidence acceptance, full attempt settlement, win flow or GF4 runtime rendering. |
| Boss / quest | BossCatalog, BossData, QuestPool, WheelEligibility, WheelFilter | Legacy eligibility/selection reusable; not durable mode assignments. B3, cards, mode objectives and committed offer flow missing. |
| Other mode systems | Canonical gameplay specs and finite draft catalogs | Shops, ranked Challenge UI, punishment/taint/Defy, bounties and economy screens missing. Content metadata is not runtime eligibility. |

## Workflow and safe integration

No supervisor `.ps1`, scheduler, claim parser or Windows runtime prompt is tracked in this checkout (`rg --files`). We cannot inspect files on the user's PC. The repository PROTOCOL describes highest-priority TODO/PARTIAL with satisfied dependencies, bounded work and ownership guards. IMPLEMENTATION_BACKLOG has `WB-*` ID/title/status tables; TASK_QUEUE has task records and continuation prose, not an executable machine schema. This is evidence of the documented contract, **not verified automatic runtime discovery**.

Integration adds one append-only discoverability section to TASK_QUEUE and one decomposition pointer to IMPLEMENTATION_BACKLOG. Full detail lives in this directory to reduce collision surface. Each new WB-UI-10+ task maps to an existing parent; existing IDs/status/history are retained. New children subdivide existing scope, not extra parallel implementations. Parent completion requires all applicable children and original backend acceptance. Do not count parent and children as separate finished features.

After approval/merge: normal repository refresh exposes both pointers. A language-model reader following repository instructions can traverse the links without a supervisor change. If the PC runner reads fixed IDs, truncates these long files before appended sections, or parses a fixed regex, automatic discovery remains unverified. Validate one refresh log showing WB-UI-10 discovered before claiming integration operational. If that fails, request approval for the smallest runner/prompt change; do not silently alter scripts. No script, schedule, lease or implementation priority was changed here.

At PR preparation, fetch latest source, merge its new commits into this design branch (ordinary merge, no rewrite), reconcile additions and recheck diff. Existing active continuation remains authoritative for the supervisor. The planning PR must not auto-merge. After it merges, Category A work may follow existing authority; Category B final visuals require a recorded user approval matching the reviewed preview/revision.

## Verification boundary

Documentation/link/ID/dependency/status checks and preview rendering are recorded in [VERIFICATION](VERIFICATION.md). No production source/build settings are changed. Gradle targets Java 11, wrapper 8.10, RuneLite `latest.release`, JUnit 4 and Mockito 4.11; do not infer Java 17 or Gradle 9 from the user's other workstation. Existing WheelPopup, WheelboundBehavior, event, persistence/session, inventory and graph tests are regression entry points for future tasks.
