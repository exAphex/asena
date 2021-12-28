package com.asena.scimgateway.controller;

import com.asena.scimgateway.service.PassPropertyService;

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
@RequestMapping("/api/v1/passproperty")
public class PassPropertyController {

	@Autowired
	private PassPropertyService propertyService;

	@PreAuthorize("isAdmin()")
	@DeleteMapping("/{id}")
	public void deleteProperty(@PathVariable long id) {
		propertyService.deleteById(id);
	}
}