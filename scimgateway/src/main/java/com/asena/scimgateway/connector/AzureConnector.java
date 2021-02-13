package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.http.HTTPClient;
import com.asena.scimgateway.http.oauth.OAuthInterceptor;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;
import com.asena.scimgateway.utils.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class AzureConnector implements IConnector {
    private String baseURL;
    private String domainName;
    private String oauthURL;
    private String oauthUser;
    private String oauthPassword;

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("azure.baseurl", "https://graph.microsoft.com",
                "Base url of microsoft graph api", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("azure.domain.name", "asenaorg.onmicrosoft.com",
                "Domain name of your azure directory", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(
                new ConnectionProperty("oauth.url", "https://login.microsoftonline.com/asenaorg.onmicrosoft.com/oauth2/token",
                        "OAuth token url", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(
                new ConnectionProperty("oauth.user", "x-xxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
                        "OAuth user name", true, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauth.password", "adminpassword", "Oauth user password", false,
                ConnectionPropertyType.STRING));
        retSystem.setType("Microsoft Azure Active Directory");

        retSystem.addAttribute(new Attribute("displayName", "displayName", "Displayname"));
        retSystem.addAttribute(new Attribute("givenName", "givenName", "Givenname"));
        retSystem.addAttribute(new Attribute("jobTitle", "jobTitle", "Job title"));
        retSystem.addAttribute(new Attribute("mail", "mail", "Mail"));
        retSystem.addAttribute(new Attribute("mobilePhone", "mobilePhone", "mobilePhone"));
        retSystem.addAttribute(new Attribute("officeLocation", "officeLocation", "officeLocation"));
        retSystem.addAttribute(new Attribute("preferredLanguage", "preferredLanguage", "preferredLanguage"));
        retSystem.addAttribute(new Attribute("surname", "surname", "surname"));
        retSystem.addAttribute(new Attribute("userPrincipalName", "userPrincipalName", "userPrincipalName"));
        retSystem.addAttribute(new Attribute("id", "id", "id"));
        retSystem.addAttribute(new Attribute("memberOf", "memberOf", "memberOf"));
        retSystem.addAttribute(new Attribute("businessPhones", "businessPhones", "businessPhones"));
        retSystem.addAttribute(new Attribute("passwordProfile", "passwordProfile", "passwordProfile"));
        retSystem.addAttribute(new Attribute("accountEnabled", "accountEnabled", "accountEnabled"));
        retSystem.addAttribute(new Attribute("mailNickname", "mailNickname", "mailNickname"));
        
        

        retSystem.addWriteMapping(new Attribute("$.active", "accountEnabled", ""));
        retSystem.addWriteMapping(new Attribute("$.displayName", "displayName", ""));
        retSystem.addWriteMapping(new Attribute("$.name.givenName", "givenName", ""));
        retSystem.addWriteMapping(new Attribute("$.name.familyName", "surname", ""));
        retSystem.addWriteMapping(new Attribute("$.id", "id", ""));
        retSystem.addWriteMapping(new Attribute("$.userName", "mailNickname", "")); 
        retSystem.addWriteMapping(new Attribute("$.password", "passwordProfile", new Script("getAzurePassword")));
        retSystem.addWriteMapping(new Attribute("$.userName", "userPrincipalName", new Script("getAzureDomain")));

        retSystem.addReadMapping(new Attribute("id", "$.id", ""));
        retSystem.addReadMapping(new Attribute("userPrincipalName", "$.userName", ""));
        retSystem.addReadMapping(new Attribute("displayName", "$.displayName", ""));
        retSystem.addReadMapping(new Attribute("preferredLanguage", "$.preferredLanguage", ""));
        retSystem.addReadMapping(new Attribute("givenName", "$.name.givenName", ""));
        retSystem.addReadMapping(new Attribute("surname", "$.name.familyName", ""));
        retSystem.addReadMapping(new Attribute("mail", "$.emails", "")); 
        

        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        for (ConnectionProperty cp : conns) {
            switch (cp.getKey()) {
                case "azure.baseurl":
                    this.baseURL = cp.getValue();
                    break;
                case "azure.domain.name":
                    this.domainName = cp.getValue();
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

    @SuppressWarnings("unchecked")
    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        OAuthInterceptor oi = new OAuthInterceptor(this.oauthUser, this.oauthPassword, this.oauthURL);
        oi.addBody("resource", this.baseURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setUserName(this.oauthUser);
        hc.setPassword(this.oauthPassword);
        hc.setExpectedResponseCode(201);

        DocumentContext jsonContext = JsonPath.parse(data);

        String retUser = hc.post(this.baseURL + "/v1.0/Users", jsonContext.jsonString());
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(retUser, map.getClass());

        return (String) JSONUtil.getFromJSONPath("$.id", map);
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
        OAuthInterceptor oi = new OAuthInterceptor(this.oauthUser, this.oauthPassword, this.oauthURL);
        oi.addBody("resource", this.baseURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setUserName(this.oauthUser);
        hc.setPassword(this.oauthPassword);

        String result = hc.get(this.baseURL + "/v1.0/Users");
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(result, map.getClass());

        return (List<HashMap<String, Object>>) map.get("value");
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        String userId = (String) ConnectorUtil.getAttributeValue(getNameId(), data);
        if (userId == null) {
            throw new InternalErrorException("UserID not found in read mapping!");
        }
        
        OAuthInterceptor oi = new OAuthInterceptor(this.oauthUser, this.oauthPassword, this.oauthURL);
        oi.addBody("resource", this.baseURL);

        HTTPClient hc = new HTTPClient();
        hc.addInterceptor(oi);
        hc.setUserName(this.oauthUser);
        hc.setPassword(this.oauthPassword);

        String result = hc.get(this.baseURL + "/v1.0/Users/" + userId);
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> map = new HashMap<>();
        map = mapper.readValue(result, map.getClass());

        return map; 
    }
    
}