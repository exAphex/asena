package com.asena.scimgateway.model.jobs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.asena.scimgateway.model.Script;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "passmappings")
public class PassMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passmapping_seq")
	@SequenceGenerator(name = "passmapping_seq", sequenceName = "passmapping_sequence", allocationSize = 1)
	private long id;
	private String source;
	private String destination;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "script_id")
	private Script script;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public long getId() {
		return id;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setId(long id) {
		this.id = id;
	}

}