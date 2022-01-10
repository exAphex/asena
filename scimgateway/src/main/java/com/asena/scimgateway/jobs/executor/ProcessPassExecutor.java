package com.asena.scimgateway.jobs.executor;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asena.scimgateway.model.jobs.Pass;

import org.springframework.stereotype.Component;

@Component
public class ProcessPassExecutor extends BaseExecutor {

	public void execute(Pass p) {
		String sQuery = p.getSourceQuery();
		List<Map<String, String>> data = queryData(sQuery);
		List<Map<String, String>> processedData = processData(p, data);
		writeData(processedData, p);
	}

	private List<Map<String, String>> queryData(String sourceQuery) {
		List<Map<String, String>> retLst = jdbcTemplate.query(sourceQuery, (rs, rowNum) -> {
			Map<String, String> retData = new HashMap<>();
			ResultSetMetaData meta = rs.getMetaData();
			int columns = meta.getColumnCount();
			for (int i = 1; i <= columns; ++i) {
				retData.put(meta.getColumnName(i), rs.getString(i));
			}
			return retData;
		});

		return retLst;
	}

}