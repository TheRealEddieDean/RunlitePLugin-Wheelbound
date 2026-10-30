package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class GrandFateProgressChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Grand progress contract violated"); } }

    private static void rejects(Runnable action, Class<? extends RuntimeException> type)
    {
        try { action.run(); }
        catch (RuntimeException error)
        { if (type.isInstance(error)) { return; } throw error; }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("progress-account", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(),
                GrandFatePool.atTicket(ticket));
            GrandFateProgress empty = new GrandFateProgress(seal, 0, Collections.emptyList());
            check(empty.evidence().isEmpty() && empty.checklist().completed().isEmpty());
            List<GrandFateProgress.Evidence> references = new ArrayList<>();
            long revision = 0;
            for (GrandFateChecklist.Objective objective : empty.checklist().required())
            {
                GrandFateProgress.Evidence reference = new GrandFateProgress.Evidence(
                    seal, objective, UUID.randomUUID(), ++revision);
                references.add(reference);
                GrandFateProgress progress = new GrandFateProgress(seal, revision, references);
                check(progress.checklist().completed().size() == references.size());
                check(progress.evidence().get(objective).equals(reference));
                check(reference.equals(new GrandFateProgress.Evidence(seal, objective,
                    reference.receiptId(), revision)));
                check(reference.hashCode() == new GrandFateProgress.Evidence(seal, objective,
                    reference.receiptId(), revision).hashCode());
                check(!reference.equals(new GrandFateProgress.Evidence(seal, objective,
                    reference.receiptId(), revision + 1)));
                final long before = revision - 1;
                rejects(() -> new GrandFateProgress(seal, before, references), IllegalArgumentException.class);
                rejects(() -> progress.evidence().clear(), UnsupportedOperationException.class);
            }
            GrandFateProgress full = new GrandFateProgress(seal, Long.MAX_VALUE, references);
            check(full.checklist().isComplete());
            GrandFateProgress.Evidence first = references.get(0);
            SealedGrandFate reconstructed = new SealedGrandFate(identity, seal.commandId(),
                GrandFatePool.atTicket(ticket));
            check(new GrandFateProgress(reconstructed, Long.MAX_VALUE,
                Collections.singletonList(first)).checklist().completed().contains(first.objective()));
            GrandFateProgress.Evidence boundary = new GrandFateProgress.Evidence(seal,
                first.objective(), UUID.randomUUID(), Long.MAX_VALUE);
            check(new GrandFateProgress(seal, Long.MAX_VALUE, Collections.singletonList(boundary))
                .evidence().get(first.objective()).verifiedRevision() == Long.MAX_VALUE);
            rejects(() -> new GrandFateProgress(seal, Long.MAX_VALUE - 1,
                Collections.singletonList(boundary)), IllegalArgumentException.class);
            rejects(() -> new GrandFateProgress(seal, Long.MAX_VALUE,
                Arrays.asList(first, first)), IllegalArgumentException.class);
            rejects(() -> new GrandFateProgress(seal, Long.MAX_VALUE, Arrays.asList(first,
                new GrandFateProgress.Evidence(seal, first.objective(), UUID.randomUUID(), 1))),
                IllegalArgumentException.class);
            if (references.size() > 1)
            {
                rejects(() -> new GrandFateProgress(seal, Long.MAX_VALUE, Arrays.asList(first,
                    new GrandFateProgress.Evidence(seal, references.get(1).objective(),
                        first.receiptId(), 2))), IllegalArgumentException.class);
            }
            references.clear();
            check(full.checklist().isComplete() && !full.evidence().isEmpty());
            check(empty.checklist().completed().isEmpty());
            SealedGrandFate resealed = new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment());
            SealedGrandFate foreign = new SealedGrandFate(
                new RunIdentity("other-account", identity.runId()), seal.commandId(), seal.commitment());
            SealedGrandFate otherRun = new SealedGrandFate(
                new RunIdentity("progress-account", UUID.randomUUID()), seal.commandId(), seal.commitment());
            for (SealedGrandFate wrong : Arrays.asList(resealed, foreign, otherRun))
            {
                rejects(() -> new GrandFateProgress(wrong, Long.MAX_VALUE,
                    Collections.singletonList(first)), IllegalArgumentException.class);
            }
            for (GrandFateChecklist.Objective objective : GrandFateChecklist.Objective.values())
            {
                if (!empty.checklist().required().contains(objective))
                { rejects(() -> new GrandFateProgress.Evidence(seal, objective, UUID.randomUUID(), 1),
                    IllegalArgumentException.class); }
            }
            rejects(() -> new GrandFateProgress.Evidence(seal, first.objective(), seal.commandId(), 1),
                IllegalArgumentException.class);
            for (long invalid : new long[] {0, -1, Long.MIN_VALUE})
            { rejects(() -> new GrandFateProgress.Evidence(seal, first.objective(), UUID.randomUUID(), invalid),
                IllegalArgumentException.class); }
            rejects(() -> new GrandFateProgress(seal, -1, Collections.emptyList()), IllegalArgumentException.class);
            rejects(() -> new GrandFateProgress(seal, 1, null), NullPointerException.class);
            rejects(() -> new GrandFateProgress(seal, 1, Collections.singletonList(null)), NullPointerException.class);
            rejects(() -> new GrandFateProgress.Evidence(null, first.objective(), UUID.randomUUID(), 1), NullPointerException.class);
            rejects(() -> new GrandFateProgress.Evidence(seal, null, UUID.randomUUID(), 1), NullPointerException.class);
            rejects(() -> new GrandFateProgress.Evidence(seal, first.objective(), null, 1), NullPointerException.class);
        }
        rejects(() -> new GrandFateProgress(null, 0, Collections.emptyList()), NullPointerException.class);
        System.out.println("Grand Fate progress checks passed");
    }
}
