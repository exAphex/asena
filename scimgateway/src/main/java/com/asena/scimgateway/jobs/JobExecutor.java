package com.asena.scimgateway.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.jobs.executor.ProcessPassExecutor;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Pass;

public class JobExecutor {
	public void executeJob(Job j) {
		Set<Pass> passes = j.getPasses();
		List<Pass> lstPasses = new ArrayList<>(passes);
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (Pass p : lstPasses) {
			executePass(p);
		}
	}

	public void executePass(Pass p) {
		if (p == null) {
			return;
		}

		switch (p.getType()) {
			case PROCESS:
				ProcessPassExecutor pe = new ProcessPassExecutor(p);
				pe.execute();
				break;
			case WRITE:
				break;
			case READ:
				break;
		}
	}
}