package com.github.robertnetz.dogstatsd;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AbstractIntegrationTest.Config.class})
public abstract class AbstractIntegrationTest {

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class Config {

    }
}
