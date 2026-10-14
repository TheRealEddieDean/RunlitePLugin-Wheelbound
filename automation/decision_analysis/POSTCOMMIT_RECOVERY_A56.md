# A56 — Q6 postcommit recovery boundary

Planning only,2026-10-13 America/Chicago. Starting verified checkpoint `d2cb5db13b95254f4444f5e8789972fc9376c3b7`, tree `7e9f0a3a6ba5665fcb52547443d28bb0648bf7d1`; guarded claim `bb940943496720f718d0433346376380d403e1be`. [Sixteen-case planning matrix](POSTCOMMIT_RECOVERY_A56.json). No runtime tests, fixtures/model executions or new adopted decisions.

## Existing rules and the actual remaining gap

Read against [current decisions](../../docs/wheelbound/DECISIONS.md), [unavailable-selection contract](../../docs/wheelbound/UNAVAILABLE_SELECTION_CONTRACT.md), [Challenge contract](../../docs/wheelbound/CHALLENGE_ASSIGNMENT_CONTRACT.md), [state](../../docs/wheelbound/STATE_CONTRACT.md), [persistence](../../docs/wheelbound/PERSISTENCE.md), [game rules](../../docs/wheelbound/GAME_RULES.md) and [Punishments](../../docs/wheelbound/PUNISHMENTS.md). WB-D132 delegation does not justify inventing evidence or overriding the no-reroll/current Reject rules.

A valid committed assignment remains the same assignment through disconnect, unknown evidence, temporary lack of gear/access, unsupported method, source refresh or pool-diversity changes. Preserve source/item/context/offer randomness, frozen quote, evidence and ledger. Existing free Pause and ordinary Reject are available; Reject retains its FP loss, consumed spin, no completion reward and mandatory Punishment. Do not treat a missing source row as verified impossibility, use an old item, backfill paused progress, or recalculate the target on restart. Existing legitimate settled bounty FP remains independent.

WB-D134 already permits continuing the same hunt when a known matching supported context is revalidated. A change to current eligibility does not silently rewrite frozen facts or authorize credit from a now unsupported/out-of-band context. Whether a pending historical receipt qualifies still needs earning-time/source/account proof and reviewed active reconciliation; it cannot be decided from a later HTML page alone.

The unresolved case is narrower: **an external game change permanently removes every qualifying way to finish a selected ordinary assignment**. The current no-reroll rule preserves it indefinitely unless the player pays the ordinary Reject consequence. A no-fault retirement would be a deliberate exception and therefore remains a user question, not an adopted delegated clarification.

## One recommendation — PROPOSED, NOT ADOPTED

Permit a narrowly reviewed no-fault retirement for that externally proven permanent-impossibility case. The task ends without completion FP, FP penalty, mandatory Punishment or consumption of its allotted ordinary spin. Preserve the original assignment/history and independent earned bounty receipts. The next assignment comes from a normal new Master selection after normal readiness/zero-spin checks; no substitute creature/item picker or automatic redraw inside the old assignment.

A reviewed versioned invalidation record must identify the affected catalog/source/context versions, external change evidence and why every qualifying completion route is gone. This is proposed planning, not an online authority service or proven detector. No player control simply declares a disliked hunt impossible. A temporary outage, self-changed task/gear, low odds, expensive supplies, low target count or unverified parser/catalog omission does not qualify. If an authored alternative context still satisfies the exact item/source, keep that hunt instead. Unknown/permanently unsupported tooling alone is not evidence of a permanent game change.

Apply only to a selected ordinary assignment still unresolved and without supported completion evidence awaiting reconciliation. Already-settled completions/Rejects/mandatory obligations are not undone. No exemption for Taint, Sacrifice, Penance or a Reject punishment. Those keep their separate recovery gates.

Spin accounting must follow the actual journal. The existing spec consumes the ordinary spin on completion or Reject and proposes reserving it earlier. Retiring an unresolved assignment would release its reservation; it must not blindly add a spin. Any implementation that previously debited at reservation needs one bounded inverse for that linked debit, with no refund of unrelated earlier costs and no negative/multiple reservation correction. Atomic receipt keyed by assignment/invalidation version makes crash/replay unable to mint extra spins. Exact schema/durability remains future engineering, contingent on approval.

## Matrix and validation boundary

The packet contains13 derivations of existing preservation/settlement rules, one still-open committed-offer reconciliation case, and two proposed retirement/restart cases. All16 are planned NOT_RUN, runtime-disabled; none is a new gameplay approval or live trace. Check unique IDs, referenced decisions, status separation and no production/model/catalog edits. No automated tests mirroring this planning table were added.

## The one question to present

If a verified game update makes an already-assigned ordinary Fate permanently impossible, should Wheelbound retire it without a reward or penalty and leave its allotted spin unconsumed?

Recommend yes, with the evidence and ordinary-only limits above: it avoids punishing the player for an external game change while preserving the no-reroll rule for every valid or temporarily unavailable Fate. Await the answer; then add the approved/rejected decision and update affected rules/contracts/persistence/UI/testing/checkpoints, validate, publish with a head guard, and verify remote before the next question.

Committed-but-unselected offers, task reacquisition permissions and mandatory-special recovery remain separate questions. This packet does not resolve them implicitly.
