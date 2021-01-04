package com.asena.validator;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public class DatabaseTypeValidator implements IValidator {

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

        switch (input) {
            case "postgres":
                input = "org.hibernate.dialect.PostgreSQL95Dialect";
                break;
            default:
                throw new ValidationException("Unkown database type!"); 
        } 

        return input;
    }
    
}