package com.asena.scimgateway.processor;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.asena.scimgateway.connector.AzureConnector;
import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.connector.LDAPConnector;
import com.asena.scimgateway.connector.NoOpConnector;
import com.asena.scimgateway.connector.SACConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;

import org.junit.jupiter.api.Test;

public class ConnectorProcessorTest {

    @Test
    void getAvailableConnectorsTest() {
        Set<RemoteSystem> conns = ConnectorProcessor.getAvailableConnectorTemplates();
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
        assertTrue(conn instanceof LDAPConnector);

        conn = ConnectorProcessor.getConnectorByType("NOOP");
        assertNotNull(conn);
        assertTrue(conn instanceof NoOpConnector);

        conn = ConnectorProcessor.getConnectorByType("Microsoft Azure Active Directory");
        assertNotNull(conn);
        assertTrue(conn instanceof AzureConnector);

        conn = ConnectorProcessor.getConnectorByType("SAP Analytics Cloud");
        assertNotNull(conn);
        assertTrue(conn instanceof SACConnector);

        assertThrows(InternalErrorException.class, () -> {
            ConnectorProcessor.getConnectorByType(null);
        });

        assertThrows(InternalErrorException.class, () -> {
            ConnectorProcessor.getConnectorByType("");
        });
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ConnectorProcessor> constructor = ConnectorProcessor.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}