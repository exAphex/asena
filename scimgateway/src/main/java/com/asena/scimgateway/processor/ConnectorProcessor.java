package com.asena.scimgateway.processor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

public class ConnectorProcessor {
    private static Logger logger = LoggerFactory.getLogger(ConnectorProcessor.class);

    private ConnectorProcessor() {
    }

    public static Set<RemoteSystem> getAvailableConnectors() {
        Set<RemoteSystem> retSystems = new HashSet<>();
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

        final Set<BeanDefinition> classes = provider.findCandidateComponents("com.asena.scimgateway.connector");

        for (BeanDefinition bean : classes) {
            IConnector clazz;
            Class<?> tempClass;
            try {
                tempClass = Class.forName(bean.getBeanClassName());
                if (IConnector.class.isAssignableFrom(tempClass)) {
                    clazz = (IConnector) tempClass.newInstance();
                    retSystems.add(clazz.getRemoteSystemTemplate());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.debug("Unexpected class", e);
            }
        }
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