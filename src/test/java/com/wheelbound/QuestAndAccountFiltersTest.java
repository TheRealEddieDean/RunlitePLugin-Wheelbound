package com.wheelbound;

import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.ScriptID;
import net.runelite.api.gameval.DBTableID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuestAndAccountFiltersTest
{
    private final Client client = mock(Client.class);
    private final SpriteManager sprites = mock(SpriteManager.class);
    private final WheelIconProvider icons = new WheelIconProvider(client, sprites,
        new SkillIconManager(), mock(ItemManager.class));

    @Test public void questsExcludeCompletedAndRespectDifficultyAndRequirements()
    {
        setupQuests();
        Set<WheelFilter> filters = EnumSet.allOf(WheelFilter.class);
        QuestPool pool = QuestPool.read(client, filters, icons);
        assertFalse(pool.allComplete);
        assertEquals(List.of("QUEST_10"), pool.entries.stream().map(e -> e.id).collect(java.util.stream.Collectors.toList()));
        filters.remove(WheelFilter.QUEST_ELIGIBLE);
        assertEquals(2, QuestPool.read(client, filters, icons).entries.size());
        filters.remove(WheelFilter.QUEST_MASTER);
        assertEquals(1, QuestPool.read(client, filters, icons).entries.size());
        filters.remove(WheelFilter.QUEST_NOVICE);
        pool = QuestPool.read(client, filters, icons);
        assertTrue(pool.entries.isEmpty());
        assertFalse("Filtering everything out is not quest completion", pool.allComplete);
    }

    @Test public void congratulationsRequiresEveryReleasedMainQuestComplete()
    {
        setupQuests();
        doAnswer(invocation -> { when(client.getIntStack()).thenReturn(new int[]{2}); return null; })
            .when(client).runScript(eq(ScriptID.QUEST_STATUS_GET), anyInt());
        QuestPool pool = QuestPool.read(client, Set.of(), icons);
        assertTrue(pool.allComplete); assertTrue(pool.entries.isEmpty());
        verify(client, never()).runScript(QuestPool.REQUIREMENT_CHECK, 10);
        verify(client, never()).runScript(ScriptID.QUEST_STATUS_GET, 13);
        verify(client, never()).runScript(ScriptID.QUEST_STATUS_GET, 14);
        verify(client, never()).runScript(ScriptID.QUEST_STATUS_GET, 15);
    }

    @Test(expected = IllegalStateException.class) public void unavailableQuestDataCannotClaimCompletion()
    { QuestPool.read(client, Set.of(), icons); }

    @Test public void questCompletionShowsIconResultAndClearsOnAccountReset() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> null, (k, v) -> {});
            WheelPopup popup = new WheelPopup(null); panel.setPopup(popup);
            panel.selectWheel(WheelType.QUESTING);
            panel.updatePool(List.of(), QuestPool.COMPLETE, panel.generation());
            WheelEntry complete = new WheelEntry("QUEST_-1", "All quests completed!",
                new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB), 1);
            panel.setQuestCompletion(complete);
            panel.onActivate();
            assertTrue(popup.hasResult()); assertFalse(panel.primaryWheel().canSpin());
            panel.setQuestCompletion(null); assertFalse(popup.hasResult());
            panel.setQuestCompletion(complete); assertTrue(popup.hasResult());
            panel.reset(); assertFalse(popup.hasResult()); assertFalse(popup.isOpen());
            panel.onActivate(); assertFalse(popup.hasResult());
            panel.onDeactivate();
        });
    }

    @Test public void petBitsUse31BitGroupsAndIncludeReclaimablePets()
    {
        EnumComposition pets = mock(EnumComposition.class);
        when(client.getEnum(OwnedPets.PET_ITEMS)).thenReturn(pets);
        when(pets.getKeys()).thenReturn(new int[]{0, 30, 31, 61, 62, 70});
        int[] ids = {ItemID.VORKATHPET, ItemID.MOLEPET, ItemID.KBDPET,
            ItemID.NEXPET, ItemID.YAMAPET, ItemID.MADANGELPET};
        int[] keys = {0, 30, 31, 61, 62, 70};
        for (int i = 0; i < keys.length; i++) { when(pets.getIntValue(keys[i])).thenReturn(ids[i]); }
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK1)).thenReturn(1 << 30);
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK2)).thenReturn(1 | (1 << 30));
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK3)).thenReturn(1 | (1 << 8));
        Set<Integer> owned = OwnedPets.read(client);
        for (int id : ids)
        {
            PetDefinition pet = PetDefinition.ALL.stream().filter(p -> p.itemId == id).findFirst().orElseThrow();
            assertEquals(id != ItemID.VORKATHPET, OwnedPets.contains(owned, pet));
        }
        verify(client, never()).getVarpValue(VarPlayerID.PET_INSURANCE_OWNED_BITMASK1);
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK1)).thenReturn(0);
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK2)).thenReturn(0);
        when(client.getVarpValue(VarPlayerID.PET_INSURANCE_BITMASK3)).thenReturn(0);
        assertTrue("A new account must not retain prior ownership", OwnedPets.read(client).isEmpty());
    }

    @Test public void difficultyFiltersAreIndependentAndTaskOnlyBossesNeedAnAssignment()
    {
        for (BossDefinition boss : BossCatalog.ALL)
        {
            assertTrue(BossDifficulty.included(boss, EnumSet.allOf(WheelFilter.class)));
            assertFalse(BossDifficulty.included(boss, Set.of(WheelFilter.ACCOUNT)));
        }
        BossDefinition obor = BossCatalog.ALL.stream().filter(b -> b.name.equals("Obor")).findFirst().orElseThrow();
        assertTrue(BossDifficulty.included(obor, Set.of(WheelFilter.BOSS_EASY)));
        assertFalse(BossDifficulty.included(obor, Set.of(WheelFilter.BOSS_HARD)));
        AccountAccess.SlayerTask none = AccountAccess.SlayerTask.unavailable();
        assertTrue(AccountAccess.taskAllows("Obor", none));
        assertFalse(AccountAccess.taskAllows("Cerberus", none));
        assertFalse(AccountAccess.taskAllows("Shellbane Gryphon", none));
        assertTrue(AccountAccess.taskAllows("Shellbane Gryphon", new AccountAccess.SlayerTask("Gryphons", "", 10, false)));
        assertTrue(AccountAccess.taskAllows("Shellbane Gryphon", new AccountAccess.SlayerTask("The Shellbane Gryphon", "", 10, false)));
        assertFalse(AccountAccess.taskAllows("Shellbane Gryphon", new AccountAccess.SlayerTask("Gryphons", "", 0, false)));
    }

    @Test public void mimicMigrationAndNewDefaultsPersistAcrossProfiles() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> k.equals("bossExcludeMimic") ? "true" : null, (k, v) -> {});
            assertFalse(panel.selected(WheelFilter.MIMIC));
            for (WheelFilter filter : WheelFilter.values())
            { if (filter.section.equals("Boss difficulty")) { assertTrue(panel.selected(filter)); } }
            panel.reloadPreferences(k -> k.equals("bossIncludeMimic") ? "true" : k.equals("bossExcludeMimic") ? "true" : null);
            assertTrue(panel.selected(WheelFilter.MIMIC));
            assertTrue(panel.selected(WheelFilter.PET_OWNED)); assertTrue(panel.selected(WheelFilter.QUEST_ELIGIBLE));
        });
    }

    private void setupQuests()
    {
        when(client.getDBTableRows(DBTableID.Quest.ID)).thenReturn(List.of(10, 11, 12, 13, 14, 15));
        EnumComposition difficulties = mock(EnumComposition.class);
        when(client.getEnum(QuestPool.DIFFICULTY_NAMES)).thenReturn(difficulties);
        when(difficulties.getStringValue(0)).thenReturn("Novice");
        when(difficulties.getStringValue(3)).thenReturn("Master");
        for (int row = 10; row <= 15; row++)
        {
            field(row, DBTableID.Quest.COL_RELEASE_TYPE, row == 13 ? 1 : 2);
            field(row, DBTableID.Quest.COL_TYPE, row == 14 ? 1 : 0);
            field(row, DBTableID.Quest.COL_PARENT_QUEST, row == 15 ? 10 : -1);
            field(row, DBTableID.Quest.COL_DIFFICULTY, row == 11 ? 3 : 0);
            field(row, DBTableID.Quest.COL_DISPLAYNAME, "Quest " + row);
        }
        doAnswer(invocation -> {
            int script = invocation.getArgument(0), row = invocation.getArgument(1);
            int state = script == ScriptID.QUEST_STATUS_GET ? (row == 12 ? 2 : row == 10 ? 0 : 1) : row == 11 ? 0 : 1;
            when(client.getIntStack()).thenReturn(new int[]{state}); return null;
        }).when(client).runScript(anyInt(), anyInt());
    }

    private void field(int row, int column, Object value)
    { when(client.getDBTableField(row, column, 0)).thenReturn(new Object[]{value}); }
}
