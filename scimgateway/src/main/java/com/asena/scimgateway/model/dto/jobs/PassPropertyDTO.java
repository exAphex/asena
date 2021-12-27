package com.asena.scimgateway.model.dto.jobs;

import com.asena.scimgateway.model.jobs.PassProperty;

public class PassPropertyDTO {
	private long id;
	private String key;
	private String value;
	private String description;

	public static PassPropertyDTO toDTO(PassProperty pp) {
		PassPropertyDTO ppDTO = new PassPropertyDTO();

		if (pp == null) {
			return null;
		}

		ppDTO.setId(pp.getId());
		ppDTO.setKey(pp.getKey());
		ppDTO.setValue(pp.getValue());
		ppDTO.setDescription(pp.getDescription());
		return ppDTO;
	}

	public PassProperty fromDTO() {
		PassProperty pp = new PassProperty();

		pp.setId(getId());
		pp.setKey(getKey());
		pp.setValue(getValue());
		pp.setDescription(getDescription());

		return pp;
	}

	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setId(long id) {
		this.id = id;
	}
}