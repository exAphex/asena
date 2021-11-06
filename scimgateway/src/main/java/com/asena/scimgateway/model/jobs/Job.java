package com.asena.scimgateway.model.jobs;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job {
	private String name;
	private String fqn;
	private List<Step> steps;
}