package com.asena.scimgateway.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "remotesystems")
public class RemoteSystem {

    @Id
    private String id;

    @Column(unique = true)
    @NotBlank(message = "System name is mandatory")
    private String name;
    private String description;
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attribute> attributes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConnectionProperty> properties;

    @NotBlank(message = "System type is mandatory")
    private String type;

    public RemoteSystem() {}

    public RemoteSystem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RemoteSystem addProperty(ConnectionProperty cp) {
        if (this.properties == null) {
            this.properties = new HashSet<>();
        }

        if (cp != null) {
            properties.add(cp);
        }
        return this;
    }

    public RemoteSystem addAttribute(Attribute a) {
        if (this.attributes == null) {
            this.attributes = new HashSet<>();
        }

        if (a != null) {
            attributes.add(a);
        }
        return this;
    }

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

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
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