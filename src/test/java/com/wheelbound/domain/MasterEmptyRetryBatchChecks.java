package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import static com.wheelbound.domain.MasterSelectionReadiness.*;

/** Offline supplied-draw orchestration checks; no RNG or durable admission claim. */
public final class MasterEmptyRetryBatchChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Retry batch invariant"); } }

    private static void rejects(Class<? extends Throwable> type, Runnable action)
    {
        try { action.run(); }
        catch (Throwable failure)
        {
            if (type.isInstance(failure)) { return; }
            throw new AssertionError(failure);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    private static MasterSelectionProvenance run(MasterSelectionProvenance prefix, int budget,
        Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        return MasterEmptyRetryBatch.propose(prefix, prefix.transactionId(), prefix.drawCount(),
            prefix.currentStreamState(), budget, source);
    }

    public static void main(String[] args)
    {
        for (ActiveWheel.Kind special : new ActiveWheel.Kind[]
            {null, ActiveWheel.Kind.TAINT, ActiveWheel.Kind.SACRIFICE})
        {
            var slices = new ArrayList<ActiveWheel.Slice>();
            var classifications = new HashMap<UUID, Classification>();
            for (int i = 0; i < 5; i++)
            {
                UUID id = new UUID(0, i + 1);
                slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + i, ActiveWheel.Enhancement.values()[0]));
                classifications.put(id, new Classification(i == 4 ? Availability.ASSIGNABLE
                    : Availability.VERIFIED_EMPTY, "reason-" + i));
            }
            if (special != null)
            {
                UUID id = new UUID(0, 6);
                slices.add(ActiveWheel.Slice.special(id, special, 7));
                classifications.put(id, new Classification(Availability.ASSIGNABLE, "mandatory"));
            }
            var start = new MasterSelectionProvenance(UUID.randomUUID(), "snapshot", "catalog",
                "opaque", "repeating-state", new MasterSelectionReadiness(new ActiveWheel(slices),
                    classifications), List.of());
            AtomicInteger calls = new AtomicInteger();
            Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> empty = p -> {
                check(p.drawCount() == calls.getAndIncrement());
                return new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 0), "repeating-state");
            };
            var yielded = run(start, 513, empty);
            check(calls.get() == 513 && yielded.drawCount() == 513 && !yielded.hasValidLanding());
            check(start.drawCount() == 0 && yielded.pool().equals(start.pool()));
            check(yielded.landings().get(512).reason().equals("reason-0"));
            var terminal = run(yielded, 100, p -> {
                check(p == yielded);
                calls.incrementAndGet();
                return new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(),
                    p.pool().totalWeight() - 1), "terminal");
            });
            check(calls.get() == 514 && terminal.drawCount() == 514 && terminal.hasValidLanding());
            check(terminal.landings().get(513).kind() == (special == null
                ? Landing.ORDINARY_ASSIGNMENT : Landing.SPECIAL_OBLIGATION));
            check(terminal.landings().get(0).step() == yielded.landings().get(0).step());
            AtomicInteger mixedCalls = new AtomicInteger();
            var mixed = run(start, 10, p -> new MasterSelectionProvenance.Step(
                new DrawCommitment(p.pool(), mixedCalls.incrementAndGet() < 3
                    ? 0 : p.pool().totalWeight() - 1), "state-" + mixedCalls.get()));
            check(mixedCalls.get() == 3 && mixed.drawCount() == 3 && mixed.hasValidLanding());
            check(mixed.landings().get(1).kind() == Landing.EMPTY_ORDINARY_RETRY);
            check(mixed.currentStreamState().equals("state-3"));
            Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> forbidden = p -> {
                throw new AssertionError("Source called before prefix validation");
            };
            rejects(IllegalStateException.class, () -> run(terminal, 1, forbidden));
            rejects(IllegalArgumentException.class, () -> run(start, 0, forbidden));
            rejects(IllegalArgumentException.class, () -> run(start, -1, forbidden));
            rejects(IllegalArgumentException.class, () -> MasterEmptyRetryBatch.propose(yielded,
                start.transactionId(), 0, "repeating-state", 1, forbidden));
            rejects(IllegalArgumentException.class, () -> MasterEmptyRetryBatch.propose(start,
                UUID.randomUUID(), 0, "repeating-state", 1, forbidden));
            rejects(IllegalArgumentException.class, () -> MasterEmptyRetryBatch.propose(start,
                start.transactionId(), 0, "changed", 1, forbidden));
            rejects(NullPointerException.class, () -> run(start, 1, p -> null));
            rejects(IllegalArgumentException.class, () -> run(start, 1, p ->
                new MasterSelectionProvenance.Step(new DrawCommitment(new WeightedPool("changed",
                    p.pool().entries()), 0), "next")));
            AtomicInteger failures = new AtomicInteger();
            rejects(IllegalStateException.class, () -> run(start, 3, p -> {
                if (failures.incrementAndGet() == 2) { throw new IllegalStateException("source failed"); }
                return new MasterSelectionProvenance.Step(new DrawCommitment(p.pool(), 0), "next");
            }));
            check(start.drawCount() == 0 && yielded.drawCount() == 513);
        }
        System.out.println("Master empty retry batch checks passed");
    }
}
