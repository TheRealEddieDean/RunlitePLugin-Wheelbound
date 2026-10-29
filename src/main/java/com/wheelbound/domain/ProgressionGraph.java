package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Immutable prerequisite rules only. Eligibility does not purchase, grant benefits or add slices. */
public final class ProgressionGraph
{
    public static final class Requirement
    {
        private enum Kind { ROOT, OWNED, ALL, ANY }
        private final Kind kind;
        private final String node;
        private final List<Requirement> children;

        private Requirement(Kind kind, String node, List<Requirement> children)
        {
            this.kind = kind;
            this.node = node;
            this.children = List.copyOf(children);
        }

        /** Explicitly declares a node with no prerequisite; does not make it free. */
        public static Requirement root() { return new Requirement(Kind.ROOT, null, List.of()); }

        public static Requirement owned(String node)
        {
            return new Requirement(Kind.OWNED, validId(node), List.of());
        }

        public static Requirement all(List<Requirement> children) { return group(Kind.ALL, children); }
        public static Requirement any(List<Requirement> children) { return group(Kind.ANY, children); }

        private static Requirement group(Kind kind, List<Requirement> children)
        {
            if (children.isEmpty()) { throw new IllegalArgumentException("Empty prerequisite group"); }
            return new Requirement(kind, null, children);
        }

        private boolean satisfied(Set<String> owned)
        {
            switch (kind)
            {
                case ROOT: return true;
                case OWNED: return owned.contains(node);
                case ALL: return children.stream().allMatch(child -> child.satisfied(owned));
                case ANY: return children.stream().anyMatch(child -> child.satisfied(owned));
                default: throw new IllegalStateException("Unknown prerequisite kind");
            }
        }

        private void references(Set<String> result)
        {
            if (kind == Kind.OWNED) { result.add(node); }
            for (Requirement child : children) { child.references(result); }
        }
    }

    private final Map<String, Requirement> rules;

    public ProgressionGraph(Map<String, Requirement> rules)
    {
        this.rules = Map.copyOf(rules);
        if (this.rules.isEmpty()) { throw new IllegalArgumentException("Empty progression graph"); }
        this.rules.keySet().forEach(ProgressionGraph::validId);
        Set<String> complete = new HashSet<>();
        for (String node : this.rules.keySet()) { validate(node, new HashSet<>(), complete); }
    }

    private void validate(String node, Set<String> visiting, Set<String> complete)
    {
        if (complete.contains(node)) { return; }
        Requirement rule = rules.get(node);
        if (rule == null) { throw new IllegalArgumentException("Unknown prerequisite: " + node); }
        if (!visiting.add(node)) { throw new IllegalArgumentException("Cyclic prerequisites: " + node); }
        Set<String> references = new HashSet<>();
        rule.references(references);
        for (String reference : references) { validate(reference, visiting, complete); }
        visiting.remove(node);
        complete.add(node);
    }

    /** Checks actual owned nodes, never inferring ownership from a satisfied prerequisite. */
    public boolean canUnlock(String node, Set<String> ownedNodes)
    {
        Requirement rule = rules.get(validId(node));
        if (rule == null) { throw new IllegalArgumentException("Unknown progression node: " + node); }
        Set<String> owned = Set.copyOf(ownedNodes);
        if (!rules.keySet().containsAll(owned))
        {
            throw new IllegalArgumentException("Ownership contains unknown progression nodes");
        }
        return !owned.contains(node) && rule.satisfied(owned);
    }

    private static String validId(String node)
    {
        Objects.requireNonNull(node, "node");
        if (node.trim().isEmpty()) { throw new IllegalArgumentException("Blank progression node"); }
        return node;
    }
}
