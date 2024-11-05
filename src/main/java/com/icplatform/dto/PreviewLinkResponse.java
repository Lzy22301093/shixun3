package com.icplatform.dto;

public class PreviewLinkResponse {
    private String previewLink;
    private String status;
    private String token;

    public PreviewLinkResponse(String previewLink, String status) {
        this.previewLink = previewLink;
        this.status = status;
    }

    public PreviewLinkResponse(String previewLink, String status, String token) {
        this.previewLink = previewLink;
        this.status = status;
        this.token = token;
    }
    public String getPreviewLink() {
        return previewLink;
    }
    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
