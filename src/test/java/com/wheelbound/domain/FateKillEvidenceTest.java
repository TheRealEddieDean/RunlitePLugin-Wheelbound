package com.wheelbound.domain;

import org.junit.Test;

public class FateKillEvidenceTest
{
    @Test public void preservesKillAttributionAndRefusesInactiveInvalidOrOverflowingCounts()
    { FateKillEvidenceChecks.main(new String[0]); }
}
