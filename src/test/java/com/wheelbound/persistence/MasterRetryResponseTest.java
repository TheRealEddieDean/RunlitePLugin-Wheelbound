package com.wheelbound.persistence;

import org.junit.Test;

public class MasterRetryResponseTest
{
    @Test public void responsesSurviveTerminalCopiesAndHistoricalCreationLookup()
    { MasterRetryResponseChecks.main(new String[0]); }
}
