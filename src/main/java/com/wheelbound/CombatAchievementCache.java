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

    void reset()
    {
        account = null;
        ready = false;
        incomplete = Set.of();
    }

    void refresh(String identity, List<BossDefinition> bosses, Map<String, List<Integer>> tasks,
        IntUnaryOperator readVarp)
    {
        reset();
        if (identity == null || tasks.isEmpty()) { return; }
        int[] flags = new int[BossData.COMPLETION.length];
        for (int i = 0; i < flags.length; i++) { flags[i] = readVarp.applyAsInt(BossData.COMPLETION[i]); }
        Set<String> open = new HashSet<>();
        for (BossDefinition boss : bosses)
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
        account = identity;
        ready = true;
    }

    boolean isReady(String identity) { return ready && account.equals(identity); }
    boolean hasIncomplete(String identity, BossDefinition boss)
    {
        return isReady(identity) && incomplete.contains(boss.hiscore.name());
    }

    static boolean isCompletionVarp(int id)
    {
        for (int varp : BossData.COMPLETION) { if (id == varp) { return true; } }
        return false;
    }
}
