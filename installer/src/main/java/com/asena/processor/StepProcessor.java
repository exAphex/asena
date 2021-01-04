package com.asena.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.asena.exception.ValidationException;
import com.asena.model.Step;

public class StepProcessor {
    
    public static String processStep(Scanner scanner, Step s) throws ValidationException {
        String input = scanner.nextLine();
        String retVal = "";
        s.setInput(input);
        retVal = s.getValidator().validate(s);
        return retVal;
    }

    public static HashMap<String, String> processSteps(Scanner scanner, List<Step> steps) {
        HashMap<String,String> retMap = new HashMap<>();
        boolean stepDone = false;
        for (Step s : steps) {
            stepDone = false;
            while (!stepDone) {
                try {
                    System.out.println(s.getMessage() + " [" + s.getDefaultValue() + "]:");
                    retMap.put(s.getName(), processStep(scanner, s));
                    stepDone = true;
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return retMap;
    }
}