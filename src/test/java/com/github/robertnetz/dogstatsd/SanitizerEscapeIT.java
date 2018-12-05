package com.github.robertnetz.dogstatsd;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@ActiveProfiles("sanitizerEscape")
public class SanitizerEscapeIT extends AbstractIntegrationTest {

    @Autowired
    private NameSanitizer sanitizer;

    @Test
    public void testCustomSanitizerWasSet() {
        Assert.isInstanceOf(NameSanitizer.EscapeOnColon.class, sanitizer);
    }

}
