package com.michalz.cloudintegrationtesting;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(initializers = {ExamplaryS3IntegrationTest.Initializer.class})
//@TestPropertySource
public class ExamplaryS3IntegrationTest extends BaseIntegrationTest {




    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "TODO"
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
