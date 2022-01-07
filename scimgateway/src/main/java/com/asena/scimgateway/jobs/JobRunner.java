package com.asena.scimgateway.jobs;

import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.service.JobService;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.jobs.executor.ProcessPassExecutor;

@DisallowConcurrentExecution
@Component

public class JobRunner extends QuartzJobBean {

	@Autowired
	private JobService jobService;

	private Logger logger = LoggerFactory.getLogger(JobRunner.class);

	@Autowired
	private ProcessPassExecutor processPassExecutor;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("New Jobrunner started....");
		long jobId = -1;
		String jobName = context.getJobDetail().getKey().getName();

		jobId = Long.parseLong(jobName);

		Job j = jobService.findById(jobId).orElseThrow(() -> new NotFoundException(0l));
		try {
			executeJob(j);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		System.out.println(j.getName());

		logger.info("Jobrunner finished!");
	}

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

				processPassExecutor.execute(p);
				break;
			case WRITE:
				break;
			case READ:
				break;
		}
	}
}