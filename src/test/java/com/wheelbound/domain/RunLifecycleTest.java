package com.wheelbound.domain;

import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;

public class RunLifecycleTest
{
    private final UUID runId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final RunIdentity identity = new RunIdentity("account:profile-a", runId);

    private RunLifecycle active(RunLifecycle.Obligation obligation)
    {
        return new RunLifecycle(identity, 7, RunLifecycle.State.ACTIVE, obligation);
    }

    @Test public void pauseAndResumePreserveEveryObligationAndOriginalSnapshot()
    {
        for (RunLifecycle.Obligation obligation : RunLifecycle.Obligation.values())
        {
            RunLifecycle original = active(obligation);
            RunLifecycle paused = original.pause(identity, 7);
            RunLifecycle resumed = paused.resume(identity, 8);
            assertEquals(RunLifecycle.State.ACTIVE, original.state());
            assertEquals(7, original.revision());
            assertEquals(RunLifecycle.State.PAUSED, paused.state());
            assertEquals(RunLifecycle.State.ACTIVE, resumed.state());
            assertEquals(9, resumed.revision());
            assertEquals(identity, resumed.identity());
            assertEquals(obligation, paused.obligation());
            assertEquals(obligation, resumed.obligation());
        }
    }

    @Test public void pausedAndPrePauseCallbacksCannotEarnAfterResume()
    {
        RunLifecycle original = active(RunLifecycle.Obligation.ACTIVE_OBJECTIVE);
        RunLifecycle paused = original.pause(identity, 7);
        RunLifecycle resumed = paused.resume(identity, 8);
        assertTrue(original.acceptsObservation(identity, 7));
        assertFalse(paused.acceptsObservation(identity, 8));
        assertFalse(resumed.acceptsObservation(identity, 7));
        assertFalse(resumed.acceptsObservation(identity, 8));
        assertTrue(resumed.acceptsObservation(identity, 9));
    }

    @Test public void foreignAccountsProfilesAndRunsAreRejected()
    {
        RunLifecycle current = active(RunLifecycle.Obligation.NONE);
        for (RunIdentity foreign : new RunIdentity[]{
            new RunIdentity("other-account:profile-a", runId),
            new RunIdentity("account:profile-b", runId),
            new RunIdentity("account:profile-a", UUID.fromString("00000000-0000-0000-0000-000000000002"))})
        {
            assertFalse(current.acceptsObservation(foreign, 7));
            assertThrows(IllegalArgumentException.class, () -> current.pause(foreign, 7));
        }
        assertFalse(current.acceptsObservation(null, 7));
    }

    @Test public void repeatedDesiredStateIsANoOpWithCurrentRevision()
    {
        RunLifecycle current = active(RunLifecycle.Obligation.NONE);
        assertSame(current, current.resume(identity, 7));
        RunLifecycle paused = current.pause(identity, 7);
        assertSame(paused, paused.pause(identity, 8));
        // This is not durable command replay: stale revisions still fail, even for a no-op.
        assertThrows(IllegalArgumentException.class, () -> paused.pause(identity, 7));
    }

    @Test public void inactiveAndTerminalStatesCannotBePausedOrResumed()
    {
        for (RunLifecycle.State state : new RunLifecycle.State[]{RunLifecycle.State.NOT_STARTED,
            RunLifecycle.State.ABANDONED, RunLifecycle.State.COMPLETED})
        {
            RunLifecycle current = new RunLifecycle(identity, 0, state, RunLifecycle.Obligation.NONE);
            assertThrows(IllegalStateException.class, () -> current.pause(identity, 0));
            assertThrows(IllegalStateException.class, () -> current.resume(identity, 0));
            assertFalse(current.acceptsObservation(identity, 0));
        }
    }

    @Test public void staleCommandsCannotChangeCurrentState()
    {
        RunLifecycle current = active(RunLifecycle.Obligation.SETTLEMENT_PENDING);
        assertThrows(IllegalArgumentException.class, () -> current.pause(identity, 6));
        assertThrows(IllegalArgumentException.class, () -> current.pause(identity, 8));
        assertEquals(7, current.revision());
    }

    @Test public void revisionOverflowCannotWrap()
    {
        RunLifecycle current = new RunLifecycle(identity, Long.MAX_VALUE,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE);
        assertThrows(ArithmeticException.class, () -> current.pause(identity, Long.MAX_VALUE));
    }

    @Test public void invalidIdentityAndSnapshotInputsAreRejected()
    {
        assertThrows(IllegalArgumentException.class, () -> new RunIdentity(" \t", runId));
        assertThrows(NullPointerException.class, () -> new RunIdentity(null, runId));
        assertThrows(NullPointerException.class, () -> new RunIdentity("account", null));
        assertThrows(IllegalArgumentException.class, () -> new RunLifecycle(identity, -1,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
        assertThrows(NullPointerException.class, () -> new RunLifecycle(null, 0,
            RunLifecycle.State.ACTIVE, RunLifecycle.Obligation.NONE));
        assertThrows(NullPointerException.class, () -> new RunLifecycle(identity, 0, null, RunLifecycle.Obligation.NONE));
        assertThrows(NullPointerException.class, () -> new RunLifecycle(identity, 0, RunLifecycle.State.ACTIVE, null));
    }

    @Test public void identityHasValueSemanticsWithoutNormalizingOpaqueKeys()
    {
        RunIdentity copy = new RunIdentity("account:profile-a", runId);
        assertEquals(identity, copy);
        assertEquals(identity.hashCode(), copy.hashCode());
        assertNotEquals(identity, new RunIdentity(" account:profile-a", runId));
    }
}
