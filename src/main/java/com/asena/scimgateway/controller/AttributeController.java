package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.dto.AttributeDTO;
import com.asena.scimgateway.service.AttributeService;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/attribute")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;
   
    @GetMapping("/{id}")
    public AttributeDTO getAttribute(@PathVariable long id) {
        Attribute a =  attributeService.findById(id).orElseThrow(() -> new NotFoundException(id));
        return AttributeDTO.toDTO(a);
    }
    
    @PutMapping("/{id}")
    public AttributeDTO updateAttribute(@RequestBody AttributeDTO attrDTO, @PathVariable long id) {
        Attribute a = attrDTO.fromDTO();
        return AttributeDTO.toDTO(attributeService.update(a, id));
    }

    @DeleteMapping("/{id}")
    public void deleteSystem(@PathVariable long id) {
        attributeService.deleteById(id);
    }
}