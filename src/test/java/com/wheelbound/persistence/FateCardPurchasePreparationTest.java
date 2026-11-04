package com.wheelbound.persistence;

import org.junit.Test;

public class FateCardPurchasePreparationTest
{
    @Test public void preparesAgainstAggregateAndJournalAndRefusesPendingOrReusedCommands()
    { FateCardPurchasePreparationChecks.main(new String[0]); }
}
