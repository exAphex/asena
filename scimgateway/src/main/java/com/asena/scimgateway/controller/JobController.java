package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.dto.jobs.JobDTO;
import com.asena.scimgateway.model.dto.jobs.PassDTO;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.service.JobService;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@PreAuthorize("isAdmin()")
	@DeleteMapping("/{id}")
	public void deleteJob(@PathVariable long id) {
		jobService.deleteById(id);
	}

	@PreAuthorize("isAdmin()")
	@GetMapping("/{id}")
	public JobDTO getJob(@PathVariable long id) {
		Job p = jobService.findById(id).orElseThrow(() -> new NotFoundException(id));
		return JobDTO.toDTO(p);
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/pass")
	public JobDTO addPass(@RequestBody PassDTO pDTO, @PathVariable long id) {
		return JobDTO.toDTO(jobService.addPass(pDTO.fromDTO(), id));
	}

	@PreAuthorize("isAdmin()")
	@PutMapping("/{id}")
	public JobDTO modifyJob(@RequestBody JobDTO jDTO, @PathVariable long id) {
		return JobDTO.toDTO(jobService.update(jDTO.fromDTO(), id));
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/{passId}/moveup")
	public JobDTO moveupPass(@PathVariable long id, @PathVariable long passId) {
		return JobDTO.toDTO(jobService.moveUp(passId, id));
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/{passId}/movedown")
	public JobDTO movedownPass(@PathVariable long id, @PathVariable long passId) {
		return JobDTO.toDTO(jobService.moveDown(passId, id));
	}

	@PreAuthorize("isAdmin()")
	@GetMapping("/{id}/run")
	public JobDTO movedownPass(@PathVariable long id) throws SchedulerException {
		return JobDTO.toDTO(jobService.startJob(id));
	}

}