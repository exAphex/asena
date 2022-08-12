package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.dto.jobs.PassMappingDTO;
import com.asena.scimgateway.model.jobs.PassMapping;
import com.asena.scimgateway.service.PassMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/passmapping")
public class PassMappingController {

	@Autowired
	private PassMappingService mappingService;

	@PreAuthorize("isAdmin()")
	@DeleteMapping("/{id}")
	public void deleteMapping(@PathVariable long id) {
		mappingService.deleteById(id);
	}

	@PreAuthorize("isAdmin()")
	@GetMapping("/{id}")
	public PassMappingDTO getPassMapping(@PathVariable long id) {
		PassMapping p = mappingService.findById(id).orElseThrow(() -> new NotFoundException(id));
		return PassMappingDTO.toDTO(p);
	}

	@PreAuthorize("isAdmin()")
	@PutMapping("/{id}")
	public PassMappingDTO updateMapping(@RequestBody PassMappingDTO pmDTO, @PathVariable long id) {
		PassMapping pm = pmDTO.fromDTO();
		return PassMappingDTO.toDTO(mappingService.update(pm, id));
	}
}