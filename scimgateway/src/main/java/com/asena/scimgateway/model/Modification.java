package com.asena.scimgateway.model;

public class Modification {
    public enum ModificationType {
        COMPLEX_ADD, COMPLEX_REMOVE, SIMPLE
    }

    private ModificationType type;
    private String attributeName;
    private Object value;

    public Modification(String attributeName, Object value, ModificationType type) {
        setAttributeName(attributeName);
        setValue(value);
        setType(type);
    }

    public Modification(String attributeName, Object value) {
        setAttributeName(attributeName);
        setValue(value);
        setType(ModificationType.SIMPLE);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public ModificationType getType() {
        return type;
    }

    public void setType(ModificationType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    
}