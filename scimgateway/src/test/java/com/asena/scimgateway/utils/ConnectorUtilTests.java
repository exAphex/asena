package com.asena.scimgateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
public class ConnectorUtilTests {

    @Test
    void getAttributeValueTests() {
        HashMap<String, Object> attrs = new HashMap<>();
        attrs.put("TEST1", "RAND1");
        attrs.put(null, null);
        attrs.put("TEST2", null);
        attrs.put(null, "RAND3");

        Object o = ConnectorUtil.getAttributeValue(null, attrs);
        assertEquals(null, o);

        o = ConnectorUtil.getAttributeValue(null, null);
        assertEquals(null, o); 

        o = ConnectorUtil.getAttributeValue("test", null);
        assertEquals(null, o); 

        o = ConnectorUtil.getAttributeValue("TEST3", attrs);
        assertEquals(null, o); 

        o = ConnectorUtil.getAttributeValue("TEST1", attrs); 
        assertNotEquals(null, o);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ConnectorUtil> constructor = ConnectorUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}