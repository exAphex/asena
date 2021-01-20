package com.asena.scimgateway.config;

import com.asena.scimgateway.security.handler.CustomMethodSecurityExpressionHandler;
import com.asena.scimgateway.service.RemoteSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Autowired
  private RemoteSystemService remoteSystemService;
  
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler expressionHandler = 
          new CustomMethodSecurityExpressionHandler(remoteSystemService);
        return expressionHandler;
    }
}