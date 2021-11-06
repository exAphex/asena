package com.asena.scimgateway.model.jobs;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "packages")
public class Package {

	@Id
	private String packageName;
	private List<Job> jobs;
}