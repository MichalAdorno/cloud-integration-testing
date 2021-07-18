package com.michalz.cloudintegrationtesting;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@ConfigurationProperties("infrastructure.aws")
@ConstructorBinding
@Validated
public class AwsConfigurationProperties {

    private final URI s3Endpoint;
    private final URI sqsEndpoint;
    @NotBlank
    private final String region;
    private final String akid;
    private final String secret;

    public AwsConfigurationProperties(URI s3Endpoint, URI sqsEndpoint, String region, String akid, String secret) {
        this.s3Endpoint = s3Endpoint;
        this.sqsEndpoint = sqsEndpoint;
        this.region = region;
        this.akid = akid;
        this.secret = secret;
    }

    public URI getS3Endpoint() {
        return this.s3Endpoint;
    }

    public URI getSqsEndpoint() {
        return this.sqsEndpoint;
    }

    public String getRegion() {
        return this.region;
    }

    public String getAkid() {
        return this.akid;
    }

    public String getSecret() {
        return this.secret;
    }
}
