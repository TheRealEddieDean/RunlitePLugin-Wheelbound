package com.wheelbound.persistence;

import org.junit.Test;

public class GrandActivationEnvelopeTest
{
    @Test public void retainsOriginalPayloadAndRefusesPrematurePersistence()
    { GrandActivationEnvelopeChecks.main(new String[0]); }
}
