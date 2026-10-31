package com.wheelbound.persistence;

import com.wheelbound.domain.MasterOrdinaryPoolEvidence;
import com.wheelbound.domain.MasterSelectionReadiness;
import com.wheelbound.domain.MasterWheelEvidence;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.Map;
import java.util.Objects;

/** Read-only new-selection readiness against one aggregate head. Not evidence authentication,
 * session authorization, a reservation, or permission to draw/debit. A storage/session caller
 * must obtain the actual current head and revalidate before any subsequent commitment.
 */
public final class MasterAggregateEvidence
{
    private MasterAggregateEvidence() { }

    /** Inventory and catalog come exclusively from the aggregate, never the evidence caller.
     * Snapshot version still requires a trusted fresh observation source. Pending selections
     * must use their own recovery path; this method cannot refresh or replace their stream.
     */
    public static MasterSelectionReadiness assemble(RunDocument current, RunIdentity expectedIdentity,
        long expectedRevision, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> ordinaryPools)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(expectedIdentity, "expectedIdentity");
        RunLifecycle head = current.lifecycle();
        if (!head.identity().equals(expectedIdentity) || head.revision() != expectedRevision
            || head.state() != RunLifecycle.State.ACTIVE
            || head.obligation() != RunLifecycle.Obligation.NONE
            || current.pendingMasterSelection().isPresent() || current.grandCurrentAttempt().isPresent())
        { throw new IllegalArgumentException("Stale, foreign or occupied Master evidence context"); }
        return MasterWheelEvidence.assemble(head.identity(), head.revision(), snapshotVersion,
            current.catalogVersion(), current.inventory().orElseThrow(
                () -> new IllegalArgumentException("Master inventory is unavailable")), ordinaryPools);
    }
}
