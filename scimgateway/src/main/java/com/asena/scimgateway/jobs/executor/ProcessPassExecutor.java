package com.asena.scimgateway.jobs.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;

public class ProcessPassExecutor extends BaseExecutor {

	public ProcessPassExecutor(Pass pass) {
		super(pass);
	}

	public void execute() {
		String sQuery = this.pass.getSourceQuery();

		List<HashMap<String, String>> lstData = processData(sQuery);
		writeData(lstData);
	}

	private List<HashMap<String, String>> processData(String sQuery) {
		List<HashMap<String, String>> lstResult = new ArrayList<>();
		Set<PassMapping> mappings = pass.getMappings();
		// Execute Query
		List<HashMap<String, String>> lstSQLResult = new ArrayList<>();

		for (HashMap<String, String> row : lstSQLResult) {
			lstResult.add(processObject(row, mappings));
		}

		return lstResult;
	}

	private void writeData(List<HashMap<String, String>> lstData) {

	}

}