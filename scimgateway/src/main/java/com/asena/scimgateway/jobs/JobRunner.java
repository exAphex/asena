package com.asena.scimgateway.jobs;

import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.service.JobService;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.asena.scimgateway.exception.NotFoundException;

@DisallowConcurrentExecution
public class JobRunner extends QuartzJobBean {

	@Autowired
	private JobService jobService;

	private Logger logger = LoggerFactory.getLogger(JobRunner.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("New Jobrunner started....");
		long jobId = -1;
		String jobName = context.getJobDetail().getKey().getName();

		jobId = Long.parseLong(jobName);

		Job j = jobService.findById(jobId).orElseThrow(() -> new NotFoundException(0l));
		System.out.println(j.getName());

		logger.info("Jobrunner finished!");
	}
}