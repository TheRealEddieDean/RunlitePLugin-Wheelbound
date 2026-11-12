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
        check(head.questCompletionHistory().isEmpty());
        var context = head.currentQuestAssignment().orElseThrow();
        var window = assignment.originalWindow();
        var receipt = new QuestFateCompletionReceipt(context, window, lifecycle.revision(),
            window.baselinePosition() + 1, UUID.randomUUID(), "quests", "completion");
        var contexts = new ArrayList<>(List.of(context));
        var completions = new QuestFateCompletionHistory(
            new QuestFateCompletionJournal(assignment.identity(), List.of(receipt)), contexts);
        contexts.clear();
        check(completions.contexts().equals(List.of(context)));
        try { completions.contexts().clear(); throw new AssertionError(); }
        catch (UnsupportedOperationException expected) { /* Immutable historical contexts. */ }
        rejects(() -> new QuestFateCompletionHistory(completions.journal(), List.of()));
        // Historical completion data does not require a current assignment or reservation.
        var historical = historical(lifecycle, catalog, Optional.of(creations), Optional.of(history), completions);
        check(historical.currentQuestAssignment().isEmpty() && historical.masterSpins().isEmpty());
        checkCodec.accept(historical);
        rejects(() -> MasterRetryRecovery.requireUnused(historical, receipt.commandId()));
        rejects(() -> MasterRetryRecovery.requireUnusedExceptCards(historical, receipt.commandId()));
        MasterRetryRecovery.requireUnused(historical, UUID.randomUUID());
        rejects(() -> LifecycleAdmission.prepare(historical, assignment.identity(), receipt.commandId(),
            lifecycle.revision(), RunReceipt.Operation.PAUSE));
        // Completion commands cannot reuse either the original draw or its terminal resolution.
        for (UUID collision : List.of(resolution.creation().transactionId(), resolution.commandId()))
        {
            var conflicting = new QuestFateCompletionReceipt(context, window, lifecycle.revision(),
                window.baselinePosition() + 1, collision, "quests", "completion");
            var conflictingHistory = new QuestFateCompletionHistory(
                new QuestFateCompletionJournal(assignment.identity(), List.of(conflicting)), List.of(context));
            rejects(() -> historical(lifecycle, catalog, Optional.of(creations), Optional.of(history), conflictingHistory));
        }
        var historicalPaused = LifecycleAdmission.prepare(historical, assignment.identity(), UUID.randomUUID(),
            lifecycle.revision(), RunReceipt.Operation.PAUSE);
        var historicalResumed = LifecycleAdmission.prepare(historicalPaused, assignment.identity(), UUID.randomUUID(),
            historicalPaused.lifecycle().revision(), RunReceipt.Operation.RESUME);
        check(historicalPaused.questCompletionHistory().orElseThrow() == completions);
        rejects(() -> MasterRetryRecovery.requireUnused(historicalPaused, receipt.commandId()));
        UUID pauseCommand = historicalPaused.receipts().get(0).commandId();
        var genericCollision = new QuestFateCompletionReceipt(context, window, lifecycle.revision(),
            window.baselinePosition() + 1, pauseCommand, "quests", "completion");
        var genericHistory = new QuestFateCompletionHistory(
            new QuestFateCompletionJournal(assignment.identity(), List.of(genericCollision)), List.of(context));
        rejects(() -> historical(historicalPaused.lifecycle(), catalog, Optional.of(creations),
            Optional.of(history), genericHistory, historicalPaused.receipts()));
        check(historicalResumed.questCompletionHistory().orElseThrow() == completions);
        check(completions.contexts().get(0).lifecycle() == lifecycle);
        completions.requireExtensionOf(completions);
        var emptyHistory = new QuestFateCompletionHistory(
            new QuestFateCompletionJournal(assignment.identity(), List.of()), List.of());
        completions.requireExtensionOf(emptyHistory);
        rejects(() -> emptyHistory.requireExtensionOf(completions));
        checkCodec.accept(historical(lifecycle, catalog, Optional.empty(), Optional.empty(), emptyHistory));
        rejects(() -> historical(lifecycle, catalog, Optional.empty(), Optional.of(history), completions));
        rejects(() -> historical(lifecycle, catalog, Optional.of(creations), Optional.empty(), completions));
        rejects(() -> historical(lifecycle, "foreign-catalog", Optional.of(creations), Optional.of(history), completions));
        rejects(() -> historical(new RunLifecycle(new RunIdentity("foreign", UUID.randomUUID()),
            lifecycle.revision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), catalog,
            Optional.empty(), Optional.empty(), completions));
        rejects(() -> completions.requireRetained(new RunLifecycle(assignment.identity(),
            lifecycle.revision() - 1, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            catalog, creations, history));
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

    private static RunDocument historical(RunLifecycle lifecycle, String catalog,
        Optional<MasterSelectionCreationJournal> creations, Optional<MasterSelectionResolutions> resolutions,
        QuestFateCompletionHistory history)
    { return historical(lifecycle, catalog, creations, resolutions, history, List.of()); }

    private static RunDocument historical(RunLifecycle lifecycle, String catalog,
        Optional<MasterSelectionCreationJournal> creations, Optional<MasterSelectionResolutions> resolutions,
        QuestFateCompletionHistory history, List<RunReceipt> receipts)
    {
        return new RunDocument(lifecycle, catalog, receipts, Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), creations, resolutions, Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.of(history));
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected aggregate rejection");
    }
}
