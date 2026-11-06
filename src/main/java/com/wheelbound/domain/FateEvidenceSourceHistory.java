package com.wheelbound.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Structural cross-assignment source uniqueness for explicitly supplied history.
 * The caller must authenticate the run, attribution and source keys and supply
 * complete retained history under the publication lock. This does not establish
 * the current assignment, evidence coverage, durability or completion eligibility.
 * XP and kills are independent metrics: one game event may produce both.
 */
public final class FateEvidenceSourceHistory
{
    private FateEvidenceSourceHistory() { }

    public static void validate(RunIdentity identity,
        Collection<FateXpEvidenceJournal> xpJournals,
        Collection<FateKillEvidenceJournal> killJournals)
    {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(xpJournals, "xpJournals");
        Objects.requireNonNull(killJournals, "killJournals");
        var xp = new Sources(identity);
        for (FateXpEvidenceJournal journal : xpJournals)
        {
            Objects.requireNonNull(journal, "XP journal");
            xp.assignment(journal.identity(), journal.assignmentId());
            for (FateXpEvidenceJournal.Event event : journal.events())
            { xp.event(event.sessionId(), event.namespace(), event.eventKey(), journal.assignmentId()); }
        }
        var kills = new Sources(identity);
        for (FateKillEvidenceJournal journal : killJournals)
        {
            Objects.requireNonNull(journal, "kill journal");
            kills.assignment(journal.identity(), journal.assignmentId());
            for (FateKillEvidenceJournal.Event event : journal.events())
            { kills.event(event.sessionId(), event.namespace(), event.eventKey(), journal.assignmentId()); }
        }
    }

    private static final class Sources
    {
        private final RunIdentity identity;
        private final Set<UUID> assignments = new HashSet<>();
        private final Map<UUID, Map<String, Map<String, UUID>>> sources = new HashMap<>();

        private Sources(RunIdentity identity) { this.identity = identity; }

        private void assignment(RunIdentity run, UUID assignment)
        {
            if (!identity.equals(run)) { throw new IllegalArgumentException("Foreign evidence history"); }
            // Two heads for one assignment cannot establish its authoritative history.
            if (!assignments.add(assignment))
            { throw new IllegalArgumentException("Duplicate assignment journal"); }
        }

        private void event(UUID session, String namespace, String key, UUID assignment)
        {
            UUID prior = sources.computeIfAbsent(session, ignored -> new HashMap<>())
                .computeIfAbsent(namespace, ignored -> new HashMap<>()).putIfAbsent(key, assignment);
            if (prior != null)
            { throw new IllegalArgumentException("Source event reused across assignments"); }
        }
    }
}
