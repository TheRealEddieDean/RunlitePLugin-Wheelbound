package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Frozen supplied eligibility observations for a complete starting XP offer origin.
 * FATE_CARDS requires eligibility before display; unknown support is not qualification.
 * This value cannot authenticate observations or establish that the supplied requirements
 * are complete. A reviewed catalog and supported client adapter must do that before
 * durable admission. It is neither a complete assignment nor a display authorization.
 * Failed observations remain representable without replacing an already retained offer.
 */
public final class FateXpOfferEligibility
{
    public enum Status { QUALIFIED, INELIGIBLE, UNKNOWN, UNSUPPORTED }

    /** Requirement keys are scoped to the enclosing eligibility catalog version. */
    public static final class Requirement
    {
        private final String key;
        private final Status status;

        public Requirement(String key, Status status)
        {
            this.key = canonical(key);
            this.status = Objects.requireNonNull(status, "status");
        }

        public String key() { return key; }
        public Status status() { return status; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof Requirement)) { return false; }
            Requirement other = (Requirement) value;
            return key.equals(other.key) && status == other.status;
        }

        @Override public int hashCode() { return Objects.hash(key, status); }
    }

    private final FateXpOfferSet offers;
    private final String catalogVersion;
    private final List<List<Requirement>> requirements;

    public FateXpOfferEligibility(FateXpOfferSet offers, String catalogVersion,
        List<Requirement> first, List<Requirement> second)
    {
        this.offers = Objects.requireNonNull(offers, "offers");
        this.catalogVersion = canonical(catalogVersion);
        requirements = List.of(copy(first), copy(second));
    }

    private static List<Requirement> copy(List<Requirement> supplied)
    {
        List<Requirement> result = List.copyOf(supplied);
        if (result.isEmpty()) { throw new IllegalArgumentException("Missing eligibility requirements"); }
        Set<String> keys = new HashSet<>();
        for (Requirement requirement : result)
        {
            if (!keys.add(requirement.key()))
            { throw new IllegalArgumentException("Duplicate eligibility requirement"); }
        }
        return result;
    }

    private static String canonical(String value)
    {
        Objects.requireNonNull(value, "key/version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical eligibility key/version"); }
        return value;
    }

    public FateXpOfferSet offers() { return offers; }
    public String catalogVersion() { return catalogVersion; }
    public List<List<Requirement>> requirements() { return requirements; }

    /** Only checks supplied observations; does not prove completeness or provenance. */
    public void requireQualified(FateXpOfferSet expectedOffers)
    {
        if (!offers.equals(Objects.requireNonNull(expectedOffers, "expectedOffers")))
        { throw new IllegalArgumentException("Eligibility belongs to another offer set"); }
        for (List<Requirement> offer : requirements)
        {
            for (Requirement requirement : offer)
            {
                if (requirement.status() != Status.QUALIFIED)
                { throw new IllegalStateException("Unqualified eligibility requirement: " + requirement.key()); }
            }
        }
    }

    /**
     * Checks exact coverage against a caller-resolved catalog manifest for each slot.
     * Expected keys are sets: observation order need not match catalog order. Empty,
     * duplicate or malformed manifests are refused rather than implying qualification.
     * The caller must authenticate the catalog/version and resolve the full applicable
     * requirements for these offers; this method cannot establish that authority.
     * It grants neither display permission nor current-assignment evidence authority.
     */
    public void requireQualifiedAgainst(FateXpOfferSet expectedOffers, String expectedCatalogVersion,
        List<String> firstRequiredKeys, List<String> secondRequiredKeys)
    {
        if (!catalogVersion.equals(canonical(expectedCatalogVersion)))
        { throw new IllegalArgumentException("Eligibility catalog version mismatch"); }
        List<Set<String>> expected = List.of(manifest(firstRequiredKeys), manifest(secondRequiredKeys));
        for (int slot = 0; slot < requirements.size(); slot++)
        {
            Set<String> observed = new HashSet<>();
            for (Requirement requirement : requirements.get(slot)) { observed.add(requirement.key()); }
            if (!observed.equals(expected.get(slot)))
            { throw new IllegalArgumentException("Eligibility requirement coverage mismatch"); }
        }
        requireQualified(expectedOffers);
    }

    private static Set<String> manifest(List<String> supplied)
    {
        Set<String> keys = new HashSet<>();
        for (String key : List.copyOf(supplied))
        {
            if (!keys.add(canonical(key)))
            { throw new IllegalArgumentException("Duplicate catalog requirement"); }
        }
        if (keys.isEmpty()) { throw new IllegalArgumentException("Missing catalog requirements"); }
        return keys;
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpOfferEligibility)) { return false; }
        FateXpOfferEligibility other = (FateXpOfferEligibility) value;
        return offers.equals(other.offers) && catalogVersion.equals(other.catalogVersion)
            && requirements.equals(other.requirements);
    }

    @Override public int hashCode() { return Objects.hash(offers, catalogVersion, requirements); }
}
