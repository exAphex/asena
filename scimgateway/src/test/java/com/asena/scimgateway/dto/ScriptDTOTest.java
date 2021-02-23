package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.dto.ScriptDTO;

import org.junit.jupiter.api.Test;

public class ScriptDTOTest {
    
    @Test
    void fromDTOTest() {
        ScriptDTO sDTO = new ScriptDTO();
        sDTO.setId(0);
        sDTO.setContent("testcontent");
        sDTO.setName("testname");

        Script s = sDTO.fromDTO();
        
        assertEquals(0, s.getId());
        assertEquals("testcontent", s.getContent());
        assertEquals("testname", s.getName());
    }

    @Test
    void toDTOTest() {
        Script s = new Script();
        s.setId(0);
        s.setContent("testcontent");
        s.setName("testname");

        ScriptDTO sDTO = ScriptDTO.toDTO(s);
        
        assertEquals(0, s.getId());
        assertEquals("testcontent", sDTO.getContent());
        assertEquals("testname", sDTO.getName());

        sDTO = ScriptDTO.toDTO(null);
        assertNull(sDTO);
    }
}