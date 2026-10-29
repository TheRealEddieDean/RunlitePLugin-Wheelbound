## Current local continuation - purchase receipt JSON prerequisite, 2026-10-29

IDLE after bounded WB-WHL-04/05, WB-ECON-08 and WB-FND-11 prerequisite (XS/S).
This continuation supersedes older next-task prose. Reconciled current domain and
persistence code against recovered receipt checkpoint and backlog. Typed purchase
receipt/proposal exist; RunReceipt still supports lifecycle/draw/standalone debit only;
RunCodec remains schema 6 with inventory. No durable purchase command exists.
Recorded merged work was not independently verified; no Git operations performed.
FND-01 remains DONE; FND-10/12 remain PARTIAL; FND-11, ECON-01/08, TREE-02 and
WHL-02/03/04/05/06/07/11/12 remain PARTIAL_UNVERIFIED. Other TODO/partial/blocked
rows retain status. Evidence and numeric-price holds remain. I01d was not selected.

Added package-private PurchaseReceiptJson, a strict serializer/parser for every frozen
purchase request field and original debit response. It validates expected account/run,
exact fields, numeric types and domain invariants through constructors. Focused JUnit
coverage authored for first/duplicate round trips, original response, missing/unknown
fields, foreign identity, numeric strings and invalid duplicate ordinal. This independently
reviewable prerequisite does not change the save schema or claim journal integration,
atomicity, authorization, inventory/history validation or durable replay. WB-D105/106/118
remain authority; no prices adopted. Master Wheel unchanged; gameplay unwired/disabled.

Validation attempted: gradlew.bat --gradle-user-home .gradle-local test --tests
com.wheelbound.persistence.PurchaseReceiptJsonTest --tests
com.wheelbound.persistence.RunCodecTest --tests com.wheelbound.WheelboundBehaviorTest
--offline --no-daemon --console=plain. Failed before compilation: missing Gradle 8.10
wrapper distribution attempted download, denied SocketException. No compilation/JUnit
or legacy regression pass claimed. No external paths, credentials, secrets, workflows
or Git operations accessed. Supervisor must run these suites with available tooling.

Next bounded unit: integrate PurchaseReceiptJson into a schema-7 SLICE_PURCHASE journal
operation; validate identity, command/revision, ledger, lifetime history and historical
instance-ID consistency, with schema-6 compatibility and tampering tests. Then implement
the atomic authorized aggregate purchase command and failed-write/lost-ack/restart tests.
Supervisor handles commits, PRs, CI and merges.

## Recovered checkpoint - typed purchase receipt, 2026-10-29

Recovered the capacity-interrupted increment on agent/autonomous-20261029-172520.
Original base: 28fbc6b1c0627184c19984cfe019f9bafb8cc7e1 (merged PR #35).
SlicePurchaseReceipt freezes run/account identity, command ID, original revision,
instance ID, activity, quote key/version/amount, purchase kind and duplicate ordinal,
and retains the original debit balance/revision response after later proposals.
Its constructor rejects quote/debit disagreement. This is an in-memory value only;
it does not establish price authority, persistence acknowledgement or durable replay.
Gameplay remains unwired/default-disabled; legacy Master Wheel code is unchanged.

Validation: full Gradle 8.10 test suite passed, 196 tests, zero failures/errors,
using a writable copied dependency cache, --offline and a separate build directory.
The sandbox could not use the default cache or clean older build files; no tests
were disabled and no build/source settings were changed to bypass validation.
No PR has been opened or merged for this recovered checkpoint during debugging.
Supervisor restart resumes testing/publication of this same reviewed increment,
requiring successful CI and original base/head verification before any later merge.

Next bounded unit: schema/journal integration of the typed purchase receipt, then
an atomic purchase command with failed-write/lost-acknowledgement/restart tests.
Related gameplay tasks remain PARTIAL_UNVERIFIED; do not claim durable purchases.

## Current local continuation - combined purchase proposal, 2026-10-29

IDLE after bounded WB-WHL-04/05 and WB-ECON-08 dependency increment (S).
This supersedes older next-task prose. Reconciled domain, persistence, focused tests
and recorded I01a/b/c receipts without Git or remote merge verification. Existing
SliceInventory already has enhancement, new purchase, storage/reactivation, stored
replacement, purchase-with-destruction/storage and destruction proposals. Schema 6
already persists inventory; standalone durable debits exist. These are partial systems,
not complete gameplay. FND-01 remains DONE; FND-10/12 remain PARTIAL; FND-11,
ECON-01/08, TREE-02 and WHL-02/03/04/05/06/07/11/12 remain PARTIAL_UNVERIFIED.
Other TODO and decision/evidence BLOCKED rows retain status. I01d was not selected.

SlicePurchaseProposal now combines a frozen activity/kind/duplicate-tier quote input,
new base instance, lifetime acquisition history, ledger debit and one lifecycle revision.
It rejects quote-tier mismatch, stale/foreign/inactive context, obligations, insufficient
funds, owned UUID collisions, invalid capacity and revision overflow without changing
the input snapshots. Authority: WB-D105/106/118 and STATE_CONTRACT. Quote construction
does not authorize prices. Only available-slot purchases are supported by this proposal.
This is not a typed durable purchase receipt or replay service. Historical UUID freshness,
complete pending-draw/offer/edit-boundary checks, catalog/quote authority and cross-operation
command-ID validation remain responsibilities of the future aggregate command. Never
persist the returned debit separately from inventory/history and a purchase receipt.
No prices adopted; Master Wheel unchanged; gameplay remains unwired/default-disabled.

Validation: javac --release 11 compiled all production domain classes and executable
SlicePurchaseProposalChecks; checks passed for first/duplicate/reacquisition, exact funds,
immutability and rejection cases. Two focused JUnit methods added using the same checks.
Gradle focused purchase/inventory and legacy WheelboundBehaviorTest attempt failed before
compilation: missing Gradle 8.10 download denied (SocketException). No JUnit or legacy
pass claimed. Supervisor must run these suites when the build distribution is available.

Next bounded unit: typed durable purchase receipt binding identity, original revision,
quote/activity/tier/amount and fresh instance ID, plus schema/journal validation. Then add
one atomic aggregate purchase command and failed-write/lost-acknowledgement/restart tests.
Preserve standalone debit/lifecycle/draw compatibility and original replay responses.
No external paths, credentials, secrets, workflows or Git accessed. Supervisor handles
commits, PRs, CI and merges.

## Current local continuation - inventory persistence, 2026-10-29

IDLE after bounded WB-FND-10/12 and WB-WHL-03 inventory persistence increment (S).
This supersedes earlier next-task prose. Reconciled production domain, persistence,
command and focused-test inventory against the backlog and recorded I01a/b/c receipts.
Recorded merges were not remotely verified; no Git operations. FND-01 remains DONE.
FND-10/12 remain PARTIAL; FND-11/ECON-08 remain PARTIAL_UNVERIFIED. TREE-02,
WHL-02/03/04/05/06/07/11/12 and ECON-01 remain PARTIAL_UNVERIFIED. All other
unstarted TODO and decision/evidence BLOCKED rows retain status. No complete gameplay
or paid wheel transaction is claimed. I01d was not selected or changed.

RunDocument now carries optional SliceInventory. RunCodec schema 6 persists ordered
active/stored UUIDs, ordinary enhancement tiers, explicit special weights, Satchel tier,
retained unlocks and independent lifetime acquisition history (including destroyed copies
and unlock-only activities). InventoryJson restores through validated domain constructors,
rejects duplicate unlock/history entries and unknown fields, and derives ordinary weights
from tiers. Schemas 1-5 retain unavailable inventory, never a fabricated starting wheel
or reset purchase history. Current schema requires explicit inventory or null. Lifecycle,
draw and standalone debit commands preserve inventory through revisioned writes.
Authority: WB-D105/106/118/121 and STATE_CONTRACT/PERSISTENCE. No prices, grants,
catalog admission, transaction provenance or authorization are invented. This remains
an incomplete aggregate; live gameplay is unwired/default-disabled. Master Wheel source
is unchanged. Historical acquired-state restoration still requires trusted evidence.

Five focused JUnit methods authored for complete inventory round trip, schemas 1-5 and
explicit null/missing-field compatibility, malformed inventory and duplicate rejection,
failed replacement/pending blocking, and mixed-command/lost-acknowledgement replay after
reopen. Updated schema downgrade fixtures to omit the new inventory field. Java 11
compilation passed for all domain sources, RunDocument/RunReceipt and dependency-free
InventoryDocumentChecks/DebitJournalChecks; both checks passed. Gradle focused inventory,
codec, debit journal, durable debit and legacy WheelboundBehaviorTest attempt failed
before compilation: missing Gradle 8.10 distribution download denied (SocketException).
No JUnit/legacy pass or codec/service compilation is claimed. Supervisor must execute
the new and affected persistence suites, including lifecycle and durable draws.

Next bounded gameplay unit: add a typed inventory-purchase receipt and atomic aggregate
command coupling an explicitly authorized reviewed debit, new instance and lifetime
history mutation. Persist all effects in one write; reject cross-operation command-ID
reuse, retain original replay response, and test failure/lost acknowledgement/restart.
Do not call the standalone debit service separately to pay for inventory changes.
Prices/catalog authority and legal edit-boundary validation remain required before live
use; WHL-04/05/06 remain unfinished. No external paths, credentials, secrets, workflows
or Git accessed. Supervisor handles commits, PRs, CI and merges.

## Current local continuation - durable debit commands, 2026-10-29

IDLE after bounded WB-FND-11 / WB-ECON-08 durable debit command increment (S).
This entry supersedes older next-task text. Reconciled domain, schema-5 persistence,
command services and focused tests against the backlog and historical merge receipts.
No Git operations or remote merge verification. FND-01 remains DONE; FND-10/12 remain
PARTIAL. FND-11 and ECON-08 remain PARTIAL_UNVERIFIED, now including the standalone
durable debit service. TREE-02, WHL-02/03/04/05/06/07/11/12 and ECON-01 remain
PARTIAL_UNVERIFIED. Other TODO and decision/evidence BLOCKED rows retain status.
No completed gameplay or integrated paid transaction is claimed. I01d was not selected.

DurableFatePointDebits uses AtomicRunStore to write lifecycle, ledger and typed receipt
in one revision-checked replacement before returning success. Exact replay restores the
original response after later mixed operations, including while paused. Changed quote,
amount/revision, foreign identity and cross-operation command-ID reuse fail closed.
Missing legacy FP is unavailable, never inferred zero. Debit proposals preserve pending
obligations. Failed writes retain the store's pending/reconciliation behavior; lost
acknowledgements can replay the committed receipt after reopen. Authority: WB-D105 and
STATE_CONTRACT. Quote references are not authorization; no candidate prices adopted.
This unwired primitive must not be called separately to pay for inventory changes:
product mutations still require one complete aggregate transaction with the debit.
Master Wheel source unchanged; incomplete gameplay remains unwired/default-disabled.

Five JUnit methods authored for restart/mixed-operation replay and original balance,
lost acknowledgements, failed replacement/pending-file blocking, request/identity and
lifecycle/draw command-ID collisions, stale/inactive/invalid/insufficient requests and
missing legacy FP. Gradle focused DurableFatePointDebitsTest/DebitJournalTest/
DurableWheelDrawsTest and legacy WheelboundBehaviorTest attempt failed before compilation:
missing Gradle 8.10 distribution download denied (SocketException Permission denied).
No JUnit or legacy suite ran; new service/codec compilation is NOT claimed. All domain
production sources plus RunDocument/RunReceipt and existing DebitJournalChecks compiled
with javac --release 11; DebitJournalChecks passed. Supervisor must run the new tests.

Next required bounded gameplay dependency: persist SliceInventory in the versioned run
aggregate/codec, preserve it through all existing commands and test schema compatibility.
Then atomically couple reviewed debit, new-instance/lifetime-history and product changes,
with failed-write/lost-acknowledgement/restart fixtures. Paid WHL-04/05/06 remains unfinished.
No external paths, credentials, secrets, workflow access or Git operations were used.
Supervisor handles commits, PRs, CI and merges.

## Current local continuation - typed debit journal, 2026-10-29

IDLE after bounded WB-FND-11 / WB-ECON-08 typed-journal prerequisite increment (S).
Reconciled production domain/persistence and test inventory with recorded I01a/b/c
merge receipts; no Git or remote verification. FND-01 remains DONE. FND-10/12 remain
PARTIAL; TREE-02, WHL-02/03/04/05/06/07/11/12 and ECON-01/08 remain
PARTIAL_UNVERIFIED. FND-11 is now PARTIAL_UNVERIFIED for typed durable journal format
only. All other unstarted TODO and decision/evidence BLOCKED rows retain status.
This entry supersedes older next-task/status prose. I01d was not selected.

RunReceipt now carries FP_DEBIT with frozen quote key/version, amount and original
balance, validating command identity and revision binding. RunDocument accepts mixed
lifecycle/draw/debit revision chains, rejects cross-operation duplicate command IDs,
preserves obligations and validates debit receipts against the ledger suffix after a
trusted legacy prefix. Current lifecycle/draw operations cannot change FP. Future
reward/penalty commands require explicit journal validation extensions. LifecycleCommands
rejects operation-specific commands rather than treating them as resume.
RunCodec schema 5 writes typed debit payloads; schemas 1-4 remain readable, with older
schemas refusing debit payloads. Legacy missing FP remains unavailable, never zero.
Authority: WB-D105 and STATE_CONTRACT. No quote authorization or prices adopted.

All production domain classes plus RunReceipt/RunDocument and DebitJournalChecks
compiled with javac --release 11. Dependency-free checks passed mixed pause/resume
and two-debit history, original response retention, absent ledger, amount/balance
mismatches, foreign receipt identity, cross-operation ID collision, inactive chain,
missing debit payload and unjournaled trailing ledger mutation rejection.
Three JUnit methods authored for these checks, codec round trip, schema-4 compatibility
and old-schema debit refusal. Gradle focused DebitJournalTest/RunCodecTest/
LifecycleCommandsTest/DurableWheelDrawsTest and legacy WheelboundBehaviorTest attempt
failed before compilation: Gradle 8.10 distribution missing, download SocketException
Permission denied. Codec/service compilation and JUnit/legacy passes are NOT claimed;
supervisor must run them. Master Wheel source unchanged; gameplay stays unwired/disabled.

Next bounded unit: durable debit command service using AtomicRunStore, exact replay from
typed journal, changed-payload/cross-operation collision checks, failed writes, lost
acknowledgements and restart tests. Then persist inventory and couple reviewed debit
and product changes atomically. Current format support is not a paid transaction,
complete aggregate, purchase/refund prevention or integrated spin/FP implementation.
No external paths, credentials, secrets, workflows or Git accessed. Supervisor handles
commits, PRs, CI and merges.

## Current local continuation - purchase with storage replacement, 2026-10-29

IDLE after bounded WB-WHL-06 purchase/storage proposal increment (S).
Reconciled SliceInventory proposals, focused tests, schema-4 RunDocument/RunCodec,
RunReceipt and in-memory FatePointDebits against backlog and recorded I01a/b/c receipts.
Recorded merges were not remotely verified; no Git operations performed. FND-01 remains
DONE; FND-10/12 remain PARTIAL. TREE-02, WHL-02/03/04/05/06/07/11/12 and ECON-01/08
remain PARTIAL_UNVERIFIED. FND-11 remains TODO for integrated durable transactions.
Other unstarted TODO and decision/evidence BLOCKED rows retain their status. This entry
supersedes older next-task prose. I01d was not selected. Chose the independently testable
remaining WHL-06 product route while the larger durable-command dependency stays open.

SliceInventory.proposePurchaseReplacingStored proposes a fresh base ordinary instance
in the explicitly selected active position and appends the exact outgoing ordinary
instance to Satchel, including enhanced Questing. Final five/three/24 and Satchel limits
are validated without invalid intermediate removal/insertion. Owned UUID reuse (including
outgoing), specials, wrong locations, locked activities, invalid diversity and history
overflow fail closed. Only incoming activity acquisition history advances in the proposal;
unlocks, unaffected instances and other history survive. Authority: WB-D106/118/121 and
WHEELS atomic final-state edits. No storage fallback, prices, payment, authorization,
durable receipts, historical destroyed-ID uniqueness or live wiring is supplied. Both
reviewed storage and purchase costs remain required at the future aggregate boundary.
Master Wheel source unchanged; incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods authored for full wheel/five ordinary, enhanced Questing,
first/duplicate history, ordering/identity, full Satchel, locations/collisions, specials,
diversity, malformed/null inputs, overflow and immutable failure. All production domain
sources compiled with javac --release 11. Dependency-free StorageReplacementSmoke passed
full-wheel Questing storage, first/duplicate history, UUID collisions, diversity/capacity,
special rejection and immutability. Focused SliceInventoryTest and legacy
WheelboundBehaviorTest Gradle attempt failed before compilation: missing Gradle 8.10
download denied (SocketException). No JUnit or legacy regression pass claimed.
Supervisor must run these suites and handles commits, PRs, CI and merges.

Next required bounded dependency: typed FP debit receipts in the unified revision journal,
explicit schema compatibility, mixed-operation restoration and cross-operation command-ID
collision rejection. Then persist inventory and atomically couple reviewed debit/product
commands, testing failed writes, lost acknowledgements and restarts. WHL-06 remains
incomplete for paid durable replacement. No external paths, credentials, secrets,
workflow access or Git operations were used.

## Current local continuation - destructive purchase replacement, 2026-10-29

IDLE after bounded WB-WHL-06 new-instance/destruction proposal increment (S).
Read required automation records and WB-D105/106/118/121 supersession entries plus
WHEELS. Reconciled domain, persistence, focused tests and recorded I01a/b/c merge
receipts; no Git operations or remote merge verification. FND-01 remains DONE;
FND-10/12 remain PARTIAL. TREE-02, WHL-02/03/04/05/06/07/11/12 and ECON-01/08
remain PARTIAL_UNVERIFIED. FND-11 remains TODO for integrated durable spin/FP
transactions. Other unstarted TODO and decision/evidence BLOCKED rows stand.
Schema 4 optional FP snapshots and in-memory debit receipt restoration do not supply
typed debit entries in the unified durable journal, inventory persistence or paid edits.
This entry supersedes older next-task/status prose; I01d was not selected.

SliceInventory.proposePurchaseReplacingDestroyed replaces an explicitly selected
active destroyable ordinary instance with a fresh base instance in the same position.
Final five/three/24 validation permits full-wheel and five-ordinary replacements
without publishing an invalid intermediate wheel. Full Satchel does not block destruction.
New UUID cannot equal any currently owned ID, including the outgoing one. Questing,
specials, wrong locations, locked activities, invalid diversity and history overflow
fail closed. Proposal retains unlocks, storage, unaffected instances and lifetime
history; only the incoming activity advances its first/duplicate classification.
No refund, price adoption, edit authorization, debit, durable receipt, replay protection
or live plugin/UI wiring is supplied. Historical destroyed UUID uniqueness still needs
aggregate receipt/history enforcement; this snapshot only knows currently owned IDs.
Master Wheel production source unchanged; incomplete gameplay remains unwired/disabled.

Two focused JUnit methods authored for full wheel/full Satchel, minimum-wheel acceptance,
first acquisition/reacquisition, base tier/order/history retention, protected slices,
identity/location collisions, invalid activity/diversity, nulls and overflow. All domain
production sources compiled with javac --release 11. Dependency-free
DestructiveReplacementSmoke passed full/minimum capacity, history/reacquisition,
protected/special slices, ID/location collision and immutable failure checks. Its initial
compile encountered a PowerShell UTF-8 BOM; saved without BOM and reran successfully.
Gradle SliceInventoryTest and WheelboundBehaviorTest failed before compilation because
the missing Gradle 8.10 distribution download returned SocketException Permission denied.
No JUnit or legacy regression pass claimed; supervisor must run those suites.

Next required bounded dependency: typed FP debit receipts in the unified revision journal,
explicit schema compatibility, mixed-operation restoration and cross-operation command-ID
collision rejection. Then durable inventory and atomic reviewed debit/product commands
with failed-write/lost-acknowledgement/restart tests. WHL-06 remains incomplete for paid
replacement and new-purchase-with-storage routing. No external paths, credentials,
secrets, workflows or Git accessed; supervisor handles commits, PRs, CI and merges.
## Current local continuation - stored replacement proposal, 2026-10-29

IDLE after bounded WB-WHL-06 product-proposal increment (S).
Read protocol, session, queue, pending decisions, checkpoint and backlog; reconciled
ActiveWheel/SliceInventory, tests and recorded I01a/b/c merged receipts. No Git or
remote verification performed. FND-01 remains DONE; existing partial/unverified,
unstarted and evidence/decision-blocked statuses stand except WHL-06 is now
PARTIAL_UNVERIFIED. This entry supersedes historical WHL-06 TODO statements.
I01d was not selected. No complete paid gameplay implementation is claimed.

SliceInventory.proposeStoredReplacement exchanges explicitly selected active ordinary
and stored instances in one immutable proposal, validating final five/three/24 and
Satchel capacity. Full wheel/full Satchel exchange preserves UUIDs, enhancements,
other instances, unlocks and lifetime history. Questing moves without destruction;
Taint/Sacrifice, foreign/wrong-location IDs and invalid final diversity fail closed.
Authority: WB-D105/106/118/121 and WHEELS final-state atomic edit contract.
This is compound storage/reactivation only, with both reviewed movement costs still
required. No free swap, new-instance replacement, special insertion, price adoption,
authorization, debit, persistence, receipt or UI/live wiring was added. Master Wheel
source unchanged; incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods authored for full capacities, exact instance/order/history
preservation, reverse exchange, minimum wheel acceptance, diversity rejection,
specials, locations, nulls and immutable failure. javac --release 11 compiled all
production domain sources. Dependency-free ReplacementSmoke passed full-capacity
Questing exchange/reversal, minimum-wheel diversity rejection, special rejection and
immutability/history checks. Initial smoke fixture incorrectly retained Questing as
a fourth activity for diversity rejection; corrected to a five-ordinary/three-activity
fixture and reran successfully. Gradle focused inventory and legacy behavior attempt
failed before compilation: missing Gradle 8.10 download denied (SocketException).
No JUnit or legacy regression pass claimed; supervisor must execute those suites.

Next required dependency for paid gameplay: typed debit receipts in the unified revision
journal, explicit schema compatibility, mixed-operation restoration and cross-operation
command-ID collision rejection; then durable inventory and atomic reviewed debit/product
commands with failed-write/lost-acknowledgement/restart tests. WHL-06 remains incomplete
for authorized durable replacement and other replacement routes. No external paths,
credentials, secrets, workflows or Git accessed. Supervisor handles publication and CI.
## Current local continuation - raw wheel odds, 2026-10-29

IDLE after bounded WB-WHL-07 raw-odds snapshot increment (S).
Reconciled ActiveWheel, SliceInventory, FP ledger/debits, persistence and focused test
inventory against backlog and recorded I01a/b/c merged-work receipts. No Git operations
or remote verification. FND-01 remains DONE; FND-10/12 remain PARTIAL. Existing
TREE-02, WHL-02/03/04/05/11/12 and ECON-01/08 PARTIAL_UNVERIFIED statuses stand.
WHL-07 is now PARTIAL_UNVERIFIED for the raw odds model only; display and integrated
builder previews remain unfinished. FND-11, WHL-06 and other TODO rows remain unstarted;
evidence/decision BLOCKED rows remain blocked. I01d was not selected.

WheelOdds supplies immutable per-UUID and summed ordinary-activity raw probabilities
as exact quarter-unit numerator/denominator pairs, plus a floating-point presentation
conversion. All active special weights remain in denominators; unoccupied slots add
nothing. Snapshot ordering is retained and reorder leaves probabilities unchanged.
Comparing original/proposed validated snapshots supports odds previews without editing
or authorizing either snapshot. Authority: WB-D008/010/096 and WHEELS. These are raw
draw odds, not accepted-assignment odds after empty-pool retries. No eligibility filter,
retry normalization, pricing, purchase authority, UI or live plugin wiring was added.
Master Wheel production source unchanged; incomplete gameplay remains unwired/disabled.

Two focused JUnit methods authored for exact aggregation, specials, Questing inclusion,
immutable output, reorder invariance and independent enhancement preview denominators.
javac --release 11 compiled all production domain sources. Dependency-free OddsSmoke
passed aggregation, special denominators, reorder invariance and immutability. Gradle
WheelOddsTest/ActiveWheelTest/WheelboundBehaviorTest failed before compilation because
missing Gradle 8.10 distribution download returned SocketException Permission denied.
No JUnit or legacy regression pass claimed; supervisor must execute those suites.

Next required dependency for paid gameplay: typed debit receipts in the unified revision
journal, explicit schema compatibility, mixed-operation restoration and cross-operation
command-ID collision rejection. Then durable inventory and atomic reviewed debit/product
mutations with failed-write/lost-acknowledgement/restart coverage. WHL-07 still needs
presentation/integration acceptance. No external paths, credentials, secrets, workflows
or Git accessed. Supervisor handles commits, PRs, CI and merges. This entry supersedes
historical next-task and WHL-07 TODO statements below.

## Current local continuation - sequential enhancement proposals, 2026-10-29

IDLE after bounded WB-WHL-12 increment (S), authority WB-D010/096/106 and WHEELS.
Reconciled source/test inventory and recorded I01a/b/c merge receipts; no Git operations
or remote verification. FND-01 remains DONE. FND-10/12 remain PARTIAL; TREE-02,
WHL-02/03/04/05/11 and ECON-01/08 remain PARTIAL_UNVERIFIED. WHL-12 is now
PARTIAL_UNVERIFIED for active ordinary instance enhancement proposals only. FND-11,
WHL-06 and other unstarted rows remain TODO; evidence/decision BLOCKED rows stand.
Schema 4 optional FP snapshots exist, but typed debit journal receipts, durable inventory,
complete gameplay aggregate and authorized paid edits remain unfinished. Historical
next-task statements below are superseded by this entry. I01d was not selected.

SliceInventory.proposeEnhancement requires an explicit next destination tier for an
active ordinary UUID. It preserves activity, identity, order, other instances, storage,
unlocks, Satchel tier and lifetime history, deriving exact quarter-unit weight from the
approved ladder. Skipped/same/lower/max-tier, special, stored, unknown and null selections
fail without mutating the original. Questing may be enhanced as an ordinary instance.
No stored enhancement route, mastery entitlement, quote/price, edit authorization, debit,
replay protection or live plugin/UI wiring is implemented. Trusted aggregate callers must
validate mastery/legal edit/identity/revision and reviewed destination-tier quote, then
atomically persist debit, full inventory and receipt. No unresolved balance adopted.
Master Wheel source unchanged; incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods authored for all four transitions, independent same-activity
instances, identity/history/weight preservation, Questing, stale/skipped/max/invalid targets
and immutable failure. javac --release 11 compiled all production domain sources.
Dependency-free local EnhancementSmoke passed sequential/stale/skipped/unknown checks.
Gradle SliceInventoryTest/ActiveWheelTest/WheelboundBehaviorTest failed before compilation:
missing Gradle 8.10 distribution could not download (SocketException Permission denied).
No JUnit or legacy regression pass claimed; supervisor must execute those suites.

Next required bounded dependency for paid gameplay: typed debit receipts in the unified
revision journal, explicit schema compatibility, mixed-operation restoration and command-ID
collision rejection. Then durable inventory and atomic reviewed debit/product mutations,
with failed-write/lost-acknowledgement/restart tests. Mastery catalog/gates and full paid
WHL-12 integration remain unfinished. No external paths, secrets, credentials, workflows
or Git accessed. Supervisor handles commits, PRs, CI and merges.

## Current local continuation - new-instance acquisition proposals, 2026-10-29

IDLE after bounded WB-WHL-04/05 gameplay increment (S). Reconciled domain inventory,
FP ledger/debits, RunDocument/RunReceipt and existing tests with recorded I01a/b/c
merged-work receipts. No Git operations or remote verification. FND-01 stays DONE;
FND-10/12 and existing PARTIAL rows stay partial. TREE-02, WHL-02/03/05/11 and
ECON-01/08 stay PARTIAL_UNVERIFIED. WB-WHL-04 is now PARTIAL_UNVERIFIED for an
in-memory product proposal only; paid transactions remain unfinished. FND-11 and
WHL-06 stay TODO; remaining TODO and evidence/decision BLOCKED rows stand. Existing
storage/destruction proposals and schema-4 optional FP snapshots are present; typed
debit journal receipts, durable inventory and complete gameplay aggregate are absent.
This supersedes older claims of no acquisition transition. I01d was not selected.

SliceInventory.proposeNewPurchase proposes a fresh base ordinary active instance and
its lifetime history together. First acquisition establishes baseline at zero paid
duplicates; a starting-grant baseline or any retained history advances the next paid
duplicate ordinal, including after last-copy destruction. UUID collisions across active
and stored ownership, locked/malformed activities, overflow and full active capacity
fail without mutating the original. Other instances, modifiers, unlocks, Satchel tier,
storage and unrelated history survive. Authority: WB-D105/106/118/121, WHEELS and
STATE_CONTRACT. No prices, new storage-purchase route or unresolved rules adopted.
Repeated proposals are not replay protection. A trusted aggregate command must validate
identity/revision/legal edit boundary and reviewed quote, then atomically persist debit,
complete inventory/history and receipt before success. No durable/authorized purchase,
full-wheel replacement or plugin/UI wiring claimed. Master Wheel source unchanged;
incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods authored for first/starting/duplicate/reacquisition cases,
base weight, retained instances/history, collision/capacity/invalid-input/overflow and
immutable failure. javac --release 11 compiled all production domain sources. Local
dependency-free purchase smoke passed (first acquisition, destruction/reacquisition,
identity collision and full capacity). Gradle SliceInventoryTest/ActiveWheelTest and
WheelboundBehaviorTest attempt failed before compilation: missing Gradle 8.10 download
returned SocketException Permission denied. No JUnit or legacy regression pass claimed.
Supervisor must execute those suites.

Next bounded required dependency: typed debit receipts in the unified revision journal,
explicit schema compatibility, mixed-operation restoration and cross-operation command
ID collision rejection. Then persist inventory and couple approved debit with complete
new-instance/history mutation; test failed writes, lost acknowledgements and restarts.
Reviewed quote payloads remain required. No external paths, credentials, secrets,
workflows or Git accessed. Supervisor handles commits, PRs, CI and merges.

## Current local continuation - Satchel movement proposals, 2026-10-29

IDLE after bounded WB-WHL-03/11 gameplay increment (S). Reconciled production domain,
persistence, focused tests and recorded I01a/b/c merge evidence. No Git operations or
remote verification. FND-01 remains DONE; FND-10/12 and other existing PARTIAL rows
remain partial. TREE-02, WHL-02/03/05/11 and ECON-01/08 remain PARTIAL_UNVERIFIED.
FND-11 and paid WHL-04/06 remain TODO; evidence/decision BLOCKED rows stay blocked.
Schema 4 already persists optional FP snapshots; RunReceipt still lacks typed debits.
This supersedes older claims that movement proposals do not exist. I01d not selected.

SliceInventory.proposeStorage/proposeReactivation now produce validated immutable
same-instance movement proposals. Ordinary Questing may enter normal capacity-limited
storage and return with UUID, enhancement, weight, unlocks and lifetime history intact.
Wrong-location/unknown IDs, special storage, full Satchel/wheel and invalid final five
ordinary/three activity configurations fail without changing the original inventory.
Survivor order is preserved; destination appends the moved instance. Authority: WB-D106,
118, 121 and WHEELS/STATE_CONTRACT. No new gameplay price or unresolved policy adopted.
These proposals do not authorize free movement: a future aggregate command must enforce
identity, revision, legal edit boundary, reviewed quote and atomic debit/full-state/receipt
persistence before success. No durable movement, purchase, full-wheel replacement,
forced-special displacement or live plugin/UI wiring claimed. Master Wheel source
unchanged; incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods added for Questing roundtrip identity/history preservation,
wrong-location/null/unknown IDs, special restrictions, capacities, count/diversity and
immutable failures. javac --release 11 compiled all production domain sources. A local
dependency-free executable smoke passed for roundtrip and rejection paths. Gradle
SliceInventoryTest/ActiveWheelTest/WheelboundBehaviorTest attempt failed before compilation:
missing Gradle 8.10 distribution download denied (SocketException). No JUnit/CI or legacy
regression pass claimed. Supervisor must execute those suites.

Next required bounded dependency: typed debit receipts in the unified revision journal,
with explicit schema compatibility, mixed-operation restoration/validation and command-ID
collision rejection; then atomic debit plus complete product mutation and failed-write,
lost-acknowledgement/restart tests. Preserve optional legacy FP absence. Durable inventory
serialization/commands are also unfinished. Reviewed quote payloads remain required.
No external paths, credentials, secrets, workflows or Git accessed. Supervisor handles
commits, PRs, CI and merges.

## Current local continuation - ordinary destruction proposals, 2026-10-29

IDLE after bounded WB-WHL-03/11 gameplay increment (S). Current user instruction
supersedes historical Git/publication requirements; no Git operations or remote merge
verification performed. Reconciled domain inventory, persistence/receipt sources and
focused tests with recorded I01a/b/c evidence: FND-01 remains DONE; FND-10/12 remain
PARTIAL; TREE-02, WHL-02/03/05 and ECON-01/08 remain PARTIAL_UNVERIFIED. WHL-11 is
now PARTIAL_UNVERIFIED for destruction protection only. FND-11, paid WHL-04/06,
Satchel movement, complete gameplay integration and other TODO units remain unfinished.
Evidence/decision BLOCKED rows remain blocked. Earlier claims of no FP serialization
are superseded by existing schema 4; debit receipts are still absent from RunReceipt.
I01d was not selected. No optional foundation refinement undertaken.

SliceInventory.proposeDestruction returns an immutable proposed snapshot for explicit
owned instance IDs across active/storage locations. It rejects Questing, Taint,
Sacrifice, unknown IDs and empty selections; final ActiveWheel validation enforces
five ordinary slices across three activities. Survivors retain identity/modifiers/order;
unlocks, lifetime acquisition history and Satchel tier survive without refund/reset.
A failed compound proposal leaves the original inventory intact. Authority: WB-D105,
106, 118 and 121 plus WHEELS/STATE_CONTRACT. Canonical catalog keys are trusted input;
Questing key is questing. This is a proposal, not an authorized/durable edit: future
aggregate caller must enforce account/run/revision/legal boundary and persist receipt
and full state before success. No movement, paid edit, forced-special insertion,
serialization or live UI/plugin wiring is claimed. Master Wheel source unchanged;
incomplete gameplay remains unwired/default-disabled.

Two focused JUnit methods added for active/stored compound destruction, retained
lifetime price after last-copy loss, protected/special/unknown selections, immutable
failure and final count/diversity. javac --release 11 compiled all production domain
sources. Dependency-free executable smoke passed for successful removal, retained
instances/history, Questing/unknown/empty rejection and five/three guards. Gradle
SliceInventoryTest/ActiveWheelTest/WheelboundBehaviorTest failed before compilation:
missing Gradle 8.10 distribution download denied (SocketException). No JUnit/CI or
legacy regression pass claimed; supervisor must execute these suites.

Next required dependency for paid gameplay: typed debit receipts in the unified
revision journal, schema compatibility, mixed-operation validation/restoration and
cross-operation ID collision rejection; then atomic debit/product mutation with
failed-write/lost-acknowledgement/restart tests. Current free destruction proposal
also needs aggregate lifecycle/replay integration before activation. Reviewed quote
payloads remain required; numeric candidates are not adopted. No external paths,
credentials, secrets or workflows accessed. Supervisor handles Git/PR/CI/merges.

## Current local continuation - FP snapshot schema, 2026-10-29

IDLE after bounded WB-FND-10/12 and WB-ECON-01 prerequisite increment (S).
Reconciled domain, persistence, command services and focused test sources with the
recorded I01a/b/c receipts; no Git operations or remote merge verification. FND-01
remains DONE. FND-10/12 remain PARTIAL; TREE-02, WHL-02/03/05 and ECON-01/08
remain PARTIAL_UNVERIFIED. FND-11 remains TODO: no durable debit command exists.
Other TODO, PARTIAL and evidence-BLOCKED rows stand. I01d was not selected.
This entry supersedes historical next-task text; no complete gameplay is claimed.

RunDocument now carries optional immutable FatePointLedger. RunCodec schema 4 persists
starting balance and ordered typed ledger entries and reconstructs via domain validation.
Absent legacy FP stays explicitly unavailable, never an invented zero or starting grant.
Schemas 1/2/3 remain readable; schema-3 draw decoding is independent of the current
schema constant. Schema-4 missing/unknown fields, invalid entries, intermediate
unaffordable spending and overflow fail closed. LifecycleCommands and DurableWheelDraws
carry existing FP state through writes and replay. AtomicRunStore uses this codec as
before. The ledger remains trusted snapshot data, not proof of reward authorization,
a command journal, or an implemented purchase. No debit receipts are serialized yet.
Authority: WB-D105 and STATE_CONTRACT/PERSISTENCE; numeric prices remain unadopted.
Master Wheel behavior source unchanged; incomplete gameplay remains unwired/disabled.

Five focused JUnit methods authored: ledger roundtrip, legacy absent-state preservation,
malformed/overspent/overflow ledger rejection, schema-3 draw compatibility, and FP
preservation across persisted pause/resume/draw/restart replay. javac --release 11
compiled all domain sources plus RunReceipt and RunDocument successfully. RunCodec and
command-service compilation require unavailable dependencies and were not verified.
Gradle focused codec/draw/lifecycle and legacy WheelboundBehaviorTest failed before
compilation: missing Gradle 8.10 download denied (SocketException). No JUnit tests ran.
Supervisor must execute focused and legacy suites; no current CI pass is claimed.

Next bounded task: typed debit receipts in the unified revision journal, mixed-operation
restoration/validation and cross-operation command-ID collision rejection. Then add the
atomic debit service with failed-write/lost-acknowledgement/restart tests, followed by
reviewed paid slice-instance/lifetime-history mutation for WHL-04/05. Do not enable a
standalone gameplay debit without its complete product mutation and reviewed quote.
No external paths, credentials, secrets, workflows or Git accessed. Supervisor handles
publication, CI and merges.

## Current local continuation - validated debit history restoration, 2026-10-29

IDLE after bounded WB-ECON-08 prerequisite increment (S). Reconciled existing domain,
persistence and focused test sources against the backlog and recorded merged I01a/b/c
receipts; no Git operations or remote merge verification. FND-01 remains DONE;
TREE-02, WHL-02/03/05, ECON-01/08 remain PARTIAL_UNVERIFIED. FND-11 remains TODO.
Other PARTIAL/TODO/BLOCKED rows retain their status. This supersedes older next-task
text. I01d was not selected. No complete gameplay purchase is claimed.

FatePointDebits.restore reconstructs a contiguous debit-only proposal stream from its
trusted original lifecycle/ledger seed and typed receipts. It validates each revision,
affordability and recorded resulting balance, rejects duplicate command IDs, and restores
original-response replay after later debits. Receipt construction validates local fields.
The original seed must precede these receipts; a post-debit ledger is not a seed. Mixed
lifecycle/draw/reward histories still require complete aggregate validation. Account/run
identity comes from the trusted enclosing seed; receipts are not independently portable
identity evidence. No save schema, file durability, prices, products or live wiring added.
Authority: WB-D105 and STATE_CONTRACT. Master Wheel source unchanged; gameplay disabled.

Two focused JUnit tests added for restoration/continuation/replay and corrupt balances,
reordering, repeated IDs, inactive seed, insufficient seed funds and invalid revisions.
javac --release 11 compiled all domain production sources. A dependency-free executable
smoke check passed for restoration, original-response replay and corrupt/duplicate rejection.
Gradle focused debit/ledger and legacy WheelboundBehaviorTest attempt failed before test
compilation: missing Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran; supervisor must run focused and legacy suites. Smoke is not JUnit/CI.

Next remaining task: integrate FP ledger and typed receipts into the versioned RunDocument,
RunCodec and AtomicRunStore. Define explicit legacy-schema compatibility without inventing
starting FP; preserve FP through lifecycle/draw operations; reject cross-operation command
ID reuse; test failed writes, lost acknowledgements and restart replay. Then couple reviewed
debits to slice instance/lifetime-history mutation. No external paths, credentials, secrets,
workflows or Git accessed. Supervisor handles publication, CI and merges.

## Current local continuation - FP debit proposals, 2026-10-29

IDLE after bounded WB-ECON-08 / WB-FND-11 prerequisite increment (S).
Reconciled domain, persistence and test inventory with recorded merged I01a/b/c receipts.
RunDocument still persists only lifecycle/draw state; no complete gameplay aggregate or
FP serialization exists. Recorded merges were not remotely verified; no Git operations.
FND-01 remains DONE. TREE-02, WHL-02/03/05 and ECON-01 remain PARTIAL_UNVERIFIED.
ECON-08 is now PARTIAL_UNVERIFIED for in-memory debit replay only. FND-11 remains TODO
for atomic durable FP/spin integration. Other partial/TODO/evidence-blocked rows stand.
This supersedes historical next-task text. I01d was not selected or changed.

FatePointDebits supplies immutable proposed lifecycle revision, ledger and debit receipts.
Commands bind account/run identity, UUID, original revision, quote key/version and amount.
Exact replay returns the original receipt after later debits without changing current state;
changed payload/identity, stale revision, inactive runs, invalid amounts, insufficient funds
and revision overflow fail closed. Existing obligations survive successful proposals.
No reviewed prices are adopted: a quote reference does not prove quote authorization.
Trusted callers must validate product/legal-boundary/quote authority and persist complete
product changes, ledger, lifecycle and receipts atomically before publishing success.
This is not durable replay protection: the seed constructor accepts trusted state only,
not restored receipts. No purchases, rewards, evidence deduplication, refunds, crash recovery,
serialization or live plugin wiring are implemented. Authority: WB-D105 and STATE_CONTRACT.
Master Wheel source is unchanged; incomplete gameplay remains unwired/default-disabled.

Three focused JUnit methods authored for original-response replay after later commands,
quote/amount/revision collisions, foreign account/run, inactive/stale requests, insufficient
funds, invalid input, immutability and overflow. javac --release 11 compiled all domain
sources successfully. Gradle focused debit/ledger and legacy WheelboundBehaviorTest attempt
failed before compilation: missing Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran; supervisor must execute focused and legacy suites.

Next bounded unit: integrate FP state and typed debit receipts into the versioned run
aggregate/codec and AtomicRunStore with explicit schema compatibility and replay restoration.
Preserve FP state through lifecycle/draw commands, reject cross-operation command-ID reuse,
and test failed writes/lost acknowledgements/restarts before claiming durable transactions.
Then couple approved debit to new-instance/lifetime-history mutation for WHL-04/05.
No external paths, credentials, secrets, workflow access or Git operations were used.
Supervisor handles publication and CI.

## Current local continuation - Fate Point ledger arithmetic, 2026-10-29

IDLE after bounded WB-ECON-01 increment (S). Reconciled domain and test inventories,
plugin/config wiring and recorded I01a/b/c merged receipts; no Git or remote verification.
FND-01 stays DONE. TREE-02, WHL-02/03/05 and ECON-01 are PARTIAL_UNVERIFIED;
other audited PARTIAL/TODO/BLOCKED statuses stand. Historical next-task text below
is superseded by this continuation. I01d was not selected or changed.

FatePointLedger implements immutable starting balance and ordered positive-amount
EARNING/DISCRETIONARY_COST/PENALTY entries. Earnings recover debt; penalties may
create debt; discretionary costs require full funds. Restoring history validates each
intermediate balance, rejecting spend-before-earn even when final funds would suffice.
Exact long arithmetic rejects overflow without wrapping/clamping; no gameplay cap is
adopted. Authority: WB-D105, ECONOMY and STATE_CONTRACT. No prices, reward eligibility,
paused processing, command deduplication, receipts or durable settlement are implemented.
Trusted caller authorization remains required; ledger append is only arithmetic/history.

Three focused JUnit methods authored for debt/recovery/exact funds, insufficient funds,
restoration ordering, defensive copies, invalid inputs and both overflow boundaries.
Java 11 production compilation passed for ledger, inventory, wheel and graph.
Gradle focused ledger/inventory and legacy WheelboundBehaviorTest attempt failed before
compilation: absent Gradle 8.10 distribution download denied (SocketException).
No JUnit tests ran. Supervisor must run focused and legacy suites before verification.

Next eligible bounded task: revision-checked replay-safe FP command/receipt integration
with the durable run aggregate, before paid slice/history transitions (FND-11/ECON-08).
Read existing receipt/store and frozen-quote contracts first; keep product amounts and
catalog payloads gated on reviewed authority. ECON-01 remains partial until durable
integration and executed tests. WHL-03 movement, WHL-04/05 purchases and WHL-11
Questing edit enforcement remain unfinished. Legacy Master Wheel source unchanged;
incomplete gameplay remains unwired/default-disabled. No Git, external-path, credential,
secret or workflow access. Supervisor handles publication and CI.

## Current local continuation - lifetime purchase classification, 2026-10-29

IDLE after bounded WB-WHL-05 classification increment (XS/S). Reconciled domain,
persistence, plugin/config and test inventories against backlog and recorded I01a/b/c
merge receipts. No Git operations or remote verification. WB-FND-01 remains DONE;
TREE-02/WHL-02/WHL-03 remain PARTIAL_UNVERIFIED. WHL-05 is PARTIAL_UNVERIFIED.
Other PARTIAL/TODO/BLOCKED statuses stand; no complete gameplay purchase is claimed.
I01d was not selected. Current user restrictions supersede publication instructions.

SliceInventory.classifyNewPurchase implements WB-D118 lifetime classification only.
Unlocked activities without acquisition evidence use FIRST_INSTANCE; a starting baseline
uses PAID_DUPLICATE ordinal 1; subsequent paid history uses count + 1, even with no
current copies. Stored copies do not change classification. Locked/malformed keys and
counter overflow fail closed. Calls never advance history or mutate ownership. This is
not a quote, receipt, readiness check, purchase transition or price adoption. Caller must
provide trustworthy history; empty ownership cannot establish never-owned status.
Read WB-D011/106/118/121, WHEELS and STATE_CONTRACT/PERSISTENCE before implementation.

Two focused JUnit methods added for first/starting/duplicate/reacquisition/storage cases,
repeat-call immutability, malformed/locked/null keys and long boundary overflow.
javac --release 11 compiled SliceInventory, ActiveWheel and ProgressionGraph successfully.
Attempted Gradle SliceInventoryTest, ActiveWheelTest, ProgressionGraphTest and legacy
WheelboundBehaviorTest using workspace-local Gradle home, offline/no-daemon options.
Missing Gradle 8.10 attempted download and failed Permission denied before compilation.
No JUnit tests ran; supervisor must execute focused and legacy suites.

Next eligible bounded dependency: WB-ECON-01 immutable FP ledger/balance validation,
reading approved negative-balance, penalty and discretionary-cost rules first. Then
implement revision-checked, replay-safe durable debit/new-instance/history receipts
before WHL-04/05 purchase transitions. Frozen reviewed prices/catalog payloads remain
required; no candidate amounts may be activated. WHL-03 inventory transitions, WHL-11
Questing edit enforcement and aggregate persistence remain unfinished. Legacy Master
Wheel source unchanged; incomplete gameplay remains unwired/default-disabled. No external
path, credential, secret or workflow access. Supervisor handles Git/PR/CI/merge.

## Current local continuation - owned slice inventory, 2026-10-29

IDLE after bounded WB-WHL-03 snapshot increment (S). Reconciled source/test inventory
against backlog and existing merged receipts; no Git operations or remote verification.
WB-FND-01 remains DONE; TREE-02/WHL-02 remain PARTIAL_UNVERIFIED; WHL-03 is now
PARTIAL_UNVERIFIED. Other PARTIAL/TODO/BLOCKED classifications remain unchanged.
I01d was not selected. Current user restrictions supersede historical publication rules.

SliceInventory validates an immutable active-wheel/Satchel ownership snapshot. Approved
Satchel tiers hold 3/6/10/15 ordinary instances independently of the 24 active slots.
UUIDs must be unique across both locations; Taint/Sacrifice cannot enter storage.
Ordinary instances require retained unlock and explicit acquisition history. Baseline
presence is distinct from paid duplicate count, and survives zero current copies;
unlock-only activities need no acquisition record. Stored instances retain IDs and
individual enhancements, including Questing. No paid movement, purchases, destruction,
refunds, grants, price selection, persistence or gameplay activation is implemented.
Snapshot restoration must supply trustworthy history and canonical catalog keys; this
model cannot establish historical evidence from current ownership. WB-D011/106/118/121
and WHEELS govern this increment. Questing destruction remains a future edit rejection.

Three JUnit methods authored for capacities, defensive copies, retained acquisition
history, stored Questing identity/upgrades, duplicate IDs, special storage and malformed
ownership. javac --release 11 successfully compiled SliceInventory, ActiveWheel and
ProgressionGraph into build/wheelbound-domain-check. This is not a JUnit pass.
Attempted Gradle SliceInventoryTest, ActiveWheelTest, ProgressionGraphTest and legacy
WheelboundBehaviorTest with workspace-local Gradle home, offline/no-daemon options.
Missing Gradle 8.10 triggered download; Permission denied stopped before compilation.
No JUnit tests ran. Supervisor must execute focused and legacy suites.

Next eligible bounded gameplay work: WB-WHL-03 acquisition-history purchase transition
and WB-WHL-05 lifetime classification, with immutable replay-safe receipt semantics;
read approved transaction contracts first. Do not adopt unapproved numeric prices.
Paid storage/reactivation and WB-WHL-04 require FP ledger/atomic transaction prerequisites;
WHL-11 destruction enforcement belongs in those edits. Complete aggregate persistence,
catalog admission and executed tests remain required before WHL-03 can be DONE.
Legacy Master Wheel unchanged; mode remains unwired/default-disabled. No external-path,
credential, secret or workflow access. Supervisor handles Git/PR/CI/merge operations.

## Current local continuation - active wheel snapshot, 2026-10-29

IDLE after bounded WB-WHL-02 (S). Reconciled current domain/persistence/plugin and test
inventories with recorded merged receipts; no remote verification or Git operations.
Existing backlog classifications stand except WHL-02 is now PARTIAL_UNVERIFIED.
I01d was not selected or modified. This supersedes historical next-task directions.

ActiveWheel provides immutable UUID slice identities, ordinary activity keys and individual
weights, separate Taint/Sacrifice kinds, shared 24-slot capacity and minimum five ordinary
slices across three activities. Exact ordinary quarter weights 4/5/6/8/12 implement
1/1.25/1.5/2/3. Special weights are explicit inputs; no unresolved Taint default adopted.
Reordering requires an exact identity permutation and preserves instances and modifiers.
Duplicate IDs, invalid weights and total overflow fail closed. Canonical activity catalog
admission remains a caller responsibility. Authority: WB-D005/006/008/010/096/157,
WHEELS.md and DEFY_FATE.md. No purchasing, Defy progression or spin authorization.

Three JUnit methods added for capacity/diversity, weights/reorder/immutability and malformed
snapshots/overflow. javac --release 11 compiled ActiveWheel and ProgressionGraph successfully
into build/wheelbound-domain-check. Production compilation is not a JUnit pass.
Attempted gradlew.bat --gradle-user-home .gradle-local test --offline --no-daemon
--console=plain with ActiveWheelTest, ProgressionGraphTest and WheelboundBehaviorTest.
Missing Gradle 8.10 and blocked download (SocketException / Permission denied) prevented
compilation; no focused or legacy JUnit tests ran. Supervisor must execute these suites.

WHL-02 remains PARTIAL pending executed tests and catalog/aggregate integration. WHL-07
odds UI remains TODO. Next eligible gameplay increment: WB-WHL-03 immutable owned-slice
and Satchel inventory after reading WB-D011/118/121 and storage capacity decisions.
Paid edits, counters and durable spins remain separate prerequisites. Legacy Master Wheel
unchanged; incomplete mode remains unwired/default-disabled. No external path, credential,
secret or workflow access, publication or background work. Supervisor handles Git/CI.

## Current local continuation - progression prerequisites, 2026-10-28

IDLE after WB-FND-01 source/backlog audit and one bounded WB-TREE-02 increment.
Current user constraints supersede older next-task/publication instructions below.
All 131 backlog rows now distinguish DONE / PARTIAL / TODO / BLOCKED with a scoped
reconciliation. Recorded merged I01a/b/c receipts remain historical evidence; no Git or
remote verification performed. I01d was not selected or modified.

Production ProgressionGraph implements immutable nested AND/OR ownership prerequisites,
explicit roots, unknown-reference and cycle validation, and rejection of already-owned
unlock candidates. Evaluating prerequisites never purchases, grants benefits or adds
wheel slices. No production catalog, prices, client wiring or gameplay activation added.
Authority: WB-D093; Challenge entry/rank test examples follow WB-D124 and WB-D147.
Three focused JUnit methods cover the complete nested boolean truth table, exact
Bossing-vs-Skilling identity, predecessor ownership, defensive copies, malformed rules,
unknown ownership and cycles (including an alternative root that must not hide a cycle).

PARTIAL_UNVERIFIED: attempted gradlew.bat --gradle-user-home .gradle-local test --offline
--no-daemon --console=plain --tests com.wheelbound.domain.ProgressionGraphTest
--tests com.wheelbound.WheelboundBehaviorTest. Wrapper attempted missing Gradle 8.10
and failed with SocketException / Permission denied before compilation. No tests ran.
Static inspection completed; old build reports do not validate current source.

Next: supervisor runs focused and legacy tests in an available build environment.
Next eligible implementation: WB-WHL-02 immutable 24-slot active-wheel model, reading
exact approved ordinary minimum/activity/special/weight rules first. WB-TREE-02 remains
partial until tests and catalog integration; purchasing/benefits remain separate tasks.
Legacy Master Wheel source unchanged; incomplete gameplay remains disabled. No Git,
external-path/credential/secret/workflow access, publication or background work.
## Latest local continuation - skip superseded queued opens, 2026-10-28

One bounded I01d increment: AsyncRunSession checks request generation when a queued
worker begins and returns false before storage access if disconnected or superseded.
Reads already in progress still use the existing publication guard; no IO cancellation
is claimed. Future completion stays outside the new generation-check lock. Cancelling
only the returned future retains its documented non-disconnect semantics.

Two focused JUnit tests use corrupt snapshots to distinguish skipped reads from read
failures: disconnect before worker execution; replacement session publishing before an
old corrupt request executes. They also check preserved corrupt bytes, fresh token
admission and exceptional failure/admission closure for a current corrupt request.
Static review covered the early-return and existing post-read generation guard.

PARTIAL_UNVERIFIED: attempted AsyncRunSessionTest, FoundationPreviewTest and legacy
WheelboundBehaviorTest with gradlew.bat --gradle-user-home .gradle-local test --offline
--no-daemon --console=plain and the three --tests filters. Gradle 8.10 was absent;
wrapper download failed with SocketException / Permission denied before compilation.
No tests executed or new pass claimed.

Next: execute focused and legacy tests in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile boundaries
and actual client baseline sampling. Preview remains unwired/default-disabled; incomplete
gameplay stays disabled and legacy Master Wheel unchanged. No Git operations, external
path inspection, credential/secret access or workflow access performed. No publication
or background work. IDLE. Current user restrictions supersede historical instructions.

## Latest local continuation - non-reentrant baseline sampling, 2026-10-27

One bounded I01d hardening increment closes reentrant baseline admission during
FoundationPreview.sampleBaseline. Direct acknowledgement and nested sampling now
return false while sampling; nested callbacks never run. A finally block clears the
sampling guard and baseline on all exits, including Errors, before successful sampling
may acknowledge its original token. Session replacement still cannot admit a stale token.
Two focused JUnit tests cover reentrant acknowledgement/nested refusal and failure
cleanup followed by retry. Preview remains unwired and disabled by default; no gameplay
activation or legacy Master Wheel changes.

PARTIAL_UNVERIFIED: attempted FoundationPreviewTest, AsyncRunSessionTest and legacy
WheelboundBehaviorTest with workspace-local --gradle-user-home .gradle-local test
--offline --no-daemon --console=plain. Gradle 8.10 was unavailable; wrapper download
failed with SocketException / Permission denied before compilation. No tests executed
or new pass claimed. Static review checked normal, exceptional and reentrant exits.

Next: execute focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile boundaries
and actual client baseline sampling. No Git operations, external-path inspection,
credentials, secrets or workflows accessed. No publication or background work. IDLE.
Current user restrictions supersede historical publication instructions below.

## Latest local continuation - baseline sampling callback, 2026-10-27

One bounded I01d increment adds FoundationPreview.sampleBaseline(token, sampler).
It checks the current token before invoking sampling, clears prior observation admission,
and acknowledges only after successful sampling against the same session. Sampling
exceptions propagate with admission closed; a session replacement during sampling cannot
admit the replacement. Caller must use its serialized client callback path and replace
old samples without awarding progress. This helper remains unwired and cannot verify
sample contents or the caller thread. Three focused JUnit tests added for sampling order,
stale token refusal, failure/retry and replacement-session isolation.

PARTIAL_UNVERIFIED: attempted FoundationPreviewTest, AsyncRunSessionTest and legacy
WheelboundBehaviorTest using gradlew.bat --gradle-user-home .gradle-local test with
--offline --no-daemon --console=plain. Wrapper failed before compilation because local
Gradle 8.10 is absent and download returned SocketException / Permission denied.
No tests executed or new pass claimed. Static review checked admission and reentrant
session-change paths. Legacy Master Wheel files unchanged; gameplay remains disabled.

Next: execute focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified account/profile events
and actual client baseline sampling. No Git operations, external-path inspection,
credentials/secrets or workflows accessed. No publication or background work. IDLE.
Current user restrictions supersede historical publication instructions below.

## I01d fresh observation baseline gate - 2026-10-27

FoundationPreview now exposes establishBaseline(token) and acceptsObservation(token).
An existing-save open only establishes a storage session. Future adapters must capture
its token, sample fresh client baselines on their serialized client callback path, then
acknowledge that token before admitting subsequent observations. The acknowledgement
stores no sample and awards no progress. Raw capture/accepts retain session-only meaning.
Reconnect (including invalid input), disable and disconnect clear baseline admission;
stale or foreign tokens cannot establish it. Failed stale acknowledgement does not replace
a valid current baseline. No live adapter calls this API yet, and actual fresh sampling
remains unfinished; the API cannot itself verify caller-provided sampling occurred.

Three focused JUnit tests cover open-without-baseline refusal, fresh token acceptance,
same-revision reconnect and stale-token refusal, and disable/disconnect invalidation.
Attempted: gradlew.bat --gradle-user-home .gradle-local test
--tests com.wheelbound.persistence.FoundationPreviewTest
--tests com.wheelbound.persistence.AsyncRunSessionTest
--tests com.wheelbound.WheelboundBehaviorTest --offline --no-daemon --console=plain.
FAILED BEFORE COMPILATION: Gradle 8.10 distribution absent; wrapper download blocked
with SocketException / Permission denied. No tests executed or new pass claimed.
Static review checked synchronized admission and invalidation paths. PARTIAL_UNVERIFIED.

Next: run focused and legacy suites in an available build environment, then supported
RuneLite directory-provider wiring, opt-in UI/config, verified identity events and actual
client baseline sampling. Gameplay remains disabled and legacy Master Wheel untouched.
No Git operations, external paths, credentials, secrets or workflows accessed. No background
work or publication. Current user restrictions override historical publication directions.

# Current implementation checkpoint

## I01d caller-supplied preview directory slice - 2026-10-27

Added a FoundationPreview constructor binding an injected plugin directory and RunCodec,
plus identity-only connect. No default/global directory lookup or construction-time IO.
Disabled connects submit no work; enabled opens use AsyncRunSession and existing-save
semantics. Invalid identity invalidates previous tokens before rejecting the request.
The existing explicit-store API remains available. This is an unwired injection seam;
no claim of RuneLite directory-provider or live identity integration is made.

Four focused JUnit tests cover delayed filesystem access/default-disabled behavior,
missing-save refusal without snapshot creation, existing-save opening/profile isolation,
exclusive use of the injected directory and invalid-input token invalidation.
Attempted FoundationPreviewTest and AsyncRunSessionTest via gradlew.bat with
--gradle-user-home .gradle-local test --offline --no-daemon --console=plain.
FAILED BEFORE COMPILATION: Gradle 8.10 distribution unavailable; wrapper download
blocked with SocketException / Permission denied. No new tests executed or pass claimed.
Static review checked worker delegation, directory use and fail-closed invalid input.

Next: execute focused and legacy suites in an available build environment; then wire
the supported RuneLite directory provider, opt-in preview UI/config and verified identity
events with fresh baselines. No run creation, gameplay, detector/reward or recovery UI
activation. Legacy Master Wheel unchanged. Current request forbids Git operations and
external paths/secrets/workflows; none inspected. No background work or publication.


## I01d opt-in preview gate slice - 2026-10-27

Added unwired `FoundationPreview`, a read-only wrapper over `AsyncRunSession`.
Opt-in starts false for each instance and is not persisted. Disabled connects return
false without submitting storage work. Disabling invalidates tokens and pending reads;
re-enabling does not reconnect. Disconnect retains opt-in but requires a fresh explicit
connect with caller-verified identity. This API neither creates runs nor enables gameplay.

Four focused JUnit tests added for disabled startup, disable/re-enable during a queued
read, published-token invalidation, and disconnect/reconnect with process-local opt-in.
Attempted focused FoundationPreviewTest and AsyncRunSessionTest using the workspace-local
Gradle cache (`--gradle-user-home .gradle-local test --offline --no-daemon --console=plain`).
The wrapper failed before compilation: Gradle 8.10 distribution absent and download
blocked with SocketException / Permission denied. No tests executed or new pass claimed.
Static review checked gate/session locking and pending-read invalidation behavior.

Next: execute focused and legacy tests in an available build environment, then add
plugin-directory injection, opt-in UI/config integration, verified account/profile event
wiring and fresh observation baselines. Preview remains unwired; no first-run creation,
gameplay, reward/detector or recovery UI activation. Legacy Master Wheel untouched.
No Git operations, external-path inspection, secrets or workflows accessed. No background
work. Current user restrictions supersede historical publication instructions below.

## I01d asynchronous opener slice - 2026-10-27

Added unwired `AsyncRunSession`, with an injected storage worker executor and a fresh
generation per open/disconnect. Workers load private sessions; only the latest request
can publish. Disconnect immediately closes admission without waiting for IO. Failed
reads/submissions never restore prior admission or initialize missing runs. Future
completion is not observation evidence; capture a fresh token. Cancellation is not
disconnect, as documented on the API.

Four focused JUnit tests added: disconnect during queued read, reversed profile reads
and token invalidation, missing-run failure without creation, rejected submission.
Tests NOT EXECUTED: default wrapper failed creating its external cache lock; retry with
workspace-local `--gradle-user-home .gradle-local` could not obtain Gradle 8.10 because
download was blocked. The prior 117-test receipt applies only to the predecessor.

Next: run new tests and legacy suite in an available build environment, then implement
opt-in preview, plugin-directory injection, verified account/profile events and fresh
observation baselines. No live event/UI wiring, commands, run creation or gameplay
activation in this slice. Legacy Master Wheel files unchanged. No Git operations,
remote verification, PR or merge performed; current user restrictions supersede older
publication instructions below.

## I01d session coordinator slice ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â 2026-10-27

Implemented `RunSession` behind the disabled mode boundary. It opens existing saves only,
issues opaque per-session/revision callback tokens, invalidates them on disconnect and
lifecycle commands, and closes observation admission after failed reads or commands.
Historical command replay refreshes from the actual persisted head rather than the old
receipt. Pause retains the committed obligation; reconnect never backfills observations.
Callers must perform storage work on a worker thread and disconnect on logout, hopping,
profile changes and shutdown. This coordinator is not yet wired to RuneLite events.

Validation: local Gradle 8.10 `test --offline --no-daemon --console=plain`, using the
existing Gradle cache and installed JDK 17 with Java release 11 compilation, passed
117 tests with zero failures/errors/skips. Four new tests exercise actual temporary-file
storage, same-revision reconnect, foreign-session tokens, pause/resume, historical replay,
failed commands/open and lost acknowledgement. This is not JDK 11 CI or live-client proof.

Next: wire opt-in foundation preview to supported plugin-directory injection and verified
account/profile events, with asynchronous stale-result guards and reinitialized observation
baselines. No first-run activation, gameplay, detector, recovery UI or Hub release is enabled.
Changes remain on the isolated `agent/i01d-session-adapter-20261027-222408` branch pending
review/CI; no PR or merge is claimed for this slice.

## Current verified batch release ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â 2026-10-27

WB-D158/159 authorize coding and verified PR integration. This release checkpoint belongs to PR5; verify its exact final head, CI and source merge before reporting merged. No background work after the owning merge.

| PR | Bounded feature | Verified result |
| --- | --- | --- |
| 2 | Versioned, validated foundation save codec | Merged393a2fe699b8382001c3d9da14324114805f67bb; final codef4bc723f15f42a6b43637dee4f54d2021ebadd31; runs40602782885/40602775250 passed |
| 3 | Account-separated atomic snapshot files | Merged8d987651bb0c90a0053e122dea4acb73741a975a; code38a7b81f8a3a52dbd27dc0c2cd119beabec82093; runs40603093612/40603089365 passed |
| 4 | Durable lifecycle command replay and history retention | Mergeddfcef5cbbc8b1cdc47e323a0ab9b7cd894a437fc; final coded81df900f3b897edbd33461277856e2f8192db98; runs40603506804/40603501702 passed |
| 5 | Persisted frozen weighted draws | Code031a8f3557f78b7cc3b9a276be53ced87489f908 passed runs40603884999/40603879444; this checkpoint is the documentation-only follow-up before merge |

Actual exported XML from artifact12780340983:109 tests, zero failures/errors/skips. New batch tests: codec6, file storage8, lifecycle transactions7, weighted draws9 (30 total), plus prior domain9 and existing70 regressions. Gradle8.10/JDK11.0.32.1/RuneLite client1.13.4 resolved in the successful build invocation. Old design_checks.py fixed-eight-catalog assertion remains a separately verified pre-existing issue. Local Java wrapper network remains blocked; remote build/test evidence is authoritative.

Acceptance limits: these are foundation snapshots/transactions, not full playable-mode persistence. No live event adapter, first-run activation, actual content admission, gameplay charges/rewards, full-mode migration, journal rollover or pending-file recovery UI is enabled. The1MiB envelope limit refuses further growth rather than truncating receipts. Interrupted pre-replace writes retain pending/original files and block writes for explicit recovery. Atomic moves and directory force are tested on CI Linux only; unsupported operations fail closed. Existing shipped wheel rendering, configuration and gameplay paths remain unchanged; no Hub submission.

Exact next bounded task: I01d opt-in foundation preview/account-session adapter, with stale login/profile callback invalidation, reinitialized observation baselines and supported plugin-directory injection; retain legacy wheel regression tests. Complete full aggregate/spin/FP/Fate transactions before enabling actual mode gameplay. Gameplay catalog/balance/client/policy gates remain unaffected. Older prepared/next paragraphs below are historical work-in-progress navigation superseded by this section.


Authority: WB-D158 permits implementation; WB-D159 permits verified PR merges into wheelbound-mode. Protected main remains c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.

Four-PR batch starts from verified 0407530abef2ebd79eef5a78207c1c880225d2bf (PR1 merged).

1. I01b.1: foundation snapshot codec. Versioned lifecycle/account/run/catalog envelope, SHA-256 corruption check, explicit constructor validation, foreign/future/unknown payload refusal; injected Gson. Six new JUnit methods include 55 lifecycle/obligation round trips. No full-mode payload or auto-migration is claimed.
2. I01b.2 next: account-separated atomic file snapshots and conflict/corruption/failure tests.
3. I01b.3 planned: durable pause/resume command receipts and collision/replay tests.
4. I01c planned: durable weighted draw commitments using frozen explicit pools; no gameplay quotas/prices or live admission rules invented.

Local wrapper Java networking remains unavailable; full Gradle/JUnit checks run in GitHub CI before each merge. Existing obsolete documentation validator catalog-count defect remains separately recorded. Check each PR's exact head and CI result before marking merged. No live client/runtime activation or Hub submission in this batch. Checkpoints on a PR describe prepared scope until its CI/merge is verified.

## I01b.2 prepared ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â atomic snapshots

PR2 merged at393a2fe699b8382001c3d9da14324114805f67bb; final code f4bc723f15f42a6b43637dee4f54d2021ebadd31 passed runs40602782885/40602775250. Gson parser compatibility corrected against actual RuneLite dependency.

AtomicRunStore uses a caller-supplied plugin directory, opaque identity paths, per-run file locks, exact expected revisions, forced temporary writes and atomic replacement with last-good backup. Corrupt/foreign/future data and interrupted temporary files fail closed without deletion. Eight JUnit cases include injected pre/post-replace IO failures and real temporary-file/lock tests. No non-atomic fallback; unsupported directory force/atomic moves fail rather than claiming platform support. This is local foundation storage, not a complete journal or live plugin adapter. Pending files require explicit recovery; no destructive automatic recovery is implemented.

Next: durable lifecycle command receipts/replay; no additional gameplay rule decisions needed.

## I01b.3 prepared ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â durable lifecycle commands

PR3 merged at8d987651bb0c90a0053e122dea4acb73741a975a, code38a7b81f8a3a52dbd27dc0c2cd119beabec82093; runs40603093612/40603089365 passed. Real filesystem tests include exclusive locking and pre/post-replace failure injection.

Schema2 persists lifecycle command receipts and state together; schema1 foundation documents can be decoded into an empty-journal document without changing their original bytes. This is not a full-mode migration. New PAUSE/RESUME commands enforce identity/revision, retain obligations, and publish only after persistence. Exact command replay returns its original receipt even after later commands; changed-operation/revision collisions fail. New commands already at their requested state are rejected without changing revision or creating receipts. No money/spin/reward logic exists in this service. Seven additional tests cover receipt-history retention, restart replay, collisions, stale/foreign contexts, lost acknowledgements, no-op/uninitialized input, legacy decode and journal invariants.

Next: frozen weighted draw commitments with their own immutable request/result journal and restart/no-reroll fixtures. Live activation remains disabled/unwired; current foundation snapshot is not a complete gameplay save.

## I01c foundation prepared ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â immutable weighted draw commitments

PR4 merged atdfcef5cbbc8b1cdc47e323a0ab9b7cd894a437fc; final coded81df900f3b897edbd33461277856e2f8192db98 passed runs40603506804/40603501702. Lifecycle receipts and snapshot commit together; storage rejects history removal/rewrite.

WeightedPool retains ordered positive-weight canonical IDs and its explicit catalog version. DrawCommitment freezes a validated sampled ticket and therefore exact result/odds. DurableWheelDraws persists that frozen request/result before returning it; it rejects foreign/stale/paused requests and further draws while a commitment is pending. Exact retries return the original draw without RNG, including after pause/resume/restart or a lost acknowledgement. Pending lifecycle stage is OFFERS_COMMITTED; no settle/reset path is invented. Schema3 supports typed draw journal entries and reads schema1/2 foundations without dropping prior lifecycle receipts. Nine new JUnit methods cover ticket boundaries, immutable pools, invalid inputs, restart, request collisions, pending commitments, lifecycle interaction and legacy/corrupt restore.

Scope: foundation transaction only, NOT a live Master spin, reviewed eligibility generator, spin charge, card offering or complete gameplay save. Caller-supplied synthetic pools test the protocol; they do not admit real content. Existing shipped wheel paths are unchanged. Live preview/account-session adapter, complete aggregate fields, spin/FP transactions, explicit pending-file recovery and cross-platform durability remain next work. Unsupported atomic/directory-force operations fail closed; no platform-wide power-loss guarantee is claimed. No Hub submission.

Batch target: four additional PRs (2ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ5), merged sequentially only after full CI success. Resolve exact final PR5 head/run/merge remotely before claiming completion. Latest merged receipts are carried in subsequent checkpoints and this session's final report.
