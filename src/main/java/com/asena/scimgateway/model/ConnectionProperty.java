package com.asena.scimgateway.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "connectionproperties")
public class ConnectionProperty {
    public enum ConnectionPropertyType {
        STRING, INT, BOOLEAN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connectionproperties_seq")
    @SequenceGenerator(name = "connectionproperties_seq", sequenceName = "connectionproperties_sequence", allocationSize = 1)
    private long id;

    @NotBlank(message = "Connection property is mandatory")
    private String key;
    private String value;
    private String description;
    private boolean encrypted;

    @Enumerated(EnumType.ORDINAL)
    private ConnectionPropertyType type;

    public ConnectionProperty(String key, String value, String description, boolean encrypted, ConnectionPropertyType type) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.encrypted = encrypted;
        this.type = type;
    }

    public ConnectionProperty() {}

    public ConnectionPropertyType getType() {
        return type;
    }

    public void setType(ConnectionPropertyType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean isEncrypted) {
        this.encrypted = isEncrypted;
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