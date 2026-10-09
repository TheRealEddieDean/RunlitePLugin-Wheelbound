# Autonomous work and checkpoint protocol

User instruction activated 2026-10-09. Supplements the existing Weekend Master Instructions without changing approved design. Git Markdown remains authoritative. Do not restart completed research; documentation/research/planning/validation only, no production plugin code, main preserved, wheelbound-mode exclusively.

## Planning and execution

Target approximately 70% productive execution and 30% verification/documentation/Git synchronization/continuation preparation. These are effort targets, not measured allowance. Never claim exact remaining quota without authoritative tooling. Prefer checkpointing when capacity is uncertain.

Before major work, identify deliverables, independently completable tasks, XS/S/M/L estimates, dependencies and priority. XS ~2–5m; S ~5–15m; M ~15–30m; L >30m. Split larger tasks into smaller useful checkpoints. Do not begin work that cannot plausibly reach one.

For each task: perform work, validate, save deliverable, commit when supported, update queue/progress/checkpoint, then assess the next safe task. DONE requires actual produced/verified artifacts. Routine choices are delegated; collect only material game-design/architecture/scope decisions. Never repeatedly ask resolved questions.

## Startup

Read latest GitHub project state, master instructions, task queue and previous session checkpoint. Check active ownership and recent commits. Select highest-priority unfinished task with satisfied dependencies and safe session size. Do not rely on conversational memory alone. Inspect interrupted/uncommitted work before continuing; never assume it reached GitHub.

## Concurrent protection

SESSION_STATE records active owner/task, acquisition and expiry. Claim/renew/release only with expected-head guarded branch update, then re-fetch. An unexpired active owner excludes overlap. If expired, inspect recent commits before takeover; expiry cannot prove the process stopped. Do not overwrite unreviewed edits. A head mismatch requires reading/reconciling the new head before any retry, never a blind force push. A disjoint task requires its own explicit ownership record; otherwise stop cleanly.

## Checkpoint records

- TASK_QUEUE: task ID, deliverable, priority, dependencies, effort, status, docs and completion criteria.
- PROGRESS: verified completions, actual commit hashes and remaining milestones.
- SESSION_STATE: task, last successful checkpoint, session work, remaining work, hashes, exact continuation, uncommitted status and blockers.
- BLOCKERS: actual missing capabilities/resources/permissions and technical limitations.
- DECISIONS_PENDING: deduplicated major questions; canonical approvals stay in docs/wheelbound/DECISIONS.md.

A commit cannot embed its own SHA. Record observed predecessor and describe the checkpoint’s owning commit, then include actual verified SHA in the next checkpoint/response. Verify remote head and main after update. Never mark a failed write committed.

## Stop and resume

At planned end: finish the current small task if practical, verify/save/commit, update state and exact next action, release ownership as IDLE and stop cleanly. Unexpected interruption resumes from last successful checkpoint after repository inspection. Continue until completed, a material decision blocks further safe work, an actual limitation prevents progress, or no safely executable task remains.

## Scheduled runs

Each genuinely available scheduled run independently reads checkpoints, checks overlap, selects one bounded task, executes only with available tools/allowance, saves/verifies/checkpoints and stops. It cannot be assumed to reopen this conversation, bypass/reset limits or automatically restart. Record unavailable capabilities instead of claiming completion. This protocol does not itself create a schedule.

## End report

Concise actual deliverables, commit links, remaining work, major questions, technical blockers and recommended next engineering milestone. No false full-completion, background-work or quota claims.
