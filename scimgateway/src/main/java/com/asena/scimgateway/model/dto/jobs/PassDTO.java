package com.asena.scimgateway.model.dto.jobs;

import java.util.ArrayList;
import java.util.List;

import com.asena.scimgateway.model.dto.RemoteSystemDTO;
import com.asena.scimgateway.model.jobs.Pass;
import com.asena.scimgateway.model.jobs.PassMapping;
import com.asena.scimgateway.model.jobs.PassProperty;
import com.asena.scimgateway.model.jobs.Pass.PassType;

public class PassDTO {

	private long id;
	private String name;
	private PassType type;
	private String description;
	private RemoteSystemDTO system;
	private long rank;
	private String tableName;
	private boolean clearTable;
	private String sourceQuery;
	private String entityType;

	private List<PassPropertyDTO> properties = new ArrayList<>();

	private List<PassMappingDTO> mappings = new ArrayList<>();

	public static PassDTO toDTO(Pass p) {
		PassDTO pDTO = new PassDTO();

		if (p == null) {
			return null;
		}

		pDTO.setId(p.getId());
		pDTO.setName(p.getName());
		pDTO.setType(p.getType());
		pDTO.setDescription(p.getDescription());
		pDTO.setSystem(RemoteSystemDTO.toDTO(p.getSystem()));
		pDTO.setRank(p.getRank());
		pDTO.setTableName(p.getTableName());
		pDTO.setClearTable(p.isClearTable());
		pDTO.setSourceQuery(p.getSourceQuery());
		pDTO.setEntityType(p.getEntityType());

		for (PassProperty pp : p.getProperties()) {
			pDTO.addProperty(PassPropertyDTO.toDTO(pp));
		}

		for (PassMapping pm : p.getMappings()) {
			pDTO.addMapping(PassMappingDTO.toDTO(pm));
		}

		return pDTO;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getSourceQuery() {
		return sourceQuery;
	}

	public void setSourceQuery(String sourceQuery) {
		this.sourceQuery = sourceQuery;
	}

	public List<PassMappingDTO> getMappings() {
		return mappings;
	}

	public void setMappings(List<PassMappingDTO> mappings) {
		this.mappings = mappings;
	}

	public List<PassPropertyDTO> getProperties() {
		return properties;
	}

	public void setProperties(List<PassPropertyDTO> properties) {
		this.properties = properties;
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

	public RemoteSystemDTO getSystem() {
		return system;
	}

	public void setSystem(RemoteSystemDTO system) {
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

	public Pass fromDTO() {
		Pass p = new Pass();
		p.setId(getId());
		p.setName(getName());
		p.setType(getType());
		p.setDescription(getDescription());
		p.setRank(getRank());
		p.setTableName(getTableName());
		p.setClearTable(isClearTable());
		p.setSourceQuery(getSourceQuery());
		p.setEntityType(getEntityType());

		if (getSystem() != null) {
			p.setSystem(getSystem().fromDTO());
		}

		for (PassPropertyDTO pp : getProperties()) {
			p.addProperty(pp.fromDTO());
		}

		for (PassMappingDTO pm : getMappings()) {
			p.addMapping(pm.fromDTO());
		}

		return p;
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

	public void setId(long id) {
		this.id = id;
	}

	public void addProperty(PassPropertyDTO p) {
		this.properties.add(p);
	}

	public void addMapping(PassMappingDTO p) {
		this.mappings.add(p);
	}

}