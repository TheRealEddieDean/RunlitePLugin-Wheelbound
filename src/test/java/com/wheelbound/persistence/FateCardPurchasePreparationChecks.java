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
        journalAdmission(head, ownership, id, command);
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

    private static void journalAdmission(RunDocument original, FateCardOwnership starting,
        RunIdentity id, UUID command)
    {
        var firstQuote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.SKILLING,
            FateCardOwnership.Type.LESSER, "fixture", "v1", 10);
        var nextQuote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.GREATER, "fixture", "v1", 10);
        var first = FateCardPurchasePreparation.prepareFresh(original, starting, id, command, 4, firstQuote);
        var journal = new FateCardPurchaseJournal(id, 8, List.of(first.receipt()));
        // Intervening non-card revisions are allowed; the journal is not a contiguous history.
        var lifecycle = new RunLifecycle(id, 8, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var head = new RunDocument(lifecycle, "fixture", List.of(), Optional.of(first.ledger()));
        var fresh = new UUID(184, 99);
        var result = FateCardPurchasePreparation.prepareFresh(head, first.ownership(), journal,
            id, fresh, 8, nextQuote);
        check(result.lifecycle().revision() == 9 && result.ledger().balance() == 80);
        check(result.ownership().owns(firstQuote.pool(), firstQuote.type())
            && result.ownership().owns(nextQuote.pool(), nextQuote.type()));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(head, first.ownership(), journal,
            id, command, 8, nextQuote));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(original, starting, journal,
            id, fresh, 4, nextQuote));
        var foreign = new FateCardPurchaseJournal(new RunIdentity("foreign", id.runId()), 8, List.of());
        rejects(() -> FateCardPurchasePreparation.prepareFresh(head, first.ownership(), foreign,
            id, fresh, 8, nextQuote));
        var envelope = new RunReceipt(command, RunReceipt.Operation.RESUME, 7, 8,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var overlap = new RunDocument(lifecycle, "fixture", List.of(envelope), Optional.of(first.ledger()));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(overlap, first.ownership(), journal,
            id, fresh, 8, nextQuote));
        // Domain-only commands must collide even when the fresh command itself is unused.
        var fixture = MasterRetryContinuationChecks.fixture(0);
        var occupied = fixture.pendingMasterSelection().get().transactionId();
        var domainHead = new RunDocument(fixture.lifecycle(), fixture.catalogVersion(), List.of(),
            Optional.of(new FatePointLedger(100, List.of())), fixture.inventory(), fixture.grandFate(),
            fixture.grandProgress(), fixture.grandJournal(), fixture.grandActivations(),
            fixture.grandCurrentAttempt(), Optional.empty(), fixture.masterCreations());
        var domainId = domainHead.lifecycle().identity();
        var receipt = new FateCardPurchaseReceipt(domainId, firstQuote, new FatePointDebits.Receipt(
            occupied, 0, 1, "fixture", "v1", 10, 90));
        var domainJournal = new FateCardPurchaseJournal(domainId, domainHead.lifecycle().revision(),
            List.of(receipt));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(domainHead,
            FateCardOwnership.starting(domainId), domainJournal, domainId, fresh,
            domainHead.lifecycle().revision(), nextQuote));
        check(FateCardPurchasePreparation.prepareFresh(head, first.ownership(),
            new FateCardPurchaseJournal(id, 8, List.of()), id, fresh, 8, nextQuote)
            .ledger().balance() == 80);
        check(head.lifecycle() == lifecycle && head.fatePoints().get() == first.ledger()
            && head.receipts().isEmpty() && journal.receipts().size() == 1
            && journal.receipts().get(command).debit() == first.debit());
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
