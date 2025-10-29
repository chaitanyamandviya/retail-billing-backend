package com.vandana.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse(String message, Map<String, String> errors, LocalDateTime timestamp, int status) {
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
