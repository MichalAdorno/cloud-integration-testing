package com.michalz.cloudintegrationtesting;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {InfrastructureTestBase.Initializer.class})
public class IntegrationTestBase extends InfrastructureTestBase {

    @LocalServerPort
    int webServerPort;
    WebTestClient webTestClient;

    @BeforeEach
    void buildWebTestClient() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl(String.format("http://localhost:", webServerPort))
                .responseTimeout(Duration.ofMinutes(5))
                .build();
    }

    public WebTestClient getWebTestClient() {
        return webTestClient;
    }
}
