package de.mhus.lib.cao;

@SuppressWarnings("serial")
public class ConnectionException extends CaoException {

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
