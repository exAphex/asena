package com.asena.scimgateway.processor;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.connector.LDAPConnector;
import com.asena.scimgateway.connector.NoOpConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;

public class ConnectorProcessor {
    private ConnectorProcessor() {}

    public static Set<RemoteSystem> getAvailableConnectors() {
        Set<RemoteSystem> retSystems = new HashSet<>();
        LDAPConnector ldap = new LDAPConnector();
        
        retSystems.add(ldap.getRemoteSystemTemplate());
        
        return retSystems;
    }

    public static RemoteSystem getRemoteSystemByType(String type) {
        Set<RemoteSystem> systems = getAvailableConnectors();

        for (RemoteSystem rs : systems) {
            if (rs.getType().equals(type)) {
                return rs;
            }
        }

        return null;
    }

    public static IConnector getConnectorByType(String type) {
        LDAPConnector csv = new LDAPConnector();
        NoOpConnector noop = new NoOpConnector();

        if (type == null) {
            throw new InternalErrorException("No connector found with type null");
        }
        
        switch (type) {
            case "LDAP":
                return csv;
            case "NOOP":
                return noop;
            default:
                throw new InternalErrorException("No connector found with type " + type);
        }
    }

}