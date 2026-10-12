# First Wheelbound Codex implementation packet

> Current authority2026-10-12: WB-D111–115 supersede prior Questing suspension/retry prohibition, Q10/manual bounty claiming and PAUSE-CLAIM uncertainty. Known-empty Questing automatically retries with retained slice; supported active bounty automatically pays once and survives failure/Reject; paused payout forbidden. Old fixture dispositions/pins remain prior-contract evidence, not current acceptance of superseded behavior. CA-MIXED and extra Bossing Challenge remain unanswered. Read current DECISIONS before coding.

Prepared2026-10-12, WB-A30. **Planning only. Production coding is not authorized by this packet.** Use after the user expressly starts implementation. Existing Markdown decisions remain authoritative. Suggested names, grouping and estimates below are implementation planning, not gameplay approvals.

## Entry checklist

Read DECISIONS, STATE_CONTRACT, PERSISTENCE, ARCHITECTURE, DEVELOPMENT_PIPELINE and OPEN_QUESTIONS under docs/wheelbound; then automation/SESSION_STATE, pipeline_acceptance/ACCEPTANCE and receipt_safety/README. Inspect actual repository branch/head/status and instructions before editing. Work only wheelbound-mode; preserve main and all unreviewed files. Follow the user's current branch restriction rather than the older generic pipeline suggestion to create feature branches. If concurrent work owns the files, stop or coordinate a genuinely disjoint task; never force-push.

Finish authorized I00 reporting or explicitly document which reporting evidence remains missing before claiming a reproducible baseline. Existing clean-build success is real;70 declared tests are not a verified execution count. Java11/compiler release11 and wrapper8.10 follow actual repository configuration. No arbitrary Java17/Gradle9 upgrade. No game client, login, live donation/trade attempt or Hub contact is needed for this slice.

The current mode does not exist in production. Existing WheelboundPlugin, normal-wheel renderers/filters and CustomWheels are compatibility surfaces, not a ready-made transactional challenge implementation. Start with new pure-domain types behind a disabled integration boundary. Package/class names below are suggestions to choose coherently with repository conventions. Reuse injected host Gson at the eventual adapter boundary; do not create a private Gson or global focus manager. Do not copy the Python oracle into runtime logic.

## Scope of the first slice

Implement account/run-separated state, revision-checked commands, invariant validation, frozen weighted selections, idempotent receipts and supported local recovery. Tests supply synthetic qualified evidence; no detector claims to establish game facts. There is no Fate shop, final economy tuning, Coffer settlement detector, trade restriction, additional Bossing Challenge, Grand win detector or production onboarding in this slice.

Unknown new-account identity/activation evidence must not fall back to an account display name or settings profile alone. Pure tests can use explicit fixture account/run identities. The pure Grand-entry predicate can be tested without a Coffer adapter: obligation NONE, free pause rules and no artificial level/time/spin minimum must be preserved.

## Bounded tasks and dependencies

Sizes indicate likely complexity, not elapsed guarantees. Each task ends with code reviewable in one coherent commit, relevant Java tests, preserved normal-wheel behavior, and a resume note. Split M tasks further if needed.

| Task | Depends on | Proposed modules | Size | Deliverable |
| --- | --- | --- | --- | --- |
| I01-C0 | Authorized coding; I00 evidence status | Versioned domain/receipt/storage contract | S | Resolve delegated API/storage details before implementation; retain gameplay gates |
| I01-C1 | C0 | Immutable run envelope, identities, obligation and receipt value types | S | State representations with explicit knownness and versioned fields |
| I01-C2 | C1 | Wheel invariant validator and probability model | S | Exact five ordinary/three activities/24 total validation and weight calculation |
| I01-C3 | C1/C0 | Revision-checked command reducer, ledger, receipt identities | M→split validation/replay | Preconditions, proposed mutation and duplicate/replay semantics without UI-side mutation |
| I01-C4 | C2/C3 | Injected selection provider and frozen assignment commitment | S | Weighted result durably committed before presentation; restart/retry preserves it |
| I01-C5 | C3/C0 | Supported serializer/store/recovery implementation | M→split codec/storage/faults | Actual persistence boundary, failed-write reconciliation, read-only recovery and migration |
| I01-C6 | C4/C5 | Account/session generation and pause/resume coordinator | S | Stale callbacks isolated; free pause preserves obligations; no paused/offline backfill |
| I01-C7 | C1–C6 | Java integration fixtures and evidence handoff | S | Actual domain/storage acceptance results and unchanged normal-wheel regressions |

C2 and C3 are independently reviewable after C1. C4 can test a fake persistence port before C5, but must not claim durability from it. C6 integration waits for actual C5 recovery behavior. C7 is a cross-module acceptance checkpoint, not a second implementation of production logic.

## I01-C0: settle implementation details within delegated authority

Produce a short versioned contract covering: stable command payload identity, reused ID with changed payload, command versus evidence namespaces, lost-ack response shape, supported storage operation ordering, failure/torn-tail classification, checkpoint rollover, single-writer ownership, and migration validation. Preserve already committed outcomes and corrupted originals. A hash is accidental-corruption detection, not anti-cheat authentication.

Choose implementation-neutral policies autonomously and document/test them. Do not ask the user to choose Java exception names or serialization layout. However, if a storage choice cannot satisfy the existing preservation/durability requirements on supported platforms, report the concrete limitation rather than changing gameplay or claiming atomicity. Do not silently translate R07's UNSPECIFIED_COMMAND_ID_COLLISION into an approved gameplay decision. Qualifying evidence supplied by tests is a premise, not a detector.

## I01-C1/C2: value types and wheel invariants

Keep run lifecycle separate from obligation stage; pausing does not clear the obligation. Track schema/revision, account/run identity, generation, frozen catalog/quote/outcome, signed integer FP and nonnegative integer spins. Use stable slice IDs; ordinary tier weights1/1.25/1.5/2/3, special weights derived separately. Do not use floating rounding to conceal impossible probabilities; use a representation that preserves the documented fractions, and specify boundary selection behavior.

Required examples: starting five activities accepted; four ordinary rejected even with specials; five ordinary representing only two activities rejected;24 total including specials accepted;25 rejected. Distinct stored instances do not count as active. Ordinary enhancement and special weight rules must remain separate. Null/unknown pool evidence does not mean an empty pool. WB-D111 resolves verified-empty Questing with automatic Master retry and retained slice; WB-D120 adds no cost for empty retry and one spin for eventual valid Fate; no broader reroll policy. Preserve permanently purchased boss membership; do not adopt normal-wheel recommended-stat filters as mode eligibility.

## I01-C3/C4: commands and durable selections

Validate identity, lifecycle, expected revision, affordability and wheel invariants before proposing a mutation. Same known command replays its committed state/financial result; rejected stale identity or revision creates no new receipt. Resolve changed-payload collisions using C0's documented rule. Discretionary spending cannot create FP debt; quoted penalties may. Do not freeze final prices from synthetic fixtures.

A selection draws once from a frozen eligible input and quote/catalog version. Persist the full outcome, not merely a seed whose future interpretation could change. Publication/animation waits for the selected durable boundary. A failure before commit does not invent a debit; a committed outcome with lost acknowledgement cannot reroll. The acceptance fixtures' mock ordinary draw allocates one spin; special fixture allocations settle once without an additional derived debit. Preserve the authoritative allocation timing; do not separately charge again at completion or Reject.

No assignment generator or real reward formula is required here. Synthetic assignment descriptors make the commitment testable without resolving Q9, real catalogs or Bossing target scaling. Do not produce actual live Fate offers from placeholder inputs.

## I01-C5: actual storage and recovery

Port logical oracle cases to the actual chosen Java storage adapter. Test create/load/restore, postcommit lost acknowledgement, malformed original, unknown future schema, migration success/failure, snapshot/journal revision mismatch, checkpoint rollover and single-writer conflict. Inject disk-full/read-only/permission/write failure through the actual adapter boundary; identify which tests emulate errors and which use a real temporary filesystem.

Test recovery at each storage operation the implementation actually performs. Do not infer filesystem crash durability from an in-memory append or thread synchronization. Record supported-platform assumptions and platform checks still needed. A malformed/unclassified tail preserves its raw bytes and disables writes until the selected safe reconciliation path; never quietly delete or truncate to make tests pass. An ambiguous donation is not a reason to ask for another donation, even though the live adapter is outside this slice.

## I01-C6: session and lifecycle

Accept explicit account/run/generation tokens supplied by the adapter port. Account switch/logout clears stale callback delivery, not the committed run. Serialization must not merge accounts by display name. Pause is immediate, free, label-free and retains pending selection/obligation; resume establishes fresh observation baselines. Inactive observations receive no retroactive credit. Genuine earlier active evidence remains preserved: WB-D114 expressly forbids payout/claim processing during pause; retain earlier active evidence.

Uninitialized/unknown XP, bank, CA or identity values do not become zero baselines. A GUI preview is not required for acceptance. If a local preview is later added, it reads immutable snapshots and cannot directly change FP or invoke OSRS actions.

## I01-C7: acceptance evidence and exit

Use [fixture disposition](FIXTURE_DISPOSITION.json) to identify the existing oracle cases relevant to this slice and those retained for later systems. Read each referenced fixture's authored expected values; names in this packet are a map, not a replacement. A passed Python fixture is not a passed Java test. The final report names actual Java tests, command, commit/toolchain, executed/skipped/failed counts from actual reports, store fault coverage and any remaining limitations. Update the actual test inventory after intentional new tests; do not misapply the archived70-method list.

Retain existing normal-wheel regressions. No unexplained production behavior change, account login, menu interception, private Gson, focus hook, credential handling or external telemetry. Runtime enabled remains false until a separately authorized integration milestone. Report exactly which C tasks are done, deferred or blocked; do not mark I01 done because only the weighted picker compiles.

## Explicit deferred behavior

- Q6: WB-D111 permits known-empty Questing automatic retry with retained slice; WB-D120 no extra retry cost; other activity recovery remains unverified.
- Q10 resolved: qualifying active bounty automatically pays independently and survives failure/Reject; no deletion, escrow or clawback.
- PAUSE-CLAIM resolved: no payout while paused. CA-MIXED still unresolved; preserve evidence; no pause penalty/backfill.
- Q9 additional Bossing Challenge: excluded until resolved; confirmed L/S/G and random boss/no reroll remain.
- Audit reward ordering: no invented withheld completion reward or per-tick charge.
- Coffer: no guessed receipt, full-Coffer escape, market-price proof or repeated donation request.
- Trade/shop/GE: no cancellation/removal/notice adapter shipped from this slice; exact policy and route gates independent.

These gates block affected paths, not the entire pure-domain milestone. An implementation safety status is not a new player-facing punishment. Material gameplay changes go through DECISIONS and relevant specs; routine architecture choices need a versioned implementation contract and validation.

Current authority WB-D116/117: Bounties Shop is item goal→FP. Master CA Grand is obtaining actual Ghommal's hilt5, with no active-only prerequisite CA tally/anti-cheese audit. Historical CA-MIXED is not a gate on this Grand objective. Preserve unrelated separate reward evidence; do not silently remove those historical systems or accept unsupported item detection. Read current DECISIONS over old source pins.
