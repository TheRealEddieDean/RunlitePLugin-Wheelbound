package com.wheelbound.persistence;

import org.junit.Test;

public class MasterRetryOperationProposalTest
{
    @Test public void unresolvedOperationsRetainOriginalPrefixAndResponse()
    { MasterRetryOperationProposalChecks.main(new String[0]); }
}
