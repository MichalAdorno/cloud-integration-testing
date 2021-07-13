package com.michalz.cloudintegrationtesting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.UUID;

@Testcontainers
@ContextConfiguration(initializers = {ExamplarySqsTest.Initializer.class})
@Slf4j
public class ExamplarySqsTest extends IntegrationTestBase {

    static String QUEUE_NAME = UUID.randomUUID().toString();
    static String testSqsQueue;
    static SqsClient testSqsClient;
    static String endpointOverride;

    @BeforeAll
    static void setUpTestSqs() throws URISyntaxException {
        endpointOverride = LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.S3).getServiceEndpoint();
        testSqsClient = SqsClient.builder()
                .region(Region.EU_CENTRAL_1)
                .endpointOverride(new URI(endpointOverride))
                .build();
        log.info("[LOCALSTACK] SQS endpoint override {}", endpointOverride);
        testSqsQueue = testSqsClient.createQueue(CreateQueueRequest.builder()
                .queueName(QUEUE_NAME)
                .build())
                .queueUrl();
        log.info("[LOCALSTACK] Test queue: {}", QUEUE_NAME);
    }

    @Test
    void testMessageSendAndReceive(){
        //given
        var message = "This is a test message";
        //send
        getWebTestClient()
                .post()
                .uri("/aws/sqs?m={message}&q={queueName}", message, QUEUE_NAME)
                .exchange()
                .expectStatus().isOk();

        //receive
        getWebTestClient()
                .get()
                .uri("/aws/sqs?q={queueName}", QUEUE_NAME)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(message);

    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    String.format(
                            "infrastructure.aws.queue-name=%s",
                            QUEUE_NAME
                    )
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
