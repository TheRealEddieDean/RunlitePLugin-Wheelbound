package com.wheelbound.domain;

import java.util.List;
import java.util.UUID;

/** Offline origin checks establish no live eligibility or commitment authority. */
public final class FateXpOfferSetChecks
{
    public static void main(String[] args)
    {
        UUID run = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID assignment = UUID.fromString("00000000-0000-0000-0000-000000000002");
        RunIdentity identity = new RunIdentity("account", run);
        FateXpOfferSet original = new FateXpOfferSet(identity, assignment, 0, offers(7));
        FateXpOfferSet restored = new FateXpOfferSet(new RunIdentity("account", run), assignment, 0, offers(7));
        check(original.equals(restored) && restored.equals(original)
            && original.hashCode() == restored.hashCode());
        check(original.identity().equals(identity) && original.assignmentId().equals(assignment)
            && original.creationRevision() == 0 && original.offers().equals(offers(7)));
        original.requireOrigin(restored.identity(), assignment, 0);
        check(!original.equals(null) && !original.equals(identity));
        for (RunIdentity foreign : List.of(new RunIdentity("other-account", run),
            new RunIdentity("account", assignment)))
        {
            invalid(() -> original.requireOrigin(foreign, assignment, 0));
            check(!original.equals(new FateXpOfferSet(foreign, assignment, 0, offers(7))));
        }
        invalid(() -> original.requireOrigin(identity, run, 0));
        invalid(() -> original.requireOrigin(identity, assignment, 1));
        invalid(() -> original.requireOrigin(identity, assignment, -1));
        invalid(() -> original.requireOrigin(null, assignment, 0));
        invalid(() -> original.requireOrigin(identity, null, 0));
        check(!original.equals(new FateXpOfferSet(identity, run, 0, offers(7))));
        check(!original.equals(new FateXpOfferSet(identity, assignment, 1, offers(7))));
        check(!original.equals(new FateXpOfferSet(identity, assignment, 0, offers(8))));
        FateXpOfferSet maximum = new FateXpOfferSet(identity, assignment, Long.MAX_VALUE, offers(7));
        maximum.requireOrigin(identity, assignment, Long.MAX_VALUE);
        invalid(() -> new FateXpOfferSet(null, assignment, 0, offers(7)));
        invalid(() -> new FateXpOfferSet(identity, null, 0, offers(7)));
        invalid(() -> new FateXpOfferSet(identity, assignment, -1, offers(7)));
        invalid(() -> new FateXpOfferSet(identity, assignment, 0, null));
        // Origin verification must preserve both generation streams and all nested payloads.
        check(original.offers().rewards().targets().offers().get(0).generationSeed() == 7);
        check(original.offers().rewards().targets().offers().get(1).generationSeed() == -7);
        check(original.equals(restored));
    }

    private static FateXpRestrictedOffers offers(long seed)
    {
        FateXpObjective a = FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, 10, "catalog", "generator");
        FateXpObjective b = FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, 20, "catalog", "generator");
        FateXpStartingOffers targets = new FateXpStartingOffers(
            new FateXpStartingOffers.Offer(a, seed), new FateXpStartingOffers.Offer(b, -seed));
        return new FateXpRestrictedOffers(new FateXpRewardOffers(targets,
            new FateXpRewardOffers.Reward(a, "quote-a", "v1", 1),
            new FateXpRewardOffers.Reward(b, "quote-b", "v1", 2)),
            new FateXpPermissionProfile(a, "p1", List.of()),
            new FateXpPermissionProfile(b, "p1", List.of()));
    }

    private static void check(boolean valid) { if (!valid) { throw new AssertionError(); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid origin accepted");
    }
}
