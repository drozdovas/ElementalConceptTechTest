package com.drozdovas.elementalconcepttechtest;

import com.drozdovas.elementalconcepttechtest.dto.IpInfo;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8081)
@SpringBootTest
public class IPValidationServiceTest {

    private IPValidationService ipValidationService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ipValidationService = new IPValidationService(restTemplate, "http://localhost:8081");
    }

    @Test
    void validateIp_ipFromBlockedCountry_returns403() {
        // Mock the external API call to ip-api.com for a blocked country (China)
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"China\", \"isp\": \"China Telecom\" }")));

        // Validate IP and check response
        IpInfo ipInfo = ipValidationService.validateIp("123.456.789.012");

        assertEquals(403, ipInfo.getResponseCode());
        assertTrue(ipInfo.getResponse().contains("Access from country China is blocked."));
    }

    @Test
    void validateIp_ipFromBlockedISP_returns403() {
        // Mock the external API call to ip-api.com for a blocked ISP (Amazon)
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"United Kingdom\", \"isp\": \"Amazon AWS\" }")));

        // Validate IP and check response
        IpInfo ipInfo = ipValidationService.validateIp("123.456.789.012");

        assertEquals(403, ipInfo.getResponseCode());
        assertTrue(ipInfo.getResponse().contains("Access from ISP Amazon AWS is blocked."));
    }

    @Test
    void validateIp_ipFromAllowedCountryAndISP_returns200() {
        // Mock the external API call to ip-api.com for an allowed country and ISP
        WireMock.stubFor(get(urlEqualTo("/json/123.456.789.012"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"country\": \"United Kingdom\", \"isp\": \"BT\" }")));

        // Validate IP and check response
        IpInfo ipInfo = ipValidationService.validateIp("123.456.789.012");

        assertEquals(200, ipInfo.getResponseCode());
        assertEquals("United Kingdom", ipInfo.getCountry());
        assertEquals("BT", ipInfo.getIsp());
    }
}
