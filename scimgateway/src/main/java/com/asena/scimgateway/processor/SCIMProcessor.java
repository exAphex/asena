package com.asena.scimgateway.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.utils.JSONUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class SCIMProcessor {

    private SCIMProcessor() {
    }

    public static HashMap<String, Object> getUser(RemoteSystem rs, String userId) throws Exception {
        IConnector conn = getConnector(rs);

        HashMap<String, Object> data = postPrepareDataToRemoteSystem(conn, rs, userId, new HashMap<>());
        data = transferGetUserToConnector(conn, "User", rs, data);
        data = prepareDataFromRemoteSystem(rs, data);
        return data;
    }

    @SuppressWarnings("unchecked")
    public static Object createUser(RemoteSystem rs, Object obj) throws Exception {
        IConnector conn = getConnector(rs);
        HashMap<String, Object> data = prepareDataToRemoteSystem(rs, obj);

        String id = transferCreateToConnector(conn, "User", rs, data);
        id = processReturningId(id, getReadMappingNameId(rs, conn));

        LinkedHashMap<Object, Object> retObj = (LinkedHashMap<Object, Object>) obj;
        SCIMResultProcessor.addMetaDataCreate(retObj, rs, id);
        return retObj;
    }

    @SuppressWarnings("unchecked")
    public static Object updateUser(RemoteSystem rs, String userId, Object obj) throws Exception {
        IConnector conn = getConnector(rs);
        HashMap<String, Object> data = prepareDataToRemoteSystem(rs, obj);
        data = postPrepareDataToRemoteSystem(conn, rs, userId, data);

        String id = transferUpdateToConnector(conn, "User", rs, data);
        id = processReturningId(id, getReadMappingNameId(rs, conn));

        LinkedHashMap<Object, Object> retObj = (LinkedHashMap<Object, Object>) obj;
        SCIMResultProcessor.addMetaDataCreate(retObj, rs, id);;
        return retObj;
    }

    public static boolean deleteUser(RemoteSystem rs, String userId) throws Exception {
        IConnector conn = getConnector(rs);
        HashMap<String, Object> data = new HashMap<>();
        data = postPrepareDataToRemoteSystem(conn, rs, userId, data);

        return transferDeleteToConnector(conn, "User", rs, data);
    }

    public static HashMap<String, Object> getUsers(RemoteSystem rs) throws Exception {
        IConnector conn = getConnector(rs);
        List<HashMap<String, Object>> data = transferGetUsersToConnector(conn, "User", rs);
        data = prepareListDataFromRemoteSystem(rs, data);

        HashMap<String, Object> scimResult = SCIMResultProcessor.createSCIMResult(data);
        return scimResult;
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

    private static List<HashMap<String, Object>> prepareListDataFromRemoteSystem(RemoteSystem rs,
            List<HashMap<String, Object>> obj) {
        List<HashMap<String, Object>> retList = new ArrayList<>();

        for (HashMap<String, Object> d : obj) {
            HashMap<String, Object> tmpObj = prepareDataFromRemoteSystem(rs, d);
            retList.add(tmpObj);
        }

        return retList;
    }

    private static HashMap<String, Object> prepareDataFromRemoteSystem(RemoteSystem rs, HashMap<String, Object> entry) {
        Set<Attribute> attrs = rs.getReadMappings();
        DocumentContext jsonContext = JsonPath.parse("{}");
        for (Attribute a : attrs) {
            Object attrObj = entry.get(a.getSource());
            if (a.getTransformation() != null) {
                attrObj = ScriptProcessor.processTransformation(a, attrObj);
            }
            create(jsonContext, a.getDestination(), attrObj);
        }
        HashMap<String, Object> tmpObj = jsonContext.read("$");
        SCIMResultProcessor.addMetaDataList(tmpObj, entry, rs, (String)tmpObj.get("id"));
        return tmpObj;
    }

    private static HashMap<String, Object> prepareDataToRemoteSystem(RemoteSystem rs, Object obj) {
        Set<Attribute> attrs = rs.getWriteMappings();
        HashMap<String, Object> data = new HashMap<>();

        for (Attribute a : attrs) {
            Object o = null;
            try {
                if (((a.getSource() == null) || (a.getSource().length() < 1)) && (a.getDestination() != null)) {
                    o = null;
                } else {
                    o = JSONUtil.getObjectFromPath(obj, a.getSource());
                }
                if (a.getTransformation() != null) {
                    o = ScriptProcessor.processTransformation(a, o);
                }
                data.put(a.getDestination(), o);
            } catch (Exception e) {
                continue;
            }
        }

        return data;
    }

    private static HashMap<String, Object> postPrepareDataToRemoteSystem(IConnector conn, RemoteSystem rs, String id, HashMap<String, Object> data) {
        String nameId = conn.getNameId();
        String newId = processWritingId(id, getWriteMappingNameId(rs, conn));
        if (data.containsKey(nameId)) {
            data.replace(nameId, newId);
        } else {
            data.put(nameId, newId);
        }
        return data;
    }

    private static String transferCreateToConnector(IConnector conn, String type, RemoteSystem rs, HashMap<String, Object> data)
            throws Exception {
       
        conn.setupConnector(rs);
        return conn.createEntity(type, data);
    }

    private static String transferUpdateToConnector(IConnector conn, String type, RemoteSystem rs, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(rs);
        return conn.updateEntity(type, data);
    }

    private static boolean transferDeleteToConnector(IConnector conn, String type, RemoteSystem rs, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(rs);
        return conn.deleteEntity(type, data);
    }

    private static List<HashMap<String, Object>> transferGetUsersToConnector(IConnector conn, String type, RemoteSystem rs)
            throws Exception {
        conn.setupConnector(rs);
        return conn.getEntities(type);
    }

    private static HashMap<String, Object> transferGetUserToConnector(IConnector conn, String type, RemoteSystem rs, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(rs);
        return conn.getEntity(type, data);
    }

    private static void checkNameId(RemoteSystem rs, IConnector conn) {
        if ((rs.getReadMappings() == null) || (rs.getReadMappings().size() < 1)) {
            throw new InternalErrorException("No read mappings configured!");
        }

        if ((rs.getWriteMappings() == null) || (rs.getWriteMappings().size() < 1)) {
            throw new InternalErrorException("No write mappings configured!");
        }

        getReadMappingNameId(rs, conn);
        getWriteMappingNameId(rs, conn);
    }

    private static Attribute getReadMappingNameId(RemoteSystem rs, IConnector conn) {
        String readNameId = "$.id";

        Set<Attribute> readMappings = rs.getReadMappings();
        for (Attribute a : readMappings) {
            if (readNameId.equals(a.getDestination())) {
                return a;
            }
        }

        throw new InternalErrorException("No read mapping with nameId " + readNameId + " found!");
    }

    private static Attribute getWriteMappingNameId(RemoteSystem rs, IConnector conn) {
        String writeNameId = conn.getNameId();
        Set<Attribute> writeMappings = rs.getWriteMappings();
        for (Attribute a : writeMappings) {
            if (writeNameId.equals(a.getDestination())) {
                return a;
            }
        }

        throw new InternalErrorException("No write mapping with nameId " + writeNameId + " found!");
    }

    private static IConnector getConnector(RemoteSystem rs) {
        IConnector conn = ConnectorProcessor.getConnectorByType(rs.getType());
        if (conn == null) {
            throw new InternalErrorException("No matching connector with type " + rs.getType() + " found!");
        }
        checkNameId(rs, conn);

        return conn;
    }

    private static String processReturningId(String o, Attribute a) {
        if (a.getTransformation() != null) {
            o = (String) ScriptProcessor.processTransformation(a, o);
        }

        return o;
    }

    private static String processWritingId(String id, Attribute a) {
        if (a.getTransformation() != null) {
            id = (String) ScriptProcessor.processTransformation(a, id);
        }

        return id;
    }

}