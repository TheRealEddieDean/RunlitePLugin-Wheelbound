package com.wheelbound.domain;

import org.junit.Test;

public class FateCardPurchaseReceiptTest
{
    @Test public void receiptBindsExactRequestAndReconstructsOriginalPayment()
    { FateCardPurchaseReceiptChecks.main(new String[0]); }
}
