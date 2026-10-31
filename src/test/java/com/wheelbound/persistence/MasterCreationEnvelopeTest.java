package com.wheelbound.persistence;

import org.junit.Test;

public class MasterCreationEnvelopeTest
{
    @Test public void retainsExactOriginalAndRefusesPrematureAggregateAdmission()
    { MasterCreationEnvelopeChecks.main(new String[0]); }
}
