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
        // Aggregate preparation requires explicit history and retains all other state.
        rejects(() -> head.appendSuppliedQuestCompletion(receipt, lifecycle.revision(),
            window.sessionId(), window.windowId()));
        var appendable = new RunDocument(lifecycle, catalog, head.receipts(), head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), head.pendingMasterSelection(),
            head.masterCreations(), head.masterResolutions(), head.masterSpins(), Optional.empty(),
            head.fateCards(), head.cardPurchases(), Optional.of(assignment), Optional.of(emptyHistory));
        for (int fault = 0; fault < 6; fault++)
        {
            var coverage = new QuestFateObservationCoverage(context, assignment.identity(), lifecycle.revision(),
                window.sessionId(), window.windowId(), window.baselinePosition(),
                window.selection().selection().questId(), QuestFateCompletion.QuestState.IN_PROGRESS);
            final int kind = fault;
            if (kind == 1) { coverage.interrupt(); }
            java.util.function.Supplier<RunDocument> prepare = () -> (kind == 2 ? head : appendable)
                .appendCoveredSuppliedQuestObservation(assignment.identity(),
                    lifecycle.revision() + (kind == 3 ? 1 : 0), coverage,
                    kind == 4 ? UUID.randomUUID() : window.sessionId(), window.windowId(),
                    receipt.observedPosition(), window.selection().selection().questId(),
                    QuestFateCompletion.QuestState.COMPLETE,
                    kind == 5 ? resolution.commandId() : receipt.commandId(), "quests", "completion");
            if (kind == 0)
            {
                var covered = prepare.get();
                check(covered.questCompletionHistory().orElseThrow().journal().receipts().equals(List.of(receipt)));
                check(covered.lifecycle() == lifecycle && covered.masterSpins().orElseThrow() == spins);
                check(covered.fatePoints().equals(appendable.fatePoints()));
                check(covered.appendSuppliedQuestCompletion(receipt, lifecycle.revision(),
                    window.sessionId(), window.windowId()) == covered);
            }
            else { rejectsObservation(() -> prepare.get()); }
            check(!coverage.isOpen());
            check(appendable.questCompletionHistory().orElseThrow() == emptyHistory);
            rejectsObservation(() -> prepare.get());
        }
        var appended = appendable.appendSuppliedQuestCompletion(receipt, lifecycle.revision(),
            window.sessionId(), window.windowId());
        var observed = observe(appendable, assignment.identity(), lifecycle.revision(), window,
            window.sessionId(), window.windowId(), window.baselinePosition() + 1,
            window.selection().selection().questId(), QuestFateCompletion.QuestState.COMPLETE, receipt.commandId());
        check(observed.questCompletionHistory().orElseThrow().journal().receipts().equals(List.of(receipt)));
        check(observed.lifecycle() == lifecycle && observed.masterSpins().orElseThrow() == spins);
        check(observed.currentQuestAssignment().orElseThrow().assignment() == assignment);
        check(observed.fatePoints().equals(appendable.fatePoints()) && observed.receipts().equals(appendable.receipts()));
        check(observe(observed, assignment.identity(), lifecycle.revision(), window,
            window.sessionId(), window.windowId(), window.baselinePosition() + 1,
            window.selection().selection().questId(), QuestFateCompletion.QuestState.COMPLETE,
            receipt.commandId()) == observed);
        for (UUID collision : List.of(resolution.creation().transactionId(), resolution.commandId()))
        {
            rejects(() -> observe(appendable, assignment.identity(), lifecycle.revision(), window,
                window.sessionId(), window.windowId(), window.baselinePosition() + 1,
                window.selection().selection().questId(), QuestFateCompletion.QuestState.COMPLETE, collision));
        }
        verifyObservationRefusals(appendable, receipt);
        verifyObservationRefusals(head, receipt); // Legacy history absence is never initialized.
        verifyObservationRefusals(historical, receipt); // History cannot synthesize a current assignment.
        check(appended != appendable && appendable.questCompletionHistory().orElseThrow() == emptyHistory);
        check(appended.questCompletionHistory().orElseThrow().journal().receipts().equals(List.of(receipt)));
        check(appended.lifecycle() == lifecycle && appended.masterSpins().orElseThrow() == spins);
        check(appended.currentQuestAssignment().orElseThrow().assignment() == assignment);
        check(appended.fatePoints().equals(head.fatePoints()) && appended.receipts().equals(head.receipts()));
        check(appended.appendSuppliedQuestCompletion(receipt, lifecycle.revision() + 10,
            UUID.randomUUID(), UUID.randomUUID()) == appended);
        var changedReplay = new QuestFateCompletionReceipt(context, window, lifecycle.revision(),
            window.baselinePosition() + 1, receipt.commandId(), "quests", "changed-completion");
        rejects(() -> appended.appendSuppliedQuestCompletion(changedReplay, lifecycle.revision(),
            window.sessionId(), window.windowId()));
        check(historical.appendSuppliedQuestCompletion(receipt, lifecycle.revision() + 10,
            UUID.randomUUID(), UUID.randomUUID()) == historical);
        rejects(() -> appendable.appendSuppliedQuestCompletion(receipt, lifecycle.revision() + 1,
            window.sessionId(), window.windowId()));
        rejects(() -> appendable.appendSuppliedQuestCompletion(receipt, lifecycle.revision(),
            UUID.randomUUID(), window.windowId()));
        var pausedAppendable = LifecycleAdmission.prepare(appendable, assignment.identity(), UUID.randomUUID(),
            lifecycle.revision(), RunReceipt.Operation.PAUSE);
        verifyObservationRefusals(pausedAppendable, receipt);
        rejects(() -> pausedAppendable.appendSuppliedQuestCompletion(receipt,
            pausedAppendable.lifecycle().revision(), window.sessionId(), window.windowId()));
        for (UUID collision : List.of(resolution.creation().transactionId(), resolution.commandId()))
        {
            var conflicting = new QuestFateCompletionReceipt(context, window, lifecycle.revision(),
                window.baselinePosition() + 1, collision, "quests", "completion");
            rejects(() -> appendable.appendSuppliedQuestCompletion(conflicting, lifecycle.revision(),
                window.sessionId(), window.windowId()));
        }
        check(appendable.questCompletionHistory().orElseThrow().journal().receipts().isEmpty());
        checkCodec.accept(appended);
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
        // Baselines derive the resumed head and retained selection, never caller assignments.
        UUID liveSession = UUID.randomUUID();
        UUID liveWindow = UUID.randomUUID();
        String questId = window.selection().selection().questId();
        var baseline = resumed.prepareSuppliedQuestObservationWindow(assignment.identity(),
            resumed.lifecycle().revision(), liveSession, liveWindow, 100, questId,
            QuestFateCompletion.QuestState.IN_PROGRESS);
        check(baseline.baselineRevision() == resumed.lifecycle().revision());
        check(baseline.sessionId().equals(liveSession) && baseline.windowId().equals(liveWindow));
        resumed.currentQuestAssignment().orElseThrow().requireFreshCompletion(baseline,
            resumed.lifecycle().revision(), liveSession, liveWindow, 101, questId,
            QuestFateCompletion.QuestState.COMPLETE);
        rejectsObservation(() -> resumed.prepareSuppliedQuestObservationWindow(assignment.identity(),
            lifecycle.revision(), liveSession, liveWindow, 100, questId,
            QuestFateCompletion.QuestState.IN_PROGRESS));
        rejectsObservation(() -> resumed.prepareSuppliedQuestObservationWindow(
            new RunIdentity("foreign", UUID.randomUUID()), resumed.lifecycle().revision(),
            liveSession, liveWindow, 100, questId, QuestFateCompletion.QuestState.IN_PROGRESS));
        rejectsObservation(() -> historical.prepareSuppliedQuestObservationWindow(assignment.identity(),
            lifecycle.revision(), liveSession, liveWindow, 100, questId,
            QuestFateCompletion.QuestState.IN_PROGRESS));
        rejectsObservation(() -> paused.prepareSuppliedQuestObservationWindow(assignment.identity(),
            paused.lifecycle().revision(), liveSession, liveWindow, 100, questId,
            QuestFateCompletion.QuestState.IN_PROGRESS));
        check(resumed.questCompletionHistory().isEmpty()); // Baseline does not upgrade legacy absence.
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

    private static RunDocument observe(RunDocument head, RunIdentity identity, long revision,
        QuestFateObservationWindow window, UUID session, UUID liveWindow, long position,
        String quest, QuestFateCompletion.QuestState state, UUID command)
    {
        return head.appendSuppliedQuestObservation(identity, revision, window, session, liveWindow,
            position, quest, state, command, "quests", "completion");
    }

    private static void verifyObservationRefusals(RunDocument head, QuestFateCompletionReceipt receipt)
    {
        var window = receipt.window();
        var identity = head.lifecycle().identity();
        long revision = head.lifecycle().revision();
        long position = receipt.observedPosition();
        String quest = window.selection().selection().questId();
        var complete = QuestFateCompletion.QuestState.COMPLETE;
        var command = receipt.commandId();
        rejectsObservation(() -> observe(head, new RunIdentity("foreign", UUID.randomUUID()), revision, window,
            window.sessionId(), window.windowId(), position, quest, complete, command));
        rejectsObservation(() -> observe(head, identity, revision + 1, window,
            window.sessionId(), window.windowId(), position, quest, complete, command));
        rejectsObservation(() -> observe(head, identity, revision, window,
            UUID.randomUUID(), window.windowId(), position, quest, complete, command));
        rejectsObservation(() -> observe(head, identity, revision, window,
            window.sessionId(), UUID.randomUUID(), position, quest, complete, command));
        rejectsObservation(() -> observe(head, identity, revision, window,
            window.sessionId(), window.windowId(), window.baselinePosition(), quest, complete, command));
        rejectsObservation(() -> observe(head, identity, revision, window,
            window.sessionId(), window.windowId(), position, "unrelated", complete, command));
        for (var state : QuestFateCompletion.QuestState.values())
        {
            if (state != complete)
            {
                rejectsObservation(() -> observe(head, identity, revision, window,
                    window.sessionId(), window.windowId(), position, quest, state, command));
            }
        }
        // Valid observations still require available history and an active current assignment.
        if (head.questCompletionHistory().isEmpty() || head.currentQuestAssignment().isEmpty()
            || head.lifecycle().state() != RunLifecycle.State.ACTIVE)
        {
            try { observe(head, identity, revision, window, window.sessionId(), window.windowId(),
                position, quest, complete, command); throw new AssertionError("Expected rejection"); }
            catch (IllegalArgumentException | IllegalStateException expected) { /* Fail closed. */ }
        }
    }

    private static void rejectsObservation(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected observation rejection");
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
