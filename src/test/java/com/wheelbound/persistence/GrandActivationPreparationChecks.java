package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Aggregate preparation only: no file durability or trusted client admission claim. */
public final class GrandActivationPreparationChecks
{
    private static void check(boolean condition)
    { if (!condition) { throw new AssertionError("Incorrect prepared activation"); } }

    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid activation prepared");
    }

    public static void main(String[] args)
    {
        for (int ticket = 0; ticket < 5; ticket++)
        {
            RunIdentity id = new RunIdentity("preparation", UUID.randomUUID());
            SealedGrandFate seal = new SealedGrandFate(id, UUID.randomUUID(), GrandFatePool.atTicket(ticket));
            GrandFateChecklist initial = GrandFateChecklist.initial(seal);
            List<GrandFateProgress.Evidence> evidence = new ArrayList<>();
            List<GrandEvidenceReceipt> evidenceReceipts = new ArrayList<>();
            List<ActiveWheel.Slice> slices = new ArrayList<>();
            Map<String, SliceInventory.AcquisitionHistory> acquisitions = new HashMap<>();
            for (String activity : List.of("mining", "mining", "fishing", "fishing", "combat"))
            {
                slices.add(ActiveWheel.Slice.ordinary(UUID.randomUUID(), activity, ActiveWheel.Enhancement.SECOND));
                acquisitions.put(activity, new SliceInventory.AcquisitionHistory(3));
            }
            SliceInventory inventory = new SliceInventory(new ActiveWheel(slices), List.of(),
                SliceInventory.SatchelTier.THIRD, acquisitions.keySet(), acquisitions);
            for (GrandFateChecklist.Objective objective : initial.required())
            {
                // Exercise each retained completed prefix and both ordinary and maximum heads.
                for (long revision : new long[] { 20, Long.MAX_VALUE - 1 })
                {
                    GrandFateProgress progress = new GrandFateProgress(seal, revision, evidence);
                    FatePointLedger ledger = new FatePointLedger(-7, List.of());
                    RunReceipt previous = new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.RESUME,
                        revision - 1, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
                    RunDocument before = new RunDocument(new RunLifecycle(id, revision,
                        RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE), "frozen-catalog",
                        List.of(previous), Optional.of(ledger),
                        revision == 20 ? Optional.of(inventory) : Optional.empty(), Optional.of(seal),
                        Optional.of(progress), revision == 20 ? Optional.of(new GrandEvidenceJournal(
                            progress, revision, evidenceReceipts)) : Optional.empty(),
                        Optional.of(new GrandActivationHistory(seal, revision, List.of())));
                    GrandActivationReceipt request = new GrandActivationReceipt(UUID.randomUUID(),
                        id, revision, progress.checklist(), objective);
                    RunDocument after = GrandActivationAdmission.prepare(before, request, id, revision);
                    check(after.lifecycle().revision() == revision + 1
                        && after.lifecycle().obligation() == RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
                    check(after.receipts().size() == 2 && after.receipts().get(0).equals(previous)
                        && after.receipts().get(1).equals(RunReceipt.grandActivation(request)));
                    check(after.grandActivations().get().receipts().size() == 1
                        && after.grandActivations().get().receipts().get(request.commandId()).equals(request)
                        && after.grandCurrentAttempt().get().activation().equals(request));
                    check(after.catalogVersion().equals(before.catalogVersion())
                        && after.fatePoints().get() == ledger && after.inventory().equals(before.inventory())
                        && after.grandFate().equals(before.grandFate())
                        && after.grandProgress().get().evidence().equals(progress.evidence())
                        && after.grandJournal().isPresent() == before.grandJournal().isPresent());
                    if (before.grandJournal().isPresent())
                    { check(after.grandJournal().get().receipts().equals(before.grandJournal().get().receipts())); }
                    check(before.receipts().size() == 1 && before.grandCurrentAttempt().isEmpty()
                        && before.grandActivations().get().receipts().isEmpty());
                    check(GrandActivationAdmission.prepare(after, request, id, revision + 1) == after);
                    rejects(() -> GrandActivationAdmission.prepare(after, request, id, revision));
                    GrandActivationReceipt competing = new GrandActivationReceipt(UUID.randomUUID(),
                        id, revision, progress.checklist(), objective);
                    rejects(() -> GrandActivationAdmission.prepare(after, competing, id, revision + 1));
                    // A historical receipt never recreates explicit absent activity, even paused.
                    RunDocument later = new RunDocument(new RunLifecycle(id, Long.MAX_VALUE,
                        RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), before.catalogVersion(),
                        List.of(), before.fatePoints(), before.inventory(), before.grandFate(),
                        before.grandProgress(), before.grandJournal(), after.grandActivations());
                    check(GrandActivationAdmission.prepare(later, request, id, Long.MAX_VALUE) == later);
                }
                GrandEvidenceReceipt completed = new GrandEvidenceReceipt(seal, objective,
                    UUID.randomUUID(), evidence.size() * 2L + 1, UUID.randomUUID(), "fixture", objective.name());
                evidenceReceipts.add(completed);
                evidence.add(completed.reference());
            }
        }
    }
}
