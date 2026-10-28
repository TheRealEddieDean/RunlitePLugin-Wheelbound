package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class FoundationPreviewTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final List<Runnable> tasks = new ArrayList<>();
    private final FoundationPreview preview = new FoundationPreview(tasks::add);
    private final RunIdentity identity = new RunIdentity("account:profile", UUID.randomUUID());

    private AtomicRunStore store() throws Exception
    {
        AtomicRunStore store = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), identity);
        store.write(new RunDocument(new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE), "fixture"), -1);
        return store;
    }

    @Test public void defaultDisabledNeverSubmitsStorageWork() throws Exception
    {
        assertFalse(preview.connect(store(), identity).join());
        assertTrue(tasks.isEmpty());
        assertFalse(preview.capture().isPresent());
        assertFalse(preview.accepts(null));
    }

    @Test public void disableThenEnableCannotPublishPreviousQueuedRead() throws Exception
    {
        AtomicRunStore store = store();
        preview.setEnabled(true);
        CompletableFuture<Boolean> stale = preview.connect(store, identity);
        preview.setEnabled(false);
        preview.setEnabled(true);
        tasks.remove(0).run();
        assertFalse(stale.join());
        assertFalse(preview.capture().isPresent());
        CompletableFuture<Boolean> fresh = preview.connect(store, identity);
        tasks.remove(0).run();
        assertTrue(fresh.join());
        assertTrue(preview.capture().isPresent());
    }

    @Test public void disableInvalidatesTokensAndEnableDoesNotReconnect() throws Exception
    {
        preview.setEnabled(true);
        preview.connect(store(), identity); tasks.remove(0).run();
        RunSession.Token old = preview.capture().get();
        assertTrue(preview.accepts(old));
        preview.setEnabled(false);
        assertFalse(preview.accepts(old));
        preview.setEnabled(true);
        assertFalse(preview.capture().isPresent());
        assertFalse(preview.accepts(old));
        assertTrue(tasks.isEmpty());
    }

    @Test public void disconnectRetainsOptInButRequiresFreshSession() throws Exception
    {
        AtomicRunStore store = store();
        preview.setEnabled(true);
        preview.connect(store, identity); tasks.remove(0).run();
        RunSession.Token old = preview.capture().get();
        preview.disconnect();
        assertFalse(preview.accepts(old));
        assertFalse(preview.capture().isPresent());
        CompletableFuture<Boolean> fresh = preview.connect(store, identity);
        tasks.remove(0).run();
        assertTrue(fresh.join());
        assertFalse(preview.accepts(old));
        assertTrue(preview.accepts(preview.capture().get()));
        assertFalse(new FoundationPreview(tasks::add).connect(store, identity).join());
    }

    @Test public void injectedDirectoryStaysUntouchedUntilEnabledWorkerRuns() throws Exception
    {
        Path directory = folder.getRoot().toPath().resolve("plugin");
        FoundationPreview bound = new FoundationPreview(tasks::add, directory, new RunCodec(new Gson()));
        assertFalse(bound.connect(identity).join());
        bound.setEnabled(true);
        assertFalse(Files.exists(directory));
        CompletableFuture<Boolean> open = bound.connect(identity);
        assertFalse(Files.exists(directory));
        assertFalse(open.isDone());
        tasks.remove(0).run();
        try { open.join(); fail("Missing save must fail"); }
        catch (CompletionException expected) { assertTrue(expected.getCause() instanceof java.io.IOException); }
        AtomicRunStore missing = new AtomicRunStore(directory, new RunCodec(new Gson()), identity);
        assertFalse(Files.exists(missing.snapshotPath()));
        assertFalse(Files.exists(missing.pendingPath()));
        assertFalse(bound.capture().isPresent());
    }

    @Test public void injectedDirectoryOpensExistingSaveAndIsolatesProfiles() throws Exception
    {
        store();
        FoundationPreview bound = new FoundationPreview(tasks::add, folder.getRoot().toPath(),
            new RunCodec(new Gson()));
        bound.setEnabled(true);
        CompletableFuture<Boolean> open = bound.connect(identity);
        tasks.remove(0).run();
        assertTrue(open.join());
        RunSession.Token old = bound.capture().get();
        RunIdentity other = new RunIdentity("account:other-profile", identity.runId());
        CompletableFuture<Boolean> foreign = bound.connect(other);
        assertFalse(bound.accepts(old));
        tasks.remove(0).run();
        try { foreign.join(); fail("Different profile must not open the first save"); }
        catch (CompletionException expected) { assertTrue(expected.getCause() instanceof java.io.IOException); }
        assertFalse(bound.capture().isPresent());
    }

    @Test public void injectedDirectoryDoesNotSearchAnotherPluginDirectory() throws Exception
    {
        store();
        FoundationPreview bound = new FoundationPreview(tasks::add,
            folder.getRoot().toPath().resolve("other-plugin"), new RunCodec(new Gson()));
        bound.setEnabled(true);
        CompletableFuture<Boolean> open = bound.connect(identity);
        tasks.remove(0).run();
        try { open.join(); fail("Must use only the injected directory"); }
        catch (CompletionException expected) { assertTrue(expected.getCause() instanceof java.io.IOException); }
        assertFalse(bound.capture().isPresent());
    }

    @Test public void invalidIdentityInvalidatesPublishedSession() throws Exception
    {
        store();
        FoundationPreview bound = new FoundationPreview(tasks::add, folder.getRoot().toPath(),
            new RunCodec(new Gson()));
        bound.setEnabled(true);
        bound.connect(identity); tasks.remove(0).run();
        RunSession.Token old = bound.capture().get();
        try { bound.connect((RunIdentity) null); fail("Identity is required"); }
        catch (NullPointerException expected) { }
        assertFalse(bound.accepts(old));
        assertFalse(bound.capture().isPresent());
        assertTrue(tasks.isEmpty());
    }

    @Test public void openRequiresExplicitFreshBaselineBeforeObservationAdmission() throws Exception
    {
        preview.setEnabled(true);
        preview.connect(store(), identity); tasks.remove(0).run();
        RunSession.Token token = preview.capture().get();
        assertTrue(preview.accepts(token));
        assertFalse(preview.acceptsObservation(token));
        assertFalse(preview.establishBaseline(null));
        assertTrue(preview.establishBaseline(token));
        assertTrue(preview.acceptsObservation(preview.capture().get()));
        assertFalse(preview.acceptsObservation(null));
    }

    @Test public void reconnectCannotReuseBaselineEvenAtSamePersistedRevision() throws Exception
    {
        AtomicRunStore store = store();
        preview.setEnabled(true);
        preview.connect(store, identity); tasks.remove(0).run();
        RunSession.Token old = preview.capture().get();
        assertTrue(preview.establishBaseline(old));
        CompletableFuture<Boolean> fresh = preview.connect(store, identity);
        assertFalse(preview.acceptsObservation(old));
        assertFalse(preview.establishBaseline(old));
        tasks.remove(0).run();
        assertTrue(fresh.join());
        RunSession.Token current = preview.capture().get();
        assertFalse(preview.acceptsObservation(current));
        assertFalse(preview.establishBaseline(old));
        assertTrue(preview.establishBaseline(current));
        assertTrue(preview.acceptsObservation(current));
    }

    @Test public void disableAndDisconnectEachRequireAnotherBaseline() throws Exception
    {
        AtomicRunStore store = store();
        preview.setEnabled(true);
        preview.connect(store, identity); tasks.remove(0).run();
        RunSession.Token old = preview.capture().get();
        assertTrue(preview.establishBaseline(old));
        preview.setEnabled(false);
        preview.setEnabled(true);
        assertFalse(preview.acceptsObservation(old));
        assertFalse(preview.establishBaseline(old));
        preview.connect(store, identity); tasks.remove(0).run();
        RunSession.Token current = preview.capture().get();
        assertFalse(preview.acceptsObservation(current));
        assertTrue(preview.establishBaseline(current));
        preview.disconnect();
        assertFalse(preview.acceptsObservation(current));
        assertFalse(preview.establishBaseline(current));
    }
}
