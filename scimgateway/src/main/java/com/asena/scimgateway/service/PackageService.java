package com.asena.scimgateway.service;

import com.asena.scimgateway.repository.JobRepository;
import com.asena.scimgateway.repository.PackageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Package;

@Service
public class PackageService {

	@Autowired
	private PackageRepository packageRepository;

	public Optional<Package> findById(long id) {
		return packageRepository.findById(id);
	}

	public List<Package> list() {
		return packageRepository.findAll();
	}

	public Package create(Package p) {
		Package pCreate = new Package();
		pCreate.setName(p.getName());
		return packageRepository.save(pCreate);
	}

	public Package deleteById(long id) {
		return findById(id).map(p -> {
			packageRepository.deleteById(id);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	public void delete(Package p) {
		if (p != null) {
			deleteById(p.getId());
		}
	}

	public Package addJob(Job j, long id) {
		Job job = new Job();
		job.setName(j.getName());
		job.setDescription(j.getDescription());
		job.setEnabled(j.isEnabled());

		Package p = findById(id).orElseThrow(() -> new NotFoundException(id));
		p.addJob(job);
		return packageRepository.save(p);
	}

	public void deleteAll() {
		List<Package> packages = packageRepository.findAll();
		for (Package p : packages) {
			delete(p);
		}
	}
}