package com.asena.scimgateway.model.dto.jobs;

import com.asena.scimgateway.model.jobs.PassMapping;

public class PassMappingDTO {

	private long id;
	private String source;
	private String destination;

	public static PassMappingDTO toDTO(PassMapping pm) {
		PassMappingDTO pmDTO = new PassMappingDTO();

		if (pm == null) {
			return null;
		}

		pmDTO.setId(pm.getId());
		pmDTO.setSource(pm.getSource());
		pmDTO.setDestination(pm.getDestination());

		return pmDTO;
	}

	public PassMapping fromDTO() {
		PassMapping pm = new PassMapping();
		pm.setId(getId());
		pm.setSource(getSource());
		pm.setDestination(getDestination());

		return pm;
	}

	public long getId() {
		return id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setId(long id) {
		this.id = id;
	}

}