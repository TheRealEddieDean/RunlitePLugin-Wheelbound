package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Historical response and terminal copy contracts; no disk or evidence authentication. */
public final class MasterRetryResponseChecks
{
    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        for (boolean suspended : new boolean[] {false, true})
        {
            var source = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var before = source.pendingMasterSelection().orElseThrow();
            var creation = source.masterCreations().orElseThrow().receipts().get(before.transactionId());
            var after = suspended ? before : before.append(before.transactionId(), count,
                before.currentStreamState(), new MasterSelectionProvenance.Step(
                    new DrawCommitment(before.pool(), 0), "repeated"));
            var status = suspended ? MasterRetryContinuation.Status.SUSPENDED : MasterRetryContinuation.Status.PROPOSED;
            var request = MasterRetryOperationProposal.restoreResponse(creation, creation.identity(),
                new UUID(95, 1), 5, count, after, status);
            rejects(() -> request.validateRetained(source));
            var next = MasterRetryOperation.prepare(source, request);
            var restored = MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, count, after, status);
            check(restored.equals(request) && restored.hashCode() == request.hashCode());
            check(restored.validateRetained(next) == restored);
            var beforeTerminalPause = LifecycleAdmission.prepare(next, request.identity(), new UUID(96, 1),
                6, RunReceipt.Operation.PAUSE);
            var beforeTerminalResume = LifecycleAdmission.prepare(beforeTerminalPause, request.identity(),
                new UUID(96, 2), 7, RunReceipt.Operation.RESUME);
            var resumedTerminal = MasterSelectionResolutionReceipt.prepare(new UUID(96, 3), 8, creation, after,
                new MasterSelectionProvenance.Step(new DrawCommitment(after.pool(), after.pool().totalWeight() - 1), "terminal"));
            var resumedResolution = MasterTaintTerminalOperation.prepare(beforeTerminalResume, resumedTerminal);
            check(resumedResolution.masterSpins().orElseThrow().available() == 6
                && restored.validateRetained(resumedResolution) == restored);
            var terminal = MasterSelectionResolutionReceipt.prepare(new UUID(95, 2), 6, creation, after,
                new MasterSelectionProvenance.Step(new DrawCommitment(after.pool(), after.pool().totalWeight() - 1), "terminal"));
            var resolved = MasterTaintTerminalOperation.prepare(next, terminal);
            check(resolved.pendingMasterSelection().isEmpty()
                && resolved.masterSpins().orElseThrow().available() == 6
                && resolved.lifecycle().obligation() == RunLifecycle.Obligation.PUNISHMENT_PENDING
                && resolved.receipts().get(0).masterRetry().equals(request));
            check(restored.validateRetained(resolved) == restored
                && MasterRetryOperation.prepare(resolved, restored) == resolved);
            var paused = LifecycleAdmission.prepare(resolved, request.identity(), new UUID(95, 3), 7,
                RunReceipt.Operation.PAUSE);
            check(restored.validateRetained(paused) == restored
                && MasterRetryOperation.prepare(paused, restored) == paused);
            var resumed = LifecycleAdmission.prepare(paused, request.identity(), new UUID(95, 4), 8,
                RunReceipt.Operation.RESUME);
            check(restored.validateRetained(resumed) == restored);
            rejects(() -> restored.validateAgainst(resolved));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, -1, after, status));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, after.drawCount() + 1, after, status));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation,
                new RunIdentity("foreign", request.identity().runId()), request.commandId(), 5, count, after, status));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 3, count, after, status));
            var changed = MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                new UUID(95, 99), 5, count, after, status);
            rejects(() -> changed.validateRetained(resolved));
            var differentAfter = before.append(before.transactionId(), count, before.currentStreamState(),
                new MasterSelectionProvenance.Step(new DrawCommitment(before.pool(), 1), "changed"));
            var different = MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, count, differentAfter, MasterRetryContinuation.Status.PROPOSED);
            rejects(() -> different.validateRetained(resolved));
            rejects(() -> MasterRetryOperation.prepare(resolved, different));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                creation.transactionId(), 5, count, after, status));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, count, terminal.resolved(), MasterRetryContinuation.Status.PROPOSED));
            rejects(() -> MasterRetryOperationProposal.restoreResponse(creation, request.identity(),
                request.commandId(), 5, count, after, suspended
                    ? MasterRetryContinuation.Status.PROPOSED : MasterRetryContinuation.Status.SUSPENDED));

            // Historical creation lookup only; no unsupported settlement/later command is invented.
            var zero = creation.initial();
            var laterZero = new MasterSelectionProvenance(new UUID(95, 5), zero.snapshotVersion(),
                zero.catalogVersion(), zero.streamDescriptor(), zero.initialStreamState(), zero.readiness(), List.of());
            var laterCreation = new MasterSelectionCreationReceipt(request.identity(), 9, laterZero);
            var history = new MasterSelectionCreationJournal(request.identity(), 10, List.of(creation, laterCreation));
            MasterRetryHistoryBinding.validateCreation(restored, history);
            rejects(() -> MasterRetryHistoryBinding.validateCreation(restored,
                new MasterSelectionCreationJournal(request.identity(), 10, List.of(laterCreation))));
            check(source.lifecycle().revision() == 5 && source.pendingMasterSelection().orElseThrow() == before
                && source.masterSpins().orElseThrow().available() == 7);
        }
        System.out.println("MasterRetryResponseChecks PASSED");
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Historical retry response changed"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid retry response accepted");
    }
}
