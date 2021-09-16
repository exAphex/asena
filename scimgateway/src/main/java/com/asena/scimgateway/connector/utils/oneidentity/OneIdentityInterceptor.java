package com.asena.scimgateway.connector.utils.oneidentity;

import java.io.IOException;
import java.util.List;

import com.asena.scimgateway.http.BasicAuthInterceptor;
import com.asena.scimgateway.http.HTTPClient;

import net.minidev.json.JSONObject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class OneIdentityInterceptor implements Interceptor {

	private String authUrl;
	private String authString;
	private String userName;
	private String password;

	public OneIdentityInterceptor(String authUrl, String authString, String userName, String password) {
		this.authString = authString;
		this.authUrl = authUrl;
		this.userName = userName;
		this.password = password;
	}

	private String getAuthCookie(String url, String userName, String password, String authString) throws IOException {
		HTTPClient hc = new HTTPClient();
		hc.setUserName(this.userName);
		hc.setPassword(this.password);
		hc.setExpectedResponseCode(200);
		hc.addInterceptor(new BasicAuthInterceptor(this.userName, this.password));

		JSONObject jo = new JSONObject();
		jo.put("authString", authString);

		Response resp = hc.postWithResponse(authUrl, jo.toJSONString());
		List<String> cookies = resp.headers("Set-Cookie");
		return getAuthCookie(cookies, "ss-id=");
	}

	private String getAuthCookie(List<String> cookies, String cookie) throws IOException {
		String retCookie = null;
		if (cookies != null) {
			for (String s : cookies) {
				int authPos = s.indexOf(cookie);
				int commaPos = s.indexOf(";");
				if (authPos >= 0) {
					return s.substring(authPos, commaPos);
				}
			}
		}
		return retCookie;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		String authCookie = getAuthCookie(this.authUrl, this.userName, this.password, this.authString);
		if (authCookie != null) {
			Builder b = request.newBuilder();
			b.addHeader("Cookie", authCookie);

			Request authenticatedRequest = b.build();
			return chain.proceed(authenticatedRequest);
		} else {
			throw new InternalError("Could not retrieve auth cookie");
		}
	}

}