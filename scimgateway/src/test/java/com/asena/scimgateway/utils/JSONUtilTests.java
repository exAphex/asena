package com.asena.scimgateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;

public class JSONUtilTests {
    
    @Test
    void getObjectFromPathTest() throws Exception {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("id", "test");
        obj.put("img", "http://img");

        assertEquals("test", (String) JSONUtil.getObjectFromPath(obj, "$.id"));

        assertThrows(Exception.class, () -> {
            JSONUtil.getObjectFromPath(obj, "$.test");
        });
    }

    @Test
    void createTest() throws Exception {
        DocumentContext d = JsonPath.parse("{}");
        JSONUtil.create(d, "$.id", "test");
        JSONUtil.create(d, "$.name.firstName", "test1");

        assertEquals("test", (String) d.read("$.id"));
        assertEquals("test1", (String) d.read("$.name.firstName")); 
    }

    @Test
    void getFromJSONPathTest() throws Exception {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("id", "test");
        obj.put("img", "http://img");

        assertEquals("test", (String) JSONUtil.getFromJSONPath("$.id", obj));
        assertNull(JSONUtil.getFromJSONPath("$.test", obj));
    }

    @Test
    void addPropertyToJSONTest() {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("id", "test");
        obj.put("img", "http://img");

        DocumentContext d = JsonPath.parse("{}");

        JSONUtil.addPropertyToJSON(d, "id", "$.id", obj);
        JSONUtil.addPropertyToJSON(d, "omg", "$.test", obj);

        assertEquals("test", (String) d.read("$.id"));
        assertNull(JSONUtil.getFromJSONPath("$.test", obj)); 
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<JSONUtil> constructor = JSONUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}