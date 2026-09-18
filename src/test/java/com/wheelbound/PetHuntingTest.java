package com.wheelbound;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.AsyncBufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetHuntingTest
{
    @Test public void catalogHasUniqueBasePetsAndEachCategoryFiltersIndependently()
    {
        assertEquals(71, PetDefinition.ALL.size());
        assertEquals(PetDefinition.ALL.size(), PetDefinition.ALL.stream().map(p -> p.itemId).distinct().count());
        assertEquals(PetDefinition.ALL.size(), PetDefinition.ALL.stream().map(p -> p.name).distinct().count());
        assertTrue(WheelEligibility.pets(Set.of()).isEmpty());
        assertEquals(PetDefinition.ALL, WheelEligibility.pets(EnumSet.allOf(WheelFilter.class)));
        Map<WheelFilter, PetDefinition.Kind> categories = Map.of(
            WheelFilter.PET_BOSSES, PetDefinition.Kind.BOSS, WheelFilter.PET_RAIDS, PetDefinition.Kind.RAID,
            WheelFilter.PET_SKILLS, PetDefinition.Kind.SKILL, WheelFilter.PET_ACTIVITIES, PetDefinition.Kind.ACTIVITY);
        categories.forEach((filter, kind) -> {
            List<PetDefinition> entries = WheelEligibility.pets(Set.of(filter));
            assertFalse(entries.isEmpty());
            assertTrue(entries.stream().allMatch(p -> p.kind == kind));
        });
        assertEquals(3, WheelEligibility.pets(Set.of(WheelFilter.PET_RAIDS)).size());
        for (PetDefinition pet : PetDefinition.ALL)
        {
            assertFalse(pet.name.isBlank()); assertFalse(pet.source.isBlank()); assertTrue(pet.itemId > 0);
            if (pet.boss != null)
            { assertTrue(BossCatalog.ALL.stream().anyMatch(b -> b.hiscore == pet.boss)); }
        }
    }

    @Test public void localPetAndSourceIconsSurviveGroupingAndMissingAssetsRetry()
    {
        ItemManager items = mock(ItemManager.class);
        SpriteManager sprites = mock(SpriteManager.class);
        SkillIconManager skills = new SkillIconManager();
        WheelIconProvider icons = new WheelIconProvider(mock(Client.class), sprites, skills, items);
        PetDefinition vorki = pet(ItemID.VORKATHPET);
        AsyncBufferedImage petImage = mock(AsyncBufferedImage.class);
        BufferedImage sourceImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        when(items.getImage(vorki.itemId)).thenReturn(null, petImage);
        when(sprites.getSprite(vorki.boss.getSpriteId(), 0)).thenReturn(sourceImage);
        WheelEntry first = icons.pet(vorki);
        assertNull(first.icon); assertSame(sourceImage, first.sourceIcon);
        WheelEntry entry = icons.pet(vorki);
        assertSame(petImage, entry.icon); assertEquals("Vorkath", entry.source);
        assertSame(entry, WheelEntry.groupRaids(List.of(entry)).get(0));
        assertSame(skills.getSkillImage(Skill.FISHING), icons.pet(pet(ItemID.SKILLPETFISH)).sourceIcon);
        icons.pet(pet(ItemID.BLOODHOUND_PET));
        verify(items).getImage(ItemID.TRAIL_REWARD_CASKET_MASTER);
        when(items.getImage(vorki.itemId)).thenThrow(new IllegalStateException("Cache unavailable"));
        assertNull(icons.pet(vorki).icon);
    }

    @Test public void petChecklistPersistsAndCategoryChangesUpdateOpenPopup() throws Exception
    {
        SwingUtilities.invokeAndWait(() -> {
            Map<String, String> preferences = new HashMap<>();
            WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, preferences::get, (k, v) -> preferences.put(k, v.toString()));
            WheelPopup popup = new WheelPopup(null); panel.setPopup(popup);
            panel.setAction(spin -> {
                if (!spin) { panel.updatePool(entries(panel), "test", panel.generation()); }
            });
            panel.selectWheel(WheelType.PET_HUNTING); panel.onActivate();
            assertEquals(71, popup.entryCount());
            button(panel, "Include raids").doClick(); assertEquals(68, popup.entryCount());
            button(panel, "Vorki").doClick(); assertEquals(67, popup.entryCount());
            assertEquals("PET_" + ItemID.VORKATHPET, preferences.get("excludedPets"));
            assertNull(preferences.get("excludedBosses"));
            panel.selectWheel(WheelType.BOSSING); panel.selectWheel(WheelType.PET_HUNTING);
            assertEquals(67, popup.entryCount());
            for (String category : List.of("Include bosses", "Include skilling", "Include other activities"))
            { button(panel, category).doClick(); }
            assertEquals(0, popup.entryCount()); assertFalse(panel.primaryWheel().canSpin());
            button(panel, "Include raids").doClick(); assertEquals(3, popup.entryCount());
            panel.reset(); assertFalse(popup.isOpen());
            WheelboundPanel restored = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, preferences::get, (k, v) -> {});
            assertEquals(WheelType.PET_HUNTING, restored.wheelType());
            assertTrue(restored.selected(WheelFilter.PET_RAIDS));
            assertFalse(restored.selected(WheelFilter.PET_BOSSES));
        });
    }

    @Test public void petResultsFitAndRenderBothNamesAndIcons() throws Exception
    {
        SkillIconManager icons = new SkillIconManager();
        // Stand-in local icons exercise geometry; live item sprites require the game cache.
        for (int[] size : new int[][]{{765, 503}, {1200, 900}})
        {
            BufferedImage image = new BufferedImage(size[0], size[1], BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            WheelEntry pet = new WheelEntry("PET", "Lil'viathan", icons.getSkillImage(Skill.FISHING), 1,
                "The Leviathan", icons.getSkillImage(Skill.SLAYER));
            WheelPopup.Layout layout = WheelPopup.paint(g, size[0], size[1],
                new WheelPopup.View("Pet Hunting", List.of(pet), 0, "", true, false), false, false);
            Rectangle result = WheelPopup.paintResultRow(g, layout, pet, null);
            assertTrue(layout.results.contains(result));
            assertEquals(layout.card.getCenterX(), result.getCenterX(), 1);
            assertTrue(result.width < layout.results.width);
            for (PetDefinition definition : PetDefinition.ALL)
            {
                Rectangle bounds = WheelPopup.paintResultRow(g, layout,
                    new WheelEntry("PET_" + definition.itemId, definition.name, null, 1, definition.source, null), null);
                assertTrue(layout.results.contains(bounds));
            }
            WheelPopup.paint(g, size[0], size[1],
                new WheelPopup.View("Pet Hunting", List.of(pet), 0, "", true, false), false, false);
            WheelPopup.paintResultRow(g, layout, pet, null); g.dispose();
            java.nio.file.Files.createDirectories(java.nio.file.Path.of("build/reports"));
            ImageIO.write(image, "png", new File("build/reports/wheelbound-pet-result-" + size[0] + ".png"));
        }
        SwingUtilities.invokeAndWait(() -> {
            try
            {
                net.runelite.client.ui.laf.RuneLiteLAF.setup();
                WheelboundPanel panel = new WheelboundPanel(net.runelite.http.api.RuneLiteAPI.GSON, k -> "selectedWheel".equals(k) ? "Pet Hunting" : null, (k, v) -> {});
                panel.updatePool(entries(panel), "test", panel.generation());
                JPanel wrapped = panel.getWrappedPanel(); wrapped.setSize(242, 900); layout(wrapped);
                BufferedImage image = new BufferedImage(242, 900, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics(); wrapped.paint(g); g.dispose();
                ImageIO.write(image, "png", new File("build/reports/wheelbound-pet-sidebar.png"));
            }
            catch (Exception ex) { throw new AssertionError(ex); }
        });
    }

    private static PetDefinition pet(int id)
    { return PetDefinition.ALL.stream().filter(p -> p.itemId == id).findFirst().orElseThrow(); }
    private static List<WheelEntry> entries(WheelboundPanel panel)
    {
        return WheelEligibility.pets(panel.selectedFilters()).stream()
            .map(p -> new WheelEntry("PET_" + p.itemId, p.name, null, 1, p.source, null))
            .collect(java.util.stream.Collectors.toList());
    }
    private static AbstractButton button(Container parent, String text)
    {
        AbstractButton found = find(parent, text);
        assertNotNull("Missing button: " + text, found);
        return found;
    }

    private static AbstractButton find(Container parent, String text)
    {
        for (Component c : parent.getComponents())
        {
            if (!c.isVisible()) { continue; }
            if (c instanceof AbstractButton && ((AbstractButton)c).getText().contains(text)) { return (AbstractButton)c; }
            if (c instanceof Container) { AbstractButton found = find((Container)c, text); if (found != null) { return found; } }
        }
        return null;
    }
    private static void layout(Container parent)
    {
        parent.doLayout();
        for (Component child : parent.getComponents()) { if (child instanceof Container) { layout((Container)child); } }
    }
}
