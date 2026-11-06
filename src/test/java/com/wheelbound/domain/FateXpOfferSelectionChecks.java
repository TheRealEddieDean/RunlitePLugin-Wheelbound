package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;

/** Dependency-free restoration and stale-selection checks; also run by JUnit. */
public final class FateXpOfferSelectionChecks
{
    public static void main(String[] args)
    {
        FateXpOfferEligibility original = fixture("account", 1, 7, "quote", "permission", "eligibility");
        FateXpOfferSelection first = new FateXpOfferSelection(original, FateXpOfferSelection.Slot.FIRST);
        FateXpOfferSelection second = new FateXpOfferSelection(original, FateXpOfferSelection.Slot.SECOND);
        check(first.target().objective().requiredXp() == 10);
        check(second.target().objective().requiredXp() == 20);
        check(first.target().generationSeed() == 7 && second.target().generationSeed() == -7);
        check(first.reward().fatePoints() == 1 && second.reward().fatePoints() == 2);
        check(first.permissions().objective().equals(first.target().objective()));
        check(second.permissions().objective().equals(second.target().objective()));
        FateXpOfferSelection restored = new FateXpOfferSelection(
            fixture("account", 1, 7, "quote", "permission", "eligibility"), FateXpOfferSelection.Slot.FIRST);
        first.requireSameSelection(restored);
        check(first.equals(restored) && first.hashCode() == restored.hashCode());
        invalid(() -> first.requireSameSelection(second));
        invalid(() -> first.requireSameSelection(null));
        // Changes to unselected content also invalidate the retained selection.
        for (FateXpOfferEligibility changed : List.of(
            fixture("foreign", 1, 7, "quote", "permission", "eligibility"),
            fixture("account", 2, 7, "quote", "permission", "eligibility"),
            fixture("account", 1, 8, "quote", "permission", "eligibility"),
            fixture("account", 1, 7, "repriced", "permission", "eligibility"),
            fixture("account", 1, 7, "quote", "changed", "eligibility"),
            fixture("account", 1, 7, "quote", "permission", "changed")))
        {
            invalid(() -> first.requireSameSelection(new FateXpOfferSelection(changed,
                FateXpOfferSelection.Slot.FIRST)));
        }
        FateXpOfferSet origin = original.offers();
        for (FateXpOfferSet foreign : List.of(
            new FateXpOfferSet(new RunIdentity("account", new UUID(0, 99)), origin.assignmentId(),
                origin.creationRevision(), origin.offers()),
            new FateXpOfferSet(origin.identity(), new UUID(0, 99), origin.creationRevision(), origin.offers())))
        {
            FateXpOfferEligibility changed = new FateXpOfferEligibility(foreign, original.catalogVersion(),
                original.requirements().get(0), original.requirements().get(1));
            invalid(() -> first.requireSameSelection(new FateXpOfferSelection(changed,
                FateXpOfferSelection.Slot.FIRST)));
        }
        FateXpOfferEligibility changedRequirements = new FateXpOfferEligibility(origin, original.catalogVersion(),
            original.requirements().get(0), List.of(new FateXpOfferEligibility.Requirement(
                "different-access", FateXpOfferEligibility.Status.QUALIFIED)));
        invalid(() -> first.requireSameSelection(new FateXpOfferSelection(changedRequirements,
            FateXpOfferSelection.Slot.FIRST)));
        for (FateXpOfferEligibility.Status status : FateXpOfferEligibility.Status.values())
        {
            if (status == FateXpOfferEligibility.Status.QUALIFIED) { continue; }
            for (boolean selected : List.of(true, false))
            {
                List<FateXpOfferEligibility.Requirement> failed = List.of(
                    new FateXpOfferEligibility.Requirement("access", status));
                FateXpOfferEligibility unqualified = new FateXpOfferEligibility(original.offers(), "eligibility",
                    selected ? failed : original.requirements().get(0),
                    selected ? original.requirements().get(1) : failed);
                invalid(() -> new FateXpOfferSelection(unqualified, FateXpOfferSelection.Slot.FIRST));
            }
        }
        invalid(() -> new FateXpOfferSelection(null, FateXpOfferSelection.Slot.FIRST));
        invalid(() -> new FateXpOfferSelection(original, null));
        invalid(() -> first.eligibility().requirements().clear());
        invalid(() -> first.eligibility().offers().offers().rewards().targets().offers().clear());
        first.requireSameSelection(restored);
        check(!first.equals(null) && !first.equals("FIRST"));
    }

    private static FateXpOfferEligibility fixture(String account, long revision, long seed,
        String secondQuote, String secondPermission, String eligibilityVersion)
    {
        FateXpObjective a = FateXpObjective.skilling("MINING", 1, 0, 10, "catalog", "generator");
        FateXpObjective b = FateXpObjective.skilling("MINING", 1, 0, 20, "catalog", "generator");
        FateXpStartingOffers pair = new FateXpStartingOffers(
            new FateXpStartingOffers.Offer(a, seed), new FateXpStartingOffers.Offer(b, -seed));
        FateXpRestrictedOffers restricted = new FateXpRestrictedOffers(new FateXpRewardOffers(pair,
            new FateXpRewardOffers.Reward(a, "first", "v1", 1),
            new FateXpRewardOffers.Reward(b, secondQuote, "v1", 2)),
            new FateXpPermissionProfile(a, "permission", List.of()),
            new FateXpPermissionProfile(b, secondPermission, List.of()));
        FateXpOfferSet offers = new FateXpOfferSet(new RunIdentity(account, new UUID(0, 1)),
            new UUID(0, 2), revision, restricted);
        List<FateXpOfferEligibility.Requirement> qualified = List.of(new FateXpOfferEligibility.Requirement(
            "access", FateXpOfferEligibility.Status.QUALIFIED));
        return new FateXpOfferEligibility(offers, eligibilityVersion, qualified, qualified);
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException | NullPointerException | UnsupportedOperationException expected)
        { return; }
        throw new AssertionError("Invalid selection accepted");
    }
}
