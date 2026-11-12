package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** In-memory collision checks; no durable or live evidence claims. */
public final class QuestFateCompletionJournalChecks
{
    public static void main(String[] args)
    {
        var assignment = QuestFateAssignmentChecks.fixture();
        long revision = assignment.creationRevision() + 1;
        var current = current(assignment, revision, RunLifecycle.State.ACTIVE);
        var window = assignment.originalWindow();
        var receipt = receipt(current, window, revision, UUID.randomUUID(), "event-1");
        var empty = new QuestFateCompletionJournal(assignment.identity(), List.of());
        var journal = empty.append(receipt, current, revision, window.sessionId(), window.windowId());
        check(empty.receipts().isEmpty() && journal.receipts().equals(List.of(receipt)));
        check(journal.identity().equals(assignment.identity()));
        var replay = receipt(current, window, revision, receipt.commandId(), "event-1");
        // Retained replay is resolved without requalifying against a changed/paused head.
        check(journal.append(replay, current(assignment, revision + 1, RunLifecycle.State.PAUSED),
            revision + 1, UUID.randomUUID(), UUID.randomUUID()) == journal);
        rejects(() -> journal.append(receipt(current, window, revision, receipt.commandId(), "changed"),
            current, revision, window.sessionId(), window.windowId()));
        rejects(() -> journal.append(receipt(current, window, revision, UUID.randomUUID(), "event-1"),
            current, revision, window.sessionId(), window.windowId()));
        rejects(() -> journal.append(receipt(current, window, revision, UUID.randomUUID(), "event-2"),
            current, revision, window.sessionId(), window.windowId()));
        rejects(() -> empty.append(receipt, current, revision + 1, window.sessionId(), window.windowId()));
        rejects(() -> empty.append(receipt, current, revision, UUID.randomUUID(), window.windowId()));
        rejects(() -> empty.append(receipt, current(assignment, revision, RunLifecycle.State.PAUSED),
            revision, window.sessionId(), window.windowId()));

        // Distinct assignment ID still cannot consume the original source event.
        var otherWindow = new QuestFateObservationWindow(new RunLifecycle(assignment.identity(),
            assignment.creationRevision(), RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE),
            UUID.randomUUID(), window.sessionId(), window.windowId(), window.baselinePosition(),
            window.selection(), window.baseline());
        var other = new QuestFateAssignment(assignment.provenance(), assignment.allocation(), otherWindow);
        var otherCurrent = current(other, revision, RunLifecycle.State.ACTIVE);
        var collision = receipt(otherCurrent, otherWindow, revision, UUID.randomUUID(), "event-1");
        rejects(() -> new QuestFateCompletionJournal(assignment.identity(), List.of(receipt, collision)));
        rejects(() -> journal.append(collision, otherCurrent, revision,
            otherWindow.sessionId(), otherWindow.windowId()));
        var distinct = receipt(otherCurrent, otherWindow, revision, UUID.randomUUID(), "event-2");
        var reusedCommand = receipt(otherCurrent, otherWindow, revision, receipt.commandId(), "event-2");
        rejects(() -> new QuestFateCompletionJournal(assignment.identity(), List.of(receipt, reusedCommand)));
        rejects(() -> journal.append(reusedCommand, otherCurrent, revision,
            otherWindow.sessionId(), otherWindow.windowId()));
        var next = journal.append(distinct, otherCurrent, revision, otherWindow.sessionId(), otherWindow.windowId());
        check(next.receipts().equals(List.of(receipt, distinct)) && journal.receipts().size() == 1);
        var supplied = new ArrayList<>(next.receipts());
        var restored = new QuestFateCompletionJournal(assignment.identity(), supplied);
        supplied.clear();
        check(restored.receipts().equals(next.receipts()));
        try { restored.receipts().clear(); throw new AssertionError(); }
        catch (UnsupportedOperationException expected) { /* Immutable view. */ }
        rejects(() -> new QuestFateCompletionJournal(assignment.identity(), List.of(receipt, replay)));
        rejects(() -> new QuestFateCompletionJournal(new RunIdentity("foreign", UUID.randomUUID()), List.of(receipt)));
        rejects(() -> new QuestFateCompletionJournal(null, List.of()));
        rejects(() -> new QuestFateCompletionJournal(assignment.identity(), null));
        rejects(() -> empty.append(null, current, revision, window.sessionId(), window.windowId()));
        check(empty.receipts().isEmpty() && journal.receipts().equals(List.of(receipt)));
        System.out.println("QuestFateCompletionJournalChecks PASSED");
    }

    private static QuestFateCompletionReceipt receipt(QuestFateCurrentAssignment current,
        QuestFateObservationWindow window, long revision, UUID command, String event)
    { return new QuestFateCompletionReceipt(current, window, revision, window.baselinePosition() + 1,
        command, "quests", event); }

    private static QuestFateCurrentAssignment current(QuestFateAssignment assignment,
        long revision, RunLifecycle.State state)
    {
        return QuestFateCurrentAssignment.restore(assignment,
            new MasterSelectionResolutions(assignment.identity(), revision, List.of(assignment.provenance().resolution())),
            new MasterSpinState(MasterSpinState.VERSION, assignment.identity(), revision,
                assignment.allocation().spinsBefore(), Optional.of(assignment.allocation())),
            new RunLifecycle(assignment.identity(), revision, state, RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException expected) { return; }
        throw new AssertionError("Expected journal refusal");
    }
}
