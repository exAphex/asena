package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.jobs.PassProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassPropertyRepository extends JpaRepository<PassProperty, Long> {

}