package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline preparation only; no detector, storage or session verification. */
public final class MasterRetryContinuationChecks
{
    private static final RunIdentity ID = new RunIdentity("retry", new UUID(1, 9));

    private static Map<String, MasterOrdinaryPoolEvidence> pools(String snapshot, Availability last)
    {
        Map<String, MasterOrdinaryPoolEvidence> result = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            String activity = "activity-" + i;
            result.put(activity, new MasterOrdinaryPoolEvidence(ID, 1, snapshot, "catalog", activity,
                Set.of("normal"), Map.of("normal", new Classification(i == 4 ? last
                    : Availability.VERIFIED_EMPTY, "complete offline fixture"))));
        }
        return result;
    }

    static RunDocument fixture(int count)
    {
        var slices = new ArrayList<ActiveWheel.Slice>();
        var history = new HashMap<String, SliceInventory.AcquisitionHistory>();
        for (int i = 0; i < 5; i++)
        {
            slices.add(ActiveWheel.Slice.ordinary(new UUID(0, i + 1), "activity-" + i, ActiveWheel.Enhancement.BASE));
            history.put("activity-" + i, new SliceInventory.AcquisitionHistory(0));
        }
        var inventory = new SliceInventory(new ActiveWheel(slices), List.of(), SliceInventory.SatchelTier.BASE,
            history.keySet(), history);
        var ready = MasterWheelEvidence.assemble(ID, 1, "snapshot", "catalog", inventory,
            pools("snapshot", Availability.ASSIGNABLE));
        var initial = new MasterSelectionProvenance(new UUID(4, 5), "snapshot", "catalog", "opaque",
            "repeated", ready, List.of());
        var creation = new MasterSelectionCreationReceipt(ID, 0, initial);
        var prefix = initial;
        for (int i = 0; i < count; i++)
        { prefix = prefix.append(prefix.transactionId(), prefix.drawCount(), "repeated",
            new MasterSelectionProvenance.Step(new DrawCommitment(prefix.pool(), 0), "repeated")); }
        return new RunDocument(new RunLifecycle(ID, 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            "catalog", List.of(RunReceipt.masterSelectionCreation(creation)), Optional.empty(), Optional.of(inventory),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(prefix),
            Optional.of(new MasterSelectionCreationJournal(ID, 1, List.of(creation))));
    }

    public static void main(String[] args)
    {
        for (int count : new int[] {0, 1, 513})
        {
            var current = fixture(count);
            var pending = current.pendingMasterSelection().get();
            AtomicInteger calls = new AtomicInteger();
            java.util.function.Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source = p -> {
                calls.incrementAndGet();
                return new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 19), "valid");
            };
            var result = MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source);
            check(result.status() == MasterRetryContinuation.Status.PROPOSED && calls.get() == 1);
            check(result.prefix().drawCount() == count + 1 && result.prefix().hasValidLanding());
            for (Availability status : Availability.values())
            {
                if (status == Availability.ASSIGNABLE) { continue; }
                var stopped = MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                    "repeated", "snapshot", pools("snapshot", status), 3, source);
                check(stopped.status() == MasterRetryContinuation.Status.SUSPENDED && stopped.prefix() == pending);
            }
            var changed = MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "changed", pools("changed", Availability.ASSIGNABLE), 3, source);
            check(changed.status() == MasterRetryContinuation.Status.SUSPENDED && changed.prefix() == pending);
            var changedReady = new HashMap<>(pools("snapshot", Availability.ASSIGNABLE));
            changedReady.put("activity-0", new MasterOrdinaryPoolEvidence(ID, 1, "snapshot", "catalog",
                "activity-0", Set.of("normal"), Map.of("normal", new Classification(Availability.ASSIGNABLE,
                    "newly available"))));
            var reconcile = MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", changedReady, 3, source);
            check(reconcile.status() == MasterRetryContinuation.Status.SUSPENDED && reconcile.prefix() == pending);
            var paused = LifecycleAdmission.prepare(current, ID, new UUID(12, count), 1, RunReceipt.Operation.PAUSE);
            rejects(() -> MasterRetryContinuation.prepare(paused, ID, 2, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, new RunIdentity("foreign", ID.runId()), 1,
                pending.transactionId(), count, "repeated", "snapshot",
                pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 0, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, UUID.randomUUID(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count + 1,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "wrong", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", Map.of(), 3, source));
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 0, source));
            check(calls.get() == 1 && current.pendingMasterSelection().get() == pending
                && pending.drawCount() == count && current.lifecycle().revision() == 1);
            var yielded = MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 2,
                p -> new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 0), "repeated"));
            check(yielded.status() == MasterRetryContinuation.Status.PROPOSED
                && yielded.prefix().drawCount() == count + 2 && !yielded.prefix().hasValidLanding());
            rejects(() -> MasterRetryContinuation.prepare(current, ID, 1, pending.transactionId(), count,
                "repeated", "snapshot", pools("snapshot", Availability.ASSIGNABLE), 3,
                p -> { throw new IllegalArgumentException("source failed"); }));
            check(current.pendingMasterSelection().get() == pending && pending.drawCount() == count);
        }
        System.out.println("MasterRetryContinuationChecks PASSED");
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError("Retry continuation invariant"); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected refusal before source");
    }
}
