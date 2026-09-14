package com.wheelbound;

import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;

final class WheelIconProvider
{
    private final SpriteManager sprites;
    private final SkillIconManager skills;

    @Inject WheelIconProvider(SpriteManager sprites, SkillIconManager skills)
    {
        this.sprites = sprites;
        this.skills = skills;
    }

    // Called on the client thread. Missing sprites are retried on the next pool update.
    WheelEntry boss(BossDefinition boss)
    {
        BufferedImage image = sprites.getSprite(boss.spriteId, 0);
        return new WheelEntry(boss.hiscore.name(), boss.name, image, 1);
    }

    WheelEntry skill(Skill skill)
    {
        return new WheelEntry(skill.name(), skill.getName(), skills.getSkillImage(skill), 1);
    }
}
