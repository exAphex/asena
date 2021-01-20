package com.asena.scimgateway.security.processor;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.User;
import com.asena.scimgateway.model.User.UserType;
import com.asena.scimgateway.security.AUserPrincipal;
import com.asena.scimgateway.service.RemoteSystemService;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class SecurityExpressProcessor extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;

    private RemoteSystemService remoteSystemService;

    public SecurityExpressProcessor(Authentication authentication, RemoteSystemService remoteSystemService) {
        super(authentication);
        this.remoteSystemService = remoteSystemService;
    }

    public boolean isAdmin() {
        return (getUser().getType() == UserType.ADMIN);
    }

    public boolean isTechnical() {
        return (getUser().getType() == UserType.TECHNICAL);
    }

    public boolean isServiceUser(String systemId) {
        RemoteSystem rs = remoteSystemService.findById(systemId).orElseThrow(() -> new NotFoundException(systemId));
        User currUser = getUser();
        User serviceUser = rs.getServiceUser();
        return (currUser.getId() == serviceUser.getId());
    }

    public boolean isRemoteSystemActive(String systemId) {
        RemoteSystem rs = remoteSystemService.findById(systemId).orElseThrow(() -> new NotFoundException(systemId));
        return rs.isActive();
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

    private User getUser() {
        AUserPrincipal ap = (AUserPrincipal) getPrincipal();
        return ap.getUser();
    }
}