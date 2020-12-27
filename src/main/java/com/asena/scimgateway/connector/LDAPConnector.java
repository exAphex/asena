package com.asena.scimgateway.connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapOperationException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.mozilla.javascript.NativeArray;

public class LDAPConnector implements IConnector {

    private String host;
    private int port;
    private String user;
    private String password;
    private RemoteSystem rs;
    private String nameId;

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

        retSystem.addAttribute(new Attribute("dn", "dn", "distinguished name"));
        retSystem.addAttribute(new Attribute("sn", "sn", "second name")); 
        return retSystem;
    }

    @Override
    public void setupConnector(RemoteSystem rs) {
        Set<ConnectionProperty> conns = rs.getProperties();
        this.rs = rs;
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
    public String writeData(String type, HashMap<String, Object> data) throws Exception {
        switch (type) {
            case "CreateUser":
                return createEntity(data);
            case "CreateGroup":
                return createEntity(data);
            default:
                throw new InternalErrorException("Unsupported operation: " + this.rs.getName());
        }
    }

    public void closeLDAPConnection(LdapConnection conn) {
        try {
            conn.unBind();
            conn.close();
        } catch (LdapException ldap) {
            // TODO Auto-generated catch block
            ldap.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String createEntity(HashMap<String, Object> data) {
        LdapConnection connection = new LdapNetworkConnection(this.host, this.port);
        String retStr = null;
        try {
            connection.bind(this.user, this.password);
            DefaultEntry newEntry = new DefaultEntry();
            retStr = (String)ConnectorUtil.getAttributeValue(nameId, data);
            newEntry.setDn(retStr);

            for (String key : data.keySet()) {
                if (!key.equals(nameId)) {
                    Object objData = data.get(key);
                    if (objData instanceof NativeArray) {
                        NativeArray tempArr = (NativeArray) objData;
                        for (int i = 0; i < tempArr.size(); i++) {
                            newEntry.add(key, (String) tempArr.get(i));
                        }
                    } else {
                        newEntry.add(key, (String)data.get(key));
                    }
                }
            }
            connection.add(newEntry);
        } catch (LdapException ldap) {
            if (ldap instanceof LdapOperationException) {
                LdapOperationException ldapOpErr = (LdapOperationException) ldap;

                throw new InternalErrorException("LDAP Error with code: " + ldapOpErr.getResultCode() + " on entry" + ldapOpErr.getResolvedDn() + " with cause: " + ldapOpErr.getMessage());
            } else {
                throw new InternalErrorException("LDAP Error with cause: " + ldap.getMessage());
            }
        } finally {
            closeLDAPConnection(connection);
        }
        return retStr; 
    }

    @Override
    public void setNameId(String nameId) {
        if ((nameId == null) || (nameId.length() == 0)) {
            throw new InternalErrorException("NameId is not valid!");
        }

        this.nameId = nameId;
    }
    
}