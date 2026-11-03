package com.wheelbound.persistence;

import org.junit.Test;
import com.wheelbound.domain.MasterSelectionReadiness;
import java.util.UUID;

public class MasterRetryOperationTest
{
    @Test public void retryPublicationRetainsExactResponseAndHistory()
    { MasterRetryOperationChecks.main(new String[0]); }

    @Test(expected = IllegalArgumentException.class)
    public void schema19RefusesLossyRetryEncoding()
    {
        var head = MasterRetryContinuationChecks.fixture(1);
        var request = MasterRetryOperationProposal.prepare(head, new UUID(90, 1),
            MasterRetryOperationProposalChecks.result(head,
                MasterSelectionReadiness.Availability.ASSIGNABLE, 0, "next"));
        new RunCodec(new com.google.gson.Gson()).encode(MasterRetryOperation.prepare(head, request));
    }
}
