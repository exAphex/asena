package com.asena.scimgateway.model.jobs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Job> jobs = new HashSet<>();

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

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	public Set<Job> getJobs() {
		return this.jobs;
	}

	public void addJob(Job j) {
		if (this.jobs == null) {
			this.jobs = new HashSet<>();
		}

		if ((j != null) && (!isJobDuplicate(j))) {
			this.jobs.add(j);
		}
	}

	private boolean isJobDuplicate(Job j) {
		if ((j == null) || (j.getName() == null)) {
			return false;
		}

		if (this.jobs == null) {
			return false;
		}

		for (Job e : this.jobs) {
			if (j.getName().equals(e.getName())) {
				return true;
			}
		}

		return false;
	}

	public void deleteJob(Job j) {
		this.jobs.remove(j);
	}
}