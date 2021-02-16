package com.asena.scimgateway.http.oauth;

import java.io.IOException;
import java.util.HashMap;

import com.asena.scimgateway.http.BasicAuthInterceptor;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OAuthAuthenticator {

    private String userName;
    private String password;
    private String tokenURL;
    private HashMap<String, String> additionalBody = new HashMap<>();

    public OAuthAuthenticator(String userName, String password, String tokenURL) {
        this.userName = userName;
        this.password = password;
        this.tokenURL = tokenURL;
    }

    public void addBody(String name, String value) {
        this.additionalBody.put(name, value);
    }

    public OAuthResponse authenticate() throws IOException {
        Gson gson = new Gson(); 
        OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new BasicAuthInterceptor(userName, password))
        .build();
        
        FormBody.Builder b = new FormBody.Builder()
        .add("grant_type", "client_credentials");
        

        for (String bodyItem : additionalBody.keySet()) {
            b.add(bodyItem, additionalBody.get(bodyItem));
        }

        RequestBody formBody = b.build();

        Request request = new Request.Builder()
        .url(tokenURL)
        .post(formBody)
        .build();

        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Unexpected http code: " + response.code());
        }

        ResponseBody responseBody = response.body();
        String strResponseBody = responseBody.string();
        OAuthResponse entity = gson.fromJson(strResponseBody, OAuthResponse.class);
        return entity;
    }
}