package com.michalz.cloudintegrationtesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.regions.Region;

@Slf4j
public class InfrastructureTestBase {
    /*
    CONTAINERS
     */
    public static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(
                    LocalStackContainer.Service.S3,
                    LocalStackContainer.Service.SQS)
            .withEnv("DEFAULT_REGION", Region.EU_CENTRAL_1.id());
//            .withEnv("AWS_PROFILE", "localstack");

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
            log.info("[LOCALSTACK] S3 test endpoint {}",
                    LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint());
            log.info("[LOCALSTACK] SQS test endpoint {}",
                    LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.SQS).getServiceEndpoint());
            TestPropertyValues.of(
                    String.format(
                            "infrastructure.aws.s3-endpoint=%s",
                            LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.S3)
                                    .getServiceEndpoint()
                    ),
                    String.format(
                            "infrastructure.aws.sqs-endpoint=%s",
                            LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.SQS)
                                    .getServiceEndpoint()
                    )
            );
        }

    }

}
