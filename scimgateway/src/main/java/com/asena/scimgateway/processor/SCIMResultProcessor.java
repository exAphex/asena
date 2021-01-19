package com.asena.scimgateway.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.asena.scimgateway.exception.InternalErrorException;
import com.asena.scimgateway.model.Attribute;
import com.asena.scimgateway.model.RemoteSystem;

public class SCIMResultProcessor {
    public static void addMetaDataCreate(LinkedHashMap<Object, Object> obj, RemoteSystem rs, String id) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/gateway/" + rs.getId() + "/scim/v2/Users/" + id));
        obj.put("meta", meta);

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        obj.put("schemas", schemas);
        
        obj.put("id", id); 
    }
    
    public static void addMetaDataList(HashMap<String, Object> resultEntry, HashMap<String, Object> sourceEntry, RemoteSystem rs, Attribute nameIdAttr) {
        if ((nameIdAttr == null) || (nameIdAttr.getDestination().length() < 1)) {
            throw new InternalErrorException("No nameId set on remote system " + rs.getName());
        }

        Object nameIdObj = sourceEntry.get(nameIdAttr.getDestination());
        resultEntry.put("id", nameIdObj);

        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/gateway/" + rs.getId() + "/scim/v2/Users/" + nameIdObj));
        resultEntry.put("meta", meta);

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        resultEntry.put("schemas", schemas);
    }

    public static HashMap<String, Object> createSCIMResult(List<HashMap<String, Object>> items) {
        HashMap<String, Object> scimResult = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
        scimResult.put("schemas", schemas);
        scimResult.put("totalResults", items.size());
        scimResult.put("startIndex", 1);
        scimResult.put("itemsPerPage", items.size());
        scimResult.put("Resources", items);
        return scimResult;
    }
}