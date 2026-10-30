package com.wheelbound.domain;

import org.junit.Test;

public class GrandFateActivationTest
{
    @Test public void requiresCurrentActiveRunWithoutAnyOutstandingObligation()
    { GrandFateActivationChecks.main(new String[0]); }
}
