package com.asena.scimgateway.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.service.LoggerService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LoggerService loggerService;

    @PreAuthorize("isAdmin()")
    @GetMapping("")
    public List<Log> getLogs() {
        List<Log> logs = loggerService.getLogs();
        return logs;
    }

    @PreAuthorize("isAdmin()")
    @DeleteMapping("")
    public void deleteLogs() {
        loggerService.deleteLogs();
    }

    @PreAuthorize("isAdmin()")
    @GetMapping("/count")
    public HashMap<String, String> getLogCount() throws IOException {
        HashMap<String, String> retVar = new HashMap<>();
        retVar.put("count", loggerService.getLogCount() + "");
        return retVar;
    }
}