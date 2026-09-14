package com.wheelbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.StructComposition;
import net.runelite.api.gameval.VarPlayerID;

/** Read-only game-cache metadata and character completion flags. Client thread only. */
final class BossData
{
	// Cache schema also documented by Combat Achievements Tracker's DataLoader:
	// https://github.com/ehubbartt/combat-achievements-tracker
	private static final int BOSS_NAMES = 3971;
	private static final int TASK_ID = 1306;
	private static final int TASK_BOSS = 1312;
	static final int[] COMPLETION = {
		VarPlayerID.CA_TASK_COMPLETED_0, VarPlayerID.CA_TASK_COMPLETED_1,
		VarPlayerID.CA_TASK_COMPLETED_2, VarPlayerID.CA_TASK_COMPLETED_3,
		VarPlayerID.CA_TASK_COMPLETED_4, VarPlayerID.CA_TASK_COMPLETED_5,
		VarPlayerID.CA_TASK_COMPLETED_6, VarPlayerID.CA_TASK_COMPLETED_7,
		VarPlayerID.CA_TASK_COMPLETED_8, VarPlayerID.CA_TASK_COMPLETED_9,
		VarPlayerID.CA_TASK_COMPLETED_10, VarPlayerID.CA_TASK_COMPLETED_11,
		VarPlayerID.CA_TASK_COMPLETED_12, VarPlayerID.CA_TASK_COMPLETED_13,
		VarPlayerID.CA_TASK_COMPLETED_14, VarPlayerID.CA_TASK_COMPLETED_15,
		VarPlayerID.CA_TASK_COMPLETED_16, VarPlayerID.CA_TASK_COMPLETED_17,
		VarPlayerID.CA_TASK_COMPLETED_18, VarPlayerID.CA_TASK_COMPLETED_19,
		VarPlayerID.CA_TASK_COMPLETED_20
	};
	private final Map<String, List<Integer>> tasks = new TreeMap<>();

	void clear()
	{
		tasks.clear();
	}

	Map<String, List<Integer>> load(Client client)
	{
		if (!tasks.isEmpty())
		{
			return java.util.Collections.unmodifiableMap(tasks);
		}
		Map<String, List<Integer>> loaded = new TreeMap<>();
		EnumComposition names = client.getEnum(BOSS_NAMES);
		if (names == null) { throw new IllegalStateException("Encounter names unavailable"); }
		for (int tier = 3981; tier <= 3986; tier++)
		{
			EnumComposition entries = client.getEnum(tier);
			if (entries == null || entries.getIntVals() == null || entries.getIntVals().length == 0)
			{
				throw new IllegalStateException("Achievement tier unavailable");
			}
			for (int structId : entries.getIntVals())
			{
				StructComposition task = client.getStructComposition(structId);
				if (task == null) { throw new IllegalStateException("Achievement unavailable"); }
				int id = task.getIntValue(TASK_ID);
				String name = names.getStringValue(task.getIntValue(TASK_BOSS));
				if (name == null || name.isBlank() || name.equalsIgnoreCase("null") || name.equalsIgnoreCase("General")) { continue; }
				loaded.computeIfAbsent(name, ignored -> new ArrayList<>()).add(id);
			}
		}
		if (loaded.isEmpty()) { throw new IllegalStateException("Achievement catalogue unavailable"); }
		loaded.replaceAll((name, ids) -> List.copyOf(ids));
		tasks.putAll(loaded);
		return java.util.Collections.unmodifiableMap(tasks);
	}

	static boolean isComplete(int flags, int bit)
	{
		return (flags & (1 << bit)) != 0;
	}
}
