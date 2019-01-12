package io.vilya.maia.core.constant;

/**
 * 
 * @author erkea <erkea@vilya.io>
 *
 */
public enum HttpStatusCode {

	NO_CONTENT(204, "No Content"),
	
	BAD_REQUEST(400, "Bad Request"),
	
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	
	NOT_IMPLEMENTED(501, "Not Implemented");
	
	private int code;
	
	private String reason;
	
	HttpStatusCode(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	public int getCode() {
		return code;
	}

	public String getReason() {
		return reason;
	}
	
}
