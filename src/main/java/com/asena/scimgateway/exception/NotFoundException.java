package com.asena.scimgateway.exception;

import com.asena.scimgateway.model.RemoteSystem;

public class NotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -5666881963271508941L;

    public NotFoundException(Long id) {
        super("Could not find id: " + id);
    }

    public NotFoundException(RemoteSystem rs) {
        super("Could not find connector type: " + rs.getType());
    }
}