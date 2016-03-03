package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ALimit class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ALimit extends AOperation {

	private int limit;
	private int offset;

	/**
	 * <p>Constructor for ALimit.</p>
	 *
	 * @param limit a int.
	 */
	public ALimit(int limit) {
		this(0,limit);
	}
	
	/**
	 * <p>Constructor for ALimit.</p>
	 *
	 * @param offset a int.
	 * @param limit a int.
	 * @since 3.2.9
	 */
	public ALimit(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		buffer.append(" LIMIT ").append(offset).append(",").append(limit); //mysql specific !!
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
	}

}
