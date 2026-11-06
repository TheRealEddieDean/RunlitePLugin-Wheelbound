package com.wheelbound.domain;

import org.junit.Test;

public class FateXpOfferSelectionTest
{
    @Test public void restoresFrozenSelectionAndRejectsChangedOrUnqualifiedSnapshots()
    { FateXpOfferSelectionChecks.main(new String[0]); }
}
