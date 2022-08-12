package com.asena.scimgateway.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.dto.jobs.JobDTO;
import com.asena.scimgateway.model.dto.jobs.PackageDTO;
import com.asena.scimgateway.model.jobs.Package;
import com.asena.scimgateway.service.PackageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/package")
public class PackageController {

	@Autowired
	private PackageService packageService;

	@PreAuthorize("isAdmin()")
	@GetMapping("")
	public Set<PackageDTO> getPackages() {
		Set<PackageDTO> retDTO = new HashSet<>();
		List<Package> packages = packageService.list();
		for (Package p : packages) {
			retDTO.add(PackageDTO.toDTO(p));
		}
		return retDTO;
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("")
	public PackageDTO createPackage(@RequestBody PackageDTO pDTO) {
		Package p = pDTO.fromDTO();
		return PackageDTO.toDTO(packageService.create(p));
	}

	@PreAuthorize("isAdmin()")
	@DeleteMapping("/{id}")
	public void deletePackage(@PathVariable long id) {
		packageService.deleteById(id);
	}

	@PreAuthorize("isAdmin()")
	@GetMapping("/{id}")
	public PackageDTO getPackage(@PathVariable long id) {
		Package p = packageService.findById(id).orElseThrow(() -> new NotFoundException(id));
		return PackageDTO.toDTO(p);
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/job")
	public PackageDTO addJob(@RequestBody JobDTO jDTO, @PathVariable long id) {
		return PackageDTO.toDTO(packageService.addJob(jDTO.fromDTO(), id));
	}
}