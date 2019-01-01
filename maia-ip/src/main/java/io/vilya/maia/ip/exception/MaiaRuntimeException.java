/**
 * 
 */
package io.vilya.maia.ip.exception;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class MaiaRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6813613787586151223L;

	public MaiaRuntimeException(Throwable cause) {
		super(cause);
	}

	public MaiaRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaiaRuntimeException(String message) {
		super(message);
	}
	
}
