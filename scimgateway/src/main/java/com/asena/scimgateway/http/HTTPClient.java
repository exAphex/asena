package com.asena.scimgateway.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public class HTTPClient {

    private enum HTTP_OPERATION {
        POST, PUT, PATCH
    }


    private String mediaType = null;
    private String userName;
    private String password;
    private int expectedResponseCode = 200;
    private OkHttpClient client;
    private HashMap<String, String> addHeaders = new HashMap<>();
    private List<Interceptor> interceptors = new ArrayList<>();

    public void addHeader(String header, String value) {
        addHeaders.put(header, value);
    }

    public void addInterceptor(Interceptor i) {
        interceptors.add(i);
    }

    public OkHttpClient buildClient() {
        Builder b = new OkHttpClient.Builder();
        for (Interceptor i : this.interceptors) {
            b.addInterceptor(i);
        }
        return b.build();
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    private void addHeaderAttributes(Request.Builder b) {
        for (String s : addHeaders.keySet()) {
            b.addHeader(s, addHeaders.get(s));
        }
    }

    public String get(String url) throws IOException {
        this.client = buildClient();

        okhttp3.Request.Builder b = new Request.Builder().url(url).get();
        addHeaderAttributes(b);

        Request request = b.build();

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
        addHeaderAttributes(b);

        switch (op) {
            case POST:
                b.post(body);
                break;
            case PUT:
                b.put(body);
                break;
            case PATCH:
                b.patch(body);
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

    public String patch(String url, String obj) throws IOException {
        return write(url, obj, HTTP_OPERATION.PATCH);
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

}