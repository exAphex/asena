package com.asena.scimgateway.processor;

import java.util.HashSet;
import java.util.Set;

import com.asena.scimgateway.connector.AzureConnector;
import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.connector.LDAPConnector;
import com.asena.scimgateway.connector.NoOpConnector;
import com.asena.scimgateway.connector.OneIdentityConnector;
import com.asena.scimgateway.connector.SACConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorProcessor {
    private static Logger logger = LoggerFactory.getLogger(ConnectorProcessor.class);

    private ConnectorProcessor() {
    }

    public static Set<RemoteSystem> getAvailableConnectors() {
        Set<RemoteSystem> retSystems = new HashSet<>();
        LDAPConnector ldap = new LDAPConnector();
        SACConnector sac = new SACConnector();
        AzureConnector az = new AzureConnector();
        OneIdentityConnector oc = new OneIdentityConnector();

        retSystems.add(ldap.getRemoteSystemTemplate());
        retSystems.add(sac.getRemoteSystemTemplate());
        retSystems.add(az.getRemoteSystemTemplate());
        retSystems.add(oc.getRemoteSystemTemplate());

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
        SACConnector sac = new SACConnector();
        AzureConnector az = new AzureConnector();
        OneIdentityConnector oi = new OneIdentityConnector();

        logger.info("Reading connector type {}", type);

        if (type == null) {
            throw new InternalErrorException("No connector found with type null");
        }

        switch (type) {
            case "LDAP":
                return csv;
            case "SAP Analytics Cloud":
                return sac;
            case "Microsoft Azure Active Directory":
                return az;
            case "NOOP":
                return noop;
            case "OneIdentity":
                return oi;
            default:
                throw new InternalErrorException("No connector found with type " + type);
        }
    }

}