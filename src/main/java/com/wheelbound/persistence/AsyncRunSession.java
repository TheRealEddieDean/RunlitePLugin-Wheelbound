package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import java.util.Objects;
import java.util.Optional;
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

    public synchronized boolean accepts(RunSession.Token token)
    {
        return current != null && current.accepts(token);
    }
}
