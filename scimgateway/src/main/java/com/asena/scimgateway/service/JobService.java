package com.asena.scimgateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.jobs.JobRunner;
import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.EntryTypeMapping;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.repository.JobRepository;
import com.asena.scimgateway.repository.PackageRepository;
import com.asena.scimgateway.repository.PassRepository;
import com.asena.scimgateway.scheduler.JobScheduleCreator;
import com.asena.scimgateway.model.jobs.Package;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassProperty;
import com.asena.scimgateway.processor.ConnectorProcessor;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class JobService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PackageRepository packageRepository;

	@Autowired
	private RemoteSystemService remoteSystemService;

	@Autowired
	private PassRepository passRepository;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private JobScheduleCreator scheduleCreator;

	@Autowired
	private ApplicationContext context;

	public Optional<Job> findById(long id) {
		return jobRepository.findById(id);
	}

	public Job deleteById(long id) {
		return findById(id).map(p -> {
			delete(p);
			return p;
		}).orElseThrow(() -> new NotFoundException(id));
	}

	private void delete(Job j) {
		List<Package> packages = packageRepository.findByJobsId(j.getId());
		for (Package p : packages) {
			p.deleteJob(j);
			p = packageRepository.save(p);
		}

	}

	private long getMaxPassRank(Set<Pass> passes) {
		long retRank = 0;
		for (Pass p : passes) {
			if (p.getRank() >= retRank) {
				retRank = p.getRank() + 1;
			}
		}
		return retRank;
	}

	private void recalculateRanks(Set<Pass> passes) {
		List<Pass> lstPasses = new ArrayList<>(passes);
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass p = lstPasses.get(i);
			p.setRank(i);
			passRepository.save(p);
		}
	}

	public Job moveUp(long passId, long jobId) {
		Job j = findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
		List<Pass> lstPasses = new ArrayList<>(j.getPasses());
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass tmpPass = lstPasses.get(i);
			if (tmpPass.getId() == passId) {
				long oldRank = tmpPass.getRank();
				if (i > 0) {
					Pass tmpChangePass = lstPasses.get(i - 1);
					long newRank = tmpChangePass.getRank();
					tmpChangePass.setRank(oldRank);
					tmpPass.setRank(newRank);
					passRepository.save(tmpChangePass);
					passRepository.save(tmpPass);
				}
			}
		}
		return findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
	}

	public Job moveDown(long passId, long jobId) {
		Job j = findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
		List<Pass> lstPasses = new ArrayList<>(j.getPasses());
		lstPasses.sort((o1, o2) -> (o1.getRank() > o2.getRank() ? 1 : -1));
		for (int i = 0; i < lstPasses.size(); i++) {
			Pass tmpPass = lstPasses.get(i);
			if (tmpPass.getId() == passId) {
				long oldRank = tmpPass.getRank();
				if (i + 1 < lstPasses.size()) {
					Pass tmpChangePass = lstPasses.get(i + 1);
					long newRank = tmpChangePass.getRank();
					tmpChangePass.setRank(oldRank);
					tmpPass.setRank(newRank);
					passRepository.save(tmpChangePass);
					passRepository.save(tmpPass);
				}
			}
		}
		return findById(jobId).orElseThrow(() -> new NotFoundException(jobId));
	}

	public Job addPass(Pass p, long id) {
		Job j = findById(id).orElseThrow(() -> new NotFoundException(id));
		Pass pass = new Pass();
		pass.setName(p.getName());
		pass.setDescription(p.getDescription());
		pass.setType(p.getType());
		pass.setRank(getMaxPassRank(j.getPasses()));

		RemoteSystem rs = p.getSystem();
		if (rs != null) {
			String rsId = rs.getId();
			rs = remoteSystemService.findById(rsId).orElseThrow(() -> new NotFoundException(rsId));
			pass.setSystem(rs);

			RemoteSystem connector = ConnectorProcessor.getRemoteSystemByType(rs.getType());
			Set<ConnectionProperty> props = connector.getProperties();
			if (props != null) {
				for (ConnectionProperty cp : props) {
					pass.addProperty(new PassProperty(cp.getKey(),
							"$function.getRemoteSystemProperty(" + cp.getKey() + ")", cp.getDescription()));
				}
			}
			Set<EntryTypeMapping> ets = connector.getEntryTypeMappings();
			if (ets != null) {
				for (EntryTypeMapping e : ets) {
					pass.setEntityType(e.getName());
					break;
				}
			}
		}
		pass = passRepository.save(pass);

		j.addPass(pass);
		j = jobRepository.save(j);

		recalculateRanks(j.getPasses());

		return findById(id).orElseThrow(() -> new NotFoundException(id));
	}

	public Job update(Job j, long id) {
		return findById(id).map(r -> {
			r.setDescription(j.getDescription());
			r.setEnabled(j.isEnabled());
			return jobRepository.save(r);
		}).orElseThrow(() -> new NotFoundException(id));
	}

	/*
	 * private Trigger getOndemandTrigger(Scheduler scheduler, long id) { Trigger
	 * retTrigger = null; try { retTrigger =
	 * scheduler.getTrigger(TriggerKey.triggerKey("ondemand_" + id)); if (retTrigger
	 * == null) { retTrigger = scheduleCreator.createSimpleTrigger("ondemand_" + id,
	 * new Date(), (long) 1, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW); } } catch
	 * (SchedulerException e) { e.printStackTrace(); } return retTrigger; }
	 */

	public Job startJob(long id) throws SchedulerException {
		Job j = findById(id).orElseThrow(() -> new NotFoundException(id));
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(JobRunner.class).withIdentity(j.getId() + "", "jobs").build();
		if (!scheduler.checkExists(jobDetail.getKey())) {

			jobDetail = scheduleCreator.createJob(JobRunner.class, false, context, j.getId() + "", "jobs");
			scheduler.addJob(jobDetail, true);
		}
		List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
		for (Trigger t : triggers) {
			scheduler.unscheduleJob(t.getKey());
		}
		scheduler.triggerJob(jobDetail.getKey());

		return j;
	}

	public void deleteAll() {
		List<Job> jobs = jobRepository.findAll();
		for (Job j : jobs) {
			delete(j);
		}
	}
}