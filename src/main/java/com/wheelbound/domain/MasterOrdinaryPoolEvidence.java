package com.wheelbound.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Complete ordinary-activity pool declaration for WB-D138/139. This validates
 * coverage and context, not the truth or freshness of detector evidence. The
 * reviewed catalog must supply all applicable family keys; callers cannot infer
 * completeness from observed results. Unwired until trusted adapters exist.
 */
public final class MasterOrdinaryPoolEvidence
{
    private final RunIdentity identity;
    private final long revision;
    private final String snapshotVersion;
    private final String catalogVersion;
    private final String activity;
    private final Map<String, MasterSelectionReadiness.Classification> families;
    private final MasterSelectionReadiness.Classification classification;

    public MasterOrdinaryPoolEvidence(RunIdentity identity, long revision,
        String snapshotVersion, String catalogVersion, String activity,
        Set<String> requiredFamilies,
        Map<String, MasterSelectionReadiness.Classification> families)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        if (revision < 0) { throw new IllegalArgumentException("Negative evidence revision"); }
        this.revision = revision;
        this.snapshotVersion = canonical(snapshotVersion);
        this.catalogVersion = canonical(catalogVersion);
        this.activity = canonical(activity);
        Set<String> required = Set.copyOf(requiredFamilies);
        if (required.isEmpty()) { throw new IllegalArgumentException("Missing complete-pool catalog"); }
        for (String family : required) { canonical(family); }
        this.families = Map.copyOf(families);
        if (!required.equals(this.families.keySet()))
        { throw new IllegalArgumentException("Missing or foreign assignment family evidence"); }

        boolean unknown = false;
        boolean unsupported = false;
        boolean assignable = false;
        for (MasterSelectionReadiness.Classification family : this.families.values())
        {
            unknown |= family.availability() == MasterSelectionReadiness.Availability.UNKNOWN;
            unsupported |= family.availability() == MasterSelectionReadiness.Availability.UNSUPPORTED;
            assignable |= family.availability() == MasterSelectionReadiness.Availability.ASSIGNABLE;
        }
        MasterSelectionReadiness.Availability availability = unsupported
            ? MasterSelectionReadiness.Availability.UNSUPPORTED : unknown
            ? MasterSelectionReadiness.Availability.UNKNOWN : assignable
            ? MasterSelectionReadiness.Availability.ASSIGNABLE
            : MasterSelectionReadiness.Availability.VERIFIED_EMPTY;
        classification = new MasterSelectionReadiness.Classification(availability,
            "Complete ordinary pool " + this.activity + ": " + availability.name());
    }

    /** Rejects mixed account, revision, snapshot, catalog and activity declarations.
     * No session token, elapsed time or caller label authenticates these declarations.
     * Special outcomes require their own supported obligation evidence path.
     */
    public MasterSelectionReadiness.Classification classifyFor(RunIdentity expectedIdentity,
        long expectedRevision, String expectedSnapshotVersion, String expectedCatalogVersion,
        ActiveWheel.Slice slice)
    {
        Objects.requireNonNull(slice, "slice");
        if (!identity.equals(expectedIdentity) || revision != expectedRevision
            || !snapshotVersion.equals(expectedSnapshotVersion)
            || !catalogVersion.equals(expectedCatalogVersion)
            || slice.kind() != ActiveWheel.Kind.ORDINARY || !activity.equals(slice.activity()))
        { throw new IllegalArgumentException("Stale or foreign ordinary pool evidence context"); }
        return classification;
    }

    /** Immutable detailed reasons, including every empty or unavailable family. */
    public Map<String, MasterSelectionReadiness.Classification> families() { return families; }

    private static String canonical(String value)
    {
        Objects.requireNonNull(value, "catalog/snapshot key");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Missing or noncanonical catalog/snapshot key"); }
        return value;
    }
}
