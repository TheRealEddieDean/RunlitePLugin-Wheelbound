package com.wheelbound.persistence;

import com.google.gson.*;
import com.wheelbound.domain.*;
import java.util.*;
import java.util.function.Consumer;
import org.junit.Test;
import static org.junit.Assert.*;

public class FateCardOwnershipCodecTest
{
    private final RunIdentity identity = new RunIdentity("card-codec", new UUID(181, 1));

    @Test public void roundTripsAllIndependentPoolOwnershipCombinations()
    {
        Gson gson = new Gson();
        for (int mask = 0; mask < 16; mask++)
        {
            var skilling = EnumSet.of(FateCardOwnership.Type.STANDARD);
            var bossing = EnumSet.of(FateCardOwnership.Type.STANDARD);
            if ((mask & 1) != 0) { skilling.add(FateCardOwnership.Type.LESSER); }
            if ((mask & 2) != 0) { skilling.add(FateCardOwnership.Type.GREATER); }
            if ((mask & 4) != 0) { bossing.add(FateCardOwnership.Type.LESSER); }
            if ((mask & 8) != 0) { bossing.add(FateCardOwnership.Type.GREATER); }
            var original = new FateCardOwnership(1, identity, Map.of(
                FateCardOwnership.Pool.SKILLING, skilling, FateCardOwnership.Pool.BOSSING, bossing));
            String json = gson.toJson(FateCardOwnershipCodec.encode(original));
            var restored = FateCardOwnershipCodec.decode(new JsonParser().parse(json).getAsJsonObject(), identity);
            assertEquals(original, restored);
            assertEquals(json, gson.toJson(FateCardOwnershipCodec.encode(restored)));
        }
    }

    @Test public void refusesMalformedUnsupportedAndForeignDeclarations()
    {
        rejects(v -> v.remove("version"));
        rejects(v -> v.addProperty("extra", true));
        rejects(v -> v.addProperty("version", 2));
        rejects(v -> v.addProperty("version", 1.5));
        rejects(v -> v.addProperty("version", "1"));
        rejects(v -> v.addProperty("version", Long.MAX_VALUE));
        rejects(v -> v.addProperty("accountKey", "other"));
        rejects(v -> v.addProperty("runId", new UUID(181, 2).toString()));
        rejects(v -> v.addProperty("runId", "0-0-0-0-1"));
        rejects(v -> v.add("pools", JsonNull.INSTANCE));
        rejects(v -> v.getAsJsonObject("pools").remove("BOSSING"));
        rejects(v -> v.getAsJsonObject("pools").add("CHALLENGE", new JsonArray()));
        rejects(v -> v.getAsJsonObject("pools").add("BOSSING", new JsonArray()));
        rejects(v -> v.getAsJsonObject("pools").addProperty("BOSSING", "STANDARD"));
        rejects(v -> v.getAsJsonObject("pools").getAsJsonArray("BOSSING").add("STANDARD"));
        rejects(v -> v.getAsJsonObject("pools").getAsJsonArray("BOSSING").add("CHALLENGE"));
        rejects(v -> v.getAsJsonObject("pools").getAsJsonArray("BOSSING").add(1));
        rejects(v -> v.getAsJsonObject("pools").getAsJsonArray("BOSSING").add(JsonNull.INSTANCE));
    }

    private void rejects(Consumer<JsonObject> mutation)
    {
        JsonObject value = FateCardOwnershipCodec.encode(FateCardOwnership.starting(identity));
        mutation.accept(value);
        try { FateCardOwnershipCodec.decode(value, identity); fail("Accepted invalid ownership: " + value); }
        catch (IllegalArgumentException | ArithmeticException expected) { }
    }
}
