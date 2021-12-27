package com.asena.scimgateway.model.jobs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "passproperties")
public class PassProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passproperty_seq")
	@SequenceGenerator(name = "passproperty_seq", sequenceName = "passproperty_sequence", allocationSize = 1)
	private long id;
	private String key;
	private String value;
	private String description;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setId(long id) {
		this.id = id;
	}
}