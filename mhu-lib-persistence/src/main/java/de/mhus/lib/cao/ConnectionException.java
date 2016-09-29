package de.mhus.lib.cao;

/**
 * <p>ConnectionException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ConnectionException extends CaoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for ConnectionException.</p>
	 */
	public ConnectionException() {
	}

	/**
	 * <p>Constructor for ConnectionException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for ConnectionException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ConnectionException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for ConnectionException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public ConnectionException(Throwable cause) {
		super(cause);
	}

}
