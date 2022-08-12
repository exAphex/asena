package com.asena.scimgateway.model.dto.jobs;

import java.util.ArrayList;
import java.util.List;

import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Package;

public class PackageDTO {

	private long id;
	private String name;
	private List<JobDTO> jobs = new ArrayList<>();

	public static PackageDTO toDTO(Package p) {
		PackageDTO pDTO = new PackageDTO();

		if (p == null) {
			return null;
		}

		pDTO.setId(p.getId());
		pDTO.setName(p.getName());

		for (Job j : p.getJobs()) {
			pDTO.addJob(JobDTO.toDTO(j));
		}

		return pDTO;
	}

	public Package fromDTO() {
		Package p = new Package();
		p.setId(getId());
		p.setName(getName());

		for (JobDTO j : getJobs()) {
			p.addJob(j.fromDTO());
		}
		return p;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void addJob(JobDTO j) {
		this.jobs.add(j);
	}

	public List<JobDTO> getJobs() {
		return this.jobs;
	}
}