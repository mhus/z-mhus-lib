package de.mhus.lib.core.cast;

/**
 * <p>FloatToString class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FloatToString implements Caster<Float, String> {

	/** {@inheritDoc} */
	@Override
	public Class<? extends String> getToClass() {
		return String.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Float> getFromClass() {
		return Float.class;
	}

	/** {@inheritDoc} */
	@Override
	public String cast(Float in, String def) {
		return toString(in);
	}

	/**
	 * <p>toString.</p>
	 *
	 * @param _in a float.
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(float _in) {
		return Float.toString(_in).replace(",", ".");
	}
	
}
