package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

public final class MasterRetryOperationChecks
{
    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var head = MasterRetryContinuationChecks.fixture(count);
            for (var availability : MasterSelectionReadiness.Availability.values())
            {
                var request = MasterRetryOperationProposal.prepare(head, new UUID(80, count),
                    MasterRetryOperationProposalChecks.result(head, availability, 0, "retry"));
                var next = MasterRetryOperation.prepare(head, request);
                check(next.lifecycle().revision() == 2 && next.receipts().size() == 2
                    && next.pendingMasterSelection().get() == request.after()
                    && next.masterSpins().equals(head.masterSpins())
                    && next.fatePoints().equals(head.fatePoints())
                    && next.inventory().equals(head.inventory())
                    && next.currentMasterSpecial().isEmpty());
                check(next.receipts().get(1).masterRetry() == request);
                check(MasterRetryOperation.prepare(next, request) == next);
                var paused = LifecycleAdmission.prepare(next, request.identity(), new UUID(81, count),
                    2, RunReceipt.Operation.PAUSE);
                check(MasterRetryOperation.prepare(paused, request) == paused);
                var resumed = LifecycleAdmission.prepare(paused, request.identity(), new UUID(82, count),
                    3, RunReceipt.Operation.RESUME);
                check(MasterRetryOperation.prepare(resumed, request) == resumed);
                var laterRequest = MasterRetryOperationProposal.prepare(resumed, new UUID(83, count),
                    MasterRetryOperationProposalChecks.result(resumed,
                        MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "later"));
                var later = MasterRetryOperation.prepare(resumed, laterRequest);
                check(later.lifecycle().revision() == 5
                    && MasterRetryOperation.prepare(later, request) == later);
                if (request.status() != MasterRetryContinuation.Status.SUSPENDED)
                { rejects(() -> copy(next, head.pendingMasterSelection(), next.masterCreations())); }
                // A suspension has the same transcript, so shortening is only observable for continuations.
                if (request.status() == MasterRetryContinuation.Status.SUSPENDED)
                { check(copy(next, head.pendingMasterSelection(), next.masterCreations()) != null); }
                rejects(() -> copy(next, next.pendingMasterSelection(), Optional.empty()));
                rejects(() -> copy(next, Optional.empty(), next.masterCreations()));
                if (request.status() != MasterRetryContinuation.Status.SUSPENDED)
                {
                    var altered = MasterRetryOperationProposalChecks.result(head,
                        MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "altered-state").prefix();
                    rejects(() -> copy(next, Optional.of(altered), next.masterCreations()));
                }
                var changed = MasterRetryOperationProposal.prepare(head, request.commandId(),
                    MasterRetryOperationProposalChecks.result(head,
                        MasterSelectionReadiness.Availability.ASSIGNABLE, 1, "different"));
                rejects(() -> MasterRetryOperation.prepare(next, changed));
                rejects(() -> MasterRetryOperation.prepare(
                    MasterRetryContinuationChecks.fixture(count + 1), request));
                check(head.lifecycle().revision() == 1 && head.receipts().size() == 1
                    && head.pendingMasterSelection().get().drawCount() == count);
            }
        }
        System.out.println("MasterRetryOperationChecks PASSED");
    }

    private static RunDocument copy(RunDocument head, Optional<MasterSelectionProvenance> pending,
        Optional<MasterSelectionCreationJournal> creations)
    {
        return new RunDocument(head.lifecycle(), head.catalogVersion(), head.receipts(), head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), pending, creations,
            head.masterResolutions(), head.masterSpins(), Optional.empty());
    }
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Retry publication invariant"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected retry publication refusal");
    }
}
