package com.wheelbound.domain;

import org.junit.Test;

public class ConsecutivePenanceTest
{
    @Test public void resetsOnlyOnCleanCompletionAndEscalatesConsecutively()
    { ConsecutivePenanceChecks.main(new String[0]); }
}
