package com.wheelbound;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntUnaryOperator;

/** Client-thread-only, account-local state. Static task metadata is owned separately by BossData. */
final class CombatAchievementCache
{
    private String account;
    private boolean ready;
    private Set<String> incomplete = Set.of();
    private int[] completion = new int[0];

    void reset()
    {
        account = null;
        ready = false;
        incomplete = Set.of();
        completion = new int[0];
    }

    void refresh(String identity, Map<String, List<Integer>> tasks,
        IntUnaryOperator readVarp)
    {
        reset();
        if (identity == null || tasks.isEmpty()) { return; }
        int[] flags = new int[BossData.COMPLETION.length];
        for (int i = 0; i < flags.length; i++) { flags[i] = readVarp.applyAsInt(BossData.COMPLETION[i]); }
        Set<String> open = new HashSet<>();
        for (BossDefinition boss : BossCatalog.ALL)
        {
            for (String encounter : boss.caEncounters)
            {
                for (int id : tasks.getOrDefault(encounter, List.of()))
                {
                    if (id >= 0 && id / 32 < flags.length && !BossData.isComplete(flags[id / 32], id % 32))
                    {
                        open.add(boss.hiscore.name());
                    }
                }
            }
        }
        incomplete = Set.copyOf(open);
        completion = flags;
        account = identity;
        ready = true;
    }

    boolean isReady(String identity) { return ready && account.equals(identity); }
    boolean hasIncomplete(String identity, BossDefinition boss)
    {
        return isReady(identity) && incomplete.contains(boss.hiscore.name());
    }

    boolean hasIncomplete(String identity, CaEncounter encounter, Set<CaTier> excludedTiers)
    {
        return !incompleteTasks(identity, encounter, excludedTiers).isEmpty();
    }

    List<CaEncounter.Task> incompleteTasks(String identity, CaEncounter encounter, Set<CaTier> excludedTiers)
    {
        if (!isReady(identity)) { return List.of(); }
        return encounter.tasks.stream().filter(task -> !excludedTiers.contains(task.tier)
            && task.id >= 0 && task.id / 32 < completion.length
            && !BossData.isComplete(completion[task.id / 32], task.id % 32))
            .collect(java.util.stream.Collectors.toUnmodifiableList());
    }

    static boolean isCompletionVarp(int id)
    {
        for (int varp : BossData.COMPLETION) { if (id == varp) { return true; } }
        return false;
    }
}
