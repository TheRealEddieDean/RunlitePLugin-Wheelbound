package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Dependency-free snapshot checks. All prices are fixtures, not reviewed quotes. */
public final class FateCardPurchaseAdmissionChecks
{
    public static void main(String[] args)
    {
        var id = new RunIdentity("card-admission", new UUID(192, 1));
        var seed = new RunLifecycle(id, 7, RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        var money = new FatePointLedger(100, List.of());
        var cards = FateCardOwnership.starting(id);
        var seal = new SealedGrandFate(id, new UUID(192, 2), GrandFatePool.atTicket(0));
        var initial = snapshot(seed, money, cards, List.of(), List.of(), Optional.of(seal));
        var head = initial;
        FateCardPurchaseAdmission.Result first = null;
        FateCardPurchaseProposal.Quote firstQuote = null;
        for (var pool : FateCardOwnership.Pool.values())
        for (var type : List.of(FateCardOwnership.Type.LESSER, FateCardOwnership.Type.GREATER))
        {
            var quote = new FateCardPurchaseProposal.Quote(pool, type, "fixture", "v1", 10);
            var before = head;
            var result = FateCardPurchaseAdmission.prepare(before, seed, money, cards, id,
                new UUID(193, before.lifecycle().revision()), before.lifecycle().revision(), quote);
            if (first == null) { first = result; firstQuote = quote; }
            head = result.proposed();
            check(!result.replay() && head != before);
            check(head.lifecycle().revision() == before.lifecycle().revision() + 1);
            check(head.fatePoints().get().balance() == before.fatePoints().get().balance() - 10);
            check(head.fateCards().get().owns(pool, type));
            check(head.cardPurchases().get().receipts().size() == before.cardPurchases().get().receipts().size() + 1);
            check(head.catalogVersion().equals(before.catalogVersion()) && head.receipts().equals(before.receipts()));
            check(head.inventory().equals(before.inventory()) && head.grandFate().equals(before.grandFate()));
            check(head.grandProgress().equals(before.grandProgress()) && head.grandJournal().equals(before.grandJournal()));
            check(head.grandActivations().equals(before.grandActivations())
                && head.grandCurrentAttempt().equals(before.grandCurrentAttempt()));
            check(head.pendingMasterSelection().equals(before.pendingMasterSelection())
                && head.masterCreations().equals(before.masterCreations())
                && head.masterResolutions().equals(before.masterResolutions())
                && head.currentMasterSpecial().equals(before.currentMasterSpecial()));
            check(head.masterSpins().get().allotted() == 12
                && head.masterSpins().get().revision() == head.lifecycle().revision()
                && head.masterSpins().get().reservation().isEmpty());
            head = LifecycleAdmission.prepare(head, id, new UUID(194, head.lifecycle().revision()),
                head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
            head = LifecycleAdmission.prepare(head, id, new UUID(194, head.lifecycle().revision()),
                head.lifecycle().revision(), RunReceipt.Operation.RESUME);
        }
        check(initial.lifecycle() == seed && money.entries().isEmpty());
        check(initial.cardPurchases().get().receipts().isEmpty() && initial.fateCards().get().equals(cards));
        var original = first.receipt();
        var request = firstQuote;
        // A plain debit advances both accounting and revision before replay.
        var debit = new FatePointDebits(head.lifecycle(), head.fatePoints().get()).debit(id,
            new UUID(195, 1), head.lifecycle().revision(), "other-fixture", "v1", 5);
        var d = debit.receipt();
        var events = new java.util.ArrayList<>(head.receipts());
        events.add(new RunReceipt(d.commandId(), RunReceipt.Operation.FP_DEBIT, d.beforeRevision(),
            d.afterRevision(), debit.proposed().lifecycle().state(), debit.proposed().lifecycle().obligation(), null, d));
        head = snapshot(debit.proposed().lifecycle(), debit.proposed().ledger(), head.fateCards().get(),
            List.copyOf(head.cardPurchases().get().receipts().values()), events, Optional.of(seal));
        head = LifecycleAdmission.prepare(head, id, new UUID(195, 2), head.lifecycle().revision(), RunReceipt.Operation.PAUSE);
        var current = head;
        var replay = FateCardPurchaseAdmission.prepare(current, seed, money, cards, id,
            original.debit().commandId(), original.debit().beforeRevision(), request);
        check(replay.replay() && replay.proposed() == current && replay.receipt() == original);
        check(replay.receipt().debit().balance() == 90 && current.fatePoints().get().balance() == 55);
        rejects(() -> FateCardPurchaseAdmission.prepare(current, seed, money, cards, id,
            original.debit().commandId(), 8, request));
        for (var changed : List.of(
            new FateCardPurchaseProposal.Quote(request.pool(), request.type(), "changed", "v1", 10),
            new FateCardPurchaseProposal.Quote(request.pool(), request.type(), "fixture", "v2", 10),
            new FateCardPurchaseProposal.Quote(request.pool(), request.type(), "fixture", "v1", 11),
            new FateCardPurchaseProposal.Quote(FateCardOwnership.Pool.BOSSING, request.type(), "fixture", "v1", 10),
            new FateCardPurchaseProposal.Quote(request.pool(), FateCardOwnership.Type.GREATER, "fixture", "v1", 10)))
        { rejects(() -> FateCardPurchaseAdmission.prepare(current, seed, money, cards, id,
            original.debit().commandId(), 7, changed)); }
        rejects(() -> FateCardPurchaseAdmission.prepare(current, seed, money, cards,
            new RunIdentity("foreign", id.runId()), original.debit().commandId(), 7, request));
        rejects(() -> FateCardPurchaseAdmission.prepare(initial, seed, money, cards, id, new UUID(196, 1), 6, request));
        rejects(() -> FateCardPurchaseAdmission.prepare(current, seed, money, cards, id, new UUID(196, 2),
            current.lifecycle().revision(), request));
        rejects(() -> FateCardPurchaseAdmission.prepare(initial, seed, new FatePointLedger(99, List.of()), cards,
            id, new UUID(196, 3), 7, request));
        rejects(() -> FateCardPurchaseAdmission.prepare(current, seed, new FatePointLedger(99, List.of()), cards,
            id, original.debit().commandId(), 7, request));
        // Reused generic command, duplicate ownership and insufficient funds fail independently.
        var resumed = LifecycleAdmission.prepare(current, id, new UUID(196, 4), current.lifecycle().revision(), RunReceipt.Operation.RESUME);
        rejects(() -> FateCardPurchaseAdmission.prepare(resumed, seed, money, cards, id,
            d.commandId(), resumed.lifecycle().revision(), request));
        rejects(() -> FateCardPurchaseAdmission.prepare(resumed, seed, money, cards, id,
            new UUID(196, 5), resumed.lifecycle().revision(), request));
        var poor = new FatePointLedger(9, List.of());
        var poorHead = snapshot(seed, poor, cards, List.of(), List.of(), Optional.empty());
        rejects(() -> FateCardPurchaseAdmission.prepare(poorHead, seed, poor, cards, id, new UUID(196, 6), 7, request));
        var unsupportedBase = MasterRetryContinuationChecks.fixture(0);
        var unsupported = FateCardAggregateChecks.attach(unsupportedBase,
            Optional.of(FateCardOwnership.starting(unsupportedBase.lifecycle().identity())),
            Optional.of(new FateCardPurchaseJournal(unsupportedBase.lifecycle().identity(),
                unsupportedBase.lifecycle().revision(), List.of())));
        rejects(() -> FateCardPurchaseAdmission.prepare(unsupported, unsupported.lifecycle(), money,
            FateCardOwnership.starting(unsupported.lifecycle().identity()), unsupported.lifecycle().identity(),
            new UUID(196, 7), unsupported.lifecycle().revision(), request));
        // Fresh admission after a plain debit preserves its envelope and accounting.
        var paid = new FatePointDebits(seed, money).debit(id, new UUID(196, 8), 7, "other", "v1", 5);
        var payment = paid.receipt();
        var debitHead = snapshot(paid.proposed().lifecycle(), paid.proposed().ledger(), cards, List.of(),
            List.of(new RunReceipt(payment.commandId(), RunReceipt.Operation.FP_DEBIT, 7, 8,
                RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE, null, payment)), Optional.empty());
        var afterDebit = FateCardPurchaseAdmission.prepare(debitHead, seed, money, cards, id,
            new UUID(196, 9), 8, request).proposed();
        check(afterDebit.receipts().equals(debitHead.receipts()) && afterDebit.fatePoints().get().balance() == 85
            && afterDebit.grandFate().isEmpty());
        // Legacy optional state must stay absent, including spin accounting.
        var legacy = new RunDocument(seed, "fixture", List.of(), Optional.of(money));
        var explicitCards = FateCardAggregateChecks.attach(legacy, Optional.of(cards),
            Optional.of(new FateCardPurchaseJournal(id, 7, List.of())));
        var minimal = FateCardPurchaseAdmission.prepare(explicitCards, seed, money, cards, id,
            new UUID(196, 10), 7, request).proposed();
        check(minimal.masterSpins().isEmpty() && minimal.inventory().isEmpty() && minimal.grandFate().isEmpty());
        rejects(() -> FateCardPurchaseAdmission.prepare(legacy, seed, money, cards, id, new UUID(196, 11), 7, request));
    }

    private static RunDocument snapshot(RunLifecycle lifecycle, FatePointLedger ledger,
        FateCardOwnership cards, List<FateCardPurchaseReceipt> purchases, List<RunReceipt> events,
        Optional<SealedGrandFate> seal)
    {
        return new RunDocument(lifecycle, "fixture-catalog", events, Optional.of(ledger),
            Optional.of(inventory()), seal, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(new MasterSpinState(
                MasterSpinState.VERSION, lifecycle.identity(), lifecycle.revision(), 12, Optional.empty())),
            Optional.empty(), Optional.of(cards), Optional.of(new FateCardPurchaseJournal(
                lifecycle.identity(), lifecycle.revision(), purchases)));
    }

    private static SliceInventory inventory()
    {
        var slices = new java.util.ArrayList<ActiveWheel.Slice>();
        var history = new java.util.HashMap<String, SliceInventory.AcquisitionHistory>();
        for (int i = 0; i < 5; i++)
        {
            slices.add(ActiveWheel.Slice.ordinary(new UUID(197, i), "activity-" + i, ActiveWheel.Enhancement.BASE));
            history.put("activity-" + i, new SliceInventory.AcquisitionHistory(0));
        }
        return new SliceInventory(new ActiveWheel(slices), List.of(), SliceInventory.SatchelTier.BASE,
            history.keySet(), history);
    }

    private static void check(boolean value) { if (!value) { throw new AssertionError(); } }
    private static void rejects(Runnable action)
    {
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException expected) { return; }
        throw new AssertionError("Expected refusal");
    }
}
