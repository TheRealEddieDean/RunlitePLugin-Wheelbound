package com.wheelbound;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.Client;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WheelFiltersTest
{
    @Test public void onlyUnfinishedTasksInIncludedTiersKeepAnEncounterEligible()
    {
        CaEncounter obor = encounter(1, "Obor", new CaEncounter.Task(0, CaTier.EASY), new CaEncounter.Task(31, CaTier.MASTER));
        CombatAchievementCache cache = new CombatAchievementCache();
        cache.refresh("A", BossCatalog.ALL, Map.of("Obor", List.of(0, 31)), id -> id == BossData.COMPLETION[0] ? 1 : 0);
        assertTrue(cache.hasIncomplete("A", obor, Set.of()));
        assertFalse("The only unfinished task is Master", cache.hasIncomplete("A", obor, Set.of(CaTier.MASTER)));
        assertFalse(cache.hasIncomplete("B", obor, Set.of()));
        cache.refresh("A", BossCatalog.ALL, Map.of("Obor", List.of(0, 31)), id -> -1);
        assertFalse(cache.hasIncomplete("A", obor, Set.of()));
        cache.reset();
        assertFalse(cache.hasIncomplete("A", obor, Set.of()));
    }

    @Test public void everyTierCanBeExcludedAndUnknownTaskIdsAreNotUnfinished()
    {
        CombatAchievementCache cache = new CombatAchievementCache();
        cache.refresh("A", BossCatalog.ALL, Map.of("Bloodveld", List.of(1)), id -> 0);
        for (CaTier tier : CaTier.values())
        {
            CaEncounter monster = encounter(2, "Bloodveld", new CaEncounter.Task(1, tier));
            assertTrue(cache.hasIncomplete("A", monster, Set.of()));
            assertFalse(cache.hasIncomplete("A", monster, Set.of(tier)));
        }
        CaEncounter unsupported = encounter(3, "Future monster", new CaEncounter.Task(-1, CaTier.EASY),
            new CaEncounter.Task(BossData.COMPLETION.length * 32, CaTier.EASY));
        assertFalse(cache.hasIncomplete("A", unsupported, Set.of()));
    }

    @Test public void bossesRaidsAndMonstersAreIndependentCategories()
    {
        CaEncounter boss = encounter(1, "Obor"), monster = encounter(2, "Bloodveld");
        CaEncounter raid = encounter(3, "Theatre of Blood: Entry Mode");
        List<CaEncounter> pool = List.of(boss, monster, raid);
        assertEquals(List.of(monster, raid), WheelEligibility.achievements(pool, true, false, e -> true, e -> true));
        assertEquals(List.of(boss, monster), WheelEligibility.achievements(pool, false, true, e -> true, e -> true));
        assertEquals(List.of(monster), WheelEligibility.achievements(pool, true, true, e -> true, e -> true));
        assertEquals(CaEncounter.Kind.BOSS, encounter(4, "Corrupted Gauntlet").kind);
        assertEquals(CaEncounter.Kind.BOSS, encounter(5, "Perilous Moons").kind);
        assertEquals(CaEncounter.Kind.BOSS, encounter(6, "TzHaar-Ket-Rak's Challenges").kind);
        assertTrue(WheelEligibility.achievements(pool, false, false, e -> false, e -> true).isEmpty());
    }

    @Test public void combatAndMaxedSkillFiltersIntersectWithoutRemovingSlayerOrSailing()
    {
        List<Skill> skills = WheelEligibility.skills(true, true, true, s -> s == Skill.MINING ? 99 : 70);
        assertEquals(Skill.values().length - 8, skills.size());
        assertFalse(skills.contains(Skill.MINING)); assertFalse(skills.contains(Skill.PRAYER));
        assertFalse(skills.contains(Skill.HITPOINTS)); assertTrue(skills.contains(Skill.SLAYER));
        assertTrue(skills.contains(Skill.SAILING));
        assertTrue(WheelEligibility.skills(true, true, false, s -> 1).isEmpty());
        assertEquals(Skill.values().length - 7, WheelEligibility.skills(true, false, false, s -> 99).size());
    }

    @Test public void taskOnlyEncountersNeedAnActiveMatchingAssignmentAndLocation()
    {
        assertTrue(AccountAccess.taskAllows("Obor", AccountAccess.SlayerTask.unavailable()));
        assertFalse(AccountAccess.taskAllows("Cerberus", AccountAccess.SlayerTask.unavailable()));
        assertTrue(AccountAccess.taskAllows("Cerberus", task("Hellhounds", "", 20, false)));
        assertTrue(AccountAccess.taskAllows("Cerberus", task("Cerberus", "", 3, false)));
        assertTrue(AccountAccess.taskAllows("Cerberus", task("Hellhounds", "Taverley Dungeon", 20, false)));
        assertFalse(AccountAccess.taskAllows("Cerberus", task("Hellhounds", "Catacombs of Kourend", 20, false)));
        assertFalse(AccountAccess.taskAllows("Cerberus", task("Hellhounds", "", 20, true)));
        assertFalse(AccountAccess.taskAllows("Cerberus", task("Hellhounds", "", 0, false)));
        assertFalse(AccountAccess.taskAllows("Cerberus", task("Gargoyles", "", 20, false)));
        assertTrue(AccountAccess.taskAllows("Grotesque Guardians", task("The Grotesque Guardians", "", 2, false)));
        assertTrue(AccountAccess.taskAllows("Alchemical Hydra", task("Hydras", "Karuulm Slayer Dungeon", 20, false)));
        assertTrue(AccountAccess.taskAllows("Araxxor", task("Araxytes", "", 20, false)));
        assertTrue(AccountAccess.taskAllows("Abyssal Sire", task("Abyssal demons", "Abyssal Area", 20, false)));
        assertTrue(AccountAccess.taskAllows("Kraken", task("The Cave Kraken Boss", "", 20, false)));
        assertTrue(AccountAccess.taskAllows("Thermonuclear Smoke Devil", task("Thermonuclear Smoke Devil", "", 20, false)));
    }

    @Test public void knownQuestLocksAreIndependentOfRecommendedLevels()
    {
        BossDefinition vorkath = BossCatalog.ALL.stream().filter(b -> b.name.equals("Vorkath")).findFirst().orElseThrow();
        assertFalse(AccountAccess.questsAllow(vorkath, q -> false));
        assertTrue(AccountAccess.questsAllow(vorkath, q -> q == Quest.DRAGON_SLAYER_II));
        BossDefinition raid = BossCatalog.ALL.stream().filter(b -> b.name.equals("Tombs of Amascut: Expert Mode")).findFirst().orElseThrow();
        assertFalse(AccountAccess.questsAllow(raid, q -> q == Quest.DRAGON_SLAYER_II));
        assertTrue(AccountAccess.questsAllow(raid, q -> q == Quest.BENEATH_CURSED_SANDS));
    }

    @Test public void readsLiveSlayerDatabaseForRegularAndDirectBossTasks()
    {
        Client client = mock(Client.class);
        when(client.getVarpValue(VarPlayerID.SLAYER_COUNT)).thenReturn(20);
        when(client.getVarpValue(VarPlayerID.SLAYER_TARGET)).thenReturn(42);
        when(client.getDBRowsByValue(DBTableID.SlayerTask.ID, DBTableID.SlayerTask.COL_ID, 0, 42)).thenReturn(List.of(900));
        when(client.getDBTableField(900, DBTableID.SlayerTask.COL_NAME_UPPERCASE, 0)).thenReturn(new Object[]{"Hellhounds"});
        when(client.getVarpValue(VarPlayerID.SLAYER_AREA)).thenReturn(5);
        when(client.getDBRowsByValue(DBTableID.SlayerArea.ID, DBTableID.SlayerArea.COL_AREA_ID, 0, 5)).thenReturn(List.of(901));
        when(client.getDBTableField(901, DBTableID.SlayerArea.COL_AREA_NAME_IN_HELPER, 0)).thenReturn(new Object[]{"Taverley Dungeon"});
        assertTrue(AccountAccess.taskAllows("Cerberus", AccountAccess.slayerTask(client)));
        when(client.getVarpValue(VarPlayerID.SLAYER_TARGET)).thenReturn(98);
        when(client.getVarbitValue(VarbitID.SLAYER_TARGET_BOSSID)).thenReturn(7);
        when(client.getDBRowsByValue(DBTableID.SlayerTaskSublist.ID, DBTableID.SlayerTaskSublist.COL_TASK_SUBTABLE_ID, 0, 7)).thenReturn(List.of(902));
        when(client.getDBTableField(902, DBTableID.SlayerTaskSublist.COL_TASK, 0)).thenReturn(new Object[]{903});
        when(client.getDBTableField(903, DBTableID.SlayerTask.COL_NAME_UPPERCASE, 0)).thenReturn(new Object[]{"Cerberus"});
        assertTrue(AccountAccess.taskAllows("Cerberus", AccountAccess.slayerTask(client)));
        when(client.getVarpValue(VarPlayerID.SLAYER_COUNT)).thenReturn(0);
        assertFalse(AccountAccess.taskAllows("Cerberus", AccountAccess.slayerTask(client)));
    }

    @Test public void checklistsAreSeparateAndDifficultyChangesPreserveManualExclusions() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> saved = new HashMap<>();
            WheelboundPanel panel = new WheelboundPanel(saved::get, (k, v) -> saved.put(k, v.toString()));
            List<WheelEntry> entries = List.of(new WheelEntry("shared", "Example", null, 1));
            for (WheelType type : WheelType.values())
            {
                if (type == WheelType.CUSTOM) { continue; }
                panel.selectWheel(type); panel.updatePool(entries, "test", panel.generation());
                assertEquals(1, panel.primaryWheel().entries().size());
                button(panel, "Example").doClick();
                assertTrue(panel.primaryWheel().entries().isEmpty());
                assertEquals("shared", saved.get(type.exclusionKey));
            }
            panel.selectWheel(WheelType.BOSSING);
            button(panel, "Include raids").doClick(); button(panel, "Include Mimic").doClick();
            button(panel, "Easy").doClick();
            assertFalse(panel.selected(WheelFilter.BOSS_RAIDS)); assertFalse(panel.selected(WheelFilter.MIMIC));
            assertFalse(panel.selected(WheelFilter.BOSS_EASY));
            assertTrue(panel.selected(WheelFilter.ACCOUNT)); assertTrue(panel.selected(WheelFilter.BOSS_TASK));
            panel.updatePool(entries, "test", panel.generation());
            assertTrue(panel.primaryWheel().entries().isEmpty());
            WheelboundPanel restored = new WheelboundPanel(saved::get, (k, v) -> {});
            for (WheelType type : List.of(WheelType.SKILLING, WheelType.COMBAT_ACHIEVEMENTS))
            {
                restored.selectWheel(type); restored.updatePool(entries, "test", restored.generation());
                assertTrue(restored.primaryWheel().entries().isEmpty());
            }
            restored.reloadPreferences(k -> null);
            assertEquals(WheelType.BOSSING, restored.wheelType());
            restored.updatePool(entries, "test", restored.generation());
            assertEquals(1, restored.primaryWheel().entries().size());
        });
    }

    private static CaEncounter encounter(int id, String name, CaEncounter.Task... tasks)
    { return new CaEncounter(id, name, List.of(tasks)); }
    private static AccountAccess.SlayerTask task(String name, String area, int count, boolean wilderness)
    { return new AccountAccess.SlayerTask(name, area, count, wilderness); }
    private static AbstractButton button(Container parent, String name)
    {
        AbstractButton result = find(parent, name);
        if (result == null) { throw new AssertionError("Missing visible button: " + name); }
        return result;
    }
    private static AbstractButton find(Container parent, String name)
    {
        for (Component component : parent.getComponents())
        {
            if (!component.isVisible()) { continue; }
            if (component instanceof AbstractButton && ((AbstractButton)component).getText().contains(name)) { return (AbstractButton)component; }
            if (component instanceof Container)
            { AbstractButton found = find((Container)component, name); if (found != null) { return found; } }
        }
        return null;
    }
}
