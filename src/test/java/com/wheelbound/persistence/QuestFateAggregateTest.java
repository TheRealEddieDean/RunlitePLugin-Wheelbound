package com.wheelbound.persistence;

import com.wheelbound.domain.QuestFateAssignmentChecks;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class QuestFateAggregateTest
{
    @Test public void retainsQuestCommitmentAndRefusesLossyWrites()
    {
        QuestFateAggregateChecks.verify(QuestFateAssignmentChecks.fixture(), head -> {
            var codec = new RunCodec(new com.google.gson.Gson());
            assertThrows(IllegalArgumentException.class, () -> codec.encode(head));
            assertThrows(IllegalArgumentException.class, () -> codec.encodeRetryCandidate(head));
        });
    }
}
