# Wheelbound session checkpoint and ownership

- Session ID: wb-20261009T222520Z-protocol-recovery
- State: ACTIVE
- Owner/task lease: WB-A04 after this A03 checkpoint is verified.
- Lease acquired UTC: 2026-10-09T22:25:20Z
- Lease expires UTC: 2026-10-10T00:25:20Z
- Branch: wheelbound-mode
- Observed parent/checkpoint: 4a5702256cd378d1d0ee41d01e6ea45ef4604568
- Protected main: c8cc38ac9a7ab74d9bc10125388da7cc75af99fa
- Current deliverable: A03 complete; next A04 final raid counts/cards/bounties/vendors/audit.
- Completed this session: verified protocol commit and prior branch/main; completed A02 review ledger, restored four sourced decisions and updated affected specs; A03 added D095–099 and restored D027/035 provenance; 99-decision checks and both review-ledger count checks passed.
- Uncommitted status: no separate completed deliverable is omitted from this checkpoint tree. Treat this snapshot as saved only if it is reachable from the verified remote branch head. Its owning commit is the first-parent commit containing it; do not embed a circular self-hash.
- Remaining: WB-A04–A16 bounded documentation/research/validation tasks. Live client gates A17 and build-network gate A18 remain blocked.
- Known blockers: see BLOCKERS; scope questions in DECISIONS_PENDING.

## Exact continuation

Read current wheelbound-mode head plus automation/SESSION_STATE, TASK_QUEUE, PROGRESS, BLOCKERS and DECISIONS_PENDING; read docs/wheelbound/DECISIONS and research/WEEKEND_MASTER_INSTRUCTIONS. Verify main baseline. Inspect any changes since the recorded checkpoint. If this ACTIVE lease is still unexpired, do not take its task; select a genuinely disjoint independent task and use a separate ownership record, or stop cleanly. If expired, inspect recent branch commits and checkpoint changes before takeover; expiry is permission to inspect, not proof the old worker stopped. Claim task with expected-head update, then re-fetch to verify ownership. Never force over concurrent commits.

After verifying this checkpoint’s owning remote commit, execute A04: read S8-M0201–0300 user turns and named adjacent assistant proposals from the preserved transcript. Reconcile final raids grouping/counts, Bossing cards, separate purchase pools, visible/grouped bounty targets, shop pricing/transparency and later audit/sacrifice approvals. Add RECOVERY_REVIEW_A04.json and source-located register/spec corrections. Preserve H1–H3 hashes. Verify/commit with expected-head guard and update queue/progress/session. Current owner may continue; other sessions must respect ACTIVE lease. No production coding/main changes. At planned end release IDLE and give exact next action.

No background execution, automatic restart, existing-conversation reopening or quota reset is established. Scheduled continuation requires its own actual capabilities and must follow the same guarded startup.
