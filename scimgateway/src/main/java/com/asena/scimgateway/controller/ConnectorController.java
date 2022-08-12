package com.asena.scimgateway.controller;

import com.asena.scimgateway.connector.IConnector;
import com.asena.scimgateway.processor.ConnectorProcessor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
@Validated
@RequestMapping("/api/v1/connector")
public class ConnectorController {

	@PreAuthorize("isAdmin()")
	@GetMapping("/{type}")
	public IConnector getConnectorDetails(@PathVariable String type) {
		return ConnectorProcessor.getConnectorByType(type);
	}

}