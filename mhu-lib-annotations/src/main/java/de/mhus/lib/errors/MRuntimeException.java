package de.mhus.lib.errors;

import java.util.UUID;

/**
 * <p>MRuntimeException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9157017165376262494L;
	private UUID errorId = UUID.randomUUID();
	
	/**
	 * <p>Constructor for MRuntimeException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public MRuntimeException(Object ... in) {
		super(MException.argToString(in),MException.argToCause(in));
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return errorId.toString() + " " + super.toString();
	}
	
	/**
	 * <p>getId.</p>
	 *
	 * @return a {@link java.util.UUID} object.
	 */
	public UUID getId() {
		return errorId;
	}
	
}
