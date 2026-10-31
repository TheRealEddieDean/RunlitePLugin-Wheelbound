package com.wheelbound.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Structural complete-wheel assembly for WB-D138/139, not authenticated admission.
 * Uses the supplied inventory's active wheel; stored copies never enter the pool.
 * Reviewed family catalogs and trusted, fresh detectors remain caller prerequisites.
 * Unwired: no draw, debit, retry, persistence or session authorization.
 */
public final class MasterWheelEvidence
{
    private MasterWheelEvidence() { }

    /** Exactly one complete-pool declaration per distinct active ordinary activity.
     * Duplicates share activity eligibility but retain their own IDs and weights.
     * Special outcomes remain unsupported until their evidence adapter exists.
     */
    public static MasterSelectionReadiness assemble(RunIdentity identity, long revision,
        String snapshotVersion, String catalogVersion, SliceInventory inventory,
        Map<String, MasterOrdinaryPoolEvidence> ordinaryPools)
    {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(inventory, "inventory");
        if (revision < 0) { throw new IllegalArgumentException("Negative evidence revision"); }
        canonical(snapshotVersion);
        canonical(catalogVersion);
        Map<String, MasterOrdinaryPoolEvidence> pools = Map.copyOf(ordinaryPools);
        ActiveWheel wheel = inventory.activeWheel();
        Set<String> required = new HashSet<>();
        Map<UUID, MasterSelectionReadiness.Classification> classifications = new HashMap<>();
        for (ActiveWheel.Slice slice : wheel.slices())
        {
            if (slice.kind() == ActiveWheel.Kind.ORDINARY)
            {
                required.add(slice.activity());
                MasterOrdinaryPoolEvidence pool = pools.get(slice.activity());
                if (pool == null) { throw new IllegalArgumentException("Missing active activity evidence"); }
                classifications.put(slice.id(), pool.classifyFor(identity, revision,
                    snapshotVersion, catalogVersion, slice));
            }
            else
            {
                classifications.put(slice.id(), new MasterSelectionReadiness.Classification(
                    MasterSelectionReadiness.Availability.UNSUPPORTED,
                    "Missing supported special obligation evidence: " + slice.kind().name()));
            }
        }
        if (!required.equals(pools.keySet()))
        { throw new IllegalArgumentException("Evidence for an inactive activity"); }
        return new MasterSelectionReadiness(wheel, classifications);
    }

    private static void canonical(String value)
    {
        Objects.requireNonNull(value, "snapshot/catalog version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Missing or noncanonical snapshot/catalog version"); }
    }
}
