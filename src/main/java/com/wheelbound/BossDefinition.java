package com.wheelbound;

import java.util.List;
import net.runelite.client.hiscore.HiscoreSkill;

/** Canonical art/name plus Wheelbound's tunable recommendations and local CA encounter aliases. */
final class BossDefinition
{
    final HiscoreSkill hiscore;
    final String name;
    final int spriteId;
    final boolean raid;
    final BossProfile profile;
    final List<String> caEncounters;

    BossDefinition(HiscoreSkill hiscore, boolean raid, BossProfile profile, List<String> caEncounters)
    {
        this.hiscore = hiscore;
        this.name = hiscore.getName();
        this.spriteId = hiscore.getSpriteId();
        this.raid = raid;
        this.profile = profile;
        this.caEncounters = List.copyOf(caEncounters);
    }
}
