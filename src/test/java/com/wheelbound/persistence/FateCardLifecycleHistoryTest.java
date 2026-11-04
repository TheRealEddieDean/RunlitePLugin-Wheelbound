package com.wheelbound.persistence;

import org.junit.Test;

public class FateCardLifecycleHistoryTest
{
    @Test public void reconstructsCompleteCardAndLifecycleSegmentsAndRejectsUnexplainedChanges()
    { FateCardLifecycleHistoryChecks.main(new String[0]); }
}
