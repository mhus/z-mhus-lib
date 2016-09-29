package de.mhus.lib.core.util;

/**
 * <p>Bits class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Bits {

	Bits() {
	}

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a boolean.
	 */
	public static boolean getBoolean(byte abyte0[], int i) {
		return abyte0[i] != 0;
	}

	/**
	 * <p>getChar.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a char.
	 */
	public static char getChar(byte abyte0[], int i) {
		return (char) (((abyte0[i + 1] & 255) << 0) + (abyte0[i + 0] << 8));
	}

	/**
	 * <p>getShort.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a short.
	 */
	public static short getShort(byte abyte0[], int i) {
		return (short) (((abyte0[i + 1] & 255) << 0) + (abyte0[i + 0] << 8));
	}

	/**
	 * <p>getInt.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a int.
	 */
	public static int getInt(byte abyte0[], int i) {
		return ((abyte0[i + 3] & 255) << 0) + ((abyte0[i + 2] & 255) << 8)
				+ ((abyte0[i + 1] & 255) << 16) + (abyte0[i + 0] << 24);
	}

	/**
	 * <p>getFloat.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a float.
	 */
	public static float getFloat(byte abyte0[], int i) {
		int j = ((abyte0[i + 3] & 255) << 0) + ((abyte0[i + 2] & 255) << 8)
				+ ((abyte0[i + 1] & 255) << 16) + (abyte0[i + 0] << 24);
		return Float.intBitsToFloat(j);
	}

	/**
	 * <p>getLong.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a long.
	 */
	public static long getLong(byte abyte0[], int i) {
		return (((long) abyte0[i + 7] & 255L) << 0)
				+ (((long) abyte0[i + 6] & 255L) << 8)
				+ (((long) abyte0[i + 5] & 255L) << 16)
				+ (((long) abyte0[i + 4] & 255L) << 24)
				+ (((long) abyte0[i + 3] & 255L) << 32)
				+ (((long) abyte0[i + 2] & 255L) << 40)
				+ (((long) abyte0[i + 1] & 255L) << 48)
				+ ((long) abyte0[i + 0] << 56);
	}

	/**
	 * <p>getDouble.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @return a double.
	 */
	public static double getDouble(byte abyte0[], int i) {
		long l = (((long) abyte0[i + 7] & 255L) << 0)
				+ (((long) abyte0[i + 6] & 255L) << 8)
				+ (((long) abyte0[i + 5] & 255L) << 16)
				+ (((long) abyte0[i + 4] & 255L) << 24)
				+ (((long) abyte0[i + 3] & 255L) << 32)
				+ (((long) abyte0[i + 2] & 255L) << 40)
				+ (((long) abyte0[i + 1] & 255L) << 48)
				+ ((long) abyte0[i + 0] << 56);
		return Double.longBitsToDouble(l);
	}

	/**
	 * <p>putBoolean.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param flag a boolean.
	 */
	public static void putBoolean(byte abyte0[], int i, boolean flag) {
		abyte0[i] = (byte) (flag ? 1 : 0);
	}

	/**
	 * <p>putChar.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param c a char.
	 */
	public static void putChar(byte abyte0[], int i, char c) {
		abyte0[i + 1] = (byte) (c >>> 0);
		abyte0[i + 0] = (byte) (c >>> 8);
	}

	/**
	 * <p>putShort.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param word0 a short.
	 */
	public static void putShort(byte abyte0[], int i, short word0) {
		abyte0[i + 1] = (byte) (word0 >>> 0);
		abyte0[i + 0] = (byte) (word0 >>> 8);
	}

	/**
	 * <p>putInt.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param j a int.
	 */
	public static void putInt(byte abyte0[], int i, int j) {
		abyte0[i + 3] = (byte) (j >>> 0);
		abyte0[i + 2] = (byte) (j >>> 8);
		abyte0[i + 1] = (byte) (j >>> 16);
		abyte0[i + 0] = (byte) (j >>> 24);
	}

	/**
	 * <p>putFloat.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param f a float.
	 */
	public static void putFloat(byte abyte0[], int i, float f) {
		int j = Float.floatToIntBits(f);
		abyte0[i + 3] = (byte) (j >>> 0);
		abyte0[i + 2] = (byte) (j >>> 8);
		abyte0[i + 1] = (byte) (j >>> 16);
		abyte0[i + 0] = (byte) (j >>> 24);
	}

	/**
	 * <p>putLong.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param l a long.
	 */
	public static void putLong(byte abyte0[], int i, long l) {
		abyte0[i + 7] = (byte) (int) (l >>> 0);
		abyte0[i + 6] = (byte) (int) (l >>> 8);
		abyte0[i + 5] = (byte) (int) (l >>> 16);
		abyte0[i + 4] = (byte) (int) (l >>> 24);
		abyte0[i + 3] = (byte) (int) (l >>> 32);
		abyte0[i + 2] = (byte) (int) (l >>> 40);
		abyte0[i + 1] = (byte) (int) (l >>> 48);
		abyte0[i + 0] = (byte) (int) (l >>> 56);
	}

	/**
	 * <p>putDouble.</p>
	 *
	 * @param abyte0 an array of byte.
	 * @param i a int.
	 * @param d a double.
	 */
	public static void putDouble(byte abyte0[], int i, double d) {
		long l = Double.doubleToLongBits(d);
		abyte0[i + 7] = (byte) (int) (l >>> 0);
		abyte0[i + 6] = (byte) (int) (l >>> 8);
		abyte0[i + 5] = (byte) (int) (l >>> 16);
		abyte0[i + 4] = (byte) (int) (l >>> 24);
		abyte0[i + 3] = (byte) (int) (l >>> 32);
		abyte0[i + 2] = (byte) (int) (l >>> 40);
		abyte0[i + 1] = (byte) (int) (l >>> 48);
		abyte0[i + 0] = (byte) (int) (l >>> 56);
	}
}
