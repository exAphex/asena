package com.asena.scimgateway.model.jobs;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "steps")
public class Step {
	private long id;
	private String name;
	private Task preProcess;
	private Task postProcess;
	private List<Task> tasks;
}