package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.ConnectionProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionPropertyRepository extends JpaRepository<ConnectionProperty, Long> {
    
}