package de.mhus.lib.adb.query;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.AttributeMap;

public class ASubQuery extends APart {

	private AAttribute left;
	private AAttribute projection;
	private AQuery<?> subQuery;

	public ASubQuery(AAttribute left, AAttribute projection,
			AQuery<?> subQuery) {
		this.left = left;
		this.projection = projection;
		this.subQuery = subQuery;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		String qualification = subQuery.toQualification(query.getManager()).trim();
		
		left.print(query, buffer);
		buffer.append(" in (SELECT DISTINCT ");
		projection.print(subQuery, buffer);
		buffer.append(" FROM $db.").append(query.getManager().getMappingName( subQuery.getType() )).append("$ ");
		
		if (MString.isSet(qualification)) {
			String low = qualification.trim().toLowerCase();
			if (low.startsWith("order"))
				buffer.append(qualification);
			else
				buffer.append("WHERE ").append(qualification);
		}
		buffer.append(")");
	}

	@Override
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		projection.getAttributes(map);
		subQuery.getAttributes(map);
	}

}
