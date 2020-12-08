package com.asena.scimgateway.service;

import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.repository.ConnectionPropertyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionPropertyService {
    @Autowired
    private ConnectionPropertyRepository connectionPropertyRepository;

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

    public void delete(ConnectionProperty cp) {
        /*List<RemoteSystem> rs = remoteSystemRepository.findByWriteMappingsId(a.getId());
        for (RemoteSystem r : rs) {
            r.deleteWriteMapping(a);
            remoteSystemRepository.save(r);
        }*/

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
}