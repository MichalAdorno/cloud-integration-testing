package com.michalz.cloudintegrationtesting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ContextConfiguration(initializers = {InfrastructureTestBase.Initializer.class})
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
        log.info("[INTEGRATION TEST] SQS endpoint override {}", endpointOverride);
        testSqsQueue = testSqsClient.createQueue(CreateQueueRequest.builder()
                .queueName(QUEUE_NAME)
                .build())
                .queueUrl();
        log.info("[INTEGRATION TEST] Test queue: {}", testSqsQueue);
        log.info("[INTEGRATION TEST] Test queue: {}", QUEUE_NAME);

        String queueUrl = testSqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(QUEUE_NAME)
                .build())
                .queueUrl();
        log.info("[INTEGRATION TEST get queue url] Test queue: {}", queueUrl);

    }

    @Test
    void testMessageSendAndReceive() {
        //given
        var message = "This is a test message";
        //send
        getWebTestClient()
                .post()
                .uri("/aws/sqs?messageBody={message}&queueName={queueName}", message, QUEUE_NAME)
                .exchange()
                .expectStatus().isOk();

        //receive
        var result = getWebTestClient()
                .get()
                .uri("/aws/sqs?queueName={queueName}", QUEUE_NAME)
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult();
        assertThat(new String(result.getResponseBodyContent())).isEqualTo(message);
        log.info("Received message payload: {}", new String(result.getResponseBodyContent()));
    }
}
