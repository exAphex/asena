package com.asena.scimgateway.model;

public class ConnectionProperty {
    public enum ConnectionPropertyType {
        STRING, INT, BOOLEAN
    }

    private long id;
    private String key;
    private String value;
    private String description;
    private boolean isEncrypted;

    public long getId() {
        return id;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setId(long id) {
        this.id = id;
    }
}