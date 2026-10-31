package com.wheelbound.domain;

import java.util.Objects;
import java.util.Optional;

/**
 * Versioned structural snapshot of allotted spins and one outstanding reservation.
 * Reservation reduces availability, not the allotted balance: consumption belongs to
 * completion/Reject or the supported special settlement. This model supplies neither
 * settlement nor grants. It must be retained atomically with the terminal history and
 * obligation in a future aggregate command before it can authorize gameplay.
 * Legacy absence must remain absent; history cannot reconstruct an allotted balance.
 */
public final class MasterSpinState
{
    public static final int VERSION = 1;
    private final RunIdentity identity;
    private final long revision;
    private final long allotted;
    private final Optional<MasterSpinAllocation> reservation;

    /** Restore explicit state; structural validation does not establish storage authority. */
    public MasterSpinState(int version, RunIdentity identity, long revision, long allotted,
        Optional<MasterSpinAllocation> reservation)
    {
        if (version != VERSION || revision < 0 || allotted < 0)
        { throw new IllegalArgumentException("Unsupported or invalid spin state"); }
        this.identity = Objects.requireNonNull(identity, "identity");
        this.reservation = Objects.requireNonNull(reservation, "reservation");
        reservation.ifPresent(allocation -> {
            if (!identity.equals(allocation.resolution().identity())
                || allocation.resolution().afterRevision() > revision
                || allocation.spinsBefore() != allotted)
            { throw new IllegalArgumentException("Reservation does not match spin state"); }
        });
        this.revision = revision;
        this.allotted = allotted;
    }

    /**
     * Reserve a previously aggregate-bound proposal against the exact current head.
     * The caller must independently bind its actual pending transcript and publish all
     * resulting state together. Replays use replay(), never a second reservation.
     */
    public MasterSpinState reserve(RunLifecycle head, MasterSpinAllocation allocation)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(allocation, "allocation");
        if (!identity.equals(head.identity()) || revision != head.revision()
            || reservation.isPresent() || allocation.spinsBefore() != allotted)
        { throw new IllegalArgumentException("Stale, occupied or mismatched spin state"); }
        // Recheck ACTIVE/NONE, receipt identity/revision and positive balance.
        MasterSpinAllocation.prepare(head, allotted, allocation.resolution());
        return new MasterSpinState(VERSION, identity, allocation.resolution().afterRevision(),
            allotted, Optional.of(allocation));
    }

    /** Return the retained original accounting without inspecting/subtracting a new balance. */
    public MasterSpinAllocation replay(RunIdentity requestedIdentity, long currentRevision,
        MasterSelectionResolutionReceipt requested)
    {
        if (currentRevision < revision)
        { throw new IllegalArgumentException("Spin state is newer than replay head"); }
        return reservation.orElseThrow(() ->
            new IllegalArgumentException("No retained spin reservation"))
            .replay(requestedIdentity, currentRevision, requested);
    }

    public int version() { return VERSION; }
    public RunIdentity identity() { return identity; }
    public long revision() { return revision; }
    public long allotted() { return allotted; }
    public long available() { return allotted - (reservation.isPresent() ? 1 : 0); }
    public Optional<MasterSpinAllocation> reservation() { return reservation; }
}
