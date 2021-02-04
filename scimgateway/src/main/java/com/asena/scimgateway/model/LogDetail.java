package com.asena.scimgateway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "logging_event_exception")
@IdClass(LogDetailPK.class)
public class LogDetail {

    @Id
    @Column(name = "event_id")
    private long event_id;

    @Id
    @Column(name = "i")
    private short i;

    @Column(name = "trace_line")
    private String message;

    public short getI() {
        return i;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public void setI(short i) {
        this.i = i;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}