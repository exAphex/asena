package com.asena.scimgateway.connector;

import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;

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
        retSystem.addProperty(new ConnectionProperty("host", "example.com", "Hostname of the ldap server", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("port", "389", "Port of the ldap server", false, ConnectionPropertyType.INT));
        retSystem.addProperty(new ConnectionProperty("user", "uid=admin,dc=example,dc=com", "[OPTIONAL] Communication user", false, ConnectionPropertyType.STRING)); 
        retSystem.addProperty(new ConnectionProperty("password", "test1234", "[OPTIONAL] Password of communication user", true, ConnectionPropertyType.STRING));
        return null;
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
    public void writeData(String type, RemoteSystem rs, HashMap<String, Object> data) throws Exception {
        // TODO Auto-generated method stub

    }

    public void createUser() {
        LdapConnection connection = new LdapNetworkConnection(this.host, this.port);
        connection.bind(bindDN,bindPassword);
        DefaultEntry newEntry = new DefaultEntry();
        newEntry.setDn(dn);
        for(Iterator iterator = json.keySet().iterator(); iterator.hasNext();) {
            Object tmpObj = iterator.next();
            String key = (String) tmpObj;
            Object value = json.get(tmpObj);
            
            if (value instanceof JSONArray) {
                JSONArray tmpArray = (JSONArray) value;
                for (Object o: tmpArray) {
                    newEntry.add(key, (String)o);
                }
            } else if (value instanceof String) {
                newEntry.add(key, (String) value);
            }
        }
        
        connection.add(newEntry);
        
        connection.unBind();
        connection.close();
        retJSON.put("operation", "success");
        return retJSON;
    }
    
}