package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandEvidenceJournalJsonTest
{
    private final RunIdentity identity = new RunIdentity("journal-json", UUID.randomUUID());

    private GrandEvidenceJournal journal(int ticket)
    {
        SealedGrandFate seal = new SealedGrandFate(identity, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
        List<GrandEvidenceReceipt> receipts = new ArrayList<>();
        List<GrandFateProgress.Evidence> references = new ArrayList<>();
        long revision = 1;
        for (GrandFateChecklist.Objective objective : GrandFateChecklist.initial(seal).required())
        {
            GrandEvidenceReceipt receipt = new GrandEvidenceReceipt(seal, objective, UUID.randomUUID(),
                revision, UUID.randomUUID(), "fixture/namespace", "event-\u96ea-" + revision);
            receipts.add(receipt);
            references.add(receipt.reference());
            revision += 2;
        }
        return new GrandEvidenceJournal(new GrandFateProgress(seal, revision, references), revision, receipts);
    }

    @Test public void fullPayloadSurvivesTextRoundTripForEveryGoalAndLaterHead()
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            GrandEvidenceJournal original = journal(ticket);
            JsonObject encoded = GrandEvidenceJournalJson.encode(original, Long.MAX_VALUE);
            GrandEvidenceJournal restored = GrandEvidenceJournalJson.decode(
                new JsonParser().parse(encoded.toString()).getAsJsonObject(), original.progress(), Long.MAX_VALUE);
            assertEquals(original.receipts(), restored.receipts());
            assertEquals(encoded, GrandEvidenceJournalJson.encode(restored, Long.MAX_VALUE));
            GrandEvidenceReceipt receipt = restored.receipts().values().iterator().next();
            RunLifecycle later = new RunLifecycle(identity, Long.MAX_VALUE,
                RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
            assertSame(restored, restored.proposeAppend(receipt, null, later, null, identity, Long.MAX_VALUE));
            GrandEvidenceReceipt collision = new GrandEvidenceReceipt(receipt.reference().seal(),
                receipt.reference().objective(), receipt.reference().receiptId(), receipt.attemptRevision(),
                receipt.sourceSessionId(), receipt.sourceNamespace(), "changed");
            assertThrows(IllegalArgumentException.class,
                () -> restored.proposeAppend(collision, null, later, null, identity, Long.MAX_VALUE));
        }
    }

    @Test public void emptyIsExplicitAndReferencesCannotSynthesizePayloads()
    {
        GrandEvidenceJournal populated = journal(0);
        SealedGrandFate seal = populated.progress().checklist().seal();
        GrandFateProgress empty = new GrandFateProgress(seal, 0, Collections.emptyList());
        JsonObject encoded = GrandEvidenceJournalJson.encode(
            new GrandEvidenceJournal(empty, 0, Collections.emptyList()), 0);
        assertTrue(GrandEvidenceJournalJson.decode(encoded, empty, 0).receipts().isEmpty());
        assertThrows(IllegalArgumentException.class,
            () -> GrandEvidenceJournalJson.decode(encoded, populated.progress(), Long.MAX_VALUE));
        assertThrows(IllegalArgumentException.class,
            () -> GrandEvidenceJournalJson.encode(populated, 0));
    }

    @Test public void rejectsForeignSealVersionsMalformedFieldsAndReceiptCorruption()
    {
        GrandEvidenceJournal original = journal(3);
        JsonObject valid = GrandEvidenceJournalJson.encode(original, Long.MAX_VALUE);
        for (String field : valid.keySet())
        {
            JsonObject missing = valid.deepCopy();
            missing.remove(field);
            rejects(missing, original);
        }
        JsonObject changed = valid.deepCopy(); changed.addProperty("version", 2); rejects(changed, original);
        changed = valid.deepCopy(); changed.addProperty("version", "1"); rejects(changed, original);
        changed = valid.deepCopy(); changed.addProperty("unknown", true); rejects(changed, original);
        changed = valid.deepCopy(); changed.addProperty("accountKey", "foreign"); rejects(changed, original);
        changed = valid.deepCopy(); changed.addProperty("runId", UUID.randomUUID().toString()); rejects(changed, original);
        changed = valid.deepCopy(); changed.addProperty("sealCommandId", UUID.randomUUID().toString()); rejects(changed, original);
        changed = valid.deepCopy(); changed.add("sealDraw", RunCodec.encodeDraw(GrandFatePool.atTicket(1))); rejects(changed, original);
        JsonObject first = valid.getAsJsonArray("receipts").get(0).getAsJsonObject();
        for (String field : first.keySet())
        {
            changed = valid.deepCopy(); changed.getAsJsonArray("receipts").get(0).getAsJsonObject().remove(field);
            rejects(changed, original);
        }
        changed = valid.deepCopy(); changed.getAsJsonArray("receipts").remove(0); rejects(changed, original);
        changed = valid.deepCopy(); changed.getAsJsonArray("receipts").add(first.deepCopy()); rejects(changed, original);
        for (String field : new String[]{"attemptRevision", "commandId", "objective", "sourceSessionId", "sourceNamespace", "eventKey"})
        {
            changed = valid.deepCopy();
            JsonObject item = changed.getAsJsonArray("receipts").get(0).getAsJsonObject();
            if (field.equals("attemptRevision")) { item.addProperty(field, Long.MAX_VALUE); }
            else { item.addProperty(field, " "); }
            rejects(changed, original);
        }
        assertThrows(IllegalArgumentException.class, () -> GrandEvidenceJournalJson.decode(valid, original.progress(), 0));
        if (valid.getAsJsonArray("receipts").size() > 1)
        {
            changed = valid.deepCopy();
            JsonArray reversed = new JsonArray();
            JsonArray entries = changed.getAsJsonArray("receipts");
            for (int i = entries.size() - 1; i >= 0; i--) { reversed.add(entries.get(i)); }
            changed.add("receipts", reversed); rejects(changed, original);
            changed = valid.deepCopy();
            JsonObject second = changed.getAsJsonArray("receipts").get(1).getAsJsonObject();
            for (String field : new String[]{"sourceSessionId", "sourceNamespace", "eventKey"})
            { second.add(field, first.get(field).deepCopy()); }
            rejects(changed, original);
        }
    }

    private void rejects(JsonObject value, GrandEvidenceJournal original)
    {
        assertThrows(RuntimeException.class,
            () -> GrandEvidenceJournalJson.decode(value, original.progress(), Long.MAX_VALUE));
    }
}
