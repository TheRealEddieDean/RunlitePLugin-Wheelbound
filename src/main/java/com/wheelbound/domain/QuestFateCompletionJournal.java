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
     * Validate one ordered historical active context per retained receipt. These
     * must be the contexts at admission, not a reconstruction from today's head.
     * Supplied contexts remain unauthenticated; this check grants no write authority.
     */
    public void requireHistoricalContexts(List<QuestFateCurrentAssignment> historicalContexts)
    {
        List<QuestFateCurrentAssignment> contexts = List.copyOf(historicalContexts);
        if (contexts.size() != receipts.size())
        { throw new IllegalArgumentException("Missing or surplus historical Questing contexts"); }
        for (int index = 0; index < receipts.size(); index++)
        {
            QuestFateCompletionReceipt receipt = receipts.get(index);
            QuestFateCurrentAssignment context = contexts.get(index);
            receipt.requireCurrent(context, context.lifecycle().revision(),
                receipt.window().sessionId(), receipt.window().windowId());
        }
    }

    /**
     * Require a lossless ordered extension before a future aggregate publication.
     * Equality permits replay; truncation, replacement and reordering are refused.
     * This does not check cross-family commands, authenticate evidence or settle FP.
     */
    public void requireExtensionOf(QuestFateCompletionJournal previous)
    {
        Objects.requireNonNull(previous, "previous");
        if (!identity.equals(previous.identity) || receipts.size() < previous.receipts.size()
            || !receipts.subList(0, previous.receipts.size()).equals(previous.receipts))
        { throw new IllegalArgumentException("Changed retained Questing journal prefix"); }
    }

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
