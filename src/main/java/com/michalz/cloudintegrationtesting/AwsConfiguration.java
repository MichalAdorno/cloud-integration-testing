package com.michalz.cloudintegrationtesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Slf4j
public class AwsConfiguration {

    @Autowired
    public AwsConfigurationProperties awsConfigurationProperties;

    @Bean
    public SqsClient amazonSQS(final AwsConfigurationProperties awsConfigurationProperties) {
        var provider = EnvironmentVariableCredentialsProvider.create();
        log.info("[PROVIDER] accessKeyId = {}", provider.resolveCredentials().accessKeyId());
        log.info("[PROVIDER] secretAccessKey = {}", provider.resolveCredentials().secretAccessKey());
        log.info("[PROVIDER] awsConfigurationProperties.sqsEndpoint() = {}", awsConfigurationProperties.getSqsEndpoint().toString());
        var builder = SqsClient.builder()
                .region(Region.of(awsConfigurationProperties.getRegion()));

        if (awsConfigurationProperties.getSqsEndpoint() != null) {
            builder.endpointOverride(awsConfigurationProperties.getSqsEndpoint());
        }

        return builder.build();
    }
}