package com.asena.scimgateway.repository.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asena.scimgateway.model.jobs.Package;

public interface PackageRepository extends JpaRepository<Package, String> {

}