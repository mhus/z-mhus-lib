package de.mhus.lib.core;

public class MMath {

	/**
	 * Pack the number in a base 36 numerical system. This means the characters 0-9 A-Z are used. This is the maximum
	 * packed system if you want to use case insensitive strings.
	 * @param value The value to convert
	 * @param digits number of minimum digits (expect the minus character if the value is negative)
	 * @return
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
	 * @return
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

	public static double truncateDecimals(double d, int len) {
		long p = pow(10, len);
		long l = (long)(d * p);
		return (double)l / (double)p;
	}

	public static long pow(long a, int b) {
		long result = 1;
		for (int i = 1; i <= b; i++) {
		   result *= a;
		}
		return result;
	}
}
