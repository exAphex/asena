package com.asena.scimgateway.connector;

import java.util.HashMap;

import com.asena.scimgateway.model.RemoteSystem;

public interface IConnector {
    public RemoteSystem getRemoteSystemTemplate();
    public void setupConnector(RemoteSystem rs);
    public void writeData(String type, RemoteSystem rs, HashMap<String, Object> data) throws Exception;
}