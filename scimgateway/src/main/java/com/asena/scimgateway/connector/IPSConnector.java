package com.asena.scimgateway.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.ModificationStep;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;

public class IPSConnector implements IConnector {

    private String nameId = "id";
    private String oauthURL = "";
    private String endpointURL = "";

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
        retSystem.addProperty(new ConnectionProperty("oauthclientsecred", "client secret", "OAuth Client Secret", false,
                ConnectionPropertyType.STRING));
        retSystem.setType("SAP Identity Provisioning Service");

        retSystem.addAttribute(new Attribute("$.id", "$.id", "identifier"));
        retSystem.addAttribute(new Attribute("$.userName", "$.userName", "user name"));

        EntryTypeMapping emUser = new EntryTypeMapping("Users");
        emUser.addWriteMapping(new Attribute("$.userName", "$.userName", ""));

        emUser.addReadMapping(new Attribute("$.id", "$.id", ""));
        emUser.addReadMapping(new Attribute("$.userName", "$.userName", ""));
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
                case "user":
                    break;
                case "password":
                    break;
                case "searchdn":
                    break;
                case "searchfilter":
                    break;
            }
        }
    }

    private String createEntity(HashMap<String, Object> data) {
        String retStr = null;
        retStr = (String) ConnectorUtil.getAttributeValue(nameId, data);
        return retStr;
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

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        return createEntity(data);
    }

    @Override
    public List<HashMap<String, Object>> getEntities(String entity) throws Exception {
        List<HashMap<String, Object>> retList = new ArrayList<>();
        return retList;
    }

    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        HashMap<String, Object> retObj = new HashMap<>();
        return retObj;
    }

    @Override
    public String getNameId() {
        return nameId;
    }

}