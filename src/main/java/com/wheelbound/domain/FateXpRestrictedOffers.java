package com.wheelbound.domain;

import java.util.List;
import java.util.Objects;

/**
 * Combines frozen reward and permission components in their original offer order.
 * Not complete offers: eligibility, identity, reviewed provenance and atomic durable
 * commitment are still required. No display admission or gameplay caller is enabled.
 */
public final class FateXpRestrictedOffers
{
    private final FateXpRewardOffers rewards;
    private final List<FateXpPermissionProfile> permissions;

    public FateXpRestrictedOffers(FateXpRewardOffers rewards,
        FateXpPermissionProfile first, FateXpPermissionProfile second)
    {
        this.rewards = Objects.requireNonNull(rewards, "rewards");
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        if (!rewards.targets().offers().get(0).objective().equals(first.objective())
            || !rewards.targets().offers().get(1).objective().equals(second.objective()))
        { throw new IllegalArgumentException("Permissions must match their frozen offer objectives"); }
        permissions = List.of(first, second);
    }

    public FateXpRewardOffers rewards() { return rewards; }
    public List<FateXpPermissionProfile> permissions() { return permissions; }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpRestrictedOffers)) { return false; }
        FateXpRestrictedOffers other = (FateXpRestrictedOffers) value;
        return rewards.equals(other.rewards) && permissions.equals(other.permissions);
    }

    @Override public int hashCode() { return Objects.hash(rewards, permissions); }
}
