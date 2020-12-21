package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.RemoteSystemDTO;
import com.asena.scimgateway.model.dto.UserDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RemoteSystemDTOTest {
    
    @Test
    void fromDTOTest() {
        RemoteSystemDTO rsDTO = new RemoteSystemDTO();
        rsDTO.setId("testid");
        rsDTO.setName("testname");
        rsDTO.setDescription("testdesc");
        rsDTO.setActive(true);
        rsDTO.setAttributes(null);
        rsDTO.setWriteMappings(null);
        rsDTO.setProperties(null);
        rsDTO.setType("testtype");
        rsDTO.setServiceUser(null);

        RemoteSystem rs = rsDTO.fromDTO();

        assertEquals("testid", rs.getId());
        assertEquals("testname", rs.getId());
        assertEquals("testdesc", rs.getId());
        assertEquals(true, rs.getId());
        assertNull(rs.getAttributes());
        assertNull(rs.getWriteMappings());
        assertNull(rs.getProperties());
        assertEquals("testtype", rs.getType());
        assertNull(rs.getServiceUser());

        UserDTO su = new UserDTO();
        su.setUserName("testusername");

        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setDescription("testdesc");
        Set<AttributeDTO> attrDTO = new HashSet<>();
        attrDTO.add(aDTO);

        rsDTO.setAttributes(attrDTO);
        rsDTO.setServiceUser(su);
        rsDTO.setWriteMappings(attrDTO);

        rs = rsDTO.fromDTO();
        assertEquals("testusername", rs.getServiceUser().getUserName());
        assertEquals(1, rs.getAttributes().size());
        assertEquals(1, rs.getWriteMappings().size());
    }
}