package com.asena.scimgateway.controller;

import javax.servlet.http.HttpServletResponse;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.SCIMProcessor;
import com.asena.scimgateway.service.RemoteSystemService;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/gateway/{systemid}/scim/v2/Users")
public class SCIMUserController {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @PostMapping("") 
    public @ResponseBody Object scimUserCreate(@PathVariable String systemid, @RequestBody Object params, HttpServletResponse response)
            throws Exception {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = SCIMProcessor.createUser(rs, params);
            response.setStatus(201);
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage(), e, params);
        }
        return o;
    }

    @PutMapping("/{id}")
    public @ResponseBody Object scimUserUpdate(@PathVariable String systemid, @PathVariable String id, @RequestBody Object params, HttpServletResponse response) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = SCIMProcessor.updateUser(rs, id, params);
            response.setStatus(201);
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage(), e, params);
        }
        return o;
    }
}