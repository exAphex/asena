package com.asena.scimgateway.script;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.RemoteSystem;
import com.asena.scimgateway.model.Script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptRunner {
    private Scriptable scope;
    private Context context;

    private Logger logger = LoggerFactory.getLogger(ScriptRunner.class);
    
    public ScriptRunner(RemoteSystem rs) {
        this.context = Context.enter();
        this.scope = this.context.initStandardObjects(new GlobalScripts(this.context, rs));
    }

    public void addScriptFunction(Script s) {
        if (s != null) {
            this.context.evaluateString(this.scope, s.getContent(), s.getName(), 1, null);
        }
    }

    public Object execute(Script s, Object param) {
        Object retData = param;
        if ((s != null) && (s.getName() != null) && (s.getContent() != null)) {
            Object obj = this.scope.get(s.getName(), this.scope);
            if (obj instanceof Function) {
                Object[] funcParams = {param};
                Function f = (Function) obj;
                retData = f.call(this.context, this.scope, this.scope, funcParams);
            } else {
                throw new InternalErrorException("Script " + s.getName() + " not found!");
            }
        } else {
            logger.warn("Could not find script. Either script doesnt exist, has no name or no content");
        }
        return retData;
    }
}