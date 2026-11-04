package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Dependency-free reconstruction checks; prices are fixtures, not adopted quotes. */
public final class FateCardLifecycleHistoryChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-lifecycle", new UUID(187, 1));
        var seed = active(id, 7);
        var money = new FatePointLedger(100, List.of());
        var cards = FateCardOwnership.starting(id);
        var ledger = money;
        var ownership = cards;
        var lifecycle = seed;
        var purchases = new ArrayList<FateCardPurchaseReceipt>();
        var events = new ArrayList<RunReceipt>();
        // Covers all four paid pool/type pairs and derived original prefixes.
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var proposal = FateCardPurchaseProposal.propose(lifecycle, ledger, ownership, id,
                new UUID(187, lifecycle.revision()), lifecycle.revision(),
                new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10));
            purchases.add(proposal.receipt());
            ledger = proposal.ledger();
            ownership = proposal.ownership();
            lifecycle = proposal.lifecycle();
            var pause = lifecycle.pause(id, lifecycle.revision());
            events.add(event(lifecycle, pause, RunReceipt.Operation.PAUSE));
            var resume = pause.resume(id, pause.revision());
            events.add(event(pause, resume, RunReceipt.Operation.RESUME));
            lifecycle = resume;
        }
        // Retain a lifecycle suffix after the interleaved purchases.
        for (int index = 0; index < 4; index++)
        {
            var paused = lifecycle.pause(id, lifecycle.revision());
            events.add(event(lifecycle, paused, RunReceipt.Operation.PAUSE));
            lifecycle = paused;
            var resumed = lifecycle.resume(id, lifecycle.revision());
            events.add(event(lifecycle, resumed, RunReceipt.Operation.RESUME));
            lifecycle = resumed;
        }
        var head = head(lifecycle, ledger, ownership, purchases, events);
        // A card may itself be the head after earlier generic lifecycle events.
        var firstResult = purchases.get(0).reconstruct(seed, money, cards);
        var secondSeed = firstResult.lifecycle().pause(id, 8).resume(id, 9);
        var secondResult = purchases.get(1).reconstruct(secondSeed, firstResult.ledger(), firstResult.ownership());
        var cardHead = head(secondResult.lifecycle(), secondResult.ledger(), secondResult.ownership(),
            purchases.subList(0, 2), events.subList(0, 2));
        FateCardLifecycleHistory.validate(cardHead, seed, money, cards);
        // Generic ordering and overlapping revisions remain structural failures.
        var reordered = new ArrayList<>(events);
        java.util.Collections.swap(reordered, 0, 1);
        rejects(() -> head(head.lifecycle(), head.fatePoints().get(), head.fateCards().get(), purchases, reordered));
        var overlap = new ArrayList<>(events);
        overlap.add(0, new RunReceipt(new UUID(190, 1), RunReceipt.Operation.RESUME, 7, 8,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
        rejects(() -> head(head.lifecycle(), head.fatePoints().get(), head.fateCards().get(), purchases, overlap));
        FateCardLifecycleHistory.validate(head, seed, money, cards);
        var pausedHead = LifecycleAdmission.prepare(head, id, new UUID(189, 1),
            head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
        FateCardLifecycleHistory.validate(pausedHead, seed, money, cards);
        FateCardLifecycleHistory.validate(LifecycleAdmission.prepare(pausedHead, id, new UUID(189, 2),
            pausedHead.lifecycle().revision(), RunReceipt.Operation.RESUME), seed, money, cards);
        check(head.lifecycle().revision() == 27 && ledger.balance() == 60);
        check(money.entries().isEmpty() && cards.equals(FateCardOwnership.starting(id)));
        // Missing leading, internal and trailing operations must not become trusted gaps.
        for (int index : new int[] {0, 3, events.size() - 1})
        {
            var missing = new ArrayList<>(events);
            missing.remove(index);
            rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
                head.fateCards().get(), purchases, missing), seed, money, cards));
        }
        rejects(() -> FateCardLifecycleHistory.validate(head, active(id, 8), money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head, active(id, 20), money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head,
            active(new RunIdentity("foreign", id.runId()), 7), money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head, seed, money, head.fateCards().get()));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            cards, purchases, events), seed, money, cards));
        var missingPurchase = new ArrayList<>(purchases);
        missingPurchase.remove(1);
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            head.fateCards().get(), missingPurchase, events), seed, money, cards));
        var corruptPurchases = new ArrayList<>(purchases);
        var first = purchases.get(0);
        corruptPurchases.set(0, new FateCardPurchaseReceipt(id, first.quote(),
            new FatePointDebits.Receipt(first.debit().commandId(), 7, 8, "fixture", "v1", 10, 89)));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            head.fateCards().get(), corruptPurchases, events), seed, money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head, new RunLifecycle(id, 7,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), money, cards));
        // The complete ledger matters even when the final balance matches.
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(),
            new FatePointLedger(60, List.of()), head.fateCards().get(), purchases, events), seed, money, cards));
        var rewritten = new ArrayList<>(head.fatePoints().get().entries());
        rewritten.set(0, new FatePointLedger.Entry(FatePointLedger.Kind.PENALTY, 10));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(),
            new FatePointLedger(100, rewritten), head.fateCards().get(), purchases, events), seed, money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get().append(
            new FatePointLedger.Entry(FatePointLedger.Kind.EARNING, 1)), head.fateCards().get(),
            purchases, events), seed, money, cards));
        // A forged obligation and an idempotent Pause cannot explain a committed revision.
        var wrong = new ArrayList<>(events);
        var original = wrong.get(0);
        wrong.set(0, new RunReceipt(original.commandId(), RunReceipt.Operation.PAUSE,
            original.beforeRevision(), original.afterRevision(), RunLifecycle.State.PAUSED,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            head.fateCards().get(), purchases, wrong), seed, money, cards));
        var pausedSeed = seed.pause(id, 7);
        var doublePause = new RunReceipt(new UUID(187, 100), RunReceipt.Operation.PAUSE, 8, 9,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE);
        rejects(() -> FateCardLifecycleHistory.validate(head(new RunLifecycle(id, 9,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), money, cards,
            List.of(), List.of(doublePause)), pausedSeed, money, cards));
        // Empty segment still checks accounting, ownership and exact lifecycle.
        FateCardLifecycleHistory.validate(head(seed, money, cards, List.of(), List.of()), seed, money, cards);
        rejects(() -> FateCardLifecycleHistory.validate(head(active(id, Long.MAX_VALUE), money, cards,
            List.of(), List.of()), seed, money, cards));
        rejects(() -> FateCardLifecycleHistory.validate(head(new RunLifecycle(id, 7,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), money, cards,
            List.of(), List.of()), seed, money, cards));
        // A plain debit is reconstructed from the original seed, not its recorded balance.
        var debit = new FatePointDebits(seed, money).debit(id, new UUID(187, 101), 7, "fixture", "v1", 10);
        var debitEvent = new RunReceipt(debit.receipt().commandId(), RunReceipt.Operation.FP_DEBIT,
            7, 8, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, debit.receipt());
        FateCardLifecycleHistory.validate(head(debit.proposed().lifecycle(),
            debit.proposed().ledger(), cards, List.of(), List.of(debitEvent)), seed, money, cards);
        checkDebitSegments(id, seed, money, cards);
        var legacy = new RunDocument(seed, "fixture", List.of(), Optional.of(money));
        rejects(() -> FateCardLifecycleHistory.validate(legacy, seed, money, cards));
        // Domain-only Master history is refused even without generic envelopes.
        var unsupported = MasterRetryContinuationChecks.fixture(0);
        var unsupportedId = unsupported.lifecycle().identity();
        var unsupportedCards = FateCardOwnership.starting(unsupportedId);
        var attached = FateCardAggregateChecks.attach(unsupported, Optional.of(unsupportedCards),
            Optional.of(new FateCardPurchaseJournal(unsupportedId, unsupported.lifecycle().revision(), List.of())));
        rejects(() -> FateCardLifecycleHistory.validate(attached, active(unsupportedId, 0), money, unsupportedCards));
    }

    private static void checkDebitSegments(RunIdentity id, RunLifecycle seed,
        FatePointLedger money, FateCardOwnership cards)
    {
        var first = new FatePointDebits(seed, money).debit(id, new UUID(191, 1), 7, "fixture", "v1", 10);
        var purchase = FateCardPurchaseProposal.propose(first.proposed().lifecycle(),
            first.proposed().ledger(), cards, id, new UUID(191, 2), 8,
            new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING,
                FateCardOwnership.Type.LESSER, "fixture", "v1", 10));
        var pause = purchase.lifecycle().pause(id, 9);
        var resume = pause.resume(id, 10);
        var last = new FatePointDebits(resume, purchase.ledger()).debit(id,
            new UUID(191, 3), 11, "other-fixture", "v2", 15);
        var events = List.of(debitEvent(first), event(purchase.lifecycle(), pause, RunReceipt.Operation.PAUSE),
            event(pause, resume, RunReceipt.Operation.RESUME), debitEvent(last));
        var purchases = List.of(purchase.receipt());
        var head = head(last.proposed().lifecycle(), last.proposed().ledger(), purchase.ownership(), purchases, events);
        FateCardLifecycleHistory.validate(head, seed, money, cards);
        check(head.fatePoints().get().balance() == 65 && money.entries().isEmpty());
        // Debit arithmetic, original receipt balance, seed funds and active state all matter.
        rejects(() -> FateCardLifecycleHistory.validate(head, seed,
            new FatePointLedger(9, List.of()), cards));
        rejects(() -> FateCardLifecycleHistory.validate(head, new RunLifecycle(id, 7,
            RunLifecycle.State.PAUSED, RunLifecycle.Obligation.NONE), money, cards));
        var corrupt = new ArrayList<>(events);
        var recorded = last.receipt();
        var bad = new FatePointDebits.Receipt(recorded.commandId(), 11, 12,
            recorded.quoteKey(), recorded.quoteVersion(), 15, 64);
        corrupt.set(3, new RunReceipt(bad.commandId(), RunReceipt.Operation.FP_DEBIT,
            11, 12, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, bad));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            purchase.ownership(), purchases, corrupt), seed, money, cards));
        // Missing debit and equal-balance ledger rewrites cannot explain the segment.
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(), head.fatePoints().get(),
            purchase.ownership(), purchases, events.subList(1, events.size())), seed, money, cards));
        var rewritten = new ArrayList<>(head.fatePoints().get().entries());
        rewritten.set(0, new FatePointLedger.Entry(FatePointLedger.Kind.PENALTY, 10));
        rejects(() -> FateCardLifecycleHistory.validate(head(head.lifecycle(),
            new FatePointLedger(100, rewritten), purchase.ownership(), purchases, events), seed, money, cards));
        // Other generic operation families remain unsupported even with exact revision coverage.
        var draw = new RunReceipt(new UUID(191, 4), RunReceipt.Operation.DRAW,
            12, 13, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.OFFERS_COMMITTED,
            new DrawCommitment(new WeightedPool("fixture", List.of(new WeightedPool.Entry("target", 1))), 0));
        var appended = new ArrayList<>(events);
        appended.add(draw);
        var drawHead = head(new RunLifecycle(id, 13, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.OFFERS_COMMITTED), head.fatePoints().get(), purchase.ownership(), purchases, appended);
        rejects(() -> FateCardLifecycleHistory.validate(drawHead, seed, money, cards));
    }

    private static RunReceipt debitEvent(FatePointDebits.Result result)
    {
        var receipt = result.receipt();
        return new RunReceipt(receipt.commandId(), RunReceipt.Operation.FP_DEBIT,
            receipt.beforeRevision(), receipt.afterRevision(), result.proposed().lifecycle().state(),
            result.proposed().lifecycle().obligation(), null, receipt);
    }

    private static RunReceipt event(RunLifecycle before, RunLifecycle after, RunReceipt.Operation operation)
    { return new RunReceipt(new UUID(188, before.revision()), operation, before.revision(),
        after.revision(), after.state(), after.obligation()); }

    private static RunLifecycle active(RunIdentity id, long revision)
    { return new RunLifecycle(id, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); }

    private static RunDocument head(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership cards, List<FateCardPurchaseReceipt> purchases, List<RunReceipt> events)
    {
        return new RunDocument(lifecycle, "fixture", events, Optional.of(ledger),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.of(cards), Optional.of(new FateCardPurchaseJournal(lifecycle.identity(),
                lifecycle.revision(), purchases)));
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
