package io.vilya.maia.ip;

/**
 * 
 * @author vilya
 *
 */
public enum MediaType {

	APPLICATION_XML("application/xml;charset=utf-8", "application", "xml"),
	
	TEXT_XML("text/xml;charset=utf-8", "text", "xml"),
	
	APPLICATION_JSON("application/json;charset=utf-8", "application", "json"),
	
	TEXT_HTML("text/html;charset=utf-8", "text", "html");
	
	private String full;
	
	private String type;
	
	private String subType;
	
	MediaType(String full, String type, String subType) {
		this.full = full;
		this.type = type;
		this.subType = subType;
	}

	public String getFull() {
		return full;
	}

	public String getType() {
		return type;
	}
	
	public String getSubType() {
		return subType;
	}
}
