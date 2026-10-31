package com.wheelbound.domain;

import java.util.*;

/** Dependency-free retained-journal validation, not a persistence test. */
public final class MasterSelectionCreationJournalChecks
{
    private static final RunIdentity ID = new RunIdentity("journal", new UUID(1, 1));

    public static void main(String[] args)
    {
        MasterSelectionCreationReceipt first = receipt(ID, 0, 1, "state");
        MasterSelectionCreationReceipt next = receipt(ID, 1, 2, "state");
        MasterSelectionCreationReceipt later = receipt(ID, 8, 3, "state");
        List<MasterSelectionCreationReceipt> input = new ArrayList<>(List.of(first, next, later));
        MasterSelectionCreationJournal journal = new MasterSelectionCreationJournal(ID, 9, input);
        input.clear();
        if (!new ArrayList<>(journal.receipts().values()).equals(List.of(first, next, later))
            || journal.replay(receipt(ID, 0, 1, "state"), ID, 100) != first
            || !journal.identity().equals(ID))
        { throw new AssertionError("Original ordered history lost"); }
        try { journal.receipts().clear(); throw new AssertionError("Mutable journal"); }
        catch (UnsupportedOperationException expected) { }
        rejects(() -> journal.replay(receipt(ID, 0, 1, "changed"), ID, 100));
        rejects(() -> journal.replay(receipt(ID, 1, 1, "state"), ID, 100));
        rejects(() -> journal.replay(receipt(ID, 0, 4, "state"), ID, 100));
        RunIdentity foreign = new RunIdentity("foreign", ID.runId());
        rejects(() -> journal.replay(first, foreign, 100));
        rejects(() -> journal.replay(receipt(foreign, 0, 1, "state"), ID, 100));
        rejects(() -> journal.replay(first, ID, 8));
        rejects(() -> journal.replay(null, ID, 100));
        rejects(() -> journal.replay(first, null, 100));
        rejects(() -> new MasterSelectionCreationJournal(ID, -1, List.of()));
        rejects(() -> new MasterSelectionCreationJournal(null, 0, List.of()));
        rejects(() -> new MasterSelectionCreationJournal(ID, 0, null));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9, Arrays.asList(first, null)));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9, List.of(first, first)));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9, List.of(next, first)));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9,
            List.of(first, receipt(ID, 0, 2, "state"))));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9,
            List.of(first, receipt(ID, 4, 1, "state"))));
        rejects(() -> new MasterSelectionCreationJournal(ID, 9,
            List.of(receipt(foreign, 0, 1, "state"))));
        new MasterSelectionCreationJournal(ID, 0, List.of());
        new MasterSelectionCreationJournal(ID, Long.MAX_VALUE,
            List.of(receipt(ID, Long.MAX_VALUE - 1, 1, "state")));
        MasterSelectionCreationJournal single = new MasterSelectionCreationJournal(ID, 1, List.of(first));
        MasterSelectionProvenance prefix = first.initial();
        if (single.matchPending(prefix, ID, 1) != first)
        { throw new AssertionError("Zero-draw pending creation lost"); }
        for (int i = 0; i < 513; i++)
        {
            prefix = prefix.append(prefix.transactionId(), i, prefix.currentStreamState(),
                new MasterSelectionProvenance.Step(new DrawCommitment(prefix.pool(), 0), "advanced"));
        }
        MasterSelectionProvenance retained = prefix;
        if (single.matchPending(retained, ID, 100) != first || first.initial().drawCount() != 0)
        { throw new AssertionError("Advanced pending replaced original"); }
        rejects(() -> single.matchPending(retained, foreign, 100));
        rejects(() -> single.matchPending(retained, ID, 0));
        rejects(() -> single.matchPending(null, ID, 1));
        rejects(() -> journal.matchPending(first.initial(), ID, 100));
        rejects(() -> new MasterSelectionCreationJournal(ID, 100, List.of())
            .matchPending(retained, ID, 100));
        rejects(() -> single.matchPending(receipt(ID, 0, 1, "changed").initial(), ID, 100));
        Map<UUID, MasterSelectionReadiness.Classification> changedReasons =
            new HashMap<>(first.initial().readiness().classifications());
        changedReasons.put(new UUID(0, 1), new MasterSelectionReadiness.Classification(
            MasterSelectionReadiness.Availability.VERIFIED_EMPTY, "different reason"));
        MasterSelectionProvenance changedEvidence = new MasterSelectionProvenance(first.transactionId(),
            "snapshot", "catalog", "stream", "state", new MasterSelectionReadiness(
                first.initial().readiness().wheel(), changedReasons), List.of());
        rejects(() -> single.matchPending(changedEvidence, ID, 100));
        for (int field = 0; field < 4; field++)
        {
            MasterSelectionProvenance changed = new MasterSelectionProvenance(first.transactionId(),
                field == 0 ? "changed" : "snapshot", field == 1 ? "changed" : "catalog",
                field == 2 ? "changed" : "stream", "state",
                field == 3 ? next.initial().readiness() : first.initial().readiness(), List.of());
            // The last case uses equal frozen declarations and must match by value.
            if (field == 3) { single.matchPending(changed, ID, 100); }
            else { rejects(() -> single.matchPending(changed, ID, 100)); }
        }
        MasterSelectionProvenance resolved = retained.append(retained.transactionId(), 513, "advanced",
            new MasterSelectionProvenance.Step(new DrawCommitment(retained.pool(),
                retained.pool().totalWeight() - 1), "resolved"));
        rejects(() -> single.matchPending(resolved, ID, 100));
        if (single.replay(first, ID, 100) != first || retained.drawCount() != 513)
        { throw new AssertionError("Matching mutated history"); }
        System.out.println("MasterSelectionCreationJournalChecks PASSED");
    }

    private static MasterSelectionCreationReceipt receipt(RunIdentity identity, long revision,
        long transaction, String state)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, "activity-" + i % 3, ActiveWheel.Enhancement.BASE));
            classifications.put(id, new MasterSelectionReadiness.Classification(
                i == 0 ? MasterSelectionReadiness.Availability.VERIFIED_EMPTY
                    : MasterSelectionReadiness.Availability.ASSIGNABLE, "supported"));
        }
        return new MasterSelectionCreationReceipt(identity, revision,
            new MasterSelectionProvenance(new UUID(2, transaction), "snapshot", "catalog",
                "stream", state, new MasterSelectionReadiness(new ActiveWheel(slices), classifications),
                List.of()));
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid journal accepted");
    }
}
