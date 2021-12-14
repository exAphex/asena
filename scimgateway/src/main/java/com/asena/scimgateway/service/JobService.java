package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.repository.JobRepository;
import com.asena.scimgateway.repository.PackageRepository;
import com.asena.scimgateway.model.jobs.Package;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PackageRepository packageRepository;

	public Optional<Job> findById(long id) {
		return jobRepository.findById(id);
	}

	public Job deleteById(long id) {
		return findById(id).map(p -> {
			delete(p);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	private void delete(Job j) {
		List<Package> packages = packageRepository.findByJobsId(j.getId());
		for (Package p : packages) {
			p.deleteJob(j);
			p = packageRepository.save(p);
		}

	}

}