package com.github.robertnetz.dogstatsd;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ApplicationStartupTest extends AbstractIntegrationTest {

    @Test
    @Ignore
    public void runForAWhile() throws InterruptedException {
        TimeUnit.MINUTES.sleep(1);
    }
}
