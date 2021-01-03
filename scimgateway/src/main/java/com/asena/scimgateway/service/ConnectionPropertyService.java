package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.repository.ConnectionPropertyRepository;
import com.asena.scimgateway.repository.RemoteSystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ConnectionPropertyService {
    @Autowired
    private ConnectionPropertyRepository connectionPropertyRepository;

    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

    public Optional<ConnectionProperty> findById(long id) {
        return connectionPropertyRepository.findById(id);
    } 

    public ConnectionProperty update(ConnectionProperty cp, long id) {
        return findById(id)
        .map(c -> {
            c.setKey(cp.getKey());
            c.setValue(cp.getValue());
            c.setDescription(cp.getDescription());
            c.setEncrypted(cp.isEncrypted());
            return connectionPropertyRepository.save(c);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }

    public ConnectionProperty create(ConnectionProperty cp) {
        return connectionPropertyRepository.save(cp);
    }

    
    public void delete(ConnectionProperty cp) {
        List<RemoteSystem> rs = remoteSystemRepository.findByPropertiesId(cp.getId());
        for (RemoteSystem r : rs) {
            r.deleteProperty(cp);
            remoteSystemRepository.save(r);
        }

        connectionPropertyRepository.delete(cp);
    }

    public ConnectionProperty deleteById(long id) {
        return findById(id)
        .map(a -> {
            delete(a);
            return a;
        })
        .orElseThrow(() -> new NotFoundException(id));
    }

    
    public void deleteAll() {
        List<ConnectionProperty> cps = connectionPropertyRepository.findAll();
        for (ConnectionProperty cp : cps) {
            delete(cp);
        }
    }
}