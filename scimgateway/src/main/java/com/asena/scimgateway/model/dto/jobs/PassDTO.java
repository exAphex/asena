package com.asena.scimgateway.model.dto.jobs;

import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.Pass.PassType;

public class PassDTO {

	private long id;
	private String name;
	private PassType type;
	private String description;
	private RemoteSystem system;

	public static PassDTO toDTO(Pass p) {
		PassDTO pDTO = new PassDTO();

		if (p == null) {
			return null;
		}

		pDTO.setId(p.getId());
		pDTO.setName(p.getName());
		pDTO.setType(p.getType());
		pDTO.setDescription(p.getDescription());
		pDTO.setSystem(p.getSystem());

		return pDTO;
	}

	public RemoteSystem getSystem() {
		return system;
	}

	public void setSystem(RemoteSystem system) {
		this.system = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PassType getType() {
		return type;
	}

	public void setType(PassType type) {
		this.type = type;
	}

	public Pass fromDTO() {
		Pass p = new Pass();
		p.setId(getId());
		p.setName(getName());
		p.setType(getType());
		p.setDescription(getDescription());
		p.setSystem(getSystem());

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

}