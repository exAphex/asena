package com.asena.scimgateway.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "attributes")
public class Attribute {
    public enum AttributeType {
        STRING, INTEGER, BOOLEAN, REFERENCE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attributes_seq")
    @SequenceGenerator(name = "attributes_seq", sequenceName = "attributes_sequence", allocationSize = 1)
    private long id;

    @NotBlank(message = "Key is mandatory")
    private String key;
    private String value;
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private AttributeType type;
    private boolean isEncrypted;

    @ManyToOne(fetch=FetchType.LAZY)
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