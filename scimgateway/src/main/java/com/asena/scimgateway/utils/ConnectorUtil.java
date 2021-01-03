package com.asena.scimgateway.utils;

import java.util.HashMap;

public class ConnectorUtil {

    private ConnectorUtil() {}
    
    public static Object getAttributeValue(String key, HashMap<String, Object> attrs) {
        if ((attrs == null) || (key == null)) {
            return null;
        }

        for (String k : attrs.keySet()) {
            if ((k != null) && (k.toUpperCase().equals(key.toUpperCase()))) {
                return attrs.get(k);
            }
        }
        return null;
    }
}