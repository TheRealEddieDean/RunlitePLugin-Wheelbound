package com.wheelbound.persistence;

import com.wheelbound.domain.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.IntUnaryOperator;

/**
 * Foundation draw transaction, not an enabled Master-spin/Fate generator or spin-debit policy.
 * Persist one supplied reviewed pool/result before returning it for display.
 */
public final class DurableWheelDraws
{
    private final AtomicRunStore store;
    private final IntUnaryOperator randomTicket;

    public DurableWheelDraws(AtomicRunStore store, IntUnaryOperator randomTicket)
    {
        this.store = Objects.requireNonNull(store, "store");
        this.randomTicket = Objects.requireNonNull(randomTicket, "randomTicket");
    }

    public DrawCommitment commit(RunIdentity identity, UUID commandId, long expectedRevision, WeightedPool pool) throws IOException
    {
        Objects.requireNonNull(commandId, "commandId"); Objects.requireNonNull(pool, "pool");
        RunDocument current = store.load().orElseThrow(() -> new IOException("Run has not been initialized"));
        RunLifecycle run = current.lifecycle();
        if (!run.identity().equals(identity)) { throw new IllegalArgumentException("Foreign run context"); }
        for (RunReceipt receipt : current.receipts())
        {
            if (!receipt.commandId().equals(commandId)) { continue; }
            if (receipt.operation() != RunReceipt.Operation.DRAW || receipt.beforeRevision() != expectedRevision
                || !receipt.draw().pool().equals(pool))
            { throw new IllegalArgumentException("Command ID reused for different draw request"); }
            return receipt.draw();
        }
        if (run.revision() != expectedRevision || !current.catalogVersion().equals(pool.version()))
        { throw new IllegalArgumentException("Stale revision/catalog snapshot"); }
        if (run.state() != RunLifecycle.State.ACTIVE || run.obligation() != RunLifecycle.Obligation.NONE)
        { throw new IllegalStateException("Cannot draw while inactive or a commitment is pending"); }
        long nextRevision = Math.addExact(expectedRevision, 1);
        DrawCommitment draw = new DrawCommitment(pool, randomTicket.applyAsInt(pool.totalWeight()));
        RunLifecycle next = new RunLifecycle(identity, nextRevision, run.state(), RunLifecycle.Obligation.OFFERS_COMMITTED);
        RunReceipt receipt = new RunReceipt(commandId, RunReceipt.Operation.DRAW, expectedRevision, nextRevision,
            next.state(), next.obligation(), draw);
        List<RunReceipt> receipts = new ArrayList<>(current.receipts()); receipts.add(receipt);
        store.write(new RunDocument(next, current.catalogVersion(), receipts, current.fatePoints(), current.inventory(), current.grandFate(), current.grandProgress()), expectedRevision);
        return draw;
    }
}
