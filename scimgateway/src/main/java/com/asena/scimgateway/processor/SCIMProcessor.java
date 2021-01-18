package com.asena.scimgateway.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class SCIMProcessor {

    private SCIMProcessor() {}

    @SuppressWarnings("unchecked")
    public static Object createUser(RemoteSystem rs, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        if (nameIdAttr == null) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        String nameId = null;
        HashMap<String, Object> data = prepareDataToRemoteSystem(rs, obj); 

        nameId = nameIdAttr.getDestination();
        String id = transferCreateToConnector("User", rs, nameId, data);
        LinkedHashMap<Object, Object> retObj = (LinkedHashMap<Object, Object>)obj;
        addMetaDataCreate(retObj, rs, id);
        return retObj;
    }

    @SuppressWarnings("unchecked")
    public static Object updateUser(RemoteSystem rs, String userId, Object obj) throws Exception {
        Attribute nameIdAttr = rs.getWriteNameId();
        if (nameIdAttr == null) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        String nameId = null;
        HashMap<String, Object> data = prepareDataToRemoteSystem(rs, obj); 

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

        if (nameIdAttr == null) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        nameId = nameIdAttr.getDestination();
        data.put(nameId, userId);

        return transferDeleteToConnector("User", rs, nameId, data);
    }

    public static List<HashMap<String, Object>> getUsers(RemoteSystem rs) throws Exception {
        Attribute nameIdAttr = rs.getReadNameId();
        if (nameIdAttr == null) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        String nameId = nameIdAttr.getDestination();
        List<HashMap<String, Object>> data = transferGetUsersToConnector("User", rs, nameId); 
        data = prepareListDataFromRemoteSystem(rs, data);

        return data;
    }

    private static Object getObjectFromPath(Object obj, String path) {
        Object retObj = null;
        try {
            retObj = JsonPath.parse(obj).read(path);
        } catch (Exception e) {
                     
        }
        return retObj;
    }

    private static void create(DocumentContext context, String path, Object value) {
        int pos = path.lastIndexOf('.');
        String parent = path.substring(0, pos);
        String child = path.substring(pos + 1);
        try {
          context.read(parent); // EX if parent missing
        } catch (PathNotFoundException e) {
          create(context, parent, new LinkedHashMap<>()); // (recursively) Create missing parent
        }
        context.put(parent, child, value);
      }

    private static List<HashMap<String, Object>> prepareListDataFromRemoteSystem(RemoteSystem rs, List<HashMap<String, Object>> obj) {
        Set<Attribute> attrs = rs.getReadMappings();
        Attribute nameIdAttr = rs.getWriteNameId();
        List<HashMap<String, Object>> retList = new ArrayList<>();
        

        if ((nameIdAttr == null) || (nameIdAttr.getDestination() == null)) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        for (HashMap<String, Object> d : obj) {
            DocumentContext jsonContext = JsonPath.parse("{}");
            for (Attribute a : attrs) {
                Object attrObj = d.get(a.getSource());
                if (a.getTransformation() != null) {
                    attrObj = ScriptProcessor.processTransformation(a, attrObj);
                }
                create(jsonContext, a.getDestination(), attrObj);
            } 
            HashMap<String, Object> tmpObj = jsonContext.read("$");
            addMetaDataList(tmpObj, d, rs, nameIdAttr);

            retList.add(tmpObj);
        } 

        return retList;
    }

    private static HashMap<String, Object> prepareDataToRemoteSystem(RemoteSystem rs, Object obj) {
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

    private static List<HashMap<String, Object>> transferGetUsersToConnector(String type, RemoteSystem rs, String nameId) throws Exception {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        conn.setupConnector(rs);
        conn.setNameId(nameId);
        return conn.getEntities(type);
    }

    private static void addMetaDataCreate(LinkedHashMap<Object, Object> obj, RemoteSystem rs, String id) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/gateway/" + rs.getId() + "/scim/v2/Users/" + id));
        obj.put("meta", meta);

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        obj.put("schemas", schemas);
        
        obj.put("id", id); 
    }
    
    private static void addMetaDataList(HashMap<String, Object> resultEntry, HashMap<String, Object> sourceEntry, RemoteSystem rs, Attribute nameIdAttr) {
        if ((nameIdAttr == null) || (nameIdAttr.getDestination().length() < 1)) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        Object nameIdObj = sourceEntry.get(nameIdAttr.getDestination());
        resultEntry.put("id", nameIdObj);

        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/gateway/" + rs.getId() + "/scim/v2/Users/" + nameIdObj));
        resultEntry.put("meta", meta);

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        resultEntry.put("schemas", schemas);
    }
}