package com.wheelbound;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import net.runelite.api.Skill;

/** Conservative Wheelbound recommendations, not mandatory game requirements. */
final class BossProfile
{
	private final Map<Skill, Integer> additional = new java.util.EnumMap<>(Skill.class);

	BossProfile requiring(Skill skill, int minimum)
	{
		additional.put(skill, minimum);
		return this;
	}
	private final Skill style;
	private final int level;
	private final int defence;
	private final int prayer;
	private final int slayer;

	BossProfile(Skill style, int level, int defence, int prayer, int slayer)
	{
		this.style = style;
		this.level = level;
		this.defence = defence;
		this.prayer = prayer;
		this.slayer = slayer;
	}

	boolean matches(ToIntFunction<Skill> stats)
	{
		return stats.applyAsInt(style) >= level
			&& (style != Skill.ATTACK || stats.applyAsInt(Skill.STRENGTH) >= level)
			&& stats.applyAsInt(Skill.DEFENCE) >= defence
			&& stats.applyAsInt(Skill.PRAYER) >= prayer
			&& stats.applyAsInt(Skill.SLAYER) >= slayer
			&& additional.entrySet().stream().allMatch(e -> stats.applyAsInt(e.getKey()) >= e.getValue());
	}

	static Map<String, BossProfile> defaults()
	{
		Map<String, BossProfile> profiles = new LinkedHashMap<>();
		add(profiles, Skill.ATTACK, 50, 40, 43, 1, "Scurrius", "Obor", "Bryophyta");
		add(profiles, Skill.MAGIC, 50, 40, 43, 1, "Barrows");
		add(profiles, Skill.RANGED, 70, 60, 43, 1, "Giant Mole", "King Black Dragon", "Chaos Elemental", "Chaos Fanatic", "Crazy Archaeologist", "Deranged Archaeologist");
		add(profiles, Skill.ATTACK, 75, 70, 60, 1, "Sarachnis", "Kalphite Queen", "Dagannoth Kings", "Grotesque Guardians", "The Hueycoatl", "Moons of Peril");
		add(profiles, Skill.RANGED, 85, 75, 70, 1, "Zulrah", "Vorkath", "Phantom Muspah", "Alchemical Hydra");
		add(profiles, Skill.ATTACK, 85, 80, 70, 1, "General Graardor", "K'ril Tsutsaroth", "Corporeal Beast", "Duke Sucellus", "Vardorvis", "The Nightmare", "Phosani's Nightmare", "Yama", "Araxxor", "Abyssal Sire", "Cerberus");
		add(profiles, Skill.RANGED, 85, 80, 70, 1, "Commander Zilyana", "Kree'Arra", "The Leviathan", "TzTok-Jad", "TzKal-Zuk", "The Fight Caves", "The Inferno");
		add(profiles, Skill.MAGIC, 85, 75, 70, 1, "The Whisperer", "Kraken");
		add(profiles, Skill.ATTACK, 80, 75, 70, 1, "Chambers of Xeric", "Theatre of Blood", "Tombs of Amascut", "The Gauntlet", "Corrupted Gauntlet", "The Corrupted Gauntlet", "Fortis Colosseum");
		add(profiles, Skill.ATTACK, 75, 70, 60, 75, "Grotesque Guardians");
		add(profiles, Skill.ATTACK, 85, 80, 70, 85, "Abyssal Sire");
		add(profiles, Skill.MAGIC, 85, 75, 70, 87, "Kraken");
		add(profiles, Skill.ATTACK, 85, 80, 70, 91, "Cerberus");
		add(profiles, Skill.ATTACK, 85, 80, 70, 92, "Araxxor");
		add(profiles, Skill.RANGED, 85, 75, 70, 95, "Alchemical Hydra");
		add(profiles, Skill.FISHING, 35, 1, 1, 1, "Tempoross");
		add(profiles, Skill.FIREMAKING, 50, 1, 1, 1, "Wintertodt");
		add(profiles, Skill.ATTACK, 40, 30, 20, 1, "Brutus");
		add(profiles, Skill.ATTACK, 70, 60, 43, 1, "Amoxliatl", "Calvar'ion", "Vet'ion", "Spindel", "Venenatis", "Skotizo", "Mimic", "Royal Titans");
		add(profiles, Skill.MAGIC, 70, 50, 43, 1, "Artio", "Callisto", "Scorpia", "Dagannoth Rex");
		add(profiles, Skill.MAGIC, 60, 40, 43, 1, "Crazy Archaeologist", "Deranged Archaeologist");
		add(profiles, Skill.RANGED, 75, 60, 43, 1, "Dagannoth Prime");
		add(profiles, Skill.ATTACK, 75, 60, 43, 1, "Dagannoth Supreme");
		add(profiles, Skill.MAGIC, 75, 70, 43, 93, "Thermonuclear Smoke Devil");
		add(profiles, Skill.RANGED, 90, 85, 77, 1, "Nex", "TzKal-Zuk", "The Inferno");
		add(profiles, Skill.ATTACK, 90, 85, 77, 1, "Sol Heredit", "Fortis Colosseum");
		add(profiles, Skill.ATTACK, 90, 85, 77, 90, "Doom of Mokhaiotl");
		for (String name : new String[]{"Chambers of Xeric", "Chambers of Xeric: Challenge Mode",
			"Theatre of Blood", "Theatre of Blood: Hard Mode", "Tombs of Amascut", "Tombs of Amascut: Expert Mode"})
		{
			profiles.put(name, new BossProfile(Skill.ATTACK, 80, 75, 70, 1)
				.requiring(Skill.RANGED, 80).requiring(Skill.MAGIC, 80));
		}
		profiles.put("Hespori", new BossProfile(Skill.ATTACK, 60, 50, 43, 1).requiring(Skill.FARMING, 65));
		profiles.put("Zalcano", new BossProfile(Skill.MINING, 70, 1, 1, 1)
			.requiring(Skill.SMITHING, 70).requiring(Skill.RUNECRAFT, 70));
		profiles.put("Commander Zilyana", profiles.get("Commander Zilyana").requiring(Skill.AGILITY, 70));
		profiles.put("The Gauntlet", profiles.get("The Gauntlet").requiring(Skill.RANGED, 80).requiring(Skill.MAGIC, 80));
		profiles.put("The Corrupted Gauntlet", profiles.get("The Corrupted Gauntlet").requiring(Skill.RANGED, 85).requiring(Skill.MAGIC, 85));
		return profiles;
	}

	private static void add(Map<String, BossProfile> map, Skill skill, int level, int defence, int prayer, int slayer, String... names)
	{
		for (String name : names)
		{
			map.put(name, new BossProfile(skill, level, defence, prayer, slayer));
		}
	}
}
