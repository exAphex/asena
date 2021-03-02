package com.asena.scimgateway.model;

import java.util.ArrayList;
import java.util.List;

public class ModificationStep {
    private List<Modification> modifications = new ArrayList<>();
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addModification(Modification m) {
        if ((m != null) && (findModificationByAttribute(m.getAttributeName()) == null)) {
            modifications.add(m);
        }
    }

    public Modification findModificationByAttribute(String attributeName) {
        if (attributeName == null) {
            return null;
        }

        for (Modification m : modifications) {
            String attr = m.getAttributeName();
            if (attributeName.equals(attr)) {
                return m;
            }
        }

        return null;
    }

    public void setModificationValueByAttribute(String attributeName, Object o) {
        if (attributeName == null) {
            return;
        }

        for (Modification m : modifications) {
            String attr = m.getAttributeName();
            if (attributeName.equals(attr)) {
                m.setValue(o);
            }
        }
    }

    public void upsertModification(Modification m) {
        if (m == null) {
            return;
        }

        if (findModificationByAttribute(m.getAttributeName()) == null) {
            modifications.add(m);
        } else {
            setModificationValueByAttribute(m.getAttributeName(), m.getValue());
        }
    }

    public List<Modification> getModifications() {
        return modifications;
    }
}