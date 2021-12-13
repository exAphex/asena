package com.asena.scimgateway.model.jobs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "packages")
public class Package {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packages_seq")
	@SequenceGenerator(name = "packages_seq", sequenceName = "packages_sequence", allocationSize = 1)
	private long id;

	@Column(unique = true)
	@NotBlank(message = "System name is mandatory")
	private String name;

	public long getId() {
		return this.id;
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