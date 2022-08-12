package com.asena.scimgateway.model.dto.jobs;

import java.util.ArrayList;
import java.util.List;

import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Pass;

public class JobDTO {

	private long id;
	private String name;
	private String description;
	private boolean enabled;
	private List<PassDTO> passes = new ArrayList<>();

	public static JobDTO toDTO(Job j) {
		JobDTO jDTO = new JobDTO();

		if (j == null) {
			return null;
		}

		jDTO.setId(j.getId());
		jDTO.setName(j.getName());
		jDTO.setDescription(j.getDescription());
		jDTO.setEnabled(j.isEnabled());

		for (Pass p : j.getPasses()) {
			jDTO.addPass(PassDTO.toDTO(p));
		}

		return jDTO;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Job fromDTO() {
		Job j = new Job();
		j.setId(getId());
		j.setName(getName());
		j.setDescription(getDescription());
		j.setEnabled(isEnabled());

		for (PassDTO p : getPasses()) {
			j.addPass(p.fromDTO());
		}

		return j;
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

	public void addPass(PassDTO p) {
		this.passes.add(p);
	}

	public List<PassDTO> getPasses() {
		return this.passes;
	}

}