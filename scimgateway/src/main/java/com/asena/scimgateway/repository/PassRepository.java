package com.asena.scimgateway.repository;

import java.util.List;

import com.asena.scimgateway.model.jobs.Pass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
	List<Pass> findByPropertiesId(long id);

	List<Pass> findByMappingsId(long id);
}