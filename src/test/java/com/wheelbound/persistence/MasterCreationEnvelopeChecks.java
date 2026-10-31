package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Executable typed-response contracts; no persistence or admission claim. */
public final class MasterCreationEnvelopeChecks
{
    public static void main(String[] args)
    {
        RunDocument source = MasterCreationAggregateChecks.fixture(0);
        MasterSelectionCreationReceipt original = source.masterCreations().get().receipts()
            .values().iterator().next();
        for (long revision : new long[] {0, 3, Long.MAX_VALUE - 1})
        {
            MasterSelectionCreationReceipt request = new MasterSelectionCreationReceipt(
                original.identity(), revision, original.initial());
            RunReceipt envelope = RunReceipt.masterSelectionCreation(request);
            RunReceipt copy = RunReceipt.masterSelectionCreation(new MasterSelectionCreationReceipt(
                request.identity(), revision, request.initial()));
            check(envelope.equals(copy) && copy.equals(envelope)
                && envelope.hashCode() == copy.hashCode());
            check(envelope.masterCreation() == request
                && envelope.commandId().equals(request.transactionId())
                && envelope.beforeRevision() == revision && envelope.afterRevision() == revision + 1
                && envelope.state() == RunLifecycle.State.ACTIVE
                && envelope.obligation() == RunLifecycle.Obligation.NONE
                && envelope.operation() == RunReceipt.Operation.MASTER_SELECTION_CREATION);
            check(envelope.draw() == null && envelope.debit() == null
                && envelope.purchase() == null && envelope.activation() == null
                && request.initial().drawCount() == 0);
            rejects(() -> new RunReceipt(request.transactionId(), envelope.operation(), revision,
                revision + 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
            rejects(() -> new RunDocument(new RunLifecycle(request.identity(), revision + 1,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "catalog", List.of(envelope)));
            check(!envelope.equals(RunReceipt.masterSelectionCreation(new MasterSelectionCreationReceipt(
                new RunIdentity("foreign", request.identity().runId()), revision, request.initial()))));
            MasterSelectionProvenance p = request.initial();
            check(!envelope.equals(RunReceipt.masterSelectionCreation(new MasterSelectionCreationReceipt(
                request.identity(), revision, new MasterSelectionProvenance(p.transactionId(), "changed",
                    p.catalogVersion(), p.streamDescriptor(), p.initialStreamState(), p.readiness(), List.of())))));
            RunReceipt pause = new RunReceipt(request.transactionId(), RunReceipt.Operation.PAUSE,
                revision, revision + 1, RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            check(!envelope.equals(pause) && pause.masterCreation() == null);
        }
        rejects(() -> RunReceipt.masterSelectionCreation(null));
        check(source.masterCreations().get().receipts().size() == 1
            && source.pendingMasterSelection().get().drawCount() == 0);
        System.out.println("MasterCreationEnvelopeChecks PASSED");
    }

    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Invalid Master creation envelope"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Unsupported envelope admitted");
    }
}
