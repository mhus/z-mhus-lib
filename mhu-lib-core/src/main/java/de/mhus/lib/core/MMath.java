package de.mhus.lib.core;

/**
 * <p>MMath class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MMath {

	/**
	 * Pack the number in a base 36 numerical system. This means the characters 0-9 A-Z are used. This is the maximum
	 * packed system if you want to use case insensitive strings.
	 *
	 * @param value The value to convert
	 * @param digits number of minimum digits (expect the minus character if the value is negative)
	 * @return a {@link java.lang.String} object.
	 */
	public static String toBasis36(long value, int digits) {
		StringBuffer sb = new StringBuffer();
		
		boolean minus = value < 0;
		if (minus) value = -value;
		
		while (value > 0) {
			long nr = value % 36;
			value = value / 36;
			char c = 0;
			if (nr < 10)
				c = (char) ('0' + nr);
			else
				c = (char) ('A' + nr - 10);
			sb.insert(0,  c);
		}
		while (sb.length() < digits)
			sb.insert(0, '0');
		if (minus) sb.insert(0, '-');
		return sb.toString();
	}

	/**
	 * Pack the number in a base 36 numerical system. This means the characters 0-9 A-Z are used. This is the maximum
	 * packed system if you want to use case insensitive strings. This function creates ids specially for logger traces.
	 *
	 * @param value The value to convert
	 * @param ident additional ident at the right side, has 4 digits
	 * @param digits number of minimum digits (expect the minus character if the value is negative)
	 * @return a {@link java.lang.String} object.
	 */
	public static String toBasis36WithIdent(long value, long ident, int digits) {
		StringBuffer sb = new StringBuffer();
		
		if (ident < 0) ident = -ident;
		while (ident > 0) {
			long nr = ident % 36;
			ident = ident / 36;
			char c = 0;
			if (nr < 10)
				c = (char) ('0' + nr);
			else
				c = (char) ('A' + nr - 10);
			sb.insert(0,  c);
		}

		while (sb.length() < 4)
			sb.insert(0, '0');

		boolean minus = value < 0;
		if (minus) value = -value;
		
		while (value > 0) {
			long nr = value % 36;
			value = value / 36;
			char c = 0;
			if (nr < 10)
				c = (char) ('0' + nr);
			else
				c = (char) ('A' + nr - 10);
			sb.insert(0,  c);
		}
		
		while (sb.length() < digits)
			sb.insert(0, '0');
		if (minus) sb.insert(0, '-');
		return sb.toString();
	}

}
