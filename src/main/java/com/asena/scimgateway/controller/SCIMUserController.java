package com.asena.scimgateway.controller;

import javax.servlet.http.HttpServletResponse;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.SCIMProcessor;
import com.asena.scimgateway.service.RemoteSystemService;

import com.asena.scimgateway.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public @ResponseBody Object usersPost(@PathVariable String systemid, @RequestBody Object params, HttpServletResponse response){
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        response.setStatus(201);
        return SCIMProcessor.processUser(rs, params);
    } 
}