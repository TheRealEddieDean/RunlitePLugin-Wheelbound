# Questing persistence prerequisite, 2026-11-06

Status: nested reward/selection codec IMPLEMENTED_UNVERIFIED. No run schema
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
| Original observation window | Account/run, baseline revision, assignment/session/window UUIDs, baseline position, exact same selection, unfinished quest baseline | Codec UNSTARTED; domain exists |
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

Next: validate this component with real Gson when build access returns, then add
the original observation-window payload and enclosing provenance/allocation binding
in bounded increments. Keep gameplay and public Questing writes disabled.
