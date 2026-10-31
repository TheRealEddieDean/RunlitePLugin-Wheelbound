package com.wheelbound.persistence;

import org.junit.Test;
import com.google.gson.Gson;

public class MasterSpinAggregateTest
{
    @Test public void retainsOnlyBoundReservationsAcrossLifecycleCopies()
    { MasterSpinAggregateChecks.main(new String[0]); }

    @Test
    public void currentCodecRetainsExplicitSpinState() throws Exception
    {
        var head = MasterResolutionAggregateChecks.resolved(0);
        var spins = new com.wheelbound.domain.MasterSpinState(1, head.lifecycle().identity(),
            6, 0, java.util.Optional.empty());
        var codec = new RunCodec(new Gson());
        var restored = codec.decode(codec.encode(MasterSpinAggregateChecks.attach(head, spins)),
            head.lifecycle().identity());
        org.junit.Assert.assertEquals(0, restored.masterSpins().get().allotted());
    }
}
