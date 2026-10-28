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
    private RunSession.Token baseline;

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
        baseline = null;
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
        if (!value) { baseline = null; session.disconnect(); }
    }

    public synchronized CompletableFuture<Boolean> connect(AtomicRunStore store, RunIdentity identity)
    {
        if (!enabled) { return CompletableFuture.completedFuture(false); }
        baseline = null;
        return session.connect(store, identity);
    }

    /** Session boundaries retain opt-in, but require an explicit fresh connect. */
    public synchronized void disconnect()
    {
        baseline = null;
        session.disconnect();
    }

    /** Call only after capturing fresh client observation baselines for this token.
     * The adapter must capture the token before sampling, on its serialized client
     * callback path. Neither storage completion nor old/paused samples qualify.
     * This acknowledgement contains no observations and awards no progress.
     */
    public synchronized boolean establishBaseline(RunSession.Token token)
    {
        if (!accepts(token)) { return false; }
        baseline = token;
        return true;
    }

    /** Runs fresh sampling on the caller's serialized client callback path, never a
     * storage completion thread. The sampler must replace its old samples and must
     * not award progress. Admission stays closed on failure or a session boundary
     * during sampling. Exceptions propagate to the adapter with admission closed.
     */
    public synchronized boolean sampleBaseline(RunSession.Token token, Runnable sampler)
    {
        if (!accepts(token)) { return false; }
        baseline = null;
        Objects.requireNonNull(sampler, "sampler").run();
        return establishBaseline(token);
    }

    /** Admission for future observation adapters; raw capture/accepts only identify
     * the storage session and do not certify that client baselines were initialized.
     */
    public synchronized boolean acceptsObservation(RunSession.Token token)
    {
        return baseline != null && accepts(baseline) && accepts(token);
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
