package com.asena.scimgateway.model.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.asena.scimgateway.model.Log;
import com.asena.scimgateway.model.Log.LogType;

public class LogDTO {
    private long id;
    private Instant timestamp;
    private LogType type;
    private String message;

    public static LogDTO toDTO(Log l) {
        LogDTO lDTO = new LogDTO();

        if (l == null) {
            return null;
        }
        
        lDTO.setId(l.getId());
        lDTO.setMessage(l.getMessage());
        lDTO.setTimestamp(l.getTimestamp());
        lDTO.setType(l.getType());

        return lDTO;
    }

    public static List<LogDTO> toDTO(List<Log> ls) {
        List<LogDTO> lss = new ArrayList<>();

        if (ls == null) {
            return lss;
        }

        for (Log l : ls) {
            lss.add(toDTO(l));
        }
        return lss;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }
}