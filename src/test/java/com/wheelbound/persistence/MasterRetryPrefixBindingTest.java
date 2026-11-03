package com.wheelbound.persistence;

import org.junit.Test;

public class MasterRetryPrefixBindingTest
{
    @Test public void emptyContinuationPreservesEveryRetainedDraw()
    { MasterRetryPrefixBindingChecks.main(new String[0]); }
}
