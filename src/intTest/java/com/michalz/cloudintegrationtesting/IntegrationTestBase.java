package com.michalz.cloudintegrationtesting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {InfrastructureTestBase.Initializer.class})
@Slf4j
public class IntegrationTestBase extends InfrastructureTestBase {

    @LocalServerPort
    int webServerPort;
    WebTestClient webTestClient;

    @BeforeEach
    void buildWebTestClient() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl(String.format("http://localhost:%s", webServerPort))
                .responseTimeout(Duration.ofMinutes(5))
                .build();
        log.info("[SYSTEM UNDER TEST] Web server port: {}", webServerPort);
    }

    public WebTestClient getWebTestClient() {
        return webTestClient;
    }

}
