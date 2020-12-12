package com.asena.scimgateway.utils;

import java.util.HashMap;

public class ConnectorUtil {
    public static Object getAttributeValue(String key, HashMap<String, Object> attrs) {
        for (String k : attrs.keySet()) {
            if (k.toUpperCase().equals(key.toUpperCase())) {
                return attrs.get(k);
            }
        }
        return null;
    }
}