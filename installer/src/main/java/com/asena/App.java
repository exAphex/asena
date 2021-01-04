package com.asena;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

import com.asena.model.Step;
import com.asena.processor.StepProcessor;
import com.asena.security.SecurityUtils;
import com.asena.validator.DatabaseTypeValidator;
import com.asena.validator.DirectoryValidator;
import com.asena.validator.FilePathValidator;
import com.asena.validator.IntegerValidator;
import com.asena.validator.StringValidator;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        List<Step> steps;
        HashMap<String, String> retValues;
        showHeader();
        steps = prepareSteps();
        retValues = StepProcessor.processSteps(scanner, steps);
        postProcessSteps(retValues);

    }

    public static List<Step> prepareSteps() {
        List<Step> steps = new ArrayList<>();
        
        steps.add(new Step("com.asena.scimgateway.installationpath", "Provide an absolute path for the installation folder", "", new DirectoryValidator()));
        steps.add(new Step("com.asena.scimgateway.jdbc", "Provide the absolute path to the jdbc driver", "", new FilePathValidator()));
        steps.add(new Step("spring.datasource.url", "Provide a jdbc url to your database",
                "jdbc:postgresql://localhost:5431/postgres", new StringValidator()));
        steps.add(new Step("spring.datasource.username", "Provide the name of the databaseuser", "postgres",
                new StringValidator()));
        steps.add(new Step("spring.datasource.password", "Provide the password of the database user", "password",
                new StringValidator()));
        steps.add(new Step("spring.jpa.properties.hibernate.dialect", "Provide the type of your database", "postgres",
                new DatabaseTypeValidator()));
        steps.add(new Step("server.port", "Provide a port for the scim gateway to listen to", "8080",
                new IntegerValidator()));

        return steps;
    }

    public static void showHeader() {
        System.out.println("=================");
        System.out.println("Asena Installer");
        System.out.println("=================");
    }

    public static HashMap<String, String> postProcessSteps(HashMap<String, String> stepValues) throws NoSuchAlgorithmException {
        stepValues.put("spring.jpa.hibernate.ddl-auto", "update");
        stepValues.put("com.asena.scimgateway.security.key", SecurityUtils.generateKey());
        return stepValues;
    }
}
