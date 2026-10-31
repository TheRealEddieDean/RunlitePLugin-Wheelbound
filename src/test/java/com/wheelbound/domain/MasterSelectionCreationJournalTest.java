package com.wheelbound.domain;

import org.junit.Test;

public class MasterSelectionCreationJournalTest
{
    @Test public void retainedOriginalRequestsRequireOrderedExactReplay()
    { MasterSelectionCreationJournalChecks.main(new String[0]); }
}
