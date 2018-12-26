package io.vilya.maia.ip.constant;

/**
 * 
 * @author vilya
 *
 */
public enum HttpStatusCode {

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
