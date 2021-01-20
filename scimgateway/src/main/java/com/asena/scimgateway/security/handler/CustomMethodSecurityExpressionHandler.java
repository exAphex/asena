package com.asena.scimgateway.security.handler;

import com.asena.scimgateway.security.processor.SecurityExpressProcessor;
import com.asena.scimgateway.service.RemoteSystemService;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler 
  extends DefaultMethodSecurityExpressionHandler {
    private RemoteSystemService remoteSystemService;
    private AuthenticationTrustResolver trustResolver = 
      new AuthenticationTrustResolverImpl();

    public CustomMethodSecurityExpressionHandler(RemoteSystemService remoteSystemService) {
        super();
        this.remoteSystemService = remoteSystemService;
    } 

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      Authentication authentication, MethodInvocation invocation) {
        SecurityExpressProcessor root = 
          new SecurityExpressProcessor(authentication, remoteSystemService);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}