package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.*;
import java.io.IOException;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class RunSessionTest
{
    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private final RunIdentity id = new RunIdentity("account:profile", UUID.randomUUID());
    private AtomicRunStore store()
    { return new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id); }
    private RunSession open() throws Exception
    {
        store().write(new RunDocument(new RunLifecycle(id, 0, RunLifecycle.State.ACTIVE,
            RunLifecycle.Obligation.ACTIVE_OBJECTIVE), "fixture"), -1);
        RunSession session = new RunSession(); session.connect(store(), id); return session;
    }

    @Test public void reconnectSameRevisionRejectsPreviousSessionAndForeignTokens() throws Exception
    {
        RunSession session = open(); RunSession.Token before = session.capture().get();
        assertTrue(session.accepts(before));
        RunSession other = new RunSession(); other.connect(store(), id);
        assertFalse(other.accepts(before));
        session.disconnect(); assertFalse(session.accepts(before)); assertFalse(session.capture().isPresent());
        session.connect(store(), id); assertFalse(session.accepts(before));
        assertTrue(session.accepts(session.capture().get()));
    }

    @Test public void pauseResumeAndHistoricalReplayDoNotAdmitOldCallbacks() throws Exception
    {
        RunSession session = open(); RunSession.Token before = session.capture().get();
        UUID pause = UUID.randomUUID();
        session.execute(pause, 0, RunReceipt.Operation.PAUSE);
        assertFalse(session.capture().isPresent()); assertFalse(session.accepts(before));
        session.execute(UUID.randomUUID(), 1, RunReceipt.Operation.RESUME);
        RunSession.Token resumed = session.capture().get(); assertTrue(session.accepts(resumed));
        assertEquals(RunLifecycle.State.PAUSED, session.execute(pause, 0, RunReceipt.Operation.PAUSE).state());
        assertFalse(session.accepts(resumed)); assertTrue(session.capture().isPresent());
        assertEquals(RunLifecycle.Obligation.ACTIVE_OBJECTIVE, store().load().get().lifecycle().obligation());
        assertEquals(2, store().load().get().receipts().size());
    }

    @Test public void failedOpenAndFailedCommandCloseAdmission() throws Exception
    {
        RunSession session = open(); RunSession.Token before = session.capture().get();
        assertThrows(IllegalArgumentException.class, () -> session.connect(store(), new RunIdentity("foreign", id.runId())));
        assertFalse(session.accepts(before)); assertFalse(session.capture().isPresent());
        session.connect(store(), id);
        assertThrows(IllegalArgumentException.class, () -> session.execute(UUID.randomUUID(), 5, RunReceipt.Operation.PAUSE));
        assertFalse(session.capture().isPresent());
        assertEquals(0, store().load().get().lifecycle().revision());
    }

    @Test public void lostAcknowledgementRequiresReopenAndPreservesPause() throws Exception
    {
        RunSession session = open(); RunSession.Token before = session.capture().get();
        AtomicRunStore failing = new AtomicRunStore(folder.getRoot().toPath(), new RunCodec(new Gson()), id,
            boundary -> { if (boundary == AtomicRunStore.Boundary.REPLACED) { throw new IOException("Lost ack"); } });
        session.connect(failing, id); UUID command = UUID.randomUUID();
        assertThrows(IOException.class, () -> session.execute(command, 0, RunReceipt.Operation.PAUSE));
        assertFalse(session.accepts(before)); assertFalse(session.capture().isPresent());
        session.connect(store(), id); session.execute(command, 0, RunReceipt.Operation.PAUSE);
        assertFalse(session.capture().isPresent()); assertEquals(1, store().load().get().receipts().size());
    }
}
