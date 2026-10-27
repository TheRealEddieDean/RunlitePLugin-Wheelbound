package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Ordered immutable input snapshot. Eligibility and gameplay weights belong to the caller's reviewed catalog. */
public final class WeightedPool
{
    public static final class Entry
    {
        private final String id;
        private final int weight;
        public Entry(String id, int weight)
        {
            this.id = Objects.requireNonNull(id, "id");
            if (id.trim().isEmpty() || weight <= 0) { throw new IllegalArgumentException("Invalid weighted entry"); }
            this.weight = weight;
        }
        public String id() { return id; }
        public int weight() { return weight; }
        @Override public boolean equals(Object value)
        { return value instanceof Entry && id.equals(((Entry)value).id) && weight == ((Entry)value).weight; }
        @Override public int hashCode() { return Objects.hash(id, weight); }
    }

    private final String version;
    private final List<Entry> entries;
    private final int total;

    public WeightedPool(String version, List<Entry> entries)
    {
        this.version = Objects.requireNonNull(version, "version");
        this.entries = List.copyOf(entries);
        if (version.trim().isEmpty() || entries.isEmpty()) { throw new IllegalArgumentException("Empty pool/version"); }
        int sum = 0; Set<String> ids = new HashSet<>();
        for (Entry entry : this.entries)
        {
            if (!ids.add(entry.id())) { throw new IllegalArgumentException("Duplicate entry identity"); }
            sum = Math.addExact(sum, entry.weight());
        }
        total = sum;
    }

    public String version() { return version; }
    public List<Entry> entries() { return entries; }
    public int totalWeight() { return total; }

    public Entry atTicket(int ticket)
    {
        if (ticket < 0 || ticket >= total) { throw new IllegalArgumentException("Ticket outside pool"); }
        for (Entry entry : entries)
        {
            if (ticket < entry.weight()) { return entry; }
            ticket -= entry.weight();
        }
        throw new IllegalStateException("Invalid pool total");
    }

    @Override public boolean equals(Object value)
    { return value instanceof WeightedPool && version.equals(((WeightedPool)value).version) && entries.equals(((WeightedPool)value).entries); }
    @Override public int hashCode() { return Objects.hash(version, entries); }
}
