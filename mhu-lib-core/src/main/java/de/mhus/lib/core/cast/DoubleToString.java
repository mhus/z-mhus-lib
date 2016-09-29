package de.mhus.lib.core.cast;

import java.text.DecimalFormat;

/**
 * <p>DoubleToString class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DoubleToString implements Caster<Double, String> {

	private static DecimalFormat doubleFormat = new DecimalFormat("0.##########");

	/** {@inheritDoc} */
	@Override
	public Class<? extends String> getToClass() {
		return String.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Double> getFromClass() {
		return Double.class;
	}

	/** {@inheritDoc} */
	@Override
	public String cast(Double in, String def) {
		return null;
	}

	/**
	 * <p>toString.</p>
	 *
	 * @param _in a double.
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(double _in) {
		String out = doubleFormat.format(_in);
		if (out.indexOf(',') >= 0) out = out.replace(",", "."); // for secure
		return out;
	}
	
}
