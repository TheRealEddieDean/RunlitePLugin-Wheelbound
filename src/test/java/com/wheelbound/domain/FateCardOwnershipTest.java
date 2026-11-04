package com.wheelbound.domain;

import org.junit.Test;

public class FateCardOwnershipTest
{
    @Test public void ownershipRemainsSeparateAndRestoresWithoutInventedUnlocks()
    { FateCardOwnershipChecks.main(new String[0]); }
}
