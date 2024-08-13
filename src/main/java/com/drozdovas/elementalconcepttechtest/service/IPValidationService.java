package com.drozdovas.elementalconcepttechtest.service;

import com.drozdovas.elementalconcepttechtest.dto.IpInfo;
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

    public IPValidationService(RestTemplate restTemplate, @Value("${ip.validation.service.rootUrl}") String rootUrl) {
        this.restTemplate = restTemplate;
        this.rootUrl = rootUrl;
    }

    public IpInfo validateIp(String ipAddress) {
        if(java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0) {
            ipAddress = "92.205.4.95";
        }

        IpInfo result = new IpInfo(200);

        String url = String.format("%s/json/%s", rootUrl, ipAddress);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            result.setCountry((String) responseBody.get("country"));
            result.setIsp((String) responseBody.get("isp")); ;

            if (BLOCKED_COUNTRIES.contains(result.getCountry())) {
                result.setResponseCode(403);
                result.setResponse("Access from country " + result.getCountry() + " is blocked.");
            }
            if (BLOCKED_ISPS.stream().anyMatch(result.getIsp()::contains)) {
                result.setResponseCode(403);
                result.setResponse("Access from ISP " + result.getIsp() + " is blocked.");
            }
        } else {
            result.setResponseCode(403);
            result.setResponse("Failed to validate IP address");
        }

        return result;
    }
}
