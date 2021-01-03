package com.asena.scimgateway.repository;

import java.util.List;

import com.asena.scimgateway.model.Attribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long>  {
    List<Attribute> findByTransformationId(long id);
}