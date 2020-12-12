package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

public class LDAPConnector implements IConnector {

    private String host;
    private int port;
    private String user;
    private String password;

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("host", "example.com", "Hostname of the ldap server", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(
                new ConnectionProperty("port", "389", "Port of the ldap server", false, ConnectionPropertyType.INT));
        retSystem.addProperty(new ConnectionProperty("user", "uid=admin,dc=example,dc=com",
                "[OPTIONAL] Communication user", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("password", "test1234",
                "[OPTIONAL] Password of communication user", true, ConnectionPropertyType.STRING));
        retSystem.setType("LDAP");
        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        for (ConnectionProperty cp : conns) {
            switch (cp.getKey()) {
                case "host":
                    this.host = cp.getValue();
                    break;
                case "port":
                    this.port = Integer.parseInt(cp.getValue());
                    break;
                case "user":
                    this.user = cp.getValue();
                    break;
                case "password":
                    this.password = cp.getValue();
                    break;
            }
        }
    }

    @Override
    public void writeData(String type, HashMap<String, Object> data) throws Exception {
        switch (type) {
            case "CreateUser":
                createEntity(data);
                break;
            case "CreateGroup":
                createEntity(data);
        } 
    }

    public void createEntity(HashMap<String, Object> data) throws Exception {
        LdapConnection connection = new LdapNetworkConnection(this.host, this.port);
        connection.bind(this.user, this.password);
        DefaultEntry newEntry = new DefaultEntry();
        newEntry.setDn((String)ConnectorUtil.getAttributeValue("dn", data));

        for (String key : data.keySet()) {
            if (!key.equals("dn")) {
                newEntry.add(key, (String)data.get(key));
            }
        }
        connection.add(newEntry);
        
        connection.unBind();
        connection.close();
    }
    
}