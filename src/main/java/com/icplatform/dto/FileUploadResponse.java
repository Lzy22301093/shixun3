package com.icplatform.dto;

public class FileUploadResponse {
    private String status;  // 成功与否
    private String message;//输出信息
    private String newToken;  //新的token

    public FileUploadResponse(String status,String message) {
        this.status = status;
        this.message = message;
    }

    public FileUploadResponse(String status,String message , String newToken) {
        this.status = status;
        this.message = message;
        this.newToken = newToken;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getNewToken() {
        return newToken;
    }
    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
}
