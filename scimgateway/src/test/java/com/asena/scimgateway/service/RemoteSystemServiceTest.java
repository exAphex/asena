package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.processor.ConnectorProcessor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RemoteSystemServiceTest {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @Autowired
    private AttributeService attributeService;

    @BeforeEach
    void prepareDb() {
        remoteSystemService.deleteAll();
        attributeService.deleteAll();
    }

    @Test
    void createTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        RemoteSystem conn = ConnectorProcessor.getRemoteSystemByType(rs.getType());

        RemoteSystem rsTwo = new RemoteSystem();
        rsTwo.setName("TestTwoname");
        rsTwo.setType("UNKOWN");
        
        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.create(rsTwo);
        });

        assertEquals(false, rs.isActive());
        assertEquals(conn.getProperties().size(), rs.getProperties().size());
        assertNotNull(rs.getEntryTypeMappings());
        assertNull(rs.getAttributes());
        assertEquals("Testdesc", rs.getDescription());
        assertEquals("Testname", rs.getName());
        assertEquals("LDAP", rs.getType());
        assertNotNull(rs.getServiceUser());

        List<RemoteSystem> rss = remoteSystemService.list();
        assertEquals(1, rss.size());
    }

    @Test
    void findByIdTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        remoteSystemService.findById(rs.getId()).orElseThrow(() -> new NotFoundException(0l));

        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.findById("").orElseThrow(() -> new NotFoundException(0l));
        }); 
    }

    @Test
    void updateTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        rs.setActive(true);
        rs.setDescription("Testdesc1");
        rs.getServiceUser().setPassword("password");

        rs = remoteSystemService.update(rs, rs.getId());

        assertEquals(true, rs.isActive());
        assertEquals("Testdesc1", rs.getDescription());
        assertEquals("password", rs.getServiceUser().getPassword());

        rs.setServiceUser(null);
        rs = remoteSystemService.update(rs, rs.getId());

        assertEquals("password", rs.getServiceUser().getPassword());

        rs = remoteSystemService.update(rs, rs.getId());
        
        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.update(null, "");
        }); 
    }

    @Test
    void deleteTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        rs = remoteSystemService.addEntryTypeMapping(new EntryTypeMapping("User"), rs.getId());

        remoteSystemService.deleteById(rs.getId());

        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.deleteById("");
        }); 
    }

    @Test
    void addEntryTypeMappingTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        attributeService.deleteAll();

        rs = remoteSystemService.addEntryTypeMapping(new EntryTypeMapping("User"), rs.getId());
        
        EntryTypeMapping[] e = new EntryTypeMapping[rs.getEntryTypeMappings().size()];
        rs.getEntryTypeMappings().toArray(e);

        assertEquals(1, e.length);
       
        assertEquals("User", e[0].getName());

        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.addEntryTypeMapping(null, "");
        }); 
    }

    @Test
    void addConnectionPropertyTest() {
        RemoteSystem rs = new RemoteSystem();
        rs.setActive(true);
        rs.setDescription("Testdesc");
        rs.setName("Testname");
        rs.setType("LDAP");
        
        rs = remoteSystemService.create(rs);

        int presetLen = rs.getProperties().size();

        rs = remoteSystemService.addConnectionProperty(new ConnectionProperty("key", "value", "dest", false, ConnectionPropertyType.BOOLEAN), rs.getId());

        assertEquals(presetLen + 1, rs.getProperties().size());

        assertThrows(NotFoundException.class, () -> {
            remoteSystemService.addConnectionProperty(null, "");
        }); 
    }
}