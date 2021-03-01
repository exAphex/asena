package com.asena.scimgateway.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JSONUtil {

    private JSONUtil() {}

    public static Object getObjectFromPath(Object obj, String path) throws Exception{
        Object retObj = JsonPath.parse(obj).read(path);
        return retObj;
    }

    public static void create(DocumentContext context, String path, Object value) {
        int pos = path.lastIndexOf('.');
        String parent = path.substring(0, pos);
        String child = path.substring(pos + 1);
        try {
            context.read(parent); // EX if parent missing
        } catch (PathNotFoundException e) {
            create(context, parent, new LinkedHashMap<>()); // (recursively) Create missing parent
        }
        context.put(parent, child, value);
    }

    public static Object getFromJSONPath(String path, Object obj) {
        Object retObj = null;
        try {
            retObj = JSONUtil.getObjectFromPath(obj, path);
        } catch (Exception e) {
            retObj = null;
        }
        return retObj;
    }

    public static void addPropertyToJSON(DocumentContext jsonContext, String src, String dest, HashMap<String,Object> entity) {
        if (entity.containsKey(src)) {
            Object o = entity.get(src);
            JSONUtil.create(jsonContext, dest, o);
        }
    }
}