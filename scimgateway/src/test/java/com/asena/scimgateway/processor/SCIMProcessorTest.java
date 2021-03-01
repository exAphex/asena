package com.asena.scimgateway.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class SCIMProcessorTest {
    private RemoteSystem rs;
    private HashMap<String, Object> data;

    @BeforeEach
    void prepareSystem() {
        data = new HashMap<>();
        data.put("userName", "testuser");
        data.put("firstName", "testfirstname");

        Attribute a = new Attribute();
        a.setSource("$.userName");
        a.setDestination("noop");

        Script s = new Script();
        s.setName("testscript");
        s.setContent("function testscript(param) { return param }");

        Attribute b = new Attribute();
        b.setSource("$.firstName");
        b.setDestination("testdest");
        b.setTransformation(s);

        Attribute c = new Attribute();
        c.setSource("$.lastName");
        c.setDescription("testdest1");

        Attribute d = new Attribute();
        d.setSource("noop");
        d.setDestination("$.id");
        d.setTransformation(s);

        Attribute e = new Attribute();
        e.setSource("noop");
        e.setDestination("$.nameId");


        EntryTypeMapping em = new EntryTypeMapping("User");
        em.addWriteMapping(a);

        rs = new RemoteSystem();
        em.addWriteMapping(a);
        em.addWriteMapping(b);
        em.addWriteMapping(c);
        em.addReadMapping(d);
        em.addReadMapping(e);
        rs.addEntryTypeMapping(em);

        rs.setType("NOOP");
    }

    @Test
    void getEntitiesTest() throws Exception {
        HashMap<String, Object> retData = new SCIMProcessor(this.rs, "User").getEntities();
        assertNotNull(retData);
    }

    @Test
    void getEntityTest() throws Exception {
        HashMap<String, Object> retData = new SCIMProcessor(this.rs, "User").getEntity("testId");
        assertNotNull(retData);
    }

    @Test
    void createUserTest() throws Exception {
        HashMap<String, Object> retData = new SCIMProcessor(this.rs, "User").createEntity(data);
        assertEquals("testuser", (String)retData.get("id"));
    }

    @Test
    void updateUserTest() throws Exception {
        HashMap<String, Object> retData = new SCIMProcessor(this.rs, "User").updateEntity("testuser", data);
        assertEquals("testuser", (String)retData.get("id"));
    }

    @Test
    void deleteUserTest() throws Exception {
        boolean deleted = new SCIMProcessor(this.rs, "User").deleteEntity("testuser");
        assertTrue(deleted); 
    }

    @Test
    void noWriteNameIdTest() {
        
        this.rs.setEntryTypeMappings(null);
        assertThrows(InternalErrorException.class, () -> {
            new SCIMProcessor(this.rs, "TestEntity").createEntity(data);
        });

        assertThrows(InternalErrorException.class, () -> {
            new SCIMProcessor(this.rs, "TestEntity").updateEntity("testuser", data);
        });

        assertThrows(InternalErrorException.class, () -> {
            new SCIMProcessor(this.rs, "TestEntity").deleteEntity("testuser");
        });

        assertThrows(InternalErrorException.class, () -> {
            new SCIMProcessor(this.rs, "TestEntity").getEntities();
        });

        assertThrows(InternalErrorException.class, () -> {
            new SCIMProcessor(this.rs, "TestEntity").getEntity("testuser");
        });
    }

    @Test
    void readUserTest() throws Exception {
        //HashMap<String, Object> users = new SCIMProcessor(this.rs, "TestEntity").getEntities();
        //assertEquals(0, users.size());
    }

}