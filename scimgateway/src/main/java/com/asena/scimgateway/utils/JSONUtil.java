package com.asena.scimgateway.utils;

import com.jayway.jsonpath.JsonPath;

public class JSONUtil {
    public static Object getObjectFromPath(Object obj, String path) throws Exception{
        Object retObj = JsonPath.parse(obj).read(path);
        return retObj;
    }
}