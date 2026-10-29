package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wheelbound.domain.*;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurchaseReceiptJsonTest
{
    private final RunIdentity identity = new RunIdentity("account:profile", UUID.randomUUID());

    private SlicePurchaseReceipt receipt(SliceInventory.PurchaseKind kind, long ordinal)
    {
        return new SlicePurchaseReceipt(identity, UUID.randomUUID(),
            new SlicePurchaseProposal.Quote("mining", "test-only", "v1", 7, kind, ordinal),
            new FatePointDebits.Receipt(UUID.randomUUID(), 12, 13, "test-only", "v1", 7, 93));
    }

    @Test public void preservesFrozenRequestAndOriginalResponse()
    {
        for (SlicePurchaseReceipt source : new SlicePurchaseReceipt[]{
            receipt(SliceInventory.PurchaseKind.FIRST_INSTANCE, 0),
            receipt(SliceInventory.PurchaseKind.PAID_DUPLICATE, Long.MAX_VALUE)})
        {
            SlicePurchaseReceipt restored = PurchaseReceiptJson.decode(PurchaseReceiptJson.encode(source), identity);
            assertTrue(restored.matches(identity, source.debit().commandId(), 12, source.instanceId(), source.quote()));
            assertEquals(13, restored.debit().afterRevision());
            assertEquals(93, restored.debit().balance());
            assertEquals(7, restored.debit().amount());
        }
    }

    @Test public void rejectsForeignIdentityAndMalformedFieldsWithoutChangingInput()
    {
        JsonObject original = PurchaseReceiptJson.encode(receipt(SliceInventory.PurchaseKind.PAID_DUPLICATE, 1));
        assertThrows(IllegalArgumentException.class, () -> PurchaseReceiptJson.decode(original,
            new RunIdentity("other", identity.runId())));
        for (String key : original.keySet())
        {
            JsonObject missing = original.deepCopy(); missing.remove(key);
            assertThrows(IllegalArgumentException.class, () -> PurchaseReceiptJson.decode(missing, identity));
        }
        for (String key : new String[]{"amount", "beforeRevision", "afterRevision", "duplicateOrdinal", "balance"})
        {
            JsonObject malformed = original.deepCopy(); malformed.add(key, new JsonPrimitive("7"));
            assertThrows(IllegalArgumentException.class, () -> PurchaseReceiptJson.decode(malformed, identity));
        }
        JsonObject invalid = original.deepCopy(); invalid.addProperty("duplicateOrdinal", 0);
        assertThrows(IllegalArgumentException.class, () -> PurchaseReceiptJson.decode(invalid, identity));
        JsonObject extra = original.deepCopy(); extra.addProperty("extra", true);
        assertThrows(IllegalArgumentException.class, () -> PurchaseReceiptJson.decode(extra, identity));
        assertEquals(1, original.get("duplicateOrdinal").getAsLong());
    }
}
