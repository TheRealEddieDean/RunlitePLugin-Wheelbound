# Questing persistence prerequisite, 2026-11-06

Status: nested reward/selection and observation-window codecs IMPLEMENTED_UNVERIFIED. No run schema
change, migration or publication is enabled. WB-QST-02/WB-WHL-08 remain PARTIAL.

Authority: WB-D028/046 retain three/four/five choices; WB-D070/102 require
prerequisites and an authored account-compatible access route, including vendor
access. STATE_CONTRACT requires frozen assignment/reward provenance and pure,
validated migrations that preserve originals on failure. This codec adopts no
gate threshold, price, route catalog or reward schedule.

`QuestFateRewardCodec` is a package-private version-1 nested payload. It retains
capacity, catalog version, ordered quest identities and every route/status
observation, selected quest, quote key/version and exact nonnegative long amount.
Offering order is stored once as observation order; domain constructors enforce
the exact capacity, distinct identities, selection membership and qualification.
Unknown fields, missing fields, invalid types/enums and unsupported versions fail
closed. Restoration uses constructors, never reflective domain deserialization.
Valid altered declarations remain altered declarations; this is no authenticity
check. The enclosing commitment must compare them against its retained context.

Reconciled remaining assignment payload dependencies:

| Component | Required retained data / validation | Status |
| --- | --- | --- |
| Reward and selection | Full ordered choices, eligibility routes/statuses, quote | IMPLEMENTED_UNVERIFIED |
| Quest provenance | Exact Master terminal resolution with creation and retry transcript, slice/activity, stream descriptor and initial/final states, nested reward | Codec UNSTARTED; domain exists |
| Spin allocation | Original spinsBefore and exact same terminal resolution; retained reservation/accounting | Codec integration UNSTARTED; domain exists |
| Original observation window | Account/run, baseline revision, assignment/session/window UUIDs, baseline position, shared exact selection, quest baseline | Nested codec IMPLEMENTED_UNVERIFIED; enclosing unfinished-original validation remains required |
| Aggregate | Explicit optional assignment, exact retained creation/resolution/reservation and compatible lifecycle/catalog | In-memory retention verified offline; codec integration UNSTARTED |

The future assignment payload may store shared selection/resolution data once,
provided reconstruction binds every reference exactly and never substitutes a
new observation window or current balance for the original commitment. Paused
documents must retain the original active baseline context independently of the
current paused lifecycle. Current assignment must never be inferred from history.

RunCodec still writes schema 19, has a candidate retry writer for 20, and reads
through 21 for cards. Both writers refuse Questing-bearing documents. Do not put
Questing fields into an existing schema or treat this nested version as a run
schema bump. A future enclosing schema requires explicit legacy absence, future
schema refusal, full Gson round-trip/mutation/migration tests, original-preserving
failure and durable admission validation before public writes can be enabled.

Added JUnit tests cover all capacities/selections, zero/one/Long.MAX_VALUE amounts,
canonical re-encoding, invalid structures/types, unqualified route/status cases,
overflow and changed valid route context. They have not run: focused Gradle failed
before compilation on the inherited wrapper-cache lock (Access is denied).
No new codec compilation, Gson, migration, store or legacy pass is claimed.

`QuestFateObservationWindowCodec` version 1 stores observation context only. The
future enclosing payload must decode the shared selection once, pass it into this
codec, and construct QuestFateAssignment to bind identity/revision/selection and
reject COMPLETE original baselines. Encode requires the supplied shared selection
to equal the window selection. The window codec retains COMPLETE observation
baselines without making them valid new assignments. It reconstructs the original
ACTIVE/ACTIVE_OBJECTIVE baseline independently of the current paused run and does
not rotate session/window IDs. UUIDs must use canonical lowercase full text.

New JUnit/Gson tests cover all baseline states, zero/long numeric bounds, original
identity/session/window retention, pause refusal for completion, strict fields and
types, malformed UUIDs and altered valid declarations. They remain unrun: focused
Gradle failed before compilation at the inherited wrapper-cache lock. All eight
existing Questing Checks passed after Java 11 domain/aggregate dependency compilation.
No fresh Gson, migration, store or legacy pass is claimed. Existing local reward
test XML reports three passing tests but was not regenerated against current source.

Next: validate components with real Gson when build access returns, then add exact
provenance/allocation binding and the enclosing assignment schema/migrations in
bounded increments. Keep gameplay and public Questing writes disabled.

## Provenance/allocation nested increment, 2026-11-06

QuestFateProvenanceCodec now implements version-1 full resolution/creation/retry
transcript, original spinsBefore, slice/activity and stream states, composing the
reward codec. Decode compares the full restored resolution and original balance
to an explicitly supplied retained allocation; IDs alone cannot hide a changed
retry prefix. This comparison supplies structural binding, not authentication.
Encode also requires exact resolution linkage. The provenance/allocation row above
is now IMPLEMENTED_UNVERIFIED; enclosing aggregate codec integration remains
UNSTARTED. No schema or writer change. Valid altered opaque stream/reward values
remain declarations for enclosing commitment/catalog authority to check.

Three focused JUnit/Gson tests added; Gradle failed before compilation at inherited
wrapper-cache lock access. No fresh codec/JUnit/Gson pass. All eight dependency-free
Questing checks passed after fresh Java 11 domain/aggregate compilation. Next:
verify nested codecs, compose enclosing assignment with retained accounting and
original observation window, then separately implement versioned run migration
and durable admission. Keep public writes refused and gameplay disabled.

## Enclosing nested assignment increment, 2026-11-06

QuestFateAssignmentCodec version 1 now composes provenance/allocation and original
window. Selection is decoded once through provenance and supplied to the window.
Decode binds the complete retained allocation, then invokes QuestFateAssignment
validation for identity, revision, selection and unfinished original baseline.
Original window/session and accounting are preserved. Valid altered declarations
remain declarations requiring future aggregate/catalog authority checks.

Status: enclosing nested codec IMPLEMENTED_UNVERIFIED, with three JUnit/Gson tests.
Fresh Java 11 domain/aggregate dependency compilation and eight domain Questing
checks passed (assignment checks invoke aggregate retention checks). Gradle failed
before compilation at inherited wrapper-cache lock access; no new codec/Gson/JUnit
or legacy pass. Run schema/migrations, durable admission and authenticated catalog
integration remain UNSTARTED. No public writer, schema or gameplay enablement.
Next: real Gson verification, then bounded future run-schema reader/migration with
explicit legacy absence. Current continuation records supersede older next steps.
