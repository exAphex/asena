package com.asena.scimgateway.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;

public class EntryTypeMappingDTO {

    private long id;
    private String name;
    private Set<AttributeDTO> writeMappings;
    private Set<AttributeDTO> readMappings;
    
    public static EntryTypeMappingDTO toDTO(EntryTypeMapping em) {
        EntryTypeMappingDTO emDTO = new EntryTypeMappingDTO();

        if (em == null) {
            return null;
        }

        emDTO.setId(em.getId());
        emDTO.setName(em.getName());

        if (em.getWriteMappings() != null) {
            emDTO.writeMappings = new HashSet<>();
            for (Attribute a : em.getWriteMappings()) {
                emDTO.writeMappings.add(AttributeDTO.toDTO(a));
            }
        }

        if (em.getReadMappings() != null) {
            emDTO.readMappings = new HashSet<>();
            for (Attribute a : em.getReadMappings()) {
                emDTO.readMappings.add(AttributeDTO.toDTO(a));
            }
        }

        return emDTO;
    }

    public EntryTypeMapping fromDTO() {
        EntryTypeMapping em = new EntryTypeMapping();
        em.setId(getId());
        em.setName(getName());
        
        if (getWriteMappings() != null) {
            for (AttributeDTO a : getWriteMappings()) {
                em.addWriteMapping(a.fromDTO());
            }
        }

        if (getReadMappings() != null) {
            for (AttributeDTO a : getReadMappings()) {
                em.addReadMapping(a.fromDTO());
            }
        }
        
        return em;
    }

    public long getId() {
        return id;
    }

    public Set<AttributeDTO> getReadMappings() {
        return readMappings;
    }

    public void setReadMappings(Set<AttributeDTO> readMappings) {
        this.readMappings = readMappings;
    }

    public Set<AttributeDTO> getWriteMappings() {
        return writeMappings;
    }

    public void setWriteMappings(Set<AttributeDTO> writeMappings) {
        this.writeMappings = writeMappings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }
}