package com.asena.scimgateway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "scripts")
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scripts_seq")
    @SequenceGenerator(name = "scripts_seq", sequenceName = "scripts_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "Scriptname is mandatory")
    private String name;
    private String content;

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }
}