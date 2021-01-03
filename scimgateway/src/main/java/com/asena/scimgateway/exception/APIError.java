package com.asena.scimgateway.exception;

import java.time.Instant;

public class APIError {
    private int status;
    private String message;
    private Instant timestamp;
    private String error;

    public APIError(int status, String error, String message) {
        this.setStatus(status);
        this.setError(error);
        this.setMessage(message);
        this.setTimestamp(Instant.now());
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}