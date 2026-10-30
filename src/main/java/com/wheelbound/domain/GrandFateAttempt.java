package com.wheelbound.domain;

import java.util.Objects;

/**
 * WB-D104 attempt transition proposals. These values must be committed with the
 * complete run and a replay receipt before publication. No client path calls this
 * model; it does not validate evidence, settle penalties, award wins or debit spins.
 */
public final class GrandFateAttempt
{
    private final RunLifecycle lifecycle;
    private final GrandFateChecklist checklist;
    private final GrandFateChecklist.Objective objective;

    private GrandFateAttempt(RunLifecycle lifecycle, GrandFateChecklist checklist,
        GrandFateChecklist.Objective objective)
    {
        this.lifecycle = lifecycle;
        this.checklist = checklist;
        this.objective = objective;
    }

    /** Select exactly one unfinished objective; no implicit choice or randomness. */
    public static GrandFateAttempt activate(RunLifecycle current, GrandFateChecklist checklist,
        GrandFateChecklist.Objective objective, RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(checklist, "checklist");
        Objects.requireNonNull(objective, "objective");
        if (!current.permitsGrandFateActivation(expectedIdentity, expectedRevision)
            || !current.identity().equals(checklist.seal().identity()))
        { throw new IllegalArgumentException("Grand attempt requires current eligible run and seal"); }
        if (!checklist.isUnfinished(objective))
        { throw new IllegalArgumentException("Grand objective is not unfinished"); }
        RunLifecycle activated = new RunLifecycle(current.identity(),
            Math.addExact(current.revision(), 1), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        return new GrandFateAttempt(activated, checklist, objective);
    }

    public RunLifecycle lifecycle() { return lifecycle; }
    public GrandFateChecklist checklist() { return checklist; }
    public GrandFateChecklist.Objective objective() { return objective; }

    /** A structural success proposal, never proof of verified or durable completion. */
    public static final class Success
    {
        private final RunLifecycle lifecycle;
        private final GrandFateProgress progress;

        private Success(RunLifecycle lifecycle, GrandFateProgress progress)
        {
            this.lifecycle = lifecycle;
            this.progress = progress;
        }

        public RunLifecycle lifecycle() { return lifecycle; }
        public GrandFateProgress progress() { return progress; }
    }

    /**
     * WB-D104: retain completed components and end the selected attempt after success.
     * The caller must first authenticate evidence, validate the live session and settle
     * off-task audit, then atomically persist the receipt and complete aggregate before
     * publication. References alone grant no authority. This unwired proposal performs
     * none of those checks and awards neither a run win nor rewards. Even the final
     * component leaves the run ACTIVE pending a separately authorized completion command.
     * Exact command replay belongs to that durable boundary, not this transition.
     */
    public Success proposeSuccess(RunLifecycle current, GrandFateProgress progress,
        GrandFateProgress.Evidence reference, RunIdentity expectedIdentity, long expectedRevision)
    {
        checkCurrent(current, expectedIdentity, expectedRevision);
        Objects.requireNonNull(progress, "progress");
        Objects.requireNonNull(reference, "reference");
        if (!checklist.seal().equals(progress.checklist().seal())
            || !checklist.completed().equals(progress.checklist().completed())
            || reference.objective() != objective)
        { throw new IllegalArgumentException("Evidence must extend the selected attempt checklist"); }
        GrandFateProgress extended = progress.appendReference(reference, current.revision());
        return new Success(new RunLifecycle(current.identity(),
            Math.addExact(current.revision(), 1), RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), extended);
    }

    /**
     * Failure/exit proposal only: retains every checklist component and returns to
     * no active Fate. Any off-task audit/mandatory settlement must be handled by the
     * future atomic command; this method cannot discharge such a settlement.
     * Explicit activation is required for a subsequent attempt.
     * Supply the current durable lifecycle inside the transaction. Any intervening
     * revision (including pause/resume) requires future durable attempt reconciliation;
     * this proposal cannot clear a newer obligation or reactivate a paused run.
     */
    public RunLifecycle endUnsuccessful(RunLifecycle current,
        RunIdentity expectedIdentity, long expectedRevision)
    {
        checkCurrent(current, expectedIdentity, expectedRevision);
        return new RunLifecycle(lifecycle.identity(), Math.addExact(lifecycle.revision(), 1),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
    }

    private void checkCurrent(RunLifecycle current, RunIdentity expectedIdentity, long expectedRevision)
    {
        Objects.requireNonNull(current, "current");
        if (!lifecycle.identity().equals(expectedIdentity) || lifecycle.revision() != expectedRevision
            || !current.identity().equals(lifecycle.identity())
            || current.revision() != lifecycle.revision()
            || current.state() != RunLifecycle.State.ACTIVE
            || current.obligation() != RunLifecycle.Obligation.ACTIVE_OBJECTIVE)
        { throw new IllegalArgumentException("Stale or foreign attempt context"); }
    }
}
