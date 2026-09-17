package com.wheelbound;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.StructComposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class BossDataTest
{
    @Test public void localMappingLoadsAllTiersOnceAndNeverReadsCompletionState()
    {
        AtomicInteger reads = new AtomicInteger();
        Client client = client(reads, false);
        BossData data = new BossData();
        Map<String, List<Integer>> mapping = data.load(client);
        assertEquals(List.of(0, 1, 2, 3, 4, 5), mapping.get("Obor"));
        assertEquals(7, reads.get());
        data.load(client);
        assertEquals(7, reads.get());
        List<CaEncounter> encounters = data.encounters(client);
        assertEquals(1, encounters.size());
        assertEquals("Obor", encounters.get(0).name);
        for (int i = 0; i < CaTier.values().length; i++)
        {
            assertEquals(CaTier.values()[i], encounters.get(0).tasks.get(i).tier);
            assertEquals("Task " + i, encounters.get(0).tasks.get(i).name);
        }
        assertEquals(7, reads.get());
        data.clear(); data.load(client);
        assertEquals(14, reads.get());
        try { mapping.put("Fake", List.of(3)); fail(); }
        catch (UnsupportedOperationException expected) { }
    }

    @Test public void partialLoadIsNotPublishedAndCanRetry()
    {
        BossData data = new BossData();
        try { data.load(client(new AtomicInteger(), true)); fail(); }
        catch (IllegalStateException expected) { }
        assertEquals(6, data.load(client(new AtomicInteger(), false)).get("Obor").size());
    }

    private static Client client(AtomicInteger reads, boolean missingTier)
    {
        return (Client) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class<?>[]{Client.class}, (p, m, args) -> {
            if (m.getName().equals("getEnum"))
            {
                reads.incrementAndGet();
                int id = (Integer)args[0];
                if (missingTier && id == 3985) { return null; }
                return Proxy.newProxyInstance(EnumComposition.class.getClassLoader(), new Class<?>[]{EnumComposition.class}, (ep, em, ea) -> {
                    if (em.getName().equals("getStringValue")) { return "Obor"; }
                    if (em.getName().equals("getIntVals")) { return new int[]{id - 3981}; }
                    throw new AssertionError(em.getName());
                });
            }
            if (m.getName().equals("getStructComposition"))
            {
                int id = (Integer)args[0];
                return Proxy.newProxyInstance(StructComposition.class.getClassLoader(), new Class<?>[]{StructComposition.class}, (sp, sm, sa) -> {
                    if (sm.getName().equals("getIntValue")) { return (Integer)sa[0] == 1306 ? id : 1; }
                    if (sm.getName().equals("getStringValue") && (Integer)sa[0] == 1308) { return "Task " + id; }
                    throw new AssertionError(sm.getName());
                });
            }
            throw new AssertionError("Unexpected client call: " + m.getName());
        });
    }
}
