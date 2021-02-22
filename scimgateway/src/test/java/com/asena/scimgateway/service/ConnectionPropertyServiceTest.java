package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ConnectionPropertyServiceTest {
    
    @Autowired
    private ConnectionPropertyService connectionPropertyService;

    @Autowired
    private RemoteSystemService remoteSystemService;

    @BeforeEach
    void prepareDb() {
        connectionPropertyService.deleteAll();
        remoteSystemService.deleteAll();
    }

    @Test
    void createTest() {
        ConnectionProperty cp = new ConnectionProperty();
        cp.setDescription("Testdesc");
        cp.setEncrypted(false);
        cp.setKey("TEST");
        cp.setType(ConnectionPropertyType.STRING);
        cp.setValue("Testval");

        cp = connectionPropertyService.create(cp);

        assertEquals("Testdesc", cp.getDescription());
        assertEquals(false, cp.isEncrypted());
        assertEquals("TEST", cp.getKey());
        assertEquals(ConnectionPropertyType.STRING, cp.getType());
        assertEquals("Testval", cp.getValue());
    }

    @Test
    void findByIdTest() {
        ConnectionProperty cp = new ConnectionProperty();
        cp.setDescription("Testdesc");
        cp.setEncrypted(false);
        cp.setKey("TEST");
        cp.setType(ConnectionPropertyType.STRING);
        cp.setValue("Testval");

        cp = connectionPropertyService.create(cp);

        connectionPropertyService.findById(cp.getId()).orElseThrow(() -> new NotFoundException(0l));

        assertThrows(NotFoundException.class, () -> {
            connectionPropertyService.findById(0).orElseThrow(() -> new NotFoundException(0l));
        });
    }

    @Test
    void updateTest() {
        ConnectionProperty cp = new ConnectionProperty();
        cp.setDescription("Testdesc");
        cp.setEncrypted(false);
        cp.setKey("TEST");
        cp.setType(ConnectionPropertyType.STRING);
        cp.setValue("Testval");

        cp = connectionPropertyService.create(cp);

        cp.setDescription("Testdesc1");
        cp.setEncrypted(true);
        cp.setKey("TEST1");
        cp.setType(ConnectionPropertyType.INT);
        cp.setValue("Testval1");
        
        cp = connectionPropertyService.update(cp, cp.getId());

        assertEquals("Testdesc1", cp.getDescription());
        assertEquals(true, cp.isEncrypted());
        assertEquals("TEST1", cp.getKey());
        assertEquals(ConnectionPropertyType.STRING, cp.getType());
        assertEquals("Testval1", cp.getValue());

        assertThrows(NotFoundException.class, () -> {
            connectionPropertyService.update(null, 0);
        });
    }

    @Test
    void deleteTest() {
        ConnectionProperty cp = new ConnectionProperty();
        cp.setDescription("Testdesc");
        cp.setEncrypted(false);
        cp.setKey("TEST");
        cp.setType(ConnectionPropertyType.STRING);
        cp.setValue("Testval");

        cp = connectionPropertyService.create(cp);

        RemoteSystem rs = new RemoteSystem();
        rs.setType("LDAP");
        rs.setName("TESTSYS");
        rs.addProperty(cp);
        rs = remoteSystemService.create(rs);

        connectionPropertyService.deleteById(cp.getId());


        assertThrows(NotFoundException.class, () -> {
            connectionPropertyService.deleteById(0);
        });
    }
}