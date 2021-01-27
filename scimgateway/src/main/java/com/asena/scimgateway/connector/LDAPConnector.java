package com.asena.scimgateway.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.ConnectionProperty.ConnectionPropertyType;
import com.asena.scimgateway.utils.ConnectorUtil;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.exception.LdapOperationException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.mozilla.javascript.NativeArray;

public class LDAPConnector implements IConnector {

    private String host;
    private int port;
    private String user;
    private String password;
    private String nameId = "dn";
    private String searchDN;
    private String searchFilter;

    @Override
    public RemoteSystem getRemoteSystemTemplate() {
        RemoteSystem retSystem = new RemoteSystem();
        retSystem.addProperty(new ConnectionProperty("host", "example.org", "Hostname of the ldap server", false,
                ConnectionPropertyType.STRING));
        retSystem.addProperty(
                new ConnectionProperty("port", "389", "Port of the ldap server", false, ConnectionPropertyType.INT));
        retSystem.addProperty(new ConnectionProperty("user", "cn=admin,dc=example,dc=org",
                "[OPTIONAL] Communication user", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("password", "test1234",
                "[OPTIONAL] Password of communication user", true, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("searchdn", "ou=users,dc=example,dc=org", "Entrypoint dn for search users", false, ConnectionPropertyType.STRING));
        retSystem.addProperty(new ConnectionProperty("searchfilter", "(objectclass=*)", "Search filter for users", false, ConnectionPropertyType.STRING));
        retSystem.setType("LDAP");

        retSystem.addAttribute(new Attribute("dn", "dn", "distinguished name"));
        retSystem.addAttribute(new Attribute("sn", "sn", "second name")); 

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
                case "searchdn":
                    this.searchDN = cp.getValue();
                    break;
                case "searchfilter":
                    this.searchFilter = cp.getValue();
                    break;
            }
        }
    }

    public void closeLDAPConnection(LdapConnection conn) {
        try {
            if (conn != null) {
                conn.unBind();
            }
        } catch (LdapException ldap) {
            ldap.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeLDAPCursor(EntryCursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LdapConnection ldapConnect() throws LdapException {
        LdapConnection connection = new LdapNetworkConnection(this.host, this.port);
        connection.bind(this.user, this.password);
        return connection;
    }

    private String createEntity(HashMap<String, Object> data) {
        LdapConnection connection = null; 
        String retStr = null;
        try {
            connection = ldapConnect();
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
            parseLDAPException(ldap, "");
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
        LdapConnection connection = null;
        String retStr = null;
        try {
            connection = ldapConnect();
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
            parseLDAPException(ldap, "");
        } finally {
            closeLDAPConnection(connection);
        } 
        return retStr;
    }

    @Override
    public String updateEntity(String entity, HashMap<String, Object> data) throws Exception {
        return updateEntity(data);
    }

    @Override
    public boolean deleteEntity(String entity, HashMap<String, Object> data) {
        boolean retBool = false;
        LdapConnection connection = null;
        String dn = "";
        try {
            connection = ldapConnect();
            dn = (String)ConnectorUtil.getAttributeValue(nameId, data);
            connection.delete(dn);
            retBool = true;
        } catch (LdapException ldap) {
            parseLDAPException(ldap, dn);
        } finally {
            closeLDAPConnection(connection);
        } 
        return retBool;
    }

    @Override
    public String createEntity(String entity, HashMap<String, Object> data) throws Exception {
        return createEntity(data);
    }

    @Override
    public List<HashMap<String, Object>> getEntities(String entity) throws Exception {
        LdapConnection connection = null;
        List<HashMap<String,Object>> retList = new ArrayList<>();
        EntryCursor cursor = null;
        try {
            connection = ldapConnect();
            cursor = connection.search(searchDN, searchFilter, SearchScope.SUBTREE, "*" );
            while ( cursor.next() )
            {
                Entry entry = cursor.get();
                Collection<org.apache.directory.api.ldap.model.entry.Attribute> col = entry.getAttributes();

                HashMap<String, Object> tmpObj = new HashMap<>();
                tmpObj.put("dn", entry.getDn().toString());
                
                for (org.apache.directory.api.ldap.model.entry.Attribute a : col) {
                    if (a.size() == 1) {
                        tmpObj.put(a.getId(), a.getString());
                    } else {
                        List<String> tmpStringArr = new ArrayList<>();
                        for (Iterator<Value> iterator = a.iterator(); iterator.hasNext();) {
                            Value tmpVal = iterator.next();
                            tmpStringArr.add(tmpVal.getString());
                        }
                        tmpObj.put(a.getId(), tmpStringArr);
                    }
                    
                }
                retList.add(tmpObj);
            }
            cursor.close();
        } catch (LdapException ldap) {
            parseLDAPException(ldap, "");
        } finally {

            closeLDAPConnection(connection);
        } 
        return retList;
    }

    private void parseLDAPException(LdapException ldap, String dn) {
        if (ldap instanceof LdapNoSuchObjectException) {
            throw new NotFoundException(dn);
        } else if (ldap instanceof LdapOperationException) {
            LdapOperationException ldapOpErr = (LdapOperationException) ldap;
            throw new InternalErrorException("LDAP Error with code: " + ldapOpErr.getResultCode() + " on entry" + ldapOpErr.getResolvedDn() + " with cause: " + ldapOpErr.getMessage());
        } else {
            throw new InternalErrorException("LDAP Error with cause: " + ldap.getMessage());
        }
    }

    @Override
    public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
        LdapConnection connection = null;
        HashMap<String, Object> retObj = new HashMap<>();
        try {
            connection = ldapConnect();
            String dn = (String)ConnectorUtil.getAttributeValue(nameId, data);
            Entry e = connection.lookup(dn);
            if (e == null) {
                throw new InternalErrorException("No LDAP entry found on " + dn);
            }
            
            Collection<org.apache.directory.api.ldap.model.entry.Attribute> col = e.getAttributes();

            
            retObj.put("dn", e.getDn().toString());
            
            for (org.apache.directory.api.ldap.model.entry.Attribute a : col) {
                if (a.size() == 1) {
                    retObj.put(a.getId(), a.getString());
                } else {
                    List<String> tmpStringArr = new ArrayList<>();
                    for (Iterator<Value> iterator = a.iterator(); iterator.hasNext();) {
                        Value tmpVal = iterator.next();
                        tmpStringArr.add(tmpVal.getString());
                    }
                    retObj.put(a.getId(), tmpStringArr);
                }
                
            }
        } catch (LdapException ldap) {
            parseLDAPException(ldap, "");
        } finally {

            closeLDAPConnection(connection);
        } 


        // TODO Auto-generated method stub
        return retObj;
    }

    @Override
    public String getNameId() {
        return nameId;
    }
    
}