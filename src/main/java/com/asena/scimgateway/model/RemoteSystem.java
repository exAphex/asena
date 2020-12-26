package com.asena.scimgateway.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private Set<Attribute> writeMappings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "writenameid_id", referencedColumnName = "id")
    private Attribute writeNameId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConnectionProperty> properties;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serviceuser_id", referencedColumnName = "id")
    private User serviceUser;

    @NotBlank(message = "System type is mandatory")
    private String type;

    public RemoteSystem() {
    }

    public Attribute getWriteNameId() {
        return writeNameId;
    }

    public void setWriteNameId(Attribute writeNameId) {
        this.writeNameId = writeNameId;
    }

    public User getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(User serviceUser) {
        this.serviceUser = serviceUser;
    }

    public Set<Attribute> getWriteMappings() {
        return writeMappings;
    }

    public void setWriteMappings(Set<Attribute> writeMappings) {
        this.writeMappings = writeMappings;
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

    public RemoteSystem addWriteMapping(Attribute a) {
        if (this.writeMappings == null) {
            this.writeMappings = new HashSet<>();
        }

        if (a != null) {
            writeMappings.add(a);
        }
        return this;
    }

    public void deleteWriteMapping(Attribute a) {
        writeMappings.remove(a);
    }

    public void deleteProperty(ConnectionProperty cp) {
        properties.remove(cp);
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