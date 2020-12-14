package com.asena.scimgateway.exception;

public class InternalErrorException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 5409117063462161396L;
    
    public InternalErrorException(String id) {
        super(id);
    }
}