package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSelectionResolutions;
import java.util.Objects;
import java.util.UUID;

/** Structural prerequisite for future aggregate retention; never publishes an assignment or debit. */
public final class MasterResolutionBinding
{
    private MasterResolutionBinding() { }

    /** Validate historical transcripts against one supplied aggregate, without changing it. */
    public static void validateHistory(RunDocument head, MasterSelectionResolutions history)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(history, "history");
        if (!head.lifecycle().identity().equals(history.identity()))
        { throw new IllegalArgumentException("Foreign resolution history"); }
        new MasterSelectionResolutions(history.identity(), head.lifecycle().revision(), history.receipts().values());
        var creations = head.masterCreations().orElseThrow(() ->
            new IllegalArgumentException("Unavailable original Master history"));
        for (MasterSelectionResolutionReceipt receipt : history.receipts().values())
        {
            creations.replay(receipt.creation(), history.identity(), head.lifecycle().revision());
            if (creations.receipts().values().stream().anyMatch(c ->
                c.beforeRevision() > receipt.creation().beforeRevision()
                    && c.beforeRevision() < receipt.afterRevision()))
            { throw new IllegalArgumentException("Later creation precedes terminal resolution"); }
            rejectCollision(head, receipt.commandId());
            if (head.receipts().stream().anyMatch(r ->
                r.beforeRevision() < receipt.afterRevision() && r.afterRevision() > receipt.beforeRevision()))
            { throw new IllegalArgumentException("Resolution overlaps another aggregate operation"); }
            if (head.pendingMasterSelection().filter(p -> p.transactionId().equals(receipt.transactionId())).isPresent())
            { throw new IllegalArgumentException("Resolved selection is still pending"); }
        }
    }

    /** Check a proposed terminal append against the actual supplied unresolved prefix.
     * Storage freshness, trusted evidence, accounting and atomic publication remain required.
     */
    public static void validateAppend(RunDocument head, MasterSelectionResolutions history,
        MasterSelectionResolutionReceipt proposed)
    {
        validateHistory(head, history);
        Objects.requireNonNull(proposed, "proposed");
        if (head.lifecycle().state() != com.wheelbound.domain.RunLifecycle.State.ACTIVE
            || proposed.beforeRevision() != head.lifecycle().revision())
        { throw new IllegalArgumentException("Terminal append requires current active head"); }
        var pending = head.pendingMasterSelection().orElseThrow(() ->
            new IllegalArgumentException("No pending Master selection"));
        var original = head.masterCreations().get().matchPending(pending,
            head.lifecycle().identity(), head.lifecycle().revision());
        rejectCollision(head, proposed.commandId());
        var terminal = proposed.resolved().landings().get(proposed.resolved().drawCount() - 1).step();
        if (!proposed.equals(MasterSelectionResolutionReceipt.prepare(proposed.commandId(),
            head.lifecycle().revision(), original, pending, terminal)))
        { throw new IllegalArgumentException("Terminal transcript changed durable prefix"); }
        var combined = new java.util.ArrayList<>(history.receipts().values());
        combined.add(proposed);
        new MasterSelectionResolutions(head.lifecycle().identity(), proposed.afterRevision(), combined);
    }

    private static void rejectCollision(RunDocument head, UUID command)
    {
        if (head.masterCreations().get().receipts().containsKey(command)
            || head.receipts().stream().anyMatch(r -> r.commandId().equals(command))
            || head.grandFate().filter(s -> s.commandId().equals(command)).isPresent()
            || head.grandActivations().filter(h -> h.receipts().containsKey(command)).isPresent()
            || head.grandProgress().filter(p -> p.evidence().values().stream()
                .anyMatch(e -> e.receiptId().equals(command))).isPresent())
        { throw new IllegalArgumentException("Resolution command collides with retained history"); }
    }
}
