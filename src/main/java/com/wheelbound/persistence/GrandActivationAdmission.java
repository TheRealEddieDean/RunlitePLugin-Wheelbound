package com.wheelbound.persistence;

import com.wheelbound.domain.GrandActivationHistory;
import com.wheelbound.domain.GrandActivationProposal;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.GrandFateChecklist;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Read-only aggregate admission for the future atomic activation command.
 * Requires explicit history; legacy absence is unavailable, not an empty history.
 * Does not authenticate a client session, persist, reserve a revision or publish success.
 * The eventual command must run admission under its write lock and commit its journal
 * envelope together with the proposal. Generic store writes still reject activation.
 */
public final class GrandActivationAdmission
{
    private GrandActivationAdmission() { }

    /**
     * Prepare the complete, immutable snapshot for a future atomic command. Exact
     * replay returns the supplied snapshot unchanged, including explicit absence of
     * a current attempt. This does not write, acknowledge success or admit a session;
     * the store still refuses activation mutations through its generic write API.
     */
    public static RunDocument prepare(RunDocument current, GrandActivationReceipt request,
        RunIdentity expectedIdentity, long expectedRevision)
    {
        GrandActivationProposal proposal = propose(current, request, expectedIdentity, expectedRevision);
        if (proposal.isReplay()) { return current; }
        ArrayList<RunReceipt> receipts = new ArrayList<>(current.receipts());
        receipts.add(RunReceipt.grandActivation(proposal.receipt()));
        return new RunDocument(proposal.lifecycle(), current.catalogVersion(), receipts,
            current.fatePoints(), current.inventory(), current.grandFate(), current.grandProgress(),
            current.grandJournal(), Optional.of(proposal.history()), proposal.currentAttempt(),
            current.pendingMasterSelection(), current.masterCreations(), current.masterResolutions(), current.masterSpins());
    }

    public static GrandActivationProposal propose(RunDocument current,
        GrandActivationReceipt request, RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(request, "request");
        GrandActivationHistory history = current.grandActivations().orElseThrow(
            () -> new IllegalArgumentException("Grand activation history is unavailable"));
        if (current.masterResolutions().filter(h -> h.receipts().containsKey(request.commandId())).isPresent()
            || current.receipts().stream().anyMatch(r -> r.commandId().equals(request.commandId())
                && !request.equals(r.activation()))
            || current.grandFate().get().commandId().equals(request.commandId())
            || current.grandProgress().map(progress -> progress.evidence().values().stream()
                .anyMatch(reference -> reference.receiptId().equals(request.commandId()))).orElse(false))
        { throw new IllegalArgumentException("Grand activation command collides with another operation"); }
        // The lifecycle guards committed obligations. Unresolved precommit selections
        // have their own aggregate guard below; historical replay preserves both.
        GrandFateChecklist checklist = current.grandProgress().map(progress -> progress.checklist())
            .orElseGet(() -> GrandFateChecklist.initial(history.seal()));
        GrandActivationProposal proposal = GrandActivationProposal.propose(current.lifecycle(), checklist, history,
            current.grandCurrentAttempt(), request, expectedIdentity, expectedRevision);
        if (!proposal.isReplay() && current.pendingMasterSelection().isPresent())
        { throw new IllegalArgumentException("Unresolved Master selection requires reconciliation before activation"); }
        return proposal;
    }
}
