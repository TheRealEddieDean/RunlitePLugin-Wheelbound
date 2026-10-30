package com.wheelbound.domain;

import org.junit.Test;

public class SealedGrandFateTest
{
    @Test public void restoresOriginalEvidenceAndRejectsForeignOrChangedRequests()
    { SealedGrandFateChecks.main(new String[0]); }
}
