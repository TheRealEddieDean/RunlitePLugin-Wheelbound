package com.wheelbound.domain;

import org.junit.Test;

public class FateXpObjectiveTest
{
    @Test public void freezesExplicitTargetsWithoutGeneratingBalanceOrCreditingProgress()
    { FateXpObjectiveChecks.main(new String[0]); }
}
