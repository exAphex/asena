package com.asena.scimgateway.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.ConnectionPropertyDTO;
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
        rsDTO.setReadMappings(null);
        rsDTO.setProperties(null);
        rsDTO.setType("testtype");
        rsDTO.setServiceUser(null);
        rsDTO.setWriteNameId(null);
        rsDTO.setReadNameId(null);

        RemoteSystem rs = rsDTO.fromDTO();

        assertEquals("testid", rs.getId());
        assertEquals("testname", rs.getName());
        assertEquals("testdesc", rs.getDescription());
        assertEquals(true, rs.isActive());
        assertNull(rs.getAttributes());
        assertNull(rs.getWriteMappings());
        assertNull(rs.getReadMappings());
        assertNull(rs.getProperties());
        assertEquals("testtype", rs.getType());
        assertNull(rs.getServiceUser());
        assertNull(rs.getWriteNameId());
        assertNull(rs.getReadNameId());

        UserDTO su = new UserDTO();
        su.setUserName("testusername");

        AttributeDTO aDTO = new AttributeDTO();
        aDTO.setDescription("testdesc");
        Set<AttributeDTO> attrDTO = new HashSet<>();
        attrDTO.add(aDTO);

        ConnectionPropertyDTO cDTO = new ConnectionPropertyDTO();
        cDTO.setId(1);
        Set<ConnectionPropertyDTO> cpDTO = new HashSet<>();
        cpDTO.add(cDTO);

        rsDTO.setAttributes(attrDTO);
        rsDTO.setServiceUser(su);
        rsDTO.setWriteMappings(attrDTO);
        rsDTO.setReadMappings(attrDTO);
        rsDTO.setProperties(cpDTO);
        rsDTO.setWriteNameId(aDTO);
        rsDTO.setReadNameId(aDTO);

        rs = rsDTO.fromDTO();
        assertEquals("testusername", rs.getServiceUser().getUserName());
        assertEquals(1, rs.getAttributes().size());
        assertEquals(1, rs.getWriteMappings().size());
        assertEquals(1, rs.getReadMappings().size());
    }

    @Test
    void toDTOTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setId("testid");
        rs.setName("testname");
        rs.setDescription("testdesc");
        rs.setActive(true);
        rs.setAttributes(null);
        rs.setWriteMappings(null);
        rs.setProperties(null);
        rs.setType("testtype");
        rs.setServiceUser(null);
        rs.setReadMappings(null);
        rs.setWriteNameId(null);
        rs.setReadNameId(null);

        RemoteSystemDTO rsDTO = RemoteSystemDTO.toDTO(rs);
        assertEquals("testid", rsDTO.getId());
        assertEquals("testname", rsDTO.getName());
        assertEquals("testdesc", rsDTO.getDescription());
        assertEquals(true, rsDTO.isActive());
        assertNull(rsDTO.getAttributes());
        assertNull(rsDTO.getWriteMappings());
        assertNull(rsDTO.getProperties());
        assertEquals("testtype", rsDTO.getType());
        assertNull(rsDTO.getServiceUser());
        assertNull(rsDTO.getReadNameId());
        assertNull(rsDTO.getReadMappings());
        assertNull(rsDTO.getWriteNameId());

        User su = new User();
        su.setUserName("testusername");

        Attribute a = new Attribute();
        a.setDescription("testdesc");

        Set<Attribute> attr = new HashSet<>();
        attr.add(a);

        ConnectionProperty c = new ConnectionProperty();
        c.setId(1);
        Set<ConnectionProperty> cp = new HashSet<>();
        cp.add(c);

        rs.setAttributes(attr);
        rs.setServiceUser(su);
        rs.setWriteMappings(attr);
        rs.setReadMappings(attr);
        rs.setProperties(cp);

        rsDTO = RemoteSystemDTO.toDTO(rs);
        assertEquals("testusername", rsDTO.getServiceUser().getUserName());
        assertEquals(1, rsDTO.getAttributes().size());
        assertEquals(1, rsDTO.getWriteMappings().size());

        rsDTO = RemoteSystemDTO.toDTO(null);
        assertNull(rsDTO);
    }
}