package com.wheelbound.persistence;

import com.wheelbound.domain.GrandActivationHistory;
import com.wheelbound.domain.GrandActivationProposal;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.GrandFateChecklist;
import com.wheelbound.domain.RunIdentity;
import java.util.Objects;

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

    public static GrandActivationProposal propose(RunDocument current,
        GrandActivationReceipt request, RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(request, "request");
        GrandActivationHistory history = current.grandActivations().orElseThrow(
            () -> new IllegalArgumentException("Grand activation history is unavailable"));
        if (current.receipts().stream().anyMatch(r -> r.commandId().equals(request.commandId())
                && !request.equals(r.activation()))
            || current.grandFate().get().commandId().equals(request.commandId())
            || current.grandProgress().map(progress -> progress.evidence().values().stream()
                .anyMatch(reference -> reference.receiptId().equals(request.commandId()))).orElse(false))
        { throw new IllegalArgumentException("Grand activation command collides with another operation"); }
        // Pending draws/offers and all mandatory settlements are represented by the
        // lifecycle obligation. The domain proposal checks that boundary only for new
        // commands: historical replay must preserve a later pending obligation.
        GrandFateChecklist checklist = current.grandProgress().map(progress -> progress.checklist())
            .orElseGet(() -> GrandFateChecklist.initial(history.seal()));
        return GrandActivationProposal.propose(current.lifecycle(), checklist, history,
            current.grandCurrentAttempt(), request, expectedIdentity, expectedRevision);
    }
}
