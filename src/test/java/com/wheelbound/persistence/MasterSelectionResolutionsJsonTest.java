package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wheelbound.domain.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSelectionResolutionsJsonTest
{
    private final RunIdentity identity = new RunIdentity("account/profile", new UUID(4, 5));

    private MasterSelectionResolutionReceipt receipt(int id, long revision, int emptyCount)
    {
        MasterSelectionProvenance source = MasterSelectionProvenanceJsonTest.fixture(null, emptyCount, true);
        MasterSelectionProvenance terminal = new MasterSelectionProvenance(new UUID(0, id),
            source.snapshotVersion(), source.catalogVersion(), source.streamDescriptor(),
            source.initialStreamState(), source.readiness(), source.landings().stream()
                .map(MasterSelectionProvenance.Landing::step).collect(java.util.stream.Collectors.toList()));
        MasterSelectionProvenance initial = new MasterSelectionProvenance(terminal.transactionId(),
            terminal.snapshotVersion(), terminal.catalogVersion(), terminal.streamDescriptor(),
            terminal.initialStreamState(), terminal.readiness(), List.of());
        return new MasterSelectionResolutionReceipt(new UUID(1, id), revision + 1,
            new MasterSelectionCreationReceipt(identity, revision, initial), terminal);
    }

    @Test public void orderedTextRoundTripRetainsReplayAndLongEmptyHistory()
    {
        MasterSelectionResolutionReceipt first = receipt(10, 0, 513);
        MasterSelectionResolutionReceipt second = receipt(11, 10, 0);
        for (List<MasterSelectionResolutionReceipt> entries : List.of(
            List.<MasterSelectionResolutionReceipt>of(), List.of(first, second)))
        {
            MasterSelectionResolutions original = new MasterSelectionResolutions(identity, 12, entries);
            String text = MasterSelectionResolutionsJson.encode(original, 12).toString();
            JsonObject input = new JsonParser().parse(text).getAsJsonObject();
            MasterSelectionResolutions restored = MasterSelectionResolutionsJson.decode(input, identity, 12);
            assertEquals(entries, List.copyOf(restored.receipts().values()));
            assertEquals(text, MasterSelectionResolutionsJson.encode(restored, 12).toString());
            input.getAsJsonArray("receipts").add(JsonNull.INSTANCE);
            assertEquals(text, MasterSelectionResolutionsJson.encode(restored, 12).toString());
            for (MasterSelectionResolutionReceipt entry : entries)
            { assertEquals(entry, restored.replay(entry, identity, 12)); }
        }
        MasterSelectionResolutions history = new MasterSelectionResolutions(identity, 12, List.of(first, second));
        assertThrows(IllegalArgumentException.class, () -> MasterSelectionResolutionsJson.encode(history, 11));
    }

    private void rejects(Consumer<JsonObject> corrupt)
    {
        JsonObject input = MasterSelectionResolutionsJson.encode(new MasterSelectionResolutions(identity,
            12, List.of(receipt(10, 0, 1), receipt(11, 10, 0))), 12);
        corrupt.accept(input);
        String before = input.toString();
        assertThrows(RuntimeException.class, () -> MasterSelectionResolutionsJson.decode(input, identity, 12));
        assertEquals(before, input.toString());
    }

    @Test public void malformedForeignRepeatedAndDisorderedHistoriesFailClosed()
    {
        rejects(j -> j.addProperty("version", 2));
        rejects(j -> j.addProperty("version", "1"));
        rejects(j -> j.addProperty("version", 1.5));
        rejects(j -> j.addProperty("extra", true));
        rejects(j -> j.remove("receipts"));
        rejects(j -> j.add("receipts", JsonNull.INSTANCE));
        rejects(j -> j.add("receipts", new JsonObject()));
        rejects(j -> j.getAsJsonArray("receipts").add(JsonNull.INSTANCE));
        rejects(j -> j.getAsJsonArray("receipts").add(new JsonArray()));
        rejects(j -> j.addProperty("accountKey", "foreign"));
        rejects(j -> j.addProperty("runId", new UUID(7, 8).toString()));
        rejects(j -> j.getAsJsonArray("receipts").add(j.getAsJsonArray("receipts").get(0).deepCopy()));
        rejects(j -> {
            JsonArray entries = j.getAsJsonArray("receipts");
            entries.add(entries.remove(0));
        });
        rejects(j -> j.getAsJsonArray("receipts").get(1).getAsJsonObject()
            .getAsJsonObject("creation").addProperty("accountKey", "foreign"));
        JsonObject input = MasterSelectionResolutionsJson.encode(new MasterSelectionResolutions(identity,
            12, List.of(receipt(10, 10, 0))), 12);
        assertThrows(IllegalArgumentException.class, () -> MasterSelectionResolutionsJson.decode(input, identity, 11));
        assertThrows(IllegalArgumentException.class, () -> MasterSelectionResolutionsJson.decode(input, identity, -1));
    }
}
