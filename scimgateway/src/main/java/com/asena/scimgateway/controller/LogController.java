package com.asena.scimgateway.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/log")
public class LogController {
    

    @PreAuthorize("isAdmin()")
    @GetMapping("")
    public String getLogs() {
        return "";
    }

    @PreAuthorize("isAdmin()")
    @DeleteMapping("")
    public void deleteLogs() {
    }

    @PreAuthorize("isAdmin()")
    @GetMapping("/count")
    public long getLogCount() {
        return 0;
    }
}