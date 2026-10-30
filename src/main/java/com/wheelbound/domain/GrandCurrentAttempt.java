package com.wheelbound.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Explicit current-attempt snapshot (WB-D089/104), independent of history presence.
 * Structural restore is not proof of session admission or intervening settlement.
 * No store or client calls this model yet; commit the complete aggregate before use.
 */
public final class GrandCurrentAttempt
{
    private final GrandActivationReceipt activation;
    private final RunLifecycle lifecycle;

    private GrandCurrentAttempt(GrandActivationReceipt activation, RunLifecycle lifecycle)
    {
        this.activation = activation;
        this.lifecycle = lifecycle;
    }

    /**
     * Restore only an explicitly persisted command, never infer activity from history.
     * The current checklist must still equal the activation prefix: success ends an attempt.
     */
    public static GrandCurrentAttempt restore(UUID activationCommand, GrandActivationHistory history,
        RunLifecycle current, GrandFateChecklist checklist)
    {
        Objects.requireNonNull(activationCommand, "activationCommand");
        Objects.requireNonNull(history, "history");
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(checklist, "checklist");
        new GrandActivationHistory(history.seal(), current.revision(), history.receipts().values());
        GrandActivationReceipt selected = history.receipts().get(activationCommand);
        if (selected == null || !current.identity().equals(history.seal().identity())
            || !history.seal().equals(checklist.seal())
            || current.obligation() != RunLifecycle.Obligation.ACTIVE_OBJECTIVE
            || (current.state() != RunLifecycle.State.ACTIVE && current.state() != RunLifecycle.State.PAUSED)
            || !selected.attempt().checklist().completed().equals(checklist.completed()))
        { throw new IllegalArgumentException("Missing or inconsistent current Grand attempt"); }
        for (GrandActivationReceipt receipt : history.receipts().values())
        {
            if (receipt.afterRevision() > selected.afterRevision())
            { throw new IllegalArgumentException("Current attempt was superseded by a later activation"); }
        }
        return new GrandCurrentAttempt(selected, current);
    }

    public GrandActivationReceipt activation() { return activation; }
    public RunLifecycle lifecycle() { return lifecycle; }

    /** Retains original activation identity and selection; paused observations stay inadmissible. */
    public GrandCurrentAttempt pause(RunIdentity identity, long revision)
    { return new GrandCurrentAttempt(activation, lifecycle.pause(identity, revision)); }

    /** Fresh live observation baselines/session admission remain the caller's responsibility. */
    public GrandCurrentAttempt resume(RunIdentity identity, long revision)
    { return new GrandCurrentAttempt(activation, lifecycle.resume(identity, revision)); }
}
