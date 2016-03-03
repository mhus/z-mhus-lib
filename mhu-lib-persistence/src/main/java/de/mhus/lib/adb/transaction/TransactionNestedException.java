package de.mhus.lib.adb.transaction;

/**
 * <p>TransactionNestedException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TransactionNestedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for TransactionNestedException.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	public TransactionNestedException(String msg) {
		super(msg);
	}

}
