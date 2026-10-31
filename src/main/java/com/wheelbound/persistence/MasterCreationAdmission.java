package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionCreationJournal;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/** Pure preparation for the locked creation command. Does not authenticate
 * evidence/session, allocate spins, sample, persist or authorize gameplay. */
public final class MasterCreationAdmission
{
    private MasterCreationAdmission() { }

    /** Exact historical replay preserves the complete current head and pending prefix.
     * New requests require explicit history; legacy absence cannot establish uniqueness.
     * The store invokes this under its lock; trusted gameplay admission is separate. */
    public static RunDocument prepare(RunDocument current, MasterSelectionCreationReceipt request)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(request, "request");
        if (MasterCreationRecovery.find(current, request).isPresent()) { return current; }
        MasterSelectionCreationJournal history = current.masterCreations().orElseThrow(
            () -> new IllegalArgumentException("Master creation history is unavailable"));
        RunLifecycle head = current.lifecycle();
        if (head.revision() != request.beforeRevision()
            || head.state() != RunLifecycle.State.ACTIVE
            || head.obligation() != RunLifecycle.Obligation.NONE
            || current.pendingMasterSelection().isPresent()
            || current.grandCurrentAttempt().isPresent()
            || !current.catalogVersion().equals(request.initial().catalogVersion()))
        { throw new IllegalArgumentException("Stale, unavailable or conflicting Master creation context"); }
        RunLifecycle next = new RunLifecycle(head.identity(), request.afterRevision(),
            head.state(), head.obligation());
        ArrayList<MasterSelectionCreationReceipt> originals = new ArrayList<>(history.receipts().values());
        originals.add(request);
        ArrayList<RunReceipt> operations = new ArrayList<>(current.receipts());
        operations.add(RunReceipt.masterSelectionCreation(request));
        return new RunDocument(next, current.catalogVersion(), operations, current.fatePoints(),
            current.inventory(), current.grandFate(), current.grandProgress(), current.grandJournal(),
            current.grandActivations(), current.grandCurrentAttempt(), Optional.of(request.initial()),
            Optional.of(new MasterSelectionCreationJournal(head.identity(), next.revision(), originals)));
    }
}
