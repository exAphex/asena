package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.jobs.PassMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassMappingRepository extends JpaRepository<PassMapping, Long> {

}