package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.dto.ConnectionPropertyDTO;
import com.asena.scimgateway.service.ConnectionPropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ConnectionPropertyController {

    @Autowired
    private ConnectionPropertyService connectionPropertyService;
   
    @GetMapping("/{id}")
    public ConnectionPropertyDTO getConnectionProperty(@PathVariable long id) {
        ConnectionProperty cp =  connectionPropertyService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return ConnectionPropertyDTO.toDTO(cp);
    }
    
    @PutMapping("/{id}")
    public ConnectionPropertyDTO updateConnectionProperty(@RequestBody ConnectionPropertyDTO cpDTO, @PathVariable long id) {
        ConnectionProperty cp = cpDTO.fromDTO();
        return ConnectionPropertyDTO.toDTO(connectionPropertyService.update(cp, id));
    }

    @DeleteMapping("/{id}")
    public void deleteConnectionProperty(@PathVariable long id) {
        connectionPropertyService.deleteById(id);
    }
}