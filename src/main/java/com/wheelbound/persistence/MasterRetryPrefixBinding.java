package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.RunLifecycle;
import java.util.Objects;

/** Structural prerequisite for empty-only continuation admission. Does not authenticate
 * evidence/randomness, publish an aggregate, persist suspension or authorize replay.
 */
public final class MasterRetryPrefixBinding
{
    private MasterRetryPrefixBinding() { }

    /** Require the complete actual pending prefix, including every raw ticket and
     * opaque state, before accepting any suffix. Equal prefixes are permitted for
     * suspension preparation; terminal landings require their own atomic operation.
     * A locked command must separately qualify identity, revision and current evidence.
     */
    public static void validate(RunDocument current, MasterSelectionProvenance proposed)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(proposed, "proposed");
        var head = current.lifecycle();
        if (head.state() != RunLifecycle.State.ACTIVE
            || head.obligation() != RunLifecycle.Obligation.NONE
            || current.grandCurrentAttempt().isPresent())
        { throw new IllegalArgumentException("Unavailable retry head"); }
        var pending = current.pendingMasterSelection().orElseThrow(
            () -> new IllegalArgumentException("Missing pending selection"));
        var history = current.masterCreations().orElseThrow(
            () -> new IllegalArgumentException("Missing creation history"));
        var original = history.matchPending(pending, head.identity(), head.revision());
        if (!original.equals(history.matchPending(proposed, head.identity(), head.revision()))
            || proposed.drawCount() < pending.drawCount())
        { throw new IllegalArgumentException("Changed or shortened retry prefix"); }
        for (int i = 0; i < pending.drawCount(); i++)
        {
            var saved = pending.landings().get(i).step();
            var supplied = proposed.landings().get(i).step();
            if (!saved.draw().equals(supplied.draw())
                || !saved.streamStateAfter().equals(supplied.streamStateAfter()))
            { throw new IllegalArgumentException("Retry changed a retained draw or stream state"); }
        }
    }
}
