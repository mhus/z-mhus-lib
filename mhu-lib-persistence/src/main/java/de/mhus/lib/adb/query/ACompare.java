package de.mhus.lib.adb.query;

import de.mhus.lib.core.parser.AttributeMap;

/**
 * <p>ACompare class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ACompare extends APart {

	private TYPE eq;
	private AAttribute left;
	private AAttribute right;

	/**
	 * <p>Constructor for ACompare.</p>
	 *
	 * @param eq a {@link de.mhus.lib.adb.query.ACompare.TYPE} object.
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 */
	public ACompare(TYPE eq, AAttribute left, AAttribute right) {
		this.eq = eq;
		this.left = left;
		this.right = right;
	}

	public enum TYPE {EQ,LT,GT,EL, EG, LIKE, NE, IN, LE, GE}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		left.getAttributes(map);
		right.getAttributes(map);
	};

}
