package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Pure assignment-bound kill accounting (WB-D087) with exact source-event replay. Source
 * identities and attribution must come from a trusted observation boundary;
 * matching caller-supplied IDs do not authenticate evidence. The enclosing
 * command must resolve the active assignment, reject cross-assignment event reuse
 * and atomically persist the full journal. No coverage or clean audit is implied,
 * even by an empty journal. Unknown attribution stays unknown; counts never infer
 * unauthorized combat XP, incidental-kill classification or settlement. This
 * value has no production client or save caller.
 */
public final class FateKillEvidenceJournal
{
    /** Immutable original observation; never regenerate source keys on retry. */
    public static final class Event
    {
        private final RunIdentity identity;
        private final UUID assignmentId;
        private final UUID sessionId;
        private final String namespace;
        private final String eventKey;
        private final FateKillEvidence.Attribution attribution;
        private final long delta;

        public Event(RunIdentity identity, UUID assignmentId, UUID sessionId,
            String namespace, String eventKey, FateKillEvidence.Attribution attribution, long delta)
        {
            this.identity = Objects.requireNonNull(identity, "identity");
            this.assignmentId = Objects.requireNonNull(assignmentId, "assignmentId");
            this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
            this.namespace = requireKey(namespace);
            this.eventKey = requireKey(eventKey);
            this.attribution = Objects.requireNonNull(attribution, "attribution");
            if (delta < 0) { throw new IllegalArgumentException("Negative kill delta"); }
            this.delta = delta;
        }

        private static String requireKey(String value)
        {
            Objects.requireNonNull(value, "source key");
            if (value.isBlank() || !value.equals(value.strip()))
            { throw new IllegalArgumentException("Noncanonical source key"); }
            return value;
        }

        public RunIdentity identity() { return identity; }
        public UUID assignmentId() { return assignmentId; }
        public UUID sessionId() { return sessionId; }
        public String namespace() { return namespace; }
        public String eventKey() { return eventKey; }
        public FateKillEvidence.Attribution attribution() { return attribution; }
        public long delta() { return delta; }

        private Source source() { return new Source(sessionId, namespace, eventKey); }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Event)) { return false; }
            Event other = (Event) value;
            return identity.equals(other.identity) && assignmentId.equals(other.assignmentId)
                && source().equals(other.source()) && attribution == other.attribution && delta == other.delta;
        }

        @Override public int hashCode()
        { return Objects.hash(identity, assignmentId, source(), attribution, delta); }
    }

    private static final class Source
    {
        private final UUID session;
        private final String namespace;
        private final String key;

        private Source(UUID session, String namespace, String key)
        { this.session = session; this.namespace = namespace; this.key = key; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Source)) { return false; }
            Source other = (Source) value;
            return session.equals(other.session) && namespace.equals(other.namespace) && key.equals(other.key);
        }

        @Override public int hashCode() { return Objects.hash(session, namespace, key); }
    }

    private final RunIdentity identity;
    private final UUID assignmentId;
    private final Map<Source, Event> events;
    private final FateKillEvidence totals;

    /**
     * Reconstruct totals solely from explicit trusted observations. Duplicate
     * source identities, including identical duplicates, are invalid saved input.
     * This cannot establish that observation history is complete or authentic.
     */
    public FateKillEvidenceJournal(RunIdentity identity, UUID assignmentId, Collection<Event> observations)
    {
        this.identity = Objects.requireNonNull(identity, "identity");
        this.assignmentId = Objects.requireNonNull(assignmentId, "assignmentId");
        Objects.requireNonNull(observations, "observations");
        Map<Source, Event> retained = new LinkedHashMap<>();
        FateKillEvidence sum = new FateKillEvidence(0, 0, 0);
        for (Event event : observations)
        {
            checkBinding(Objects.requireNonNull(event, "event"));
            if (retained.putIfAbsent(event.source(), event) != null)
            { throw new IllegalArgumentException("Duplicate kill source event"); }
            sum = sum.observe(RunLifecycle.State.ACTIVE, event.attribution(), event.delta());
        }
        this.events = Collections.unmodifiableMap(retained);
        this.totals = sum;
    }

    public RunIdentity identity() { return identity; }
    public UUID assignmentId() { return assignmentId; }
    public Collection<Event> events() { return events.values(); }
    public FateKillEvidence totals() { return totals; }

    /**
     * Propose one currently admitted observation. Exact replay returns this head,
     * including any later observations. Conflicting payloads never replace prior
     * attribution. Inactive/stale/foreign observations are refused before replay;
     * paused observations cannot be buffered for retrospective credit here.
     */
    public FateKillEvidenceJournal observe(RunLifecycle current, long expectedRevision,
        UUID currentSessionId, Event event)
    {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(currentSessionId, "currentSessionId");
        checkBinding(Objects.requireNonNull(event, "event"));
        if (!identity.equals(current.identity()) || current.revision() != expectedRevision
            || !currentSessionId.equals(event.sessionId()))
        { throw new IllegalArgumentException("Foreign or stale kill observation"); }
        if (current.state() != RunLifecycle.State.ACTIVE)
        { throw new IllegalStateException("Active observation required"); }
        Event prior = events.get(event.source());
        if (prior != null)
        {
            if (!prior.equals(event)) { throw new IllegalArgumentException("Conflicting kill source event"); }
            return this;
        }
        var next = new ArrayList<>(events.values());
        next.add(event);
        return new FateKillEvidenceJournal(identity, assignmentId, next);
    }

    private void checkBinding(Event event)
    {
        if (!identity.equals(event.identity()) || !assignmentId.equals(event.assignmentId()))
        { throw new IllegalArgumentException("Foreign run or assignment kill evidence"); }
    }
}
