package com.asena.scimgateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

	private long getMaxPassRank(Set<Pass> passes) {
		long retRank = 0;
		for (Pass p : passes) {
			if (p.getRank() >= retRank) {
				retRank = p.getRank() + 1;
			}
		}
		return retRank;
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

	public Job moveUp(long passId, long jobId) {
		Job j = findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
		List<Pass> lstPasses = new ArrayList<>(j.getPasses());
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass tmpPass = lstPasses.get(i);
			if (tmpPass.getId() == passId) {
				long oldRank = tmpPass.getRank();
				if (i > 0) {
					Pass tmpChangePass = lstPasses.get(i - 1);
					long newRank = tmpChangePass.getRank();
					tmpChangePass.setRank(oldRank);
					tmpPass.setRank(newRank);
					passRepository.save(tmpChangePass);
					passRepository.save(tmpPass);
				}
			}
		}
		return findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
	}

	public Job moveDown(long passId, long jobId) {
		Job j = findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
		List<Pass> lstPasses = new ArrayList<>(j.getPasses());
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass tmpPass = lstPasses.get(i);
			if (tmpPass.getId() == passId) {
				long oldRank = tmpPass.getRank();
				if (i + 1 < lstPasses.size()) {
					Pass tmpChangePass = lstPasses.get(i + 1);
					long newRank = tmpChangePass.getRank();
					tmpChangePass.setRank(oldRank);
					tmpPass.setRank(newRank);
					passRepository.save(tmpChangePass);
					passRepository.save(tmpPass);
				}
			}
		}
		return findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
	}

	public Job addPass(Pass p, long id) {
		Job j = findById(id).orElseThrow(() -> new NotFoundException(id));
		Pass pass = new Pass();
		pass.setName(p.getName());
		pass.setDescription(p.getDescription());
		pass.setType(p.getType());
		pass.setRank(getMaxPassRank(j.getPasses()));

		RemoteSystem rs = p.getSystem();
		if (rs != null) {
			String rsId = rs.getId();
			rs = remoteSystemService.findById(rsId).orElseThrow(() -> new NotFoundException(rsId));
			pass.setSystem(rs);
		}
		pass = passRepository.save(pass);

		j.addPass(pass);
		j = jobRepository.save(j);

		recalculateRanks(j.getPasses());

		return findById(id).orElseThrow(() -> new NotFoundException(id));
	}

}