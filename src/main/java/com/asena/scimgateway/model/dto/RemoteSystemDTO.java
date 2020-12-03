package com.asena.scimgateway.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;

public class RemoteSystemDTO {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private Set<AttributeDTO> attributes;
    private Set<ConnectionPropertyDTO> properties;
    private String type;

    public static RemoteSystemDTO toDTO(RemoteSystem rs) {
        RemoteSystemDTO rsDTO = new RemoteSystemDTO();
        rsDTO.setId(rs.getId());
        rsDTO.setName(rs.getName());
        rsDTO.setDescription(rs.getDescription());
        rsDTO.setActive(rs.isActive());
        rsDTO.setType(rs.getType());
        
        if (rs.getAttributes() != null) {
            rsDTO.attributes = new HashSet<>();
            for (Attribute a : rs.getAttributes()) {
                
                rsDTO.attributes.add(AttributeDTO.toDTO(a));
            }
        }
        
        if (rs.getProperties() != null) {
            rsDTO.properties = new HashSet<>();
            for (ConnectionProperty cp : rs.getProperties()) {
                rsDTO.properties.add(ConnectionPropertyDTO.toDTO(cp));
            }
        }
        
        return rsDTO;
    }

    public RemoteSystem fromDTO() {
        RemoteSystem rs = new RemoteSystem();
        rs.setId(this.id);
        rs.setName(this.name);
        rs.setDescription(this.description);
        rs.setActive(this.active);
        rs.setType(this.type);
        
        if (this.attributes != null) {
            for (AttributeDTO a : this.attributes) {
                rs.addAttribute(a.fromDTO());
            }
        }
        
        if (this.properties != null) {
            for (ConnectionPropertyDTO cp : this.properties) {
                rs.addProperty(cp.fromDTO());
            }
        }
        
        return rs;
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

    public Set<ConnectionPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(Set<ConnectionPropertyDTO> properties) {
        this.properties = properties;
    }

    public Set<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AttributeDTO> attributes) {
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