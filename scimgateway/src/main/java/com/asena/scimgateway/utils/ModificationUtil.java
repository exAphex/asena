package com.asena.scimgateway.utils;

import java.util.HashMap;

import com.asena.scimgateway.model.Modification;
import com.asena.scimgateway.model.ModificationStep;

public class ModificationUtil {
    public static HashMap<String, Object> collectSimpleModifications(ModificationStep ms) {
        HashMap<String, Object> retData = new HashMap<String, Object>();
        for (Modification m : ms.getModifications()) {
            retData.put(m.getAttributeName(), m.getValue());
        }
        return retData;
    }
}