package com.wheelbound.persistence;

import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.MasterOrdinaryPoolEvidence;
import com.wheelbound.domain.MasterSelectionReadiness;
import java.util.Map;
import com.wheelbound.domain.GrandActivationReceipt;
import com.wheelbound.domain.MasterSelectionCreationReceipt;
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

    /** Read-only worker boundary against the actual durable head. The token qualifies
     * this connection, not the evidence source. No reservation or draw authority is
     * returned; subsequent commitment must revalidate. Failures after IO begins close
     * admission. Rejected callbacks perform no IO and preserve the fresh connection.
     */
    public synchronized MasterSelectionReadiness masterReadiness(Token token, String snapshotVersion,
        Map<String, MasterOrdinaryPoolEvidence> ordinaryPools) throws IOException
    {
        if (!accepts(token))
        { throw new IllegalArgumentException("Stale or foreign Master readiness callback"); }
        generation = new Object();
        try
        {
            RunDocument head = store.load().orElseThrow(() -> new IOException("Run disappeared"));
            MasterSelectionReadiness readiness = MasterAggregateEvidence.assemble(head, token.identity,
                token.revision, snapshotVersion, ordinaryPools);
            current = head.lifecycle();
            return readiness;
        }
        catch (IOException | RuntimeException ex)
        {
            disconnect();
            throw ex;
        }
    }

    /** Immediate callback qualification only; never authenticates eligibility evidence. */
    synchronized boolean acceptsMasterCreation(Token token, MasterSelectionCreationReceipt request)
    {
        return accepts(token) && request != null && token.identity.equals(request.identity())
            && token.revision == request.beforeRevision();
    }

    /** New callback submission, qualified against this connection and revision.
     * Rejected callbacks leave the current connection intact and perform no IO.
     * Historical recovery/replay uses the explicit request-only APIs instead.
     */
    public synchronized MasterSelectionCreationReceipt createMasterSelection(Token token,
        MasterSelectionCreationReceipt request) throws IOException
    {
        if (!acceptsMasterCreation(token, request))
        { throw new IllegalArgumentException("Stale or foreign Master creation callback"); }
        return createMasterSelection(request);
    }

    /** Read-only recovery of the exact original request. Absence never authorizes
     * creation or a fresh stream. Refreshes the durable lifecycle and invalidates
     * callbacks even when absent; failures require reconnecting.
     */
    public synchronized Optional<MasterSelectionCreationReceipt> committedMasterSelection(
        MasterSelectionCreationReceipt request) throws IOException
    {
        if (store == null) { throw new IllegalStateException("No connected run"); }
        AtomicRunStore target = store;
        generation = new Object();
        try
        {
            Optional<MasterSelectionCreationReceipt> receipt = target.committedMasterSelection(request);
            current = target.load().orElseThrow(() -> new IOException("Run disappeared")).lifecycle();
            return receipt;
        }
        catch (IOException | RuntimeException ex)
        {
            disconnect();
            throw ex;
        }
    }

    /** Persists the supplied frozen creation on the storage worker, including exact
     * historical replay. Invalidates callbacks and reloads the actual durable head.
     * This does not authenticate evidence, allocate spins or sample outcomes.
     * Any failure requires reconnecting before further commands or observations.
     */
    public synchronized MasterSelectionCreationReceipt createMasterSelection(
        MasterSelectionCreationReceipt request) throws IOException
    {
        if (store == null) { throw new IllegalStateException("No connected run"); }
        AtomicRunStore target = store;
        generation = new Object();
        try
        {
            MasterSelectionCreationReceipt receipt = target.createMasterSelection(request);
            current = target.load().orElseThrow(() -> new IOException("Run disappeared")).lifecycle();
            return receipt;
        }
        catch (IOException | RuntimeException ex)
        {
            disconnect();
            throw ex;
        }
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
