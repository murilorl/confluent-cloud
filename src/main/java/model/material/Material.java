package model.material;

import model.BaseEntity;

public class Material extends BaseEntity {

	private String id;
	private String materialNumber;

	private String changedBy;

	private String materialType;

	private String industrySector;

	private String eventType;

	public String getMaterialNumber() {

		return materialNumber;

	}

	public void setMaterialNumber(String materialNumber) {

		this.materialNumber = materialNumber;

	}

	public String getChangedBy() {

		return changedBy;

	}

	public void setChangedBy(String changedBy) {

		this.changedBy = changedBy;

	}

	public String getMaterialType() {

		return materialType;

	}

	public void setMaterialType(String materialType) {

		this.materialType = materialType;

	}

	public String getIndustrySector() {

		return industrySector;

	}

	public void setIndustrySector(String industrySector) {

		this.industrySector = industrySector;

	}

	public String getEventType() {

		return eventType;

	}

	public void setEventType(String eventType) {

		this.eventType = eventType;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}