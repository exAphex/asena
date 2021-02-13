package com.asena.scimgateway.script;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.ScriptableObject;

public class GlobalScripts extends ImporterTopLevel {

    private static final long serialVersionUID = -3475930588680580956L;

    public GlobalScripts(Context ctx) {
        super(ctx);
        ctx.initStandardObjects(this, false);
        createHooks();
    }

    private void createHooks() {
        List<String> hooks = new ArrayList<>();
        hooks.add("getSystemProperty");

        String[] hookNames = new String[hooks.size()];
        hookNames = hooks.toArray(hookNames);

        defineFunctionProperties(hookNames, GlobalScripts.class, ScriptableObject.DONTENUM);
    }
    
}