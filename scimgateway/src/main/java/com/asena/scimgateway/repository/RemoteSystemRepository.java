package com.asena.scimgateway.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.model.RemoteSystem;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RemoteSystemRepository extends JpaRepository<RemoteSystem, Long>  {
    public Optional<RemoteSystem> findById(String id); 
    public void deleteById(String id);
    List<RemoteSystem> findByWriteMappingsId(long id);
    List<RemoteSystem> findByReadMappingsId(long id);
    List<RemoteSystem> findByPropertiesId(long id);
    List<RemoteSystem> findByServiceUserId(long id);
}