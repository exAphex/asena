package com.asena.scimgateway.processor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.asena.scimgateway.connector.IConnector;
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

    public static Set<IConnector> getAvailableConnectors() {
        Set<IConnector> retConnectors = new HashSet<>();
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

        final Set<BeanDefinition> classes = provider.findCandidateComponents("com.asena.scimgateway.connector");

        for (BeanDefinition bean : classes) {
            Class<?> tempClass;
            try {
                tempClass = Class.forName(bean.getBeanClassName());
                if (IConnector.class.isAssignableFrom(tempClass)) {
                    IConnector clazz = (IConnector) tempClass.newInstance();
                    retConnectors.add(clazz);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.debug("Unexpected class", e);
            }
        }
        return retConnectors;
    }

    public static Set<RemoteSystem> getAvailableConnectorTemplates() {
        Set<RemoteSystem> retSystems = new HashSet<>();
        Set<IConnector> connectors = getAvailableConnectors();
        for (IConnector c : connectors) {
            retSystems.add(c.getRemoteSystemTemplate());
        }
        return retSystems;
    }

    public static RemoteSystem getRemoteSystemByType(String type) {
        Set<RemoteSystem> systems = getAvailableConnectorTemplates();

        for (RemoteSystem rs : systems) {
            if (rs.getType().equals(type)) {
                return rs;
            }
        }
        return null;
    }

    public static IConnector getConnectorByType(String type) {
        Set<IConnector> connectors = getAvailableConnectors();

        logger.info("Reading connector type {}", type);

        if (type == null) {
            throw new InternalErrorException("No connector found with type null");
        }

        for (IConnector c : connectors) {
            if (type.equalsIgnoreCase(c.getRemoteSystemTemplate().getType())) {
                return c;
            }
        }
        throw new InternalErrorException("No connector found with type " + type);
    }

}