package com.asena.scimgateway.connector;

import java.util.HashMap;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

public class NoOpConnector implements IConnector {
    private String nameId;

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("noop", "noop.com", "noopdesc", false,
                ConnectionPropertyType.STRING));
        retSystem.setType("NOOP");
        retSystem.addAttribute(new Attribute("noop", "noop", "noop desc"));
        retSystem.setWriteNameId(new Attribute("", "noop", ""));
        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
    }

    @Override
    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        return (String)data.get(this.nameId);
    }

    @Override
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception {
        return (String)data.get(this.nameId);
    }

    @Override
    public boolean deleteEntity(String entity, HashMap<String, Object> data) {
        return true;
    }
    
}