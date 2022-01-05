package com.asena.scimgateway.jobs.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;

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
		String sQuery = this.pass.getSourceQuery();

		List<HashMap<String, String>> lstData = processData(sQuery);
		writeData(lstData);
	}

	private HashMap<String, String> processRow(HashMap<String, String> object, Set<PassMapping> mappings) {
		HashMap<String, String> mapResult = new HashMap<>();
		for (PassMapping pm : mappings) {
			String source = pm.getSource();
			String destination = pm.getDestination();
			String processed = object.get(source);
			mapResult.put(destination, processed);
		}
		return mapResult;
	}

	private List<HashMap<String, String>> processData(String sQuery) {
		List<HashMap<String, String>> lstResult = new ArrayList<>();
		Set<PassMapping> mappings = pass.getMappings();
		// Execute Query
		List<HashMap<String, String>> lstSQLResult = new ArrayList<>();

		for (HashMap<String, String> row : lstSQLResult) {
			lstResult.add(processRow(row, mappings));
		}

		return lstResult;
	}

	private void writeData(List<HashMap<String, String>> lstData) {

	}

}