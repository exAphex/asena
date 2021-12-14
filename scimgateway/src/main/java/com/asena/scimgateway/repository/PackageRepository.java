package com.asena.scimgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.asena.scimgateway.model.jobs.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
	List<Package> findByJobsId(long id);

}