package com.asena.scimgateway.controller;

import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.model.dto.jobs.PassDTO;
import com.asena.scimgateway.model.dto.jobs.PassMappingDTO;
import com.asena.scimgateway.model.dto.jobs.PassPropertyDTO;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.service.PassService;

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
@RequestMapping("/api/v1/pass")
public class PassController {

	@Autowired
	private PassService passService;

	@PreAuthorize("isAdmin()")
	@DeleteMapping("/{id}")
	public void deletePass(@PathVariable long id) {
		passService.deleteById(id);
	}

	@PreAuthorize("isAdmin()")
	@GetMapping("/{id}")
	public PassDTO getPass(@PathVariable long id) {
		Pass p = passService.findById(id).orElseThrow(() -> new NotFoundException(id));
		return PassDTO.toDTO(p);
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/property")
	public PassDTO addProperty(@RequestBody PassPropertyDTO pDTO, @PathVariable long id) {
		return PassDTO.toDTO(passService.addPassProperty(pDTO.fromDTO(), id));
	}

	@PreAuthorize("isAdmin()")
	@PostMapping("/{id}/mapping")
	public PassDTO addMapping(@RequestBody PassMappingDTO pDTO, @PathVariable long id) {
		return PassDTO.toDTO(passService.addPassMapping(pDTO.fromDTO(), id));
	}

	@PreAuthorize("isAdmin()")
	@PutMapping("/{id}")
	public PassDTO modifyJob(@RequestBody PassDTO pDTO, @PathVariable long id) {
		return PassDTO.toDTO(passService.update(pDTO.fromDTO(), id));
	}
}