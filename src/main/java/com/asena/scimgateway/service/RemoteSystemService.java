package com.asena.scimgateway.service;

import java.util.List;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.repository.RemoteSystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteSystemService {

    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

    public List<RemoteSystem> list() {
        return remoteSystemRepository.findAll();
    }
}