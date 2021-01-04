package com.asena.model;

import com.asena.validator.IValidator;

public class Step {
    public enum InputType {
        STRING, INT
    }

    private String name;
    private String message;
    private String input;
    private String defaultValue;
    private IValidator validator;

    public Step(String name, String message, String defaultValue, IValidator validator) {
        this.name = name;
        this.message = message;
        this.defaultValue = defaultValue;
        this.setValidator(validator);
    }

    public IValidator getValidator() {
        return validator;
    }

    public void setValidator(IValidator validator) {
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

}