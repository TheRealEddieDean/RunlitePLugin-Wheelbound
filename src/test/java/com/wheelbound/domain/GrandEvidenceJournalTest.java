package com.wheelbound.domain;

import org.junit.Test;

public class GrandEvidenceJournalTest
{
    @Test public void preservesFullReplayAndRejectsReceiptCollisions()
    { GrandEvidenceJournalChecks.main(new String[0]); }
}
