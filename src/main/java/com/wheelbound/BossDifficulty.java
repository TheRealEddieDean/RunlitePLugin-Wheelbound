package com.wheelbound;

import java.util.Set;

/** Local mapping of the OSRS Wiki Bossing Ladder, with solo GWD ratings where listed. */
enum BossDifficulty
{
    EASY("Easy"), MEDIUM("Medium"), HARD("Hard"), ELITE("Elite"), MASTER("Master"),
    GRANDMASTER("Grandmaster"), UNRATED("Unrated");

    final String title;
    BossDifficulty(String title) { this.title = title; }

    static BossDifficulty of(BossDefinition boss)
    {
        switch (boss.hiscore)
        {
            case BRUTUS: case WINTERTODT: case TEMPOROSS: case BARROWS_CHESTS:
            case OBOR: case BRYOPHYTA: case GIANT_MOLE: case DERANGED_ARCHAEOLOGIST: case SCURRIUS:
                return EASY;
            case AMOXLIATL: case THE_HUEYCOATL: case HESPORI: case CRAZY_ARCHAEOLOGIST:
            case CHAOS_FANATIC: case KRAKEN: case SARACHNIS: case KING_BLACK_DRAGON:
            case ZALCANO: case THERMONUCLEAR_SMOKE_DEVIL: case MIMIC: case THE_ROYAL_TITANS:
            case LUNAR_CHESTS: return MEDIUM;
            case SCORPIA: case CHAOS_ELEMENTAL: case VETION: case CALVARION:
            case VENENATIS: case SPINDEL: case CALLISTO: case ARTIO:
            case DAGANNOTH_PRIME: case DAGANNOTH_REX: case DAGANNOTH_SUPREME:
            case GROTESQUE_GUARDIANS: case SKOTIZO: case TZTOK_JAD: case THE_GAUNTLET:
            case ABYSSAL_SIRE: case CERBERUS: case ARAXXOR: case ALCHEMICAL_HYDRA:
            case KALPHITE_QUEEN: case KRIL_TSUTSAROTH: case COMMANDER_ZILYANA: return HARD;
            case ZULRAH: case VORKATH: case PHANTOM_MUSPAH: case THE_CORRUPTED_GAUNTLET:
            case GENERAL_GRAARDOR: case KREEARRA: case DUKE_SUCELLUS: case THE_WHISPERER:
            case THE_LEVIATHAN: case VARDORVIS: case NEX:
            case TOMBS_OF_AMASCUT: case TOMBS_OF_AMASCUT_EXPERT: return ELITE;
            case CHAMBERS_OF_XERIC: case NIGHTMARE: case PHOSANIS_NIGHTMARE: case YAMA:
            case DOOM_OF_MOKHAIOTL: case THEATRE_OF_BLOOD: case TZKAL_ZUK: case SOL_HEREDIT:
                return MASTER;
            case CHAMBERS_OF_XERIC_CHALLENGE_MODE: case THEATRE_OF_BLOOD_HARD_MODE:
                return GRANDMASTER;
            default: return UNRATED;
        }
    }

    static boolean included(BossDefinition boss, Set<WheelFilter> filters)
    { return filters.contains(WheelFilter.valueOf("BOSS_" + of(boss).name())); }
}
