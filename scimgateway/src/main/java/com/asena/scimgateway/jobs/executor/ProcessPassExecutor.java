package com.asena.scimgateway.jobs.executor;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;
import com.asena.scimgateway.script.ScriptRunner;
import com.asena.scimgateway.service.ScriptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

@Component
public class ProcessPassExecutor extends BaseExecutor {

	private static String variablePrefix = "${";
	private static String variableSuffix = "}";
	private static String functionPrefix = "$function.";
	private static String functionStart = "(";
	private static String functionEnd = ")";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ScriptService scriptService;

	private void handleTable(Pass p, List<PassMapping> passes) {
		if (p.isClearTable()) {
			jdbcTemplate.execute("DROP TABLE IF EXISTS " + p.getTableName() + ";");
		}

		String createTableSQL = "CREATE TABLE IF NOT EXISTS " + p.getTableName() + "(";
		for (PassMapping pm : passes) {
			createTableSQL += pm.getDestination() + " varchar(255),";
		}

		if (createTableSQL.substring(createTableSQL.length() - 1).equals(",")) {
			createTableSQL = createTableSQL.substring(0, createTableSQL.length() - 1);
		}
		createTableSQL += ");";
		jdbcTemplate.execute(createTableSQL);
	}

	private String getPlaceHolderSQL(List<PassMapping> passes) {
		String placeHolderMappingSQL = "";
		for (int i = 0; i < passes.size(); i++) {
			placeHolderMappingSQL += "?,";
		}

		if (placeHolderMappingSQL.substring(placeHolderMappingSQL.length() - 1).equals(",")) {
			placeHolderMappingSQL = placeHolderMappingSQL.substring(0, placeHolderMappingSQL.length() - 1);
		}

		return placeHolderMappingSQL;
	}

	private String getInsertValuesSQL(List<PassMapping> passes) {
		String insertMappingSQL = "";
		for (PassMapping pm : passes) {
			insertMappingSQL += pm.getDestination() + ",";
		}

		if (insertMappingSQL.substring(insertMappingSQL.length() - 1).equals(",")) {
			insertMappingSQL = insertMappingSQL.substring(0, insertMappingSQL.length() - 1);
		}

		return insertMappingSQL;
	}

	public void execute(Pass p) {
		String sQuery = p.getSourceQuery();
		List<Map<String, String>> data = queryData(sQuery);
		List<Map<String, String>> processedData = processData(p, data);
		writeData(processedData, p);
	}

	private void writeData(List<Map<String, String>> data, Pass p) {
		List<PassMapping> lstPasses = new ArrayList<>(p.getMappings());
		lstPasses.sort((o1, o2) -> (o1.getDestination().compareTo(o2.getDestination())));

		handleTable(p, lstPasses);
		String placeHolderMappingSQL = getPlaceHolderSQL(lstPasses);
		String insertMappingSQL = getInsertValuesSQL(lstPasses);

		for (Map<String, String> d : data) {
			String insertSQL = "INSERT INTO " + p.getTableName() + " (" + insertMappingSQL + ") VALUES ("
					+ placeHolderMappingSQL + ")";

			jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

					for (int i = 0; i < lstPasses.size(); i++) {
						ps.setString(i + 1, d.get(lstPasses.get(i).getDestination()));
					}
					return ps.execute();

				}
			});
		}
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

	private List<Map<String, String>> processData(Pass pass, List<Map<String, String>> data) {
		List<Map<String, String>> lstResult = new ArrayList<>();
		Set<PassMapping> mappings = pass.getMappings();
		for (Map<String, String> elem : data) {
			lstResult.add(processObject(elem, mappings, pass.getSystem()));
		}

		return lstResult;
	}

	protected HashMap<String, String> processObject(Map<String, String> object, Set<PassMapping> mappings,
			RemoteSystem rs) {
		HashMap<String, String> mapResult = new HashMap<>();
		for (PassMapping pm : mappings) {
			String source = pm.getSource();
			String destination = pm.getDestination();
			String expandedStr = expandStr(source, object);
			String evaluedStr = evalStr(expandedStr, rs);
			mapResult.put(destination, evaluedStr);
		}
		return mapResult;
	}

	String evalStr(String str, RemoteSystem rs) {
		String retStr = str;
		int idxFunctionStart = str.indexOf(functionPrefix);
		int idxFunctionEnd = -1;
		if (idxFunctionStart >= 0) {
			idxFunctionEnd = str.indexOf(functionEnd, idxFunctionStart);
			if (idxFunctionEnd > idxFunctionStart) {
				idxFunctionEnd++;
				int idxLastFunctionStart = str.lastIndexOf(functionPrefix, idxFunctionEnd);
				String strPrefix = str.substring(0, idxLastFunctionStart);
				String strFunctionCall = str.substring(idxLastFunctionStart + functionPrefix.length(), idxFunctionEnd);
				String strSuffix = str.substring(idxFunctionEnd);
				int idxFirstParenthese = strFunctionCall.indexOf(functionStart);

				if (idxFirstParenthese >= 0) {
					String strFunctionName = strFunctionCall.substring(0, idxFirstParenthese);
					String strParameter = strFunctionCall.substring(idxFirstParenthese + functionStart.length());
					strParameter = strParameter.substring(0, strParameter.length() - 1);
					String strCallResult = runScript(strFunctionName, strParameter, rs);
					retStr = strPrefix + strCallResult + strSuffix;
				} else {
					retStr = strPrefix + strSuffix;
				}
				idxFunctionStart = retStr.indexOf(functionPrefix);
				if (idxFunctionStart >= 0) {
					return evalStr(retStr, rs);
				}
			}
		}
		return retStr;
	}

	String runScript(String scriptName, String param, RemoteSystem rs) {
		Script s = scriptService.findByName(scriptName);
		if (s == null) {
			throw new InternalErrorException("No script found with name: " + scriptName);
		}

		ScriptRunner sr = new ScriptRunner(rs);
		sr.addScriptFunction(s);
		return sr.execute(s, param).toString();
	}

	String expandStr(String str, Map<String, String> object) {
		int idxAttr = str.indexOf(variablePrefix);
		int idxAttrFinish = -1;
		String retStr = str;
		if (idxAttr >= 0) {
			idxAttrFinish = str.indexOf("}", idxAttr + variablePrefix.length());
			if (idxAttrFinish > idxAttr) {
				String prefix = str.substring(0, idxAttr);
				String strAttr = str.substring(idxAttr + variablePrefix.length(), idxAttrFinish);
				String suffix = str.substring(idxAttrFinish + variableSuffix.length());
				retStr = expandStr(prefix + object.get(strAttr) + suffix, object);
			}
		}
		return retStr;
	}

}