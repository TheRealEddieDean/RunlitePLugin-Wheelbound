package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandCurrentAttemptJsonTest
{
    @Test public void textRestartRetainsOriginalActivationAcrossPauseResumeAndAllGoals()
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity identity = new RunIdentity("current-json", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist initial = GrandFateChecklist.initial(seal);
            for (GrandFateChecklist.Objective objective : initial.unfinished())
            {
                // Exercise both empty and retained completed prefixes where the goal allows them.
                for (GrandFateChecklist checklist : List.of(initial, GrandFateChecklist.restore(seal,
                    initial.unfinished().size() > 1
                        ? Collections.singleton(initial.unfinished().stream().filter(o -> o != objective).findFirst().get())
                        : Collections.emptySet())))
                {
                    GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), identity, 0, checklist, objective);
                    GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(receipt));
                    GrandCurrentAttempt active = GrandCurrentAttempt.restore(receipt.commandId(), history,
                        receipt.attempt().lifecycle(), checklist);
                    for (GrandCurrentAttempt attempt : List.of(active, active.pause(identity, 1),
                        active.pause(identity, 1).resume(identity, 2), GrandCurrentAttempt.restore(receipt.commandId(), history,
                            new RunLifecycle(identity, Long.MAX_VALUE, RunLifecycle.State.PAUSED,
                                RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist)))
                    {
                        JsonObject json = new JsonParser().parse(GrandCurrentAttemptJson.encode(attempt, history,
                            attempt.lifecycle(), checklist).toString()).getAsJsonObject();
                        GrandCurrentAttempt restored = GrandCurrentAttemptJson.decode(json, history, attempt.lifecycle(), checklist);
                        assertEquals(receipt, restored.activation());
                        assertEquals(attempt.lifecycle().state(), restored.lifecycle().state());
                        assertEquals(attempt.lifecycle().revision(), restored.lifecycle().revision());
                    }
                }
            }
        }
    }

    @Test public void malformedChangedMissingSupersededAndStalePayloadsFailClosed()
    {
        RunIdentity identity = new RunIdentity("current-json", UUID.randomUUID());
        SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(3));
        GrandFateChecklist checklist = GrandFateChecklist.initial(seal);
        GrandFateChecklist.Objective objective = checklist.unfinished().iterator().next();
        GrandActivationReceipt receipt = new GrandActivationReceipt(UUID.randomUUID(), identity, 0, checklist, objective);
        GrandActivationHistory history = new GrandActivationHistory(seal, 1, List.of(receipt));
        GrandCurrentAttempt attempt = GrandCurrentAttempt.restore(receipt.commandId(), history, receipt.attempt().lifecycle(), checklist);
        List<Consumer<JsonObject>> mutations = List.of(
            j -> j.remove("activation"), j -> j.addProperty("activation", false),
            j -> j.addProperty("version", 2), j -> j.addProperty("version", "1"),
            j -> j.remove("version"), j -> j.addProperty("unknown", true),
            j -> j.getAsJsonObject("activation").addProperty("commandId", UUID.randomUUID().toString()),
            j -> j.getAsJsonObject("activation").addProperty("accountKey", "foreign"));
        for (Consumer<JsonObject> mutation : mutations)
        {
            JsonObject json = GrandCurrentAttemptJson.encode(attempt, history, attempt.lifecycle(), checklist);
            mutation.accept(json);
            assertThrows(RuntimeException.class, () -> GrandCurrentAttemptJson.decode(json, history, attempt.lifecycle(), checklist));
        }
        JsonObject json = GrandCurrentAttemptJson.encode(attempt, history, attempt.lifecycle(), checklist);
        GrandActivationReceipt changed = new GrandActivationReceipt(receipt.commandId(), identity, 0, checklist,
            checklist.unfinished().stream().filter(o -> o != objective).findFirst().get());
        GrandActivationHistory replaced = new GrandActivationHistory(seal, 1, List.of(changed));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.decode(json, replaced, attempt.lifecycle(), checklist));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.encode(attempt, replaced, attempt.lifecycle(), checklist));
        GrandActivationReceipt retry = new GrandActivationReceipt(UUID.randomUUID(), identity, 2, checklist, objective);
        GrandActivationHistory later = new GrandActivationHistory(seal, 3, List.of(receipt, retry));
        RunLifecycle head = new RunLifecycle(identity, 3, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.decode(json, later, head, checklist));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.decode(json,
            new GrandActivationHistory(seal, 1, Collections.emptyList()), attempt.lifecycle(), checklist));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.encode(attempt, history,
            attempt.pause(identity, 1).lifecycle(), checklist));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.decode(json, history,
            new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.ACTIVE_OBJECTIVE), checklist));
        assertThrows(IllegalArgumentException.class, () -> GrandCurrentAttemptJson.decode(json, history,
            attempt.lifecycle(), GrandFateChecklist.restore(seal, Collections.singleton(objective))));
    }
}
