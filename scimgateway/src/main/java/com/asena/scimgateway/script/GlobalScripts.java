package com.asena.scimgateway.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asena.scimgateway.model.ConnectionProperty;
import com.asena.scimgateway.model.RemoteSystem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.ScriptableObject;

public class GlobalScripts extends ImporterTopLevel {

    private static final long serialVersionUID = -3475930588680580956L;

    private RemoteSystem remoteSystem;

    public GlobalScripts(Context ctx, RemoteSystem rs) {
        super(ctx);
        ctx.initStandardObjects(this, false);
        this.setRemoteSystem(rs);
        createHooks();
    }

    public RemoteSystem getRemoteSystem() {
        return remoteSystem;
    }

    public void setRemoteSystem(RemoteSystem remoteSystem) {
        this.remoteSystem = remoteSystem;
    }

    private void createHooks() {
        List<String> hooks = new ArrayList<>();
        hooks.add("getSystemProperty");
        hooks.add("getRemoteSystem");
        hooks.add("getSystemId");
        hooks.add("println");
        hooks.add("readJSONFromString");

        String[] hookNames = new String[hooks.size()];
        hookNames = hooks.toArray(hookNames);

        defineFunctionProperties(hookNames, GlobalScripts.class, ScriptableObject.DONTENUM);
    }

    public String getSystemProperty(String searchKey) {
        if ((searchKey == null) || (remoteSystem == null)) {
            return null;
        }

        Set<ConnectionProperty> props = remoteSystem.getProperties();
        for (ConnectionProperty p : props) {
            if ((p.getKey() != null) && (p.getKey().equals(searchKey))) {
                return p.getValue();
            }
        }
        return null;
    }

    public String getSystemId() {
        if (remoteSystem != null) {
            return remoteSystem.getId();
        } else {
            return null;
        }
    }

    public String println(Object s) {
        System.out.println(s);
        return null;
    }

    public HashMap<String, Object> readJSONFromString(String json)
            throws JsonMappingException, JsonProcessingException {
        return new ObjectMapper().readValue(json, HashMap.class);
    }

}