package com.asena.scimgateway.processor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.jayway.jsonpath.JsonPath;

public class SCIMProcessor {

    private SCIMProcessor() {}

    @SuppressWarnings("unchecked")
    public static Object createUser(RemoteSystem rs, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        String nameId = null;
        HashMap<String, Object> data = prepareData(rs, obj); 
        
        nameId = nameIdAttr.getDestination();
        String id = transferCreateToConnector("User", rs, nameId, data);
        LinkedHashMap<Object, Object> retObj = (LinkedHashMap<Object, Object>)obj;
        retObj.put("id", id);
        return retObj;
    }

    @SuppressWarnings("unchecked")
    public static Object updateUser(RemoteSystem rs, String userId, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        String nameId = null;
        HashMap<String, Object> data = prepareData(rs, obj); 

        nameId = nameIdAttr.getDestination();
        data.replace(nameIdAttr.getDestination(), userId);

        String id = transferUpdateToConnector("User", rs, nameId, data);
        LinkedHashMap<Object, Object> retObj = (LinkedHashMap<Object, Object>)obj;
        retObj.put("id", id);
        return retObj;
    }

    public static boolean deleteUser(RemoteSystem rs, String userId) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        String nameId = null;
        HashMap<String, Object> data = new HashMap<>();
        nameId = nameIdAttr.getDestination();
        data.put(nameId, userId);

        return transferDeleteToConnector("User", rs, nameId, data);
    }

    private static Object getObjectFromPath(Object obj, String path) {
        Object retObj = null;
        try {
            retObj = JsonPath.parse(obj).read(path);
        } catch (Exception e) {
                     
        }
        return retObj;
    }

    private static HashMap<String, Object> prepareData(RemoteSystem rs, Object obj) {
        Set<Attribute> attrs = rs.getWriteMappings();
        Attribute nameIdAttr = rs.getWriteNameId();
        HashMap<String, Object> data = new HashMap<>();

        if ((nameIdAttr == null) || (nameIdAttr.getDestination() == null)) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        for (Attribute a : attrs) {
            Object o = getObjectFromPath(obj, a.getSource());
            if (a.getTransformation() != null) {
                o = ScriptProcessor.processTransformation(a, o);
            }
            data.put(a.getDestination(), o);
        } 

        return data;
    }

    private static String transferCreateToConnector(String type, RemoteSystem rs, String nameId, HashMap<String, Object> data)
            throws Exception {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.setupConnector(rs);
        conn.setNameId(nameId);
        return conn.createEntity(type, data);
    }

    private static String transferUpdateToConnector(String type, RemoteSystem rs, String nameId, HashMap<String, Object> data)
            throws Exception {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.setupConnector(rs);
        conn.setNameId(nameId);
        return conn.updateEntity(type, data);
    }

    private static boolean transferDeleteToConnector(String type, RemoteSystem rs, String nameId, HashMap<String, Object> data)
            throws Exception {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.setupConnector(rs);
        conn.setNameId(nameId);
        return conn.deleteEntity(type, data);
    }
}