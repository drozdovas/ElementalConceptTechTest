package com.drozdovas.elementalconcepttechtest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class IPValidationService {

    private static final List<String> BLOCKED_COUNTRIES = List.of("China", "Spain", "United States");
    private static final List<String> BLOCKED_ISPS = List.of("Amazon", "Google", "Microsoft");

    private final RestTemplate restTemplate;
    private final String rootUrl;

    // Constructor
    public IPValidationService(RestTemplate restTemplate, @Value("${ip.validation.service.rootUrl}") String rootUrl) {
        this.restTemplate = restTemplate;
        this.rootUrl = rootUrl;
    }

    public void validateIp(String ipAddress) {
        if(java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0) {
            ipAddress = "92.205.4.95";
        }

        String url = String.format("%s/json/%s", rootUrl, ipAddress);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        System.out.println(url);
        System.out.println(response);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            String country = (String) responseBody.get("country");
            String isp = (String) responseBody.get("isp");

            if (BLOCKED_COUNTRIES.contains(country)) {
                throw new RuntimeException("Access from country " + country + " is blocked.");
            }
            if (BLOCKED_ISPS.stream().anyMatch(isp::contains)) {
                throw new RuntimeException("Access from ISP " + isp + " is blocked.");
            }
        } else {
            throw new RuntimeException("Failed to validate IP address");
        }
    }
}
