package com.wheelbound.domain;

import java.util.Objects;

/**
 * Explicit chosen slot in a retained starting Standard XP pair (FATE_CARDS).
 * Retains both offers and their supplied eligibility observations for restoration.
 * This structural selection is not a command, a complete assignment, or authority
 * to admit evidence. Reviewed restrictions, catalog completeness/authentication,
 * lifecycle/revision checks and atomic aggregate commitment remain caller duties.
 * No objective, seed, reward or permission is regenerated at selection time.
 */
public final class FateXpOfferSelection
{
    public enum Slot
    {
        FIRST(0), SECOND(1);

        private final int index;
        Slot(int index) { this.index = index; }
    }

    private final FateXpOfferEligibility eligibility;
    private final Slot slot;

    public FateXpOfferSelection(FateXpOfferEligibility eligibility, Slot slot)
    {
        this.eligibility = Objects.requireNonNull(eligibility, "eligibility");
        this.slot = Objects.requireNonNull(slot, "slot");
        // Both offers must have qualified supplied observations before display.
        eligibility.requireQualified(eligibility.offers());
    }

    public FateXpOfferEligibility eligibility() { return eligibility; }
    public Slot slot() { return slot; }
    public FateXpStartingOffers.Offer target()
    { return eligibility.offers().offers().rewards().targets().offers().get(slot.index); }
    public FateXpRewardOffers.Reward reward()
    { return eligibility.offers().offers().rewards().rewards().get(slot.index); }
    public FateXpPermissionProfile permissions()
    { return eligibility.offers().offers().permissions().get(slot.index); }

    /** Compare the entire trusted retained selection; never replace its chosen slot. */
    public void requireSameSelection(FateXpOfferSelection expected)
    {
        if (!equals(Objects.requireNonNull(expected, "expected")))
        { throw new IllegalArgumentException("Changed frozen XP selection"); }
    }

    @Override public boolean equals(Object value)
    {
        if (!(value instanceof FateXpOfferSelection)) { return false; }
        FateXpOfferSelection other = (FateXpOfferSelection) value;
        return eligibility.equals(other.eligibility) && slot == other.slot;
    }

    @Override public int hashCode() { return Objects.hash(eligibility, slot); }
}
