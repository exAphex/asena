package com.asena.scimgateway.jobs.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.processor.SCIMProcessor;

import org.springframework.stereotype.Component;

@Component
public class ReaderPassExecutor extends BaseExecutor {

	public void execute(Pass p) throws Exception {
		RemoteSystem rs = p.getSystem();
		if (rs == null) {
			throw new InternalErrorException("No system selected on Pass " + p.getName() + " (" + p.getId() + ")");
		}

		if (!rs.isActive()) {
			throw new InternalErrorException("Remote system is not enabled.");
		}

		List<HashMap<String, Object>> result = new SCIMProcessor(rs, p.getEntityType()).getEntitiesRaw(null);
		List<Map<String, Object>> processedData = processDataHashMap(p, result);
		writeData(processedData, p);

		System.out.println(result);
	}

}