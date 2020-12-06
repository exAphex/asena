package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.Attribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long>  {
    
}