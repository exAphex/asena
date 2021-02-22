package com.asena.scimgateway.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.ConnectorProcessor;
import com.asena.scimgateway.repository.RemoteSystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class RemoteSystemService {

    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

    @Autowired
    private UserService userService;


    @Autowired
    private EntryTypeMappingService entryTypeMappingService;

    public List<RemoteSystem> list() {
        return remoteSystemRepository.findAll();
    }

    public RemoteSystem create(RemoteSystem rs) {
        RemoteSystem connector = ConnectorProcessor.getRemoteSystemByType(rs.getType());

        if (connector == null) {
            throw new NotFoundException(rs);
        }

        rs.setId(UUID.randomUUID().toString());
        rs.setActive(false);
        rs.setProperties(connector.getProperties());
        rs.setServiceUser(userService.createServiceUser(rs.getName()));

        Set<EntryTypeMapping> entryTypeMappings = connector.getEntryTypeMappings();
        Set<EntryTypeMapping> newEntryTypeMappings = new HashSet<>();
        if (entryTypeMappings != null) {
            for (EntryTypeMapping em : entryTypeMappings) {
                newEntryTypeMappings.add(entryTypeMappingService.create(em));
            }
        }
        rs.setEntryTypeMappings(newEntryTypeMappings);

        return remoteSystemRepository.save(rs);
    }

    public Optional<RemoteSystem> findById(String id) {
        return remoteSystemRepository.findById(id);
    }

    public RemoteSystem update(RemoteSystem rs, String id) {
        return findById(id)
        .map(r -> {
            r.setDescription(rs.getDescription()); 
            r.setActive(rs.isActive());

            if (rs.getServiceUser() != null) {
                r.getServiceUser().setPassword(rs.getServiceUser().getPassword());
            }

            return remoteSystemRepository.save(r);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }

    
    public RemoteSystem deleteById(String id) {
        return findById(id)
        .map(sys -> {
            remoteSystemRepository.deleteById(id);
            return sys;
        })
        .orElseThrow(() -> new NotFoundException(id));
    } 

    public RemoteSystem addEntryTypeMapping(EntryTypeMapping em, String id) {
        RemoteSystem rs = findById(id).orElseThrow(() -> new NotFoundException(id));
        rs.addEntryTypeMapping(em);
        return remoteSystemRepository.save(rs);
    }
   
    public RemoteSystem addConnectionProperty(ConnectionProperty cp, String id) {
        RemoteSystem rs = findById(id).orElseThrow(() -> new NotFoundException(id));
        rs.addProperty(cp);
        return remoteSystemRepository.save(rs);
    }

    public void deleteAll() {
        remoteSystemRepository.deleteAll();
    }
    
}