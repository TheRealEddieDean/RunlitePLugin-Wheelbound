package com.wheelbound.persistence;

import org.junit.Test;

public class MasterSpinStateTest
{
    @Test public void reservesWithoutConsumingAndReplaysOnlyRetainedAccounting()
    { MasterSpinStateChecks.main(new String[0]); }
}
