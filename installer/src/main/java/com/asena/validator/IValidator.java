package com.asena.validator;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public interface IValidator {
    public String validate(Step s) throws ValidationException;
}