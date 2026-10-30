package com.wheelbound.domain;

import org.junit.Test;

public class MasterSelectionCreationReceiptTest
{
    @Test public void originalCreationPayloadRequiresExactReplay()
    { MasterSelectionCreationChecks.main(new String[0]); }
}
