package com.wheelbound.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Frozen supplied XP permission payload (WB-D087/098). No observation is authorized
 * by this value alone. The caller must authenticate the reviewed detector mapping,
 * active assignment and positive source/action attribution. Unavailable or ambiguous
 * attribution remains unknown; neither a skill match nor an equipped tool proves it.
 * This class supplies no default exemptions, tolerance, method restrictions or audit.
 */
public final class FateXpPermissionProfile
{
    private final FateXpObjective objective;
    private final String version;
    private final List<ByProduct> byProducts;

    public FateXpPermissionProfile(FateXpObjective objective, String version, List<ByProduct> byProducts)
    {
        this.objective = Objects.requireNonNull(objective, "objective");
        this.version = key(version);
        this.byProducts = List.copyOf(Objects.requireNonNull(byProducts, "byProducts"));
        Set<ByProduct> unique = new HashSet<>();
        for (ByProduct rule : this.byProducts)
        {
            if (rule.skillKey().equals(objective.skillKey()) || !unique.add(rule))
            { throw new IllegalArgumentException("Primary XP or duplicate by-product rule"); }
        }
    }

    private static String key(String value)
    {
        Objects.requireNonNull(value, "permission key/version");
        if (value.isBlank() || !value.equals(value.strip()))
        { throw new IllegalArgumentException("Noncanonical permission key/version"); }
        return value;
    }

    public FateXpObjective objective() { return objective; }
    public String version() { return version; }
    public List<ByProduct> byProducts() { return byProducts; }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpPermissionProfile)) { return false; }
        FateXpPermissionProfile other = (FateXpPermissionProfile) value;
        return objective.equals(other.objective) && version.equals(other.version)
            && byProducts.equals(other.byProducts);
    }

    @Override public int hashCode() { return Objects.hash(objective, version, byProducts); }

    /**
     * One inherent by-product under a specific supplied action/source context and
     * detector revision. These identifiers retain requirements, not evidence of them.
     * Multiple contexts for one skill are permitted; none grants blanket training.
     */
    public static final class ByProduct
    {
        private final String skillKey;
        private final String contextKey;
        private final String detectorKey;
        private final String detectorVersion;

        public ByProduct(String skillKey, String contextKey, String detectorKey, String detectorVersion)
        {
            this.skillKey = key(skillKey);
            this.contextKey = key(contextKey);
            this.detectorKey = key(detectorKey);
            this.detectorVersion = key(detectorVersion);
        }

        public String skillKey() { return skillKey; }
        public String contextKey() { return contextKey; }
        public String detectorKey() { return detectorKey; }
        public String detectorVersion() { return detectorVersion; }

        @Override public boolean equals(Object value)
        {
            if (!(value instanceof ByProduct)) { return false; }
            ByProduct other = (ByProduct) value;
            return skillKey.equals(other.skillKey) && contextKey.equals(other.contextKey)
                && detectorKey.equals(other.detectorKey) && detectorVersion.equals(other.detectorVersion);
        }

        @Override public int hashCode()
        { return Objects.hash(skillKey, contextKey, detectorKey, detectorVersion); }
    }
}
