package com.asena.scimgateway.script;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

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

        Context context = Context.enter();
        gb = new GlobalScripts(context, remoteSystem);
        context.initStandardObjects(gb);
        
        ConnectionProperty cp = new ConnectionProperty("test.test","testval", "testdesc", false, ConnectionPropertyType.STRING);
        ConnectionProperty cpTwo = new ConnectionProperty(null,null, "testdesc", false, ConnectionPropertyType.STRING); 
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
}