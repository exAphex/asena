package com.asena.scimgateway.processor;

import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.script.ScriptRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptProcessor {
    private static Logger logger = LoggerFactory.getLogger(ScriptProcessor.class);

    public static Object processTransformation(Attribute a, Object o, RemoteSystem rs) {
        ScriptRunner sr = new ScriptRunner(rs);
        sr.addScriptFunction(a.getTransformation());
        logger.info("Executing script:" + a.getTransformation());
        return sr.execute(a.getTransformation(), o);
       
    }
}