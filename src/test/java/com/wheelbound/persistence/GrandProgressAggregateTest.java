package com.wheelbound.persistence;

import org.junit.Test;

public class GrandProgressAggregateTest
{
    @Test public void validatesSealRevisionAndOperationNamespaces()
    { GrandProgressAggregateChecks.main(new String[0]); }

    @Test(expected = IllegalArgumentException.class)
    public void schemaEightCannotSilentlyDiscardProgressReferences()
    {
        com.wheelbound.domain.RunIdentity id = new com.wheelbound.domain.RunIdentity(
            "codec-account", java.util.UUID.randomUUID());
        com.wheelbound.domain.SealedGrandFate seal = new com.wheelbound.domain.SealedGrandFate(
            id, java.util.UUID.randomUUID(), com.wheelbound.domain.GrandFatePool.atTicket(0));
        RunDocument document = new RunDocument(new com.wheelbound.domain.RunLifecycle(id, 0,
            com.wheelbound.domain.RunLifecycle.State.NOT_STARTED,
            com.wheelbound.domain.RunLifecycle.Obligation.NONE), "test", java.util.List.of(),
            java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.of(seal),
            java.util.Optional.of(new com.wheelbound.domain.GrandFateProgress(seal, 0, java.util.List.of())));
        new RunCodec(new com.google.gson.Gson()).encode(document);
    }
}
