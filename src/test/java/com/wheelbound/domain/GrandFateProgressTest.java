package com.wheelbound.domain;

import org.junit.Test;

public class GrandFateProgressTest
{
    @Test public void restoresOnlySealBoundRevisionCheckedUniqueEvidenceReferences()
    { GrandFateProgressChecks.main(new String[0]); }

    @Test public void extendsProgressMonotonicallyWithExactReferenceReplay()
    { GrandProgressExtensionChecks.main(new String[0]); }
}
