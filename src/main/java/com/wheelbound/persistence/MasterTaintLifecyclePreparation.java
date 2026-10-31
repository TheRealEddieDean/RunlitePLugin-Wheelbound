package com.wheelbound.persistence;

import com.wheelbound.domain.MasterCurrentSpecialObligation;
import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSpecialObligation;
import com.wheelbound.domain.RunLifecycle;

/**
 * Couples a Taint terminal proposal to its mandatory pending lifecycle context.
 * Does not publish an aggregate, consume a spin, choose a punishment or admit gameplay.
 * Atomic operation receipts and durable publication remain required.
 */
public final class MasterTaintLifecyclePreparation
{
    private final MasterSpecialTerminalPreparation terminal;
    private final MasterCurrentSpecialObligation current;

    private MasterTaintLifecyclePreparation(MasterSpecialTerminalPreparation terminal,
        MasterCurrentSpecialObligation current)
    {
        this.terminal = terminal;
        this.current = current;
    }

    public static MasterTaintLifecyclePreparation prepare(RunDocument head,
        MasterSelectionResolutionReceipt request)
    {
        var terminal = MasterSpecialTerminalPreparation.prepare(head, request);
        if (terminal.obligation().requirement() != MasterSpecialObligation.Requirement.MANDATORY_PUNISHMENT)
        { throw new IllegalArgumentException("Sacrifice requires its supported route payload"); }
        boolean replay = head.masterResolutions()
            .filter(history -> history.receipts().containsKey(request.commandId())).isPresent();
        if (replay)
        {
            var retained = head.currentMasterSpecial().orElseThrow(() ->
                new IllegalArgumentException("Terminal history alone cannot restore current obligation"));
            retained.obligation().validateResolution(request);
            return new MasterTaintLifecyclePreparation(terminal, retained);
        }
        var next = new RunLifecycle(head.lifecycle().identity(), request.afterRevision(),
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.PUNISHMENT_PENDING);
        return new MasterTaintLifecyclePreparation(terminal, MasterCurrentSpecialObligation.restore(
            terminal.obligation(), terminal.resolutions(), terminal.spins(), next));
    }

    public MasterSpecialTerminalPreparation terminal() { return terminal; }
    public MasterCurrentSpecialObligation current() { return current; }
}
