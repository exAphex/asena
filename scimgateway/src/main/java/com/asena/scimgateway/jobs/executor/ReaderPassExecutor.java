package com.asena.scimgateway.jobs.executor;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.jobs.Pass;

import org.springframework.stereotype.Component;

@Component
public class ReaderPassExecutor extends BaseExecutor {

	public void execute(Pass p) {
		RemoteSystem rs = p.getSystem();
		if (rs == null) {
			throw new InternalErrorException("No system selected on Pass " + p.getName() + " (" + p.getId() + ")");
		}

		if (!rs.isActive()) {
			throw new InternalErrorException("Remote system is not enabled.");
		}

	}

}