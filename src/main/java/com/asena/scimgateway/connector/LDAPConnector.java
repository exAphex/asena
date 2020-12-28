package com.asena.scimgateway.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
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

        retSystem.setWriteNameId(new Attribute("", "dn", ""));
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

    private String createEntity(HashMap<String, Object> data) {
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

    private void upsertAttribute(LdapConnection conn, String dn, String attr, String value) throws LdapException  {
		Modification modAttr = new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, attr);

		if (value == null) {
			modAttr.setOperation(ModificationOperation.REMOVE_ATTRIBUTE);
		} else {
			modAttr.getAttribute().add(value);
		}
		
		conn.modify(dn, modAttr);
	}
	
	private void upsertAttributeArray(LdapConnection conn, String dn, String attr, List<String> valueList) throws LdapException {
		Modification modAttr = new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, attr);
		
		for (String s: valueList) {
            modAttr.getAttribute().add(s);
		}
		
		if ((valueList == null) || (valueList.size() == 0)) {
			modAttr.setOperation(ModificationOperation.REMOVE_ATTRIBUTE);
			org.apache.directory.api.ldap.model.entry.Attribute a = modAttr.getAttribute();
			a.clear();
		}
		
		conn.modify(dn, modAttr);
	}

    private String updateEntity(HashMap<String, Object> data) {
        LdapConnection connection = new LdapNetworkConnection(this.host, this.port);
        String retStr = null;
        try {
            connection.bind(this.user, this.password);
            retStr = (String)ConnectorUtil.getAttributeValue(nameId, data);
            for (String key : data.keySet()) {
                if (!key.equals(nameId)) {
                    Object objData = data.get(key);
                    if (objData instanceof NativeArray) {
                        NativeArray tempArr = (NativeArray) objData;
                        List<String> lstValues = new ArrayList<String>();
                        for (int i = 0; i < tempArr.size(); i++) {
                            lstValues.add((String) tempArr.get(i));
                        }
                        upsertAttributeArray(connection, retStr, key, lstValues);
                    } else {
                        upsertAttribute(connection, retStr, key, (String)data.get(key));
                    }
                }
            } 

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

    @Override
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception {
        return updateEntity(data);
    }

    @Override
    public boolean deleteEntity(String entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        return createEntity(data);
    }
    
}