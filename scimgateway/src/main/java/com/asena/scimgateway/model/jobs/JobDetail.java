package com.asena.scimgateway.model.jobs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "qrtz_job_details")
@IdClass(JobId.class)
public class JobDetail {

    @Id
    @Column(name = "sched_name")
    private String schedName;

    @Id
    @Column(name = "job_name")
    private String name;

    @Id
    @Column(name = "job_group")
    private String group;

    @Column(name = "description")
    private String description;

    @Column(name = "job_class_name")
    private String className;

    @Column(name = "is_durable")
    private boolean durable;

    @Column(name = "is_nonconcurrent")
    private boolean nonConcurrent;

    @Column(name = "is_update_data")
    private boolean updateData;

    @Column(name = "requests_recovery")
    private boolean requestsRecovery;

    @Column(name = "job_data")
    private byte[] data;

    public String getSchedName() {
        return schedName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(boolean requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public boolean isUpdateData() {
        return updateData;
    }

    public void setUpdateData(boolean updateData) {
        this.updateData = updateData;
    }

    public boolean isNonConcurrent() {
        return nonConcurrent;
    }

    public void setNonConcurrent(boolean nonConcurrent) {
        this.nonConcurrent = nonConcurrent;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }
}
