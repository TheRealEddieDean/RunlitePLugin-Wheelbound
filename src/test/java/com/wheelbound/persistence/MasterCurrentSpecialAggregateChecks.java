package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Offline structural aggregate retention; no terminal command admission. */
public final class MasterCurrentSpecialAggregateChecks
{
    public static void verify(MasterCurrentSpecialObligation context, MasterSelectionResolutions history)
    {
        var head = document(context.lifecycle(), history, context.spins(), Optional.of(context.obligation()));
        var id = head.lifecycle().identity();
        var paused = LifecycleAdmission.prepare(head, id, UUID.randomUUID(), idRevision(head), RunReceipt.Operation.PAUSE);
        var resumed = LifecycleAdmission.prepare(paused, id, UUID.randomUUID(), idRevision(paused), RunReceipt.Operation.RESUME);
        var retained = resumed.currentMasterSpecial().orElseThrow();
        if (retained.obligation() != context.obligation() || retained.spins() != context.spins()
            || retained.lifecycle().revision() != context.lifecycle().revision() + 2
            || retained.lifecycle().obligation() != context.lifecycle().obligation()
            || head.lifecycle() != context.lifecycle())
        { throw new AssertionError("Current special lost across lifecycle copy"); }
        if (document(context.lifecycle(), history, context.spins(), Optional.empty()).currentMasterSpecial().isPresent())
        { throw new AssertionError("Current special inferred from history"); }
        try
        {
            document(new RunLifecycle(id, idRevision(head), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
                history, context.spins(), Optional.of(context.obligation()));
        }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Incompatible aggregate lifecycle accepted");
    }

    private static long idRevision(RunDocument head) { return head.lifecycle().revision(); }

    private static RunDocument document(RunLifecycle lifecycle, MasterSelectionResolutions history,
        MasterSpinState spins, Optional<MasterSpecialObligation> special)
    {
        return new RunDocument(lifecycle, "catalog", List.of(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.of(new MasterSelectionCreationJournal(lifecycle.identity(), lifecycle.revision(),
                history.receipts().values().stream().map(r -> r.creation()).collect(java.util.stream.Collectors.toList()))),
            Optional.of(history), Optional.of(spins), special);
    }

    public static void main(String[] args)
    {
        MasterCurrentSpecialObligationChecks.main(args);
        System.out.println("MasterCurrentSpecialAggregateChecks PASSED");
    }
}
