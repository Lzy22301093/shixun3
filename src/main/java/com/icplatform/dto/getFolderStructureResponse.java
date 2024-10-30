package com.icplatform.dto;

public class getFolderStructureResponse {

    private String path;
    private String newToken;

    public getFolderStructureResponse(String path, String newToken) {
        this.path = path;
        this.newToken = newToken;
    }
}
