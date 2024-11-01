package com.icplatform.dto;

import org.springframework.core.io.Resource;

public class DownloadResponse {
    private String status;
    private String message;
    private String token;
    private Resource resource;

    public DownloadResponse(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public DownloadResponse(String status, String message, String token, Resource resource) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.resource = resource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

