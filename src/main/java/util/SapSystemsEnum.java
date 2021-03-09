package util;

public enum SapSystemsEnum {

	ESTRIPES("EUP"), NASTRIPES("AMP"), SEASTRIPES("APP"), IPES("G3P"), NAPES("S8P"), FRONTIER("XTP"), GEMS("G9P"),
	CARS("F2P");

	private String systemId;

	SapSystemsEnum(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemId() {
		return systemId;
	}

}
