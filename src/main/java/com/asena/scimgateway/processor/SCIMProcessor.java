package com.asena.scimgateway.processor;

import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.jayway.jsonpath.JsonPath;

public class SCIMProcessor {
    public static Object processUser(RemoteSystem rs, Object obj) {
        Set<Attribute> attrs = rs.getWriteMappings();
        HashMap<String, Object> data = new HashMap<>();
        for (Attribute a : attrs) {
            Object o = getObjectFromPath(obj, a.getSource());
            data.put(a.getDestination(), o);
        } 
        transferToConnector("User", rs, data);
        return obj;
    }

    public static Object getObjectFromPath(Object obj, String path) {
        Object retObj = null;
        try {
            retObj = JsonPath.parse(obj).read(path);
        } catch (Exception e) {
                     
        }
        return retObj;
    }

    public static void transferToConnector(String type, RemoteSystem rs, HashMap<String, Object> data) {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.writeData(type, rs, data);
    }

}