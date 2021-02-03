package com.asena.scimgateway.repository;

import com.asena.scimgateway.model.Script;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    public Script findByName(String name);
}