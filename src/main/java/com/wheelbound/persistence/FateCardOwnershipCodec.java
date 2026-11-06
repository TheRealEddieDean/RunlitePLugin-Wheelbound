package com.wheelbound.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wheelbound.domain.FateCardOwnership;
import com.wheelbound.domain.RunIdentity;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Candidate ownership payload for the card migration. Does not enable aggregate
 * persistence or authenticate ownership. Absent legacy state must remain absent.
 * Purchase journals and aggregate history must be migrated and validated separately.
 */
final class FateCardOwnershipCodec
{
    private FateCardOwnershipCodec() { }

    static JsonObject encode(FateCardOwnership ownership)
    {
        Objects.requireNonNull(ownership, "ownership");
        JsonObject value = new JsonObject();
        value.addProperty("version", ownership.version());
        value.addProperty("accountKey", ownership.identity().accountKey());
        value.addProperty("runId", ownership.identity().runId().toString());
        JsonObject pools = new JsonObject();
        for (var pool : FateCardOwnership.Pool.values())
        {
            JsonArray types = new JsonArray();
            // Enum order makes output independent of input set iteration order.
            for (var type : FateCardOwnership.Type.values())
            {
                if (ownership.owns(pool, type)) { types.add(type.name()); }
            }
            pools.add(pool.name(), types);
        }
        value.add("pools", pools);
        return value;
    }

    static FateCardOwnership decode(JsonObject value, RunIdentity expectedIdentity)
    {
        Objects.requireNonNull(expectedIdentity, "expectedIdentity");
        RunCodec.keys(value, Set.of("version", "accountKey", "runId", "pools"));
        int version = Math.toIntExact(RunCodec.integer(value, "version"));
        String runId = RunCodec.string(value, "runId");
        UUID uuid = UUID.fromString(runId);
        if (!uuid.toString().equals(runId))
        { throw new IllegalArgumentException("Noncanonical card run identity"); }
        RunIdentity identity = new RunIdentity(RunCodec.string(value, "accountKey"), uuid);
        if (!expectedIdentity.equals(identity))
        { throw new IllegalArgumentException("Foreign card ownership"); }
        JsonElement rawPools = value.get("pools");
        if (!rawPools.isJsonObject()) { throw new IllegalArgumentException("Expected card pools"); }
        JsonObject pools = rawPools.getAsJsonObject();
        RunCodec.keys(pools, Set.of("SKILLING", "BOSSING"));
        var owned = new EnumMap<FateCardOwnership.Pool, Set<FateCardOwnership.Type>>(FateCardOwnership.Pool.class);
        for (var pool : FateCardOwnership.Pool.values())
        {
            JsonElement raw = pools.get(pool.name());
            if (!raw.isJsonArray()) { throw new IllegalArgumentException("Expected card types"); }
            var types = EnumSet.noneOf(FateCardOwnership.Type.class);
            for (JsonElement type : raw.getAsJsonArray())
            {
                if (!type.isJsonPrimitive() || !type.getAsJsonPrimitive().isString())
                { throw new IllegalArgumentException("Expected card type string"); }
                if (!types.add(FateCardOwnership.Type.valueOf(type.getAsString())))
                { throw new IllegalArgumentException("Duplicate card type"); }
            }
            owned.put(pool, types);
        }
        return new FateCardOwnership(version, identity, owned);
    }
}
