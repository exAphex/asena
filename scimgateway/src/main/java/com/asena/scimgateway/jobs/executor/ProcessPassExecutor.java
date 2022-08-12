package com.asena.scimgateway.jobs.executor;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asena.scimgateway.model.jobs.Pass;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.stereotype.Component;

@Component
public class ProcessPassExecutor extends BaseExecutor {

	public void execute(Pass p) throws JsonProcessingException {
		String sQuery = p.getSourceQuery();
		List<Map<String, Object>> data = queryData(sQuery);
		List<Map<String, Object>> processedData = processData(p, data);
		writeData(processedData, p);
	}

	private List<Map<String, Object>> queryData(String sourceQuery) {
		List<Map<String, Object>> retLst = jdbcTemplate.query(sourceQuery, (rs, rowNum) -> {
			Map<String, Object> retData = new HashMap<>();
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