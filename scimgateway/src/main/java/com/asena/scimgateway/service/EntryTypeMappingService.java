package com.asena.scimgateway.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.repository.EntryTypeMappingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryTypeMappingService {

    @Autowired
    private EntryTypeMappingRepository entryTypeMappingRepository; 

    @Autowired 
    private AttributeService attributeService;

    public Optional<EntryTypeMapping> findById(long id) {
        return entryTypeMappingRepository.findById(id);
    }

    public EntryTypeMapping create(EntryTypeMapping em) {
        if (em != null) {

            Set<Attribute> writeMappings = em.getWriteMappings();
            Set<Attribute> newWriteMappings = new HashSet<>();
            if (writeMappings != null) {
                for (Attribute a : writeMappings) {
                    newWriteMappings.add(attributeService.create(a));
                }
            }
            em.setWriteMappings(newWriteMappings);

            Set<Attribute> readMappings = em.getReadMappings();
            Set<Attribute> newReadMappings = new HashSet<>();
            if (readMappings != null) {
                for (Attribute a : readMappings) {
                    newReadMappings.add(attributeService.create(a));
                }
            }
            em.setReadMappings(newReadMappings);

            return entryTypeMappingRepository.save(em);
        } else {
            return null;
        }
    } 

    public EntryTypeMapping addWriteMapping(Attribute a, long id) {
        EntryTypeMapping em = findById(id).orElseThrow(() -> new NotFoundException(id));
        em.addWriteMapping(a);
        return entryTypeMappingRepository.save(em);
    }

    public EntryTypeMapping addReadMapping(Attribute a, long id) {
        EntryTypeMapping em = findById(id).orElseThrow(() -> new NotFoundException(id));
        em.addReadMapping(a);
        return entryTypeMappingRepository.save(em);
    }
}