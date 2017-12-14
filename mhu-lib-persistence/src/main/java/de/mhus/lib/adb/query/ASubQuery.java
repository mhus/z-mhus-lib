package de.mhus.lib.adb.query;

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
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		projection.getAttributes(map);
		subQuery.getAttributes(map);
	}

	public AAttribute getLeft() {
		return left;
	}

	public AAttribute getProjection() {
		return projection;
	}

	public AQuery<?> getSubQuery() {
		return subQuery;
	}

}
