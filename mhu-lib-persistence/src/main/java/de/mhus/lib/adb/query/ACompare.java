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
	public void print(AQuery<?> query, StringBuffer buffer) {
		left.print(query, buffer);
		switch (eq) {
		case EG:
			buffer.append(" => ");
			break;
		case EL:
			buffer.append(" <= ");
			break;
		case EQ:
			buffer.append(" = ");
			break;
		case GT:
			buffer.append(" > ");
			break;
		case GE:
			buffer.append(" >= ");
			break;
		case LIKE:
			buffer.append(" like ");
			break;
		case LT:
			buffer.append(" < ");
			break;
		case LE:
			buffer.append(" <= ");
			break;
		case NE:
			buffer.append(" <> ");
			break;
		case IN:
			buffer.append(" in ");
			break;

		}
		right.print(query, buffer);
	}

	@Override
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		right.getAttributes(map);
	};

}
