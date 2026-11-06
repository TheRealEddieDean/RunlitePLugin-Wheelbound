package com.wheelbound.domain;

import org.junit.Test;

public class FateKillEvidenceJournalTest
{
    @Test public void bindsObservationsAndReplaysWithoutDoubleCounting()
    { FateKillEvidenceJournalChecks.main(new String[0]); }
}
