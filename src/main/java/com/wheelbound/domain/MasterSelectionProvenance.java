package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Frozen WB-D138/139 precommit transcript, with no RNG, allocation or persistence.
 * Stream descriptor/state are opaque caller declarations (including algorithm/seed
 * and cursor as appropriate); this class cannot authenticate or advance that stream.
 * A future durable command must revalidate live evidence and retain this transcript
 * when suspending. A changed snapshot requires separate explicit reconciliation.
 */
public final class MasterSelectionProvenance
{
    public static final class Step
    {
        private final DrawCommitment draw;
        private final String streamStateAfter;

        public Step(DrawCommitment draw, String streamStateAfter)
        {
            this.draw = Objects.requireNonNull(draw, "draw");
            this.streamStateAfter = canonical(streamStateAfter);
        }

        public DrawCommitment draw() { return draw; }
        public String streamStateAfter() { return streamStateAfter; }
    }

    public static final class Landing
    {
        private final Step step;
        private final MasterSelectionReadiness.Landing kind;
        private final String reason;

        private Landing(Step step, MasterSelectionReadiness.Landing kind, String reason)
        {
            this.step = step;
            this.kind = kind;
            this.reason = reason;
        }

        public Step step() { return step; }
        public MasterSelectionReadiness.Landing kind() { return kind; }
        public String reason() { return reason; }
    }

    private final UUID transactionId;
    private final String snapshotVersion;
    private final String catalogVersion;
    private final String streamDescriptor;
    private final String initialStreamState;
    private final MasterSelectionReadiness readiness;
    private final WeightedPool pool;
    private final List<Landing> landings;

    /** Restores and validates an ordered prefix without drawing or replacing any ticket. */
    public MasterSelectionProvenance(UUID transactionId, String snapshotVersion,
        String catalogVersion, String streamDescriptor, String initialStreamState,
        MasterSelectionReadiness readiness, List<Step> steps)
    {
        this.transactionId = Objects.requireNonNull(transactionId, "transactionId");
        this.snapshotVersion = canonical(snapshotVersion);
        this.catalogVersion = canonical(catalogVersion);
        this.streamDescriptor = canonical(streamDescriptor);
        this.initialStreamState = canonical(initialStreamState);
        this.readiness = Objects.requireNonNull(readiness, "readiness");
        if (readiness.status() != MasterSelectionReadiness.Status.READY)
        { throw new IllegalArgumentException("Cannot start a transcript without ready frozen evidence"); }
        List<WeightedPool.Entry> entries = new ArrayList<>();
        for (ActiveWheel.Slice slice : readiness.wheel().slices())
        { entries.add(new WeightedPool.Entry(slice.id().toString(), slice.weightUnits())); }
        pool = new WeightedPool(this.snapshotVersion, entries);
        List<Landing> validated = new ArrayList<>();
        boolean resolved = false;
        for (Step step : List.copyOf(steps))
        {
            if (resolved) { throw new IllegalArgumentException("Cannot redraw a valid landing"); }
            if (!pool.equals(step.draw().pool()))
            { throw new IllegalArgumentException("Draw changed the frozen full-weight pool"); }
            UUID selected = UUID.fromString(step.draw().selectedId());
            MasterSelectionReadiness.Landing kind = readiness.classifyLanding(selected);
            validated.add(new Landing(step, kind, readiness.classifications().get(selected).reason()));
            resolved = kind != MasterSelectionReadiness.Landing.EMPTY_ORDINARY_RETRY;
        }
        landings = List.copyOf(validated);
    }

    public UUID transactionId() { return transactionId; }
    public String snapshotVersion() { return snapshotVersion; }
    public String catalogVersion() { return catalogVersion; }
    public String streamDescriptor() { return streamDescriptor; }
    public String initialStreamState() { return initialStreamState; }
    public MasterSelectionReadiness readiness() { return readiness; }
    public WeightedPool pool() { return pool; }
    public List<Landing> landings() { return landings; }
    /** Number of retained raw Master draws, not a PRNG cursor or an allocation count. */
    public int drawCount() { return landings.size(); }
    public String currentStreamState()
    { return landings.isEmpty() ? initialStreamState : landings.get(landings.size() - 1).step().streamStateAfter(); }
    /** A valid landing ends retries; this does not commit an offer or mandatory obligation. */
    public boolean hasValidLanding()
    { return !landings.isEmpty() && landings.get(landings.size() - 1).kind() != MasterSelectionReadiness.Landing.EMPTY_ORDINARY_RETRY; }

    /**
     * Proposes the next supplied draw on this frozen transcript. The count and opaque
     * state guard the exact prefix, including streams whose state strings can repeat.
     * This does not sample, authenticate stream continuity, revalidate evidence, debit
     * an allocation or persist. A durable caller must admit and save the proposal under
     * its lock before publishing it; stale requests must not be retried on a new prefix.
     */
    public MasterSelectionProvenance append(UUID expectedTransactionId, int expectedDrawCount,
        String expectedStreamState, Step next)
    {
        Objects.requireNonNull(expectedTransactionId, "expectedTransactionId");
        Objects.requireNonNull(expectedStreamState, "expectedStreamState");
        Objects.requireNonNull(next, "next");
        if (!transactionId.equals(expectedTransactionId) || drawCount() != expectedDrawCount
            || !currentStreamState().equals(expectedStreamState))
        { throw new IllegalArgumentException("Stale or foreign selection prefix"); }
        if (hasValidLanding())
        { throw new IllegalStateException("Cannot redraw a valid landing"); }
        List<Step> steps = new ArrayList<>();
        for (Landing landing : landings) { steps.add(landing.step()); }
        steps.add(next);
        return new MasterSelectionProvenance(transactionId, snapshotVersion, catalogVersion,
            streamDescriptor, initialStreamState, readiness, steps);
    }

    private static String canonical(String value)
    {
        Objects.requireNonNull(value, "identity/state");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Missing or noncanonical identity/state"); }
        return value;
    }
}
