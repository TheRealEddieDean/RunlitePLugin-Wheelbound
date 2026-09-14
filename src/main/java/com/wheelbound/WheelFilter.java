package com.wheelbound;

/** Declarative sidebar sections; new wheels can register their own filter controls here. */
enum WheelFilter
{
    ACCOUNT(WheelType.BOSSING, "Account", "bossAccount", "Account for skill level", true,
        "Check recommended real levels and known quest/access requirements."),
    BOSS_TASK(WheelType.BOSSING, "Account", "bossSlayerTask", "Match my Slayer task", true,
        "Task-only bosses need a matching active task and location. Other bosses are unaffected."),
    BOSS_RAIDS(WheelType.BOSSING, "Boss pool", "bossExcludeRaids", "Exclude raids", false, "Remove every raid mode."),
    MIMIC(WheelType.BOSSING, "Boss pool", "bossExcludeMimic", "Exclude Mimic", false, "Remove the Mimic encounter."),
    COMBAT_SKILLS(WheelType.SKILLING, "Skills", "skillExcludeCombat", "Exclude combat skills", false,
        "Remove Attack, Strength, Defence, Hitpoints, Ranged, Magic and Prayer. Slayer remains a training skill."),
    MAXED_SKILLS(WheelType.SKILLING, "Skills", "skillExclude99", "Exclude skills with 99", true, "Use real levels, not boosted or virtual levels."),
    XP(WheelType.SKILLING, "XP target", "includeXpGoal", "Include XP goal", false, "Spin the existing second XP wheel after choosing a skill."),
    CA_BOSSES(WheelType.COMBAT_ACHIEVEMENTS, "Encounters", "caExcludeBosses", "Exclude bosses", false,
        "Remove boss encounters. Raids have their own separate filter."),
    CA_RAIDS(WheelType.COMBAT_ACHIEVEMENTS, "Encounters", "caExcludeRaids", "Exclude raids", false, "Remove all raid encounters and modes."),
    CA_TASK(WheelType.COMBAT_ACHIEVEMENTS, "Encounters", "caSlayerTask", "Match my Slayer task", true,
        "Task-only encounters need a matching active task and location."),
    EASY(CaTier.EASY), MEDIUM(CaTier.MEDIUM), HARD(CaTier.HARD),
    ELITE(CaTier.ELITE), MASTER(CaTier.MASTER), GRANDMASTER(CaTier.GRANDMASTER);

    final WheelType wheel;
    final String section, key, title, tip;
    final boolean defaultValue;
    final CaTier tier;
    WheelFilter(WheelType wheel, String section, String key, String title, boolean defaultValue, String tip)
    { this.wheel = wheel; this.section = section; this.key = key; this.title = title; this.defaultValue = defaultValue; this.tip = tip; tier = null; }
    WheelFilter(CaTier tier)
    {
        wheel = WheelType.COMBAT_ACHIEVEMENTS; section = "Exclude task tiers";
        key = "caExclude" + tier.title; title = "Exclude " + tier.title; defaultValue = false;
        tip = "Ignore unfinished " + tier.title + " tasks when deciding which encounters to include.";
        this.tier = tier;
    }
}
