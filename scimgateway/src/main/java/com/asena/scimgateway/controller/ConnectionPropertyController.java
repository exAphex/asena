package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.dto.ConnectionPropertyDTO;
import com.asena.scimgateway.service.ConnectionPropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/property")
public class ConnectionPropertyController {

    @Autowired
    private ConnectionPropertyService connectionPropertyService;
   
    @PreAuthorize("isAdmin()")
    @GetMapping("/{id}")
    public ConnectionPropertyDTO getConnectionProperty(@PathVariable long id) {
        ConnectionProperty cp =  connectionPropertyService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return ConnectionPropertyDTO.toDTO(cp);
    }
    
    @PreAuthorize("isAdmin()")
    @PutMapping("/{id}")
    public ConnectionPropertyDTO updateConnectionProperty(@RequestBody ConnectionPropertyDTO cpDTO, @PathVariable long id) {
        ConnectionProperty cp = cpDTO.fromDTO();
        return ConnectionPropertyDTO.toDTO(connectionPropertyService.update(cp, id));
    }

    @PreAuthorize("isAdmin()")
    @DeleteMapping("/{id}")
    public void deleteConnectionProperty(@PathVariable long id) {
        connectionPropertyService.deleteById(id);
    }
}