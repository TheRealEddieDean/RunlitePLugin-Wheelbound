package com.wheelbound.domain;

import java.util.UUID;

/** Supplied-value fixtures; no live or durable authority claims. */
public final class QuestFateAssignmentChecks
{
    public static void main(String[] args)
    {
        MasterSelectionResolutionReceipt resolution = QuestFateProvenanceChecks.resolution(false, true);
        QuestFateReward reward = QuestFateProvenanceChecks.reward("q1", "v1");
        QuestFateProvenance provenance = new QuestFateProvenance(resolution, new UUID(0, 2),
            "questing", "rng", "0", "3", reward);
        UUID assignment = UUID.randomUUID();
        long revision = resolution.afterRevision();
        QuestFateObservationWindow original = window(resolution.identity(), revision, assignment, reward);
        MasterSpinAllocation allocation = MasterSpinAllocation.restore(resolution, 5);
        QuestFateAssignment value = new QuestFateAssignment(provenance, allocation, original);
        QuestFateAssignment restored = new QuestFateAssignment(provenance,
            MasterSpinAllocation.restore(resolution, 5), original);
        check(value.equals(restored) && restored.equals(value) && value.hashCode() == restored.hashCode());
        check(value.assignmentId().equals(assignment) && value.identity().equals(resolution.identity())
            && value.creationRevision() == revision && value.provenance() == provenance
            && value.allocation() == allocation && value.originalWindow() == original);
        value.requireWindow(original);
        QuestFateObservationWindow refreshed = window(resolution.identity(), revision + 1, assignment, reward);
        value.requireWindow(refreshed);
        check(value.originalWindow() == original && !original.equals(refreshed));
        QuestFateObservationWindow completedResume = new QuestFateObservationWindow(
            context(resolution.identity(), revision + 1), assignment, UUID.randomUUID(), UUID.randomUUID(),
            0, reward.selection(), QuestFateCompletion.QuestState.COMPLETE);
        value.requireWindow(completedResume);
        rejects(() -> new QuestFateAssignment(provenance, allocation, completedResume));
        for (QuestFateObservationWindow foreign : new QuestFateObservationWindow[] {
            window(resolution.identity(), revision, UUID.randomUUID(), reward),
            window(new RunIdentity("other", resolution.identity().runId()), revision, assignment, reward),
            window(resolution.identity(), revision - 1, assignment, reward),
            window(resolution.identity(), revision, assignment, QuestFateProvenanceChecks.reward("q2", "v1"))})
        { rejects(() -> value.requireWindow(foreign)); }
        rejects(() -> new QuestFateAssignment(provenance, allocation,
            window(resolution.identity(), revision - 1, assignment, reward)));
        rejects(() -> new QuestFateAssignment(provenance, allocation,
            window(resolution.identity(), revision, assignment, QuestFateProvenanceChecks.reward("q2", "v1"))));
        rejects(() -> new QuestFateAssignment(provenance,
            MasterSpinAllocation.restore(QuestFateProvenanceChecks.resolution(false, true), 5), original));
        QuestFateAssignment later = new QuestFateAssignment(provenance, allocation, refreshed);
        rejects(() -> later.requireWindow(original));
        check(!value.equals(new QuestFateAssignment(provenance,
            MasterSpinAllocation.restore(resolution, 6), original)));
        check(!value.equals(later) && !value.equals(null) && !value.equals(provenance));
        rejects(() -> new QuestFateAssignment(null, allocation, original));
        rejects(() -> new QuestFateAssignment(provenance, null, original));
        rejects(() -> new QuestFateAssignment(provenance, allocation, null));
        rejects(() -> value.requireWindow(null));
        check(allocation.spinsBefore() == 5 && allocation.spinsAfter() == 4);
        System.out.println("QuestFateAssignmentChecks PASSED");
    }

    private static RunLifecycle context(RunIdentity identity, long revision)
    { return new RunLifecycle(identity, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE); }
    private static QuestFateObservationWindow window(RunIdentity identity, long revision,
        UUID assignment, QuestFateReward reward)
    { return new QuestFateObservationWindow(context(identity, revision), assignment,
        UUID.randomUUID(), UUID.randomUUID(), 0, reward.selection(), QuestFateCompletion.QuestState.IN_PROGRESS); }
    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Expected invalid assignment rejection");
    }
}
