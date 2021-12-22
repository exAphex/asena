package com.asena.scimgateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.repository.JobRepository;
import com.asena.scimgateway.repository.PassRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassService {

	@Autowired
	private PassRepository passRepository;

	@Autowired
	private JobRepository jobRepository;

	public Optional<Pass> findById(long id) {
		return passRepository.findById(id);
	}

	public Pass deleteById(long id) {
		return findById(id).map(p -> {
			delete(p);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	private void recalculateRanks(Set<Pass> passes) {
		List<Pass> lstPasses = new ArrayList<>(passes);
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass p = lstPasses.get(i);
			p.setRank(i);
			passRepository.save(p);
		}
	}

	private void delete(Pass p) {
		List<Job> jobs = jobRepository.findByPassesId(p.getId());
		for (Job j : jobs) {
			j.deletePass(p);
			j = jobRepository.save(j);
			recalculateRanks(j.getPasses());
		}

	}
}