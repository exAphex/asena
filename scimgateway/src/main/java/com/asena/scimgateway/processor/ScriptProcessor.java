package com.asena.scimgateway.processor;

import com.asena.scimgateway.logger.Logger;
import com.asena.scimgateway.logger.LoggerFactory;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.script.ScriptRunner;

public class ScriptProcessor {
    private static Logger logger = LoggerFactory.getLogger();

    public static Object processTransformation(Attribute a, Object o) {
        ScriptRunner sr = new ScriptRunner();
        sr.addScriptFunction(a.getTransformation());
        logger.info("Executing script:" + a.getTransformation());
        return sr.execute(a.getTransformation(), o);
       
    }
}