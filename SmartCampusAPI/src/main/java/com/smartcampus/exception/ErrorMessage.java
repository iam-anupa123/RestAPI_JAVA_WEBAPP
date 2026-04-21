package com.smartcampus.exception;

public class ErrorMessage {
    private String message;
    private int status;

    public ErrorMessage() {} // Required default constructor

    public ErrorMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    // Standard Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}