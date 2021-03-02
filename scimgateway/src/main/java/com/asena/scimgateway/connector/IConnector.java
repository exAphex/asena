package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.List;

import com.asena.scimgateway.model.ModificationStep;
import com.asena.scimgateway.model.RemoteSystem;

public interface IConnector {
    public RemoteSystem getRemoteSystemTemplate();
    public void setupConnector(RemoteSystem rs);
    public String getNameId();
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception;
    public String updateEntity(String entity, ModificationStep ms) throws Exception;
    public boolean deleteEntity(String entity, HashMap<String, Object> data) throws Exception;
    public List<HashMap<String,Object>> getEntities(String entity) throws Exception;
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception;
}