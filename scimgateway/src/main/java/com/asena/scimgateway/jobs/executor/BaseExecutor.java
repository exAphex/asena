package com.asena.scimgateway.jobs.executor;

import java.util.HashMap;
import java.util.Set;

import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;

public class BaseExecutor {

	protected Pass pass;

	public BaseExecutor(Pass pass) {
		setPass(pass);
	}

	protected HashMap<String, String> processObject(HashMap<String, String> object, Set<PassMapping> mappings) {
		HashMap<String, String> mapResult = new HashMap<>();
		for (PassMapping pm : mappings) {
			String source = pm.getSource();
			String destination = pm.getDestination();
			String processed = object.get(source);
			mapResult.put(destination, processed);
		}
		return mapResult;
	}

	public Pass getPass() {
		return pass;
	}

	public void setPass(Pass pass) {
		this.pass = pass;
	}

}