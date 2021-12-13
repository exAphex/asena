package com.asena.scimgateway.model.dto.jobs;

import com.asena.scimgateway.model.jobs.Package;

public class PackageDTO {

	private long id;
	private String name;

	public static PackageDTO toDTO(Package p) {
		PackageDTO pDTO = new PackageDTO();

		if (p == null) {
			return null;
		}

		pDTO.setId(p.getId());
		pDTO.setName(p.getName());
		return pDTO;
	}

	public Package fromDTO() {
		Package p = new Package();
		p.setId(getId());
		p.setName(getName());
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