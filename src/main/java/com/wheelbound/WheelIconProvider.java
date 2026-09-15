package com.wheelbound;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.StructComposition;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final class WheelIconProvider
{
    private final SpriteManager sprites;
    private final ItemManager items;
    private final SkillIconManager skills;
    private final Client client;
    private final Map<Integer, BufferedImage> encounterIcons = new HashMap<>();

    @Inject WheelIconProvider(Client client, SpriteManager sprites, SkillIconManager skills, ItemManager items)
    {
        this.client = client;
        this.items = items;
        this.sprites = sprites;
        this.skills = skills;
    }

    // Called on the client thread. Missing sprites are retried on the next pool update.
    WheelEntry boss(BossDefinition boss)
    {
        BufferedImage image = sprites.getSprite(boss.spriteId, 0);
        return new WheelEntry(boss.hiscore.name(), boss.name, image, 1);
    }

    WheelEntry quest(int row, String name)
    {
        BufferedImage image = null;
        try { image = sprites.getSprite(net.runelite.api.gameval.SpriteID.AchievementDiaryIcons.BLUE_QUESTS, 0); }
        catch (RuntimeException ex) { log.debug("Quest artwork is unavailable", ex); }
        return new WheelEntry("QUEST_" + row, name, image, 1);
    }

    WheelEntry skill(Skill skill)
    {
        return new WheelEntry(skill.name(), skill.getName(), skills.getSkillImage(skill), 1);
    }

    WheelEntry encounter(CaEncounter encounter)
    {
        BufferedImage image = null;
        try
        {
            image = encounter.boss != null ? sprites.getSprite(encounter.boss.spriteId, 0)
                : encounterIcons.computeIfAbsent(encounter.id, this::monster);
        }
        catch (RuntimeException ex) { log.debug("CA artwork is unavailable for {}", encounter.name, ex); }
        return new WheelEntry("CA_" + encounter.id, encounter.name, image, 1);
    }

    WheelEntry pet(PetDefinition pet)
    {
        BufferedImage image = itemIcon(pet.itemId);
        BufferedImage source = null;
        try
        {
            source = pet.boss != null ? sprites.getSprite(pet.boss.getSpriteId(), 0)
                : pet.skill != null ? skills.getSkillImage(pet.skill) : itemIcon(pet.sourceItemId);
        }
        catch (RuntimeException ex) { log.debug("Pet source artwork is unavailable for {}", pet.name, ex); }
        return new WheelEntry("PET_" + pet.itemId, pet.name, image, 1, pet.source, source);
    }

    private BufferedImage itemIcon(int itemId)
    {
        try { return items.getImage(itemId); }
        catch (RuntimeException ex) { log.debug("Item artwork is unavailable for {}", itemId, ex); return null; }
    }

    private BufferedImage monster(int id)
    {
        // The CA interface uses enum 3987 and model/view parameters 1315, 1322, 1326, 1327.
        // Missing assets retry on a later pool update, as with boss sprites.
        EnumComposition definitions = client.getEnum(3987);
        if (definitions == null || definitions.getIntVals() == null) { return null; }
        for (int structId : definitions.getIntVals())
        {
            StructComposition definition = client.getStructComposition(structId);
            if (definition == null || definition.getIntValue(1315) != id) { continue; }
            int modelId = definition.getIntValue(1322);
            return modelId < 0 ? null : MonsterThumbnail.render(client.loadModel(modelId),
                definition.getIntValue(1327), definition.getIntValue(1326));
        }
        return null;
    }
}
