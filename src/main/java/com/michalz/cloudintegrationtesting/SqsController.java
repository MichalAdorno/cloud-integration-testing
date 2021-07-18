package com.michalz.cloudintegrationtesting;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@RestController
@Log4j2
public class SqsController {

    @Autowired
    public SqsClient sqsClient;
    @Autowired
    public AwsConfigurationProperties awsConfigurationProperties;

    @PostMapping("/aws/sqs")
    public String sendToQueue(@RequestParam String messageBody,
                              @RequestParam String queueName) {
        log.info("[POST ENDPOINT] Message body {}", messageBody);
        String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build())
                .queueUrl();
        log.info("[POST ENDPOINT] qeue name {}", queueUrl);
        var request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        var response = sqsClient.sendMessage(request);
        log.info("[POST ENDPOINT] Message response.messageId {}", response.messageId());
        log.info("[POST ENDPOINT] Message response.sequenceNumber {}", response.sequenceNumber());
        log.info("[POST ENDPOINT] Message response {}", response);
        return response.toString();
    }

    @GetMapping("/aws/sqs")
    public String receiveFromQueue(@RequestParam String queueName) {
        String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build())
                .queueUrl();
        log.info("[GET ENDPOINT] Queue name {}", queueUrl);

        var response = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .build());
        if (!response.hasMessages()) {
            return "";
        }
        var message = response.messages().get(0); //only one message in this example
        if (message == null) {
            return "";
        }
        log.info("[GET ENDPOINT] Message messageAttributes {}", message.messageAttributes());
        log.info("[GET ENDPOINT] Message messageId {}", message.messageId());
        log.info("[GET ENDPOINT] Message body{}", message.body());
        log.info("[GET ENDPOINT] Message {}", message);
        return message.body();
    }
}
