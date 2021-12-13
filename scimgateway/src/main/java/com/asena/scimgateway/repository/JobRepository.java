package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.jobs.Job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}