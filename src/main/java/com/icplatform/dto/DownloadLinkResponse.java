package com.icplatform.dto;

public class DownloadLinkResponse {
    private String downloadLink;
    private String status;
    private String token;

    public DownloadLinkResponse(String downloadLink,String status) {
        this.downloadLink = downloadLink;
        this.status = status;
    }

    public DownloadLinkResponse(String downloadLink,String status,String token) {
        this.downloadLink = downloadLink;
        this.status = status;
        this.token = token;
    }
    public String getDownloadLink() {
        return downloadLink;
    }
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
