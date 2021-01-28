package com.asena.scimgateway.http.oauth;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class OAuthInterceptor implements Interceptor {
    private OAuthAuthenticator oauthAuthenticator;

    public OAuthInterceptor(String userName, String password, String tokenURL) {
        this.oauthAuthenticator = new OAuthAuthenticator(userName, password, tokenURL);       
    }

    public Response intercept(Chain chain) throws IOException {
        OAuthResponse resp = oauthAuthenticator.authenticate();
        Request request = chain.request();
        Builder authenticatedRequest = request.newBuilder();
        authenticatedRequest.header("Authorization", String.format("%s %s", resp.getToken_type(), resp.getAccess_token()));
        authenticatedRequest.header("x-sap-sac-custom-auth","true").build();
        return chain.proceed(authenticatedRequest.build());
    }
    
}