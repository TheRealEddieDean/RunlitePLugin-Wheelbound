package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.RunLifecycle;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Disabled-mode adapter boundary. Tokens qualify immediate callbacks, never durable evidence.
 * Call disconnect on logout, hopping, profile changes and shutdown. Storage runs on the
 * caller's worker thread; this class must not be used to perform IO on the client or EDT.
 */
public final class RunSession
{
    public static final class Token
    {
        private final Object generation;
        private final RunIdentity identity;
        private final long revision;
        private Token(Object generation, RunLifecycle lifecycle)
        {
            this.generation = generation;
            identity = lifecycle.identity();
            revision = lifecycle.revision();
        }
    }

    private Object generation = new Object();
    private AtomicRunStore store;
    private RunLifecycle current;

    public synchronized void disconnect()
    {
        generation = new Object();
        store = null;
        current = null;
    }

    /** Opens an existing run only. Missing or unreadable data never starts or resets a run. */
    public synchronized void connect(AtomicRunStore candidate, RunIdentity expected) throws IOException
    {
        disconnect();
        Objects.requireNonNull(expected, "expected");
        RunLifecycle loaded = Objects.requireNonNull(candidate, "candidate").load()
            .orElseThrow(() -> new IOException("Run has not been initialized")).lifecycle();
        if (!expected.equals(loaded.identity())) { throw new IllegalArgumentException("Foreign run context"); }
        store = candidate;
        current = loaded;
    }

    public synchronized Optional<Token> capture()
    {
        return current != null && current.state() == RunLifecycle.State.ACTIVE
            ? Optional.of(new Token(generation, current)) : Optional.empty();
    }

    public synchronized boolean accepts(Token token)
    {
        return token != null && token.generation == generation && current != null
            && current.acceptsObservation(token.identity, token.revision);
    }

    /** Worker-thread command boundary, including historical replay while paused.
     * Invalidates callbacks before storage and refreshes from the durable head.
     * Does not establish fresh baselines or authenticate gameplay evidence sources.
     */
    public synchronized GrandActivationReceipt activateGrandFate(GrandActivationReceipt request,
        long expectedRevision) throws IOException
    {
        if (store == null) { throw new IllegalStateException("No connected run"); }
        AtomicRunStore target = store;
        generation = new Object();
        try
        {
            GrandActivationReceipt receipt = target.activateGrandFate(request, expectedRevision);
            current = target.load().orElseThrow(() -> new IOException("Run disappeared")).lifecycle();
            return receipt;
        }
        catch (IOException | RuntimeException ex)
        {
            disconnect();
            throw ex;
        }
    }

    /** Replays durable commands using their original revision; refreshes from the actual head,
     * not the historical replay response. Any failure closes observation admission.
     */
    public synchronized RunReceipt execute(UUID commandId, long expectedRevision,
        RunReceipt.Operation operation) throws IOException
    {
        if (store == null) { throw new IllegalStateException("No connected run"); }
        AtomicRunStore target = store;
        RunIdentity identity = current.identity();
        generation = new Object();
        try
        {
            RunReceipt receipt = new LifecycleCommands(target).execute(identity, commandId, expectedRevision, operation);
            current = target.load().orElseThrow(() -> new IOException("Run disappeared")).lifecycle();
            return receipt;
        }
        catch (IOException | RuntimeException ex)
        {
            disconnect();
            throw ex;
        }
    }
}
