package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable, run-scoped supplied completion journal. This is an in-memory
 * collision check, not authenticated evidence, durable consumption or settlement.
 * A future aggregate must preserve this prefix and publish receipts and rewards
 * atomically, including command uniqueness across other command families.
 */
public final class QuestFateCompletionJournal
{
    private final RunIdentity identity;
    private final List<QuestFateCompletionReceipt> receipts;

    public QuestFateCompletionJournal(RunIdentity identity,
        Collection<QuestFateCompletionReceipt> payloads)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        List<QuestFateCompletionReceipt> retained = new ArrayList<>();
        for (QuestFateCompletionReceipt receipt : Objects.requireNonNull(payloads, "payloads"))
        {
            requireIdentity(receipt);
            for (QuestFateCompletionReceipt prior : retained)
            { requireDistinct(prior, receipt); }
            retained.add(receipt);
        }
        receipts = List.copyOf(retained);
    }

    public RunIdentity identity() { return identity; }
    public List<QuestFateCompletionReceipt> receipts() { return receipts; }

    /**
     * Exact replay returns this journal before checking the live head. New requests
     * must match the supplied active head and live IDs. Caller authenticates them.
     * Replay acknowledges retained data only; it must never trigger another payout.
     */
    public QuestFateCompletionJournal append(QuestFateCompletionReceipt request,
        QuestFateCurrentAssignment current, long revision, UUID sessionId, UUID windowId)
    {
        requireIdentity(request);
        for (QuestFateCompletionReceipt prior : receipts)
        {
            if (prior.commandId().equals(request.commandId()))
            {
                if (prior.equals(request)) { return this; }
                throw new IllegalArgumentException("Changed Questing completion command");
            }
            requireDistinct(prior, request);
        }
        request.requireCurrent(current, revision, sessionId, windowId);
        List<QuestFateCompletionReceipt> next = new ArrayList<>(receipts);
        next.add(request);
        return new QuestFateCompletionJournal(identity, next);
    }

    private void requireIdentity(QuestFateCompletionReceipt receipt)
    {
        if (!identity.equals(Objects.requireNonNull(receipt, "receipt").assignment().identity()))
        { throw new IllegalArgumentException("Foreign Questing completion run"); }
    }

    private static void requireDistinct(QuestFateCompletionReceipt prior,
        QuestFateCompletionReceipt receipt)
    {
        if (prior.commandId().equals(receipt.commandId()) || prior.sameSourceEvent(receipt)
            || prior.assignment().assignmentId().equals(receipt.assignment().assignmentId()))
        { throw new IllegalArgumentException("Repeated Questing command, source or assignment"); }
    }
}
