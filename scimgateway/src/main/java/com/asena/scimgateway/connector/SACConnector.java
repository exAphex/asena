package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

public class SACConnector implements IConnector {

    private String sacURL;
    private String oauthURL;
    private String oauthUser;
    private String oauthPassword;

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("sac.url", "https://example.eu10.hcs.cloud.sap/api/v1/scim/", "URL to SAC", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauth.url", "https://example.authentication.eu10.hana.ondemand.com/oauth/token",
                "OAuth token url", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauth.user", "x-xxxxxx-xxxx-xxxx-xxxx-xxxxxxxx!xxxxxxx|client!b3650",
                "OAuth user name", true, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauth.password", "adminpassword", "Oauth user password", false, ConnectionPropertyType.STRING));
        retSystem.setType("SAP Analyitcs Cloud");

        retSystem.addAttribute(new Attribute("cn", "cn", "common name"));
        retSystem.addAttribute(new Attribute("dn", "dn", "distinguished name"));
        retSystem.addAttribute(new Attribute("sn", "sn", "second name")); 
        retSystem.addAttribute(new Attribute("objectClass", "objectClass", "object class"));

        retSystem.addWriteMapping(new Attribute("$.userName", "cn", ""));
        retSystem.addWriteMapping(new Attribute("$.userName", "uid", ""));
        retSystem.addWriteMapping(new Attribute("$.name.familyName", "sn", ""));
        retSystem.addWriteMapping(new Attribute("$.userName", "homeDirectory", ""));
        retSystem.addWriteMapping(new Attribute("", "gidNumber", ""));
        retSystem.addWriteMapping(new Attribute("", "objectClass", ""));

        retSystem.addReadMapping(new Attribute("cn", "$.userName", ""));
        retSystem.addReadMapping(new Attribute("sn", "$.name.familyName", "")); 

        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        for (ConnectionProperty cp : conns) {
            switch (cp.getKey()) {
                case "sac.url":
                    this.sacURL = cp.getValue();
                    break;
                case "oauth.url":
                    this.oauthURL = cp.getValue();
                    break;
                case "oauth.user":
                    this.oauthUser = cp.getValue();
                    break;
                case "oauth.password":
                    this.oauthPassword = cp.getValue();
                    break;
            }
        }
    }

    @Override
    public String getNameId() {
        return "$.id";
    }

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteEntity(String entity, HashMap<String, Object> data) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<HashMap<String, Object>> getEntities(String entity) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
}