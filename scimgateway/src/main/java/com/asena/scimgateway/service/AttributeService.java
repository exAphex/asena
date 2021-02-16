package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.repository.AttributeRepository;
import com.asena.scimgateway.repository.EntryTypeMappingRepository;
import com.asena.scimgateway.repository.ScriptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private EntryTypeMappingRepository entryTypeMappingRepository;

    @Autowired 
    private ScriptRepository scriptRepository;

    public Optional<Attribute> findById(long id) {
        return attributeRepository.findById(id);
    }

    public Attribute create(Attribute a) {
        if (a != null) {
            if ((a.getTransformation() != null) && (a.getTransformation().getId() == 0)) {
                Script s = scriptRepository.findByName(a.getTransformation().getName());
                if (s == null) {
                    a.setTransformation(null);
                } else {
                    a.setTransformation(s);
                }
            }
            return attributeRepository.save(a);
        } else {
            return null;
        }
    }

    public List<Attribute> list() {
        return attributeRepository.findAll();
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
        List<EntryTypeMapping> em = entryTypeMappingRepository.findByWriteMappingsId(a.getId());
        for (EntryTypeMapping e : em) {
            e.deleteWriteMapping(a);
            entryTypeMappingRepository.save(e);
        }


        em = entryTypeMappingRepository.findByReadMappingsId(a.getId());
        for (EntryTypeMapping e : em) {
            e.deleteReadMapping(a);
            entryTypeMappingRepository.save(e);
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

    public void deleteAll() {
        List<Attribute> lstAttributes = attributeRepository.findAll();
        for (Attribute attr : lstAttributes) {
            delete(attr);
        }
    }
}