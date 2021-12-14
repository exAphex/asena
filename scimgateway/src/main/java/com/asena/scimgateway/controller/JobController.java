package com.asena.scimgateway.controller;

import com.asena.scimgateway.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public void deletePackage(@PathVariable long id) {
		jobService.deleteById(id);
	}

}