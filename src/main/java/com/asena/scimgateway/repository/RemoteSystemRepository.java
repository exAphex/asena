package com.asena.scimgateway.repository;

import org.springframework.stereotype.Repository;
import com.asena.scimgateway.model.RemoteSystem;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RemoteSystemRepository extends JpaRepository<RemoteSystem, Long>  {
    
}