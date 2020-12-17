package com.asena.scimgateway.service;

import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.repository.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void create(Log l) {
        logRepository.save(l);
    }
    
    public List<Log> list() {
        return logRepository.findAll();
    }

    public void deleteAll() {
        logRepository.deleteAll();
    }

    public long getCount() {
        return logRepository.count();
    }
}