package com.wheelbound.domain;

import java.util.Collections;
import java.util.UUID;

/** Executable without client or persistence dependencies. */
public final class GrandProgressExtensionChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Grand progress extension contract violated"); } }

    private static void rejects(Runnable action, Class<? extends RuntimeException> type)
    {
        try { action.run(); }
        catch (RuntimeException error)
        { if (type.isInstance(error)) { return; } throw error; }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args)
    {
        RunIdentity identity = new RunIdentity("extension-account", UUID.randomUUID());
        for (int ticket = 0; ticket < 5; ticket++)
        {
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(),
                GrandFatePool.atTicket(ticket));
            GrandFateProgress progress = new GrandFateProgress(seal, 0, Collections.emptyList());
            long revision = 0;
            for (GrandFateChecklist.Objective objective : progress.checklist().required())
            {
                GrandFateProgress prefix = progress;
                long before = revision;
                GrandFateProgress.Evidence reference = new GrandFateProgress.Evidence(
                    seal, objective, UUID.randomUUID(), revision + 1);
                rejects(() -> prefix.appendReference(reference, before + 1), IllegalArgumentException.class);
                rejects(() -> prefix.appendReference(new GrandFateProgress.Evidence(
                    seal, objective, UUID.randomUUID(), before + 2), before), IllegalArgumentException.class);
                if (!prefix.evidence().isEmpty())
                {
                    UUID reused = prefix.evidence().values().iterator().next().receiptId();
                    rejects(() -> prefix.appendReference(new GrandFateProgress.Evidence(
                        seal, objective, reused, before + 1), before), IllegalArgumentException.class);
                }
                progress = prefix.appendReference(reference, revision++);
                check(prefix.checklist().isUnfinished(objective));
                check(progress.evidence().size() == prefix.evidence().size() + 1);
                for (GrandFateProgress.Evidence retained : prefix.evidence().values())
                { check(progress.evidence().get(retained.objective()).equals(retained)); }
                check(progress.appendReference(reference, revision) == progress);
                check(progress.appendReference(new GrandFateProgress.Evidence(seal, objective,
                    reference.receiptId(), reference.verifiedRevision()), revision + 10) == progress);
                GrandFateProgress extended = progress;
                long current = revision;
                rejects(() -> extended.appendReference(reference, before), IllegalArgumentException.class);
                rejects(() -> extended.appendReference(new GrandFateProgress.Evidence(seal,
                    objective, UUID.randomUUID(), current + 1), current), IllegalArgumentException.class);
                rejects(() -> extended.appendReference(new GrandFateProgress.Evidence(seal,
                    objective, reference.receiptId(), current + 1), current), IllegalArgumentException.class);
                SealedGrandFate[] wrongSeals = {
                    new SealedGrandFate(identity, UUID.randomUUID(), seal.commitment()),
                    new SealedGrandFate(new RunIdentity("other", identity.runId()),
                        seal.commandId(), seal.commitment()),
                    new SealedGrandFate(new RunIdentity(identity.accountKey(), UUID.randomUUID()),
                        seal.commandId(), seal.commitment())
                };
                for (SealedGrandFate wrong : wrongSeals)
                {
                    rejects(() -> extended.appendReference(new GrandFateProgress.Evidence(
                        wrong, objective, UUID.randomUUID(), current + 1), current), IllegalArgumentException.class);
                }
                rejects(() -> prefix.appendReference(null, before), NullPointerException.class);
                rejects(() -> prefix.appendReference(reference, -1), IllegalArgumentException.class);
                check(extended.appendReference(reference, Long.MAX_VALUE) == extended);
            }
            check(progress.checklist().isComplete());
            GrandFateChecklist.Objective objective = progress.checklist().required().iterator().next();
            GrandFateProgress empty = new GrandFateProgress(seal, 0, Collections.emptyList());
            GrandFateProgress.Evidence last = new GrandFateProgress.Evidence(
                seal, objective, UUID.randomUUID(), Long.MAX_VALUE);
            GrandFateProgress atLimit = empty.appendReference(last, Long.MAX_VALUE - 1);
            check(atLimit.appendReference(last, Long.MAX_VALUE) == atLimit);
            rejects(() -> empty.appendReference(last, Long.MAX_VALUE), ArithmeticException.class);
        }
        System.out.println("Grand progress extension checks passed");
    }
}
