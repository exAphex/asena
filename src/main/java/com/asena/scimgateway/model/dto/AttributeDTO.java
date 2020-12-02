package com.asena.scimgateway.model.dto;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.Attribute.AttributeType;

public class AttributeDTO {
    private long id;
    private String key;
    private String value;
    private String description;
    private AttributeType type;
    private boolean isEncrypted;
    private ScriptDTO transformation;

    public static AttributeDTO toDTO(Attribute a) {
        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setId(a.getId());
        aDTO.setKey(a.getKey());
        aDTO.setValue(a.getValue());
        aDTO.setDescription(a.getDescription());
        aDTO.setType(a.getType());
        aDTO.setEncrypted(a.isEncrypted());
        aDTO.setTransformation(ScriptDTO.toDTO(a.getTransformation()));
        return aDTO;
    }

    public Attribute fromDTO() {
        Attribute a = new Attribute();
        a.setId(this.id);
        a.setKey(this.key);
        a.setValue(this.value);
        a.setDescription(this.description);
        a.setType(this.type);
        a.setEncrypted(this.isEncrypted);
        a.setTransformation(this.transformation.fromDTO());
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