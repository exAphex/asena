package com.asena.scimgateway.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asena.scimgateway.connector.utils.oneidentity.OneIdentityInterceptor;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.http.BasicAuthInterceptor;
import com.asena.scimgateway.http.HTTPClient;
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

public class OneIdentityConnector implements IConnector {

	private String host;
	private String apiEndPoint;
	private String authEndPoint;
	private String userName;
	private String password;
	private String authString;
	private String apiEntityEndPoint;

	@Override
	public RemoteSystem getRemoteSystemTemplate() {
		RemoteSystem retSystem = new RemoteSystem();
		retSystem.addProperty(new ConnectionProperty("host", "http://10.10.110.42", "Hostname of the app server", false,
				ConnectionPropertyType.STRING));

		retSystem.addProperty(new ConnectionProperty("apiendpoint", "/AppServer/api/entities/",
				"API endpoint of the AppServer", false, ConnectionPropertyType.STRING));
		retSystem.addProperty(new ConnectionProperty("authstring", "Module=DialogUser;User=<user>;Password=<pass>",
				"Auth string", false, ConnectionPropertyType.STRING));
		retSystem.addProperty(new ConnectionProperty("authendpoint", "/AppServer/auth/apphost",
				"Endpoint of the Auth API", true, ConnectionPropertyType.STRING));
		retSystem.addProperty(new ConnectionProperty("username", "viadmin", "Basic auth user name", false,
				ConnectionPropertyType.STRING));
		retSystem.addProperty(new ConnectionProperty("password", "pass", "Basic auth user password", false,
				ConnectionPropertyType.STRING));
		retSystem.addProperty(new ConnectionProperty("apientityendpoint", "/AppServer/api/entity/",
				"API endpoint for single entitiy calls", false, ConnectionPropertyType.STRING));
		retSystem.setType("OneIdentity");

		retSystem.addAttribute(new Attribute("display", "display", "display name"));
		retSystem.addAttribute(new Attribute("longDisplay", "longDisplay", "long display name"));
		retSystem.addAttribute(new Attribute("values.CentralAccount", "values.CentralAccount", "account name"));
		retSystem.addAttribute(new Attribute("values.UID_Person", "values.UID_Person", "id"));

		EntryTypeMapping emUser = new EntryTypeMapping("Users");
		emUser.addWriteMapping(new Attribute("$.userName", "values.CentralAccount", ""));
		emUser.addWriteMapping(new Attribute("$.userName", "uid", ""));

		emUser.addReadMapping(new Attribute("values.CentralAccount", "$.userName", ""));
		emUser.addReadMapping(new Attribute("values.UID_Person", "$.id", ""));
		retSystem.addEntryTypeMapping(emUser);

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
				case "apiendpoint":
					this.apiEndPoint = cp.getValue();
					break;
				case "authstring":
					this.authString = cp.getValue();
					break;
				case "authendpoint":
					this.authEndPoint = cp.getValue();
					break;
				case "username":
					this.userName = cp.getValue();
					break;
				case "password":
					this.password = cp.getValue();
					break;
				case "apientityendpoint":
					this.apiEntityEndPoint = cp.getValue();
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
		OneIdentityInterceptor oi = new OneIdentityInterceptor(this.host + this.authEndPoint, this.authString,
				this.userName, this.password);
		BasicAuthInterceptor bi = new BasicAuthInterceptor(this.userName, this.password);

		HTTPClient hc = new HTTPClient();
		hc.addInterceptor(oi);
		hc.addInterceptor(bi);
		hc.setUserName(this.userName);
		hc.setPassword(this.password);

		HashMap<String, Object> valuesObj = new HashMap<>();
		valuesObj.put("values", data);
		DocumentContext jsonContext = JsonPath.parse(valuesObj);

		String retUser = hc.post(this.host + this.apiEntityEndPoint + "Person", jsonContext.jsonString());
		ObjectMapper mapper = new ObjectMapper();

		HashMap<String, Object> map = new HashMap<>();
		map = mapper.readValue(retUser, map.getClass());

		return (String) JSONUtil.getFromJSONPath("$.uid", map);
	}

	@Override
	public String updateEntity(String entity, ModificationStep ms) throws Exception {
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
	public List<HashMap<String, Object>> getEntities(String entity, Map<String, String> params) throws Exception {
		OneIdentityInterceptor oi = new OneIdentityInterceptor(this.host + this.authEndPoint, this.authString,
				this.userName, this.password);
		BasicAuthInterceptor bi = new BasicAuthInterceptor(this.userName, this.password);

		HTTPClient hc = new HTTPClient();
		hc.addInterceptor(oi);
		hc.addInterceptor(bi);
		hc.setUserName(this.userName);
		hc.setPassword(this.password);

		String result = hc.get(this.host + this.apiEndPoint + "Person");
		ObjectMapper mapper = new ObjectMapper();

		List<HashMap<String, Object>> map = new ArrayList<>();
		map = mapper.readValue(result, map.getClass());

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getEntity(String entity, HashMap<String, Object> data) throws Exception {
		String userId = (String) ConnectorUtil.getAttributeValue(getNameId(), data);
		if (userId == null) {
			throw new InternalErrorException("UserID not found in read mapping!");
		}

		OneIdentityInterceptor oi = new OneIdentityInterceptor(this.host + this.authEndPoint, this.authString,
				this.userName, this.password);
		BasicAuthInterceptor bi = new BasicAuthInterceptor(this.userName, this.password);

		HTTPClient hc = new HTTPClient();
		hc.addInterceptor(oi);
		hc.addInterceptor(bi);
		hc.setUserName(this.userName);
		hc.setPassword(this.password);

		String result = hc.get(this.host + this.apiEntityEndPoint + "Person/" + userId);
		ObjectMapper mapper = new ObjectMapper();

		HashMap<String, Object> map = new HashMap<>();
		map = mapper.readValue(result, map.getClass());

		return map;

	}

}