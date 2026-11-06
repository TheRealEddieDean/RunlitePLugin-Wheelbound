package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Supplied structural retention only; no durable or live evidence authority. */
public final class QuestFateAggregateChecks
{
    public static void verify(QuestFateAssignment assignment)
    { verify(assignment, head -> { }); }

    public static void verify(QuestFateAssignment assignment, java.util.function.Consumer<RunDocument> checkCodec)
    {
        var resolution = assignment.provenance().resolution();
        var lifecycle = new RunLifecycle(assignment.identity(), assignment.creationRevision(),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        var creations = new MasterSelectionCreationJournal(assignment.identity(), lifecycle.revision(),
            List.of(resolution.creation()));
        var history = new MasterSelectionResolutions(assignment.identity(), lifecycle.revision(), List.of(resolution));
        var spins = new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), lifecycle.revision(),
            assignment.allocation().spinsBefore(), Optional.of(assignment.allocation()));
        String catalog = resolution.resolved().catalogVersion();
        var head = document(lifecycle, catalog, Optional.of(creations), Optional.of(history),
            Optional.of(spins), Optional.of(assignment));
        checkCodec.accept(head);
        var paused = LifecycleAdmission.prepare(head, assignment.identity(), UUID.randomUUID(),
            lifecycle.revision(), RunReceipt.Operation.PAUSE);
        var resumed = LifecycleAdmission.prepare(paused, assignment.identity(), UUID.randomUUID(),
            paused.lifecycle().revision(), RunReceipt.Operation.RESUME);
        check(paused.currentQuestAssignment().orElseThrow().assignment() == assignment);
        check(resumed.currentQuestAssignment().orElseThrow().assignment() == assignment);
        check(resumed.currentQuestAssignment().orElseThrow().lifecycle() == resumed.lifecycle());
        check(resumed.masterSpins().orElseThrow() == spins && spins.available() == spins.allotted() - 1);
        check(head.lifecycle() == lifecycle && assignment.originalWindow().baselineRevision() == lifecycle.revision());
        check(document(lifecycle, catalog, Optional.of(creations), Optional.of(history),
            Optional.of(spins), Optional.empty()).currentQuestAssignment().isEmpty());
        rejects(() -> document(lifecycle, catalog, Optional.empty(), Optional.of(history), Optional.of(spins), Optional.of(assignment)));
        rejects(() -> document(lifecycle, catalog, Optional.of(creations), Optional.empty(), Optional.of(spins), Optional.of(assignment)));
        rejects(() -> document(lifecycle, catalog, Optional.of(creations), Optional.of(history), Optional.empty(), Optional.of(assignment)));
        rejects(() -> document(lifecycle, "foreign-catalog", Optional.of(creations), Optional.of(history), Optional.of(spins), Optional.of(assignment)));
        rejects(() -> document(new RunLifecycle(assignment.identity(), lifecycle.revision(),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), catalog,
            Optional.of(creations), Optional.of(history), Optional.of(spins), Optional.of(assignment)));
    }

    private static RunDocument document(RunLifecycle lifecycle, String catalog,
        Optional<MasterSelectionCreationJournal> creations, Optional<MasterSelectionResolutions> history,
        Optional<MasterSpinState> spins, Optional<QuestFateAssignment> assignment)
    {
        return new RunDocument(lifecycle, catalog, List.of(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), creations, history, spins, Optional.empty(), Optional.empty(), Optional.empty(), assignment);
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected aggregate rejection");
    }
}
