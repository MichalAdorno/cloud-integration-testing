package com.michalz.cloudintegrationtesting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

@Configuration
public class AwsConfiguration {

    @Bean
    SqsClient sqsClient(final AwsConfigurationProperties awsConfigurationProperties){
        final SqsClientBuilder builder = SqsClient.builder()
                .region(Region.of(awsConfigurationProperties.getRegion()));

        if(awsConfigurationProperties.getSqsEndpoint() != null) {
            builder.endpointOverride(awsConfigurationProperties.getSqsEndpoint());
        }

        return builder.build();
    }
}