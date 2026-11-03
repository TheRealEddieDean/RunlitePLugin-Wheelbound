package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

/** Bounded WB-D138 proposal over frozen evidence, deliberately unwired from gameplay.
 * The source receives the exact evolving prefix and supplies a full-pool draw/state.
 * It must be side-effect-free: this class neither authenticates randomness nor saves
 * a cursor. Admission, live revalidation and atomic persistence remain caller duties.
 */
public final class MasterEmptyRetryBatch
{
    private MasterEmptyRetryBatch() { }

    /**
     * Continue through empty ordinary landings until a valid landing or work budget.
     * Budget exhaustion is a scheduling yield, never a gameplay failure or reroll.
     * The returned immutable prefix retains every supplied empty reason and stream
     * state. A valid landing is returned for separate terminal admission, not charged
     * or published here. Re-enter using that retained prefix, never a new stream.
     */
    public static MasterSelectionProvenance propose(MasterSelectionProvenance prefix,
        UUID expectedTransactionId, int expectedDrawCount, String expectedStreamState,
        int maxDraws, Function<MasterSelectionProvenance, MasterSelectionProvenance.Step> source)
    {
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(expectedTransactionId, "expectedTransactionId");
        Objects.requireNonNull(expectedStreamState, "expectedStreamState");
        if (maxDraws <= 0) { throw new IllegalArgumentException("Positive work budget required"); }
        if (!prefix.transactionId().equals(expectedTransactionId)
            || prefix.drawCount() != expectedDrawCount
            || !prefix.currentStreamState().equals(expectedStreamState))
        { throw new IllegalArgumentException("Stale or foreign retry prefix"); }
        if (prefix.hasValidLanding())
        { throw new IllegalStateException("Cannot retry a valid landing"); }
        MasterSelectionProvenance current = prefix;
        for (int i = 0; i < maxDraws; i++)
        {
            MasterSelectionProvenance.Step next = Objects.requireNonNull(source.apply(current), "next");
            current = current.append(current.transactionId(), current.drawCount(),
                current.currentStreamState(), next);
            if (current.hasValidLanding()) { break; }
        }
        return current;
    }
}
