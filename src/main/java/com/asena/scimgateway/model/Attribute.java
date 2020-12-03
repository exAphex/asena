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

    private String source;
    private String destination;
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private AttributeType type;
    private boolean isEncrypted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Script transformation;

    public Attribute(String source, String destination, String description) {
        this.source = source;
        this.destination = destination;
        this.description = description;
    }

    public Attribute() {}

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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