package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Frozen supplied Questing generation context and player choice tied to a Master
 * resolution. No RNG runs on construction or restoration. Stream descriptors
 * (including algorithm/version/seed) and states are opaque caller declarations,
 * as in MasterSelectionProvenance; they do not prove fair generation or continuity.
 * The caller must resolve the reviewed Questing activity key, authenticate catalogs,
 * routes and quotes, and atomically admit a complete assignment with its spin receipt.
 * This value is neither current-assignment authority nor permission to reroll.
 */
public final class QuestFateProvenance
{
    private final MasterSelectionResolutionReceipt resolution;
    private final UUID sliceId;
    private final String activityKey;
    private final String streamDescriptor;
    private final String initialStreamState;
    private final String finalStreamState;
    private final QuestFateReward reward;

    public QuestFateProvenance(MasterSelectionResolutionReceipt resolution,
        UUID sliceId, String activityKey, String streamDescriptor,
        String initialStreamState, String finalStreamState, QuestFateReward reward)
    {
        this.resolution = Objects.requireNonNull(resolution, "resolution");
        this.sliceId = Objects.requireNonNull(sliceId, "sliceId");
        this.activityKey = key(activityKey);
        this.streamDescriptor = key(streamDescriptor);
        this.initialStreamState = key(initialStreamState);
        this.finalStreamState = key(finalStreamState);
        this.reward = Objects.requireNonNull(reward, "reward");
        MasterSelectionProvenance transcript = resolution.resolved();
        MasterSelectionProvenance.Landing terminal = transcript.landings()
            .get(transcript.drawCount() - 1);
        if (terminal.kind() != MasterSelectionReadiness.Landing.ORDINARY_ASSIGNMENT
            || !terminal.step().draw().selectedId().equals(sliceId.toString()))
        { throw new IllegalArgumentException("Questing requires the resolved ordinary slice"); }
        ActiveWheel.Slice selected = transcript.readiness().wheel().slices().stream()
            .filter(slice -> slice.id().equals(sliceId)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Missing resolved slice"));
        if (!activityKey.equals(selected.activity()))
        { throw new IllegalArgumentException("Resolved activity differs from supplied Questing key"); }
    }

    public MasterSelectionResolutionReceipt resolution() { return resolution; }
    public UUID sliceId() { return sliceId; }
    public String activityKey() { return activityKey; }
    public String streamDescriptor() { return streamDescriptor; }
    public String initialStreamState() { return initialStreamState; }
    public String finalStreamState() { return finalStreamState; }
    public QuestFateReward reward() { return reward; }

    /** Compare the complete frozen payload, including the Master retry prefix. */
    public void requireSameProvenance(QuestFateProvenance expected)
    {
        if (!equals(Objects.requireNonNull(expected, "expected")))
        { throw new IllegalArgumentException("Changed frozen Questing provenance"); }
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "activity/stream context");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical activity/stream context"); }
        return value;
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof QuestFateProvenance)) { return false; }
        QuestFateProvenance other = (QuestFateProvenance) value;
        return resolution.equals(other.resolution) && sliceId.equals(other.sliceId)
            && activityKey.equals(other.activityKey) && streamDescriptor.equals(other.streamDescriptor)
            && initialStreamState.equals(other.initialStreamState)
            && finalStreamState.equals(other.finalStreamState) && reward.equals(other.reward);
    }

    @Override public int hashCode()
    { return Objects.hash(resolution, sliceId, activityKey, streamDescriptor,
        initialStreamState, finalStreamState, reward); }
}
