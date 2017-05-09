package de.mhus.lib.core;

public class MMath {


   public static final int ROTATE_LEFT = 1;
   public static final int ROTATE_RIGHT = 2;
   
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

	public static int calcMask(int bitstorotate, int direction) {
		int mask = 0;
		int c;

		if (bitstorotate == 0)
			return 0;

		c = 0x80000000;
		mask = (c >> bitstorotate);
		if (direction == ROTATE_RIGHT) {
			mask = (c >> (32 - bitstorotate));
			mask = ~mask;
		} else
			mask = (c >> bitstorotate);

		return mask;
	}

	private static int rotr(int value, int bitstorotate, int sizet) {
		int tmprslt = 0;
		int mask = 0;
		int target = 0;

		bitstorotate %= sizet;
		target = value;

		// determine which bits will be impacted by the rotate
		mask = calcMask(bitstorotate, ROTATE_RIGHT);

		// save off the bits which will be impacted
		tmprslt = value & mask;

		// perform the actual rotate right
		target = (value >>> bitstorotate);

		// now rotate the saved off bits so they are in the proper place
		tmprslt <<= (sizet - bitstorotate);

		// now add the saved off bits
		target |= tmprslt;

		// and return the result
		return target;
	}

	private static int rotl(int value, int bitstorotate, int sizet) {
		int tmprslt = 0;
		int mask = 0;
		int target = 0;

		bitstorotate %= sizet;

		// determine which bits will be impacted by the rotate
		mask = calcMask(bitstorotate, ROTATE_LEFT);
		// shift the mask into the correct place (i.e. if we are delaying with a
		// byte rotate, we
		// need to ensure we have the mask setup for a byte or 8 bits)
		mask >>>= (32 - sizet);

		// save off the affected bits
		tmprslt = value & mask;

		// perform the actual rotate
		target = (value << bitstorotate);

		// now shift the saved off bits
		tmprslt >>>= (sizet - bitstorotate);

		// add the rotated bits back in (in the proper location)
		target |= tmprslt;

		// now return the result
		return target;
	}

	public static int rotr(int value, int bitstorotate) {
		return (rotr(value, bitstorotate, 32));
	}

	public static short rotr(short value, int bitstorotate) {
		return (short) rotr((0x0000ffff & value), bitstorotate, 16);
	}

	public static byte rotr(byte value, int bitstorotate) {
		return (byte) rotr((0x000000ff & value), bitstorotate, 8);
	}

	public static int rotl(int value, int bitstorotate) {
		return (rotl(value, bitstorotate, 32));
	}

	public static short rotl(short value, int bitstorotate) {
		return (short) rotl((0x0000ffff & value), bitstorotate, 16);
	}

	public static byte rotl(byte value, int bitstorotate) {
		return (byte) rotl((0x000000ff & value), bitstorotate, 8);
	}
}
