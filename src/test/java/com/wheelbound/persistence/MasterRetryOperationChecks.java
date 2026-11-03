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
            freshPreparation(head, count);
            for (var availability : MasterSelectionReadiness.Availability.values())
            {
                var request = MasterRetryOperationProposal.prepare(head, new UUID(80, count),
                    MasterRetryOperationProposalChecks.result(head, availability, 0, "retry"));
                var next = MasterRetryOperation.prepare(head, request);
                check(MasterRetryRecovery.find(head, request).isEmpty());
                check(MasterRetryRecovery.find(next, request).get() == request);
                var restored = MasterRetryOperationProposal.restoreResponse(
                    head.masterCreations().get().receipts().get(request.before().transactionId()),
                    request.identity(), request.commandId(), request.beforeRevision(),
                    request.before().drawCount(), request.after(), request.status());
                check(restored != request && MasterRetryRecovery.find(next, restored).get() == request);
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
                check(MasterRetryRecovery.find(paused, restored).get() == request);
                var resumed = LifecycleAdmission.prepare(paused, request.identity(), new UUID(82, count),
                    3, RunReceipt.Operation.RESUME);
                check(MasterRetryOperation.prepare(resumed, request) == resumed);
                var laterRequest = MasterRetryOperationProposal.prepare(resumed, new UUID(83, count),
                    MasterRetryOperationProposalChecks.result(resumed,
                        MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "later"));
                var later = MasterRetryOperation.prepare(resumed, laterRequest);
                check(later.lifecycle().revision() == 5
                    && MasterRetryOperation.prepare(later, request) == later);
                check(MasterRetryRecovery.find(later, restored).get() == request);
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
                rejects(() -> MasterRetryRecovery.find(next, changed));
                var collision = MasterRetryOperationProposal.restoreResponse(
                    head.masterCreations().get().receipts().get(request.before().transactionId()),
                    request.identity(), paused.receipts().get(2).commandId(), request.beforeRevision(),
                    request.before().drawCount(), request.after(), request.status());
                rejects(() -> MasterRetryRecovery.find(paused, collision));
                var absentRequest = MasterRetryOperationProposal.restoreResponse(
                    head.masterCreations().get().receipts().get(request.before().transactionId()),
                    request.identity(), new UUID(99, count), request.beforeRevision(),
                    request.before().drawCount(), request.after(), request.status());
                // An unrelated request is absent, never freshly prepared by recovery.
                check(MasterRetryRecovery.find(head, absentRequest).isEmpty());
                var foreign = new RunDocument(new RunLifecycle(
                    new RunIdentity("foreign", request.identity().runId()), 0,
                    RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "catalog");
                rejects(() -> MasterRetryRecovery.find(foreign, request));
                rejects(() -> MasterRetryOperation.prepare(
                    MasterRetryContinuationChecks.fixture(count + 1), request));
                check(head.lifecycle().revision() == 1 && head.receipts().size() == 1
                    && head.pendingMasterSelection().get().drawCount() == count);
            }
        }
        System.out.println("MasterRetryOperationChecks PASSED");
    }

    private static void freshPreparation(RunDocument head, int count)
    {
        var pending = head.pendingMasterSelection().get();
        var id = head.lifecycle().identity();
        var calls = new java.util.concurrent.atomic.AtomicInteger();
        java.util.function.Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source = p -> {
            calls.incrementAndGet();
            return new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 0), "next");
        };
        for (var availability : MasterSelectionReadiness.Availability.values())
        {
            Map<String, MasterOrdinaryPoolEvidence> pools = new HashMap<>();
            for (int i = 0; i < 5; i++)
            {
                String activity = "activity-" + i;
                pools.put(activity, new MasterOrdinaryPoolEvidence(id, 1, "snapshot", "catalog", activity,
                    Set.of("normal"), Map.of("normal", new MasterSelectionReadiness.Classification(
                        i == 4 ? availability : MasterSelectionReadiness.Availability.VERIFIED_EMPTY,
                        "complete offline fixture"))));
            }
            calls.set(0);
            var next = MasterRetryOperation.prepareFresh(head, id, new UUID(95, count), 1,
                pending.transactionId(), count, "repeated", "snapshot", pools, 2, source);
            boolean continued = availability == MasterSelectionReadiness.Availability.ASSIGNABLE;
            var response = next.receipts().get(1).masterRetry();
            check(calls.get() == (continued ? 2 : 0)
                && next.lifecycle().revision() == 2
                && response.status() == (continued ? MasterRetryContinuation.Status.PROPOSED
                    : MasterRetryContinuation.Status.SUSPENDED)
                && response.after().drawCount() == count + (continued ? 2 : 0)
                && next.fatePoints().equals(head.fatePoints())
                && next.masterSpins().equals(head.masterSpins()));
            if (!continued) { check(response.after() == pending); }
            calls.set(0);
            rejects(() -> MasterRetryOperation.prepareFresh(head, id, head.receipts().get(0).commandId(),
                1, pending.transactionId(), count, "repeated", "snapshot", pools, 2, source));
            rejects(() -> MasterRetryOperation.prepareFresh(head, id, new UUID(96, count),
                0, pending.transactionId(), count, "repeated", "snapshot", pools, 2, source));
            check(calls.get() == 0);
            if (continued)
            {
                rejects(() -> MasterRetryOperation.prepareFresh(head, id, new UUID(97, count), 1,
                    pending.transactionId(), count, "repeated", "snapshot", pools, 2,
                    p -> new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 19), "valid")));
                try
                {
                    MasterRetryOperation.prepareFresh(head, id, new UUID(98, count), 1,
                        pending.transactionId(), count, "repeated", "snapshot", pools, 2,
                        p -> { throw new IllegalStateException("source failed"); });
                    throw new AssertionError("Expected source failure");
                }
                catch (IllegalStateException expected) { }
            }
            check(head.lifecycle().revision() == 1 && head.receipts().size() == 1
                && head.pendingMasterSelection().get() == pending);
        }
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
