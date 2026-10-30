package com.wheelbound.domain;

import org.junit.Test;

public class MasterSelectionProvenanceTest
{
    @Test public void frozenTranscriptPreservesDrawsReasonsAndValidLandingBoundary()
    { MasterSelectionProvenanceChecks.main(new String[0]); }

    @Test public void suppliedDrawsAdvanceOnlyTheExpectedUnresolvedPrefix()
    { MasterSelectionAppendChecks.main(new String[0]); }
}
