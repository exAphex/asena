package com.asena.scimgateway.model.jobs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.asena.scimgateway.model.RemoteSystem;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "passes")
public class Pass {

	public enum PassType {
		WRITE, READ, PROCESS
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passes_seq")
	@SequenceGenerator(name = "passes_seq", sequenceName = "passes_sequence", allocationSize = 1)
	private long id;
	private String name;
	private String description;
	private PassType type;
	private long rank;
	private String tableName;
	private boolean clearTable;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PassProperty> properties = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PassMapping> mappings = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_id")
	private RemoteSystem system;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		return hcb.toHashCode();
	}

	public boolean isClearTable() {
		return clearTable;
	}

	public void setClearTable(boolean clearTable) {
		this.clearTable = clearTable;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public RemoteSystem getSystem() {
		return system;
	}

	public void setSystem(RemoteSystem system) {
		this.system = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PassType getType() {
		return type;
	}

	public void setType(PassType type) {
		this.type = type;
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

	public long getId() {
		return this.id;
	}

	public void addProperty(PassProperty p) {
		if (this.properties == null) {
			this.properties = new HashSet<>();
		}

		if (p != null) {
			this.properties.add(p);
		}
	}

	public void deleteProperty(PassProperty p) {
		this.properties.remove(p);
	}

	public void addMapping(PassMapping p) {
		if (this.mappings == null) {
			this.mappings = new HashSet<>();
		}

		if (p != null) {
			this.mappings.add(p);
		}
	}

	public void deleteMapping(PassMapping p) {
		this.mappings.remove(p);
	}

	public Set<PassProperty> getProperties() {
		return this.properties;
	}

	public Set<PassMapping> getMappings() {
		return this.mappings;
	}

}