package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import java.nio.file.Path;
import java.util.Objects;
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
    private final Path pluginDirectory;
    private final RunCodec codec;
    private boolean enabled;

    public FoundationPreview(Executor worker)
    {
        session = new AsyncRunSession(worker);
        pluginDirectory = null;
        codec = null;
    }

    /** Binds storage to the directory supplied by the future plugin adapter.
     * Construction performs no filesystem access. No default/global directory is used.
     */
    public FoundationPreview(Executor worker, Path pluginDirectory, RunCodec codec)
    {
        session = new AsyncRunSession(worker);
        this.pluginDirectory = Objects.requireNonNull(pluginDirectory, "pluginDirectory");
        this.codec = Objects.requireNonNull(codec, "codec");
    }

    /** Opens only an existing save for a caller-verified identity in the injected directory.
     * Loading remains on the storage worker; missing saves are never initialized.
     */
    public synchronized CompletableFuture<Boolean> connect(RunIdentity identity)
    {
        if (!enabled) { return CompletableFuture.completedFuture(false); }
        // Invalidate admission even when adapter input is invalid.
        session.disconnect();
        if (pluginDirectory == null)
        {
            throw new IllegalStateException("Preview storage directory was not supplied");
        }
        return session.connect(new AtomicRunStore(pluginDirectory, codec,
            Objects.requireNonNull(identity, "identity")), identity);
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
