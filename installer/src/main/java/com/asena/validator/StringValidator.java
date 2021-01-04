package com.asena.validator;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public class StringValidator implements IValidator {

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

        return input;
    }
    
}