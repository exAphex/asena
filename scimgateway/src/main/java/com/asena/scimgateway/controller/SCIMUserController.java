package com.asena.scimgateway.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.SCIMProcessor;
import com.asena.scimgateway.service.RemoteSystemService;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/gateway/{systemid}/scim/v2/Users")
public class SCIMUserController {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @GetMapping("")
    public @ResponseBody List<HashMap<String, Object>> scimUserList(@PathVariable String systemid, @RequestParam Map<String, String> params) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        List<HashMap<String, Object>> retUsers = new ArrayList<>();
        try {
            retUsers = SCIMProcessor.getUsers(rs);
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return retUsers;
    }

    @PostMapping("") 
    public @ResponseBody Object scimUserCreate(@PathVariable String systemid, @RequestBody Object params, HttpServletResponse response)
            throws Exception {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = SCIMProcessor.createUser(rs, params);
            response.setStatus(201);
        } catch (Exception e) {
            handleControllerError(e, params);
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
            handleControllerError(e, params);
        }
        return o;
    }
    
    @DeleteMapping("/{id}")
    public void scimUserDelete(@PathVariable String systemid, @PathVariable String id, HttpServletResponse response) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        boolean isDeleted = false;
        try {
            isDeleted = SCIMProcessor.deleteUser(rs, id);
            response.setStatus(204);
        } catch (Exception e) {
            handleControllerError(e, null);
        }

        if (!isDeleted) {
            throw new NotFoundException(id);
        }
    }

    private void handleControllerError(Exception e, Object params) {
        if (e instanceof NotFoundException) {
            throw new NotFoundException(e.getMessage());
        } else {
            throw new InternalErrorException(e.getMessage(), e, params);
        }
    }
}