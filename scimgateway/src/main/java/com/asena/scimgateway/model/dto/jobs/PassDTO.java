package com.asena.scimgateway.model.dto.jobs;

import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.Pass.PassType;

public class PassDTO {

	private long id;
	private String name;
	private PassType type;

	public static PassDTO toDTO(Pass p) {
		PassDTO pDTO = new PassDTO();

		if (p == null) {
			return null;
		}

		pDTO.setId(p.getId());
		pDTO.setName(p.getName());
		pDTO.setType(p.getType());

		return pDTO;
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