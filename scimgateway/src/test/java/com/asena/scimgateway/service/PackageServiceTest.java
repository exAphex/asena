package com.asena.scimgateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.jobs.Job;
import com.asena.scimgateway.model.jobs.Package;

@SpringBootTest
@ActiveProfiles("test")
public class PackageServiceTest {

	@Autowired
	private PackageService packageService;

	@Autowired
	private JobService jobService;

	@BeforeEach
	void prepareDb() {
		jobService.deleteAll();
		packageService.deleteAll();
	}

	@Test
	void createTest() {
		Package p = new Package();
		p.setName("test");

		p = packageService.create(p);
		assertEquals("test", p.getName());
	}

	@Test
	void listTest() {
		Package p = new Package();
		p.setName("test");
		p = packageService.create(p);

		p = new Package();
		p.setName("test2");
		p = packageService.create(p);

		List<Package> packages = packageService.list();
		assertEquals(2, packages.size());
	}

	@Test
	void deleteTest() {
		Package p = new Package();
		p.setName("test");
		p = packageService.create(p);

		packageService.delete(null);
		assertThrows(NotFoundException.class, () -> {
			packageService.deleteById(-1);
		});
		packageService.delete(p);
		List<Package> packages = packageService.list();
		assertEquals(0, packages.size());

	}

	@Test
	void addJobTest() {
		Package p = new Package();
		p.setName("test");
		p = packageService.create(p);

		assertThrows(NotFoundException.class, () -> {
			packageService.addJob(new Job(), -1);
		});

		Job j = new Job();
		j.setName("testjob");
		j.setDescription("testjobdescription");
		j.setEnabled(true);

		p = packageService.addJob(j, p.getId());

		assertEquals(1, p.getJobs().size());
	}
}
