package com.asena.scimgateway.service;

import java.io.IOException;
import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    @Autowired
    private LogRepository logRepository;

    @Value("${logging.file.path}")
    private String logPath;

    @Value("${logging.file.name}")
    private String logFileName;

    public void deleteLogs() {
        logRepository.deleteAll();
    }

    public long getLogCount() throws IOException {
        return logRepository.count();
    }

    public List<Log> getLogs() {
        return logRepository.findAll();
    }
}