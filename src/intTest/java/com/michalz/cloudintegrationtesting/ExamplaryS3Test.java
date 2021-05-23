package com.michalz.cloudintegrationtesting;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;

@Testcontainers
@ContextConfiguration(initializers = {ExamplaryS3Test.Initializer.class})
//@TestPropertySource
public class ExamplaryS3Test extends IntegrationTestBase {

    static String QUEUE_NAME = UUID.randomUUID().toString();
    static String testSqsQueue;
    static SqsClient testSqsClient;

    Random randomGenerator = new Random();

    @BeforeAll
    static void setUpTestSqs() throws URISyntaxException {
        testSqsClient = SqsClient.builder()
                .region(Region.EU_CENTRAL_1)
                .endpointOverride(new URI(LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint()))
                .build();
        testSqsQueue = testSqsClient.createQueue(CreateQueueRequest.builder()
                .queueName(QUEUE_NAME)
                .build())
                .queueUrl();
    }


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "TODO"
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
