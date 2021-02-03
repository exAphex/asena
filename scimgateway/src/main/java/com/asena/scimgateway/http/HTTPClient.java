package com.asena.scimgateway.http;

import java.io.IOException;

import com.asena.scimgateway.http.oauth.OAuthInterceptor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public class HTTPClient {

    private enum HTTP_OPERATION {
        POST, PUT
    }

    private boolean isOAuth = false;
    private String oAuthURL;
    private boolean isCSRF = false;
    private String csrfURL;
    private String mediaType = null;
    private String userName;
    private String password;
    private int expectedResponseCode = 200;
    private OkHttpClient client;

    public OkHttpClient buildClient() {
        Builder b = new OkHttpClient.Builder();
        if (isOAuth) {
            b.addInterceptor(new OAuthInterceptor(userName, password, oAuthURL));
        }

        if (isCSRF) {
            b.addInterceptor(new CSRFInterceptor(csrfURL));
        }
        return b.build();
    }

    public String getCsrfURL() {
        return csrfURL;
    }

    public void setCsrfURL(String csrfURL) {
        this.csrfURL = csrfURL;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String get(String url) throws IOException {
        this.client = buildClient();

        Request request = new Request.Builder().url(url).get().build();

        Response response = client.newCall(request).execute();
        if (response.code() != expectedResponseCode) {
            throw new IOException("Unexpected http code: " + response.code());
        }
        return response.body().string();
    }

    public String post(String url, String obj) throws IOException {
        return write(url, obj, HTTP_OPERATION.POST);
    }

    private String write(String url, String obj, HTTP_OPERATION op) throws IOException {
        this.client = buildClient();
        MediaType mt = MediaType.parse((this.mediaType != null) ? this.mediaType : "application/json; charset=utf-8");
        RequestBody body = RequestBody.create(obj, mt);
        Request.Builder b = new Request.Builder().url(url);
        
        switch (op) {
            case POST:
                b.post(body);
                break;
            case PUT:
                b.put(body);
                break;
        }

        Request request = b.build();
        Response response = client.newCall(request).execute();
        if (response.code() != expectedResponseCode) {
            throw new IOException("Unexpected http code: " + response.code() + " - " + response.body().string());
        }
        return response.body().string();
    }

    public String put(String url, String obj) throws IOException {
        return write(url, obj, HTTP_OPERATION.PUT);
    }

    public void delete(String url) throws IOException {
        this.client = buildClient();
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        if (response.code() != expectedResponseCode) {
            throw new IOException("Unexpected http code: " + response.code() + " - " + response.body().string());
        }
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

    public boolean isCSRF() {
        return isCSRF;
    }

    public void setCSRF(boolean isCSRF) {
        this.isCSRF = isCSRF;
    }

}