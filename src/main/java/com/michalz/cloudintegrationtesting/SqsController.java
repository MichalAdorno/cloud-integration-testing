package com.michalz.cloudintegrationtesting;


import com.amazonaws.services.sqs.AmazonSQS;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SqsController {

    @Autowired
    public AmazonSQS amazonSQS;
    @Autowired
    public AwsConfigurationProperties awsConfigurationProperties;

    @PostMapping("/aws/sqs")
    public String sendToQueue(@RequestParam String messageBody,
                              @RequestParam String queueName) {
        log.info("Message body {}", messageBody);
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        log.info("Queue name {}", queueUrl);
        var response = amazonSQS.sendMessage(queueUrl, messageBody);
        log.info("Message response.messageId {}", response.getMessageId());
        log.info("Message response.sequenceNumber {}", response.getSequenceNumber());
        log.info("Message response {}", response);
        return response.toString();
    }

    @GetMapping("/aws/sqs")
    public String receiveFromQueue(@RequestParam String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        log.info("Queue name {}", queueUrl);
        var response = amazonSQS.receiveMessage(queueUrl);

        if (response.getMessages() == null || response.getMessages().isEmpty()) {
            return "";
        }
        var message = response.getMessages().get(0);
        //only one message in this example
        if (message == null) {
            return "";
        }
        log.info("Message attributesAsStrings {}", message.getAttributes());
        log.info("Message messageId {}", message.getMessageId());
        log.info("Message body{}", message.getBody());
        log.info("Message {}", message);
        return message.getBody();
    }
}
