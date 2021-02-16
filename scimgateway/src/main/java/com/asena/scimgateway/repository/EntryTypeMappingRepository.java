package com.asena.scimgateway.repository;

import java.util.List;

import com.asena.scimgateway.model.EntryTypeMapping;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EntryTypeMappingRepository extends JpaRepository<EntryTypeMapping, Long> {
    List<EntryTypeMapping> findByWriteMappingsId(long id);
    List<EntryTypeMapping> findByReadMappingsId(long id);
}