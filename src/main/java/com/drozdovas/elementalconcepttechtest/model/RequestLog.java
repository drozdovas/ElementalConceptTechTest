package com.drozdovas.elementalconcepttechtest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_logs")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID requestId;
    private String requestUri;
    private LocalDateTime requestTimestamp;
    private int responseCode;
    private String requestIpAddress;
    private String requestCountryCode;
    private String requestIpProvider;
    private long timeLapsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public LocalDateTime getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(LocalDateTime requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestIpAddress() {
        return requestIpAddress;
    }

    public void setRequestIpAddress(String requestIpAddress) {
        this.requestIpAddress = requestIpAddress;
    }

    public String getRequestCountryCode() {
        return requestCountryCode;
    }

    public void setRequestCountryCode(String requestCountryCode) {
        this.requestCountryCode = requestCountryCode;
    }

    public String getRequestIpProvider() {
        return requestIpProvider;
    }

    public void setRequestIpProvider(String requestIpProvider) {
        this.requestIpProvider = requestIpProvider;
    }

    public long getTimeLapsed() {
        return timeLapsed;
    }

    public void setTimeLapsed(long timeLapsed) {
        this.timeLapsed = timeLapsed;
    }
}