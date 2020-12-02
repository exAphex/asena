package com.asena.scimgateway.model;

import java.sql.Date;

public class Log {
    private long id;
    private Date timestamp;
    private String error;

    public long getId() {
        return id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }
}