package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.*;

/** Dependency-free structural checks. Prices are fixtures, never a catalog. */
public final class FateCardAggregateChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-aggregate", new UUID(185, 1));
        var command = new UUID(185, 2);
        var start = FateCardOwnership.starting(id);
        var quote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.SKILLING,
            FateCardOwnership.Type.LESSER, "fixture", "v1", 10);
        var first = FateCardPurchaseProposal.propose(new RunLifecycle(id, 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            new FatePointLedger(100, List.of()), start, id, command, 0, quote);
        var journal = new FateCardPurchaseJournal(id, 4, List.of(first.receipt()));
        var legacy = new RunDocument(new RunLifecycle(id, 4, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "fixture", List.of(), Optional.of(first.ledger()));
        check(legacy.fateCards().isEmpty() && legacy.cardPurchases().isEmpty());
        rejects(() -> FateCardPurchasePreparation.prepareFresh(legacy, id, new UUID(185, 3), 4, quote));
        var head = attach(legacy, Optional.of(first.ownership()), Optional.of(journal));
        check(head.fateCards().get() == first.ownership());
        check(head.cardPurchases().get().receipts().get(command) == journal.receipts().get(command));
        var nextQuote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
            FateCardOwnership.Type.GREATER, "fixture", "v1", 10);
        var proposal = FateCardPurchasePreparation.prepareFresh(head, id, new UUID(185, 3), 4, nextQuote);
        check(proposal.ledger().balance() == 80 && proposal.lifecycle().revision() == 5);
        rejects(() -> FateCardPurchasePreparation.prepareFresh(head, id, command, 4, nextQuote));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(head, start, id, new UUID(185, 3), 4, quote));
        rejects(() -> FateCardPurchasePreparation.prepareFresh(head, first.ownership(),
            new FateCardPurchaseJournal(id, 4, List.of()), id, new UUID(185, 3), 4, nextQuote));
        // The separate-journal overload accepts the exact aggregate journal.
        check(FateCardPurchasePreparation.prepareFresh(head, first.ownership(), journal,
            id, new UUID(185, 3), 4, nextQuote).ledger().balance() == 80);
        rejects(() -> MasterRetryRecovery.requireUnused(head, command));
        rejects(() -> LifecycleAdmission.prepare(head, id, command, 4, RunReceipt.Operation.PAUSE));
        var paused = LifecycleAdmission.prepare(head, id, new UUID(185, 4), 4, RunReceipt.Operation.PAUSE);
        retained(head, paused);
        check(LifecycleAdmission.prepare(paused, id, new UUID(185, 4), 4,
            RunReceipt.Operation.PAUSE) == paused);
        var resumed = LifecycleAdmission.prepare(paused, id, new UUID(185, 5), 5, RunReceipt.Operation.RESUME);
        retained(head, resumed);
        check(head.lifecycle().revision() == 4 && head.receipts().isEmpty());
        rejects(() -> attach(legacy, Optional.empty(), Optional.of(journal)));
        var foreignId = new RunIdentity("foreign", id.runId());
        rejects(() -> attach(legacy, Optional.of(FateCardOwnership.starting(foreignId)), Optional.empty()));
        rejects(() -> attach(legacy, Optional.of(start), Optional.of(
            new FateCardPurchaseJournal(foreignId, 4, List.of()))));
        var earlier = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), "fixture");
        rejects(() -> attach(earlier, Optional.of(first.ownership()), Optional.of(journal)));
        var occupied = new RunDocument(legacy.lifecycle(), "fixture", List.of(new RunReceipt(command,
            RunReceipt.Operation.RESUME, 3, 4, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE)));
        rejects(() -> attach(occupied, Optional.of(first.ownership()), Optional.of(journal)));
        var seal = new SealedGrandFate(id, new UUID(185, 6), GrandFatePool.atTicket(0));
        var grand = new RunDocument(legacy.lifecycle(), "fixture", List.of(), legacy.fatePoints(),
            Optional.empty(), Optional.of(seal), Optional.empty(), Optional.empty(),
            Optional.of(new GrandActivationHistory(seal, 4, List.of())));
        var grandHead = attach(grand, head.fateCards(), head.cardPurchases());
        var checklist = GrandFateChecklist.initial(seal);
        var objective = checklist.required().iterator().next();
        var activation = new GrandActivationReceipt(new UUID(185, 7), id, 4, checklist, objective);
        retained(grandHead, GrandActivationAdmission.prepare(grandHead, activation, id, 4));
        rejects(() -> GrandActivationAdmission.prepare(grandHead,
            new GrandActivationReceipt(command, id, 4, checklist, objective), id, 4));
        for (int count : new int[] {0, 1, 513})
        {
            retryRetention(count);
            var special = MasterSpecialTerminalPreparationChecks.fixture(ActiveWheel.Kind.TAINT, count, 7);
            var specialId = special.lifecycle().identity();
            var specialHead = attach(special, Optional.of(FateCardOwnership.starting(specialId)),
                Optional.of(new FateCardPurchaseJournal(specialId, 5, List.of())));
            var pending = specialHead.pendingMasterSelection().get();
            var terminal = MasterSelectionResolutionReceipt.prepare(new UUID(185, 200 + count), 5,
                specialHead.masterCreations().get().receipts().get(pending.transactionId()), pending,
                new MasterSelectionProvenance.Step(new DrawCommitment(pending.pool(),
                    pending.pool().totalWeight() - 1), "terminal"));
            retained(specialHead, MasterTaintTerminalOperation.prepare(specialHead, terminal));
        }
    }

    private static void retryRetention(int count)
    {
        var original = MasterRetryContinuationChecks.fixture(count);
        var id = original.lifecycle().identity();
        var cards = FateCardOwnership.starting(id);
        var journal = new FateCardPurchaseJournal(id, original.lifecycle().revision(), List.of());
        var head = attach(original, Optional.of(cards), Optional.of(journal));
        var request = MasterRetryOperationProposal.prepare(head, new UUID(185, 100 + count),
            MasterRetryOperationProposalChecks.result(head,
                MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "retry"));
        var next = MasterRetryOperation.prepare(head, request);
        retained(head, next);
        check(MasterRetryRecovery.find(next, request).get() == request);
        // A card command cannot overlap even domain-only creation history.
        var quote = new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.SKILLING,
            FateCardOwnership.Type.LESSER, "fixture", "v1", 1);
        var purchase = FateCardPurchaseProposal.propose(new RunLifecycle(id, 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE),
            new FatePointLedger(10, List.of()), cards, id,
            original.pendingMasterSelection().get().transactionId(), 0, quote);
        var overlap = new FateCardPurchaseJournal(id, head.lifecycle().revision(), List.of(purchase.receipt()));
        rejects(() -> attach(head, Optional.of(purchase.ownership()), Optional.of(overlap)));
        var domainOnly = new RunDocument(head.lifecycle(), head.catalogVersion(), List.of(),
            head.fatePoints(), head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(),
            head.grandActivations(), head.grandCurrentAttempt(), Optional.empty(), head.masterCreations());
        rejects(() -> attach(domainOnly, Optional.of(purchase.ownership()), Optional.of(overlap)));
        // Fresh creation also retains explicit (empty) card history.
        var creation = original.masterCreations().get().receipts().values().iterator().next();
        var before = new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.NONE), original.catalogVersion(), List.of(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.of(new MasterSelectionCreationJournal(id, 0, List.of())));
        var withCards = attach(before, Optional.of(cards), Optional.of(new FateCardPurchaseJournal(id, 0, List.of())));
        retained(withCards, MasterCreationAdmission.prepare(withCards, creation));
    }

    static RunDocument attach(RunDocument head, Optional<FateCardOwnership> cards,
        Optional<FateCardPurchaseJournal> journal)
    {
        return new RunDocument(head.lifecycle(), head.catalogVersion(), head.receipts(), head.fatePoints(),
            head.inventory(), head.grandFate(), head.grandProgress(), head.grandJournal(), head.grandActivations(),
            head.grandCurrentAttempt(), head.pendingMasterSelection(), head.masterCreations(), head.masterResolutions(),
            head.masterSpins(), head.currentMasterSpecial().map(value -> value.obligation()), cards, journal);
    }

    private static void retained(RunDocument before, RunDocument after)
    {
        check(after.fateCards().equals(before.fateCards()));
        check(after.cardPurchases().get().receipts().equals(before.cardPurchases().get().receipts()));
    }
    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
