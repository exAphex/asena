package com.asena.scimgateway.model;

import java.io.Serializable;

public class LogDetailPK implements Serializable {

    private static final long serialVersionUID = -5663128979661097757L;

    private long event_id;

    private short i;

    public long getEvent_id() {
        return event_id;
    }

    public short getI() {
        return i;
    }

    public void setI(short i) {
        this.i = i;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

}