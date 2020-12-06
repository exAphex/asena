package com.asena.scimgateway.service;

import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.repository.AttributeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    public Optional<Attribute> findById(long id) {
        return attributeRepository.findById(id);
    }

    public Attribute update(Attribute attr, long id) {
        return findById(id)
        .map(a -> {
            a.setSource(attr.getSource());
            a.setDestination(attr.getDestination());
            a.setDescription(attr.getDescription());
            a.setEncrypted(attr.isEncrypted());
            a.setType(attr.getType());
            a.setTransformation(a.getTransformation());
            return attributeRepository.save(a);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }
}