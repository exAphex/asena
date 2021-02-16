package com.asena.scimgateway.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "entrytypemappings")
public class EntryTypeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrytypemappings_seq")
    @SequenceGenerator(name = "entrytypemappings_seq", sequenceName = "entrytypemappings_sequence", allocationSize = 1)
    private long id;

    private String name;

    public EntryTypeMapping() {}
    
    public EntryTypeMapping(String name) {
        setName(name);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attribute> writeMappings;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attribute> readMappings;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(id);
        return hcb.toHashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Attribute> getReadMappings() {
        return readMappings;
    }

    public void setReadMappings(Set<Attribute> readMappings) {
        this.readMappings = readMappings;
    }

    public Set<Attribute> getWriteMappings() {
        return writeMappings;
    }

    public void setWriteMappings(Set<Attribute> writeMappings) {
        this.writeMappings = writeMappings;
    }

    public EntryTypeMapping addWriteMapping(Attribute a) {
        if (this.writeMappings == null) {
            this.writeMappings = new HashSet<>();
        }

        if ((a != null) && (!isWriteMappingDuplicate(a))){
            writeMappings.add(a);
        }
        return this;
    }

    public EntryTypeMapping addReadMapping(Attribute a) {
        if (this.readMappings == null) {
            this.readMappings = new HashSet<>();
        }

        if ((a != null) && (!isReadMappingDuplicate(a))){
            readMappings.add(a);
        }
        return this;
    }

    private boolean isWriteMappingDuplicate(Attribute attr) {
        if ((attr == null) || (attr.getDestination() == null)) {
            return false;
        }

        if (this.writeMappings == null) {
            return false;
        }

        for (Attribute a : this.writeMappings) {
            if (attr.getDestination().equals(a.getDestination())) {
                return true;
            }
        }

        return false;
    }

    private boolean isReadMappingDuplicate(Attribute attr) {
        if ((attr == null) || (attr.getDestination() == null)) {
            return false;
        }

        if (this.readMappings == null) {
            return false;
        }

        for (Attribute a : this.readMappings) {
            if (attr.getDestination().equals(a.getDestination())) {
                return true;
            }
        }

        return false;
    }

    public void deleteWriteMapping(Attribute a) {
        writeMappings.remove(a);
    }

    public void deleteReadMapping(Attribute a) {
        readMappings.remove(a);
    }
}