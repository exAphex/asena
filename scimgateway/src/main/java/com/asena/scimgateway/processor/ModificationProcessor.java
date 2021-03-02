package com.asena.scimgateway.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Modification;
import com.asena.scimgateway.model.Modification.ModificationType;

public class ModificationProcessor {

    @SuppressWarnings("unchecked")
    public static List<Modification> patch(HashMap<String, Object> obj) {
        List<String> schema = (List<String>) obj.get("schemas");
        List<HashMap<String, Object>> operations = (List<HashMap<String,Object>>) obj.get("Operations");
        List<Modification> modifications = new ArrayList<>();
        if (schema == null) {
            throw new InternalErrorException("No schema found on PATCH request");
        }

        if (operations == null) {
            throw new InternalErrorException("No operations attribute found on PATCH request");
        }

        if (!schema.contains("urn:ietf:params:scim:api:messages:2.0:PatchOp")) {
            throw new InternalErrorException("Schema for PATCH request not supported!");
        }

        for (HashMap<String,Object> elem : operations) {
            Object elemOp = elem.get("op");
            Object elemPath = elem.get("path");
            HashMap<String,Object> elemValue = (HashMap<String,Object>) elem.get("value");
            if ((elemOp == null) || (elemPath == null) || (elemPath == null)) {
                continue;
            }

            if (elemOp.equals("add")) {
                modifications.add(new Modification((String) elemPath, elemValue.get("value"), ModificationType.COMPLEX_ADD));
            } else if (elemOp.equals("remove")) {
                modifications.add(new Modification((String) elemPath, elemValue.get("value"), ModificationType.COMPLEX_REMOVE));
            } else {
                modifications.add(new Modification((String) elemPath, elemValue.get("value")));
            }
        }
        return modifications;
    }
}