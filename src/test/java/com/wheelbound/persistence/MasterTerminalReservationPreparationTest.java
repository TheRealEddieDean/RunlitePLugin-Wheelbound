package com.wheelbound.persistence;

import org.junit.Test;

public class MasterTerminalReservationPreparationTest
{
    @Test public void bindsExplicitBalanceAndReplaysOnlyRetainedReservation()
    { MasterTerminalReservationPreparationChecks.main(new String[0]); }
}
