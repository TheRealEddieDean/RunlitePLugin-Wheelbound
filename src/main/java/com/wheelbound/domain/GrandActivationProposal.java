package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Combined, unwired activation proposal (WB-D089/104). The caller must admit the
 * session, reject pending draws/offers and global command collisions, and persist
 * the complete aggregate atomically. This value neither charges spins nor commits.
 */
public final class GrandActivationProposal
{
    private final RunLifecycle lifecycle;
    private final GrandActivationHistory history;
    private final Optional<GrandCurrentAttempt> currentAttempt;
    private final GrandActivationReceipt receipt;
    private final boolean replay;

    private GrandActivationProposal(RunLifecycle lifecycle, GrandActivationHistory history,
        Optional<GrandCurrentAttempt> currentAttempt, GrandActivationReceipt receipt, boolean replay)
    {
        this.lifecycle = lifecycle;
        this.history = history;
        this.currentAttempt = currentAttempt;
        this.receipt = receipt;
        this.replay = replay;
    }

    /** Exact replay preserves the supplied current state, even after the attempt ended. */
    public static GrandActivationProposal propose(RunLifecycle current,
        GrandFateChecklist checklist, GrandActivationHistory history,
        Optional<GrandCurrentAttempt> currentAttempt, GrandActivationReceipt request,
        RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(checklist, "checklist");
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(currentAttempt, "currentAttempt");
        Objects.requireNonNull(request, "request");
        if (!current.identity().equals(expectedIdentity)
            || current.revision() != expectedRevision
            || !history.seal().equals(checklist.seal())
            || !current.identity().equals(history.seal().identity()))
        { throw new IllegalArgumentException("Foreign or stale activation context"); }
        new GrandActivationHistory(history.seal(), current.revision(), history.receipts().values());
        if (currentAttempt.isPresent())
        {
            GrandCurrentAttempt supplied = currentAttempt.get();
            GrandCurrentAttempt restored = GrandCurrentAttempt.restore(
                supplied.activation().commandId(), history, current, checklist);
            if (!restored.activation().equals(supplied.activation())
                || supplied.lifecycle().revision() != current.revision()
                || supplied.lifecycle().state() != current.state()
                || supplied.lifecycle().obligation() != current.obligation()
                || !supplied.lifecycle().identity().equals(current.identity()))
            { throw new IllegalArgumentException("Inconsistent current attempt"); }
        }
        if (history.receipts().containsKey(request.commandId()))
        {
            GrandActivationReceipt original = history.replay(request, expectedIdentity, current.revision());
            return new GrandActivationProposal(current, history, currentAttempt, original, true);
        }
        if (currentAttempt.isPresent() || request.beforeRevision() != current.revision())
        { throw new IllegalArgumentException("Activation requires a fresh unoccupied head"); }
        GrandActivationReceipt proposed = GrandActivationReceipt.propose(request.commandId(),
            current, checklist, request.attempt().objective(), expectedIdentity, expectedRevision);
        if (!proposed.equals(request))
        { throw new IllegalArgumentException("Activation payload does not match current checklist"); }
        ArrayList<GrandActivationReceipt> receipts = new ArrayList<>(history.receipts().values());
        receipts.add(proposed);
        RunLifecycle next = proposed.attempt().lifecycle();
        GrandActivationHistory extended = new GrandActivationHistory(history.seal(), next.revision(), receipts);
        GrandCurrentAttempt attempt = GrandCurrentAttempt.restore(proposed.commandId(), extended, next, checklist);
        return new GrandActivationProposal(next, extended, Optional.of(attempt), proposed, false);
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public GrandActivationHistory history() { return history; }
    public Optional<GrandCurrentAttempt> currentAttempt() { return currentAttempt; }
    public GrandActivationReceipt receipt() { return receipt; }
    public boolean isReplay() { return replay; }
}
