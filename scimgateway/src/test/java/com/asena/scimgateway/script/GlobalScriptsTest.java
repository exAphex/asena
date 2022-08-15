package com.asena.scimgateway.script;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;

import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.Context;

public class GlobalScriptsTest {
    private RemoteSystem remoteSystem;
    private GlobalScripts gb;

    @BeforeEach
    void prepareRemoteSystem() {
        remoteSystem = new RemoteSystem();
        remoteSystem.setName("testremotesystem");
        remoteSystem.setId("remSystem");

        Context context = Context.enter();
        gb = new GlobalScripts(context, remoteSystem);
        context.initStandardObjects(gb);

        ConnectionProperty cp = new ConnectionProperty("test.test", "testval", "testdesc", false,
                ConnectionPropertyType.STRING);
        ConnectionProperty cpTwo = new ConnectionProperty(null, null, "testdesc", false, ConnectionPropertyType.STRING);
        remoteSystem.addProperty(cp);
        remoteSystem.addProperty(cpTwo);
    }

    @Test
    void getRemoteSystemTest() {
        assertNotNull(gb.getRemoteSystem());
        assertEquals("testremotesystem", gb.getRemoteSystem().getName());
    }

    @Test
    void getSystemProperty() {
        assertNull(gb.getSystemProperty("test"));
        assertNull(gb.getSystemProperty(null));

        assertEquals("testval", gb.getSystemProperty("test.test"));

        gb.setRemoteSystem(null);

        assertNull(gb.getSystemProperty("test.test"));
    }

    @Test
    void getSystemIdTest() {
        String sId = gb.getSystemId();
        assertEquals("remSystem", sId);

        Context context = Context.enter();
        gb = new GlobalScripts(context, null);
        sId = gb.getSystemId();
        assertNull(sId);
    }

    @Test
    void printLnTest() {
        gb.println("Test");
    }

    @Test
    void readJSONTest() throws JsonMappingException, JsonProcessingException {
        String sJSON = "{\"str\":\"test\", \"bol\": true, \"inte\":1}";

        HashMap<String, Object> json = gb.readJSONFromString(sJSON);
        assertEquals("test", json.get("str"));
        assertEquals(true, json.get("bol"));
        assertEquals(1, json.get("inte"));
    }
}