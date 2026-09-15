package com.wheelbound;

import java.util.List;
import net.runelite.api.Skill;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.hiscore.HiscoreSkill;

/** Huntable base pets. Item IDs/names match RuneLite 1.12.38; morphs share one entry. */
final class PetDefinition
{
    enum Kind { BOSS, RAID, SKILL, ACTIVITY }
    final int itemId;
    final String name, source;
    final Kind kind;
    final HiscoreSkill boss;
    final Skill skill;
    final int sourceItemId;

    private PetDefinition(int itemId, String name, String source, Kind kind,
        HiscoreSkill boss, Skill skill, int sourceItemId)
    {
        this.itemId = itemId; this.name = name; this.source = source; this.kind = kind;
        this.boss = boss; this.skill = skill; this.sourceItemId = sourceItemId;
    }

    private static PetDefinition boss(int id, String name, HiscoreSkill boss)
    { return boss(id, name, boss, boss.getName()); }
    private static PetDefinition boss(int id, String name, HiscoreSkill boss, String source)
    {
        boolean raid = boss == HiscoreSkill.CHAMBERS_OF_XERIC || boss == HiscoreSkill.THEATRE_OF_BLOOD
            || boss == HiscoreSkill.TOMBS_OF_AMASCUT;
        return new PetDefinition(id, name, source, raid ? Kind.RAID : Kind.BOSS, boss, null, -1);
    }
    private static PetDefinition skill(int id, String name, Skill skill)
    { return skill(id, name, skill, skill.getName()); }
    private static PetDefinition skill(int id, String name, Skill skill, String source)
    { return new PetDefinition(id, name, source, Kind.SKILL, null, skill, -1); }
    private static PetDefinition activity(int id, String name, String source, int sourceItem)
    { return new PetDefinition(id, name, source, Kind.ACTIVITY, null, null, sourceItem); }

    static final List<PetDefinition> ALL = List.of(
        boss(ItemID.ABYSSALSIRE_PET, "Abyssal orphan", HiscoreSkill.ABYSSAL_SIRE),
        boss(ItemID.HYDRAPET, "Ikkle Hydra", HiscoreSkill.ALCHEMICAL_HYDRA),
        boss(ItemID.CALLISTO_PET, "Callisto cub", HiscoreSkill.CALLISTO, "Callisto / Artio"),
        boss(ItemID.HELL_PET, "Hellpuppy", HiscoreSkill.CERBERUS),
        boss(ItemID.CHAOSELEPET, "Pet Chaos Elemental", HiscoreSkill.CHAOS_ELEMENTAL, "Chaos Elemental / Chaos Fanatic"),
        boss(ItemID.SARADOMINPET, "Pet Zilyana", HiscoreSkill.COMMANDER_ZILYANA),
        boss(ItemID.COREPET, "Pet dark core", HiscoreSkill.CORPOREAL_BEAST),
        boss(ItemID.PRIMEPET, "Pet Dagannoth Prime", HiscoreSkill.DAGANNOTH_PRIME),
        boss(ItemID.SUPREMEPET, "Pet Dagannoth Supreme", HiscoreSkill.DAGANNOTH_SUPREME),
        boss(ItemID.REXPET, "Pet Dagannoth Rex", HiscoreSkill.DAGANNOTH_REX),
        boss(ItemID.JAD_PET, "TzRek-Jad", HiscoreSkill.TZTOK_JAD),
        boss(ItemID.BANDOSPET, "Pet General Graardor", HiscoreSkill.GENERAL_GRAARDOR),
        boss(ItemID.MOLEPET, "Baby Mole", HiscoreSkill.GIANT_MOLE),
        boss(ItemID.DAWNPET, "Noon", HiscoreSkill.GROTESQUE_GUARDIANS),
        boss(ItemID.INFERNOPET, "Jal-Nib-Rek", HiscoreSkill.TZKAL_ZUK),
        boss(ItemID.KQPET_WALKING, "Kalphite Princess", HiscoreSkill.KALPHITE_QUEEN),
        boss(ItemID.KBDPET, "Prince Black Dragon", HiscoreSkill.KING_BLACK_DRAGON),
        boss(ItemID.KRAKENPET, "Pet Kraken", HiscoreSkill.KRAKEN),
        boss(ItemID.ARMADYLPET, "Pet Kree'arra", HiscoreSkill.KREEARRA),
        boss(ItemID.ZAMORAKPET, "Pet K'ril Tsutsaroth", HiscoreSkill.KRIL_TSUTSAROTH),
        boss(ItemID.SCORPIA_PET, "Scorpia's offspring", HiscoreSkill.SCORPIA),
        boss(ItemID.SKOTIZOPET, "Skotos", HiscoreSkill.SKOTIZO),
        boss(ItemID.SMOKEPET, "Pet Smoke Devil", HiscoreSkill.THERMONUCLEAR_SMOKE_DEVIL),
        boss(ItemID.VENENATIS_PET, "Venenatis spiderling", HiscoreSkill.VENENATIS, "Venenatis / Spindel"),
        boss(ItemID.VETION_PET, "Vet'ion jr.", HiscoreSkill.VETION, "Vet'ion / Calvar'ion"),
        boss(ItemID.VORKATHPET, "Vorki", HiscoreSkill.VORKATH),
        boss(ItemID.PHOENIXPET, "Phoenix", HiscoreSkill.WINTERTODT),
        boss(ItemID.SNAKEPET, "Pet Snakeling", HiscoreSkill.ZULRAH),
        boss(ItemID.OLMPET, "Olmlet", HiscoreSkill.CHAMBERS_OF_XERIC),
        boss(ItemID.VERZIKPET, "Lil' Zik", HiscoreSkill.THEATRE_OF_BLOOD),
        boss(ItemID.SARACHNISPET, "Sraracha", HiscoreSkill.SARACHNIS),
        boss(ItemID.ZALCANOPET, "Smolcano", HiscoreSkill.ZALCANO),
        boss(ItemID.GAUNTLETPET, "Youngllef", HiscoreSkill.THE_GAUNTLET, "Gauntlet / Corrupted Gauntlet"),
        boss(ItemID.NIGHTMAREPET, "Little Nightmare", HiscoreSkill.NIGHTMARE, "Nightmare / Phosani's Nightmare"),
        boss(ItemID.TEMPOROSSPET, "Tiny tempor", HiscoreSkill.TEMPOROSS),
        boss(ItemID.NEXPET, "Nexling", HiscoreSkill.NEX),
        boss(ItemID.WARDENPET_TUMEKEN, "Tumeken's guardian", HiscoreSkill.TOMBS_OF_AMASCUT),
        boss(ItemID.MUSPAHPET, "Muphin", HiscoreSkill.PHANTOM_MUSPAH),
        boss(ItemID.WHISPERERPET, "Wisp", HiscoreSkill.THE_WHISPERER),
        boss(ItemID.VARDORVISPET, "Butch", HiscoreSkill.VARDORVIS),
        boss(ItemID.DUKESUCELLUSPET, "Baron", HiscoreSkill.DUKE_SUCELLUS),
        boss(ItemID.LEVIATHANPET, "Lil'viathan", HiscoreSkill.THE_LEVIATHAN),
        boss(ItemID.SCURRIUSPET, "Scurry", HiscoreSkill.SCURRIUS),
        boss(ItemID.SOLHEREDITPET, "Smol Heredit", HiscoreSkill.SOL_HEREDIT),
        boss(ItemID.ARAXXORPET, "Nid", HiscoreSkill.ARAXXOR),
        boss(ItemID.HUEYPET, "Huberte", HiscoreSkill.THE_HUEYCOATL),
        boss(ItemID.AMOXLIATLPET, "Moxi", HiscoreSkill.AMOXLIATL),
        boss(ItemID.RTBRANDAPET, "Bran", HiscoreSkill.THE_ROYAL_TITANS),
        boss(ItemID.YAMAPET, "Yami", HiscoreSkill.YAMA),
        boss(ItemID.DOMPET, "Dom", HiscoreSkill.DOOM_OF_MOKHAIOTL),
        boss(ItemID.GRYPHONBOSSPET, "Gull", HiscoreSkill.SHELLBANE_GRYPHON),
        boss(ItemID.COWBOSSPET, "Beef", HiscoreSkill.BRUTUS),
        boss(ItemID.MAGGOTKINGPET, "Maggot marquess", HiscoreSkill.MAGGOT_KING),
        boss(ItemID.MADANGELPET, "Aggy", HiscoreSkill.MAD_ANGEL),
        skill(ItemID.SKILLPETFISH, "Heron", Skill.FISHING),
        skill(ItemID.SKILLPETMINING, "Rock golem", Skill.MINING),
        skill(ItemID.SKILLPETWC, "Beaver", Skill.WOODCUTTING),
        skill(ItemID.SKILLPETHUNTER_GREY, "Baby chinchompa", Skill.HUNTER, "Hunter: chinchompas"),
        skill(ItemID.SKILLPETAGILITY, "Giant Squirrel", Skill.AGILITY),
        skill(ItemID.SKILLPETFARMING, "Tangleroot", Skill.FARMING),
        skill(ItemID.SKILLPETTHIEVING, "Rocky", Skill.THIEVING),
        skill(ItemID.SKILLPETRUNECRAFTING_FIRE, "Rift guardian", Skill.RUNECRAFT),
        skill(ItemID.HERBIBOARPET, "Herbi", Skill.HUNTER, "Hunter: herbiboars"),
        skill(ItemID.QUETZALPET, "Quetzin", Skill.HUNTER, "Hunter rumours"),
        skill(ItemID.SKILLPETSAILING, "Soup", Skill.SAILING),
        skill(ItemID.GOATPITPET, "Mr McGroot", Skill.HUNTER, "Hunter: Wyrmscraig goats"),
        activity(ItemID.BLOODHOUND_PET, "Bloodhound", "Master clue scrolls", ItemID.TRAIL_REWARD_CASKET_MASTER),
        activity(ItemID.PENANCEPET, "Pet Penance Queen", "Barbarian Assault", ItemID.BARBASSAULT_PENANCE_FIGHTER_TORSO),
        activity(ItemID.SOULWARSPET_BLUE, "Lil' Creator", "Soul Wars", ItemID.SOUL_WARS_SPOILS),
        activity(ItemID.ABYSSALPET, "Abyssal protector", "Guardians of the Rift", ItemID.GOTR_BOOK),
        activity(ItemID.CHOMPYBIRD_PET, "Chompy chick", "Chompy hunting (elite diary)", ItemID.RAW_CHOMPY)
    );
}
