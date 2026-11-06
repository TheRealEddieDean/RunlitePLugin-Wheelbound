package com.wheelbound.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Offline supplied-value fixtures; no live catalog, RNG or persistence claims. */
public final class QuestFateProvenanceChecks
{
    public static void main(String[] args)
    {
        UUID questSlice = new UUID(0, 2);
        MasterSelectionResolutionReceipt resolution = resolution(false, true);
        QuestFateReward reward = reward("q1", "v1");
        QuestFateProvenance value = make(resolution, questSlice, reward, "rng-v1:seed-7", "0", "3");
        QuestFateProvenance restored = make(resolution, questSlice, reward("q1", "v1"), "rng-v1:seed-7", "0", "3");
        check(value.equals(restored) && restored.equals(value) && value.hashCode() == restored.hashCode());
        value.requireSameProvenance(restored);
        MasterSelectionProvenance original = resolution.resolved();
        List<MasterSelectionProvenance.Step> steps = new ArrayList<>();
        original.landings().forEach(landing -> steps.add(landing.step()));
        steps.set(0, new MasterSelectionProvenance.Step(steps.get(0).draw(), "changed-retry-state"));
        MasterSelectionProvenance changedPrefix = new MasterSelectionProvenance(original.transactionId(),
            original.snapshotVersion(), original.catalogVersion(), original.streamDescriptor(),
            original.initialStreamState(), original.readiness(), steps);
        MasterSelectionResolutionReceipt changedResolution = new MasterSelectionResolutionReceipt(
            resolution.commandId(), resolution.beforeRevision(), resolution.creation(), changedPrefix);
        // Same transaction/command/slice cannot hide a changed empty-retry prefix.
        rejects(() -> value.requireAllocation(MasterSpinAllocation.restore(changedResolution, 5)));
        rejects(() -> value.requireAllocation(MasterSpinAllocation.restore(resolution(false, false), 5)));
        rejects(() -> value.requireAllocation(MasterSpinAllocation.restore(resolution(true, false), 5)));
        rejects(() -> value.requireAllocation(newAllocationWithCommand(resolution)));
        rejects(() -> value.requireAllocation(MasterSpinAllocation.restore(
            new MasterSelectionResolutionReceipt(resolution.commandId(), resolution.beforeRevision() + 1,
                resolution.creation(), resolution.resolved()), 5)));
        MasterSelectionCreationReceipt foreignAccount = new MasterSelectionCreationReceipt(
            new RunIdentity("other-account", resolution.identity().runId()),
            resolution.creation().beforeRevision(), resolution.creation().initial());
        rejects(() -> value.requireAllocation(MasterSpinAllocation.restore(
            new MasterSelectionResolutionReceipt(resolution.commandId(), resolution.beforeRevision(),
                foreignAccount, resolution.resolved()), 5)));
        rejects(() -> value.requireAllocation(null));
        rejects(() -> value.requireSameProvenance(make(changedResolution, questSlice, reward,
            "rng-v1:seed-7", "0", "3")));
        MasterSelectionProvenance restoredTranscript = new MasterSelectionProvenance(original.transactionId(),
            original.snapshotVersion(), original.catalogVersion(), original.streamDescriptor(),
            original.initialStreamState(), original.readiness(), original.landings().stream()
                .map(MasterSelectionProvenance.Landing::step).collect(java.util.stream.Collectors.toList()));
        MasterSelectionResolutionReceipt restoredResolution = new MasterSelectionResolutionReceipt(
            resolution.commandId(), resolution.beforeRevision(), resolution.creation(), restoredTranscript);
        for (long spins : new long[] {1, 5, Long.MAX_VALUE})
        {
            MasterSpinAllocation allocation = MasterSpinAllocation.restore(restoredResolution, spins);
            value.requireAllocation(allocation);
            value.requireAllocation(allocation);
            check(allocation.spinsBefore() == spins && allocation.spinsAfter() == spins - 1);
            check(allocation.resolution().equals(resolution));
        }
        value.requireSameProvenance(make(new MasterSelectionResolutionReceipt(resolution.commandId(),
            resolution.beforeRevision(), resolution.creation(), restoredTranscript), questSlice,
            reward("q1", "v1"), "rng-v1:seed-7", "0", "3"));
        check(value.resolution() == resolution && value.reward() == reward);
        check(value.sliceId().equals(questSlice) && value.activityKey().equals("questing"));
        check(value.streamDescriptor().equals("rng-v1:seed-7"));
        check(value.initialStreamState().equals("0") && value.finalStreamState().equals("3"));
        for (QuestFateProvenance changed : List.of(
            make(resolution, questSlice, reward("q2", "v1"), "rng-v1:seed-7", "0", "3"),
            make(resolution, questSlice, reward("q1", "v2"), "rng-v1:seed-7", "0", "3"),
            make(resolution, questSlice, reward, "rng-v2:seed-7", "0", "3"),
            make(resolution, questSlice, reward, "rng-v1:seed-8", "0", "3"),
            make(resolution, questSlice, reward, "rng-v1:seed-7", "1", "3"),
            make(resolution, questSlice, reward, "rng-v1:seed-7", "0", "4"),
            make(resolution(false, false), questSlice, reward, "rng-v1:seed-7", "0", "3")))
        { rejects(() -> value.requireSameProvenance(changed)); }
        rejects(() -> make(resolution, new UUID(0, 1), reward, "rng", "0", "3"));
        rejects(() -> make(resolution(true, false), new UUID(0, 6), reward, "rng", "0", "3"));
        rejects(() -> new QuestFateProvenance(resolution, questSlice, "combat", "rng", "0", "3", reward));
        rejects(() -> make(null, questSlice, reward, "rng", "0", "3"));
        rejects(() -> make(resolution, null, reward, "rng", "0", "3"));
        rejects(() -> make(resolution, questSlice, null, "rng", "0", "3"));
        for (String bad : new String[] {null, "", " ", " rng", "rng ", "\n"})
        {
            rejects(() -> make(resolution, questSlice, reward, bad, "0", "3"));
            rejects(() -> make(resolution, questSlice, reward, "rng", bad, "3"));
            rejects(() -> make(resolution, questSlice, reward, "rng", "0", bad));
            rejects(() -> new QuestFateProvenance(resolution, questSlice, bad, "rng", "0", "3", reward));
        }
        rejects(() -> value.requireSameProvenance(null));
        check(!value.equals(null) && !value.equals(reward));
        // Opaque states may repeat; no invented RNG advancement rule.
        make(resolution, questSlice, reward, "rng", "same", "same");
    }

    private static QuestFateProvenance make(MasterSelectionResolutionReceipt resolution,
        UUID slice, QuestFateReward reward, String descriptor, String before, String after)
    { return new QuestFateProvenance(resolution, slice, "questing", descriptor, before, after, reward); }

    private static MasterSpinAllocation newAllocationWithCommand(MasterSelectionResolutionReceipt resolution)
    {
        return MasterSpinAllocation.restore(new MasterSelectionResolutionReceipt(UUID.randomUUID(),
            resolution.beforeRevision(), resolution.creation(), resolution.resolved()), 5);
    }

    static QuestFateReward reward(String selected, String quoteVersion)
    {
        QuestFateOffers offers = new QuestFateOffers(QuestFateOffers.Capacity.THREE,
            "quests-v1", List.of("q1", "q2", "q3"));
        List<QuestFateEligibility.Observation> observations = new ArrayList<>();
        for (String id : offers.questIds())
        {
            observations.add(new QuestFateEligibility.Observation(id, "route-" + id, "v1",
                QuestFateEligibility.Status.QUALIFIED, QuestFateEligibility.Status.QUALIFIED,
                QuestFateEligibility.Status.QUALIFIED));
        }
        return new QuestFateReward(new QuestFateSelection(
            new QuestFateEligibility(offers, observations), offers.select(selected)), "quote", quoteVersion, 17);
    }

    static MasterSelectionResolutionReceipt resolution(boolean special, boolean retry)
    {
        List<ActiveWheel.Slice> slices = new ArrayList<>();
        Map<UUID, MasterSelectionReadiness.Classification> statuses = new HashMap<>();
        String[] activities = {"combat", "questing", "mining", "combat", "mining"};
        for (int i = 0; i < activities.length; i++)
        {
            UUID id = new UUID(0, i + 1);
            slices.add(ActiveWheel.Slice.ordinary(id, activities[i], ActiveWheel.Enhancement.BASE));
            statuses.put(id, new MasterSelectionReadiness.Classification(i == 0
                ? MasterSelectionReadiness.Availability.VERIFIED_EMPTY
                : MasterSelectionReadiness.Availability.ASSIGNABLE, "supplied"));
        }
        if (special)
        {
            UUID id = new UUID(0, 6);
            slices.add(ActiveWheel.Slice.special(id, ActiveWheel.Kind.TAINT, 4));
            statuses.put(id, new MasterSelectionReadiness.Classification(
                MasterSelectionReadiness.Availability.ASSIGNABLE, "special"));
        }
        MasterSelectionProvenance zero = new MasterSelectionProvenance(UUID.randomUUID(),
            "snapshot", "master-catalog", "master-rng", "0",
            new MasterSelectionReadiness(new ActiveWheel(slices), statuses), List.of());
        MasterSelectionProvenance resolved = zero;
        if (retry)
        { resolved = resolved.append(zero.transactionId(), 0, "0",
            new MasterSelectionProvenance.Step(new DrawCommitment(zero.pool(), 0), "1")); }
        resolved = resolved.append(zero.transactionId(), resolved.drawCount(), resolved.currentStreamState(),
            new MasterSelectionProvenance.Step(new DrawCommitment(zero.pool(), special ? 20 : 4), "terminal"));
        return new MasterSelectionResolutionReceipt(UUID.randomUUID(), 2,
            new MasterSelectionCreationReceipt(new RunIdentity("account", UUID.randomUUID()), 0, zero), resolved);
    }

    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError(); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | NullPointerException expected) { return; }
        throw new AssertionError("Expected changed/invalid provenance rejection");
    }
}
