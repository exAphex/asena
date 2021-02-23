package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.EntryTypeMappingDTO;

import org.junit.jupiter.api.Test;

public class EntryTypeMappingDTOTest {

    @Test
    void fromDTOTest() {
        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setDescription("testdesc");

        Set<AttributeDTO> attrDTO = new HashSet<>();
        attrDTO.add(aDTO);

        EntryTypeMappingDTO emDTO = new EntryTypeMappingDTO();
        emDTO.setName(null);
        emDTO.setReadMappings(null);
        emDTO.setWriteMappings(null);
      
        EntryTypeMapping em = emDTO.fromDTO();
        assertNull(em.getName());
        assertNull(em.getReadMappings());
        assertNull(em.getWriteMappings());

        emDTO.setName("User");
        emDTO.setReadMappings(attrDTO);
        emDTO.setWriteMappings(attrDTO);
        em = emDTO.fromDTO();
        assertEquals("User", em.getName());
        assertEquals(1, em.getReadMappings().size());
        assertEquals(1, em.getWriteMappings().size());
    }
    
    @Test
    void toDTOTest() {
        Attribute a = new Attribute();
        a.setDescription("testdesc");

        Set<Attribute> attr = new HashSet<>();
        attr.add(a);

        EntryTypeMapping em = new EntryTypeMapping(null);
        em.setReadMappings(null);
        em.setWriteMappings(null);

        EntryTypeMappingDTO emDTO = EntryTypeMappingDTO.toDTO(em);
        assertNull(emDTO.getName());
        assertNull(emDTO.getReadMappings());
        assertNull(emDTO.getWriteMappings());

        em.setName("User");
        em.setReadMappings(attr);
        em.setWriteMappings(attr);

        emDTO = EntryTypeMappingDTO.toDTO(em);
        assertEquals("User", emDTO.getName());
        assertEquals(1, emDTO.getReadMappings().size());
        assertEquals(1, emDTO.getWriteMappings().size());

        emDTO = EntryTypeMappingDTO.toDTO(null);
        assertNull(emDTO);
    }
}