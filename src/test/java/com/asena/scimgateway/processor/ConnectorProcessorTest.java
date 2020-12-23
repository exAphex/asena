package com.asena.scimgateway.processor;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.model.RemoteSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConnectorProcessorTest {
    
    @Test
    void getAvailableConnectorsTest() {
        Set<RemoteSystem> conns = ConnectorProcessor.getAvailableConnectors();
        assertNotEquals(0, conns.size());
    }

    @Test
    void getRemoteSystemByTypeTest() {
        RemoteSystem rs = ConnectorProcessor.getRemoteSystemByType(null);
        assertNull(rs);

        rs = ConnectorProcessor.getRemoteSystemByType("LDAP");
        assertNotNull(rs);
    }

    @Test
    void getConnectorByTypeTest() {
        IConnector conn = ConnectorProcessor.getConnectorByType("LDAP");
        assertNotNull(conn);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ConnectorProcessor> constructor = ConnectorProcessor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}