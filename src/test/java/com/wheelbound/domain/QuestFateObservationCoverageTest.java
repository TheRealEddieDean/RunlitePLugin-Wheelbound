package com.wheelbound.domain;

import org.junit.Test;

public class QuestFateObservationCoverageTest
{
    @Test public void closesCoverageOnCompletionInterruptionAndUnorderedObservations()
    { QuestFateObservationCoverageChecks.main(new String[0]); }
}
