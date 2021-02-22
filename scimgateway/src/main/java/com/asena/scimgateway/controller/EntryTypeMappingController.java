package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.EntryTypeMappingDTO;
import com.asena.scimgateway.service.EntryTypeMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/entrytypemapping")
public class EntryTypeMappingController {

    @Autowired
    private EntryTypeMappingService entryTypeMappingService;
    
    @PreAuthorize("isAdmin()")
    @GetMapping("/{id}")
    public EntryTypeMappingDTO getEntryTypeMapping(@PathVariable long id) {
        EntryTypeMapping em = entryTypeMappingService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return EntryTypeMappingDTO.toDTO(em);
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("/{id}/write")
    public EntryTypeMappingDTO addWriteMapping(@RequestBody AttributeDTO attrDTO, @PathVariable long id) {
        return EntryTypeMappingDTO.toDTO(entryTypeMappingService.addWriteMapping(attrDTO.fromDTO(), id));
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("/{id}/read")
    public EntryTypeMappingDTO addReadMapping(@RequestBody AttributeDTO attrDTO, @PathVariable long id) {
        return EntryTypeMappingDTO.toDTO(entryTypeMappingService.addReadMapping(attrDTO.fromDTO(), id));
    }
}