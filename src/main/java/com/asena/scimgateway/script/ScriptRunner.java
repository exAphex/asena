package com.asena.scimgateway.script;

import com.asena.scimgateway.model.Script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

public class ScriptRunner {
    private Scriptable scope;
    private Context context;
    
    public ScriptRunner() {
        this.context = Context.enter();
        this.scope = this.context.initStandardObjects(new ImporterTopLevel(this.context));
    }

    public void addScriptFunction(Script s) {
        if (s != null) {
            this.context.evaluateString(this.scope, s.getContent(), s.getName(), 1, null);
        }
    }

    public Object execute(Script s, Object param) {
        Object retData = null;
        if (s != null) {
            Object obj = this.scope.get(s.getName(), this.scope);
            if (obj instanceof Function) {
                Object[] funcParams = {param};
                Function f = (Function) obj;
                retData = f.call(this.context, this.scope, this.scope, funcParams);
            }
        }
        return retData;
    }
}