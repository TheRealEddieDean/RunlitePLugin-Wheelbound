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
        }
        // RunDocument currently retains only a contiguous generic-operation suffix.
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
        FateCardLifecycleHistory.validate(head, seed, money, cards);
        var pausedHead = LifecycleAdmission.prepare(head, id, new UUID(189, 1),
            head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
        FateCardLifecycleHistory.validate(pausedHead, seed, money, cards);
        FateCardLifecycleHistory.validate(LifecycleAdmission.prepare(pausedHead, id, new UUID(189, 2),
            pausedHead.lifecycle().revision(), RunReceipt.Operation.RESUME), seed, money, cards);
        check(head.lifecycle().revision() == 19 && ledger.balance() == 60);
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
        // Unsupported generic debit history cannot be treated as a lifecycle gap.
        var debit = new FatePointDebits(seed, money).debit(id, new UUID(187, 101), 7, "fixture", "v1", 10);
        var debitEvent = new RunReceipt(debit.receipt().commandId(), RunReceipt.Operation.FP_DEBIT,
            7, 8, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, debit.receipt());
        rejects(() -> FateCardLifecycleHistory.validate(head(debit.proposed().lifecycle(),
            debit.proposed().ledger(), cards, List.of(), List.of(debitEvent)), seed, money, cards));
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

    private static RunReceipt event(RunLifecycle before, RunLifecycle after, RunReceipt.Operation operation)
    { return new RunReceipt(new UUID(188, before.revision()), operation, before.revision(),
        after.revision(), after.state(), after.obligation()); }

    private static RunLifecycle active(RunIdentity id, long revision)
    { return new RunLifecycle(id, revision, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE); }

    private static RunDocument head(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership cards, List<FateCardPurchaseReceipt> purchases, List<RunReceipt> events)
    {
        return FateCardAggregateChecks.attach(new RunDocument(lifecycle, "fixture", events, Optional.of(ledger)),
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
