package com.asena.scimgateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
}