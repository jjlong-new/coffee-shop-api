package com.jjcoffee.coffee_shop_api.service;

// JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

// Spring Boot Test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// MockServer
import org.mockserver.integration.ClientAndServer;
import org.mockserver.client.MockServerClient;

// Assertions
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

// MockServer request/response builders
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

// Java standard libraries
import java.math.BigDecimal;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PricingServiceTest {

    private ClientAndServer mockServer;
    private MockServerClient mockClient;

    @Autowired
    private PricingService pricingService;

    @BeforeAll
    void startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
        mockClient = new MockServerClient("localhost", 1080);
    }

    @AfterAll
    void stopMockServer() {
        mockClient.close();
        mockServer.stop();
    }

    @Test
    void shouldParseBasePriceFromMockServer() {

        mockClient
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/pricing/1")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "coffeeId": 1,
                        "basePrice": 2.75,
                        "currency": "USD"
                        }
                    """)
            );

        Optional<BigDecimal> result = pricingService.getPriceForCoffee(1L);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("2.75"), result.get());
    }

}


/** MockServer Test Instructions
Overview

MockServer is used to simulate the external Pricing API during testing.
It allows tests to run without depending on Mockoon or any real external service.

How it works
MockServer starts automatically on port 1080 during tests
It intercepts calls to:
GET /pricing/{coffeeId}

Returns a mocked JSON response:
{
  "coffeeId": 1,
  "basePrice": 2.75,
  "currency": "USD"
}

Running the tests
Run the following command:

mvn test

Or run directly from your IDE (Run Test).

Configuration
Test profile is used: application-test.properties

Base URL is set to:
http://localhost:1080

Important Notes
No need to start MockServer manually (handled in test lifecycle)
Ensure port 1080 is not used by another application
Mockoon or any external API should be stopped to avoid confusion **/
