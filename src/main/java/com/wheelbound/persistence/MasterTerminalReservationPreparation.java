package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpinAllocation;
import com.wheelbound.domain.MasterSpinState;
import java.util.Objects;

/**
 * Prepares history and a reservation from one explicit aggregate spin balance.
 * This immutable proposal does not publish a lifecycle, assignment or obligation.
 * Storage admission must eventually publish all of those together before gameplay.
 */
public final class MasterTerminalReservationPreparation
{
    private final MasterSelectionResolutions resolutions;
    private final MasterSpinState spins;

    private MasterTerminalReservationPreparation(MasterSelectionResolutions resolutions,
        MasterSpinState spins)
    {
        this.resolutions = resolutions;
        this.spins = spins;
    }

    /** Never infer legacy balance or reconstruct a missing reservation from history. */
    public static MasterTerminalReservationPreparation prepare(RunDocument head,
        MasterSelectionResolutionReceipt request)
    {
        Objects.requireNonNull(head, "head");
        Objects.requireNonNull(request, "request");
        var spins = head.masterSpins().orElseThrow(() ->
            new IllegalArgumentException("Unavailable Master spin state"));
        var history = head.masterResolutions();
        if (history.filter(h -> h.receipts().containsKey(request.commandId())).isPresent())
        {
            var retained = history.get();
            MasterResolutionBinding.validateHistory(head, retained);
            retained.replay(request, head.lifecycle().identity(), head.lifecycle().revision());
            spins.replay(head.lifecycle().identity(), head.lifecycle().revision(), request);
            return new MasterTerminalReservationPreparation(retained, spins);
        }
        var terminal = MasterTerminalPreparation.prepare(head, spins.allotted(), request);
        return new MasterTerminalReservationPreparation(terminal.resolutions(),
            spins.reserve(head.lifecycle(), terminal.allocation()));
    }

    public MasterSelectionResolutions resolutions() { return resolutions; }
    public MasterSpinState spins() { return spins; }
    public MasterSpinAllocation allocation() { return spins.reservation().orElseThrow(); }
}
