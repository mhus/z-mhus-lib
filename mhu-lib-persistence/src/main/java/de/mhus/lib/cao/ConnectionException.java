package de.mhus.lib.cao;

public class ConnectionException extends CaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionException() {
	}

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(Throwable cause) {
		super(cause);
	}

}
