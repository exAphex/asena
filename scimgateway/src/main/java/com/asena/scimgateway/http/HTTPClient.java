package com.asena.scimgateway.http;

import java.io.IOException;

import com.asena.scimgateway.http.oauth.OAuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public class HTTPClient {

    private boolean isOAuth;
    private String oAuthURL;
    private String userName;
    private String password;
    private int expectedResponseCode = 200;
    private OkHttpClient client;

    public OkHttpClient buildClient() {
        Builder b = new OkHttpClient.Builder();
        if (isOAuth) {
            b.addInterceptor(new OAuthInterceptor(userName,
            password,
            oAuthURL));
        }
        return b.build();
    }

    public String get(String url) throws IOException {
        this.client = buildClient(); 

        Request request = new Request.Builder().url(url)
                .get().build();

        Response response = client.newCall(request).execute();
        if (response.code() != expectedResponseCode) {
            throw new IOException("Unexpected http code: " + response.code());
        }
        return response.body().string();
    }

    public int getExpectedResponseCode() {
        return expectedResponseCode;
    }

    public void setExpectedResponseCode(int expectedResponseCode) {
        this.expectedResponseCode = expectedResponseCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getoAuthURL() {
        return oAuthURL;
    }

    public void setoAuthURL(String oAuthURL) {
        this.oAuthURL = oAuthURL;
    }

    public boolean isOAuth() {
        return isOAuth;
    }

    public void setOAuth(boolean isOAuth) {
        this.isOAuth = isOAuth;
    }
    
}