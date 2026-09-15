package com.wheelbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.ScriptID;
import net.runelite.api.gameval.DBTableID;

/** Client-thread reads of the same released quest rows and checks used by the quest journal. */
final class QuestPool
{
    static final String COMPLETE = "Congrats, you have completed all the quests!";
    static final int REQUIREMENT_CHECK = 5989;
    static final int DIFFICULTY_NAMES = 2094;
    final List<WheelEntry> entries;
    final boolean allComplete;

    private QuestPool(List<WheelEntry> entries, boolean allComplete)
    { this.entries = List.copyOf(entries); this.allComplete = allComplete; }

    static QuestPool read(Client client, Set<WheelFilter> filters, WheelIconProvider icons)
    {
        List<Integer> rows = client.getDBTableRows(DBTableID.Quest.ID);
        if (rows == null || rows.isEmpty()) { throw new IllegalStateException("Quest data unavailable"); }
        EnumComposition difficulties = client.getEnum(DIFFICULTY_NAMES);
        if (difficulties == null) { throw new IllegalStateException("Quest difficulties unavailable"); }
        List<WheelEntry> entries = new ArrayList<>();
        int total = 0, unfinished = 0;
        for (int row : rows)
        {
            // Exclude unreleased rows, miniquests and subquests; RFD appears once as its parent quest.
            if (integer(client, row, DBTableID.Quest.COL_RELEASE_TYPE) != 2
                || integer(client, row, DBTableID.Quest.COL_TYPE) != 0
                || integer(client, row, DBTableID.Quest.COL_PARENT_QUEST) != -1) { continue; }
            total++;
            client.runScript(ScriptID.QUEST_STATUS_GET, row);
            int state = client.getIntStack()[0];
            if (state == 2) { continue; }
            if (state != 0 && state != 1) { throw new IllegalStateException("Unknown quest state"); }
            unfinished++;
            String difficulty = difficulties.getStringValue(integer(client, row, DBTableID.Quest.COL_DIFFICULTY));
            if (!includesDifficulty(filters, difficulty)) { continue; }
            if (filters.contains(WheelFilter.QUEST_ELIGIBLE))
            {
                client.runScript(REQUIREMENT_CHECK, row);
                if (client.getIntStack()[0] != 1) { continue; }
            }
            String name = (String)client.getDBTableField(row, DBTableID.Quest.COL_DISPLAYNAME, 0)[0];
            entries.add(icons.quest(row, name));
        }
        if (total == 0) { throw new IllegalStateException("No released quests found"); }
        return new QuestPool(entries, unfinished == 0);
    }

    static boolean includesDifficulty(Set<WheelFilter> filters, String difficulty)
    {
        for (WheelFilter filter : filters)
        {
            if (filter.wheel == WheelType.QUESTING && filter.section.equals("Quest difficulty")
                && filter.title.equalsIgnoreCase(difficulty)) { return true; }
        }
        return false;
    }

    private static int integer(Client client, int row, int column)
    { return (Integer)client.getDBTableField(row, column, 0)[0]; }
}
