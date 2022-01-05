package com.asena.scimgateway.jobs.executor;

import com.asena.scimgateway.model.jobs.Pass;

public class ProcessPassExecutor {
	private Pass pass;

	public ProcessPassExecutor(Pass pass) {
		setPass(pass);
	}

	public Pass getPass() {
		return pass;
	}

	public void setPass(Pass pass) {
		this.pass = pass;
	}

	public void execute() {

	}

}