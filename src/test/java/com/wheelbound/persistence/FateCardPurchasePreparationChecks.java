package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Prices are test fixtures, not an adopted catalog. */
public final class FateCardPurchasePreparationChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-preparation", new UUID(184, 1));
        var lifecycle = new RunLifecycle(id, 4, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var ledger = new FatePointLedger(100, List.of());
        var ownership = FateCardOwnership.starting(id);
        var head = new RunDocument(lifecycle, "fixture", List.of(), Optional.of(ledger));
        var command = new UUID(184, 2);
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10);
            var proposal = FateCardPurchasePreparation.prepareFresh(head, ownership, id, command, 4, quote);
            check(proposal.ledger().balance() == 90 && proposal.lifecycle().revision() == 5);
            check(proposal.ownership().owns(pool, type));
            check(head.fatePoints().get() == ledger && ledger.balance() == 100
                && head.lifecycle() == lifecycle && head.receipts().isEmpty());
            rejects(() -> FateCardPurchasePreparation.prepareFresh(head, ownership, id, command, 3, quote));
            rejects(() -> FateCardPurchasePreparation.prepareFresh(new RunDocument(lifecycle, "fixture"),
                ownership, id, command, 4, quote));
            var foreign = new RunIdentity("foreign", id.runId());
            rejects(() -> FateCardPurchasePreparation.prepareFresh(head, ownership, foreign, command, 4, quote));
            var retained = new RunReceipt(command, RunReceipt.Operation.RESUME, 3, 4,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
            var collision = new RunDocument(lifecycle, "fixture", List.of(retained), Optional.of(ledger));
            rejects(() -> FateCardPurchasePreparation.prepareFresh(collision, ownership, id, command, 4, quote));
            var poor = new RunDocument(lifecycle, "fixture", List.of(),
                Optional.of(new FatePointLedger(9, List.of())));
            rejects(() -> FateCardPurchasePreparation.prepareFresh(poor, ownership, id, command, 4, quote));
            for (var state : List.of(RunLifecycle.State.PAUSED, RunLifecycle.State.ACTIVE))
            {
                var unavailable = new RunDocument(new RunLifecycle(id, 4, state,
                    RunLifecycle.Obligation.ACTIVE_OBJECTIVE), "fixture", List.of(), Optional.of(ledger));
                rejects(() -> FateCardPurchasePreparation.prepareFresh(unavailable, ownership, id, command, 4, quote));
            }
            for (int count : new int[] {0, 1, 513})
            {
                var fixture = MasterRetryContinuationChecks.fixture(count);
                // Supply funds so the pending-work guard is the only refusal reason.
                var pending = new RunDocument(fixture.lifecycle(), fixture.catalogVersion(), fixture.receipts(),
                    Optional.of(ledger), fixture.inventory(), fixture.grandFate(), fixture.grandProgress(),
                    fixture.grandJournal(), fixture.grandActivations(), fixture.grandCurrentAttempt(),
                    fixture.pendingMasterSelection(), fixture.masterCreations());
                check(pending.fatePoints().get().balance() == 100);
                rejects(() -> FateCardPurchasePreparation.prepareFresh(pending,
                    FateCardOwnership.starting(pending.lifecycle().identity()), pending.lifecycle().identity(),
                    command, pending.lifecycle().revision(), quote));
                // A creation retained without its generic envelope still occupies its ID.
                var journalOnly = new RunDocument(fixture.lifecycle(), fixture.catalogVersion(), List.of(),
                    Optional.of(ledger), fixture.inventory(), fixture.grandFate(), fixture.grandProgress(),
                    fixture.grandJournal(), fixture.grandActivations(), fixture.grandCurrentAttempt(),
                    Optional.empty(), fixture.masterCreations());
                var journalOwnership = FateCardOwnership.starting(journalOnly.lifecycle().identity());
                var occupied = fixture.pendingMasterSelection().get().transactionId();
                rejects(() -> FateCardPurchasePreparation.prepareFresh(journalOnly, journalOwnership,
                    journalOnly.lifecycle().identity(), occupied, journalOnly.lifecycle().revision(), quote));
                check(FateCardPurchasePreparation.prepareFresh(journalOnly, journalOwnership,
                    journalOnly.lifecycle().identity(), command, journalOnly.lifecycle().revision(), quote)
                    .ledger().balance() == 90);
            }
        }
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
