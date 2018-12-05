package com.github.robertnetz.dogstatsd;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class SanitizerDefaultIT extends AbstractIntegrationTest {

    @Autowired
    private NameSanitizer sanitizer;

    @Test
    public void testCustomSanitizerWasSet() {
        Assert.isInstanceOf(NameSanitizer.RaiseOnColon.class, sanitizer);
    }

}
