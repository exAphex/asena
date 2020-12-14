package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.repository.AttributeRepository;
import com.asena.scimgateway.repository.RemoteSystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

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
            a.setTransformation(attr.getTransformation());
            return attributeRepository.save(a);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }

    public void delete(Attribute a) {
        List<RemoteSystem> rs = remoteSystemRepository.findByWriteMappingsId(a.getId());
        for (RemoteSystem r : rs) {
            r.deleteWriteMapping(a);
            remoteSystemRepository.save(r);
        }

        attributeRepository.delete(a);
    }

    public Attribute deleteById(long id) {
        return findById(id)
        .map(a -> {
            delete(a);
            return a;
        })
        .orElseThrow(() -> new NotFoundException(id));
    }
}