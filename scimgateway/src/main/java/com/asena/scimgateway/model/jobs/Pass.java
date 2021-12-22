package com.asena.scimgateway.model.jobs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.asena.scimgateway.model.RemoteSystem;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "passes")
public class Pass {

	public enum PassType {
		WRITE, READ, PROCESS
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passes_seq")
	@SequenceGenerator(name = "passes_seq", sequenceName = "passes_sequence", allocationSize = 1)
	private long id;
	private String name;
	private String description;
	private PassType type;
	private long rank;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_id")
	private RemoteSystem system;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
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