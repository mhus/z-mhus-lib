package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

public class ACompare extends APart {

	private TYPE eq;
	private AAttribute left;
	private AAttribute right;

	public ACompare(TYPE eq, AAttribute left, AAttribute right) {
		this.eq = eq;
		this.left = left;
		this.right = right;
	}

	public enum TYPE {EQ,LT,GT,EL, EG, LIKE, NE, IN, LE, GE}

	@Override
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		right.getAttributes(map);
	}

	public TYPE getEq() {
		return eq;
	}

	public AAttribute getLeft() {
		return left;
	}

	public AAttribute getRight() {
		return right;
	};

}
