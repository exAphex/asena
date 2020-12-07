package com.asena.scimgateway.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.dto.ScriptDTO;
import com.asena.scimgateway.service.ScriptService;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/script")
public class ScriptController {
   
    @Autowired
    private ScriptService scriptService;

    @GetMapping("")
    public Set<ScriptDTO> getAllScripts() {
        Set<ScriptDTO> retDTO = new HashSet<>();
        List<Script> lstScripts = scriptService.list();
        for (Script s : lstScripts) {
            retDTO.add(ScriptDTO.toDTO(s));
        }
        return retDTO;
    }

    @GetMapping("/{id}")
    public ScriptDTO getScript(@PathVariable long id) {
        Script sc = scriptService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return ScriptDTO.toDTO(sc);
    }

    @PostMapping("") 
    public ScriptDTO createScript(@RequestBody ScriptDTO scDTO) {
        Script s = scDTO.fromDTO();
        return ScriptDTO.toDTO(scriptService.create(s));
    }

    @PutMapping("/{id}")
    public ScriptDTO updateScript(@RequestBody ScriptDTO scDTO, @PathVariable long id) {
        Script s = scDTO.fromDTO();
        return ScriptDTO.toDTO(scriptService.update(s, id));
    }

    @DeleteMapping("/{id}")
    public void deleteScript(@PathVariable long id) {
        scriptService.deleteById(id);
    }
}