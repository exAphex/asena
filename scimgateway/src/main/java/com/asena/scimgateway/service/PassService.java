package com.asena.scimgateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;
import com.asena.scimgateway.model.jobs.PassProperty;
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

	public Pass addPassProperty(PassProperty prop, long id) {
		Pass p = findById(id).orElseThrow(() -> new NotFoundException(id));

		PassProperty pp = new PassProperty();
		pp.setKey(prop.getKey());
		pp.setValue(prop.getValue());
		pp.setDescription(prop.getDescription());

		p.addProperty(pp);
		return passRepository.save(p);
	}

	public Pass addPassMapping(PassMapping pm, long id) {
		Pass p = findById(id).orElseThrow(() -> new NotFoundException(id));

		PassMapping pp = new PassMapping();
		pp.setSource(pm.getSource());
		pp.setDestination(pm.getDestination());

		p.addMapping(pp);
		return passRepository.save(p);
	}

	public Pass update(Pass p, long id) {
		return findById(id).map(r -> {
			r.setClearTable(p.isClearTable());
			r.setDescription(p.getDescription());
			r.setTableName(p.getTableName());
			return passRepository.save(r);
		}).orElseThrow(() -> new NotFoundException(id));
	}
}