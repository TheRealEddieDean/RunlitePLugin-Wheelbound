package com.wheelbound.persistence;

import org.junit.Test;

public class GrandActivationAdmissionTest
{
    @Test public void admitsOnlyNoncollidingAggregateRequestsAndPreservesReplay()
    { GrandActivationAdmissionChecks.main(new String[0]); }
}
