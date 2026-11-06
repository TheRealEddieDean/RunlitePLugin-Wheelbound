package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Structural fixtures do not establish supported live attribution or balance. */
public final class FateXpPermissionProfileChecks
{
    public static void main(String[] args)
    {
        FateXpObjective a = objective(10);
        FateXpObjective b = objective(20);
        FateXpPermissionProfile.ByProduct hp = rule("HITPOINTS", "assigned-action", "detector", "v1");
        List<FateXpPermissionProfile.ByProduct> input = new ArrayList<>(List.of(hp));
        FateXpPermissionProfile first = new FateXpPermissionProfile(a, "p1", input);
        input.clear();
        check(first.byProducts().equals(List.of(hp)) && first.objective().equals(a));
        FateXpPermissionProfile restored = new FateXpPermissionProfile(objective(10), "p1", List.of(
            rule("HITPOINTS", "assigned-action", "detector", "v1")));
        check(first.equals(restored) && restored.equals(first) && first.hashCode() == restored.hashCode());
        check(hp.skillKey().equals("HITPOINTS") && hp.contextKey().equals("assigned-action")
            && hp.detectorKey().equals("detector") && hp.detectorVersion().equals("v1"));
        check(!first.equals(null) && !first.equals("p1") && !hp.equals(null) && !hp.equals("HITPOINTS"));
        immutable(() -> first.byProducts().clear());
        check(!first.equals(new FateXpPermissionProfile(a, "p2", List.of(hp))));
        check(!first.equals(new FateXpPermissionProfile(b, "p1", List.of(hp))));
        check(new FateXpPermissionProfile(a, "p1", List.of()).byProducts().isEmpty());
        for (FateXpPermissionProfile.ByProduct changed : List.of(
            rule("MAGIC", "assigned-action", "detector", "v1"),
            rule("HITPOINTS", "other-action", "detector", "v1"),
            rule("HITPOINTS", "assigned-action", "other-detector", "v1"),
            rule("HITPOINTS", "assigned-action", "detector", "v2")))
        {
            check(!hp.equals(changed));
            check(!first.equals(new FateXpPermissionProfile(a, "p1", List.of(changed))));
        }
        FateXpPermissionProfile.ByProduct otherContext = rule("HITPOINTS", "other-action", "detector", "v1");
        FateXpPermissionProfile multiple = new FateXpPermissionProfile(a, "p1", List.of(hp, otherContext));
        check(!multiple.equals(new FateXpPermissionProfile(a, "p1", List.of(otherContext, hp))));
        invalid(() -> new FateXpPermissionProfile(a, "p1", List.of(hp, hp)));
        invalid(() -> new FateXpPermissionProfile(a, "p1", List.of(rule("ATTACK", "c", "d", "v"))));
        invalid(() -> new FateXpPermissionProfile(null, "p1", List.of()));
        invalid(() -> new FateXpPermissionProfile(a, "p1", null));
        invalid(() -> new FateXpPermissionProfile(a, "p1", Arrays.asList(hp, null)));
        for (String bad : new String[] { null, "", " ", " key", "key ", "\t" })
        {
            invalid(() -> new FateXpPermissionProfile(a, bad, List.of()));
            invalid(() -> rule(bad, "c", "d", "v"));
            invalid(() -> rule("HITPOINTS", bad, "d", "v"));
            invalid(() -> rule("HITPOINTS", "c", bad, "v"));
            invalid(() -> rule("HITPOINTS", "c", "d", bad));
        }
        FateXpStartingOffers targets = new FateXpStartingOffers(
            new FateXpStartingOffers.Offer(a, Long.MIN_VALUE), new FateXpStartingOffers.Offer(b, Long.MAX_VALUE));
        FateXpRewardOffers rewards = new FateXpRewardOffers(targets,
            new FateXpRewardOffers.Reward(a, "a", "v", 1), new FateXpRewardOffers.Reward(b, "b", "v", 2));
        FateXpPermissionProfile second = new FateXpPermissionProfile(b, "p1", List.of(hp));
        FateXpRestrictedOffers offers = new FateXpRestrictedOffers(rewards, first, second);
        FateXpRestrictedOffers copy = new FateXpRestrictedOffers(rewards, restored, second);
        check(offers.equals(copy) && copy.equals(offers) && offers.hashCode() == copy.hashCode());
        check(offers.rewards() == rewards && offers.permissions().equals(List.of(first, second)));
        check(offers.rewards().targets().offers().get(0).generationSeed() == Long.MIN_VALUE);
        immutable(() -> offers.permissions().clear());
        check(!offers.equals(null) && !offers.equals(rewards));
        check(!offers.equals(new FateXpRestrictedOffers(rewards,
            new FateXpPermissionProfile(a, "p2", List.of(hp)), second)));
        invalid(() -> new FateXpRestrictedOffers(rewards, second, first));
        invalid(() -> new FateXpRestrictedOffers(rewards, first, first));
        invalid(() -> new FateXpRestrictedOffers(null, first, second));
        invalid(() -> new FateXpRestrictedOffers(rewards, null, second));
        invalid(() -> new FateXpRestrictedOffers(rewards, first, null));
        // Same skill/target but a different objective context must not bind.
        for (FateXpObjective changed : List.of(
            FateXpObjective.skilling("ATTACK", 1, 0, 10, "c", "g"),
            FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 2, 0, 10, "c", "g"),
            FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 1, 10, "c", "g"),
            FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, 10, "c2", "g"),
            FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, 10, "c", "g2")))
        { invalid(() -> new FateXpRestrictedOffers(rewards,
            new FateXpPermissionProfile(changed, "p1", List.of(hp)), second)); }
        System.out.println("FateXpPermissionProfileChecks PASSED");
    }

    private static FateXpObjective objective(long xp)
    { return FateXpObjective.combat(FateXpObjective.CombatSkill.ATTACK, 1, 0, xp, "c", "g"); }
    private static FateXpPermissionProfile.ByProduct rule(String skill, String context, String detector, String version)
    { return new FateXpPermissionProfile.ByProduct(skill, context, detector, version); }
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Permission payload mismatch"); } }
    private static void invalid(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Invalid payload accepted");
    }
    private static void immutable(Runnable action)
    {
        try { action.run(); }
        catch (UnsupportedOperationException expected) { return; }
        throw new AssertionError("Mutable payload");
    }
}
