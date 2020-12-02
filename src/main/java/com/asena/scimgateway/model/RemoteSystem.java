package com.asena.scimgateway.model;

import java.util.Set;

import com.asena.scimgateway.model.Attribute.AttributeType;

public class RemoteSystem {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private Set<AttributeType> attributes;
    private Set<ConnectionProperty> properties;
    private String type;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<ConnectionProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<ConnectionProperty> properties) {
        this.properties = properties;
    }

    public Set<AttributeType> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AttributeType> attributes) {
        this.attributes = attributes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}