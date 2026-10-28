package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Unwired, read-only foundation preview. Opt-in is process-local and starts disabled.
 * This does not enable gameplay or create runs. Supply a storage worker executor.
 * Live adapters must disconnect on every account/profile boundary and establish fresh
 * observation baselines before using a token; opening a save is not observation evidence.
 */
public final class FoundationPreview
{
    private final AsyncRunSession session;
    private boolean enabled;

    public FoundationPreview(Executor worker)
    {
        session = new AsyncRunSession(worker);
    }

    /** Disabling immediately invalidates both published sessions and queued reads.
     * Enabling alone never reconnects; the caller must supply a verified identity again.
     */
    public synchronized void setEnabled(boolean value)
    {
        enabled = value;
        if (!value) { session.disconnect(); }
    }

    public synchronized CompletableFuture<Boolean> connect(AtomicRunStore store, RunIdentity identity)
    {
        if (!enabled) { return CompletableFuture.completedFuture(false); }
        return session.connect(store, identity);
    }

    /** Session boundaries retain opt-in, but require an explicit fresh connect. */
    public synchronized void disconnect()
    {
        session.disconnect();
    }

    public synchronized Optional<RunSession.Token> capture()
    {
        return enabled ? session.capture() : Optional.empty();
    }

    public synchronized boolean accepts(RunSession.Token token)
    {
        return enabled && session.accepts(token);
    }
}
