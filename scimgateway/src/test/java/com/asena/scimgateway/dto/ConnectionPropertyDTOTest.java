package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.model.dto.ConnectionPropertyDTO;

import org.junit.jupiter.api.Test;

public class ConnectionPropertyDTOTest {
    
    @Test
    void fromDTOTest() {
        ConnectionPropertyDTO cDTO = new ConnectionPropertyDTO();
        cDTO.setId(0);
        cDTO.setKey("testkey");
        cDTO.setValue("testvalue");
        cDTO.setDescription("testdesc");
        cDTO.setEncrypted(true);
        cDTO.setType(ConnectionPropertyType.BOOLEAN);

        ConnectionProperty c = cDTO.fromDTO();
        
        assertEquals(0, c.getId());
        assertEquals("testkey", c.getKey());
        assertEquals("testvalue", c.getValue());
        assertEquals("testdesc", c.getDescription());
        assertEquals(true, c.isEncrypted());
        assertEquals(ConnectionPropertyType.BOOLEAN, c.getType());
    }

    @Test
    void toDTOTest() {
        ConnectionProperty c = new ConnectionProperty();
        c.setId(0);
        c.setKey("testkey");
        c.setValue("testvalue");
        c.setDescription("testdesc");
        c.setEncrypted(true);
        c.setType(ConnectionPropertyType.BOOLEAN);

        ConnectionPropertyDTO cDTO = ConnectionPropertyDTO.toDTO(c);
        assertEquals(0, cDTO.getId());
        assertEquals("testkey", cDTO.getKey());
        assertEquals("testvalue", cDTO.getValue());
        assertEquals("testdesc", cDTO.getDescription());
        assertEquals(true, cDTO.isEncrypted());
        assertEquals(ConnectionPropertyType.BOOLEAN, cDTO.getType());

        cDTO = ConnectionPropertyDTO.toDTO(null);
        assertNull(cDTO);
    }
}