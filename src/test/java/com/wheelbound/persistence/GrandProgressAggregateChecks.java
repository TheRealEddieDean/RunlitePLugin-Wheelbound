package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Executable aggregate contract checks; no codec dependencies required. */
public final class GrandProgressAggregateChecks
{
    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected invalid aggregate rejection");
    }
    private static RunDocument document(RunIdentity id, long revision, SealedGrandFate seal,
        GrandFateProgress progress, List<RunReceipt> receipts)
    {
        return new RunDocument(new RunLifecycle(id, revision, RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.NONE), "test", receipts, Optional.empty(), Optional.empty(),
            Optional.ofNullable(seal), Optional.ofNullable(progress));
    }
    public static void main(String[] args)
    {
        RunIdentity id = new RunIdentity("aggregate-account", UUID.randomUUID());
        check(!document(id, 0, null, null, List.of()).grandProgress().isPresent());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateProgress empty = new GrandFateProgress(seal, 0, List.of());
            check(document(id, 0, seal, empty, List.of()).grandProgress().get().evidence().isEmpty());
            rejects(() -> document(id, 0, null, empty, List.of()));
            SealedGrandFate changed = new SealedGrandFate(id, UUID.randomUUID(), seal.commitment());
            rejects(() -> document(id, 0, changed, empty, List.of()));
            UUID receiptId = UUID.randomUUID();
            GrandFateProgress.Evidence reference = new GrandFateProgress.Evidence(seal,
                empty.checklist().required().iterator().next(), receiptId, 2);
            GrandFateProgress progress = new GrandFateProgress(seal, 2, List.of(reference));
            rejects(() -> document(id, 1, seal, progress, List.of()));
            RunDocument restored = document(id, 2, seal, progress, List.of());
            check(restored.grandProgress().get().evidence().get(reference.objective()).equals(reference));
            check(document(id, Long.MAX_VALUE, seal, progress, List.of()).grandProgress().isPresent());
            GrandEvidenceReceipt payload = new GrandEvidenceReceipt(seal, reference.objective(),
                receiptId, 1, UUID.randomUUID(), "fixture", "event");
            GrandEvidenceJournal journal = new GrandEvidenceJournal(progress, 2, List.of(payload));
            RunLifecycle head = new RunLifecycle(id, 2, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            RunDocument full = new RunDocument(head, "test", List.of(), Optional.empty(), Optional.empty(),
                Optional.of(seal), Optional.of(progress), Optional.of(journal));
            check(full.grandJournal().get().receipts().equals(journal.receipts()));
            rejects(() -> new RunDocument(head, "test", List.of(), Optional.empty(), Optional.empty(),
                Optional.of(seal), Optional.empty(), Optional.of(journal)));
            rejects(() -> new RunDocument(head, "test", List.of(), Optional.empty(), Optional.empty(),
                Optional.of(seal), Optional.of(empty), Optional.of(journal)));
            RunReceipt collision = new RunReceipt(receiptId, RunReceipt.Operation.PAUSE, 1, 2,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            rejects(() -> document(id, 2, seal, progress, List.of(collision)));
            RunReceipt unrelated = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 1, 2,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            check(document(id, 2, seal, progress, List.of(unrelated)).grandProgress().isPresent());
            RunIdentity foreign = new RunIdentity("other", id.runId());
            rejects(() -> document(foreign, 2, seal, progress, List.of()));
        }
    }
}
