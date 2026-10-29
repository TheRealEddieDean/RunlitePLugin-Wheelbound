package com.wheelbound.persistence;

import com.wheelbound.domain.FatePointDebits;
import com.wheelbound.domain.FatePointLedger;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Dependency-free checks also invoked by the JUnit suite. */
public final class DebitJournalChecks
{
    private static final RunIdentity ID = new RunIdentity("journal-test", UUID.randomUUID());
    private static final UUID COMMAND = UUID.randomUUID();
    private static final RunLifecycle.Obligation NONE = RunLifecycle.Obligation.NONE;

    private static RunReceipt debit(long amount, long balance)
    {
        return new RunReceipt(COMMAND, RunReceipt.Operation.FP_DEBIT, 2, 3,
            RunLifecycle.State.ACTIVE, NONE, null,
            new FatePointDebits.Receipt(COMMAND, 2, 3, "test-only", "v1", amount, balance));
    }

    public static RunDocument mixed()
    {
        return document(List.of(
            new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 0, 1, RunLifecycle.State.PAUSED, NONE),
            new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.RESUME, 1, 2, RunLifecycle.State.ACTIVE, NONE),
            debit(7, 13)), Optional.of(new FatePointLedger(20, List.of(
                new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, 7)))));
    }

    private static RunDocument document(List<RunReceipt> receipts, Optional<FatePointLedger> fp)
    {
        return new RunDocument(new RunLifecycle(ID, 3, RunLifecycle.State.ACTIVE, NONE), "v1", receipts, fp);
    }

    private static void rejects(Runnable action)
    {
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid journal accepted");
    }

    public static void main(String[] args)
    {
        RunDocument valid = mixed();
        if (valid.fatePoints().get().balance() != 13 || valid.receipts().get(2).debit().balance() != 13)
        { throw new AssertionError("Lost debit response"); }
        rejects(() -> document(List.of(debit(7, 13)), Optional.empty()));
        rejects(() -> document(List.of(debit(8, 13)), valid.fatePoints()));
        rejects(() -> document(List.of(debit(7, 12)), valid.fatePoints()));
        rejects(() -> document(List.of(
            new RunReceipt(COMMAND, RunReceipt.Operation.RESUME, 1, 2, RunLifecycle.State.ACTIVE, NONE),
            debit(7, 13)), valid.fatePoints()));
        rejects(() -> document(List.of(
            new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 1, 2, RunLifecycle.State.PAUSED, NONE),
            debit(7, 13)), valid.fatePoints()));
        rejects(() -> new RunReceipt(COMMAND, RunReceipt.Operation.FP_DEBIT, 2, 3,
            RunLifecycle.State.ACTIVE, NONE));
        rejects(() -> new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.FP_DEBIT, 2, 3,
            RunLifecycle.State.ACTIVE, NONE, null, debit(7, 13).debit()));
        // Later lifecycle operations and debits retain the first original response.
        java.util.ArrayList<RunReceipt> history = new java.util.ArrayList<>(valid.receipts());
        history.add(new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.PAUSE, 3, 4,
            RunLifecycle.State.PAUSED, NONE));
        history.add(new RunReceipt(UUID.randomUUID(), RunReceipt.Operation.RESUME, 4, 5,
            RunLifecycle.State.ACTIVE, NONE));
        UUID second = UUID.randomUUID();
        history.add(new RunReceipt(second, RunReceipt.Operation.FP_DEBIT, 5, 6,
            RunLifecycle.State.ACTIVE, NONE, null,
            new FatePointDebits.Receipt(second, 5, 6, "second-test-only", "v2", 4, 9)));
        FatePointLedger ledger = valid.fatePoints().get().append(
            new FatePointLedger.Entry(FatePointLedger.Kind.DISCRETIONARY_COST, 4));
        RunDocument later = new RunDocument(new RunLifecycle(ID, 6, RunLifecycle.State.ACTIVE, NONE),
            "v1", history, Optional.of(ledger));
        if (later.receipts().get(2).debit().balance() != 13 || later.fatePoints().get().balance() != 9)
        { throw new AssertionError("Original response changed after later debit"); }
        // An unjournaled trailing earning cannot hide a changed debit result.
        rejects(() -> new RunDocument(later.lifecycle(), "v1", history, Optional.of(ledger.append(
            new FatePointLedger.Entry(FatePointLedger.Kind.EARNING, 1)))));
        System.out.println("DebitJournalChecks passed");
    }
}
