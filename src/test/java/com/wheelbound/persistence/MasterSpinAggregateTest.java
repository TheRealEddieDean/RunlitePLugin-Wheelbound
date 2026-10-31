package com.wheelbound.persistence;

import org.junit.Test;
import com.google.gson.Gson;

public class MasterSpinAggregateTest
{
    @Test public void retainsOnlyBoundReservationsAcrossLifecycleCopies()
    { MasterSpinAggregateChecks.main(new String[0]); }

    @Test(expected = IllegalArgumentException.class)
    public void currentCodecRefusesToDropExplicitSpinState()
    {
        var head = MasterResolutionAggregateChecks.resolved(0);
        var spins = new com.wheelbound.domain.MasterSpinState(1, head.lifecycle().identity(),
            6, 0, java.util.Optional.empty());
        new RunCodec(new Gson()).encode(MasterSpinAggregateChecks.attach(head, spins));
    }
}
