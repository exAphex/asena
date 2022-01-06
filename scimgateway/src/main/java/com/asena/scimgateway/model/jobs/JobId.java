package com.asena.scimgateway.model.jobs;

import java.io.Serializable;

public class JobId implements Serializable {

	private static final long serialVersionUID = 2570295224456779813L;

	private String schedName;

	private String name;

	private String group;

	public String getSchedName() {
		return schedName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}
}