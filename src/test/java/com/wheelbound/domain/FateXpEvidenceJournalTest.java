package com.wheelbound.domain;

import org.junit.Test;

public class FateXpEvidenceJournalTest
{
    @Test public void bindsObservationsAndReplaysWithoutDoubleCounting()
    { FateXpEvidenceJournalChecks.main(new String[0]); }
}
