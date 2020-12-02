package com.asena.scimgateway.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.connector.CSVConnector;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.dto.RemoteSystemDTO;
import com.asena.scimgateway.service.RemoteSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/remotesystem")
public class RemoteSystemController {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @GetMapping("/templates")
	public Set<RemoteSystemDTO> getRemoteSystemTemplates() {
        Set<RemoteSystemDTO> retDTO = new HashSet<>();

        CSVConnector csv = new CSVConnector();
        retDTO.add(RemoteSystemDTO.toDTO(csv.getRemoteSystemTemplate()));

		return retDTO; 
    } 

    @GetMapping("")
    public Set<RemoteSystemDTO> getAllRemoteSystems() {
        Set<RemoteSystemDTO> retDTO = new HashSet<>();
        List<RemoteSystem> lstRemoteSystems = remoteSystemService.list();
        for (RemoteSystem rs : lstRemoteSystems) {
            retDTO.add(RemoteSystemDTO.toDTO(rs));
        }
        return retDTO;
    }


}