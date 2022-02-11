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
import javax.persistence.Transient;
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

    @Transient
    private Set<Attribute> attributes;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EntryTypeMapping> entryTypeMappings;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConnectionProperty> properties;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serviceuser_id", referencedColumnName = "id")
    private User serviceUser;

    @NotBlank(message = "System type is mandatory")
    private String type;

    public RemoteSystem() {
    }

    public User getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(User serviceUser) {
        this.serviceUser = serviceUser;
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

    public RemoteSystem addEntryTypeMapping(EntryTypeMapping em) {
        if (this.entryTypeMappings == null) {
            this.entryTypeMappings = new HashSet<>();
        }

        if ((em != null) && (!isEntryTypeMappingDuplicate(em))) {
            this.entryTypeMappings.add(em);
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

    private boolean isEntryTypeMappingDuplicate(EntryTypeMapping em) {
        if ((em == null) || (em.getName() == null)) {
            return false;
        }

        if (this.entryTypeMappings == null) {
            return false;
        }

        for (EntryTypeMapping e : this.entryTypeMappings) {
            if (em.getName().equals(e.getName())) {
                return true;
            }
        }

        return false;
    }

    public void deleteProperty(ConnectionProperty cp) {
        properties.remove(cp);
    }

    public void deleteEntryTypeMapping(EntryTypeMapping em) {
        this.entryTypeMappings.remove(em);
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

    public Set<EntryTypeMapping> getEntryTypeMappings() {
        return entryTypeMappings;
    }

    public void setProperties(Set<ConnectionProperty> properties) {
        this.properties = properties;
    }

    public void setEntryTypeMappings(Set<EntryTypeMapping> entryTypeMappings) {
        this.entryTypeMappings = entryTypeMappings;
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