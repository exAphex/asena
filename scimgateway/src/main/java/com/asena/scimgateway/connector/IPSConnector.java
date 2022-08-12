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

    private String nameId = "dn";

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("endpoint", "https://ipsproxyabcd.hana.ondemand.com/api/test",
                "Endpoint of IPS", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthtokenurl", "sdsdsd", "OAuth Token URL", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthclientid", "clientid", "OAuth Client ID", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("oauthclientsecred", "client secret", "OAuth Client Secret", false,
                ConnectionPropertyType.STRING));
        retSystem.setType("IPS");

        retSystem.addAttribute(new Attribute("cn", "cn", "common name"));
        retSystem.addAttribute(new Attribute("dn", "dn", "distinguished name"));
        retSystem.addAttribute(new Attribute("sn", "sn", "second name"));
        retSystem.addAttribute(new Attribute("objectClass", "objectClass", "object class"));

        EntryTypeMapping emUser = new EntryTypeMapping("Users");
        emUser.addWriteMapping(new Attribute("$.userName", "cn", ""));
        emUser.addWriteMapping(new Attribute("$.userName", "uid", ""));
        emUser.addWriteMapping(new Attribute("$.name.familyName", "sn", ""));
        emUser.addWriteMapping(new Attribute("$.userName", "homeDirectory", ""));
        emUser.addWriteMapping(new Attribute("", "gidNumber", ""));
        emUser.addWriteMapping(new Attribute("", "objectClass", ""));

        emUser.addReadMapping(new Attribute("cn", "$.userName", ""));
        emUser.addReadMapping(new Attribute("sn", "$.name.familyName", ""));
        retSystem.addEntryTypeMapping(emUser);

        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        for (ConnectionProperty cp : conns) {
            switch (cp.getKey()) {
                case "host":
                    break;
                case "port":
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