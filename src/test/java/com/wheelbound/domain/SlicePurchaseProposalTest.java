package com.wheelbound.domain;

import org.junit.Test;

public class SlicePurchaseProposalTest
{
    @Test public void combinesDebitInstanceAndLifetimeHistoryWithoutMutatingSeed()
    { SlicePurchaseProposalChecks.successfulProposals(); }

    @Test public void rejectsWrongTierContextCapacityIdentityAndUnaffordablePurchases()
    { SlicePurchaseProposalChecks.rejectedProposals(); }
}
