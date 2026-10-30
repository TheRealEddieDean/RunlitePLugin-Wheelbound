package com.wheelbound.persistence;

import com.wheelbound.domain.FatePointDebits;
import com.wheelbound.domain.RunIdentity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Persists a trusted debit and its original response together. This is not a purchase
 * service: callers must supply authorized quotes and legal actions. Product mutations
 * require a future aggregate transaction; never call this separately to pay for an edit.
 * Deliberately unwired from the playable plugin.
 */
public final class DurableFatePointDebits
{
    private final AtomicRunStore store;

    public DurableFatePointDebits(AtomicRunStore store)
    { this.store = Objects.requireNonNull(store, "store"); }

    public FatePointDebits.Receipt debit(RunIdentity identity, UUID commandId,
        long expectedRevision, String quoteKey, String quoteVersion, long amount) throws IOException
    {
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(quoteKey, "quoteKey");
        Objects.requireNonNull(quoteVersion, "quoteVersion");
        RunDocument current = store.load().orElseThrow(() -> new IOException("Run has not been initialized"));
        if (!current.lifecycle().identity().equals(identity))
        { throw new IllegalArgumentException("Foreign run context"); }
        for (RunReceipt recorded : current.receipts())
        {
            if (!recorded.commandId().equals(commandId)) { continue; }
            FatePointDebits.Receipt debit = recorded.debit();
            if (recorded.operation() != RunReceipt.Operation.FP_DEBIT
                || recorded.beforeRevision() != expectedRevision || debit.amount() != amount
                || !debit.quoteKey().equals(quoteKey) || !debit.quoteVersion().equals(quoteVersion))
            { throw new IllegalArgumentException("Command ID reused for different debit request"); }
            return debit;
        }
        FatePointDebits.Result result = new FatePointDebits(current.lifecycle(),
            current.fatePoints().orElseThrow(() -> new IllegalStateException("FP state unavailable")))
            .debit(identity, commandId, expectedRevision, quoteKey, quoteVersion, amount);
        FatePointDebits proposed = result.proposed();
        RunReceipt receipt = new RunReceipt(commandId, RunReceipt.Operation.FP_DEBIT,
            expectedRevision, proposed.lifecycle().revision(), proposed.lifecycle().state(),
            proposed.lifecycle().obligation(), null, result.receipt());
        List<RunReceipt> history = new ArrayList<>(current.receipts());
        history.add(receipt);
        store.write(new RunDocument(proposed.lifecycle(), current.catalogVersion(), history,
            Optional.of(proposed.ledger()), current.inventory(), current.grandFate(), current.grandProgress(), current.grandJournal(), current.grandActivations()), expectedRevision);
        return result.receipt();
    }
}
