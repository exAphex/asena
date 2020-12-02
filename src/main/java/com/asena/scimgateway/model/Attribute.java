package com.asena.scimgateway.model;

public class Attribute {
    public enum AttributeType {
        STRING, INTEGER, BOOLEAN, REFERENCE
    }

    private long id;
    private String key;
    private String value;
    private String description;
    private AttributeType type;
    private boolean isEncrypted;
    private Script transformation;

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public Script getTransformation() {
        return transformation;
    }

    public void setTransformation(Script transformation) {
        this.transformation = transformation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public void setEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

}