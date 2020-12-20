package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.Attribute.AttributeType;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.ScriptDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AttributeDTOTest {
    
    @Test
    void fromDTOTest() {
        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setId(0);
        aDTO.setDestination("testdest");
        aDTO.setSource("testsrc");
        aDTO.setDescription("testdesc");
        aDTO.setType(AttributeType.BOOLEAN);
        aDTO.setEncrypted(true);
        aDTO.setTransformation(null);

        Attribute a = aDTO.fromDTO();

        assertEquals(0, a.getId());
        assertEquals("testdest", a.getDestination());
        assertEquals("testsrc", a.getSource());
        assertEquals("testdesc", a.getDescription());
        assertEquals(AttributeType.BOOLEAN, a.getType());
        assertEquals(true, a.isEncrypted());
        assertNull(a.getTransformation());

        ScriptDTO sDTO = new ScriptDTO();
        sDTO.setContent("testcontent");
        sDTO.setName("testname");

        aDTO.setTransformation(sDTO);

        a = aDTO.fromDTO();

        assertEquals("testcontent", a.getTransformation().getContent());
    }

    @Test
    void toDTOTest() {
        Attribute a = new Attribute();
        a.setId(0);
        a.setDestination("testdest");
        a.setSource("testsrc");
        a.setDescription("testdesc");
        a.setType(AttributeType.BOOLEAN);
        a.setEncrypted(true);
        a.setTransformation(null);

        AttributeDTO aDTO = AttributeDTO.toDTO(a);
        assertEquals(0, aDTO.getId());
        assertEquals("testdest", aDTO.getDestination());
        assertEquals("testsrc", aDTO.getSource());
        assertEquals("testdesc", aDTO.getDescription());
        assertEquals(AttributeType.BOOLEAN, aDTO.getType());
        assertEquals(true, aDTO.isEncrypted());
        assertNull(aDTO.getTransformation()); 

        Script s = new Script();
        s.setContent("testcontent");
        s.setName("testname");
        
        a.setTransformation(s);
        aDTO = AttributeDTO.toDTO(a);

        assertEquals("testcontent", aDTO.getTransformation().getContent());

        aDTO = AttributeDTO.toDTO(null);
        assertNull(aDTO);
    }
}