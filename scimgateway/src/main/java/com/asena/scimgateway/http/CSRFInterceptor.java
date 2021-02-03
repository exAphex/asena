package com.asena.scimgateway.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class CSRFInterceptor implements Interceptor {
    private List<String> cookies = new ArrayList<>();
    private String tokenURL = "";


    public CSRFInterceptor(String tokenURL) {
        this.tokenURL = tokenURL;
    }

    private String getCSRFToken(String url, String authHeader) throws IOException {
        Builder b = new Request.Builder().url(url).get();
        if (authHeader != null) {
            b.addHeader("Authorization", authHeader);
        }
        b.addHeader("x-csrf-token", "fetch");
        b.addHeader("x-sap-sac-custom-auth", "true");

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = b.build();

        Response response = client.newCall(request).execute();
        if (response.code() >= 400) {
            throw new IOException("Unexpected http code: " + response.code() + " on CSRF-Filter Interceptor");
        }
        List<String> cookies = response.headers("Set-Cookie");
        setCookies(cookies);
        return response.header("x-csrf-token");
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String authHeader = request.header("Authorization");
        String url = tokenURL;
        String token = getCSRFToken(url, authHeader);
        
        Builder b = request.newBuilder();
        addCookiesToRequest(b);

        Request authenticatedRequest = b
                    .header("X-Csrf-Token", token).build();
        return chain.proceed(authenticatedRequest);
    }

    private void setCookies(List<String> cookies) throws IOException {
        if (cookies != null) {
            for (String s : cookies) {
                this.cookies.add(s);
            }
        }
    }

    private void addCookiesToRequest(Builder b) {
        for (String c : this.cookies) {
            b.addHeader("Cookie", c);
        }
    }
}