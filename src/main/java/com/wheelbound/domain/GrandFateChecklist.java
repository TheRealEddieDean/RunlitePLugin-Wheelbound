package com.wheelbound.domain;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * WB-D104/117 objective shape and retained progress, bound to the original seal.
 * This value neither detects evidence nor authorizes attempts or completion awards.
 * Restore requires already validated progress from a trusted durable boundary; client
 * observations, item names and CA points must never be passed as verified progress.
 */
public final class GrandFateChecklist
{
    public enum Objective
    {
        INFERNAL_CAPE_COMPLETION,
        DIZANAS_QUIVER_COMPLETION,
        RADIANT_OATHPLATE_FULL_SET,
        AWAKENED_LEVIATHAN,
        AWAKENED_DUKE_SUCELLUS,
        AWAKENED_VARDORVIS,
        AWAKENED_WHISPERER,
        GHOMMALS_HILT_5
    }

    private final SealedGrandFate seal;
    private final Set<Objective> required;
    private final Set<Objective> completed;
    private final Set<Objective> unfinished;

    private GrandFateChecklist(SealedGrandFate seal, Set<Objective> completed)
    {
        this.seal = Objects.requireNonNull(seal, "seal");
        EnumSet<Objective> goals = objectives(seal.goal());
        EnumSet<Objective> retained = EnumSet.noneOf(Objective.class);
        for (Objective objective : Objects.requireNonNull(completed, "completed"))
        {
            if (!goals.contains(Objects.requireNonNull(objective, "objective")))
            { throw new IllegalArgumentException("Objective does not belong to sealed Grand Fate"); }
            retained.add(objective);
        }
        EnumSet<Objective> remaining = EnumSet.copyOf(goals);
        remaining.removeAll(retained);
        this.required = Collections.unmodifiableSet(goals);
        this.completed = Collections.unmodifiableSet(retained);
        this.unfinished = Collections.unmodifiableSet(remaining);
    }

    public static GrandFateChecklist initial(SealedGrandFate seal)
    { return new GrandFateChecklist(seal, Collections.emptySet()); }

    /** Restores validated progress only; does not infer missing or legacy evidence. */
    public static GrandFateChecklist restore(SealedGrandFate seal, Set<Objective> completed)
    { return new GrandFateChecklist(seal, completed); }

    public SealedGrandFate seal() { return seal; }
    public Set<Objective> required() { return required; }
    public Set<Objective> completed() { return completed; }
    public Set<Objective> unfinished() { return unfinished; }

    /** Checklist predicate only, never a run win/award decision. */
    public boolean isComplete() { return unfinished.isEmpty(); }

    /** Does not reserve an objective or replace the lifecycle/durable revision gate. */
    public boolean isUnfinished(Objective objective)
    { return objective != null && unfinished.contains(objective); }

    private static EnumSet<Objective> objectives(GrandFatePool.Goal goal)
    {
        switch (goal)
        {
            case INFERNO: return EnumSet.of(Objective.INFERNAL_CAPE_COMPLETION);
            case FORTIS_COLOSSEUM: return EnumSet.of(Objective.DIZANAS_QUIVER_COMPLETION);
            case RADIANT_OATHPLATE: return EnumSet.of(Objective.RADIANT_OATHPLATE_FULL_SET);
            case AWAKENED_DT2: return EnumSet.of(Objective.AWAKENED_LEVIATHAN,
                Objective.AWAKENED_DUKE_SUCELLUS, Objective.AWAKENED_VARDORVIS,
                Objective.AWAKENED_WHISPERER);
            case MASTER_HILT_5: return EnumSet.of(Objective.GHOMMALS_HILT_5);
            default: throw new IllegalArgumentException("Unsupported Grand Fate goal");
        }
    }
}
