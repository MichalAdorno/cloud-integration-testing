package com.michalz.cloudintegrationtesting;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;

@Configuration
@Slf4j
public class AwsConfiguration {

    @Autowired
    public AwsConfigurationProperties awsConfigurationProperties;

    @Bean
    public AmazonSQS amazonSQS(final AwsConfigurationProperties awsConfigurationProperties) {
        var provider = EnvironmentVariableCredentialsProvider.create();
        log.info("[PROVIDER] accessKeyId = {}", provider.resolveCredentials().accessKeyId());
        log.info("[PROVIDER] secretAccessKey = {}", provider.resolveCredentials().secretAccessKey());
        var builder = AmazonSQSClientBuilder.standard()
                .withRegion(awsConfigurationProperties.getRegion());

        if (awsConfigurationProperties.getSqsEndpoint() != null) {
            builder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(
                            awsConfigurationProperties.getSqsEndpoint().toString(),
                            awsConfigurationProperties.getRegion()
                    )
            );
        }

        return builder.build();
    }
}