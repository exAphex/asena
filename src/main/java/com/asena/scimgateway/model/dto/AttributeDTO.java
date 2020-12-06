package com.asena.scimgateway.model.dto;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.Attribute.AttributeType;

public class AttributeDTO {
    private long id;
    private String destination;
    private String source;
    private String description;
    private AttributeType type;
    private boolean isEncrypted;
    private ScriptDTO transformation;

    public static AttributeDTO toDTO(Attribute a) {
        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setId(a.getId());
        aDTO.setDestination(a.getDestination());
        aDTO.setSource(a.getSource());
        aDTO.setDescription(a.getDescription());
        aDTO.setType(a.getType());
        aDTO.setEncrypted(a.isEncrypted());
        if (a.getTransformation() != null) {
            aDTO.setTransformation(ScriptDTO.toDTO(a.getTransformation()));
        }
        
        return aDTO;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Attribute fromDTO() {
        Attribute a = new Attribute();
        a.setId(this.id);
        a.setDestination(this.destination);
        a.setSource(this.source);
        a.setDescription(this.description);
        a.setType(this.type);
        a.setEncrypted(this.isEncrypted);
        if (this.transformation != null) {
            a.setTransformation(this.transformation.fromDTO());
        }
        return a;
    }

    public long getId() {
        return id;
    }

    public ScriptDTO getTransformation() {
        return transformation;
    }

    public void setTransformation(ScriptDTO transformation) {
        this.transformation = transformation;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }
}