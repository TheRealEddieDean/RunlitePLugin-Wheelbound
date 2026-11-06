package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class FateXpOfferEligibilityChecks
{
    public static void main(String[] args)
    {
        FateXpOfferSet offers = offers("account", 0, 7);
        FateXpOfferEligibility.Requirement access = requirement("access", FateXpOfferEligibility.Status.QUALIFIED);
        FateXpOfferEligibility.Requirement support = requirement("support", FateXpOfferEligibility.Status.QUALIFIED);
        List<FateXpOfferEligibility.Requirement> supplied = new ArrayList<>(List.of(access, support));
        FateXpOfferEligibility snapshot = new FateXpOfferEligibility(offers, "eligibility-v1", supplied, List.of(access));
        supplied.clear();
        snapshot.requireQualified(offers("account", 0, 7));
        check(snapshot.offers() == offers && snapshot.requirements().get(0).equals(List.of(access, support)));
        check(snapshot.catalogVersion().equals("eligibility-v1"));
        FateXpOfferEligibility restored = new FateXpOfferEligibility(offers("account", 0, 7),
            "eligibility-v1", List.of(access, support), List.of(access));
        check(snapshot.equals(restored) && restored.equals(snapshot) && snapshot.hashCode() == restored.hashCode());
        check(!snapshot.equals(null) && !snapshot.equals(offers));
        invalid(() -> snapshot.requirements().clear());
        invalid(() -> snapshot.requirements().get(0).clear());
        for (FateXpOfferEligibility.Status status : FateXpOfferEligibility.Status.values())
        {
            FateXpOfferEligibility.Requirement observation = requirement("access", status);
            check(observation.status() == status && observation.key().equals("access"));
            check(observation.equals(requirement("access", status)));
            for (boolean first : List.of(true, false))
            {
                FateXpOfferEligibility candidate = new FateXpOfferEligibility(offers, "eligibility-v1",
                    List.of(first ? observation : access), List.of(first ? access : observation));
                if (status == FateXpOfferEligibility.Status.QUALIFIED) { candidate.requireQualified(offers); }
                else { invalid(() -> candidate.requireQualified(offers)); }
                check(candidate.requirements().get(first ? 0 : 1).get(0).status() == status);
            }
        }
        for (FateXpOfferSet foreign : List.of(offers("other", 0, 7), offers("account", 1, 7), offers("account", 0, 8),
            new FateXpOfferSet(offers.identity(), UUID.randomUUID(), 0, offers.offers()),
            new FateXpOfferSet(new RunIdentity("account", UUID.randomUUID()), offers.assignmentId(), 0, offers.offers())))
        { invalid(() -> snapshot.requireQualified(foreign)); }
        FateXpRewardOffers rewards = offers.offers().rewards();
        FateXpObjective a = rewards.targets().offers().get(0).objective();
        FateXpObjective b = rewards.targets().offers().get(1).objective();
        FateXpRewardOffers repriced = new FateXpRewardOffers(rewards.targets(),
            new FateXpRewardOffers.Reward(a, "a", "v2", 99), rewards.rewards().get(1));
        for (FateXpRestrictedOffers changed : List.of(
            new FateXpRestrictedOffers(repriced, offers.offers().permissions().get(0), offers.offers().permissions().get(1)),
            new FateXpRestrictedOffers(rewards, new FateXpPermissionProfile(a, "v2", List.of()),
                new FateXpPermissionProfile(b, "v1", List.of()))))
        {
            invalid(() -> snapshot.requireQualified(new FateXpOfferSet(offers.identity(), offers.assignmentId(), 0, changed)));
        }
        invalid(() -> snapshot.requireQualified(null));
        invalid(() -> new FateXpOfferEligibility(null, "v1", List.of(access), List.of(access)));
        invalid(() -> new FateXpOfferEligibility(offers, null, List.of(access), List.of(access)));
        invalid(() -> new FateXpOfferEligibility(offers, "v1", null, List.of(access)));
        invalid(() -> new FateXpOfferEligibility(offers, "v1", List.of(access), List.of()));
        invalid(() -> new FateXpOfferEligibility(offers, "v1", List.of(), List.of(access)));
        invalid(() -> new FateXpOfferEligibility(offers, "v1", java.util.Arrays.asList(access, null), List.of(access)));
        for (FateXpOfferEligibility.Status status : FateXpOfferEligibility.Status.values())
        { invalid(() -> new FateXpOfferEligibility(offers, "v1", List.of(access, requirement("access", status)), List.of(access))); }
        for (String key : List.of("", " ", " leading", "trailing ", "\t"))
        {
            invalid(() -> requirement(key, FateXpOfferEligibility.Status.UNKNOWN));
            invalid(() -> new FateXpOfferEligibility(offers, key, List.of(access), List.of(access)));
        }
        invalid(() -> requirement(null, FateXpOfferEligibility.Status.UNKNOWN));
        invalid(() -> requirement("access", null));
        check(!snapshot.equals(new FateXpOfferEligibility(offers, "v2", List.of(access, support), List.of(access))));
        check(!snapshot.equals(new FateXpOfferEligibility(offers, "eligibility-v1", List.of(support, access), List.of(access))));
        snapshot.requireQualified(offers);
    }

    private static FateXpOfferEligibility.Requirement requirement(String key, FateXpOfferEligibility.Status status)
    { return new FateXpOfferEligibility.Requirement(key, status); }

    private static FateXpOfferSet offers(String account, long revision, long seed)
    {
        FateXpObjective a = FateXpObjective.skilling("MINING", 1, 0, 10, "catalog", "generator");
        FateXpObjective b = FateXpObjective.skilling("MINING", 1, 0, 20, "catalog", "generator");
        FateXpStartingOffers targets = new FateXpStartingOffers(
            new FateXpStartingOffers.Offer(a, seed), new FateXpStartingOffers.Offer(b, -seed));
        FateXpRestrictedOffers restricted = new FateXpRestrictedOffers(new FateXpRewardOffers(targets,
            new FateXpRewardOffers.Reward(a, "a", "v1", 1), new FateXpRewardOffers.Reward(b, "b", "v1", 2)),
            new FateXpPermissionProfile(a, "v1", List.of()), new FateXpPermissionProfile(b, "v1", List.of()));
        return new FateXpOfferSet(new RunIdentity(account, new UUID(0, 1)), new UUID(0, 2), revision, restricted);
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException | UnsupportedOperationException expected) { return; }
        throw new AssertionError("Invalid eligibility accepted");
    }
}
