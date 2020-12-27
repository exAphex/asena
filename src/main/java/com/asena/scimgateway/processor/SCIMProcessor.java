package com.asena.scimgateway.processor;

import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class SCIMProcessor {
    public static Object processUser(RemoteSystem rs, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        String nameId = null;
        HashMap<String, Object> data = prepareData(rs, obj); 
        
        nameId = nameIdAttr.getDestination();
        String id = transferToConnector("CreateUser", rs, nameId, data);
        obj = putIdToObject(obj, id);
        return obj;
    }

    public static Object updateUser(RemoteSystem rs, String userId, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        String nameId = null;
        HashMap<String, Object> data = prepareData(rs, obj); 

        nameId = nameIdAttr.getDestination();
        data.replace(nameIdAttr.getDestination(), userId);

        String id = transferToConnector("CreateUser", rs, nameId, data);
        obj = putIdToObject(obj, id);
        return obj;
    }

    private static Object getObjectFromPath(Object obj, String path) {
        Object retObj = null;
        try {
            retObj = JsonPath.parse(obj).read(path);
        } catch (Exception e) {
                     
        }
        return retObj;
    }

    private static Object putIdToObject(Object obj, String id) {
        Object retObj = null;
        try {
            DocumentContext doc = JsonPath.parse(obj).set("$.id", id);
            return doc.json();
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

    private static String transferToConnector(String type, RemoteSystem rs, String nameId, HashMap<String, Object> data)
            throws Exception {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.setupConnector(rs);
        return conn.writeData(type, data);
    }

}