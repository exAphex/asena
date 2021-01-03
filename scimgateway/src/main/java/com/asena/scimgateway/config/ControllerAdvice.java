package com.asena.scimgateway.config;

import com.asena.scimgateway.exception.APIError;
import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.exception.NotFoundException;
import com.asena.scimgateway.logger.Logger;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class ControllerAdvice {
    
    @Autowired
    private Logger logger;
    
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    APIError NotFoundHandler(NotFoundException ex) {
        int code = 404;
        String error = "NotFound";
        return new APIError(code, error, ex.getMessage());
    }

    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    APIError InternalServerErrorHandler(InternalErrorException ex) {
        int code = 500;
        String error = "Internal Server Error";
        logger.error(ex);
        return new APIError(code, error, ex.getMessage());
    }
}