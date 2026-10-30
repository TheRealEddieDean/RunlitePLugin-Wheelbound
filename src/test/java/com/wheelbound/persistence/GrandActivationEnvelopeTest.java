package com.wheelbound.persistence;

import org.junit.Test;

public class GrandActivationEnvelopeTest
{
    @Test public void validatesOriginalPayloadHistoryAndOperationChain()
    { GrandActivationEnvelopeChecks.main(new String[0]); }

    @Test(expected = IllegalArgumentException.class)
    public void refusesSchema12PayloadLoss()
    {
        com.wheelbound.domain.RunIdentity id = new com.wheelbound.domain.RunIdentity(
            "codec-activation", java.util.UUID.randomUUID());
        com.wheelbound.domain.SealedGrandFate seal = new com.wheelbound.domain.SealedGrandFate(
            id, java.util.UUID.randomUUID(), com.wheelbound.domain.GrandFatePool.atTicket(0));
        com.wheelbound.domain.GrandFateChecklist checklist =
            com.wheelbound.domain.GrandFateChecklist.initial(seal);
        com.wheelbound.domain.GrandActivationReceipt activation =
            new com.wheelbound.domain.GrandActivationReceipt(java.util.UUID.randomUUID(), id, 0,
                checklist, checklist.unfinished().iterator().next());
        RunDocument document = new RunDocument(activation.attempt().lifecycle(), "test",
            java.util.List.of(RunReceipt.grandActivation(activation)), java.util.Optional.empty(),
            java.util.Optional.empty(), java.util.Optional.of(seal), java.util.Optional.empty(),
            java.util.Optional.empty(), java.util.Optional.of(new com.wheelbound.domain.GrandActivationHistory(
                seal, 1, java.util.List.of(activation))));
        new RunCodec(new com.google.gson.Gson()).encode(document);
    }
}
