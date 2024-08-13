package com.drozdovas.elementalconcepttechtest.dto;

public class IpInfo {
    private String country;
    private String isp;
    private String response;
    private int responseCode;

    public IpInfo(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getCountry() {
        return country;
    }

    public String getIsp() {
        return isp;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
