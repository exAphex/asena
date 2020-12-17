package com.asena.scimgateway.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "logs")
public class Log {
    public enum LogType {
        INFO, DEBUG, WARNING, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logs_seq")
    @SequenceGenerator(name = "logs_seq", sequenceName = "logs_sequence", allocationSize = 1)
    private long id;
    private Date timestamp;

    @Enumerated(EnumType.ORDINAL)
    private LogType type;

    @Column(length = 1024)
    private String message;

    public Log(String message, LogType type) {
        setMessage(message);
        this.type = type;
        this.timestamp = new java.sql.Date(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if ((message != null) && (message.length() >= 1024)) {
            message = message.substring(0,1023);
        }
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }
}