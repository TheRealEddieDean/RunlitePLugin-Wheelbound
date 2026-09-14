package com.wheelbound;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;

/** Reads are client-thread-only. These access checks supplement recommended combat stats. */
final class AccountAccess
{
    private static final Map<String, Quest> QUESTS = Map.ofEntries(
        Map.entry("Zulrah", Quest.REGICIDE),
        Map.entry("Vorkath", Quest.DRAGON_SLAYER_II),
        Map.entry("Phantom Muspah", Quest.SECRETS_OF_THE_NORTH),
        Map.entry("The Gauntlet", Quest.SONG_OF_THE_ELVES),
        Map.entry("The Corrupted Gauntlet", Quest.SONG_OF_THE_ELVES),
        Map.entry("Zalcano", Quest.SONG_OF_THE_ELVES),
        Map.entry("Duke Sucellus", Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE),
        Map.entry("Vardorvis", Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE),
        Map.entry("The Leviathan", Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE),
        Map.entry("The Whisperer", Quest.DESERT_TREASURE_II__THE_FALLEN_EMPIRE),
        Map.entry("Barrows Chests", Quest.PRIEST_IN_PERIL),
        Map.entry("Grotesque Guardians", Quest.PRIEST_IN_PERIL),
        Map.entry("Araxxor", Quest.PRIEST_IN_PERIL),
        Map.entry("Nightmare", Quest.PRIEST_IN_PERIL),
        Map.entry("Phosani's Nightmare", Quest.PRIEST_IN_PERIL),
        Map.entry("Lunar Chests", Quest.PERILOUS_MOONS),
        Map.entry("Amoxliatl", Quest.THE_HEART_OF_DARKNESS),
        Map.entry("Doom of Mokhaiotl", Quest.THE_FINAL_DAWN),
        Map.entry("Brutus", Quest.THE_RIBBITING_TALE_OF_A_LILY_PAD_LABOUR_DISPUTE),
        Map.entry("Nex", Quest.THE_FROZEN_DOOR));
    private static final Set<String> GOD_WARS = Set.of("General Graardor", "Kree'Arra", "Commander Zilyana", "K'ril Tsutsaroth", "Nex");

    static boolean questsAllow(BossDefinition boss, Predicate<Quest> complete)
    {
        Quest quest = QUESTS.get(boss.name);
        if (quest != null && !complete.test(quest)) { return false; }
        if (GOD_WARS.contains(boss.name) && !complete.test(Quest.TROLL_STRONGHOLD)) { return false; }
        return !boss.name.startsWith("Tombs of Amascut") || complete.test(Quest.BENEATH_CURSED_SANDS);
    }

    static Predicate<BossDefinition> requirements(Client client)
    {
        Map<Quest, Boolean> states = new EnumMap<>(Quest.class);
        return boss -> questsAllow(boss, quest -> states.computeIfAbsent(quest,
            q -> q.getState(client) == QuestState.FINISHED))
            && (!boss.name.equals("Grotesque Guardians") || client.getVarbitValue(VarbitID.GARGBOSS_UNLOCKED_ROOF) != 0);
    }

    static final class SlayerTask
    {
        final String name, area;
        final int remaining;
        final boolean wilderness;
        SlayerTask(String name, String area, int remaining, boolean wilderness)
        { this.name = CaEncounter.normalize(name); this.area = CaEncounter.normalize(area); this.remaining = remaining; this.wilderness = wilderness; }
        static SlayerTask unavailable() { return new SlayerTask("", "", 0, false); }
    }

    static SlayerTask slayerTask(Client client)
    {
        int count = client.getVarpValue(VarPlayerID.SLAYER_COUNT);
        if (count <= 0) { return SlayerTask.unavailable(); }
        int target = client.getVarpValue(VarPlayerID.SLAYER_TARGET);
        List<Integer> rows = target == 98
            ? client.getDBRowsByValue(DBTableID.SlayerTaskSublist.ID, DBTableID.SlayerTaskSublist.COL_TASK_SUBTABLE_ID,
                0, client.getVarbitValue(VarbitID.SLAYER_TARGET_BOSSID))
            : client.getDBRowsByValue(DBTableID.SlayerTask.ID, DBTableID.SlayerTask.COL_ID, 0, target);
        if (rows.isEmpty()) { return SlayerTask.unavailable(); }
        int row = target == 98 ? (Integer)client.getDBTableField(rows.get(0), DBTableID.SlayerTaskSublist.COL_TASK, 0)[0] : rows.get(0);
        String name = (String)client.getDBTableField(row, DBTableID.SlayerTask.COL_NAME_UPPERCASE, 0)[0];
        int areaId = client.getVarpValue(VarPlayerID.SLAYER_AREA);
        String area = "";
        if (areaId > 0)
        {
            List<Integer> areas = client.getDBRowsByValue(DBTableID.SlayerArea.ID, DBTableID.SlayerArea.COL_AREA_ID, 0, areaId);
            if (areas.isEmpty()) { return SlayerTask.unavailable(); }
            area = (String)client.getDBTableField(areas.get(0), DBTableID.SlayerArea.COL_AREA_NAME_IN_HELPER, 0)[0];
        }
        return new SlayerTask(name, area, count, client.getVarbitValue(VarbitID.SLAYER_MASTER) == 7);
    }

    static boolean taskAllows(String encounter, SlayerTask task)
    {
        String name = CaEncounter.normalize(encounter);
        String assignment, location;
        switch (name)
        {
            case "abyssalsire": assignment = "abyssaldemons"; location = "abyss"; break;
            case "kraken": case "cavekraken": assignment = "cavekraken"; location = "krakencove"; break;
            case "cerberus": assignment = "hellhounds"; location = "taverleydungeon"; break;
            case "grotesqueguardians": assignment = "gargoyles"; location = "slayertower"; break;
            case "alchemicalhydra": assignment = "hydras"; location = "karuulmslayerdungeon"; break;
            case "araxxor": assignment = "araxytes"; location = "araxytecave"; break;
            case "thermonuclearsmokedevil": assignment = "smokedevils"; location = "smokedevildungeon"; break;
            default: return true;
        }
        String assigned = task.name;
        boolean matches = assigned.equals(assignment) || assigned.equals(name)
            || name.equals("kraken") && assigned.equals("cavekrakenboss");
        return task.remaining > 0 && !task.wilderness && matches
            && (task.area.isEmpty() || task.area.contains(location));
    }

    static boolean slayerChanged(VarbitChanged event)
    {
        int varp = event.getVarpId(), varbit = event.getVarbitId();
        return varp == VarPlayerID.SLAYER_COUNT || varp == VarPlayerID.SLAYER_TARGET || varp == VarPlayerID.SLAYER_AREA
            || varbit == VarbitID.SLAYER_TARGET_BOSSID || varbit == VarbitID.SLAYER_MASTER;
    }
}
