package com.drozdovas.elementalconcepttechtest;

import com.drozdovas.elementalconcepttechtest.service.IPValidationService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 8081)
@SpringBootTest
public class IPValidationServiceTest {

    private IPValidationService ipValidationService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    void setUp() {
        // Create the RestTemplate without autowiring the rootUrl
        RestTemplate restTemplate = restTemplateBuilder.build();

        // Pass the rootUrl manually to the service constructor
        ipValidationService = new IPValidationService(restTemplate, "http://localhost:8081");
    }

    @Test
    void validateIp_ipFromBlockedCountry_throwsException() {
        // Mock the external API call to ip-api.com for a blocked country (China)
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012")) // Exact URL match
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"China\", \"isp\": \"China Telecom\" }")));

        // Assert that an exception is thrown for a blocked country
        assertThrows(RuntimeException.class, () -> ipValidationService.validateIp("123.456.789.012"),
                "Access from country China is blocked.");
    }

    @Test
    void validateIp_ipFromBlockedISP_throwsException() {
        // Mock the external API call to ip-api.com for a blocked ISP (Amazon)
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012")) // Exact URL match
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"United Kingdom\", \"isp\": \"Amazon AWS\" }")));

        // Assert that an exception is thrown for a blocked ISP
        assertThrows(RuntimeException.class, () -> ipValidationService.validateIp("123.456.789.012"),
                "Access from ISP Amazon AWS is blocked.");
    }

    @Test
    void validateIp_ipFromAllowedCountryAndISP_doesNotThrowException() {
        // Mock the external API call to ip-api.com for an allowed country and ISP
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012")) // Exact URL match
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"United Kingdom\", \"isp\": \"BT\" }")));

        // Assert that no exception is thrown for an allowed country and ISP
        ipValidationService.validateIp("123.456.789.012");  // Should not throw an exception
    }
}
