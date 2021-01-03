package com.asena.scimgateway.processor;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.script.ScriptRunner;

public class ScriptProcessor {
    public static Object processTransformation(Attribute a, Object o) {
        ScriptRunner sr = new ScriptRunner();
        sr.addScriptFunction(a.getTransformation());
        return sr.execute(a.getTransformation(), o);
       
    }
}