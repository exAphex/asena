package com.asena.scimgateway.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.http.HTTPClient;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;
import com.asena.scimgateway.utils.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        retSystem.setType("SAP Analytics Cloud");

        retSystem.addAttribute(new Attribute("userName", "userName", "User name"));
        retSystem.addAttribute(new Attribute("id", "id", "User Id"));
        retSystem.addAttribute(new Attribute("preferredLanguage", "preferredLanguage", "Prefered language")); 
        retSystem.addAttribute(new Attribute("givenName", "givenName", "First name"));
        retSystem.addAttribute(new Attribute("familyName", "familyName", "Last name"));
        retSystem.addAttribute(new Attribute("displayName", "displayName", "Displayname"));
        retSystem.addAttribute(new Attribute("active", "active", "is active"));
        retSystem.addAttribute(new Attribute("emails", "emails", "Email adress"));
        retSystem.addAttribute(new Attribute("roles", "roles", "User roles"));
        retSystem.addAttribute(new Attribute("groups", "groups", "User groups"));

        retSystem.addWriteMapping(new Attribute("$.userName", "id", ""));
        retSystem.addWriteMapping(new Attribute("$.userName", "userName", ""));
        retSystem.addWriteMapping(new Attribute("$.preferredLanguage", "preferredLanguage", ""));
        retSystem.addWriteMapping(new Attribute("$.name.givenName", "givenName", ""));
        retSystem.addWriteMapping(new Attribute("$.name.familyName", "familyName", ""));
        retSystem.addWriteMapping(new Attribute("$.displayName", "displayName", ""));
        retSystem.addWriteMapping(new Attribute("$.active", "active", ""));

        retSystem.addReadMapping(new Attribute("id", "$.id", ""));
        retSystem.addReadMapping(new Attribute("userName", "$.userName", "")); 
        retSystem.addReadMapping(new Attribute("preferredLanguage", "$.preferredLanguage", ""));
        retSystem.addReadMapping(new Attribute("givenName", "$.name.givenName", ""));
        retSystem.addReadMapping(new Attribute("familyName", "$.name.familyName", ""));
        retSystem.addReadMapping(new Attribute("displayName", "$.displayName", ""));
        retSystem.addReadMapping(new Attribute("active", "$.active", ""));

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
        return "id";
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

    @SuppressWarnings("unchecked")
    @Override
    public List<HashMap<String, Object>> getEntities(String entity) throws Exception {
        HTTPClient hc = new HTTPClient();
        hc.setOAuth(true);
        hc.setoAuthURL(this.oauthURL);
        hc.setUserName(this.oauthUser);
        hc.setPassword(this.oauthPassword);

        String result = hc.get(this.sacURL + "/Users");
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(result, map.getClass());


        return transformEntityListFrom((List<HashMap<String, Object>>) map.get("Resources"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        String userId = (String) ConnectorUtil.getAttributeValue(getNameId(), data);
        if (userId == null) {
            throw new InternalErrorException("UserID not found in read mapping!");
        }

        HTTPClient hc = new HTTPClient();
        hc.setOAuth(true);
        hc.setoAuthURL(this.oauthURL);
        hc.setUserName(this.oauthUser);
        hc.setPassword(this.oauthPassword);

        String result = hc.get(this.sacURL + "/Users/" + userId);
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(result, map.getClass());

        // TODO Auto-generated method stub
        return transformEntityFrom(map);
    }

    private List<HashMap<String,Object>> transformEntityListFrom(List<HashMap<String,Object>> entities) {
        List<HashMap<String,Object>> retList = new ArrayList<>();
        if (entities == null) {
            throw new InternalErrorException("No entity returned!");
        }

        for (HashMap<String,Object> e : entities) {
            retList.add(transformEntityFrom(e));
        }

        return retList;
    }

    private HashMap<String, Object> transformEntityFrom(HashMap<String,Object> entity) {
        HashMap<String,Object> tmpEntity = new HashMap<>();
        tmpEntity.put("id", getFromJSONPath("$.id", entity));
        tmpEntity.put("userName", getFromJSONPath("$.userName", entity));
        tmpEntity.put("preferredLanguage", getFromJSONPath("$.preferredLanguage", entity));
        tmpEntity.put("givenName",getFromJSONPath("$.name.givenName", entity));
        tmpEntity.put("familyName",getFromJSONPath("$.name.familyName", entity));
        tmpEntity.put("displayName",getFromJSONPath("$.displayName", entity));
        tmpEntity.put("active",getFromJSONPath("$.active", entity));
        return tmpEntity;
    }

    private Object getFromJSONPath(String path, Object obj) {
        Object retObj = null;
        try {
            retObj = JSONUtil.getObjectFromPath(obj, path);
        } catch (Exception e) {
            retObj = null;
        }
        return retObj;
    }
    
}