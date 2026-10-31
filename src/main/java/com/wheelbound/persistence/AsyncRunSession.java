package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.MasterOrdinaryPoolEvidence;
import com.wheelbound.domain.MasterSelectionReadiness;
import java.util.Map;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Unwired foundation opener. Supply a storage worker executor, never the client/EDT.
 * Disconnect immediately on logout, hopping, profile changes and shutdown.
 * Each successful open starts a fresh observation boundary; no observations are buffered.
 */
public final class AsyncRunSession
{
    private final Executor worker;
    private Object generation = new Object();
    private RunSession current;

    public AsyncRunSession(Executor worker)
    {
        this.worker = Objects.requireNonNull(worker, "worker");
    }

    public synchronized void disconnect()
    {
        generation = new Object();
        if (current != null) { current.disconnect(); }
        current = null;
    }

    /** Returns true only if this open was published. Superseded queued reads skip storage;
     * reads already in progress may finish but cannot publish. Superseded reads return false;
     * read/submission failures complete exceptionally and never restore an old session.
     * Cancelling the returned future is not a disconnect; call disconnect explicitly.
     */
    public synchronized CompletableFuture<Boolean> connect(AtomicRunStore store, RunIdentity expected)
    {
        disconnect();
        Objects.requireNonNull(store, "store");
        Objects.requireNonNull(expected, "expected");
        Object request = generation;
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        try
        {
            worker.execute(() -> {
                boolean superseded;
                synchronized (this) { superseded = generation != request; }
                if (superseded)
                {
                    result.complete(false);
                    return;
                }
                RunSession candidate = new RunSession();
                try
                {
                    candidate.connect(store, expected);
                    boolean published;
                    synchronized (this)
                    {
                        published = generation == request;
                        if (published) { current = candidate; }
                        else { candidate.disconnect(); }
                    }
                    result.complete(published);
                }
                catch (Exception ex)
                {
                    candidate.disconnect();
                    result.completeExceptionally(ex);
                }
            });
        }
        catch (RuntimeException ex) { result.completeExceptionally(ex); }
        return result;
    }

    public synchronized Optional<RunSession.Token> capture()
    {
        return current == null ? Optional.empty() : current.capture();
    }

    /** Queues read-only durable-head assembly. Evidence is frozen before queuing;
     * tokens establish no detector/catalog authority. Outer empty means superseded
     * before execution. Started reads can finish after disconnect, but cannot restore
     * admission; their result is advisory and must never authorize a later draw.
     */
    public synchronized CompletableFuture<Optional<MasterSelectionReadiness>> masterReadiness(
        RunSession.Token token, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> ordinaryPools)
    {
        if (current == null || !current.accepts(token))
        {
            CompletableFuture<Optional<MasterSelectionReadiness>> rejected = new CompletableFuture<>();
            rejected.completeExceptionally(new IllegalArgumentException("Stale or foreign Master readiness callback"));
            return rejected;
        }
        Objects.requireNonNull(snapshotVersion, "snapshotVersion");
        Map<String, MasterOrdinaryPoolEvidence> frozen = Map.copyOf(ordinaryPools);
        return submit(target -> target.masterReadiness(token, snapshotVersion, frozen));
    }

    /** Queues activation on the storage worker and immediately closes callback admission.
     * Empty means superseded before execution, with no command sent to storage. Once
     * started, a command may commit even after disconnect; its receipt still completes
     * the future, but cannot restore that session. Cancellation is not disconnect.
     * This boundary does not authenticate sources or establish observation baselines.
     */
    public synchronized CompletableFuture<Optional<GrandActivationReceipt>> activateGrandFate(
        GrandActivationReceipt receipt, long expectedRevision)
    {
        Objects.requireNonNull(receipt, "receipt");
        return submit(target -> target.activateGrandFate(receipt, expectedRevision));
    }

    /** Queues a frozen Master creation using the operation-specific store command.
     * Closes callbacks immediately; supersession and cancellation follow activation
     * semantics. Exact replay reloads the current head, including Pause and retained
     * draw prefixes. No trusted evidence, spin allocation or sampling is established.
     */
    public synchronized CompletableFuture<Optional<MasterSelectionCreationReceipt>> createMasterSelection(
        MasterSelectionCreationReceipt request)
    {
        Objects.requireNonNull(request, "request");
        return submit(target -> target.createMasterSelection(request));
    }

    /** Qualifies the callback before closing admission or scheduling storage, then
     * rechecks on the worker. A rejected callback cannot invalidate a fresh session.
     * Tokens establish no baseline or complete-pool evidence authority.
     */
    public synchronized CompletableFuture<Optional<MasterSelectionCreationReceipt>> createMasterSelection(
        RunSession.Token token, MasterSelectionCreationReceipt request)
    {
        if (current == null || !current.acceptsMasterCreation(token, request))
        {
            CompletableFuture<Optional<MasterSelectionCreationReceipt>> rejected = new CompletableFuture<>();
            rejected.completeExceptionally(new IllegalArgumentException("Stale or foreign Master creation callback"));
            return rejected;
        }
        return submit(target -> target.createMasterSelection(token, request));
    }

    /** Read-only recovery on the storage worker, closing callback admission immediately.
     * Outer empty means superseded before execution; inner empty means the lookup
     * completed without an exact retained creation. Neither permits creation/reseeding.
     * A started lookup may complete after disconnect but cannot restore the session.
     * Failures close admission; cancellation is not disconnect.
     */
    public synchronized CompletableFuture<Optional<Optional<MasterSelectionCreationReceipt>>> committedMasterSelection(
        MasterSelectionCreationReceipt request)
    {
        Objects.requireNonNull(request, "request");
        return submit(target -> target.committedMasterSelection(request));
    }

    /** Worker-thread Pause/Resume boundary. Admission closes before queuing, including
     * historical replay; a replay refreshes from the actual durable head. Resume does
     * not authenticate observation sources or establish a fresh gameplay baseline.
     * Supersession, disconnect and cancellation follow activateGrandFate semantics.
     */
    public synchronized CompletableFuture<Optional<RunReceipt>> execute(UUID commandId,
        long expectedRevision, RunReceipt.Operation operation)
    {
        Objects.requireNonNull(commandId, "commandId");
        Objects.requireNonNull(operation, "operation");
        if (operation != RunReceipt.Operation.PAUSE && operation != RunReceipt.Operation.RESUME)
        { throw new IllegalArgumentException("Use the operation-specific transaction service"); }
        return submit(target -> target.execute(commandId, expectedRevision, operation));
    }

    @FunctionalInterface
    private interface Command<T>
    {
        T execute(RunSession target) throws IOException;
    }

    /** Called while holding this session's monitor. Never hold it during storage IO. */
    private <T> CompletableFuture<Optional<T>> submit(Command<T> command)
    {
        CompletableFuture<Optional<T>> result = new CompletableFuture<>();
        if (current == null)
        {
            result.completeExceptionally(new IllegalStateException("No connected run"));
            return result;
        }
        RunSession target = current;
        current = null;
        generation = new Object();
        Object request = generation;
        try
        {
            worker.execute(() -> {
                boolean superseded;
                synchronized (this) { superseded = generation != request; }
                if (superseded)
                {
                    target.disconnect();
                    result.complete(Optional.empty());
                    return;
                }
                try
                {
                    T committed = command.execute(target);
                    synchronized (this)
                    {
                        if (generation == request) { current = target; }
                        else { target.disconnect(); }
                    }
                    result.complete(Optional.of(committed));
                }
                catch (Exception ex)
                {
                    target.disconnect();
                    result.completeExceptionally(ex);
                }
            });
        }
        catch (RuntimeException ex)
        {
            target.disconnect();
            result.completeExceptionally(ex);
        }
        return result;
    }

    public synchronized boolean accepts(RunSession.Token token)
    {
        return current != null && current.accepts(token);
    }
}
