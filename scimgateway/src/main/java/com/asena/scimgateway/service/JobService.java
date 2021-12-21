package com.asena.scimgateway.service;

import java.util.List;
import java.util.Optional;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.repository.JobRepository;
import com.asena.scimgateway.repository.PackageRepository;
import com.asena.scimgateway.repository.PassRepository;
import com.asena.scimgateway.model.jobs.Package;
import com.asena.scimgateway.model.jobs.Pass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private RemoteSystemService remoteSystemService;

	@Autowired
	private PassRepository passRepository;

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

	public Job addPass(Pass p, long id) {
		Pass pass = new Pass();
		pass.setName(p.getName());
		pass.setDescription(p.getDescription());
		pass.setType(p.getType());

		RemoteSystem rs = p.getSystem();
		if (rs != null) {
			String rsId = rs.getId();
			rs = remoteSystemService.findById(rsId).orElseThrow(() -> new NotFoundException(rsId));
			pass.setSystem(rs);
		}
		pass = passRepository.save(pass);

		Job j = findById(id).orElseThrow(() -> new NotFoundException(id));
		j.addPass(pass);
		return jobRepository.save(j);
	}

}