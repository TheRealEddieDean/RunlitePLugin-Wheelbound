package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandCurrentAttemptAggregateTest
{
    @Test public void structuralContracts() { GrandCurrentAttemptAggregateChecks.main(new String[0]); }

    @Test public void schemaElevenRefusesToDiscardCurrentAttempt()
    {
        RunIdentity id = new RunIdentity("current-codec-guard", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(0));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), id, 0, checklist,
            checklist.unfinished().iterator().next());
        GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(receipt));
        GrandCurrentAttempt attempt = GrandCurrentAttempt.restore(receipt.commandId(), history,
            receipt.attempt().lifecycle(), checklist);
        RunDocument document = GrandCurrentAttemptAggregateChecks.document(attempt.lifecycle(), seal, history, attempt, null);
        assertThrows(IllegalArgumentException.class, () -> new RunCodec(new Gson()).encode(document));
    }
}
