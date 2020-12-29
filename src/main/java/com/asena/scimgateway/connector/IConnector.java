package com.asena.scimgateway.connector;

import java.util.HashMap;

import com.asena.scimgateway.model.RemoteSystem;

public interface IConnector {
    public RemoteSystem getRemoteSystemTemplate();
    public void setupConnector(RemoteSystem rs);
    public void setNameId(String nameId);
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception;
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception;
    public boolean deleteEntity(String entity, HashMap<String, Object> data) throws Exception;
}