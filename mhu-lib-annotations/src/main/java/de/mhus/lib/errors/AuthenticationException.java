package de.mhus.lib.errors;

/**
 * <p>AuthenticationException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for AuthenticationException.</p>
	 */
	public AuthenticationException() {
		super();
	}

	/**
	 * <p>Constructor for AuthenticationException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 * @param enableSuppression a boolean.
	 * @param writableStackTrace a boolean.
	 */
	public AuthenticationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * <p>Constructor for AuthenticationException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for AuthenticationException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public AuthenticationException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for AuthenticationException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public AuthenticationException(Throwable cause) {
		super(cause);
	}

}
