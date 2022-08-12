package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;
import com.asena.scimgateway.repository.PassMappingRepository;
import com.asena.scimgateway.repository.PassRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassMappingService {

	@Autowired
	private PassMappingRepository mappingRepository;

	@Autowired
	private PassRepository passRepository;

	public Optional<PassMapping> findById(long id) {
		return mappingRepository.findById(id);
	}

	public PassMapping deleteById(long id) {
		return findById(id).map(p -> {
			delete(p);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	private void delete(PassMapping pm) {
		List<Pass> pass = passRepository.findByMappingsId(pm.getId());
		for (Pass p : pass) {
			p.deleteMapping(pm);
			p = passRepository.save(p);
		}
	}

	public PassMapping update(PassMapping pp, long id) {
		return findById(id).map(p -> {
			p.setDestination(pp.getDestination());
			p.setSource(pp.getSource());
			return mappingRepository.save(p);
		}).orElseThrow(() -> new NotFoundException(id));
	}

}