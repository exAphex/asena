package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassProperty;
import com.asena.scimgateway.repository.PassPropertyRepository;
import com.asena.scimgateway.repository.PassRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassPropertyService {

	@Autowired
	private PassPropertyRepository propertyRepository;

	@Autowired
	private PassRepository passRepository;

	public Optional<PassProperty> findById(long id) {
		return propertyRepository.findById(id);
	}

	public PassProperty deleteById(long id) {
		return findById(id).map(p -> {
			delete(p);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	private void delete(PassProperty pp) {
		List<Pass> pass = passRepository.findByPropertiesId(pp.getId());
		for (Pass p : pass) {
			p.deleteProperty(pp);
			p = passRepository.save(p);
		}
	}

}