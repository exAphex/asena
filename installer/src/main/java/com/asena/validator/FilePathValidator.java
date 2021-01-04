package com.asena.validator;

import java.io.File;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public class FilePathValidator implements IValidator {

    @Override
    public String validate(Step s) throws ValidationException {
        String input;
        File f;
        if (s == null) {
            throw new ValidationException("Step is null");
        }

        input = s.getInput();
        f = new File(input);
        if(!f.exists() || f.isDirectory()) { 
            throw new ValidationException("File " + input + " not found!");
        }

        return input;
    }
    
}