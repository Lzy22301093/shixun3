package com.icplatform.dto;

public class HomeworkResponse {

    private String message;
    private String newToken;

    public HomeworkResponse(String message, String newToken) {
        this.message = message;
        this.newToken = newToken;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getNewToken() {
        return newToken;
    }
    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }

}
