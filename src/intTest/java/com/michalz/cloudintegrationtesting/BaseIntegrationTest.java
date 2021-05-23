package com.michalz.cloudintegrationtesting;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.regions.Region;

public class BaseIntegrationTest {
    /*
    CONTAINERS
     */
    public static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(
                    LocalStackContainer.Service.S3,
                    LocalStackContainer.Service.SQS)
            .withEnv("DEFAULT_REGION", Region.EU_CENTRAL_1.id());

    /*
    CONTAINER INITIALIZATION FOR INTEGRATION TESTS
     */
    static {
        LOCAL_STACK_CONTAINER.start();
    }

    /*
     SPRING CONTEXT INITIALIZER
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    String.format(
                            "infrastructure.aws.s3-endpoint=%s",
                            LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.S3)
                                    .getServiceEndpoint()
                    )
            );
        }

    }

}
