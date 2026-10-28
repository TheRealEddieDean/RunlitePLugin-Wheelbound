package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.RunIdentity;
import com.wheelbound.domain.RunLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
}
