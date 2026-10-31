package com.wheelbound.persistence;

import org.junit.Test;

public class MasterAggregateEvidenceTest
{
    @Test public void bindsReadinessToAggregateAndRefusesOccupiedOrStaleContext()
    { MasterAggregateEvidenceChecks.main(new String[0]); }
}
