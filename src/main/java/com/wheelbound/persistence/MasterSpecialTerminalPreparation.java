package com.wheelbound.persistence;

import com.wheelbound.domain.MasterSelectionResolutionReceipt;
import com.wheelbound.domain.MasterSelectionResolutions;
import com.wheelbound.domain.MasterSpecialObligation;
import com.wheelbound.domain.MasterSpinState;

/**
 * Coupled special-only terminal proposal from one aggregate head. No lifecycle,
 * current-obligation publication, storage admission or settlement is performed.
 * A retained reservation alone cannot prove that an obligation was published.
 */
public final class MasterSpecialTerminalPreparation
{
    private final MasterTerminalReservationPreparation reservation;
    private final MasterTerminalOperationProposal response;
    private final MasterSpecialObligation obligation;

    private MasterSpecialTerminalPreparation(MasterTerminalReservationPreparation reservation,
        MasterSpecialObligation obligation)
    {
        this.reservation = reservation;
        this.obligation = obligation;
        this.response = MasterTerminalOperationProposal.restore(
            reservation.allocation().resolution(), reservation.allocation().spinsBefore());
        obligation.validateResolution(response.resolution());
    }

    /** Ordinary outcomes require a separate supported assignment implementation. */
    public static MasterSpecialTerminalPreparation prepare(RunDocument head,
        MasterSelectionResolutionReceipt request)
    {
        var obligation = new MasterSpecialObligation(request);
        return new MasterSpecialTerminalPreparation(
            MasterTerminalReservationPreparation.prepare(head, request), obligation);
    }

    public MasterSelectionResolutions resolutions() { return reservation.resolutions(); }
    public MasterSpinState spins() { return reservation.spins(); }
    public MasterTerminalOperationProposal response() { return response; }
    public MasterSpecialObligation obligation() { return obligation; }
}
