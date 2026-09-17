package com.wheelbound;

import java.util.List;
import java.util.Locale;

/** One wheel entry per CA encounter, never one wedge per task. */
final class CaEncounter
{
    enum Kind { MONSTER, BOSS, RAID }
    static final class Task
    {
        final int id;
        final CaTier tier;
        final String name;
        Task(int id, CaTier tier) { this(id, tier, null); }
        Task(int id, CaTier tier, String name)
        {
            this.id = id; this.tier = tier;
            this.name = name == null || name.isBlank() || name.equalsIgnoreCase("null")
                ? "Combat Achievement #" + id : name;
        }
    }
    final int id;
    final String name;
    final List<Task> tasks;
    final BossDefinition boss;
    final Kind kind;

    CaEncounter(int id, String name, List<Task> tasks)
    {
        this.id = id; this.name = name; this.tasks = List.copyOf(tasks);
        boss = BossCatalog.ALL.stream().filter(b -> b.caEncounters.stream()
            .anyMatch(alias -> normalize(alias).equals(normalize(name)))).findFirst().orElse(null);
        String normalized = normalize(name);
        boolean raid = normalized.startsWith("chambersofxeric") || normalized.startsWith("theatreofblood")
            || normalized.startsWith("tombsofamascut");
        kind = raid ? Kind.RAID : boss != null || normalized.equals("tzhaarketrakschallenges")
            ? Kind.BOSS : Kind.MONSTER;
    }
    static String normalize(String name)
    { return name.toLowerCase(Locale.ROOT).replaceFirst("^the\\s+", "").replaceAll("[^a-z0-9]", ""); }
}
