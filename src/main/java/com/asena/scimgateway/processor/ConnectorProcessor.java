package com.asena.scimgateway.processor;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.connector.LDAPConnector;
import com.asena.scimgateway.model.RemoteSystem;

public class ConnectorProcessor {
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
        return csv;
    }

}