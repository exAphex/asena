package com.asena.scimgateway.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.SCIMProcessor;
import com.asena.scimgateway.service.RemoteSystemService;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/gateway/{systemid}/scim/v2")
public class SCIMEntityController {

    Logger logger = LoggerFactory.getLogger(SCIMEntityController.class);
    
    @Autowired
    private RemoteSystemService remoteSystemService;

    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @GetMapping("/{entity}")
    public @ResponseBody HashMap<String, Object> scimEntityList(@PathVariable String systemid, @PathVariable String entity, @RequestParam Map<String, String> params) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        HashMap<String, Object> retEntities = new HashMap<>();
        try {
            retEntities = new SCIMProcessor(rs, entity).getEntities();
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return retEntities;
    }

    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @GetMapping("/{entity}/{id}")
    public @ResponseBody HashMap<String, Object> scimEntityGet(@PathVariable String systemid, @PathVariable String entity, @PathVariable String id, @RequestParam Map<String, String> params) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        HashMap<String, Object> retUsers = new HashMap<>();
        try {
            retUsers = new SCIMProcessor(rs, entity).getEntity(id);
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return retUsers;
    }

    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @PostMapping("/{entity}") 
    public @ResponseBody Object scimUserCreate(@PathVariable String systemid, @PathVariable String entity, @RequestBody HashMap<String, Object> params, HttpServletResponse response)
            throws Exception {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = new SCIMProcessor(rs, entity).createEntity(params);
            response.setStatus(201);
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return o;
    }

    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @PatchMapping("/{entity}/{id}")
    public @ResponseBody Object scimUserPatch(@PathVariable String systemid, @PathVariable String entity, @PathVariable String id, @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = new SCIMProcessor(rs, entity).patchEntity(id, params);
            response.setStatus(201);
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return o;
    }

    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @PutMapping("/{entity}/{id}")
    public @ResponseBody Object scimUserUpdate(@PathVariable String systemid, @PathVariable String entity, @PathVariable String id, @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        Object o = null;
        try {
            o = new SCIMProcessor(rs, entity).updateEntity(id, params);
            response.setStatus(201);
        } catch (Exception e) {
            handleControllerError(e, params);
        }
        return o;
    }
    
    @PreAuthorize("isTechnical() and isServiceUser(#systemid) and isRemoteSystemActive(#systemid)")
    @DeleteMapping("/{entity}/{id}")
    public void scimUserDelete(@PathVariable String systemid, @PathVariable String entity, @PathVariable String id, HttpServletResponse response) {
        RemoteSystem rs = remoteSystemService.findById(systemid).orElseThrow(() -> new NotFoundException(systemid));
        boolean isDeleted = false;
        try {
            isDeleted = new SCIMProcessor(rs, entity).deleteEntity(id);
            response.setStatus(204);
        } catch (Exception e) {
            handleControllerError(e, null);
        }

        if (!isDeleted) {
            throw new NotFoundException(id);
        }
    }

    private void handleControllerError(Exception e, Object params) {
        logger.error("SCIM Endpoint error", e);
        if (e instanceof NotFoundException) {
            throw new NotFoundException(e.getMessage());
        } else {
            throw new InternalErrorException(e.getMessage(), e, params);
        }
    }
}