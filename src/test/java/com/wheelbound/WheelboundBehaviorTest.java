package com.wheelbound;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import net.runelite.api.Skill;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.HiscoreSkillType;
import org.junit.Test;
import static org.junit.Assert.*;

public class WheelboundBehaviorTest
{
    private BossDefinition boss(HiscoreSkill id)
    {
        return BossCatalog.ALL.stream().filter(b -> b.hiscore == id).findFirst().orElseThrow();
    }

    @Test public void catalogUsesCanonicalSpritesAndNames()
    {
        long count = Arrays.stream(HiscoreSkill.values()).filter(s -> s.getType() == HiscoreSkillType.BOSS).count();
        assertEquals(count, BossCatalog.ALL.size());
        assertEquals(count, BossCatalog.ALL.stream().map(b -> b.hiscore).distinct().count());
        assertEquals(Set.of("Mad Angel", "Maggot King", "Shellbane Gryphon"),
            BossCatalog.ALL.stream().filter(b -> b.profile == null).map(b -> b.name).collect(java.util.stream.Collectors.toSet()));
        for (BossDefinition boss : BossCatalog.ALL)
        {
            assertEquals(boss.hiscore.getName(), boss.name);
            assertEquals(boss.hiscore.getSpriteId(), boss.spriteId);
            assertTrue(boss.spriteId >= 0);
        }
    }

    @Test public void raidModesAreExplicitlyClassified()
    {
        assertEquals(6, BossCatalog.ALL.stream().filter(b -> b.raid).count());
        assertTrue(boss(HiscoreSkill.TOMBS_OF_AMASCUT_EXPERT).raid);
        assertFalse(boss(HiscoreSkill.THE_CORRUPTED_GAUNTLET).raid);
    }

    @Test public void allFilterCombinationsIntersect()
    {
        BossDefinition obor = boss(HiscoreSkill.OBOR);
        BossDefinition raid = boss(HiscoreSkill.CHAMBERS_OF_XERIC);
        BossDefinition hydra = boss(HiscoreSkill.ALCHEMICAL_HYDRA);
        List<BossDefinition> catalog = List.of(obor, raid, hydra);
        for (boolean raids : new boolean[]{false, true})
        for (boolean incomplete : new boolean[]{false, true})
        for (boolean levels : new boolean[]{false, true})
        {
            List<BossDefinition> result = WheelEligibility.bosses(catalog, raids, incomplete, levels, true,
                s -> 80, b -> b != obor);
            assertEquals(!incomplete, result.contains(obor));
            assertEquals(raids, result.contains(raid));
            assertEquals(!levels, result.contains(hydra));
        }
    }

    @Test public void realStyleStatsSlayerAndAdditionalRequirements()
    {
        BossProfile melee = boss(HiscoreSkill.OBOR).profile;
        assertFalse(melee.matches(s -> s == Skill.STRENGTH ? 1 : 99));
        assertFalse(melee.matches(s -> s == Skill.PRAYER ? 1 : 99));
        assertTrue(melee.matches(s -> s == Skill.MAGIC || s == Skill.RANGED ? 1 : 99));
        BossProfile hydra = boss(HiscoreSkill.ALCHEMICAL_HYDRA).profile;
        assertFalse(hydra.matches(s -> s == Skill.SLAYER ? 94 : 99));
        assertTrue(hydra.matches(s -> s == Skill.SLAYER ? 95 : 99));
        assertFalse(boss(HiscoreSkill.COMMANDER_ZILYANA).profile.matches(s -> s == Skill.AGILITY ? 69 : 99));
        assertFalse(boss(HiscoreSkill.CHAMBERS_OF_XERIC).profile.matches(s -> s == Skill.MAGIC ? 79 : 99));
        assertTrue(boss(HiscoreSkill.TEMPOROSS).profile.matches(s -> s == Skill.FISHING ? 35 : 1));
    }

    @Test public void unknownRecommendationsAndOfflineStateFailClosed()
    {
        BossDefinition unknown = new BossDefinition(HiscoreSkill.OBOR, false, null, List.of("Obor"));
        assertTrue(WheelEligibility.bosses(List.of(unknown), false, false, true, true, s -> 99, b -> true).isEmpty());
        assertEquals(1, WheelEligibility.bosses(List.of(unknown), false, false, false, false, s -> 1, b -> false).size());
        assertTrue(WheelEligibility.bosses(BossCatalog.ALL, true, true, false, false, s -> 99, b -> true).isEmpty());
        assertTrue(WheelEligibility.bosses(List.of(), true, false, false, true, s -> 99, b -> true).isEmpty());
    }

    @Test public void caAliasesKeepDifferentModesSeparate()
    {
        assertEquals(List.of("Barrows"), boss(HiscoreSkill.BARROWS_CHESTS).caEncounters);
        assertTrue(boss(HiscoreSkill.DAGANNOTH_REX).caEncounters.contains("Dagannoth Kings"));
        assertFalse(boss(HiscoreSkill.THEATRE_OF_BLOOD_HARD_MODE).caEncounters.contains("Theatre of Blood"));
        assertTrue(boss(HiscoreSkill.TZKAL_ZUK).caEncounters.contains("The Inferno"));
    }

    @Test public void completionCacheRefreshesAllWordsAndResetsBetweenAccounts()
    {
        CombatAchievementCache cache = new CombatAchievementCache();
        BossDefinition obor = boss(HiscoreSkill.OBOR);
        Map<String, List<Integer>> mapping = Map.of("Obor", List.of(0, 31, 32));
        AtomicInteger reads = new AtomicInteger();
        cache.refresh("A", BossCatalog.ALL, mapping, varp -> { reads.incrementAndGet(); return 0; });
        assertEquals(BossData.COMPLETION.length, reads.get());
        assertTrue(cache.hasIncomplete("A", obor));
        assertFalse(cache.hasIncomplete("B", obor));
        for (int i = 0; i < 10; i++) { cache.hasIncomplete("A", obor); }
        assertEquals(BossData.COMPLETION.length, reads.get());
        cache.refresh("A", BossCatalog.ALL, mapping, varp -> -1);
        assertFalse(cache.hasIncomplete("A", obor));
        cache.refresh("B", BossCatalog.ALL, mapping, varp -> 0);
        assertFalse(cache.hasIncomplete("A", obor));
        assertTrue(cache.hasIncomplete("B", obor));
        cache.reset();
        assertFalse(cache.isReady("B"));
        assertFalse(cache.hasIncomplete("B", obor));
    }

    @Test public void caPartialCompletionUnknownIdsAndMissingMapping()
    {
        CombatAchievementCache cache = new CombatAchievementCache();
        BossDefinition obor = boss(HiscoreSkill.OBOR);
        cache.refresh("A", BossCatalog.ALL, Map.of("Obor", List.of(0, 31)), varp -> 1);
        assertTrue(cache.hasIncomplete("A", obor));
        cache.refresh("A", BossCatalog.ALL, Map.of("Obor", List.of(-1, 100000)), varp -> 0);
        assertFalse(cache.hasIncomplete("A", obor));
        cache.refresh("A", BossCatalog.ALL, Map.of("Unknown encounter", List.of(0)), varp -> 0);
        assertFalse(cache.hasIncomplete("A", obor));
        cache.refresh(null, BossCatalog.ALL, Map.of("Obor", List.of(0)), varp -> { fail(); return 0; });
        assertFalse(cache.isReady(null));
    }

    @Test public void onlyCompletionVarpsTriggerRefresh()
    {
        for (int id : BossData.COMPLETION) { assertTrue(CombatAchievementCache.isCompletionVarp(id)); }
        assertFalse(CombatAchievementCache.isCompletionVarp(-1));
        assertFalse(CombatAchievementCache.isCompletionVarp(3138));
        assertTrue(BossData.isComplete(Integer.MIN_VALUE, 31));
        assertFalse(BossData.isComplete(Integer.MIN_VALUE, 30));
    }

    @Test public void skillsExcludeReal99AndOverallButIncludeAllTrainableSkills()
    {
        List<Skill> all = WheelEligibility.skills(false, false, s -> 99);
        assertEquals(Skill.values().length, all.size());
        assertTrue(all.stream().allMatch(Objects::nonNull));
        assertTrue(all.contains(Skill.SAILING));
        assertTrue(all.contains(Skill.ATTACK));
        assertTrue(WheelEligibility.skills(true, true, s -> 99).isEmpty());
        assertTrue(WheelEligibility.skills(true, false, s -> 1).isEmpty());
        assertEquals(List.of(Skill.MINING), WheelEligibility.skills(true, true, s -> s == Skill.MINING ? 98 : 99));
    }

    @Test public void weightedTicketsExactlyMatchVisualWedges()
    {
        List<WheelEntry> entries = XpGoal.entries();
        assertEquals(100, WheelSelection.totalWeight(entries));
        int[] counts = new int[entries.size()];
        for (int ticket = 0; ticket < 100; ticket++) { counts[WheelSelection.indexAt(entries, ticket)]++; }
        for (int i = 0; i < entries.size(); i++) { assertEquals(entries.get(i).weight, counts[i]); }
        assertEquals(1, entries.get(entries.size() - 1).weight);
        assertEquals(1_000_000, XpGoal.MILLION.xp);
        assertEquals(-1, WheelSelection.select(List.of(), new Random(1)));
        assertEquals(-1, WheelSelection.indexAt(entries, 100));
    }

    @Test public void weightedAndEqualWheelsLandAtChosenSliceAfterRepeatedSpins()
    {
        double angle = 0;
        for (List<WheelEntry> entries : List.of(XpGoal.entries(),
            List.of(new WheelEntry("a", "A", null, 1), new WheelEntry("b", "B", null, 1))))
        {
            for (int selected = 0; selected < entries.size(); selected++)
            {
                double end = WheelSelection.targetAngle(angle, entries, selected);
                assertTrue(end - angle >= 1800);
                assertEquals(90, ((WheelSelection.center(entries, selected) - end) % 360 + 360) % 360, .000001);
                angle = end;
            }
        }
    }

    @Test public void sampledBossPoolHasNoDuplicatesAndUniformOverallOdds()
    {
        Random random = new Random(1234);
        List<Integer> pool = new ArrayList<>();
        for (int i = 0; i < 70; i++) { pool.add(i); }
        int[] selected = new int[70];
        for (int i = 0; i < 70000; i++)
        {
            List<Integer> sample = WheelSelection.sample(pool, 12, random);
            assertEquals(12, new HashSet<>(sample).size());
            selected[sample.get(random.nextInt(sample.size()))]++;
        }
        for (int count : selected) { assertTrue(count > 800 && count < 1200); }
        assertTrue(WheelSelection.sample(List.of(), 12, random).isEmpty());
    }
}
