package com.asena.scimgateway.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SCIMProcessorTest {
    private RemoteSystem rs;
    private LinkedHashMap<String, Object> data;

    @BeforeEach
    void prepareSystem() {
        data = new LinkedHashMap<>();
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

        rs = new RemoteSystem();
        rs.addWriteMapping(a);
        rs.addWriteMapping(b);
        rs.addWriteMapping(c);
        rs.setType("NOOP");
        rs.setWriteNameId(new Attribute("", "noop", ""));
    }

    @Test
    @SuppressWarnings("unchecked")
    void createUserTest() throws Exception {
        LinkedHashMap<String, Object> retData = (LinkedHashMap<String, Object>)SCIMProcessor.createUser(this.rs, data);
        assertEquals("testuser", (String)retData.get("id"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void updateUserTest() throws Exception {
        LinkedHashMap<String, Object> retData = (LinkedHashMap<String, Object>)SCIMProcessor.updateUser(this.rs, "testuser", data);
        assertEquals("testuser", (String)retData.get("id"));
    }

    @Test
    void noWriteNameIdTest() {
        rs.setWriteNameId(null);
        
        
        assertThrows(InternalErrorException.class, () -> {
            SCIMProcessor.createUser(this.rs, data);
        });

        Attribute a = new Attribute();
        rs.setWriteNameId(a);
        assertThrows(InternalErrorException.class, () -> {
            SCIMProcessor.createUser(this.rs, data);
        });
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<SCIMProcessor> constructor = SCIMProcessor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}