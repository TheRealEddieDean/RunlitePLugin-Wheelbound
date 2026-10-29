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

# Wheelbound implementation backlog ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â initial decomposition

> Planning inventory, not a claim that every item is unimplemented or independently PR-sized. Reconcile against the current source tree and merged PRs before execution. This file does not supersede approved decisions, implementation checkpoints, or evidence gates.

## Operating instructions

- Source of truth: `docs/wheelbound/DECISIONS.md` and subsystem specs; current code and merged CI evidence determine DONE status.
- Default each item to `PLANNED_REVIEW`: inspect code first, then mark DONE / PARTIAL / TODO / BLOCKED with evidence. Never redo completed functionality.
- Execute in dependency order: FND ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ core WHL/FAT/ECON ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ subsystems ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ integrated REL. Independent UI/catalog work can proceed when safe.
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
| WB-WHL-02 | Define immutable 24-slot active wheel model | PARTIAL_UNVERIFIED |
| WB-WHL-03 | Implement slice ownership and storage inventory | PARTIAL_UNVERIFIED |
| WB-WHL-04 | Implement paid slice add/remove transaction | PARTIAL_UNVERIFIED |
| WB-WHL-05 | Implement purchase escalation on reacquisition | PARTIAL_UNVERIFIED |
| WB-WHL-06 | Implement active-slot replacement at capacity | PARTIAL_UNVERIFIED |
| WB-WHL-07 | Implement weighted odds display and previews | PARTIAL_UNVERIFIED |
| WB-WHL-08 | Implement deterministic Master Wheel spin commitment | PARTIAL |
| WB-WHL-09 | Implement valid-empty landing free retry | TODO |
| WB-WHL-10 | Implement all-unavailable readiness stop | TODO |
| WB-WHL-11 | Implement Questing indestructible storage behavior | PARTIAL_UNVERIFIED |
| WB-WHL-12 | Implement slice mastery weight upgrades | PARTIAL_UNVERIFIED |

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
| WB-QST-02 | Implement 1ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ3 quest display and upgrade capacities | TODO |
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
| WB-TREE-02 | Implement AND/OR prerequisite graph engine | PARTIAL_UNVERIFIED |
| WB-TREE-03 | Implement progressive unlock retention | TODO |
| WB-TREE-04 | Implement tier-count unlock gates | TODO |
| WB-TREE-05 | Implement unlock purchases and immutable receipts | TODO |
| WB-TREE-06 | Implement mastery and card upgrade UI | TODO |
| WB-TREE-07 | Implement progression reset/recovery tests | TODO |

### Fate Points and shops (9)

| ID | Implementation unit | Initial status |
| --- | --- | --- |
| WB-ECON-01 | Implement Fate Point ledger and balance checks | PARTIAL_UNVERIFIED |
| WB-ECON-02 | Implement FP earning from verified objectives | TODO |
| WB-ECON-03 | Implement FP cost and price versioning | TODO |
| WB-ECON-04 | Implement Fate Shop catalog and ownership | TODO |
| WB-ECON-05 | Implement Wheel Builder shop transactions | TODO |
| WB-ECON-06 | Implement vendor unlock shop and access gates | TODO |
| WB-ECON-07 | Implement Bounties Shop entitlement catalog | TODO |
| WB-ECON-08 | Implement purchase replay and refund prevention | PARTIAL_UNVERIFIED |
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
