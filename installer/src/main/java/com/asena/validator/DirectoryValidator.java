package com.asena.validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public class DirectoryValidator implements IValidator {

    @Override
    public String validate(Step s) throws ValidationException {
        String input;

        if (s == null) {
            throw new ValidationException("Step is null");
        }

        input = s.getInput();

        try {
            Files.createDirectories(Paths.get(input));
        } catch (IOException e) {
            throw new ValidationException("No access to provided directory!");
        }

        return input;
    }
    
}