package com.wheelbound.persistence;

import org.junit.Test;

public class GrandActivationPreparationTest
{
    @Test public void preparesCompleteSnapshotsWithoutChargingOrReactivatingReplay()
    { GrandActivationPreparationChecks.main(new String[0]); }
}
