package com.asena.scimgateway.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "logging_event")
public class Log {

    @Id
    @Column(name = "event_id")
    private long id;

    @Column(name = "formatted_message")
    private String message;

    @Column(name = "level_string")
    private String type;

    @Column(name = "timestmp")
    private long timestamp;

    @OneToMany(mappedBy="event_id", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LogDetail> event;

    public String getMessage() {
        return this.message;
    }

    public Set<LogDetail> getEvent() {
        return event;
    }

    public void setEvent(Set<LogDetail> event) {
        this.event = event;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return this.type;
    }
}