package com.asena.scimgateway.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

public class NoOpConnector implements IConnector {
    private String nameId = "noop";

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(
                new ConnectionProperty("noop", "noop.com", "noopdesc", false, ConnectionPropertyType.STRING));
        retSystem.setType("NOOP");
        retSystem.addAttribute(new Attribute("noop", "noop", "noop desc"));
        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
    }

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        return (String) data.get(this.nameId);
    }

    @Override
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception {
        return (String) data.get(this.nameId);
    }

    @Override
    public boolean deleteEntity(String entity, HashMap<String, Object> data) {
        return true;
    }

    @Override
    public List<HashMap<String, Object>> getEntities(String entity) throws Exception {
        List<HashMap<String,Object>> ret = new ArrayList<>();
        HashMap<String,Object> retObj = new HashMap<>();
        retObj.put("noop", "test");
        ret.add(retObj);
        return ret;
    }

    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        HashMap<String,Object> retObj = new HashMap<>();
        retObj.put("noop", "test");
        return retObj;
    }

    @Override
    public String getNameId() {
        return nameId;
    }
    
}