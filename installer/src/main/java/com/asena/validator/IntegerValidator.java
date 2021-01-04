package com.asena.validator;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

import org.apache.commons.lang3.StringUtils;

public class IntegerValidator implements IValidator {

    @Override
    public String validate(Step s) throws ValidationException {
        String input = "";

        if (s == null) {
            throw new ValidationException("Step is null");
        }

        input = s.getInput();
        if ((input == null) || (input.length() == 0)) {
            input = s.getDefaultValue();
        }

        if (!StringUtils.isNumeric(input)) {
            throw new ValidationException(input + " is not a valid integer for parameter " + s.getName());
        }

        return input;
    }

}