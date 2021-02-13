package com.asena.scimgateway.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/version")
public class VersionController {

    @Value("${app.version}")
    private String version; 

    @PreAuthorize("isAdmin()")
    @GetMapping("")
    public HashMap<String, String> getVersion() {
        HashMap<String, String> retData = new HashMap<>();
        retData.put("version", version);
        return retData;
    }
}