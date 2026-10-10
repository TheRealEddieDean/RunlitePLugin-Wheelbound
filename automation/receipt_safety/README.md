# Offline receipt acceptance supplement

WB-A24, 2026-10-10. This automation supplement executes previously specified acceptance cases before production implementation. Gameplay authority remains `docs/wheelbound/DECISIONS.md`; no decision was added or superseded. The source manifest pins the existing state, pipeline, bounty, defiance and question contracts by exact byte hashes; starting branch checkpoint is recorded separately. The current Markdown-derived design PDF remains the gameplay publication; this executable QA supplement is outside its chapter list.

Run from the repository root with Python 3.12:

```bash
python3 automation/receipt_safety/checks.py
python3 automation/receipt_safety/checks.py --verify
```

The first command writes `RESULTS.json`; the second reproduces its bytes without overwriting it. No dependency installation, network, RuneLite, credentials or game account is needed. These tools belong outside the plugin JAR and must never be imported into runtime code. Python 3.12.14 was used for the saved receipt; read the actual version in RESULTS rather than assuming another interpreter reproduces its metadata byte for byte.

## What ran

- 44 independently authored expected traces, repeated for deterministic interpreter agreement.
- 32 systematic combinations: eight operations × four logical write boundaries. Each verifies the pending-write gate, recovery, retry and ledger delta; malformed tails retain the original mock journal.
- 10 intentionally defective oracle variants. Every variant was detected by at least one expected trace, with the exact case IDs saved. This checks that the acceptance assertions catch selected mistakes; it is not a comprehensive mutation score for future production code.
- Six malformed fixture packets rejected: runtime enabling, negative/noninteger quotes, an unauthorized credential-shaped field, missing expectations and an unknown operation.

All FP prices, rewards, penalty amounts, spin conversions, target names and identities here are synthetic test inputs. They do not adopt or calibrate an economy. A `verified` donation flag is a supplied test premise; the oracle never determines whether an OSRS donation happened.

| Guarantee from existing contracts | Explicit cases | Meaning of pass |
| --- | --- | --- |
| Commit before animation; same restored outcome | R01–R06, R38 | A sealed mock record reconstructs the frozen result and one debit |
| Duplicate command/evidence settlement | R03/R04/R12/R17/R19/R23/R25 | Replaying the same known command or evidence does not credit twice |
| Account/run/session/revision isolation | R08–R11/R41 | Foreign identities and stale revisions/generations produce no new mock write |
| Free pause/no backfill | R13–R16/R42/R43 | Obligation survives pause; inactive draws cannot allocate; paused claim of earlier active evidence remains unspecified |
| Pardon Banned→Locked | R17–R19 | Synthetic fixed quote pays once, without automatically unlocking vendor |
| Discretionary spending vs penalty debt | R20/R21 | Voluntary cost refused at insufficient FP; quoted penalty may make FP negative |
| One special spin and no derived allocation | R22–R25/R44 | Taint/Sacrifice pay one allocation; settlement or retry adds no second debit |
| Grand pending-obligation gate | R26/R27 | Zero spins are allowed; mandatory settlement blocks activation |
| Preserve malformed saves | R05/R28–R30 and sweep | Corruption enters a read-only state without deleting the mock payload |
| Keep unresolved settlement rules unresolved | R31–R34/R37 | Q10, CA-MIXED, audit ordering and Q6 stay gated; receipts preserved |
| Manual, qualified bounty claim | R35/R36 | Possession-only key fails; supplied qualified evidence pays only on claim |

## Limits and unresolved implementation questions

This is an in-memory reference interpreter, not the future Java domain implementation. The mock `journal.append` is assumed durable. No file was killed during a write; no fsync, rename, platform filesystem, RuneLite profile write, database, interprocess lock, real migration or live callback was tested. The single-object stale-revision fixture does not establish thread/process concurrency safety. Hashes detect simulated accidental corruption; they do not authenticate player actions or stop tampering. Logical recovery begins at revision zero and does not test journal compaction/checkpoint rollover.

Do not turn a passing oracle into a release claim. Later I01 tests must drive the actual revision-checked Java domain and supported persistence implementation with these acceptance inputs, plus real storage failure injection. Existing build success remains distinct from this suite. No plugin tests, live traces or additional balance trajectories were added.

The state contract does not yet fully specify:

1. Reuse of a command ID with a different payload, identity generation or quote. R07 reports `UNSPECIFIED_COMMAND_ID_COLLISION` and leaves state intact; that label is a fixture safety stop, not a selected production exception policy. A restored legitimate command needs a stable immutable payload identity independent of transient callbacks.
2. Exact torn-tail classification, checkpoint/journal durability ordering and compaction. This oracle preserves an unparsed mock tail and stops writes; it does not decide whether a particular platform write is committed, uncommitted or repairable.
3. Whether a lost acknowledgement is returned as the original complete response or a typed replay response. `REPLAYED` means same financial/state result, not a final API response shape.
4. Evidence-key namespaces across observation sessions, run boundaries and multiple qualifying grouped rewards. The fixture uses one already-qualified reward key; source adapters still need actual stable receipt identity and eligibility rules.

These four are implementation details to resolve in I01 within the existing delegated architecture, with an explicit versioned contract and tests. They are not new gameplay questions for the user. A separate timing ambiguity is R14: claiming a receipt earned while active after pausing is not the same as backfilling paused progress. The visible rules do not clearly settle that claim timing; this oracle preserves the evidence and reports an unspecified interaction rather than adopting a new pause restriction. Resolve it before enabling that particular UI action. Q10 and CA-MIXED remain material design questions. A genuine Coffer receipt and restrictive-control approval remain independent live/policy gates.

## Future Java acceptance handoff

Read the pinned sources, retain independently authored expected values, and port each case to the authorized implementation rather than copying this oracle as runtime logic. Separate injected evidence classification from domain settlement. Verify success is published only after the chosen durable boundary, repeat commands after account switch/reconnect, and preserve the original failed payload during recovery. Add checkpoint rollover, concurrent writer ownership, disk-full/read-only/permission failure, unknown future schema and successful migration fixtures against actual supported storage. Workflow activation and production coding still require separate authorization.
