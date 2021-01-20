package com.asena.scimgateway.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.model.dto.ConnectionPropertyDTO;
import com.asena.scimgateway.model.dto.RemoteSystemDTO;
import com.asena.scimgateway.processor.ConnectorProcessor;
import com.asena.scimgateway.service.RemoteSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/remotesystem")
public class RemoteSystemController {

    @Autowired
    private RemoteSystemService remoteSystemService;

    @PreAuthorize("isAdmin()")
    @GetMapping("/templates")
	public Set<RemoteSystemDTO> getRemoteSystemTemplates() {
        Set<RemoteSystemDTO> retDTO = new HashSet<>();
        Set<RemoteSystem> systems = ConnectorProcessor.getAvailableConnectors();

        for (RemoteSystem rs : systems) {
            retDTO.add(RemoteSystemDTO.toDTO(rs));
        }

		return retDTO; 
    } 

    @PreAuthorize("isAdmin()")
    @GetMapping("{id}/template")
    public RemoteSystemDTO getRemoteSystemTemplate(@PathVariable String id) {
        RemoteSystem rs = remoteSystemService.findById(id).orElseThrow(() -> new NotFoundException(id));
        RemoteSystem rsSuggestions = ConnectorProcessor.getRemoteSystemByType(rs.getType());
        return RemoteSystemDTO.toDTO(rsSuggestions);
    }

    @PreAuthorize("isAdmin()")
    @GetMapping("")
    public Set<RemoteSystemDTO> getAllRemoteSystems() {
        Set<RemoteSystemDTO> retDTO = new HashSet<>();
        List<RemoteSystem> lstRemoteSystems = remoteSystemService.list();
        for (RemoteSystem rs : lstRemoteSystems) {
            retDTO.add(RemoteSystemDTO.toDTO(rs));
        }
        return retDTO;
    }

    @PreAuthorize("isAdmin()")
    @GetMapping("/{id}")
    public RemoteSystemDTO getRemoteSystem(@PathVariable String id) {
        RemoteSystem rs = remoteSystemService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return RemoteSystemDTO.toDTO(rs);
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("")
    public RemoteSystemDTO createSystem(@RequestBody RemoteSystemDTO rsDTO) {
        RemoteSystem system = rsDTO.fromDTO();
        return RemoteSystemDTO.toDTO(remoteSystemService.create(system));
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("/{id}/write")
    public RemoteSystemDTO addWriteMapping(@RequestBody AttributeDTO attrDTO, @PathVariable String id) {
        return RemoteSystemDTO.toDTO(remoteSystemService.addWriteMapping(attrDTO.fromDTO(), id));
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("/{id}/read")
    public RemoteSystemDTO addReadMapping(@RequestBody AttributeDTO attrDTO, @PathVariable String id) {
        return RemoteSystemDTO.toDTO(remoteSystemService.addReadMapping(attrDTO.fromDTO(), id));
    }

    @PreAuthorize("isAdmin()")
    @PostMapping("/{id}/connection")
    public RemoteSystemDTO addConnectionProperty(@RequestBody ConnectionPropertyDTO cpDTO, @PathVariable String id) {
        return RemoteSystemDTO.toDTO(remoteSystemService.addConnectionProperty(cpDTO.fromDTO(), id));
    }

    @PreAuthorize("isAdmin()")
    @PutMapping("/{id}")
    public RemoteSystemDTO updateSystem(@RequestBody RemoteSystemDTO rsDTO, @PathVariable String id) {
        RemoteSystem system = rsDTO.fromDTO();
        return RemoteSystemDTO.toDTO(remoteSystemService.update(system, id));
    }

    @PreAuthorize("isAdmin()")
    @DeleteMapping("/{id}")
    public void deleteSystem(@PathVariable String id) {
        remoteSystemService.deleteById(id);
    }
}