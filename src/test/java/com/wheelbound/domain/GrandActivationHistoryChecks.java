package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GrandActivationHistoryChecks
{
    private static void check(boolean value)
    { if (!value) { throw new AssertionError("Activation history contract violated"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid activation history accepted");
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("activation-history", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
            GrandFateChecklist.Objective objective = checklist.unfinished().iterator().next();
            UUID command = UUID.randomUUID();
            GrandActivationReceipt first = new GrandActivationReceipt(command, identity, 0, checklist, objective);
            GrandActivationReceipt retry = new GrandActivationReceipt(UUID.randomUUID(), identity, 2, checklist, objective);
            List<GrandActivationReceipt> input = new ArrayList<>(Arrays.asList(first, retry));
            GrandActivationHistory history = new GrandActivationHistory(seal, 3, input);
            input.clear();
            check(history.receipts().size() == 2 && history.seal().equals(seal));
            check(new ArrayList<>(history.receipts().values()).equals(Arrays.asList(first, retry)));
            GrandActivationReceipt restored = new GrandActivationReceipt(command, identity, 0,
                GrandFateChecklist.restore(seal, checklist.completed()), objective);
            check(history.replay(restored, identity, Long.MAX_VALUE) == first);
            check(new GrandActivationHistory(seal, 0, Collections.emptyList()).receipts().isEmpty());
            try { history.receipts().clear(); throw new AssertionError("Mutable history"); }
            catch (UnsupportedOperationException expected) { /* immutable */ }
            rejects(() -> new GrandActivationHistory(seal, -1, Collections.emptyList()));
            rejects(() -> new GrandActivationHistory(seal, 2, Arrays.asList(first, retry)));
            rejects(() -> new GrandActivationHistory(seal, 3, Arrays.asList(retry, first)));
            rejects(() -> new GrandActivationHistory(seal, 3, Arrays.asList(first, first)));
            rejects(() -> new GrandActivationHistory(seal, 3, Arrays.asList(first,
                new GrandActivationReceipt(command, identity, 2, checklist, objective))));
            rejects(() -> new GrandActivationHistory(seal, 3, Arrays.asList(first,
                new GrandActivationReceipt(UUID.randomUUID(), identity, 1, checklist, objective))));
            rejects(() -> new GrandActivationHistory(seal, 3, Collections.singletonList(
                new GrandActivationReceipt(seal.commandId(), identity, 0, checklist, objective))));
            rejects(() -> history.replay(retry, identity, 2));
            rejects(() -> history.replay(first, new RunIdentity("foreign", identity.runId()), 3));
            rejects(() -> history.replay(new GrandActivationReceipt(command, identity, 2, checklist, objective), identity, 3));
            rejects(() -> history.replay(new GrandActivationReceipt(UUID.randomUUID(), identity, 0, checklist, objective), identity, 3));
            SealedGrandFate otherSeal = new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment());
            GrandActivationReceipt foreign = new GrandActivationReceipt(command, identity, 0,
                GrandFateChecklist.initial(otherSeal), objective);
            rejects(() -> new GrandActivationHistory(seal, 3, Collections.singletonList(foreign)));
            rejects(() -> history.replay(foreign, identity, 3));
            for (GrandFateChecklist.Objective different : checklist.unfinished())
            {
                if (different == objective) { continue; }
                GrandFateChecklist advanced = GrandFateChecklist.restore(seal, Collections.singleton(objective));
                GrandActivationReceipt later = new GrandActivationReceipt(UUID.randomUUID(), identity, 4, advanced, different);
                check(new GrandActivationHistory(seal, 5, Arrays.asList(first, retry, later)).receipts().size() == 3);
                rejects(() -> new GrandActivationHistory(seal, 7, Arrays.asList(later,
                    new GrandActivationReceipt(UUID.randomUUID(), identity, 6, checklist, different))));
                rejects(() -> history.replay(new GrandActivationReceipt(command, identity, 0,
                    checklist, different), identity, 3));
                rejects(() -> history.replay(new GrandActivationReceipt(command, identity, 0,
                    GrandFateChecklist.restore(seal, Collections.singleton(different)), objective), identity, 3));
            }
            GrandActivationReceipt last = new GrandActivationReceipt(UUID.randomUUID(), identity,
                Long.MAX_VALUE - 1, checklist, objective);
            check(new GrandActivationHistory(seal, Long.MAX_VALUE, Arrays.asList(first, last))
                .replay(last, identity, Long.MAX_VALUE) == last);
        }
        System.out.println("Grand activation history checks passed");
    }
}
