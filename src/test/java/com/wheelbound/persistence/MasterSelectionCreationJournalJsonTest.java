package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.MasterSelectionCreationJournal;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import com.wheelbound.domain.MasterSelectionProvenance;
import com.wheelbound.domain.RunIdentity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSelectionCreationJournalJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterSelectionCreationReceipt receipt(long revision, long transaction)
    {
        MasterSelectionProvenance base = MasterSelectionProvenanceJsonTest.fixture(null, 0, false);
        return new MasterSelectionCreationReceipt(identity, revision,
            new MasterSelectionProvenance(new UUID(0, transaction), base.snapshotVersion(),
                base.catalogVersion(), base.streamDescriptor(), base.initialStreamState(),
                base.readiness(), List.of()));
    }

    private JsonObject fixture()
    {
        return MasterSelectionCreationJournalJson.encode(new MasterSelectionCreationJournal(identity, 9,
            List.of(receipt(0, 1), receipt(1, 2), receipt(8, 3))), 9);
    }

    @Test public void textRoundTripRetainsOrderedOriginalsForHistoricalReplay()
    {
        String text = fixture().toString();
        JsonObject input = new JsonParser().parse(text).getAsJsonObject();
        MasterSelectionCreationJournal restored = MasterSelectionCreationJournalJson.decode(input, identity, 100);
        assertEquals(List.of(receipt(0, 1), receipt(1, 2), receipt(8, 3)),
            new ArrayList<>(restored.receipts().values()));
        assertEquals(receipt(0, 1), restored.replay(receipt(0, 1), identity, 100));
        input.getAsJsonArray("receipts").get(0).getAsJsonObject().getAsJsonObject("initial")
            .addProperty("initialStreamState", "changed input");
        assertEquals(text, MasterSelectionCreationJournalJson.encode(restored, 100).toString());
        assertThrows(IllegalArgumentException.class, () -> MasterSelectionCreationJournalJson.encode(restored, 8));
        assertThrows(UnsupportedOperationException.class, () -> restored.receipts().clear());
    }

    @Test public void emptyJournalIsExplicitAndStillChecksIdentityAndHead()
    {
        for (long head : new long[] {0, Long.MAX_VALUE})
        {
            MasterSelectionCreationJournal empty = new MasterSelectionCreationJournal(identity, head, List.of());
            JsonObject encoded = MasterSelectionCreationJournalJson.encode(empty, head);
            assertTrue(MasterSelectionCreationJournalJson.decode(encoded, identity, head).receipts().isEmpty());
            assertThrows(IllegalArgumentException.class,
                () -> MasterSelectionCreationJournalJson.decode(encoded, identity, -1));
            assertThrows(IllegalArgumentException.class, () -> MasterSelectionCreationJournalJson.decode(encoded,
                new RunIdentity("foreign", identity.runId()), head));
            assertThrows(IllegalArgumentException.class, () -> MasterSelectionCreationJournalJson.decode(encoded, null, head));
        }
        MasterSelectionCreationJournal boundary = new MasterSelectionCreationJournal(identity, Long.MAX_VALUE,
            List.of(receipt(Long.MAX_VALUE - 1, 1)));
        assertEquals(receipt(Long.MAX_VALUE - 1, 1), MasterSelectionCreationJournalJson.decode(
            MasterSelectionCreationJournalJson.encode(boundary, Long.MAX_VALUE), identity, Long.MAX_VALUE)
            .receipts().values().iterator().next());
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = fixture();
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterSelectionCreationJournalJson.decode(input, identity, 9));
        assertEquals(before, input.toString());
    }

    @Test public void malformedForeignDuplicateUnorderedOverlappingAndFutureHistoryFailsClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", 1.5));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("receipts"));
        rejects(j -> j.add("receipts", JsonNull.INSTANCE));
        rejects(j -> j.add("receipts", new JsonObject()));
        rejects(j -> j.addProperty("accountKey", "foreign"));
        rejects(j -> j.addProperty("runId", new UUID(6, 7).toString()));
        rejects(j -> j.getAsJsonArray("receipts").add(JsonNull.INSTANCE));
        rejects(j -> j.getAsJsonArray("receipts").add("invalid"));
        rejects(j -> j.getAsJsonArray("receipts").add(j.getAsJsonArray("receipts").get(0)));
        rejects(j -> j.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("accountKey", "foreign"));
        rejects(j -> j.getAsJsonArray("receipts").get(0).getAsJsonObject().addProperty("version", 2));
        rejects(j -> j.getAsJsonArray("receipts").add(MasterSelectionCreationReceiptJson.encode(receipt(9, 4))));
        rejects(j -> {
            JsonArray entries = new JsonArray();
            entries.add(MasterSelectionCreationReceiptJson.encode(receipt(1, 2)));
            entries.add(MasterSelectionCreationReceiptJson.encode(receipt(0, 1)));
            j.add("receipts", entries);
        });
        rejects(j -> {
            JsonArray entries = new JsonArray();
            entries.add(MasterSelectionCreationReceiptJson.encode(receipt(0, 1)));
            entries.add(MasterSelectionCreationReceiptJson.encode(receipt(0, 2)));
            j.add("receipts", entries);
        });
    }
}
