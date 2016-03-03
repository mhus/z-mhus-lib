package de.mhus.lib.adb.query;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ASubQuery class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ASubQuery extends APart {

	private AAttribute left;
	private AAttribute projection;
	private AQuery<?> subQuery;

	/**
	 * <p>Constructor for ASubQuery.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param projection a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param subQuery a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public ASubQuery(AAttribute left, AAttribute projection,
			AQuery<?> subQuery) {
		this.left = left;
		this.projection = projection;
		this.subQuery = subQuery;
	}

	/** {@inheritDoc} */
	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		String qualification = subQuery.toQualification(query.getManager()).trim();
		
		left.print(query, buffer);
		buffer.append(" IN (");
		
		StringBuffer buffer2 = new StringBuffer().append("DISTINCT ");
		projection.print(subQuery, buffer2);
		
		buffer.append(query.getManager().createSqlSelect(subQuery.getType(), buffer2.toString() , qualification));

		buffer.append(")");
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		projection.getAttributes(map);
		subQuery.getAttributes(map);
	}

}
