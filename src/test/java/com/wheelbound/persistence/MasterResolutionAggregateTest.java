package com.wheelbound.persistence;

import com.google.gson.Gson;
import com.wheelbound.domain.MasterSelectionResolutions;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

public class MasterResolutionAggregateTest
{
    @Test public void retainsAndValidatesStructuralHistory()
    { MasterResolutionAggregateChecks.main(new String[0]); }

    @Test public void schema16RefusesHistoryRatherThanDroppingIt() throws Exception
    {
        RunCodec codec = new RunCodec(new Gson());
        for (int count : new int[] {0, 1, 513})
        {
            RunDocument head = MasterResolutionAggregateChecks.resolved(count);
            assertThrows(IllegalArgumentException.class, () -> codec.encode(head));
        }
        RunDocument legacy = MasterCreationAggregateChecks.fixture(0);
        assertFalse(codec.decode(codec.encode(legacy), legacy.lifecycle().identity()).masterResolutions().isPresent());
        RunDocument empty = MasterResolutionAggregateChecks.copy(legacy, legacy.lifecycle(),
            legacy.pendingMasterSelection(), Optional.of(new MasterSelectionResolutions(
                legacy.lifecycle().identity(), 5, List.of())));
        assertThrows(IllegalArgumentException.class, () -> codec.encode(empty));
    }
}
