package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.processor.ConnectorProcessor;
import com.asena.scimgateway.repository.RemoteSystemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteSystemService {

    @Autowired
    private RemoteSystemRepository remoteSystemRepository;

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
        rs.setAttributes(connector.getAttributes());
        rs.setProperties(connector.getProperties());

        return remoteSystemRepository.save(rs);
    }

    public Optional<RemoteSystem> findById(long id) {
        return remoteSystemRepository.findById(id);
    }

    public RemoteSystem update(RemoteSystem rs, long id) {
        return findById(id)
        .map(r -> {
            return remoteSystemRepository.save(r);
        })
        .orElseThrow(() -> new NotFoundException(id));
    }
}