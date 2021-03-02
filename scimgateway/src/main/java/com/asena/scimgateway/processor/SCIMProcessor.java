package com.asena.scimgateway.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.Modification;
import com.asena.scimgateway.model.ModificationStep;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Modification.ModificationType;
import com.asena.scimgateway.utils.JSONUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class SCIMProcessor {
    private String entity;
    private RemoteSystem remoteSystem;

    public SCIMProcessor(RemoteSystem rs, String entity) {
        setEntity(entity);
        setRemoteSystem(rs);
    }

    public HashMap<String, Object> getEntities() throws Exception {
        IConnector conn = getConnector();
        List<HashMap<String, Object>> data = transferGetEntitiesToConnector(conn);
        data = prepareListDataFromRemoteSystem(data);

        HashMap<String, Object> scimResult = SCIMResultProcessor.createSCIMResult(data);
        return scimResult;
    }

    public HashMap<String, Object> getEntity(String userId) throws Exception {
        IConnector conn = getConnector();

        HashMap<String, Object> data = postPrepareDataToRemoteSystem(conn, remoteSystem, userId, new HashMap<>());
        data = transferGetUserToConnector(conn, data);
        data = prepareDataFromRemoteSystem(data);
        return data;
    }

    public HashMap<String, Object> patchEntity(String entityId, HashMap<String, Object> obj) throws Exception {
        IConnector conn = getConnector();
        List<Modification> modifications = ModificationProcessor.patch(obj);
        ModificationStep mStep = prepareDataFromRemoteSystem(modifications);
        Modification mId = getWriteNameId(conn, entityId);
 
        mStep.upsertModification(mId);
        mStep.setId((String) mId.getValue());

        String id = transferUpdateToConnector(conn, mStep);
        id = processId(id, getReadMappingNameId(conn));

        SCIMResultProcessor.addMetaDataCreate(obj, remoteSystem, id, entity);
        return null;
    }

    public HashMap<String, Object> createEntity(HashMap<String, Object> obj) throws Exception {
        IConnector conn = getConnector();
        HashMap<String, Object> data = prepareDataToRemoteSystem(obj);

        String id = transferCreateToConnector(conn, data);
        id = processId(id, getReadMappingNameId(conn));

        SCIMResultProcessor.addMetaDataCreate(obj, remoteSystem, id, entity);
        return obj;
    }

    public HashMap<String, Object> updateEntity(String entityId, HashMap<String, Object> obj) throws Exception {
        IConnector conn = getConnector();
        HashMap<String, Object> data = prepareDataToRemoteSystem(obj);
        data = postPrepareDataToRemoteSystem(conn, remoteSystem, entityId, data);

        String id = transferUpdateToConnector(conn, data);
        id = processId(id, getReadMappingNameId(conn));

        SCIMResultProcessor.addMetaDataCreate(obj, remoteSystem, id, entity);
        return obj;
    }

    public boolean deleteEntity(String entityId) throws Exception {
        IConnector conn = getConnector();
        HashMap<String, Object> data = new HashMap<>();
        data = postPrepareDataToRemoteSystem(conn, remoteSystem, entityId, data);

        return transferDeleteToConnector(conn, data);
    }

    private ModificationStep prepareDataFromRemoteSystem(List<Modification> data) {
        Set<Attribute> attrs = getWriteMappings();
        ModificationStep retModifications = new ModificationStep();

        if (data == null) {
            throw new InternalErrorException("No data for modification found!");
        }

        for (Attribute a : attrs) {
            Object o = null;
            Modification m = null;
            if (((a.getSource() == null) || (a.getSource().length() < 1)) && (a.getDestination() != null)) {
                o = null;
            } else {
                try {
                    m = findInModifications(data, a.getSource());
                    o = m.getValue();
                } catch (Exception e) {
                    continue;
                }
            }
            if (a.getTransformation() != null) {
                o = ScriptProcessor.processTransformation(a, o, remoteSystem);
            }
            retModifications.addModification(new Modification(a.getDestination(), o, (m != null ? m.getType() : ModificationType.SIMPLE)));
        }

        return retModifications;
    }

    private Modification findInModifications(List<Modification> data, String attr) {
        String finalAttrName = attr.substring(2);
        for (Modification m : data) {
            if (finalAttrName.equals(m.getAttributeName())) {
                return m;
            }
        }

        throw new InternalErrorException("Attribute not found!");
    }

    private HashMap<String, Object> prepareDataFromRemoteSystem(HashMap<String, Object> entry) {
        Set<Attribute> attrs = getReadMappings();
        DocumentContext jsonContext = JsonPath.parse("{}");
        for (Attribute a : attrs) {
            Object attrObj = entry.get(a.getSource());
            if (a.getTransformation() != null) {
                attrObj = ScriptProcessor.processTransformation(a, attrObj, remoteSystem);
            }
            JSONUtil.create(jsonContext, a.getDestination(), attrObj);
        }
        HashMap<String, Object> tmpObj = jsonContext.read("$");
        SCIMResultProcessor.addMetaDataList(tmpObj, entry, remoteSystem, (String)tmpObj.get("id"), entity);
        return tmpObj;
    }

    private HashMap<String, Object> prepareDataToRemoteSystem(HashMap<String, Object> obj) {
        Set<Attribute> attrs = getWriteMappings();
        HashMap<String, Object> data = new HashMap<>();

        for (Attribute a : attrs) {
            Object o = null;
            if (((a.getSource() == null) || (a.getSource().length() < 1)) && (a.getDestination() != null)) {
                o = null;
            } else {
                try {
                    o = JSONUtil.getObjectFromPath(obj, a.getSource());
                } catch (Exception e) {
                    continue;
                }
            }
            if (a.getTransformation() != null) {
                o = ScriptProcessor.processTransformation(a, o, remoteSystem);
            }
            data.put(a.getDestination(), o);
        }

        return data;
    }

    private List<HashMap<String, Object>> prepareListDataFromRemoteSystem(List<HashMap<String, Object>> obj) {
        List<HashMap<String, Object>> retList = new ArrayList<>();

        for (HashMap<String, Object> d : obj) {
            HashMap<String, Object> tmpObj = prepareDataFromRemoteSystem(d);
            retList.add(tmpObj);
        }

        return retList;
    }

    private Attribute getWriteMappingNameId(IConnector conn) {
        String writeNameId = conn.getNameId();
        Set<Attribute> writeMappings = getWriteMappings();
        for (Attribute a : writeMappings) {
            if (writeNameId.equals(a.getDestination())) {
                return a;
            }
        }

        throw new InternalErrorException("No write mapping with nameId " + writeNameId + " found!");
    }

    private Modification getWriteNameId(IConnector conn, String id) {
        String nameId = conn.getNameId();
        String newId = processId(id, getWriteMappingNameId(conn));
        return new Modification(nameId, newId);
    }

    private HashMap<String, Object> postPrepareDataToRemoteSystem(IConnector conn, RemoteSystem rs, String id, HashMap<String, Object> data) {
        String nameId = conn.getNameId();
        String newId = processId(id, getWriteMappingNameId(conn));
        if (data.containsKey(nameId)) {
            data.replace(nameId, newId);
        } else {
            data.put(nameId, newId);
        }
        return data;
    }

    private Attribute getReadMappingNameId(IConnector conn) {
        String readNameId = "$.id";

        Set<Attribute> readMappings = getReadMappings();
        for (Attribute a : readMappings) {
            if (readNameId.equals(a.getDestination())) {
                return a;
            }
        }

        throw new InternalErrorException("No read mapping with nameId " + readNameId + " found!");
    }

    private Set<Attribute> getReadMappings() {
        if ((remoteSystem == null) || (remoteSystem.getEntryTypeMappings() == null)) {
            throw new InternalErrorException("Cannot retrieve read mappings");
        }

        for (EntryTypeMapping em : remoteSystem.getEntryTypeMappings()) {
            if (entity.equals(em.getName())) {
                return em.getReadMappings();
            }
        }

        throw new InternalErrorException("No read mapping found for entity: " + entity + " on target system: " + remoteSystem.getName());
    }

    private Set<Attribute> getWriteMappings() {
        if ((remoteSystem == null) || (remoteSystem.getEntryTypeMappings() == null)) {
            throw new InternalErrorException("Cannot retrieve read mappings");
        }

        for (EntryTypeMapping em : remoteSystem.getEntryTypeMappings()) {
            if (entity.equals(em.getName())) {
                return em.getWriteMappings();
            }
        }

        throw new InternalErrorException("No write mapping found for entity: " + entity + " on target system: " + remoteSystem.getName());
    }

    private String processId(String o, Attribute a) {
        if (a.getTransformation() != null) {
            o = (String) ScriptProcessor.processTransformation(a, o, remoteSystem);
        }

        return o;
    }

    private IConnector getConnector() {
        IConnector conn = ConnectorProcessor.getConnectorByType(remoteSystem.getType());
        if (conn == null) {
            throw new InternalErrorException("No matching connector with type " + remoteSystem.getType() + " found!");
        }
        checkNameId(conn);

        return conn;
    }

    private void checkNameId(IConnector conn) {
        if ((getReadMappings() == null) || (getReadMappings().size() < 1)) {
            throw new InternalErrorException("No read mappings configured!");
        }

        if ((getWriteMappings() == null) || (getWriteMappings().size() < 1)) {
            throw new InternalErrorException("No write mappings configured!");
        }

        getReadMappingNameId(conn);
        getWriteMappingNameId(conn);
    }

    private String transferCreateToConnector(IConnector conn, HashMap<String, Object> data)
            throws Exception {
       
        conn.setupConnector(remoteSystem);
        return conn.createEntity(entity, data);
    }

    private String transferUpdateToConnector(IConnector conn, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(remoteSystem);
        return conn.updateEntity(entity, data);
    }

    private String transferUpdateToConnector(IConnector conn, ModificationStep ms)
            throws Exception {
        conn.setupConnector(remoteSystem);
        return null;
    }

    private boolean transferDeleteToConnector(IConnector conn, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(remoteSystem);
        return conn.deleteEntity(entity, data);
    }

    private List<HashMap<String, Object>> transferGetEntitiesToConnector(IConnector conn)
            throws Exception {
        conn.setupConnector(remoteSystem);
        return conn.getEntities(entity);
    }

    private HashMap<String, Object> transferGetUserToConnector(IConnector conn, HashMap<String, Object> data)
            throws Exception {
        conn.setupConnector(remoteSystem);
        return conn.getEntity(entity, data);
    }


    public void setRemoteSystem(RemoteSystem remoteSystem) {
        this.remoteSystem = remoteSystem;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

}