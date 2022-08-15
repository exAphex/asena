package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.Attribute.AttributeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AttributeServiceTest {
    @Autowired
    private AttributeService attributeService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private RemoteSystemService remoteSystemService;

    @Autowired
    private EntryTypeMappingService entryTypeMappingService;

    @BeforeEach
    void prepareDb() {
        attributeService.deleteAll();
        scriptService.deleteAll();
        remoteSystemService.deleteAll();
    }

    @Test
    void createTest() {
        Script s = new Script();
        s.setName("Testname");
        s = scriptService.create(s);

        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(s);
        a = attributeService.create(a);

        assertEquals("Testdesc", a.getDescription());
        assertEquals("Testdest", a.getDestination());
        assertEquals(false, a.isEncrypted());
        assertEquals("Testsrc", a.getSource());
        assertEquals(AttributeType.BOOLEAN, a.getType());
        assertEquals("Testname", a.getTransformation().getName());
    }

    @Test
    void createNullTest() {
        Attribute a = attributeService.create(null);
        assertNull(a);
    }

    @Test
    void createNonTransformationTest() {
        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(null);
        a = attributeService.create(a);
        assertNotNull(a);
    }

    @Test
    void createNewTransformationTest() {
        Script s = new Script();
        s.setName("testscript");

        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(s);
        a = attributeService.create(a);

        assertNotNull(a);
        assertNull(a.getTransformation());
    }

    @Test
    void createExistingTransformationTest() {
        Script s = new Script();
        s.setName("testscript");
        scriptService.create(s);

        Script sNew = new Script();
        sNew.setName("testscript");

        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(sNew);
        a = attributeService.create(a);

        assertNotNull(a);
        assertNotNull(a.getTransformation());
    }

    @Test
    void findByIdTest() {
        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a = attributeService.create(a);

        attributeService.findById(a.getId()).orElseThrow(() -> new NotFoundException(0l));

        assertThrows(NotFoundException.class, () -> {
            attributeService.findById(0).orElseThrow(() -> new NotFoundException(0l));
        });
    }

    @Test
    void updateTest() {
        Script s = new Script();
        s.setName("Testname");
        s = scriptService.create(s);

        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(s);
        a = attributeService.create(a);

        a.setDescription("Testdesc1");
        a.setDestination("Testdest1");
        a.setEncrypted(true);
        a.setSource("Testsrc1");
        a.setType(AttributeType.STRING);
        a.setTransformation(null);
        a = attributeService.update(a, a.getId());

        assertEquals("Testdesc1", a.getDescription());
        assertEquals("Testdest1", a.getDestination());
        assertEquals(true, a.isEncrypted());
        assertEquals("Testsrc1", a.getSource());
        assertEquals(AttributeType.STRING, a.getType());
        assertEquals(null, a.getTransformation());

        assertThrows(NotFoundException.class, () -> {
            attributeService.update(null, 0);
        });
    }

    @Test
    void deleteTest() {
        Script s = new Script();
        s.setName("Testname");
        s = scriptService.create(s);

        EntryTypeMapping em = new EntryTypeMapping("User");

        Attribute a = new Attribute();
        a.setDescription("Testdesc");
        a.setDestination("Testdest");
        a.setEncrypted(false);
        a.setSource("Testsrc");
        a.setType(AttributeType.BOOLEAN);
        a.setTransformation(s);
        a = attributeService.create(a);

        em = entryTypeMappingService.create(em);
        em.addWriteMapping(a);

        RemoteSystem rs = new RemoteSystem();
        rs.setType("LDAP");
        rs.setName("TESTSYS");
        rs.addEntryTypeMapping(em);
        rs = remoteSystemService.create(rs);

        attributeService.deleteById(a.getId());

        assertThrows(NotFoundException.class, () -> {
            attributeService.deleteById(0);
        });

        attributeService.deleteAll();

        List<Attribute> attrs = attributeService.list();
        assertEquals(0, attrs.size());
    }
}