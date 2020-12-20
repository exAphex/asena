package com.asena.scimgateway.model.dto;

import com.asena.scimgateway.model.Script;

public class ScriptDTO {
    private long id;
    private String name;
    private String content;

    public static ScriptDTO toDTO(Script s) {
        ScriptDTO sDTO = new ScriptDTO();
        sDTO.setId(s.getId());
        sDTO.setName(s.getName());
        sDTO.setContent(s.getContent());
        return sDTO;
    }

    public Script fromDTO() {
        Script s = new Script();
        s.setId(getId());
        s.setName(getName());
        s.setContent(getContent());
        return s;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }

    
}