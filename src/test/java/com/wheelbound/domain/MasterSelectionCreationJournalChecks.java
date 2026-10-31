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
                MasterSelectionReadiness.Availability.ASSIGNABLE, "supported"));
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
