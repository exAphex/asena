package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.http.HTTPClient;
import com.asena.scimgateway.http.oauth.OAuthInterceptor;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.ModificationStep;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;
import com.asena.scimgateway.utils.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class IPSConnector implements IConnector {

    private String nameId = "$.id";
    private String oauthURL = "";
    private String endpointURL = "";
    private String oauthClientId = "";
    private String oauthClientSecret = "";

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("endpoint",
                "https://ipsproxysapiam-abcde123.eu2.hana.ondemand.com/ipsproxy/api/v1/scim/d27a6691-40c7-4bd2-b644-d544fc7ba18f",
                "Endpoint of IPS", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthtokenurl",
                "https://oauthasservices-abcde123.eu2.hana.ondemand.com/oauth2/api/v1/token", "OAuth Token URL", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthclientid", "clientid", "OAuth Client ID", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthclientsecret", "client secret", "OAuth Client Secret", false,
                ConnectionPropertyType.STRING));
        retSystem.setType("SAP Identity Provisioning Service");

        retSystem.addAttribute(new Attribute("$.id", "$.id", "identifier"));
        retSystem.addAttribute(new Attribute("$.userName", "$.userName", "user name"));
        retSystem.addAttribute(new Attribute("$.active", "$.active", "is user active?"));
        retSystem.addAttribute(new Attribute("$.displayName", "$.displayName", "display name"));
        retSystem.addAttribute(new Attribute("$.name.givenName", "$.name.givenName", "first name"));
        retSystem.addAttribute(new Attribute("$.name.familyName", "$.name.familyName", "last name"));

        EntryTypeMapping emUser = new EntryTypeMapping("Users");
        emUser.addWriteMapping(new Attribute("$.id", "$.id", ""));
        emUser.addWriteMapping(new Attribute("$.userName", "$.userName", ""));
        emUser.addWriteMapping(new Attribute("$.active", "$.active", ""));
        emUser.addWriteMapping(new Attribute("$.displayName", "$.displayName", ""));
        emUser.addWriteMapping(new Attribute("$.name.givenName", "$.name.givenName", ""));
        emUser.addWriteMapping(new Attribute("$.name.familyName", "$.name.familyName", ""));

        emUser.addReadMapping(new Attribute("$.id", "$.id", ""));
        emUser.addReadMapping(new Attribute("$.userName", "$.userName", ""));
        emUser.addReadMapping(new Attribute("$.active", "$.active", ""));
        emUser.addReadMapping(new Attribute("$.displayName", "$.displayName", ""));
        emUser.addReadMapping(new Attribute("$.name.givenName", "$.name.givenName", ""));
        emUser.addReadMapping(new Attribute("$.name.familyName", "$.name.familyName", ""));

        retSystem.addEntryTypeMapping(emUser);

        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        for (ConnectionProperty cp : conns) {
            switch (cp.getKey()) {
                case "oauthtokenurl":
                    this.oauthURL = cp.getValue();
                    break;
                case "endpoint":
                    this.endpointURL = cp.getValue();
                    break;
                case "oauthclientid":
                    this.oauthClientId = cp.getValue();
                    break;
                case "oauthclientsecret":
                    this.oauthClientSecret = cp.getValue();
                    break;
            }
        }
    }

    private String transformTo(HashMap<String, Object> data) {
        DocumentContext jsonContext = JsonPath.parse("{}");
        for (String keys : data.keySet()) {
            JSONUtil.addPropertyToJSON(jsonContext, keys, keys, data);
        }
        return jsonContext.jsonString();
    }

    @Override
    public String updateEntity(String entity, ModificationStep ms) throws Exception {
        return null;
    }

    @Override
    public boolean deleteEntity(String entity, HashMap<String, Object> data) {
        boolean retBool = false;
        return retBool;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        String postData = transformTo(data);
        OAuthInterceptor oi = new OAuthInterceptor(this.oauthClientId, this.oauthClientSecret, this.oauthURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setMediaType("application/scim+json");
        hc.setUserName(this.oauthClientId);
        hc.setPassword(this.oauthClientSecret);
        hc.setExpectedResponseCode(201);

        String retUser = hc.post(this.endpointURL + "/" + entity, postData);
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(retUser, map.getClass());

        return (String) JSONUtil.getFromJSONPath(nameId, map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HashMap<String, Object>> getEntities(String entity, Map<String, String> params) throws Exception {
        OAuthInterceptor oi = new OAuthInterceptor(this.oauthClientId, this.oauthClientSecret, this.oauthURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setUserName(this.oauthClientId);
        hc.setPassword(this.oauthClientSecret);

        String url = this.endpointURL + "/" + entity;
        if ((params != null) && (params.size() > 0)) {
            url += "?";
            for (String p : params.keySet()) {
                url += p + "=" + params.get(p);
            }
        }
        String result = hc.get(url);
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(result, map.getClass());

        return (List<HashMap<String, Object>>) map.get("Resources");
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        String userId = (String) ConnectorUtil.getAttributeValue(getNameId(), data);
        if (userId == null) {
            throw new InternalErrorException("UserID not found in read mapping!");
        }

        OAuthInterceptor oi = new OAuthInterceptor(this.oauthClientId, this.oauthClientSecret, this.oauthURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setUserName(this.oauthClientId);
        hc.setPassword(this.oauthClientSecret);

        try {
            String result = hc.get(this.endpointURL + "/" + entity + "/" + userId);
            ObjectMapper mapper = new ObjectMapper();

            HashMap<String, Object> map = new HashMap<>();
            map = mapper.readValue(result, map.getClass());
            return map;
        } catch (Exception e) {
            throw new NotFoundException(userId);
        }
    }

    @Override
    public String getNameId() {
        return nameId;
    }

}