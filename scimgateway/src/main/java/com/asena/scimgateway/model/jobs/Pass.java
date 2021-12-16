package com.asena.scimgateway.model.jobs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "passes")
public class Pass {

	public enum PassType {
		READ_PASS, READ_PROCESS, WRITE_PASS
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passes_seq")
	@SequenceGenerator(name = "passes_seq", sequenceName = "passes_sequence", allocationSize = 1)
	private long id;
	private String name;
	private PassType type;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public PassType getType() {
		return type;
	}

	public void setType(PassType type) {
		this.type = type;
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

	public long getId() {
		return this.id;
	}
}