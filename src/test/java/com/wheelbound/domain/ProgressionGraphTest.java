package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static com.wheelbound.domain.ProgressionGraph.Requirement.*;
import static org.junit.Assert.*;

public class ProgressionGraphTest
{
    @Test public void challengeEntryRequiresBothOrdinaryCardsAndActualPredecessorOwnership()
    {
        ProgressionGraph graph = new ProgressionGraph(Map.of(
            "bossing.lesser", root(), "bossing.greater", root(), "skilling.lesser", root(),
            "challenge.lesser", all(List.of(owned("bossing.lesser"), owned("bossing.greater"))),
            "challenge.standard", owned("challenge.lesser")));
        assertFalse(graph.canUnlock("challenge.lesser", Set.of()));
        assertFalse(graph.canUnlock("challenge.lesser", Set.of("bossing.lesser")));
        assertFalse(graph.canUnlock("challenge.lesser", Set.of("bossing.greater", "skilling.lesser")));
        Set<String> entry = Set.of("bossing.lesser", "bossing.greater");
        assertTrue(graph.canUnlock("challenge.lesser", entry));
        assertFalse(graph.canUnlock("challenge.standard", entry));
        assertTrue(graph.canUnlock("challenge.standard", Set.of("challenge.lesser")));
        assertFalse(graph.canUnlock("challenge.lesser", Set.of("challenge.lesser")));
    }

    @Test public void nestedAlternativePathsDoNotGrantOrConsumeOtherNodeBenefits()
    {
        ProgressionGraph graph = new ProgressionGraph(Map.of("a", root(), "b", root(), "c", root(),
            "goal", all(List.of(owned("a"), any(List.of(owned("b"), owned("c")))))));
        for (int mask = 0; mask < 8; mask++)
        {
            Set<String> owned = new HashSet<>();
            if ((mask & 1) != 0) { owned.add("a"); }
            if ((mask & 2) != 0) { owned.add("b"); }
            if ((mask & 4) != 0) { owned.add("c"); }
            Set<String> before = Set.copyOf(owned);
            assertEquals((mask & 1) != 0 && (mask & 6) != 0, graph.canUnlock("goal", owned));
            assertEquals(before, owned);
        }
    }

    @Test public void graphCopiesInputsAndRejectsMalformedCatalogs()
    {
        List<ProgressionGraph.Requirement> children = new ArrayList<>(List.of(owned("a")));
        Map<String, ProgressionGraph.Requirement> rules = new HashMap<>();
        rules.put("a", root());
        rules.put("b", all(children));
        ProgressionGraph graph = new ProgressionGraph(rules);
        children.clear();
        rules.clear();
        assertFalse(graph.canUnlock("b", Set.of()));
        assertTrue(graph.canUnlock("b", Set.of("a")));
        assertTrue(graph.canUnlock("a", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> all(List.of()));
        assertThrows(IllegalArgumentException.class, () -> any(List.of()));
        assertThrows(IllegalArgumentException.class, () -> owned(" "));
        assertThrows(NullPointerException.class, () -> all(java.util.Arrays.asList(root(), null)));
        assertThrows(IllegalArgumentException.class, () -> new ProgressionGraph(Map.of()));
        assertThrows(IllegalArgumentException.class, () -> new ProgressionGraph(Map.of("a", owned("missing"))));
        assertThrows(IllegalArgumentException.class, () -> new ProgressionGraph(Map.of("a", owned("a"))));
        assertThrows(IllegalArgumentException.class, () -> new ProgressionGraph(Map.of(
            "a", any(List.of(root(), owned("b"))), "b", owned("a"))));
        assertThrows(IllegalArgumentException.class, () -> graph.canUnlock("missing", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> graph.canUnlock("a", Set.of("foreign")));
    }
}
