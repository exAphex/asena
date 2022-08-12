package com.asena.scimgateway.model.jobs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "jobs")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobs_seq")
	@SequenceGenerator(name = "jobs_seq", sequenceName = "jobs_sequence", allocationSize = 1)
	private long id;

	@NotBlank(message = "System name is mandatory")
	private String name;
	private String description;
	private boolean enabled;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Pass> passes = new HashSet<>();

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public long getId() {
		return this.id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void addPass(Pass p) {
		if (this.passes == null) {
			this.passes = new HashSet<>();
		}

		if (p != null) {
			this.passes.add(p);
		}
	}

	public void deletePass(Pass p) {
		this.passes.remove(p);
	}

	public Set<Pass> getPasses() {
		return this.passes;
	}

	public void setPasses(Set<Pass> passes) {
		this.passes = passes;
	}

}